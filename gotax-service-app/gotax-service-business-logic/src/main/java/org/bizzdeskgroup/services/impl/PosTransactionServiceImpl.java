package org.bizzdeskgroup.services.impl;

import org.apache.ibatis.session.SqlSession;
import org.bizzdeskgroup.Dtos.Command.CreatePosTransactionDto;
import org.bizzdeskgroup.Dtos.Command.GenerateRemittanceDto;
import org.bizzdeskgroup.Dtos.Command.UpdateTransactionRem;
import org.bizzdeskgroup.Dtos.Query.*;
import org.bizzdeskgroup.factory.MyBatisConnectionFactory;
import org.bizzdeskgroup.models.*;
import org.bizzdeskgroup.services.PosTransactionService;
import org.joda.time.DateTime;

import java.sql.Timestamp;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static org.bizzdeskgroup.Helpers.NotificationMixer.*;
import static org.bizzdeskgroup.Mappers.PosMapper.NewPosTransactionItem;
import static org.bizzdeskgroup.Mappers.TransactionMapper.SingleRemittanceMap;
import static org.eclipse.jetty.util.IO.close;

public class PosTransactionServiceImpl implements PosTransactionService {

    @Override
    public PosTransactionResponseDto NewPosTransaction(int CreatedBy, CreatePosTransactionDto posTransactionDto, int project) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try{
            Pos pos = ConfirmPos(posTransactionDto.posId);
//            Pos pos = ConfirmPos(posTransactionDto.posId);
//            if (pos == null) throw new Exception("unknown device");
//            if (!pos.isActive) throw new Exception("device not authorized");

            User user = session.selectOne("User.getUserById", CreatedBy);
            if (user == null) throw new Exception("operator does not exist");
            if (!user.isActive) throw new Exception("operator not authorized");

            //check pending remittance
            RemittanceDto pendingRem = session.selectOne("Remittance.CheckRemittancesPending", CreatedBy);
            if (pendingRem != null) throw new Exception("Remittance no "+ pendingRem.getRemittanceNo()+" with amount NGN"+pendingRem.getTotalAmount()+" is yet to be paid");

//            if(posTransactionDto.isAssessment) {
//                Assessment assessed = session.selectOne("Assessment.singleAssess", posTransactionDto.assessmentId);
//                if (assessed == null) throw new Exception("assessment does not exist");
//            }

            MdaService mdaService = session.selectOne("MdaService.getMdaServiceById", posTransactionDto.mdaServiceId);
            if (mdaService == null) throw new Exception("Service does not exist");

            Mda mda = session.selectOne("Mda.getMdaById", mdaService.mdaId);
            if (mda == null) throw new Exception("mda does not exist");

            String transId = RandomNumbersString(10, 0, 15);
            Timestamp transDate = DateTime();
            String transStatus = "Success";

            PosTransaction newPosTransaction = NewPosTransactionItem(posTransactionDto);
            newPosTransaction.setCreatedBy(CreatedBy);
            newPosTransaction.setOperatorId(CreatedBy);
            newPosTransaction.setTransactionId(transId);
            newPosTransaction.setCreatedDate(transDate);
            newPosTransaction.setTransactionStatus(transStatus);
            newPosTransaction.setMdaId(mda.id);
            newPosTransaction.setBusinessId(project);
//            newPosTransaction.setTransactionDate(ParseDate(posTransactionDto.TransactionDate.toString()));yyyy-mm-dd hh:mm:ss[.fffffffff]
//            newPosTransaction.setTransactionDate(Timestamp.valueOf(String.valueOf(posTransactionDto.TransactionDate)));
            newPosTransaction.setTransactionDate(new Timestamp(posTransactionDto.transactionDate.getTime()));


            Invoice inv = session.selectOne("Invoice.invoicesByNoX", newPosTransaction.invoiceNo);
            if(newPosTransaction.amountPaid != inv.getInvoiceAmount()) throw new Exception("Wrong invoice Amount");
            session.insert("PosTransaction.insert", newPosTransaction);

            double paidInvSum = 0.0;
            double assessAmnt = 0.0;
//                Remittance rem = session.selectOne("Remittance.RemittancesByRef", transaction.transactionReference);
            if(inv != null && !inv.isPaid()){

////                if(inv.getAssessmentId() > 0 posTransactionDto.isAssessment) {
//                if(inv.getAssessmentId() > 0) {
//                    Assessment assessed = session.selectOne("Assessment.singleAssess", posTransactionDto.assessmentId);
//                    if (assessed == null) throw new Exception("assessment does not exist");
//                }
                    System.out.println(inv.getId());
                    System.out.println(inv.getInvoiceAmount());
                inv.setPaid(true);
                inv.setUpdatedBy(CreatedBy);
                inv.setUpdatedDate(transDate);
                inv.setPaymentChannel("Cash");
                session.update("Invoice.update", inv);

                System.out.println(inv.getAssessmentId());


                paidInvSum = inv.getAssessmentId() > 0 ? session.selectOne("Invoice.singleAssessPaidSum", inv.getAssessmentId()) : 0;
                Assessment assessment = inv.getAssessmentId() > 0 ? session.selectOne("Assessment.singleAssess", inv.getAssessmentId()) : null;

                if (assessment != null) assessAmnt = assessment.taxAmount;
//

                Thread object
                        = new Thread(new UpdateInvoiceAssessment(inv.getAssessmentId()));
                object.start();
            }

            PosTransactionResponseDto success = new PosTransactionResponseDto();

            success.posDeviceSerial = pos.posDeviceSerial;
            success.operator = user.firstName + " " + user.lastName;
            success.customerName = posTransactionDto.customerName;
            success.customerPhone = posTransactionDto.customerPhone;
            success.customerEmail = posTransactionDto.customerEmail;
            success.assessmentInvoice = posTransactionDto.invoiceNo;
            success.mda = mda.name;
            success.mdaService = mdaService.name;
            success.transactionId = transId;
            success.transactionStatus = transStatus;
            success.transactionDate = posTransactionDto.transactionDate.toString();
            success.createdDate = transDate.toString();
            success.amountPaid = posTransactionDto.amountPaid;
            success.pendingBalance = assessAmnt - paidInvSum ;
//                ase.balance = ase.taxAmount - ase.amountPaid;
//                ase.isSettled = ase.taxAmount <= ase.amountPaid;

            return  success;

        } catch (NullPointerException e){
            return null;
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }  finally {
            session.commit();
            session.close();
            close(session);
        }
    }

    @Override
    public PosTransactionResponseDto ReversePosTransaction(int CreatedBy, CreatePosTransactionDto posTransactionDto) {
        return null;
    }

    @Override
    public PaginatedDto AgentPosTransactions(int businessId, int MdaId, int agentId, int payerId, String sortBy, int pageNo, int pageSize, Date startDate, Date endDate) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            int startFrom = 0;
            if (pageNo > 0 && pageSize > 0) {
                startFrom = (pageNo - 1) * pageSize;
            } else {
                pageSize = 0;
            }

            PageFinder posFind = new PageFinder();
            posFind.from = startFrom;
            posFind.recordsPerPage = pageSize;
            posFind.orderBy = sortBy;
            posFind.agentId = agentId;
            posFind.mdaId = MdaId;
            posFind.payerId = payerId;
            posFind.businessId = businessId;
            posFind.startTransactionDate = new Timestamp(startDate.getTime());
            posFind.endTransactionDate = new Timestamp(endDate.getTime());

            List<PosTransactionDto> pos = session.selectList("PosTransaction.AgentTransaction", posFind);
            PaginatedDto paged = new PaginatedDto();
            paged.setStatus(200);
            if (startFrom == 0) {
                startFrom = 1;
            }
