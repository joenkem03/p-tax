package org.bizzdeskgroup.services.impl;

import org.apache.ibatis.session.SqlSession;
import org.bizzdeskgroup.Dtos.Command.CreateCardWebTransactionDto;
import org.bizzdeskgroup.Dtos.Command.CreatePosCardTransactionDto;
import org.bizzdeskgroup.Dtos.Command.CreateTransactionDto;
import org.bizzdeskgroup.Dtos.PayStack.TransactionVerificationResponseDto;
import org.bizzdeskgroup.Dtos.Query.*;
import org.bizzdeskgroup.Helpers.RequestHandler;
import org.bizzdeskgroup.factory.MyBatisConnectionFactory;
import org.bizzdeskgroup.models.*;
import org.bizzdeskgroup.services.TransactionService;
import org.eclipse.microprofile.rest.client.RestClientBuilder;
import org.joda.time.DateTime;

import java.net.URL;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static org.bizzdeskgroup.Helpers.NotificationMixer.DateTime;
import static org.bizzdeskgroup.Mappers.TransactionMapper.NewCardTransactionItem;
import static org.bizzdeskgroup.Mappers.TransactionMapper.NewTransactionItem;
import static org.eclipse.jetty.util.IO.close;

public class TransactionServiceImpl implements TransactionService {
    @Override
    public CreateTransactionDto NewTransaction(CreateCardWebTransactionDto newCardTransaction, int userId, int businessId) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try{
        Timestamp timestamp = DateTime();


        RequestHandler velidatePayment = RestClientBuilder.newBuilder()
                .baseUrl(new URL("https://middleware.paysure.ng/paystack/verify"))
//                .baseUrl(new URL("http://middleware.paysure.ng/paystack/test/verify"))
//                .baseUrl(new URL("http://localhost:8021/paystack/verify"))
                .build(RequestHandler.class);
        System.out.println("buz buz: "+businessId);
//        BusinessDto buzz = session.selectOne("Business.singleClients", businessId);
            Business buzz = session.selectOne("Business.businessByIdz", businessId);
        TransactionVerificationResponseDto requests = velidatePayment.validatePayStack(newCardTransaction.transactionId);

        System.out.println("paystack: "+requests.toString());


                    CreateTransactionDto  newTransaction = new CreateTransactionDto();
                    newTransaction.transactionDate = requests.getData().getTransaction_date();
                    newTransaction.transactionId = newCardTransaction.transactionId;
                    newTransaction.transactionReference = newCardTransaction.transactionReference;
                    newTransaction.customerEmail = requests.getData().getCustomer().getEmail();
                    newTransaction.customerPhone = requests.getData().getCustomer().getPhone();
                    newTransaction.customerName = requests.getData().getCustomer().getFirst_name() +" "+ requests.getData().getCustomer().getLast_name();
                    newTransaction.transactionDescription = newCardTransaction.transactionDescription;
//                    newTransaction.serviceAmount = (requests.getData().getAmount()/100) - 10;
                    newTransaction.serviceAmount = (requests.getData().getAmount()/100) - buzz.getCommission() - buzz.getFee();
                    newTransaction.channel="WEB";
                    newTransaction.commission = buzz.getCommission();
                    newTransaction.fee = buzz.getFee();
                    newTransaction.businessId = businessId;

//                    newTransaction.mdaId = 1;
//                    newTransaction.businessId = 1;
                    newTransaction.totalAmountPaid = requests.getData().getAmount()/100;
//                    newTransaction.transactionType = "INV";
                    newTransaction.transactionType = newCardTransaction.transactionType;
                    newTransaction.businessName = buzz.clientName;


//            if(requests.getData().getStatus().equals("success") && newCardTransaction.transactionId.equals(requests.getData().getReference())){
            if(requests.getData().getStatus().equals("success") && newCardTransaction.transactionId.equals(requests.getData().getReference())){
                System.out.println("isATrans");

                Transaction transaction = NewTransactionItem(newTransaction);

                    transaction.setTransactionDate(new Timestamp(newTransaction.transactionDate.getTime()));
                    transaction.setCreatedDate(timestamp);
                    transaction.setCreatedBy(userId);
                    transaction.setCreatedDateStamp(timestamp.getTime()/1000);


                if(transaction.transactionType.equals("REM")){
                    Remittance rem = session.selectOne("Remittance.RemittancesByRef", transaction.transactionReference);
                    if(!rem.status){
//
                        if(transaction.totalAmountPaid != rem.getTotalAmount()) throw new Exception("Wrong remittance Amount");
                        transaction.mdaId = rem.getMdaId();
//                        transaction.businessId = businessId;
                        transaction.createdBy = rem.getCreatedBy();

                        newTransaction.mdaId = transaction.mdaId;
                        session.insert("Transaction.insert", transaction);
                        System.out.println(rem.status);
                        System.out.println(rem.id);
                        rem.setStatus(true);
                        rem.setUpdatedBy(userId);
                        rem.setUpdatedDate(timestamp);
                        session.update("Remittance.update", rem);
                    }
                }
                if(transaction.transactionType.equals("INV")) {
                    Invoice inv = session.selectOne("Invoice.invoicesByNoX", transaction.transactionReference);
//                Remittance rem = session.selectOne("Remittance.RemittancesByRef", transaction.transactionReference);
                    if (inv != null && !inv.isPaid()) {
                        System.out.println(inv.getInvoiceAmount());
                        System.out.println(transaction.totalAmountPaid);
                        if (transaction.totalAmountPaid != inv.getInvoiceAmount())
                            throw new Exception("Wrong invoice Amount");
//                        if (1 ==1)
//                            throw new Exception("Wrong invoice Amountx");
                        System.out.println("isAInv");
                        transaction.mdaId = inv.getMdaId();
//                        transaction.businessId = businessId;
                        transaction.createdBy = inv.getCreatedBy();

                        newTransaction.mdaId = transaction.mdaId;
                        session.insert("Transaction.insert", transaction);
//                    System.out.println(inv.status);
//                    System.out.println(rem.id);
                        inv.setPaid(true);
                        inv.setUpdatedBy(userId);
                        inv.setUpdatedDate(timestamp);
                        inv.setPaymentChannel(newTransaction.channel);
                        session.update("Invoice.update", inv);


                        double paidInvSum = 0.0;
                        double assessAmnt = 0.0;


                        paidInvSum = inv.getAssessmentId() > 0 ? session.selectOne("Invoice.singleAssessPaidSum", inv.getAssessmentId()) : 0;
                        Assessment assessment = inv.getAssessmentId() > 0 ? session.selectOne("Assessment.singleAssess", inv.getAssessmentId()) : null;

                        if (assessment != null) assessAmnt = assessment.taxAmount;

                        newTransaction.pendingBalance = assessAmnt - paidInvSum ;

                        Thread object
                                = new Thread(new UpdateInvoiceAssessment(inv.getAssessmentId()));
                        object.start();

                    }
                }
                Mda mdaDto = session.selectOne("Mda.getMdaById", newTransaction.mdaId);
                if(mdaDto != null) newTransaction.mdaName = mdaDto.name;
                newTransaction.mdaId = 0;
            }
            return newTransaction;
        } catch (Exception e){
            e.printStackTrace();
            throw new Exception(e.getMessage());
        } finally {
            session.commit();
            session.close();
            close(session);
        }

    }

    @Override
    public CreateTransactionDto NewTestTransaction(CreateCardWebTransactionDto newCardTransaction, int userId, int businessId) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try{
            Timestamp timestamp = DateTime();


            RequestHandler velidatePayment = RestClientBuilder.newBuilder()
                    .baseUrl(new URL("https://middleware.paysure.ng/paystack/test/verify"))
//                .baseUrl(new URL("http://middleware.paysure.ng/paystack/test/verify"))
//                .baseUrl(new URL("http://localhost:8021/paystack/verify"))
                    .build(RequestHandler.class);
            System.out.println("buz buz: "+businessId);
            Business buzz = session.selectOne("Business.businessByIdz", businessId);
            TransactionVerificationResponseDto requests = velidatePayment.validatePayStack(newCardTransaction.transactionId);

            System.out.println("paystack: "+requests.toString());


            CreateTransactionDto  newTransaction = new CreateTransactionDto();
            newTransaction.transactionDate = requests.getData().getTransaction_date();
            newTransaction.transactionId = newCardTransaction.transactionId;
            newTransaction.transactionReference = newCardTransaction.transactionReference;
            newTransaction.customerEmail = requests.getData().getCustomer().getEmail();
            newTransaction.customerPhone = requests.getData().getCustomer().getPhone();
            newTransaction.customerName = requests.getData().getCustomer().getFirst_name() +" "+ requests.getData().getCustomer().getLast_name();
            newTransaction.transactionDescription = newCardTransaction.transactionDescription;
            newTransaction.serviceAmount = (requests.getData().getAmount()/100) - buzz.getCommission() - buzz.getFee();
            newTransaction.channel="WEB";
            newTransaction.commission = buzz.getCommission();
            newTransaction.fee = buzz.getFee();
//                    newTransaction.mdaId = 1;
//                    newTransaction.businessId = 1;
            newTransaction.totalAmountPaid = requests.getData().getAmount()/100;
//                    newTransaction.transactionType = "INV";
            newTransaction.transactionType = newCardTransaction.transactionType;
            newTransaction.businessName = buzz.getClientName();
            newTransaction.businessId = businessId;


//            if(requests.getData().getStatus().equals("success") && newCardTransaction.transactionId.equals(requests.getData().getReference())){
            if(requests.getData().getStatus().equals("success") && newCardTransaction.transactionId.equals(requests.getData().getReference())){
                System.out.println("isATrans");

                Transaction transaction = NewTransactionItem(newTransaction);

                transaction.setTransactionDate(new Timestamp(newTransaction.transactionDate.getTime()));
                transaction.setCreatedDate(timestamp);
                transaction.setCreatedBy(userId);
                transaction.setCreatedDateStamp(timestamp.getTime()/1000);


                if(transaction.transactionType.equals("REM")){
                    Remittance rem = session.selectOne("Remittance.RemittancesByRef", transaction.transactionReference);
                    if(!rem.status){
//
                        if(transaction.totalAmountPaid != rem.getTotalAmount()) throw new Exception("Wrong remittance Amount");
                        transaction.mdaId = rem.getMdaId();
//                        transaction.businessId = businessId;
                        transaction.createdBy = rem.getCreatedBy();

                        newTransaction.mdaId = transaction.mdaId;
                        session.insert("Transaction.insert", transaction);
                        System.out.println(rem.status);
                        System.out.println(rem.id);
                        rem.setStatus(true);
                        rem.setUpdatedBy(userId);
                        rem.setUpdatedDate(timestamp);
                        session.update("Remittance.update", rem);
                    }
                }
                if(transaction.transactionType.equals("INV")) {
                    Invoice inv = session.selectOne("Invoice.invoicesByNoX", transaction.transactionReference);
//                Remittance rem = session.selectOne("Remittance.RemittancesByRef", transaction.transactionReference);
                    if (inv != null && !inv.isPaid()) {
                        System.out.println(inv.getInvoiceAmount());
                        System.out.println(transaction.totalAmountPaid);
                        if (transaction.totalAmountPaid != inv.getInvoiceAmount())
                            throw new Exception("Wrong invoice Amount");
//                        if (1 ==1)
//                            throw new Exception("Wrong invoice Amountx");
                        System.out.println("isAInv");
                        transaction.mdaId = inv.getMdaId();
//                        transaction.businessId = businessId;
                        transaction.createdBy = inv.getCreatedBy();

                        newTransaction.mdaId = transaction.mdaId;
                        session.insert("Transaction.insert", transaction);
//                    System.out.println(inv.status);
//                    System.out.println(rem.id);
                        inv.setPaid(true);
                        inv.setUpdatedBy(userId);
                        inv.setUpdatedDate(timestamp);
                        inv.setPaymentChannel(newTransaction.channel);
                        session.update("Invoice.update", inv);


                        double paidInvSum = 0.0;
                        double assessAmnt = 0.0;


                        paidInvSum = inv.getAssessmentId() > 0 ? session.selectOne("Invoice.singleAssessPaidSum", inv.getAssessmentId()) : 0;
                        Assessment assessment = inv.getAssessmentId() > 0 ? session.selectOne("Assessment.singleAssess", inv.getAssessmentId()) : null;

                        if (assessment != null) assessAmnt = assessment.taxAmount;

                        newTransaction.pendingBalance = assessAmnt - paidInvSum ;

                        Thread object
                                = new Thread(new UpdateInvoiceAssessment(inv.getAssessmentId()));
                        object.start();

                    }
                }
                Mda mdaDto = session.selectOne("Mda.getMdaById", newTransaction.mdaId);
                if(mdaDto != null) newTransaction.mdaName = mdaDto.name;
                newTransaction.mdaId = 0;
            }
            return newTransaction;
        } catch (Exception e){
            e.printStackTrace();
            throw new Exception(e.getMessage());
        } finally {
            session.commit();
            session.close();
            close(session);
        }

    }


    @Override
    public void NewPosCardTransaction(CreatePosCardTransactionDto transactionDto, int userId, int businessId) throws Exception {

        Timestamp timestamp = DateTime();
        Transaction transaction = NewCardTransactionItem(transactionDto);

        transaction.setTransactionDate(new Timestamp(transactionDto.transactionDate.getTime()));
        transaction.setCreatedDate(timestamp);
        transaction.setCreatedDateStamp(timestamp.getTime()/1000);
        transaction.setCreatedBy(userId);
        transaction.setBusinessId(businessId);

        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try{
            Business buzz = session.selectOne("Business.businessByIdz", businessId);

            transaction.setTotalAmountPaid(transactionDto.totalAmountPaid - buzz.getCommission() - buzz.getFee());
            transaction.setFee(buzz.getFee());
            transaction.setComission(buzz.getCommission());

            if(transaction.transactionType.equals("INV")){
                Invoice inv = session.selectOne("Invoice.invoicesByNoX", transaction.transactionReference);
//                Remittance rem = session.selectOne("Remittance.RemittancesByRef", transaction.transactionReference);
                if(inv != null && !inv.isPaid()){
//                    System.out.println(inv.status);
//                    System.out.println(rem.id);
                    inv.setPaid(true);
                    inv.setUpdatedBy(userId);
                    inv.setUpdatedDate(timestamp);
                    inv.setPaymentChannel(transactionDto.channel);

                    transaction.setMdaId(inv.getMdaId());


//
                    session.insert("Transaction.insert", transaction);
                    session.update("Invoice.update", inv);
                    double paidInvSum = 0.0;
                    double assessAmnt = 0.0;


//                    paidInvSum = inv.getAssessmentId() > 0 ? session.selectOne("Invoice.singleAssessPaidSum", inv.getAssessmentId()) : 0;
//                    Assessment assessment = inv.getAssessmentId() > 0 ? session.selectOne("Assessment.singleAssess", inv.getAssessmentId()) : null;
//
//                    if (assessment != null) assessAmnt = assessment.taxAmount;
//
//                    newTransaction.pendingBalance = assessAmnt - paidInvSum ;


                    Thread object
                            = new Thread(new UpdateInvoiceAssessment(inv.getAssessmentId()));
                    object.start();
                }
            }

            if(transaction.transactionType.equals("REM")){
                Remittance rem = session.selectOne("Remittance.RemittancesByRef", transaction.transactionReference);
                if(rem != null && !rem.status){
//                    System.out.println(rem.status);
//                    System.out.println(rem.id);
                    rem.setStatus(true);
                    rem.setUpdatedBy(userId);
                    rem.setUpdatedDate(timestamp);

                    transaction.setMdaId(rem.getMdaId());
//
                    session.insert("Transaction.insert", transaction);
                    session.update("Remittance.update", rem);
                }
            }
        } catch (Exception e){
            System.out.println(e.getCause());
            e.printStackTrace();
            throw new Exception(e.getMessage());
        } finally {
            session.commit();
            session.close();
            close(session);
        }
    }

    @Override
    public PaginatedDto AllTransactions(String sortBy, int pageNo, int pageSize, boolean applyFilter, String filterBy, String filterValue, Date startDate, Date endDate) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            int startFrom = 0;
            if (pageNo > 0 && pageSize > 0) {
                startFrom = (pageNo - 1) * pageSize;
            } else {
                //                pageSize = 10;
                pageSize = 0;
            }

            PageFinder posFind = new PageFinder();
            posFind.from = startFrom;
            posFind.recordsPerPage = pageSize;
            posFind.orderBy = sortBy;
            posFind.filter = applyFilter? 1 : 0;
            posFind.filterBy = filterBy;
            posFind.filterValue = filterValue;
            posFind.startTransactionDate = new Timestamp(startDate.getTime());
            posFind.endTransactionDate = new Timestamp(endDate.getTime());


            System.out.println(posFind.filter+"----"+posFind.filterBy+"-----"+posFind.filterValue+"#####"+posFind.startTransactionDate+"*******"+posFind.endTransactionDate);

            List<AdminTransactionDto> transactionDtoList = session.selectList("Transaction.AllTransactionsPaged", posFind);