//            int total = session.selectOne("Mda.getCountMda");
            int total = 0;
            double sum = 0;
            int totalFound = 0;
            if(pos.size() > 0) {
                total = session.selectOne("PosTransaction.getCountAgent", posFind);
                sum = session.selectOne("PosTransaction.getSumAgent", posFind);
                totalFound = pos.size();
            }

            paged.setTotal(total);
            paged.setData(pos);
            paged.setFrom(startFrom);
            paged.setSum(sum);

            if(pageSize > 0) {
                if (total > pageSize) {
                    long remainder = total % pageSize;
                    int dividend = (total / pageSize);
                    if (remainder < pageSize) {
                        paged.setLast_page(dividend + 1);
                    } else {
                        paged.setLast_page(dividend);
                    }
                } else {
                    paged.setLast_page(1);
                }

                paged.setPer_page(pageSize);
                if (startFrom == 1) {
                    paged.setTo(totalFound);
                } else {
                    paged.setTo(startFrom + totalFound);
                    paged.setFrom(startFrom + 1);
                }
            }

            return paged;

        } catch (NullPointerException e){
            return null;
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }  finally {
            session.commit();
            session.close();
            close(session);
        }
    }

    @Override
    public PaginatedDto MdaPosTransactions(int mdaId, String sortBy, int pageNo, int pageSize, Date startDate, Date endDate) throws Exception {

        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            int startFrom = 0;
            if (pageNo > 0 && pageSize > 0) {
                startFrom = (pageNo - 1) * pageSize;
            } else {
                pageSize = 0;
            }

            PageFinder posFind = new PageFinder();
            posFind.from = startFrom;
            posFind.recordsPerPage = pageSize;
            posFind.orderBy = sortBy;
            posFind.mdaId = mdaId;
            posFind.startTransactionDate = new Timestamp(startDate.getTime());
            posFind.endTransactionDate = new Timestamp(endDate.getTime());
//            posFind.agentId = AgentId;

            List<PosTransactionDto> pos = session.selectList("PosTransaction.AllTransactionMda", posFind);
            PaginatedDto paged = new PaginatedDto();
            paged.setStatus(200);
            if (startFrom == 0) {
                startFrom = 1;
            }

            int total = 0;
            double sum = 0;
            int totalFound = 0;
            if(pos.size() > 0) {
                total = session.selectOne("PosTransaction.getCountMda", posFind);
                sum = session.selectOne("PosTransaction.getSumMda", posFind);
                totalFound = pos.size();
            }

            paged.setTotal(total);
            paged.setData(pos);
            paged.setSum(sum);
            paged.setFrom(startFrom);

            if(pageSize > 0) {
                if (total > pageSize) {
                    long remainder = total % pageSize;
                    int dividend = (total / pageSize);
                    if (remainder < pageSize) {
                        paged.setLast_page(dividend + 1);
                    } else {
                        paged.setLast_page(dividend);
                    }
                } else {
                    paged.setLast_page(1);
                }

                paged.setPer_page(pageSize);
                if (startFrom == 1) {
                    paged.setTo(totalFound);
                } else {
                    paged.setTo(startFrom + totalFound);
                    paged.setFrom(startFrom + 1);
                }
            }
            return paged;

        } catch (NullPointerException e){
            return null;
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }  finally {
            session.commit();
            session.close();
            close(session);
        }
    }

    @Override
    public PaginatedDto ProjectPosTransactions(int businessId, String sortBy, int pageNo, int pageSize, Date startDate, Date endDate) throws Exception {

        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            int startFrom = 0;
            if (pageNo > 0 && pageSize > 0) {
                startFrom = (pageNo - 1) * pageSize;
            } else {
                pageSize = 0;
            }

            PageFinder posFind = new PageFinder();
            posFind.from = startFrom;
            posFind.recordsPerPage = pageSize;
            posFind.orderBy = sortBy;
            posFind.businessId = businessId;
            posFind.startTransactionDate = new Timestamp(startDate.getTime());
            posFind.endTransactionDate = new Timestamp(endDate.getTime());
//            posFind.agentId = AgentId;

            List<PosTransactionDto> pos = session.selectList("PosTransaction.AllTransactionProject", posFind);
            PaginatedDto paged = new PaginatedDto();
            paged.setStatus(200);
            if (startFrom == 0) {
                startFrom = 1;
            }
//            int total = session.selectOne("Mda.getCountMda");
            int total = 0;
            double sum = 0;
            int totalFound = 0;
            if(pos.size() > 0) {
                total = session.selectOne("PosTransaction.getCountProject", posFind);
                sum = session.selectOne("PosTransaction.getSumProject", posFind);
                totalFound = pos.size();
            }

            paged.setTotal(total);
            paged.setData(pos);
            paged.setSum(sum);
            paged.setFrom(startFrom);

            if(pageSize > 0) {
                if (total > pageSize) {
                    long remainder = total % pageSize;
                    int dividend = (total / pageSize);
                    if (remainder < pageSize) {
                        paged.setLast_page(dividend + 1);
                    } else {
                        paged.setLast_page(dividend);
                    }
                } else {
                    paged.setLast_page(1);
                }

                paged.setPer_page(pageSize);
                if (startFrom == 1) {
                    paged.setTo(totalFound);
                } else {
                    paged.setTo(startFrom + totalFound);
                    paged.setFrom(startFrom + 1);
                }
            }
            return paged;

        } catch (NullPointerException e){
            return null;
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }  finally {
            session.commit();
            session.close();
            close(session);
        }
    }


    @Override
    public PaginatedDto AllPosTransactions(String sortBy, int pageNo, int pageSize, Date startDate, Date endDate) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            int startFrom = 0;
            if (pageNo > 0 && pageSize > 0) {
                startFrom = (pageNo - 1) * pageSize;
            } else {
                pageSize = 0;
            }

            PageFinder posFind = new PageFinder();
            posFind.from = startFrom;
            posFind.recordsPerPage = pageSize;
            posFind.orderBy = sortBy;
            posFind.startTransactionDate = new Timestamp(startDate.getTime());
            posFind.endTransactionDate = new Timestamp(endDate.getTime());
//            posFind.mdaId = mdaId;
//            posFind.agentId = AgentId;

            List<PosTransactionDto> pos = session.selectList("PosTransaction.AllTransaction", posFind);
            PaginatedDto paged = new PaginatedDto();
            paged.setStatus(200);
            if (startFrom == 0) {
                startFrom = 1;
            }