//            for (AdminTransactionDto tnx:
//                    transactionDtoList) {
//                tnx.setMdaName(GetMdaName(tnx.getMdaId()));
//            }
            PaginatedDto paged = new PaginatedDto();
            paged.setStatus(200);
            if (startFrom == 0) {
                startFrom = 1;
            }
//            int total = session.selectOne("Mda.getCountMda");
            int total = 0;
            double sum = 0;
            int totalFound = 0;
            double netSum = 0;
            double charge = 0;
            if(transactionDtoList.size() > 0) {
                total = session.selectOne("Transaction.CountAll", posFind);
                sum = session.selectOne("Transaction.SumAll", posFind);
                netSum = session.selectOne("Transaction.SumNetAll", posFind);
                charge = session.selectOne("Transaction.SumChargeAll", posFind);
                totalFound = transactionDtoList.size();
            }

            paged.setTotal(total);
            paged.setSum(sum);
            paged.setData(transactionDtoList);
            paged.setService_charge(charge);
            paged.setNet_sum(netSum);
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
        } catch (Exception e){
            throw new Exception(e.getMessage());
        } finally {
            session.commit();
            session.close();
            close(session);
        }
    }

    @Override
    public PaginatedDto AllProjectTransactions(int projectId, String sortBy, int pageNo, int pageSize, boolean applyFilter, String filterBy, String filterValue, Date startDate, Date endDate) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            int startFrom = 0;
            if (pageNo > 0 && pageSize > 0) {
                startFrom = (pageNo - 1) * pageSize;
            } else {
                //                pageSize = 10;
                pageSize = 0;
            }

            PageFinder posFind = new PageFinder();
            posFind.from = startFrom;
            posFind.recordsPerPage = pageSize;
            posFind.orderBy = sortBy;
            posFind.businessId = projectId;
            posFind.filter = applyFilter? 1 : 0;
            posFind.filterBy = filterBy;
            posFind.filterValue = filterValue;
            posFind.startTransactionDate = new Timestamp(startDate.getTime());
            posFind.endTransactionDate = new Timestamp(endDate.getTime());

            List<AdminTransactionDto> transactionDtoList = session.selectList("Transaction.AllProjectTransactionsPaged", posFind);
            PaginatedDto paged = new PaginatedDto();
            paged.setStatus(200);
            if (startFrom == 0) {
                startFrom = 1;
            }