//            int total = session.selectOne("Mda.getCountMda");
            int total = 0;
            double sum = 0;
            int totalFound = 0;
            if(pos.size() > 0) {
                total = session.selectOne("PosTransaction.getCount", posFind);
                sum = session.selectOne("PosTransaction.getSum", posFind);
                totalFound = pos.size();
            }

            paged.setTotal(total);
            paged.setData(pos);
            paged.setSum(sum);
            paged.setFrom(startFrom);

            if(pageSize > 0) {
                if (total > pageSize) {
                    long remainder = total % pageSize;
                    int dividend = (total / pageSize);
                    if (remainder < pageSize) {
                        paged.setLast_page(dividend + 1);
                    } else {
                        paged.setLast_page(dividend);
                    }
                } else {
                    paged.setLast_page(1);
                }

                paged.setPer_page(pageSize);
                if (startFrom == 1) {
                    paged.setTo(totalFound);
                } else {
                    paged.setTo(startFrom + totalFound);
                    paged.setFrom(startFrom + 1);
                }
            }
            return paged;

        } catch (NullPointerException e){
            return null;
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }  finally {
            session.commit();
            session.close();
            close(session);
        }
    }

    @Override
    public PosTransactionDto SingleAgentPosTransaction(int MdaId, String TransactionId) {
        PosTransaction ps = new PosTransaction();
        ps.mdaId = MdaId;
        ps.transactionId = TransactionId;
//        ps.offlineTransactionId = TransactionId;
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            return session.selectOne("PosTransaction.singleAgentTransaction", ps);

        } finally {
            session.commit();
            session.close();
            close(session);
        }
    }

    @Override
    public PosTransactionDto SinglePosTransaction(String transactionId) throws Exception {

        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            return session.selectOne("PosTransaction.singleTransaction", transactionId);


        } catch (NullPointerException e){
            return null;
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }  finally {
            session.commit();
            session.close();
            close(session);
        }
    }

    @Override
    public List<PosTransactionDto> SearchAgentPosTransaction(int businessId, int MdaId, int operatorId, int payerId, String sortBy, String search) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {

            PosTransaction md = new PosTransaction();
            md.transactionId = "%"+search+"%";
            md.customerName = "%"+search+"%";
            md.customerEmail = "%"+search+"%";
            md.customerPhone = "%"+search+"%";
            md.offlineTransactionId = "%"+search+"%";
            md.mdaId = MdaId;
            md.customerId = payerId;
            md.businessId = businessId;
            md.operatorId = operatorId;

            //sort criteria
//            sortBy = sortBy.isEmpty() ? null : sortBy;
//            md.setSortBy(sortBy);
            return session.selectList("PosTransaction.SearchMdaTransaction", md);


        } catch (NullPointerException e){
            return null;
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }  finally {
            session.commit();
            session.close();
            close(session);
        }
    }

    @Override
    public List<PosTransactionDto> SearchPosTransaction(String sortBy, String search) throws Exception {

        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {


            PosTransaction md = new PosTransaction();
            md.transactionId = "%"+search+"%";
            md.customerName = "%"+search+"%";
            md.customerEmail = "%"+search+"%";
            md.customerPhone = "%"+search+"%";
            md.offlineTransactionId = "%"+search+"%";

            //sort criteria
//            sortBy = sortBy.isEmpty() ? null : sortBy;
//            md.setSortBy(sortBy);
            return session.selectList("PosTransaction.SearchTransaction", md);


        } catch (NullPointerException e){
            return null;
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }  finally {
            session.commit();
            session.close();
            close(session);
        }
    }

    @Override
    public RemittanceResponseDto NewRemittance(int CreatedBy, GenerateRemittanceDto posTransactionDto, int mdaId, int officeId) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try{
            Pos pos = session.selectOne("Pos.getPosById", posTransactionDto.posId);

            Mda mda = session.selectOne("Mda.getMdaById", posTransactionDto.mdaId);
            if (mda == null) {
                mda = session.selectOne("Mda.getMdaById", mdaId);
                if (mda == null) throw new Exception("mda does not exist");
            }

            if (pos == null) throw new Exception("unknown device");
            if (!pos.isActive) throw new Exception("device not authorized");

            User user = session.selectOne("User.getUserById", CreatedBy);
            if (user == null) throw new Exception("operator does not exist");
            if (!user.isActive) throw new Exception("operator not authorized");

            PageFinder pf = new PageFinder();
            pf.mdaId = posTransactionDto.mdaId;
            pf.agentId = CreatedBy;

            System.out.println(CreatedBy);
            System.out.println(posTransactionDto.mdaId);

            UpdateTransactionRem update = new UpdateTransactionRem();
            update.list = session.selectList("PosTransaction.AgentTransactionUnlimited", pf);
            if (update.list.size() == 0) throw new Exception("no pending collection");

//            double sum = pendingTransactions.stream().mapToInt(Double::intValue).sum();
            double pendingSum = 0.0;
            for (PosTransaction pendingItem : update.list) {
                pendingSum += pendingItem.amountPaid;
            }
            Random rand = new Random();
            int randomNumOdd = rand.nextInt(10);
            randomNumOdd = (randomNumOdd % 2 == 0) ? randomNumOdd + 1 : randomNumOdd;

            String remId = RandomNumbersString(9, 0, 4) + randomNumOdd + RandomNumbersString(9, 0, 9) ;

//            String remId = RandomNumbersString(10, 0, 15);
            Timestamp transDate = DateTime();
//            String transStatus = "Success";

            Remittance newRemittance = new Remittance();
            newRemittance.setCreatedBy(CreatedBy);
            newRemittance.setCreatedDate(transDate);
            newRemittance.setRemittanceNo(remId);
            newRemittance.setStatus(false);
            newRemittance.setTerminalId(posTransactionDto.posId);
            newRemittance.setUserId(CreatedBy);
            newRemittance.setTotalAmount(pendingSum);
            newRemittance.setMdaId(posTransactionDto.mdaId);
            newRemittance.setBusinessId(mda.getBusinessId());
            newRemittance.setMdaOfficeId(officeId);

            System.out.println(pendingSum);
            System.out.println(CreatedBy);
//            return null;

            session.insert("Remittance.insert", newRemittance);

            for (PosTransaction pendingRem : update.list) {
                pendingRem.setRemittanceId(newRemittance.getId());
                pendingRem.setUpdatedBy(CreatedBy);
                pendingRem.setUpdatedDate(DateTime());

                System.out.println("updating: "+ pendingRem.id);
                session.update("PosTransaction.updateRem", pendingRem);
            }

            RemittanceResponseDto success = new RemittanceResponseDto();

            success.posDeviceSerial = pos.posDeviceSerial;
            success.operator = user.firstName + " " + user.lastName;
            success.mda = mda.name;
            success.remittanceStatus = String.valueOf(false);
            success.remittanceNo = remId;
            success.amountGenerated = pendingSum;
            success.generatedDate = transDate.toString();

            return  success;

        } catch (NullPointerException e){
            return null;
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }  finally {
            session.commit();
            session.close();
            close(session);
        }
    }

    @Override
    public PaginatedDto AgentRemittance(int AgentId, int MdaOfficeId, String sortBy, int pageNo, int pageSize, Date startDate, Date endDate) {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            int startFrom = 0;
            if (pageNo > 0 && pageSize > 0) {
                startFrom = (pageNo - 1) * pageSize;
            } else {
                pageSize = 0;
            }

            PageFinder posFind = new PageFinder();
            posFind.from = startFrom;
            posFind.recordsPerPage = pageSize;
            posFind.orderBy = sortBy;
            posFind.agentId = AgentId;
            posFind.mdaId = MdaOfficeId;
            posFind.startTransactionDate = new Timestamp(startDate.getTime());
            posFind.endTransactionDate = new Timestamp(endDate.getTime());

            List<RemittanceDto> pos = session.selectList("Remittance.AllMdaAgentRemittancesPaged", posFind);
            PaginatedDto paged = new PaginatedDto();
            paged.setStatus(200);
            if (startFrom == 0) {
                startFrom = 1;
            }
//            int total = session.selectOne("Mda.getCountMda");
            int total = 0;
            double sum = 0;
            int totalFound = 0;
            if(pos.size() > 0) {
                total = session.selectOne("Remittance.getCountAgent", posFind);
                sum = session.selectOne("Remittance.getSumAgent", posFind);
                totalFound = pos.size();
            }

            paged.setTotal(total);
            paged.setData(pos);
            paged.setFrom(startFrom);
            paged.setSum(sum);

            if(pageSize > 0) {
                if (total > pageSize) {
                    long remainder = total % pageSize;
                    int dividend = (total / pageSize);
                    if (remainder < pageSize) {
                        paged.setLast_page(dividend + 1);
                    } else {
                        paged.setLast_page(dividend);
                    }
                } else {
                    paged.setLast_page(1);
                }

                paged.setPer_page(pageSize);
                if (startFrom == 1) {
                    paged.setTo(totalFound);
                } else {
                    paged.setTo(startFrom + totalFound);
                    paged.setFrom(startFrom + 1);
                }
            }
            return paged;
        } finally {
            session.commit();
            session.close();
            close(session);
        }
    }

    @Override
    public PaginatedDto MdaRemittance(int mdaId, String sortBy, int pageNo, int pageSize, boolean remStatus, boolean applyFilter, Date startDate, Date endDate) throws Exception {

        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            int startFrom = 0;
            if (pageNo > 0 && pageSize > 0) {
                startFrom = (pageNo - 1) * pageSize;
            } else {
                pageSize = 0;
            }

            PageFinder posFind = new PageFinder();
            posFind.from = startFrom;
            posFind.recordsPerPage = pageSize;
            posFind.orderBy = sortBy;
            posFind.mdaId = mdaId;
            posFind.status = remStatus;
            posFind.filter = applyFilter? 1 : 0;
            posFind.startTransactionDate = new Timestamp(startDate.getTime());
            posFind.endTransactionDate = new Timestamp(endDate.getTime());

//            List<PosTransactionDto> pos = session.selectList("Remittance.AllMdaRemittancesPaged", posFind);
            List<RemittanceDto> pos = session.selectList("Remittance.AllMdaRemittancesPaged", posFind);
            PaginatedDto paged = new PaginatedDto();
            paged.setStatus(200);
            if (startFrom == 0) {
                startFrom = 1;
            }
//            int total = session.selectOne("Mda.getCountMda");
            int total = 0;
            double sum = 0;
            int totalFound = 0;
            if(pos.size() > 0) {
                total = session.selectOne("Remittance.getCountMda", posFind);
                sum = session.selectOne("Remittance.getSumMda", posFind);
                totalFound = pos.size();
            }

            paged.setTotal(total);
            paged.setData(pos);
            paged.setFrom(startFrom);
            paged.setSum(sum);

            if(pageSize > 0) {
                if (total > pageSize) {
                    long remainder = total % pageSize;
                    int dividend = (total / pageSize);
                    if (remainder < pageSize) {
                        paged.setLast_page(dividend + 1);
                    } else {
                        paged.setLast_page(dividend);
                    }
                } else {
                    paged.setLast_page(1);
                }

                paged.setPer_page(pageSize);
                if (startFrom == 1) {
                    paged.setTo(totalFound);
                } else {
                    paged.setTo(startFrom + totalFound);
                    paged.setFrom(startFrom + 1);
                }

                System.out.println("total is: " + total);
            }
            return paged;

        } catch (NullPointerException e){
            return null;
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }  finally {
            session.commit();
            session.close();
            close(session);
        }
    }


    @Override
    public PaginatedDto ProjectRemittance(int mdaId, String sortBy, int pageNo, int pageSize, boolean remStatus, boolean applyFilter, Date startDate, Date endDate) throws Exception {

        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            int startFrom = 0;
            if (pageNo > 0 && pageSize > 0) {
                startFrom = (pageNo - 1) * pageSize;
            } else {
                pageSize = 0;
            }

            PageFinder posFind = new PageFinder();
            posFind.from = startFrom;
            posFind.recordsPerPage = pageSize;
            posFind.orderBy = sortBy;
            posFind.mdaId = mdaId;
            posFind.filter = applyFilter? 1 : 0;
            posFind.status = remStatus;
            posFind.startTransactionDate = new Timestamp(startDate.getTime());
            posFind.endTransactionDate = new Timestamp(endDate.getTime());

//            List<PosTransactionDto> pos = session.selectList("Remittance.AllMdaRemittancesPaged", posFind);
            List<RemittanceDto> pos = session.selectList("Remittance.AllProjectRemittancesPaged", posFind);
            PaginatedDto paged = new PaginatedDto();
            paged.setStatus(200);
            if (startFrom == 0) {
                startFrom = 1;
            }
//            int total = session.selectOne("Mda.getCountMda");
            int total = 0;
            double sum = 0;
            int totalFound = 0;
            if(pos.size() > 0) {
                total = session.selectOne("Remittance.getCountProject", posFind);
                sum = session.selectOne("Remittance.getSumProject", posFind);
                totalFound = pos.size();
            }

            paged.setTotal(total);
            paged.setData(pos);
            paged.setFrom(startFrom);
            paged.setSum(sum);

            if(pageSize > 0) {
                if (total > pageSize) {
                    long remainder = total % pageSize;
                    int dividend = (total / pageSize);
                    if (remainder < pageSize) {
                        paged.setLast_page(dividend + 1);
                    } else {
                        paged.setLast_page(dividend);
                    }
                } else {
                    paged.setLast_page(1);
                }

                paged.setPer_page(pageSize);
                if (startFrom == 1) {
                    paged.setTo(totalFound);
                } else {
                    paged.setTo(startFrom + totalFound);
                    paged.setFrom(startFrom + 1);
                }

                System.out.println("total is: " + total);
            }
            return paged;

        } catch (NullPointerException e){
            return null;
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }  finally {
            session.commit();
            session.close();
            close(session);
        }
    }


    @Override
    public PaginatedDto AllRemittance(String sortBy, int pageNo, int pageSize, boolean remStatus, boolean applyFilter, Date startDate, Date endDate) throws Exception {

        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            int startFrom = 0;
            if (pageNo > 0 && pageSize > 0) {
                startFrom = (pageNo - 1) * pageSize;
            } else {
                pageSize = 0;
            }

            PageFinder posFind = new PageFinder();
            posFind.from = startFrom;
            posFind.recordsPerPage = pageSize;
            posFind.orderBy = sortBy;
            posFind.filter = applyFilter? 1 : 0;
            posFind.status = remStatus;
            posFind.startTransactionDate = new Timestamp(startDate.getTime());
            posFind.endTransactionDate = new Timestamp(endDate.getTime());

            List<RemittanceDto> pos = session.selectList("Remittance.AllRemittancesPaged", posFind);
            PaginatedDto paged = new PaginatedDto();
            paged.setStatus(200);
            if (startFrom == 0) {
                startFrom = 1;
            }
//            int total = session.selectOne("Mda.getCountMda");
            int total = 0;
            double sum = 0;
            int totalFound = 0;
            if(pos.size() > 0) {
                total = session.selectOne("Remittance.getCount", posFind);
                sum = session.selectOne("Remittance.getSum", posFind);
                totalFound = pos.size();
            }

            paged.setTotal(total);
            paged.setData(pos);
            paged.setFrom(startFrom);
            paged.setSum(sum);

            if(pageSize > 0) {
                if (total > pageSize) {
                    long remainder = total % pageSize;
                    int dividend = (total / pageSize);
                    if (remainder < pageSize) {
                        paged.setLast_page(dividend + 1);
                    } else {
                        paged.setLast_page(dividend);
                    }
                } else {
                    paged.setLast_page(1);
                }

                paged.setPer_page(pageSize);
                if (startFrom == 1) {
                    paged.setTo(totalFound);
                } else {
                    paged.setTo(startFrom + totalFound);
                    paged.setFrom(startFrom + 1);
                }
            }
            return paged;

//        } catch (NullPointerException e){
//            return null;
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }  finally {
            session.commit();
            session.close();
            close(session);
        }
    }

    @Override
    public PosTransactionDto SingleAgentRemittance(int MdaId, String TransactionId) {
        return null;
    }

    @Override
    public List<RemittanceDto> SearchAgentRemittance(int MdaId, int AgentId, String sortBy, String search) throws Exception{
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {


            Remittance md = new Remittance();
            md.remittanceNo = "%"+search+"%";
//            md.customerName = "%"+search+"%";
//            md.customerEmail = "%"+search+"%";
//            md.customerPhone = "%"+search+"%";
//            md.offlineTransactionId = "%"+search+"%";
            md.mdaId = MdaId;
            md.userId = AgentId;

            //sort criteria
//            sortBy = sortBy.isEmpty() ? null : sortBy;
//            md.setSortBy(sortBy);
            return session.selectList("Remittance.SearchAgentRemittances", md);


        } catch (NullPointerException e){
            return null;
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }  finally {
            session.commit();
            session.close();
            close(session);
        }
    }

    @Override
    public SingleRemittanceDto SingleRemittance(String remittanceId) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            SingleRemittanceDto sRem = new SingleRemittanceDto();
            IntRemittanceResponseDto remittanceResponse = session.selectOne("Remittance.SingleRemittancesWithId", remittanceId);
            if(remittanceResponse == null) throw new Exception("Remittance with no: "+remittanceId+" not found");
            sRem.transactionList = session.selectList("PosTransaction.AllTransactionByRem", remittanceResponse.id);
            sRem.remittance = SingleRemittanceMap(remittanceResponse);

            return sRem;


        } catch (NullPointerException e){
            return null;
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }  finally {
            session.commit();
            session.close();
            close(session);
        }
    }

    @Override
    public List<RemittanceDto> SearchRemittance(int ProjectId, int MdaId, int AgentId, String sortBy, String search) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {


            Remittance md = new Remittance();
            md.remittanceNo = "%"+search+"%";
//            md.customerName = "%"+search+"%";
//            md.customerEmail = "%"+search+"%";
//            md.customerPhone = "%"+search+"%";
//            md.offlineTransactionId = "%"+search+"%";
            md.mdaId = MdaId;
            md.userId = AgentId;
            md.terminalId = ProjectId;

            //sort criteria
//            sortBy = sortBy.isEmpty() ? null : sortBy;
//            md.setSortBy(sortBy);
            return session.selectList("Remittance.SearchRemittances", md);


        } catch (NullPointerException e){
            return null;
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }  finally {
            session.commit();
            session.close();
            close(session);
        }
    }

    @Override
    public RemittanceResponseDto CheckAgentOutStandingRemit(int id, int posId) throws Exception {
        ConfirmPos(posId);
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            RemittanceIntDto pendingRem = session.selectOne("Remittance.CheckRemittancesPending", id);
            if (pendingRem != null){
                id = pendingRem.getMdaId();
                Mda remMda = session.selectOne("Mda.getMdaById", id);
                RemittanceResponseDto outstandingRem = new RemittanceResponseDto();
                outstandingRem.generatedDate = pendingRem.getGeneratedDate();
                outstandingRem.remittanceNo = pendingRem.getRemittanceNo();
                outstandingRem.remittanceStatus = pendingRem.getRemittanceStatus();
                outstandingRem.amountGenerated = pendingRem.getTotalAmount();
                outstandingRem.posDeviceSerial = pendingRem.getPosSerial();
                outstandingRem.operator = pendingRem.getOperatorFirstName() + " " + pendingRem.getOperatorLastName();
                outstandingRem.mda = remMda.name;

                return outstandingRem;
            }
            return null;

        } catch (NullPointerException e){
            return null;
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }  finally {
            session.commit();
            session.close();
            close(session);
        }
    }

    @Override
    public boolean CheckAgentOutStandingCollect(int id, String s) throws Exception {
//        System.out.println(s);
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            PageFinder ps = new PageFinder();
            ps.agentId = id;
            ps.orderBy = s;
            List<PosTransactionDto> pendingTrans = session.selectList("PosTransaction.AgentTransactionUnRemitted", ps);
            System.out.println(pendingTrans.size());
            return pendingTrans.size() != 0;

//            if(pendingTrans.size() > 0){
//                double pendingAmount = 0.0;
//                for (PosTransactionDto pendingItem: pendingTrans
//                     ) {
//                    pendingAmount += Double.parseDouble(pendingItem.amountPaid);
//                }
//
//            }


        } catch (NullPointerException e){
//            return 0;
            throw new Exception("Agent record not found");
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }  finally {
            session.commit();
            session.close();
            close(session);
        }
    }


    @Override
    public double AllAgentTransactionAmount(int agentId, int agentProjId) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            RemittanceIntDto trans = new RemittanceIntDto();
            trans.setGeneratedDate(DateTime().toString());
            trans.setAgentId(agentId);
            trans.setMdaId(agentProjId);

            return session.selectOne("Remittance.AgentYTDAmount", trans);


        } catch (NullPointerException e){
            return 0;
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }  finally {
            session.commit();
            session.close();
            close(session);
        }
    }

    @Override
    public double AllAgentTransactionCount(int agentId, int agentProjId) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            RemittanceIntDto trans = new RemittanceIntDto();
            trans.setGeneratedDate(DateTime().toString());
            trans.setAgentId(agentId);
            trans.setMdaId(agentProjId);
            return session.selectOne("Remittance.AgentYTDCount", trans);


        } catch (NullPointerException e){
            return 0;
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }  finally {
            session.commit();
            session.close();
            close(session);
        }
    }

    @Override
    public List<MonthlyTransactionsDto> GetAgentMonthlySummary(int agentId, int mdaId) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            RemittanceIntDto trans = new RemittanceIntDto();
            trans.setGeneratedDate(DateTime().toString());
            trans.setAgentId(agentId);
            trans.setMdaId(mdaId);
            return session.selectList("Remittance.AgentMonthly", trans);


        } catch (NullPointerException e){
            return null;
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }  finally {
            session.commit();
            session.close();
            close(session);
        }
    }

    @Override
    public double MonthAgentTransactionAmount(int agentId, int mdaId, DateTime dateTime) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            PosTransaction trans = new PosTransaction();
            trans.transactionDate = new Timestamp(dateTime.toDate().getTime());
            trans.operatorId = agentId;
            trans.mdaId = mdaId;
            return session.selectOne("Remittance.MonthSumAgent", trans);

        } catch (NullPointerException e){
            return 0;
        } catch (Exception e){
            throw new Exception(e.getMessage());
        } finally {
            session.commit();
            session.close();
            close(session);
        }
    }

    @Override
    public double MonthAgentTransactionCount(int agentId, int mdaId, DateTime dateTime) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            PosTransaction trans = new PosTransaction();
            trans.transactionDate = new Timestamp(dateTime.toDate().getTime());
            trans.operatorId = agentId;
            trans.mdaId = mdaId;
            return session.selectOne("Remittance.MonthCountAgent", trans);

        } catch (NullPointerException e){
            return 0;
        } catch (Exception e){
            throw new Exception(e.getMessage());
        } finally {
            session.commit();
            session.close();
            close(session);
        }
    }
    @Override
    public double DayAgentTransactionAmount(int agentId, int mdaId, DateTime dateTime) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
//            int year = Year.now().getValue();
//            int year = Calendar.getInstance().get(Calendar.YEAR);

            PosTransaction trans = new PosTransaction();
            trans.transactionDate = new Timestamp(dateTime.toDate().getTime());
            trans.operatorId = agentId;
            trans.mdaId = mdaId;

            return session.selectOne("PosTransaction.DaySumAgent", trans);

        } catch (NullPointerException e){
            return 0;
        } catch (Exception e){
            throw new Exception(e.getMessage());
        } finally {
            session.commit();
            session.close();
            close(session);
        }
    }

    @Override
    public double DayAgentTransactionCount(int agentId, int mdaId, DateTime today) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            PosTransaction trans = new PosTransaction();
            trans.transactionDate = new Timestamp(today.toDate().getTime());
            trans.operatorId = agentId;
            trans.mdaId = mdaId;
            return session.selectOne("PosTransaction.DayCountAgent", trans);

        } catch (NullPointerException e){
            return 0;
        } catch (Exception e){
            throw new Exception(e.getMessage());
        } finally {
            session.commit();
            session.close();
            close(session);
        }
    }

    @Override
    public AgentCash AgentCashAtHand(int agentId, int mdaId) throws Exception {
//        PosTransaction pos = new PosTransaction();
//        pos.operatorId = agentId;
//        pos.mdaId = mdaId;
        int[] pendingIds;
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            List<Remittance> allPendingRem = session.selectList("Remittance.ListAgentRemittancesPending", agentId);
//            pendingIds = new int[allPendingRem.size()];
            if (allPendingRem.size() > 0){
                int counter = 1;
                pendingIds = new int[allPendingRem.size()+1];
                pendingIds[0] = 0;
                for (Remittance re: allPendingRem
                     ) {
                    pendingIds[counter] = re.getId();
                    counter++;
                }
            } else {
                pendingIds = new int[1];
                pendingIds[0] = 0;
            }

            return AllAgentCashAtHand(agentId, mdaId, pendingIds);

        } catch (NullPointerException e){
            return null;
        } catch (Exception e){
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }  finally {
            session.commit();
            session.close();
            close(session);
        }
    }

    public PaginatedDto AgentCashAtHandList(String sortBy, int pageNo, int pageSize, String filterBy, int filterId) throws Exception {
        int[] pendingIds;

        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {

            int startFrom = 0;
            if (pageNo > 0 && pageSize > 0) {
                startFrom = (pageNo - 1) * pageSize;
            } else {
                pageSize = 0;
            }

            PageFinder ps = new PageFinder();
            ps.from = startFrom;
            ps.recordsPerPage = pageSize;
            ps.orderBy = sortBy;

//            if (filterBy.equals(UserRoles.NonIndividual) || filterBy.equals(UserRoles.Individual)) ps.payerId = filterId;
            if (filterBy.equals(UserRoles.SubAdmin1) || filterBy.equals(UserRoles.ProjectReport) || filterBy.equals(UserRoles.Admin)) ps.businessId = filterId;
            if (filterBy.equals(UserRoles.SubAdmin2) || filterBy.equals(UserRoles.Agent)) ps.mdaId = filterId;



            List<Remittance> allPendingRem = session.selectList("Remittance.ListAgentRemittancesPending", ps);
//            pendingIds = new int[allPendingRem.size()];
            if (allPendingRem.size() > 0){
                int counter = 1;
                pendingIds = new int[allPendingRem.size()];
                pendingIds[0] = 0;
                for (Remittance re: allPendingRem
                ) {
                    pendingIds[counter] = re.getId();
                    counter++;
                }
            } else {
                pendingIds = new int[1];
                pendingIds[0] = 0;
            }
            List<AgentCashList> agl = new LinkedList<>();
            for (Remittance re:
                 allPendingRem) {
                AgentCashList aglItem = new AgentCashList();
                AgentCash cash = AllAgentCashAtHand(re.userId, re.mdaId, pendingIds);
                aglItem.setAmount(cash.getAmount());
                aglItem.setCount(cash.getCount());
                aglItem.setAgent(String.valueOf(re.getUserId()));
                aglItem.setMda(String.valueOf(re.getMdaId()));

                agl.add(aglItem);
            }

//            List<PosTransactionDto> pos = session.selectList("Remittance.AllMdaAgentRemittancesPaged", posFind);
            PaginatedDto paged = new PaginatedDto();
            paged.setStatus(200);
            if (startFrom == 0) {
                startFrom = 1;
            }
//            int total = session.selectOne("Mda.getCountMda");
            int total = session.selectOne("Remittance.getCountAgent", ps);
            int totalFound = agl.size();

            paged.setTotal(total);
            paged.setData(agl);
            paged.setFrom(startFrom);

            if(pageSize > 0) {
                if (total > pageSize) {
                    long remainder = total % pageSize;
                    int dividend = (total / pageSize);
                    if (remainder < pageSize) {
                        paged.setLast_page(dividend + 1);
                    } else {
                        paged.setLast_page(dividend);
                    }
                } else {
                    paged.setLast_page(1);
                }

                paged.setPer_page(pageSize);
                if (startFrom == 1) {
                    paged.setTo(totalFound);
                } else {
                    paged.setTo(startFrom + totalFound);
                    paged.setFrom(startFrom + 1);
                }
            }
            return paged;

//            return AllAgentCashAtHand(agentId, mdaId, pendingIds);

        } catch (NullPointerException e){
            return null;
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }  finally {
            session.commit();
            session.close();
            close(session);
        }
    }

    @Override
    public List<PosTransactionDto> AgentPosTransactionsDownload(int AgentId, int MdaId, String sortBy, int pageNo, int pageSize, Date startDate, Date endDate) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            int startFrom = 0;
            if (pageNo > 0 && pageSize > 0) {
                startFrom = (pageNo - 1) * pageSize;
            } else {
                pageSize = 0;
            }

            PageFinder posFind = new PageFinder();
            posFind.from = startFrom;
            posFind.recordsPerPage = pageSize;
            posFind.orderBy = sortBy;
            posFind.agentId = AgentId;
            posFind.mdaId = MdaId;
            posFind.startTransactionDate = new Timestamp(startDate.getTime());
            posFind.endTransactionDate = new Timestamp(endDate.getTime());

            List<PosTransactionDto> pos = session.selectList("PosTransaction.AgentTransaction", posFind);
            return pos;

        } catch (NullPointerException e){
            return null;
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }  finally {
            session.commit();
            session.close();
            close(session);
        }
    }

    @Override
    public List<PosTransactionDto> MdaPosTransactionsDownload(int MdaId, String sortBy, Date startDate, Date endDate) throws Exception {

        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            int startFrom = 0;

            PageFinder posFind = new PageFinder();
            posFind.from = startFrom;
            posFind.recordsPerPage = startFrom;
            posFind.orderBy = sortBy;
            posFind.mdaId = MdaId;
            posFind.startTransactionDate = new Timestamp(startDate.getTime());
            posFind.endTransactionDate = new Timestamp(endDate.getTime());
//            posFind.agentId = AgentId;

            List<PosTransactionDto> pos = session.selectList("PosTransaction.AllTransactionMda", posFind);
            return pos;

        } catch (NullPointerException e){
            return null;
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }  finally {
            session.commit();
            session.close();
            close(session);
        }
    }

    @Override
    public List<PosTransactionDto> ProjectPosTransactionsDownload(int BusinessId, String sortBy, Date startDate, Date endDate) throws Exception {

        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            int startFrom = 0;

            PageFinder posFind = new PageFinder();
            posFind.from = startFrom;
            posFind.recordsPerPage = startFrom;
            posFind.orderBy = sortBy;
            posFind.businessId = BusinessId;
            posFind.startTransactionDate = new Timestamp(startDate.getTime());
            posFind.endTransactionDate = new Timestamp(endDate.getTime());
//            posFind.agentId = AgentId;

            List<PosTransactionDto> pos = session.selectList("PosTransaction.AllTransactionProject", posFind);
            return pos;

        } catch (NullPointerException e){
            return null;
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }  finally {
            session.commit();
            session.close();
            close(session);
        }
    }

    @Override
    public List<PosTransactionDto> AllPosTransactionsDownload(String sortBy, Date startDate, Date endDate) throws Exception {

        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            int startFrom = 0;

            PageFinder posFind = new PageFinder();
            posFind.from = startFrom;
            posFind.recordsPerPage = startFrom;
            posFind.orderBy = sortBy;
            posFind.startTransactionDate = new Timestamp(startDate.getTime());
            posFind.endTransactionDate = new Timestamp(endDate.getTime());
//            posFind.mdaId = mdaId;
//            posFind.agentId = AgentId;

            List<PosTransactionDto> pos = session.selectList("PosTransaction.AllTransaction", posFind);
            return pos;
        } catch (NullPointerException e){
            return null;
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }  finally {
            session.commit();
            session.close();
            close(session);
        }
    }

    @Override
    public List<RemittanceDto> AgentRemittanceDownload(int AgentId, int MdaOfficeId, String sortBy, int pageNo, int pageSize, Date startDate, Date endDate) {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            int startFrom = 0;
            if (pageNo > 0 && pageSize > 0) {
                startFrom = (pageNo - 1) * pageSize;
            } else {
                pageSize = 0;
            }

            PageFinder posFind = new PageFinder();
            posFind.from = startFrom;
            posFind.recordsPerPage = pageSize;
            posFind.orderBy = sortBy;
            posFind.agentId = AgentId;
            posFind.mdaId = MdaOfficeId;
            posFind.startTransactionDate = new Timestamp(startDate.getTime());
            posFind.endTransactionDate = new Timestamp(endDate.getTime());

            List<RemittanceDto> pos = session.selectList("Remittance.AllMdaAgentRemittancesPaged", posFind);
            for (RemittanceDto remItem:
                 pos) {
//                remItem.getRemittanceStatus().equals("1")? remItem.setRemittanceStatus("Remitted") : remItem.setRemittanceStatus("Pending");
                remItem.setRemittanceStatus(remItem.getRemittanceStatus().equals("1")? "Remitted" : "Pending");
            }
            return pos;
        } finally {
            session.commit();
            session.close();
            close(session);
        }
    }

    @Override
    public List<RemittanceDto> MdaRemittanceDownload(int MdaId, String sortBy, boolean remStatus, boolean applyFilter, Date startDate, Date endDate) throws Exception {

        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            int startFrom = 0;


            PageFinder posFind = new PageFinder();
            posFind.from = startFrom;
            posFind.recordsPerPage = startFrom;
            posFind.orderBy = sortBy;
            posFind.mdaId = MdaId;
            posFind.status = remStatus;
            posFind.filter = applyFilter? 1 : 0;
            posFind.startTransactionDate = new Timestamp(startDate.getTime());
            posFind.endTransactionDate = new Timestamp(endDate.getTime());

//            List<PosTransactionDto> pos = session.selectList("Remittance.AllMdaRemittancesPaged", posFind);
            List<RemittanceDto> pos = session.selectList("Remittance.AllMdaRemittancesPaged", posFind);
            for (RemittanceDto remItem:
                    pos) {
                remItem.setRemittanceStatus(remItem.getRemittanceStatus().equals("1")? "Remitted" : "Pending");
            }
            return pos;

        } catch (NullPointerException e){
            return null;
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }  finally {
            session.commit();
            session.close();
            close(session);
        }
    }

    @Override
    public List<RemittanceDto> ProjectRemittanceDownload(int mdaId, String sortBy, boolean remStatus, boolean applyFilter, Date startDate, Date endDate) throws Exception {

        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            int startFrom = 0;

            PageFinder posFind = new PageFinder();
            posFind.from = startFrom;
            posFind.recordsPerPage = startFrom;
            posFind.orderBy = sortBy;
            posFind.mdaId = mdaId;
            posFind.filter = applyFilter? 1 : 0;
            posFind.status = remStatus;
            posFind.startTransactionDate = new Timestamp(startDate.getTime());
            posFind.endTransactionDate = new Timestamp(endDate.getTime());

//            List<PosTransactionDto> pos = session.selectList("Remittance.AllMdaRemittancesPaged", posFind);
            List<RemittanceDto> pos = session.selectList("Remittance.AllProjectRemittancesPaged", posFind);
            for (RemittanceDto remItem:
                    pos) {
                remItem.setRemittanceStatus(remItem.getRemittanceStatus().equals("1")? "Remitted" : "Pending");
            }
            return pos;

        } catch (NullPointerException e){
            return null;
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }  finally {
            session.commit();
            session.close();
            close(session);
        }
    }

    @Override
    public List<RemittanceDto> AllRemittanceDownload(String sortBy, boolean remStatus, boolean applyFilter, Date startDate, Date endDate) throws Exception {

        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            int startFrom = 0;
            PageFinder posFind = new PageFinder();
            posFind.from = startFrom;
            posFind.recordsPerPage = startFrom;
            posFind.orderBy = sortBy;
            posFind.filter = applyFilter? 1 : 0;
            posFind.status = remStatus;
            posFind.startTransactionDate = new Timestamp(startDate.getTime());
            posFind.endTransactionDate = new Timestamp(endDate.getTime());

            List<RemittanceDto> pos = session.selectList("Remittance.AllRemittancesPaged", posFind);
            for (RemittanceDto remItem:
                    pos) {
                remItem.setRemittanceStatus(remItem.getRemittanceStatus().equals("1")? "Remitted" : "Pending");
            }
            return pos;

//        } catch (NullPointerException e){
//            return null;
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }  finally {
            session.commit();
            session.close();
            close(session);
        }
    }


    @Override
    public double SumRemittances(int businessId, int mdaId, int agentId, boolean isRemited, DateTime dateTime) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            PageFinder pg = new PageFinder();
            pg.startTransactionDate = new Timestamp(dateTime.toDate().getTime());
            pg.businessId = businessId;
            pg.mdaId = mdaId;
            pg.agentId = agentId;
            pg.status = isRemited;

            System.out.println("SumDate");
            System.out.println(dateTime);

            return session.selectOne("Remittance.SumRemitMega", pg);
        } catch (NullPointerException e){
            return 0;
        } catch (Exception e){
            throw new Exception(e.getMessage());
        } finally {
            session.commit();
            session.close();
            close(session);
        }
    }

    @Override
    public int CountRemittances(int businessId, int mdaId, int agentId, boolean isRemited, DateTime dateTime) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            PageFinder pg = new PageFinder();
            pg.startTransactionDate = new Timestamp(dateTime.toDate().getTime());
            pg.businessId = businessId;
            pg.mdaId = mdaId;
            pg.agentId = agentId;
            pg.status = isRemited;

            System.out.println("CountDate");
            System.out.println(dateTime);
            int rc = session.selectOne("Remittance.CountRemitMega", pg);
            System.out.println(rc);

            return session.selectOne("Remittance.CountRemitMega", pg);
        } catch (NullPointerException e){
            return 0;
        } catch (Exception e){
            System.out.println(e.getCause());
            throw new Exception(e.getMessage());
        } finally {
            session.commit();
            session.close();
            close(session);
        }
    }

    @Override
    public List<RemitSummaryListDto> ListRemitSummary(int businessId, int mdaId, int officeId, Date startDate, Date endDate) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            PageFinder pf = new PageFinder();
            pf.businessId = businessId;
            pf.mdaId = mdaId;
            pf.officeId = officeId;
            pf.status = businessId == 0 && mdaId == 0 && officeId == 0;
            pf.startTransactionDate = new Timestamp(startDate.getTime());
            pf.endTransactionDate = new Timestamp(endDate.getTime());