//            int total = session.selectOne("Mda.getCountMda");
            int total = 0;
            double sum = 0;
            int totalFound = 0;
            double charge = 0;
            double netSum = 0;
            if(transactionDtoList.size() > 0) {
                total = session.selectOne("Transaction.CountProject", posFind);
                sum = session.selectOne("Transaction.SumProject", posFind);
                charge = session.selectOne("Transaction.SumChargeProject", posFind);
                netSum = session.selectOne("Transaction.SumNetProject", posFind);
                totalFound = transactionDtoList.size();
            }

            paged.setTotal(total);
            paged.setData(transactionDtoList);
            paged.setFrom(startFrom);
            paged.setSum(sum);
            paged.setNet_sum(netSum);
            paged.setService_charge(charge);

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
        } catch (Exception e){
            e.printStackTrace();
            throw new Exception(e.getMessage());
        } finally {
            session.commit();
            session.close();
            close(session);
        }
    }

    @Override
    public PaginatedDto AllMdaTransactions(int mdaId, int projectId, String sortBy, int pageNo, int pageSize, boolean applyFilter, String filterBy, String filterValue, Date startDate, Date endDate) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            int startFrom = 0;
            if (pageNo > 0 && pageSize > 0) {
                startFrom = (pageNo - 1) * pageSize;
            } else {
                //                pageSize = 10;
                pageSize = 0;
            }

            PageFinder posFind = new PageFinder();
            posFind.from = startFrom;
            posFind.recordsPerPage = pageSize;
            posFind.orderBy = sortBy;
            posFind.mdaId = mdaId;
            posFind.businessId = projectId;
            posFind.filter = applyFilter? 1 : 0;
            posFind.filterBy = filterBy;
            posFind.filterValue = filterValue;
            posFind.startTransactionDate = new Timestamp(startDate.getTime());
            posFind.endTransactionDate = new Timestamp(endDate.getTime());


//            System.out.println(posFind.filter+"----"+posFind.filterBy+"-----"+filterValue);

            List<TransactionDto> transactionDtoList = session.selectList("Transaction.AllMdaTransactionsPaged", posFind);
//            for (TransactionDto tnx:
//                    transactionDtoList) {
//                tnx.setMdaName(GetMdaName(tnx.getMdaId()));
//            }
//            for (TransactionDto tnx:
//                 transactionDtoList) {
//                tnx.setMdaName(GetMdaName(tnx.getMdaId()));
//            }
            PaginatedDto paged = new PaginatedDto();
            paged.setStatus(200);
            if (startFrom == 0) {
                startFrom = 1;
            }
//            int total = session.selectOne("Mda.getCountMda");
            int total = 0;
            double sum = 0;
            double charge = 0;
            double netSum = 0;
            int totalFound = 0;
            if(transactionDtoList.size() > 0) {
                total = session.selectOne("Transaction.CountMda", posFind);
                sum = session.selectOne("Transaction.SumMda", posFind);
                charge = session.selectOne("Transaction.SumChargeMda", posFind);
                netSum = session.selectOne("Transaction.SumNetMda", posFind);

                totalFound = transactionDtoList.size();
            }

            paged.setTotal(total);
            paged.setData(transactionDtoList);
            paged.setFrom(startFrom);
            paged.setSum(sum);
            paged.setService_charge(charge);
            paged.setNet_sum(netSum);

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
        } catch (Exception e){
            throw new Exception(e.getMessage());
        } finally {
            session.commit();
            session.close();
            close(session);
        }
    }

    @Override
    public PaginatedDto AllAgentTransactions(int AgentId, int mdaId, int projectId, String sortBy, int pageNo, int pageSize, boolean applyFilter, String filterBy, String filterValue, Date startDate, Date endDate) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            int startFrom = 0;
            if (pageNo > 0 && pageSize > 0) {
                startFrom = (pageNo - 1) * pageSize;
            } else {
                //                pageSize = 10;
                pageSize = 0;
            }

            PageFinder posFind = new PageFinder();
            posFind.from = startFrom;
            posFind.recordsPerPage = pageSize;
            posFind.orderBy = sortBy;
            posFind.mdaId = mdaId;
            posFind.businessId = projectId;
            posFind.filter = applyFilter? 1 : 0;
            posFind.filterBy = filterBy;
            posFind.filterValue = filterValue;
            posFind.startTransactionDate = new Timestamp(startDate.getTime());
            posFind.endTransactionDate = new Timestamp(endDate.getTime());
            posFind.agentId = AgentId;


//            System.out.println(posFind.filter+"----"+posFind.filterBy+"-----"+filterValue);

            List<TransactionDto> transactionDtoList = session.selectList("Transaction.AllMdaTransactionsPaged", posFind);
            for (TransactionDto tnx:
                    transactionDtoList) {
                tnx.setMdaName(GetMdaName(tnx.getMdaId()));
            }
            PaginatedDto paged = new PaginatedDto();
            paged.setStatus(200);
            if (startFrom == 0) {
                startFrom = 1;
            }
//            int total = session.selectOne("Mda.getCountMda");
            int total = 0;
            double sum = 0;
            int totalFound = 0;
            if(transactionDtoList.size() > 0) {
                total = session.selectOne("Transaction.CountMda", posFind);
                sum = session.selectOne("Transaction.SumMda", posFind);
                totalFound = transactionDtoList.size();
            }

            paged.setTotal(total);
            paged.setData(transactionDtoList);
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
        } catch (Exception e){
            throw new Exception(e.getMessage());
        } finally {
            session.commit();
            session.close();
            close(session);
        }
    }

    @Override
    public List<AdminTransactionDto> AllTransactionsDownload(String sortBy, boolean applyFilter, String filterBy, String filterValue, Date startDate, Date endDate) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            int startFrom = 0;
            PageFinder posFind = new PageFinder();
            posFind.from = startFrom;
            posFind.recordsPerPage = startFrom;
            posFind.orderBy = sortBy;
            posFind.filter = applyFilter? 1 : 0;
            posFind.filterBy = filterBy;
            posFind.filterValue = filterValue;
            posFind.startTransactionDate = new Timestamp(startDate.getTime());
            posFind.endTransactionDate = new Timestamp(endDate.getTime());