//            pf.status = isActive;



//            PageFinder pfAlt = new PageFinder();
//            pfAlt.businessId = 0;
//            pfAlt.mdaId = 0;
//            pfAlt.officeId = 0;

            List<RemitSummaryListDto> list = session.selectList("Remittance.remSummaryList", pf);


            for (RemitSummaryListDto item : list
            ) {
                PageFinder pfAlt = new PageFinder();

                if(businessId > 0){
                    pfAlt.businessId = 0;
                    pfAlt.mdaId = item.mdaId;
                    pfAlt.officeId = 0;
                }
                if(mdaId > 0){

                    pfAlt.businessId = 0;
                    pfAlt.mdaId = 0;
                    pfAlt.officeId = item.officeId;
                }
                if(officeId > 0){

                    pfAlt.businessId = 0;
                    pfAlt.mdaId = 0;
                    pfAlt.officeId = 0;
                    pfAlt.agentId = item.officeId;
                }

                if(businessId == 0 && mdaId == 0 && officeId == 0){
                    pfAlt.businessId = item.businessId;
                    pfAlt.mdaId = 0;
                    pfAlt.officeId = 0;
                }
                pfAlt.startTransactionDate = new Timestamp(startDate.getTime());
                pfAlt.endTransactionDate = new Timestamp(endDate.getTime());

                System.out.println(pfAlt.businessId);
                System.out.println(pfAlt.mdaId);
                System.out.println(pfAlt.officeId);

                item.amountPaid = session.selectOne("Remittance.remSummaryListAlt", pfAlt);
//                item.approved = item.count - item.unapproved;
            }

            return list;

        } catch (NullPointerException e){
            System.out.println(e.getCause());
            e.printStackTrace();
            return null;
        } catch (Exception e){
            System.out.println(e.getCause());
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }  finally {
            session.commit();
            session.close();
            close(session);
        }
    }

    @Override
    public RemitSummaryListDto AgentRemittanceSum(int agentId, DateTime today) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {

            PageFinder posFind = new PageFinder();
            posFind.agentId = agentId;
            posFind.startTransactionDate = new Timestamp(today.toDate().getTime());

            RemitSummaryListDto rsl = new RemitSummaryListDto();

            rsl.count = session.selectOne("Remittance.getCountAgentPaid", posFind);
            rsl.amountPaid = session.selectOne("Remittance.getSumAgentPaid", posFind);

//            item.amountPaid = session.selectOne("Remittance.remSummaryListAlt", pfAlt);
            return rsl;
        } finally {
            session.commit();
            session.close();
            close(session);
        }
    }

    @Override
    public PaginatedDto MdaOfficeRemittance(int officeInt, String sortBy, Integer pageNo, Integer pageSize, boolean remStatus, boolean applyFilter, Date startDate, Date endDate) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            int startFrom = 0;
            if (pageNo > 0 && pageSize > 0) {
                startFrom = (pageNo - 1) * pageSize;
            } else {
                pageSize = 0;
            }

            PageFinder posFind = new PageFinder();
            posFind.from = startFrom;
            posFind.recordsPerPage = pageSize;
            posFind.orderBy = sortBy;
            posFind.officeId = officeInt;
            posFind.status = remStatus;
            posFind.filter = applyFilter? 1 : 0;
            posFind.startTransactionDate = new Timestamp(startDate.getTime());
            posFind.endTransactionDate = new Timestamp(endDate.getTime());