//            System.out.println(posFind.filter+"----"+posFind.filterBy+"-----"+posFind.filterValue+"#####"+posFind.startTransactionDate+"*******"+posFind.endTransactionDate);

            List<AdminTransactionDto> transactionDtoList = session.selectList("Transaction.AllTransactionsPaged", posFind);
            for (AdminTransactionDto tnx:
                    transactionDtoList) {
                tnx.setMdaName(GetMdaName(tnx.getMdaId()));
            }
            return transactionDtoList;
        } catch (Exception e){
            throw new Exception(e.getMessage());
        } finally {
            session.commit();
            session.close();
            close(session);
        }
    }

    @Override
    public List<AdminTransactionDto> AllProjectTransactionsDownload(int projectId, String sortBy, boolean applyFilter, String filterBy, String filterValue, Date startDate, Date endDate) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            int startFrom = 0;
            PageFinder posFind = new PageFinder();
            posFind.from = startFrom;
            posFind.recordsPerPage = startFrom;
            posFind.orderBy = sortBy;
            posFind.businessId = projectId;
            posFind.filter = applyFilter? 1 : 0;
            posFind.filterBy = filterBy;
            posFind.filterValue = filterValue;
            posFind.startTransactionDate = new Timestamp(startDate.getTime());
            posFind.endTransactionDate = new Timestamp(endDate.getTime());

            List<AdminTransactionDto> transactionDtoList = session.selectList("Transaction.AllProjectTransactionsPaged", posFind);
            for (AdminTransactionDto tnx:
                    transactionDtoList) {
                tnx.setMdaName(GetMdaName(tnx.getMdaId()));
            }
            return transactionDtoList;
        } catch (Exception e){
            throw new Exception(e.getMessage());
        } finally {
            session.commit();
            session.close();
            close(session);
        }
    }

    @Override
    public List<TransactionDto> AllMdaTransactionsDownload(int mdaId, int projectId, String sortBy, boolean applyFilter, String filterBy, String filterValue, Date startDate, Date endDate) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            int startFrom = 0;
            PageFinder posFind = new PageFinder();
            posFind.from = startFrom;
            posFind.recordsPerPage = startFrom;
            posFind.orderBy = sortBy;
            posFind.mdaId = mdaId;
            posFind.businessId = projectId;
            posFind.filter = applyFilter? 1 : 0;
            posFind.filterBy = filterBy;
            posFind.filterValue = filterValue;
            posFind.startTransactionDate = new Timestamp(startDate.getTime());
            posFind.endTransactionDate = new Timestamp(endDate.getTime());


//            System.out.println(posFind.filter+"----"+posFind.filterBy+"-----"+filterValue);

            List<TransactionDto> transactionDtoList = session.selectList("Transaction.AllMdaTransactionsPaged", posFind);
            for (TransactionDto tnx:
                    transactionDtoList) {
                tnx.setMdaName(GetMdaName(tnx.getMdaId()));
            }

            return transactionDtoList;
        } catch (Exception e){
            throw new Exception(e.getMessage());
        } finally {
            session.commit();
            session.close();
            close(session);
        }
    }

    @Override
    public List<TransactionDto> AllAgentTransactionsDownload(int AgentId, int mdaId, int projectId, String sortBy, boolean applyFilter, String filterBy, String filterValue, Date startDate, Date endDate) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            int startFrom = 0;
            PageFinder posFind = new PageFinder();
            posFind.from = startFrom;
            posFind.recordsPerPage = startFrom;
            posFind.orderBy = sortBy;
            posFind.mdaId = mdaId;
            posFind.businessId = projectId;
            posFind.filter = applyFilter? 1 : 0;
            posFind.filterBy = filterBy;
            posFind.filterValue = filterValue;
            posFind.startTransactionDate = new Timestamp(startDate.getTime());
            posFind.endTransactionDate = new Timestamp(endDate.getTime());
            posFind.agentId = AgentId;


//            System.out.println(posFind.filter+"----"+posFind.filterBy+"-----"+filterValue);

            List<TransactionDto> transactionDtoList = session.selectList("Transaction.AllMdaTransactionsPaged", posFind);
            for (TransactionDto tnx:
                    transactionDtoList) {
                tnx.setMdaName(GetMdaName(tnx.getMdaId()));
            }

            return transactionDtoList;
        } catch (Exception e){
            throw new Exception(e.getMessage());
        } finally {
            session.commit();
            session.close();
            close(session);
        }
    }

    @Override
    public List<AdminTransactionDto> SearchTransactions(String search, String sortBy) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {

            Transaction md = new Transaction();
            md.transactionId = "%"+search+"%";
            md.transactionReference = "%"+search+"%";
            md.transactionDescription = "%"+search+"%";
            md.customerName = "%"+search+"%";
            md.customerEmail = "%"+search+"%";
            md.customerPhone = "%"+search+"%";

            //sort criteria
//            sortBy = sortBy.isEmpty() ? null : sortBy;
//            md.setSortBy(sortBy);
//            return session.selectList("Transaction.SearchTransactions", md);
            //            for (AdminTransactionDto tnx:
//                    transactionDtoList) {
//                tnx.setMdaName(GetMdaName(tnx.getMdaId()));
//            }
            return session.selectList("Transaction.SearchTransactions", md);

        } catch (NullPointerException e){
            return null;
        } catch (Exception e){
            throw new Exception(e.getMessage());
        } finally {
            session.commit();
            session.close();
            close(session);
        }
    }
    @Override
    public List<TransactionDto> SearchAgentTransactions(int AgentId, int MdaId, int projectId, String search, String sortBy) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {

            Transaction md = new Transaction();
            md.transactionId = "%"+search+"%";
            md.transactionReference = "%"+search+"%";
            md.transactionDescription = "%"+search+"%";
            md.customerName = "%"+search+"%";
            md.customerEmail = "%"+search+"%";
            md.customerPhone = "%"+search+"%";
            md.mdaId = MdaId;
            md.businessId = projectId;
            md.createdBy = AgentId;

            //sort criteria
//            sortBy = sortBy.isEmpty() ? null : sortBy;
//            md.setSortBy(sortBy);
//            return session.selectList("Transaction.SearchMdaTransactions", md);
            List<TransactionDto> transactionDtoList = session.selectList("Transaction.SearchMdaTransactions", md);
            for (TransactionDto tnx:
                    transactionDtoList) {
                tnx.setMdaName(GetMdaName(tnx.getMdaId()));
            }
            return transactionDtoList;

        } catch (NullPointerException e){
            return null;
        } catch (Exception e){
            throw new Exception(e.getMessage());
        } finally {
            session.commit();
            session.close();
            close(session);
        }
    }

    @Override
    public List<TransactionDto> SearchMdaTransactions(int MdaId, int projectId, String search, String sortBy) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {

            Transaction md = new Transaction();
            md.transactionId = "%"+search+"%";
            md.transactionReference = "%"+search+"%";
            md.transactionDescription = "%"+search+"%";
            md.customerName = "%"+search+"%";
            md.customerEmail = "%"+search+"%";
            md.customerPhone = "%"+search+"%";
            md.mdaId = MdaId;
            md.businessId = projectId;

            //sort criteria
//            sortBy = sortBy.isEmpty() ? null : sortBy;
//            md.setSortBy(sortBy);
//            return session.selectList("Transaction.SearchMdaTransactions", md);
            List<TransactionDto> transactionDtoList = session.selectList("Transaction.SearchMdaTransactions", md);
//            for (TransactionDto tnx:
//                    transactionDtoList) {
//                tnx.setMdaName(GetMdaName(tnx.getMdaId()));
//            }
            return transactionDtoList;

        } catch (NullPointerException e){
            return null;
        } catch (Exception e){
            throw new Exception(e.getMessage());
        } finally {
            session.commit();
            session.close();
            close(session);
        }
    }

    @Override
    public List<TransactionDto> SearchProjectTransactions(int projectId, String search, String sortBy) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {

            Transaction md = new Transaction();
            md.transactionId = "%"+search+"%";
            md.transactionReference = "%"+search+"%";
            md.transactionDescription = "%"+search+"%";
            md.customerName = "%"+search+"%";
            md.customerEmail = "%"+search+"%";
            md.customerPhone = "%"+search+"%";
            md.businessId = projectId;

            //sort criteria
//            sortBy = sortBy.isEmpty() ? null : sortBy;
//            md.setSortBy(sortBy);
//            return session.selectList("Transaction.SearchProjectTransactions", md);
            List<TransactionDto> transactionDtoList = session.selectList("Transaction.SearchProjectTransactions", md);
//            for (TransactionDto tnx:
//                    transactionDtoList) {
//                tnx.setMdaName(GetMdaName(tnx.getMdaId()));
//            }
            return transactionDtoList;

        } catch (NullPointerException e){
            return null;
        } catch (Exception e){
            throw new Exception(e.getMessage());
        } finally {
            session.commit();
            session.close();
            close(session);
        }
    }

    @Override
    public AdminTransactionDto SingleTransactions(String id) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
//            return session.selectOne("Transaction.SingleTransactionAdmin", id);
            //            transactionDto.setMdaName(GetMdaName(transactionDto.getMdaId()));
            return session.selectOne("Transaction.SingleTransactionAdmin", id);

        } catch (NullPointerException e){
            return null;
        } catch (Exception e){
            throw new Exception(e.getMessage());
        } finally {
            session.commit();
            session.close();
            close(session);
        }
    }


    @Override
    public TransactionDto SingleProjectTransactions(String id, int projectId) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {

            Transaction md = new Transaction();
            md.transactionId = id;
            md.businessId = projectId;
//            return session.selectOne("Transaction.SingleProjectTransactions", md);

            //            transactionDto.setMdaName(GetMdaName(transactionDto.getMdaId()));
            return session.selectOne("Transaction.SingleProjectTransactions", md);

        } catch (Exception e){
            throw new Exception(e.getMessage());
        } finally {
            session.commit();
            session.close();
            close(session);
        }
    }


    @Override
    public TransactionDto SingleMdaTransactions(String id, int projectId, int MdaId) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {

            Transaction md = new Transaction();
            md.transactionId = id;
            md.businessId = projectId;
            md.mdaId = MdaId;
//            return session.selectOne("Transaction.SingleMdaTransactions", md);


            //            transactionDto.setMdaName(GetMdaName(transactionDto.getMdaId()));
            return session.selectOne("Transaction.SingleMdaTransactions", md);
        } catch (Exception e){
            throw new Exception(e.getMessage());
        } finally {
            session.commit();
            session.close();
            close(session);
        }
    }

    @Override
    public TransactionDto SingleAgentTransactions(String id, int projectId, int MdaId, int AgentId) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {

            Transaction md = new Transaction();
            md.transactionId = id;
            md.businessId = projectId;
            md.mdaId = MdaId;
            md.createdBy = AgentId;
//            return session.selectOne("Transaction.SingleMdaTransactions", md);

            TransactionDto transactionDto = session.selectOne("Transaction.SingleMdaTransactions", md);
            transactionDto.setMdaName(GetMdaName(transactionDto.getMdaId()));
            return transactionDto;
        } catch (Exception e){
            throw new Exception(e.getMessage());
        } finally {
            session.commit();
            session.close();
            close(session);
        }
    }

    //////------ work here ------/////
    @Override
    public double AllTransactionAmount() throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
//            int year = Year.now().getValue();
//            int year = Calendar.getInstance().get(Calendar.YEAR);

            Transaction trans = new Transaction();
            trans.transactionReference = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
            return session.selectOne("MonthlyTransactions.YTDSumAll", trans);

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
    public double AllTransactionCount() throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            Transaction trans = new Transaction();
            trans.transactionReference = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
            return session.selectOne("MonthlyTransactions.YTDCountAll", trans);

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
    public double AllProjectTransactionAmount(int projId) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            Transaction trans = new Transaction();
            trans.transactionReference = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
            trans.businessId = projId;
            return session.selectOne("MonthlyTransactions.YTDSumProject", trans);

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
    public double AllProjectTransactionCount(int projId) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            Transaction trans = new Transaction();
            trans.transactionReference = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
            trans.businessId = projId;
            return session.selectOne("MonthlyTransactions.YTDCountProject", trans);

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
    public double AllMdaTransactionAmount(int mdaId, int mdaProjId) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            Transaction trans = new Transaction();
            trans.transactionReference = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
            trans.businessId = mdaProjId;
            trans.mdaId = mdaId;
            return session.selectOne("MonthlyTransactions.YTDSumMda", trans);

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
    public double AllMdaTransactionCount(int mdaId, int mdaProjId) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            Transaction trans = new Transaction();
            trans.transactionReference = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
            trans.businessId = mdaProjId;
            trans.mdaId = mdaId;
            return session.selectOne("MonthlyTransactions.YTDCountMda", trans);

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

    ////

    @Override
    public double MonthTransactionAmount(DateTime dateTime) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
//            int year = Year.now().getValue();
//            int year = Calendar.getInstance().get(Calendar.YEAR);

            Transaction trans = new Transaction();
            trans.transactionDate = new Timestamp(dateTime.toDate().getTime());
            System.out.println("Date is: " + trans.transactionDate);
            return session.selectOne("MonthlyTransactions.MonthSumAll", trans);

        } catch (NullPointerException e){
            return 0;
        } catch (Exception e){
            System.out.println(e.getLocalizedMessage());
            e.printStackTrace();
            throw new Exception(e.getMessage());
        } finally {
            session.commit();
            session.close();
            close(session);
        }
    }

    @Override
    public double MonthTransactionCount(DateTime dateTime) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            Transaction trans = new Transaction();
            trans.transactionDate = new Timestamp(dateTime.toDate().getTime());
            return session.selectOne("MonthlyTransactions.MonthCountAll", trans);

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
    public double MonthProjectTransactionAmount(int projId, DateTime dateTime) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            Transaction trans = new Transaction();
            trans.transactionDate = new Timestamp(dateTime.toDate().getTime());
            trans.businessId = projId;
            return session.selectOne("MonthlyTransactions.MonthSumProject", trans);

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
    public double MonthProjectTransactionCount(int projId, DateTime dateTime) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            Transaction trans = new Transaction();
            trans.transactionDate = new Timestamp(dateTime.toDate().getTime());
            trans.businessId = projId;
            return session.selectOne("MonthlyTransactions.MonthCountProject", trans);

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
    public double MonthMdaTransactionAmount(int mdaId, int mdaProjId, DateTime dateTime) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            Transaction trans = new Transaction();
            trans.transactionDate = new Timestamp(dateTime.toDate().getTime());
            trans.businessId = mdaProjId;
            trans.mdaId = mdaId;
            return session.selectOne("MonthlyTransactions.MonthSumMda", trans);

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
    public double MonthMdaTransactionCount(int mdaId, int mdaProjId, DateTime dateTime) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            Transaction trans = new Transaction();
            trans.transactionDate = new Timestamp(dateTime.toDate().getTime());
            trans.businessId = mdaProjId;
            trans.mdaId = mdaId;
            return session.selectOne("MonthlyTransactions.MonthCountMda", trans);

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
    public double DayTransactionAmount(DateTime dateTime) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