//            List<PosTransactionDto> pos = session.selectList("Remittance.AllMdaRemittancesPaged", posFind);
            List<RemittanceDto> pos = session.selectList("Remittance.AllMdaOfficeRemittancesPaged", posFind);
            PaginatedDto paged = new PaginatedDto();
            paged.setStatus(200);
            if (startFrom == 0) {
                startFrom = 1;
            }
//            int total = session.selectOne("Mda.getCountMda");
            int total = 0;
            double sum = 0;
            int totalFound = 0;
            if(pos.size() > 0) {
                total = session.selectOne("Remittance.getCountMdaOffice", posFind);
                sum = session.selectOne("Remittance.getSumMdaOffice", posFind);
                totalFound = pos.size();
            }

            paged.setTotal(total);
            paged.setData(pos);
            paged.setFrom(startFrom);
            paged.setSum(sum);

            if(pageSize > 0) {
                if (total > pageSize) {
                    long remainder = total % pageSize;
                    int dividend = (total / pageSize);
                    if (remainder < pageSize) {
                        paged.setLast_page(dividend + 1);
                    } else {
                        paged.setLast_page(dividend);
                    }
                } else {
                    paged.setLast_page(1);
                }

                paged.setPer_page(pageSize);
                if (startFrom == 1) {
                    paged.setTo(totalFound);
                } else {
                    paged.setTo(startFrom + totalFound);
                    paged.setFrom(startFrom + 1);
                }

                System.out.println("total is: " + total);
            }
            return paged;

        } catch (NullPointerException e){
            return null;
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }  finally {
            session.commit();
            session.close();
            close(session);
        }
    }


    private AgentCash AllAgentCashAtHand(int agentId, int mdaId, int[] ids) throws Exception {
        double totalAmount = 0.0;
        int totalCount = 0;
        PosTransaction pos = new PosTransaction();
        pos.operatorId = agentId;
        pos.mdaId = mdaId;
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            for (int id = 0; id < ids.length; id++) {
                pos.remittanceId = ids[id];
                double amount = session.selectOne("PosTransaction.getAgentUnremSum", pos);
                int count = session.selectOne("PosTransaction.getAgentUnremCount", pos);

                totalAmount += amount;
                totalCount += count;

            }
            AgentCash agentCash= new AgentCash();
            agentCash.setAmount(totalAmount);
            agentCash.setCount(totalCount);

            return agentCash;

        } catch (NullPointerException e){
            return null;
        } catch (Exception e){
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }  finally {
            session.commit();
            session.close();
            close(session);
        }
    }

    private Pos ConfirmPos(int posId) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
//            return session.selectOne("Pos.getPosById", posId);
            Pos pos = session.selectOne("Pos.getPosById", posId);
            if (pos == null) throw new Exception("unknown device");
            if (!pos.isActive) throw new Exception("device not authorized");
            return pos;

        } catch (NullPointerException e){
            return null;
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }  finally {
            session.commit();
            session.close();
            close(session);
        }
    }
}