//            int year = Year.now().getValue();
//            int year = Calendar.getInstance().get(Calendar.YEAR);

            Transaction trans = new Transaction();
            trans.transactionDate = new Timestamp(dateTime.toDate().getTime());
            return session.selectOne("Transaction.DaySumAll", trans);

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
    public double DayTransactionCount(DateTime today) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            Transaction trans = new Transaction();
            trans.transactionDate = new Timestamp(today.toDate().getTime());
            return session.selectOne("Transaction.DayCountAll", trans);

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
    public double DayProjectTransactionAmount(int projId, DateTime today) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            Transaction trans = new Transaction();
            trans.transactionDate = new Timestamp(today.toDate().getTime());
            trans.businessId = projId;
            return session.selectOne("Transaction.DaySumProject", trans);

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
    public double DayProjectTransactionCount(int projId, DateTime today) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            Transaction trans = new Transaction();
            trans.transactionDate = new Timestamp(today.toDate().getTime());
            trans.businessId = projId;
            return session.selectOne("Transaction.DayCountProject", trans);

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
    public double DayMdaTransactionAmount(int mdaId, int mdaProjId, DateTime today) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            Transaction trans = new Transaction();
            trans.transactionDate = new Timestamp(today.toDate().getTime());
            trans.businessId = mdaProjId;
            trans.mdaId = mdaId;
            return session.selectOne("Transaction.DaySumMda", trans);

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
    public double DayMdaTransactionCount(int mdaId, int mdaProjId, DateTime today) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            Transaction trans = new Transaction();
            trans.transactionDate = new Timestamp(today.toDate().getTime());
            trans.businessId = mdaProjId;
            trans.mdaId = mdaId;
            return session.selectOne("Transaction.DayCountMda", trans);

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
    public double AllUserTransactionAmount(int userId) throws Exception {
        return 0;
    }

    @Override
    public double AllUserTransactionCount(int userId) throws Exception {
        return 0;
    }

    @Override
    public List<MonthlyTransactionsDto> GetAllMonthlySummary() throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            return session.selectList("MonthlyTransactions.All");

        } catch (NullPointerException e){
            return null;
        } catch (Exception e){
            throw new Exception(e.getMessage());
        } finally {
            session.commit();
            session.close();
            close(session);
        }
    }

    @Override
    public List<MonthlyTransactionsDto> GetProjectMonthlySummary(int id) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            return session.selectList("MonthlyTransactions.Project", id);

        } catch (NullPointerException e){
            return null;
        } catch (Exception e){
            throw new Exception(e.getMessage());
        } finally {
            session.commit();
            session.close();
            close(session);
        }
    }

    @Override
    public List<MonthlyTransactionsDto> GetMdaMonthlySummary(int mdaProjId, int mdaId) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            Transaction trans = new Transaction();
            trans.businessId = mdaProjId;
            trans.mdaId = mdaId;
            return session.selectList("MonthlyTransactions.Mda", trans);

        } catch (NullPointerException e){
            return null;
        } catch (Exception e){
            throw new Exception(e.getMessage());
        } finally {
            session.commit();
            session.close();
            close(session);
        }
    }

    @Override
    public List<DailyTransactionsDto> DailyTransactionsSummaryList(DateTime today, String filterBy, int filterId) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            Transaction trans = new Transaction();


//            if (filterBy.equals(UserRoles.NonIndividual) || filterBy.equals(UserRoles.Individual)) assessFInd.payerId = filterId;
            if (filterBy.equals(UserRoles.SubAdmin1) || filterBy.equals(UserRoles.ProjectReport)) trans.businessId = filterId;
            if (filterBy.equals(UserRoles.SubAdmin2)) trans.mdaId = filterId;
//            if (filterBy.equals(UserRoles.SubAdmin2)) trans.mdaId = filterId;

            List<DailyTransactionsDto> dailyList= new LinkedList<DailyTransactionsDto>();

            for (int i = 0; i < 8; i++) {
                trans.transactionDate = new Timestamp(today.minusDays(i).toDate().getTime());
                DailyTransactionsDto daily = session.selectOne("Transaction.SingleDateTransaction", trans);
                if (daily.count == 0) {
//                    String[] dateString = String.valueOf(trans.transactionDate).split(" ");
                    daily.transactionDate = today.minusDays(i).toDate().getTime();
                }
                dailyList.add(daily);
            }
            return dailyList;

        } catch (NullPointerException e){
            return null;
        } catch (Exception e){
            e.printStackTrace();
            throw new Exception(e.getCause());
        } finally {
            session.commit();
            session.close();
            close(session);
        }
    }

    @Override
    public TransactionMonthlySummaryDto AltMonthMdaTransactionAmount(Integer formMdaId, Integer formProjectId, DateTime today) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            Transaction trans = new Transaction();
            trans.transactionDate = new Timestamp(today.toDate().getTime());
            trans.businessId = formProjectId;
            trans.mdaId = formMdaId;
            return session.selectOne("MonthlyTransactions.AltMonthSumMda", trans);

        } catch (NullPointerException e){
            TransactionMonthlySummaryDto tx = new TransactionMonthlySummaryDto();
            tx.count =0;
            tx.totalRev =0;
            tx.income =0;
            tx.remitted =0;

            return tx;
        } catch (Exception e){
            throw new Exception(e.getMessage());
        } finally {
            session.commit();
            session.close();
            close(session);
        }
    }

    @Override
    public TransactionMonthlySummaryDto AltMonthProjectTransactionAmount(Integer formProjectId, DateTime today) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            Transaction trans = new Transaction();
            trans.transactionDate = new Timestamp(today.toDate().getTime());
            trans.businessId = formProjectId;
            return session.selectOne("MonthlyTransactions.AltMonthSumProject", trans);

        } catch (NullPointerException e){
            return null;
        } catch (Exception e){
            throw new Exception(e.getMessage());
        } finally {
            session.commit();
            session.close();
            close(session);
        }
    }

    @Override
    public TransactionMonthlySummaryDto AltMonthTransactionAmount(DateTime today) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {

            Transaction trans = new Transaction();
            trans.transactionDate = new Timestamp(today.toDate().getTime());
            System.out.println("Date is: " + trans.transactionDate);
            return session.selectOne("MonthlyTransactions.AltMonthSumAll", trans);

        } catch (NullPointerException e){
            return null;
        } catch (Exception e){
            e.printStackTrace();
            throw new Exception(e.getCause());
        } finally {
            session.commit();
            session.close();
            close(session);
        }
    }

    @Override
    public List<TransactionChannelSummaryDto> MonthMdaTransactionAmountChannel(Integer formMdaId, Integer formProjectId, DateTime today) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            Transaction trans = new Transaction();
            trans.transactionDate = new Timestamp(today.toDate().getTime());
            trans.businessId = formProjectId;
            trans.mdaId = formMdaId;
            return session.selectList("MonthlyTransactions.MonthSumMdaChannel", trans);

        } catch (NullPointerException e){
            return null;
        } catch (Exception e){
            throw new Exception(e.getMessage());
        } finally {
            session.commit();
            session.close();
            close(session);
        }
    }

    @Override
    public List<TransactionChannelSummaryDto> MonthProjectTransactionAmountChannel(Integer formProjectId, DateTime today) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            Transaction trans = new Transaction();
            trans.transactionDate = new Timestamp(today.toDate().getTime());
            trans.businessId = formProjectId;
            return session.selectList("MonthlyTransactions.MonthSumProjectChannel", trans);

        } catch (NullPointerException e){
            return null;
        } catch (Exception e){
            throw new Exception(e.getMessage());
        } finally {
            session.commit();
            session.close();
            close(session);
        }
    }

    @Override
    public List<TransactionChannelSummaryDto> MonthTransactionAmountChannel(DateTime today) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {

            Transaction trans = new Transaction();
            trans.transactionDate = new Timestamp(today.toDate().getTime());
            System.out.println("Date is: " + trans.transactionDate);
            return session.selectList("MonthlyTransactions.MonthSumAllChannel", trans);

        } catch (NullPointerException e){
            return null;
        } catch (Exception e){
            e.printStackTrace();
            throw new Exception(e.getCause());
        } finally {
            session.commit();
            session.close();
            close(session);
        }
    }



    @Override
    public TransactionMonthlySummaryDto AltMonthTransactionAmountAgent(DateTime today, int cr) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {

            Transaction trans = new Transaction();
            trans.transactionDate = new Timestamp(today.toDate().getTime());
            trans.createdBy = cr;
            System.out.println("Date is: " + trans.transactionDate);
            return session.selectOne("MonthlyTransactions.AltMonthSumAllAgent", trans);

        } catch (NullPointerException e){
            return null;
        } catch (Exception e){
            e.printStackTrace();
            throw new Exception(e.getCause());
        } finally {
            session.commit();
            session.close();
            close(session);
        }
    }

    @Override
    public TransactionChannelSummaryDto SingleChannelMonthlySummary(int businessId, int mdaId, int officeId, int agentId, DateTime today, String channel) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {

            Transaction trans = new Transaction();
            trans.businessId = businessId;
            trans.mdaId = mdaId;
            trans.officeId = officeId;
            trans.createdBy = agentId;
            trans.transactionDate = new Timestamp(today.toDate().getTime());
            trans.monthNo = today.getMonthOfYear();
            trans.channel = channel;
            trans.yearNo = today.getYear();
            System.out.println("Date is: " + trans.yearNo);
            System.out.println("Date is: " + trans.monthNo);
            System.out.println("Date is: " + trans.createdBy);
            System.out.println("Date is: " + trans.channel);
            return session.selectOne("MonthlyTransactions.MonthSumByChannel", trans);

        } catch (NullPointerException e){
            return null;
        } catch (Exception e){
            e.printStackTrace();
            throw new Exception(e.getCause());
        } finally {
            session.commit();
            session.close();
            close(session);
        }
    }


    //    @Override
    public PaginatedDto MonthlyTransactionsSummary(int projectId, String sortBy, int pageNo, int pageSize, boolean applyFilter, String filterBy, String filterValue, Date startDate, Date endDate) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            int startFrom = 0;
            if (pageNo > 0 && pageSize > 0) {
                startFrom = (pageNo - 1) * pageSize;
            } else {
                //                pageSize = 10;
                pageSize = 0;
            }

            PageFinder posFind = new PageFinder();
            posFind.from = startFrom;
            posFind.recordsPerPage = pageSize;
            posFind.orderBy = sortBy;
            posFind.businessId = projectId;
            posFind.filter = applyFilter? 1 : 0;
            posFind.filterBy = filterBy;
            posFind.filterValue = filterValue;
            posFind.startTransactionDate = new Timestamp(startDate.getTime());
            posFind.endTransactionDate = new Timestamp(endDate.getTime());

            List<AdminTransactionDto> transactionDtoList = session.selectList("Transaction.AllProjectTransactionsPaged", posFind);
            PaginatedDto paged = new PaginatedDto();
            paged.setStatus(200);
            if (startFrom == 0) {
                startFrom = 1;
            }
//            int total = session.selectOne("Mda.getCountMda");
            int total = 0;
            double sum = 0;
            int totalFound = 0;
            double charge = 0;
            double netSum = 0;
            if(transactionDtoList.size() > 0) {
                total = session.selectOne("Transaction.CountProject", posFind);
                sum = session.selectOne("Transaction.SumProject", posFind);
                charge = session.selectOne("Transaction.SumChargeProject", posFind);
                netSum = session.selectOne("Transaction.SumNetProject", posFind);
                totalFound = transactionDtoList.size();
            }

            paged.setTotal(total);
            paged.setData(transactionDtoList);
            paged.setFrom(startFrom);
            paged.setSum(sum);
            paged.setNet_sum(netSum);
            paged.setService_charge(charge);

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
        } catch (Exception e){
            e.printStackTrace();
            throw new Exception(e.getMessage());
        } finally {
            session.commit();
            session.close();
            close(session);
        }
    }


    private String GetMdaName(int mdaId) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
        Mda mdaCheck = session.selectOne("Mda.getMdaById", mdaId);
        return mdaCheck.getName();

        } catch (NullPointerException e){
            return null;
        } catch (Exception e){
            e.printStackTrace();
            throw new Exception(e.getCause());
        } finally {
            session.commit();
            session.close();
            close(session);
        }
    };
}
