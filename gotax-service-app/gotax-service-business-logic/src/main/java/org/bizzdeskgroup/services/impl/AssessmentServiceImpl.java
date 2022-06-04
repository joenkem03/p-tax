package org.bizzdeskgroup.services.impl;

import org.apache.ibatis.session.SqlSession;
import org.bizzdeskgroup.Dtos.Command.*;
import org.bizzdeskgroup.Dtos.Query.*;
import org.bizzdeskgroup.Helpers.ComputeTax;
import org.bizzdeskgroup.factory.MyBatisConnectionFactory;
import org.bizzdeskgroup.models.*;
import org.bizzdeskgroup.services.AssessmentService;
import org.bizzdeskgroup.services.UserService;
import org.joda.time.DateTime;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.lang.Math.abs;
import static org.bizzdeskgroup.Helpers.NotificationMixer.DateTime;
import static org.bizzdeskgroup.Helpers.NotificationMixer.RandomNumbersString;
import static org.bizzdeskgroup.Mappers.AssessmentMapper.*;
import static org.eclipse.jetty.util.IO.close;

public class AssessmentServiceImpl implements AssessmentService {
    @Override
    public ComplexTaxDto CreateAssessment(CustomerAssessmentDto userAssessmentDto, int CreatedBy, int businessId, int officeId) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try{
            //
//            double grossIncome;
//            int numberOfChildrenBelow18 = 2;
//            int numberOfDependants = 1;
            double grossIncomeAmount = abs(userAssessmentDto.grossIncomeAmount);
            double recommendedAmount = abs(userAssessmentDto.recommendedAmount);
            if(grossIncomeAmount == 0 && recommendedAmount == 0) throw new Exception("gross and recommended amount cannot be 0");
            ComplexTaxDto computedTax = new ComplexTaxDto();
            ComputeTax computeTax = new ComputeTax(grossIncomeAmount);
            if (grossIncomeAmount != 0 && grossIncomeAmount < 30000) throw new Exception("Invalid gross");
            if (grossIncomeAmount <= 300000){
                computedTax = computeTax.SimpleTax();
            } else {
                computedTax = computeTax.ComplexTax();
            }

            double taxedAmount = recommendedAmount > 0 ? recommendedAmount : computedTax.computedTax;
            Assessment newAssessment = NewAssessment(userAssessmentDto);
            newAssessment.setCreatedBy(CreatedBy);
            newAssessment.setCreatedDate(DateTime());
            newAssessment.setBusinessId(businessId);
            newAssessment.setGrossIncomeAmount(computedTax.grossIncome);
            newAssessment.setTaxAmount(taxedAmount);
//            newAssessment.setReferenceNumber("rhrhrnnsjhj");
            newAssessment.setInvoiceNumber(" ");
            newAssessment.setRecommendationDate(recommendedAmount > 0 ? DateTime() : null);

            newAssessment.setMdaOfficeId(officeId);

            session.insert("Assessment.insert", newAssessment);

            computedTax.computedTax = taxedAmount;
            computedTax.assessmentId = newAssessment.id;

            return computedTax;
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
    public SimpleTaxDto CreateAssessmentAlt(CustomerAssessmentAltDto userAssessmentDto, int CreatedBy, int businessId, int officeId) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try{

            Assessment newAssessment = NewAssessmentAlt(userAssessmentDto);
            newAssessment.setCreatedBy(CreatedBy);
            newAssessment.setCreatedDate(DateTime());
            newAssessment.setBusinessId(businessId);//////////////
            newAssessment.setGrossIncomeAmount(0);
            newAssessment.setTaxAmount(abs(userAssessmentDto.amount));
//            newAssessment.setReferenceNumber("rhrhrnnsjhj");
            newAssessment.setInvoiceNumber(" ");
            newAssessment.setRecommendationDate(null);

            newAssessment.setMdaOfficeId(officeId);


            MdaService mdaService = session.selectOne("MdaService.getMdaServiceById", newAssessment.assessedServiceId);
            if(mdaService == null) throw new Exception("Invalid MDA Service");

            newAssessment.setMdaId(mdaService.mdaId);

            session.insert("Assessment.insert", newAssessment);
            SimpleTaxDto computedTax = new SimpleTaxDto();
            computedTax.amount = newAssessment.taxAmount;
            computedTax.assessmentId = newAssessment.id;

            return computedTax;
        } catch (Exception e){
            throw new Exception(e.getMessage());
        } finally {
            session.commit();
            session.close();
            close(session);
        }
    }

    @Override
    public void UpdateAssessment(CustomerAssessmentDto userAssessmentDto, int UpdatedBy) {

    }

    @Override
    public PaginatedDto AllAssessment(String sortBy, int pageNo, int pageSize) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            int startFrom = 0;
            if (pageNo > 0 && pageSize > 0) {
                startFrom = (pageNo - 1) * pageSize;
            } else {
//                pageSize = 10;
                pageSize = 0;
            }

            PageFinder assessFInd = new PageFinder();
            assessFInd.from = startFrom;
            assessFInd.recordsPerPage = pageSize;
            assessFInd.orderBy = sortBy;

            List<AssessmentDto> assess = session.selectList("Assessment.selectAll", assessFInd);
            PaginatedDto paged = new PaginatedDto();
            paged.setStatus(200);
            if (startFrom == 0) {
                startFrom = 1;
            }
//            int total = session.selectOne("Mda.getCountMda");
            int total = session.selectOne("Assessment.getCount");
            int totalFound = assess.size();

            paged.setTotal(total);
            paged.setData(assess);
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
    public PaginatedDto AllMultiAssessment(String sortBy, int pageNo, int pageSize, String filterBy, int filterId, boolean isObjection, Date startDate, Date endDate) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            int startFrom = 0;
            if (pageNo > 0 && pageSize > 0) {
                startFrom = (pageNo - 1) * pageSize;
            } else {
                pageSize = 0;
            }

            PageFinder assessFInd = new PageFinder();
            assessFInd.from = startFrom;
            assessFInd.recordsPerPage = pageSize;
            assessFInd.orderBy = sortBy;
            assessFInd.status = isObjection;
            assessFInd.startTransactionDate = new Timestamp(startDate.getTime());
            assessFInd.endTransactionDate = new Timestamp(endDate.getTime());

            if (filterBy.equals(UserRoles.NonIndividual) || filterBy.equals(UserRoles.Individual)) assessFInd.payerId = filterId;
            if (filterBy.equals(UserRoles.SubAdmin1) || filterBy.equals(UserRoles.ProjectReport)) assessFInd.businessId = filterId;
            if (filterBy.equals(UserRoles.SubAdmin2)) assessFInd.mdaId = filterId;
            if (filterBy.equals(UserRoles.SubAdmin3)) assessFInd.officeId = filterId;
            if (filterBy.equals(UserRoles.Agent)) assessFInd.agentId = filterId;
            if (filterBy.equals(UserRoles.Admin) && filterId > 0) assessFInd.businessId = filterId;

            List<AssessmentDto> assess = session.selectList("Assessment.selectAllMulti", assessFInd);
            for (AssessmentDto ase:
                 assess) {
                if(ase.assessedBy != null && Integer.parseInt(ase.assessedBy) > 0) {
                    ase.assessedBy = GetUser(Integer.parseInt(ase.assessedBy));
                }
                if(ase.objectionAuthorizedBy != null && Integer.parseInt(ase.objectionAuthorizedBy) > 0) {
                    ase.objectionAuthorizedBy = GetUser(Integer.parseInt(ase.objectionAuthorizedBy));
                }
//                ase.amountPaid = 0;
//                try {
//                    ase.amountPaid = session.selectOne("Invoice.PaidAssessmentInvoice", ase.id);
//                } catch (Exception e){
//                    e.printStackTrace();
//                }
                ase.balance = ase.taxAmount - ase.amountPaid;
                ase.isSettled = ase.taxAmount <= ase.amountPaid;
            }
            PaginatedDto paged = new PaginatedDto();
            paged.setStatus(200);
            if (startFrom == 0) {
                startFrom = 1;
            }
//            int total = session.selectOne("Mda.getCountMda");
            int total = assess.size() > 0 ? session.selectOne("Assessment.getCountMultiZ", assessFInd) : 0;
            double sum = assess.size() > 0 ? session.selectOne("Assessment.getSumMultiZ", assessFInd) : 0;
            int totalFound = assess.size();

            paged.setTotal(total);
            paged.setData(assess);
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
    public List<AssessmentDto> SearchAssessment(String search, String sortBy) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {

            AssessmentDto as = new AssessmentDto();
//            as.invoiceNumber = "%"+search+"%";
            as.payerFirstName = "%"+search+"%";
            as.payerLastName = "%"+search+"%";
            as.payerTin = "%"+search+"%";
            as.assesedService = "%"+search+"%";
            as.assesedServiceCode = "%"+search+"%";

            //sort criteria
//            sortBy = sortBy.isEmpty() ? null : sortBy;
//            as.setSortBy(sortBy);
//            return session.selectList("Assessment.search", as);
            List<AssessmentDto> assess = session.selectList("Assessment.search", as);
            for (AssessmentDto ase:
                    assess) {
                if(ase.assessedBy != null && Integer.parseInt(ase.assessedBy) > 0) {
                    ase.assessedBy = GetUser(Integer.parseInt(ase.assessedBy));
                }
                if(ase.objectionAuthorizedBy != null && Integer.parseInt(ase.objectionAuthorizedBy) > 0) {
                    ase.objectionAuthorizedBy = GetUser(Integer.parseInt(ase.objectionAuthorizedBy));
                }
            }
            return assess;

        } catch (Exception e){
            throw new Exception(e.getMessage());
        } finally {
            session.commit();
            session.close();
            close(session);
        }
    }

    @Override
    public PaginatedDto AllMdaAssessment(int MdaId, String sortBy, int pageNo, int pageSize) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            int startFrom = 0;
            if (pageNo > 0 && pageSize > 0) {
                startFrom = (pageNo - 1) * pageSize;
            } else {
                pageSize = 0;
            }

            PageFinder assessFInd = new PageFinder();
            assessFInd.from = startFrom;
            assessFInd.recordsPerPage = pageSize;
            assessFInd.orderBy = sortBy;
            assessFInd.mdaId = MdaId;

            List<AssessmentDto> assess = session.selectList("Assessment.selectAllMda", assessFInd);
            PaginatedDto paged = new PaginatedDto();
            paged.setStatus(200);
            if (startFrom == 0) {
                startFrom = 1;
            }
            int total = session.selectOne("Assessment.getCountMda");
            int totalFound = assess.size();

            paged.setTotal(total);
            paged.setData(assess);
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
    public List<AssessmentDto> SearchMdaAssessment(int MdaId, String search, String sortBy) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {

            AssessmentDto as = new AssessmentDto();
//            as.invoiceNumber = "%"+search+"%";
            as.payerFirstName = "%"+search+"%";
            as.payerLastName = "%"+search+"%";
            as.payerTin = "%"+search+"%";
            as.assesedService = "%"+search+"%";
            as.assesedServiceCode = "%"+search+"%";
//            as.AssesedService = "%"+search+"%";
            as.mdaId = MdaId;

            //sort criteria
//            sortBy = sortBy.isEmpty() ? null : sortBy;
//            as.setSortBy(sortBy);
            return session.selectList("Assessment.search", as);

        } catch (Exception e){
            throw new Exception(e.getMessage());
        } finally {
            session.commit();
            session.close();
            close(session);
        }
    }

    @Override
    public AssessmentDto SingleAssessment(int Id) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            return session.selectOne("Assessment.single", Id);
        } catch (Exception e){
            throw new Exception(e.getMessage());
        } finally {
            session.commit();
            session.close();
            close(session);
        }
    }

    @Override
    public void RequestAssessmentObjection(AssessmentObjectionRequestDto addDto, int parseInt) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            Assessment assessment = session.selectOne("Assessment.singleAssess", addDto.getAssessmentId());
            if (assessment == null) throw new Exception("Assessment not found");
            assessment.setObjectionRequest(true);
            assessment.setObjectionReason(addDto.getObjectionReason());
            assessment.setUpdatedBy(parseInt);
            assessment.setUpdatedDate(DateTime());
            session.update("Assessment.update", assessment);
        } catch (Exception e){
            throw new Exception(e.getMessage());
        } finally {
            session.commit();
            session.close();
            close(session);
        }

    }

    @Override
    public void SubmitAssessmentReport(AssessmentObjectionReportDto addDto, int parseInt) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            Assessment assessment = session.selectOne("Assessment.singleAssess", addDto.getAssessmentId());
            if (assessment == null) throw new Exception("Assessment not found");
            if (!assessment.objectionRequest) throw new Exception("Assessment has no objection request");
//            assessment.setObjectionRequest(true);
            assessment.setAssessmentOfficerRecommendation(addDto.getAssessmentOfficerRecommendation());
            assessment.setRecommendationDate(DateTime());
            assessment.setRecommendedAmount(addDto.getRecommendedAmount());
            assessment.setRecommendationBy(parseInt);
            assessment.setUpdatedBy(parseInt);
            assessment.setUpdatedDate(DateTime());
            session.update("Assessment.update", assessment);
        } catch (Exception e){
            throw new Exception(e.getMessage());
        } finally {
            session.commit();
            session.close();
            close(session);
        }

    }

    @Override
    public void ApproveDeclineAssessmentObjection(AssessmentObjectionDto addDto, int parseInt) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            Assessment assessment = session.selectOne("Assessment.singleAssess", addDto.assessmentId);
            if (assessment == null) throw new Exception("Assessment not found");
            if (!assessment.objectionRequest) throw new Exception("Assessment has no objection request");
            if (assessment.recommendationBy <= 0) throw new Exception("Assessment objection has not been inspected");
//            assessment.setObjectionRequest(true);
            assessment.setObjected(addDto.isObjected);
            assessment.setObjectionAuthorizedBy(parseInt);
            assessment.setUpdatedBy(parseInt);
            assessment.setUpdatedDate(DateTime());
            session.update("Assessment.update", assessment);
        } catch (Exception e){
            throw new Exception(e.getMessage());
        } finally {
            session.commit();
            session.close();
            close(session);
        }

    }

    @Override
    public InvoiceDto GeneratePaymentInvoice(GenerateInvoiceDto invoiceDto, int parseInt, int officeId) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try{
            System.out.println(parseInt);

            Random rand = new Random();
//            int randomNumEven = rand.nextInt(10/2) *2;
            int randomNumEven = rand.nextInt(10);
            randomNumEven = (randomNumEven % 2 != 0) ? randomNumEven + 1 : randomNumEven;

            String invId = RandomNumbersString(9, 0, 4) + randomNumEven + RandomNumbersString(9, 0, 5) ;


            Invoice newInvoice = NewInvoice(invoiceDto);
            newInvoice.setInvoiceNo(invId);
            newInvoice.setPaid(false);
            newInvoice.setCreatedBy(parseInt);
            newInvoice.setCreatedDate(DateTime());
            newInvoice.setMdaOfficeId(officeId);


            InvoiceDto invoice = ReturnInvoice(newInvoice);


            System.out.println("Assess ID :"+ invoiceDto.isAssessment);
            if(invoiceDto.isAssessment()) {
                Assessment assessment = session.selectOne("Assessment.singleAssess", invoiceDto.getAssessmentId());
                if (assessment == null) throw new Exception("Invalid Assessment");
                if(assessment.recommendedAmount > 0){
                    if(assessment.recommendedAmount < abs(invoiceDto.getInvoiceAmount())) throw new Exception("invoice amount greater than assessed amount");
                }else {
                    if (assessment.taxAmount < abs(invoiceDto.getInvoiceAmount()))
                        throw new Exception("invoice amount greater than assessed amount");
                }
                newInvoice.setUserId(assessment.payerId);
                newInvoice.setServiceId(assessment.assessedServiceId);
                invoice.setServiceId(assessment.assessedServiceId);

                System.out.println("MDA Service ID :"+ assessment.assessedServiceId);
                System.out.println("MDA Service ID :"+ assessment.assessedServiceId);
            }
            System.out.println("MDA ID :"+ newInvoice.getMdaId());
            System.out.println("MDA Service ID :"+ newInvoice.getServiceId());
            MdaService mdaService = session.selectOne("MdaService.getMdaServiceById", newInvoice.getServiceId());
            if(mdaService == null) throw new Exception("Invalid MDA Service");
//            Mda mda = session.selectOne("Mda.getMdaById", invoice.getMdaId());
            Mda mda = session.selectOne("Mda.getMdaById", mdaService.mdaId);
            if(mda == null) throw new Exception("Invalid MDA");
//            TaxPayerDto user = session.selectOne("User.getTaxPayerById", newInvoice.getUserId());
            TaxPayerDto user = session.selectOne("User.getTaxPayerById", newInvoice.getUserId());
            if(user == null) throw new Exception("User not found");

            //

            session.insert("Invoice.insert", newInvoice);

            invoice.setMda(mda.getName());
            invoice.setPayer(user.registererFirstName +" "+user.registererLastName);
            invoice.setAssesedService(mdaService.getName()+"-"+mdaService.getCode());
            invoice.setPayerEmail(user.registererEmail);
            invoice.setPayerPhone(user.registererPhone);
            invoice.setCustomerId(user.payerId);
//            invoice.setMdaId(mda.id);
//            invoice.setPayerTin(invoiceDto);


            try {
                if (user.role.equals(UserRoles.NonIndividual)) {
                    invoice.setPayerTin( user.njtbTin != null && !user.njtbTin.trim().isEmpty() ? user.njtbTin.trim() : user.ntemporaryTin.trim());
                    invoice.setTinType( user.njtbTin != null && !user.njtbTin.trim().isEmpty() ? "JTB" : "Temp.");
                }
                if (user.role.equals(UserRoles.Individual)) {
                    invoice.setPayerTin( user.ijtbTin != null && !user.ijtbTin.trim().isEmpty() ? user.ijtbTin.trim() : user.itemporaryTin.trim() );
                    invoice.setTinType( user.ijtbTin != null && !user.ijtbTin.trim().isEmpty() ? "JTB" : "Temp." );
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }

//
//            "assesedService": "Budger processing fee",
//                    "businessId": 0,
//                    "customerId": 70,
//                    "invoiceAmount": 200,
//                    "invoiceNo": "597086824176",
//                    "month": 0,
//                    "paid": false,
//                    "payerEmail": "080694527492@paysure.digi",
//                    "payerFirstName": "Jay",
//                    "payerLastName": "Jus",
//                    "payerPhone": "080694527492",
//                    "payerTin": "3002798756944",
//                    "payerType": "Individual",
//                    "serviceId": 873,
//                    "tinType": "Temp.",
//                    "year": "2017

            return invoice;
        } catch (Exception e){
            throw new Exception(e.getMessage());
        } finally {
            session.commit();
            session.close();
            close(session);
        }
    }

    @Override
    public List<AssessmentDto> FilterSearchAssessment(String search, String sortBy, String filterBy, int filterId, boolean isObjection, boolean isSettled) throws Exception {

        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {

            AssessmentInDto as = new AssessmentInDto();
//            as.invoiceNumber = "%"+search+"%";
            as.payerFirstName = "%"+search+"%";
            as.payerLastName = "%"+search+"%";
            as.payerTin = "%"+search+"%";
            as.assesedService = "%"+search+"%";
            as.assesedServiceCode = "%"+search+"%";
            as.invoiceNumber = "%"+search+"%";
            as.phone = "%"+search+"%";
            as.isObjected = isObjection;
            as.isSettled = isSettled;

            if (filterBy.equals(UserRoles.NonIndividual) || filterBy.equals(UserRoles.Individual)) as.payerId = filterId;
            if (filterBy.equals(UserRoles.SubAdmin1) || filterBy.equals(UserRoles.ProjectReport)) as.projectId = filterId;
            if (filterBy.equals(UserRoles.SubAdmin2)) as.mdaId = filterId;
            if (filterBy.equals(UserRoles.Agent)) as.assesor = filterId;///
            if (filterBy.equals(UserRoles.SubAdmin3)) as.officeId = filterId;
            if (filterBy.equals(UserRoles.Admin) && filterId > 0) as.projectId = filterId;

            //sort criteria
//            sortBy = sortBy.isEmpty() ? null : sortBy;
//            as.setSortBy(sortBy);
//            return session.selectList("Assessment.searchMulti", as);
            System.out.println(search);
            System.out.println(filterId);

            List<AssessmentDto> assess = session.selectList("Assessment.searchMulti", as);
            System.out.println(assess.size());
            for (AssessmentDto ase:
                    assess) {
                if(ase.assessedBy != null && Integer.parseInt(ase.assessedBy) > 0) {
                    ase.assessedBy = GetUser(Integer.parseInt(ase.assessedBy));
                }
                if(ase.objectionAuthorizedBy != null && Integer.parseInt(ase.objectionAuthorizedBy) > 0) {
                    ase.objectionAuthorizedBy = GetUser(Integer.parseInt(ase.objectionAuthorizedBy));
                }
//                ase.amountPaid = 0;
//                try {
//                    ase.amountPaid = session.selectOne("Invoice.PaidAssessmentInvoice", ase.id);
//                } catch (Exception e){
//                    e.printStackTrace();
//                }
                ase.balance = ase.taxAmount - ase.amountPaid;
                ase.isSettled = ase.taxAmount <= ase.amountPaid;
            }
            return assess;

        } catch (Exception e){
            throw new Exception(e.getMessage());
        } finally {
            session.commit();
            session.close();
            close(session);
        }
    }

    @Override
    public PaginatedDto AllInvoice(String sortBy, Integer pageNo, Integer pageSize, String filterBy, int filterId, Date startDate, Date endDate) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            int startFrom = 0;
            if (pageNo > 0 && pageSize > 0) {
                startFrom = (pageNo - 1) * pageSize;
            } else {
                pageSize = 0;
            }

            PageFinder assessFInd = new PageFinder();
            assessFInd.from = startFrom;
            assessFInd.recordsPerPage = pageSize;
            assessFInd.orderBy = sortBy;
            assessFInd.startTransactionDate = new Timestamp(startDate.getTime());
            assessFInd.endTransactionDate = new Timestamp(endDate.getTime());

            if (filterBy.equals(UserRoles.NonIndividual) || filterBy.equals(UserRoles.Individual)) assessFInd.payerId = filterId;
            if (filterBy.equals(UserRoles.SubAdmin1) || filterBy.equals(UserRoles.ProjectReport)) assessFInd.businessId = filterId;
            if (filterBy.equals(UserRoles.SubAdmin2)) assessFInd.mdaId = filterId;
            if (filterBy.equals(UserRoles.Agent)) assessFInd.agentId = filterId;

//            List<InvoiceDto> assess = session.selectList("Invoice.selectAllMulti", assessFInd);

            List<InvoiceTinDto> invInt = session.selectList("Invoice.selectAllMulti", assessFInd);

            List<InvoiceDto> assess = new LinkedList<InvoiceDto>();
            for (InvoiceTinDto invx:
                    invInt) {
                assess.add(ReturnFromInvoiceTin(invx));
            }

            PaginatedDto paged = new PaginatedDto();
            paged.setStatus(200);
            if (startFrom == 0) {
                startFrom = 1;
            }
//            int total = session.selectOne("Mda.getCountMda");
            int total = session.selectOne("Invoice.getCountMulti", assessFInd);
            int totalFound = assess.size();

            paged.setTotal(total);
            paged.setData(assess);
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
    public List<InvoiceDto> SearchInvoices(String search, String sortBy, String filterBy, int filterId) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {

            InvoiceIntDto as = new InvoiceIntDto();
            as.setInvoiceNo("%"+search+"%");
            as.setPayerFirstName("%"+search+"%");
            as.setPayerLastName("%"+search+"%");
            as.setMda("%"+search+"%");
//            as.assesedServiceCode = "%"+search+"%";

            if (filterBy.equals(UserRoles.NonIndividual) || filterBy.equals(UserRoles.Individual)) as.setUserId(filterId);
            if (filterBy.equals(UserRoles.SubAdmin1) || filterBy.equals(UserRoles.ProjectReport)) as.setBusinessId(filterId);
            if (filterBy.equals(UserRoles.SubAdmin2) || filterBy.equals(UserRoles.Agent)) as.setMdaId(filterId);

            //sort criteria
//            sortBy = sortBy.isEmpty() ? null : sortBy;
//            as.setSortBy(sortBy);
//            return session.selectList("Invoice.searchMulti", as);
            List<InvoiceTinDto> invInt = session.selectList("Invoice.searchMulti", as);

            List<InvoiceDto> exInv = new LinkedList<InvoiceDto>();
            for (InvoiceTinDto invx:
                    invInt) {
                exInv.add(ReturnFromInvoiceTin(invx));
            }
            return exInv;

        } catch (Exception e){
            throw new Exception(e.getMessage());
        } finally {
            session.commit();
            session.close();
            close(session);
        }
    }

    @Override
    public List<InvoiceDto> SearchPendingInvoices(String search, String sortBy, String filterBy, int filterId) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {

//            InvoiceTinDto as = new InvoiceTinDto();
            InvoiceIntDto as = new InvoiceIntDto();
            as.setInvoiceNo(search);
//            as.setUserId(Integer.parseInt(search));
//            as.setPayerLastName("%"+search+"%");
//            as.setMda(search);

            if (filterBy.equals(UserRoles.NonIndividual) || filterBy.equals(UserRoles.Individual)) as.setUserId(filterId);
            if (filterBy.equals(UserRoles.SubAdmin1) || filterBy.equals(UserRoles.ProjectReport)) as.setBusinessId(filterId);
            if (filterBy.equals(UserRoles.SubAdmin2) || filterBy.equals(UserRoles.Agent)) as.setMdaId(filterId);

            System.out.println(as.getMdaId());
            System.out.println(as.getUserId());

            //sort criteria
//            sortBy = sortBy.isEmpty() ? null : sortBy;
//            as.setSortBy(sortBy);
////            return session.selectList("Invoice.searchMultiPending", as);
//            List<InvoiceTinDto> invInt =  session.selectList("Invoice.searchMultiPending", as);
//            for (InvoiceTinDto invx:
//                 invInt) {
//                if (invx.getPayerType().equals(UserRoles.NonIndividual)) {
//                    invx.setPayerTin(invx.getNjtbTin().trim().isEmpty() ? invx.getNtemporaryTin().trim() : invx.getNjtbTin().trim());
//                }
//                if (invx.getPayerType().equals(UserRoles.Individual)) {
//                    invx.setPayerTin(invx.getIjtbTin().trim().isEmpty() ? invx.getItemporaryTin().trim() : invx.getIjtbTin().trim());
//                }
//            }

            List<InvoiceTinDto> invInt =  session.selectList("Invoice.searchMultiPending", as);
            System.out.println(invInt.size());

            List<InvoiceDto> exInv = new LinkedList<InvoiceDto>();
            for (InvoiceTinDto invx:
                    invInt) {
//                if (invx.getPayerType().equals(UserRoles.NonIndividual)) {
//                    NonIndividualDto non = session.selectOne("NonIndividual.getNonIndividual", invx.getCustomerId());
//                    invx.setPayerTin(non.jtbTin.trim().isEmpty() ? non.temporaryTin.trim() : non.jtbTin.trim());
//                }
//                if (invx.getPayerType().equals(UserRoles.Individual)) {
//                    IndividualDto in = session.selectOne("Individual.getIndividual", invx.getCustomerId());
//                    invx.setPayerTin(in.jtbTin.trim().isEmpty() ? in.temporaryTin.trim() : in.jtbTin.trim());
//                }
                exInv.add(ReturnFromInvoiceTin(invx));

            }
//            List<InvoiceDto> = ReturnFromInvoiceTin(invInt);
//            return List<InvoiceDto>(ReturnFromInvoiceTin(invInt));
            return exInv;

        } catch (Exception e){
//            e.printStackTrace();
            throw new Exception(e.getMessage());
        } finally {
            session.commit();
            session.close();
            close(session);
        }
    }


    @Override
    public PaginatedDto PaidInvoice(String sortBy, Integer pageNo, Integer pageSize, int filterId) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            int startFrom = 0;
            if (pageNo > 0 && pageSize > 0) {
                startFrom = (pageNo - 1) * pageSize;
            } else {
                pageSize = 0;
            }

            PageFinder assessFInd = new PageFinder();
            assessFInd.from = startFrom;
            assessFInd.recordsPerPage = pageSize;
            assessFInd.orderBy = sortBy;

            assessFInd.payerId = filterId;

            List<TransactionDto> assess = session.selectList("Invoice.PaidInvoice", assessFInd);
            PaginatedDto paged = new PaginatedDto();
            paged.setStatus(200);
            if (startFrom == 0) {
                startFrom = 1;
            }
//            int total = session.selectOne("Mda.getCountMda");
            int total = session.selectOne("Invoice.PaidInvoiceCount", assessFInd);
            int totalFound = assess.size();

            paged.setTotal(total);
            paged.setData(assess);
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
    public TransactionMonthlySummaryDto PayerPaidInvoiceSum(int filterId, DateTime today) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            PageFinder assessFInd = new PageFinder();
            assessFInd.startTransactionDate = new Timestamp(today.toDate().getTime());

            assessFInd.payerId = filterId;

            return session.selectOne("Invoice.PayerPaidInvoiceSummary", assessFInd);
        } catch (Exception e){
            throw new Exception(e.getMessage());
        } finally {
            session.commit();
            session.close();
            close(session);
        }
    }

    @Override
    public PaginatedDto GetIndividualList(String sortBy, Integer pageNo, Integer pageSize, int businessId) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            int startFrom = 0;
            if (pageNo > 0 && pageSize > 0) {
                startFrom = (pageNo - 1) * pageSize;
            } else {
//                pageSize = 10;
                pageSize = 0;
            }

            PageFinder assessFInd = new PageFinder();
            assessFInd.from = startFrom;
            assessFInd.recordsPerPage = pageSize;
            assessFInd.orderBy = sortBy;

            assessFInd.businessId = businessId;

            List<IndividualDto> assess = session.selectList("Individual.getIndividualList", assessFInd);
            for (IndividualDto ase:
                    assess) {
                if(ase.enumBy != null && Integer.parseInt(ase.enumBy) > 0) {
                    ase.enumBy = GetUser(Integer.parseInt(ase.enumBy));
                }
            }
            PaginatedDto paged = new PaginatedDto();
            paged.setStatus(200);
            if (startFrom == 0) {
                startFrom = 1;
            }
//            int total = session.selectOne("Mda.getCountMda");
            int total = session.selectOne("Individual.getIndividualListCount", assessFInd);
            int totalFound = assess.size();

            paged.setTotal(total);
            paged.setData(assess);
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
    public PaginatedDto GetNonIndividualList(String sortBy, Integer pageNo, Integer pageSize, int businessId) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            int startFrom = 0;
            if (pageNo > 0 && pageSize > 0) {
                startFrom = (pageNo - 1) * pageSize;
            } else {
//                pageSize = 10;
                pageSize = 0;
            }

            PageFinder assessFInd = new PageFinder();
            assessFInd.from = startFrom;
            assessFInd.recordsPerPage = pageSize;
            assessFInd.orderBy = sortBy;

            assessFInd.businessId = businessId;

            List<NonIndividualDto> assess = session.selectList("NonIndividual.getNonIndividualList", assessFInd);
            for (NonIndividualDto ase:
                    assess) {
                if(ase.enumBy != null && Integer.parseInt(ase.enumBy) > 0) {
                    ase.enumBy = GetUser(Integer.parseInt(ase.enumBy));
                }
            }
            PaginatedDto paged = new PaginatedDto();
            paged.setStatus(200);
            if (startFrom == 0) {
                startFrom = 1;
            }
//            int total = session.selectOne("Mda.getCountMda");
            int total = session.selectOne("NonIndividual.getNonIndividualListCount", assessFInd);
            int totalFound = assess.size();

            paged.setTotal(total);
            paged.setData(assess);
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
    public List<IndividualDto> SearchIndividuals(String sortBy, String search, int businessId) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            Individual taxPayers = new Individual();
            taxPayers.setBusinessId(businessId);
            taxPayers.setEmployerName(sortBy);

            taxPayers.setJtbTin("%"+search+"%");
            taxPayers.setTemporaryTin("%"+search+"%");
            taxPayers.setOccupation("%"+search+"%");
            taxPayers.setNationality("%"+search+"%");

//            return session.selectList("Individual.searchIndividualList", taxPayers);
//            List<IndividualDto> assess = session.selectList("Individual.searchIndividualList", taxPayers);
            List<IndividualDto> assess = session.selectList("Individual.searchIndividual", taxPayers);
            for (IndividualDto ase:
                    assess) {
                if(ase.enumBy != null && Integer.parseInt(ase.enumBy) > 0) {
                    ase.enumBy = GetUser(Integer.parseInt(ase.enumBy));
                }
            }
            return assess;

        } catch (Exception e){
            throw new Exception(e.getMessage());
        } finally {
            session.commit();
            session.close();
            close(session);
        }
    }

    @Override
    public List<NonIndividualDto> SearchNonIndividuals(String sortBy, String search, int businessId) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            NonIndividual taxPayers = new NonIndividual();
            taxPayers.setBusinessId(businessId);
            taxPayers.setCacRegNo(sortBy);

            taxPayers.setJtbTin("%"+search+"%");
            taxPayers.setTemporaryTin("%"+search+"%");
            taxPayers.setEmail("%"+search+"%");
            taxPayers.setPhoneNumber1("%"+search+"%");
            taxPayers.setPhoneNumber2("%"+search+"%");

//            return session.selectList("NonIndividual.searchNonIndividual", taxPayers);
            List<NonIndividualDto> assess = session.selectList("NonIndividual.searchNonIndividual", taxPayers);
            for (NonIndividualDto ase:
                    assess) {
                if(ase.enumBy != null && Integer.parseInt(ase.enumBy) > 0) {
                    ase.enumBy = GetUser(Integer.parseInt(ase.enumBy));
                }
            }
            return assess;

        } catch (Exception e){
            throw new Exception(e.getMessage());
        } finally {
            session.commit();
            session.close();
            close(session);
        }
    }

    @Override
    public int CountIndividuals(int businessId, int agentId) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            PageFinder assessFInd = new PageFinder();

            assessFInd.agentId = agentId;
            assessFInd.businessId = businessId;
//            System.out.println("bizzzz : "+businessId);
            return session.selectOne("Individual.getIndividualListCount", assessFInd);
        } catch (NullPointerException e){
            e.printStackTrace();
            return 0;
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
    public int CountNonIndividuals(int businessId, int agentId) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            PageFinder assessFInd = new PageFinder();

            assessFInd.agentId = agentId;
            assessFInd.businessId = businessId;
            return session.selectOne("NonIndividual.getNonIndividualListCount", assessFInd);
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
    public int CountAssessments(int businessId, int mdaId, int agentId, int TaxPayerId) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            PageFinder pg = new PageFinder();
            pg.startTransactionDate = null;
            pg.businessId = businessId;
            pg.mdaId = mdaId;
            pg.agentId = agentId;
            pg.payerId = TaxPayerId;
//            pg.status = isPaid;

            return session.selectOne("Assessment.CountAssessmentMonthly", pg);
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
    public int CountAssessmentObjsAlt(int businessId, int mdaId, int agentId, int TaxPayerId, String filterBy) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            PageFinder pg = new PageFinder();
            pg.businessId = businessId;
            pg.mdaId = mdaId;
            pg.agentId = agentId;
            pg.payerId = TaxPayerId;
            pg.filterBy = filterBy;

            return session.selectOne("Assessment.CountAssessmentObjMonthly", pg);
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
    public double GetAssessmentSum(int businessId, int mdaId, int agentId, int TaxPayerId) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            PageFinder pg = new PageFinder();
            pg.startTransactionDate = null;
            pg.businessId = businessId;
            pg.mdaId = mdaId;
            pg.agentId = agentId;
            pg.payerId = TaxPayerId;
//            pg.status = isPaid;

            return session.selectOne("Assessment.SumAssessmentMonthly", pg);
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
    public List<TransactionDto> SearchPaidInvoice(String search, String sortBy, int userId) throws Exception {

        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
        InvoiceIntDto as = new InvoiceIntDto();
        as.setInvoiceNo("%"+search+"%");
//        as.setPayerFirstName("%"+search+"%");
//        as.setPayerLastName("%"+search+"%");
//        as.setMda("%"+search+"%");
//            as.assesedServiceCode = "%"+search+"%";

        return session.selectList("Invoice.SearchPaidInvoice", as);

        } catch (Exception e){
            throw new Exception(e.getMessage());
        } finally {
            session.commit();
            session.close();
            close(session);
        }
    }

    @Override
    public void CreateNonIndividual(BaseNonIndividualEnumerationDto enumeration, int createdBy, String UserEmail, int ProjectId) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {

            int UserId = createdBy;
//            User user = session.selectOne("User.getUserByPhoneOrEmail", userEmail);
            if (!UserEmail.trim().isEmpty()) {
                User user = session.selectOne("User.getUserByEmail", UserEmail);
                UserId = user.id;
            }

            NonIndividual newNonIndividual = NewNonIndividual(enumeration);
            newNonIndividual.setCreatedBy(createdBy);
            newNonIndividual.setCreatedDate(DateTime());
            newNonIndividual.setPayerId(UserId);
            newNonIndividual.setBusinessId(ProjectId);

            session.insert("NonIndividual.Create", newNonIndividual);

        } catch (Exception e){
            throw new Exception(e.getMessage());
        } finally {
            session.commit();
            session.close();
            close(session);
        }

    }



    @Override
    public List<NonIndividualDto> GetNonIndividual(String payerId) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {

//            return session.selectList("NonIndividual.getNonIndividual", payerId);
            List<NonIndividualDto> assess = session.selectList("NonIndividual.getNonIndividual", payerId);
            for (NonIndividualDto ase:
                    assess) {
                if(ase.enumBy != null && Integer.parseInt(ase.enumBy) > 0) {
                    ase.enumBy = GetUser(Integer.parseInt(ase.enumBy));
                }
            }
            return assess;

        } catch (Exception e){
            throw new Exception(e.getMessage());
        } finally {
            session.commit();
            session.close();
            close(session);
        }

    }

    @Override
    public void CreateIndividual(BaseIndividualEnumerationDto enumeration, int createdBy, String UserEmail, int ProjectId) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            int UserId = createdBy;
//            User user = session.selectOne("User.getUserByPhoneOrEmail", userEmail);
            if (!UserEmail.trim().isEmpty()) {
                User user = session.selectOne("User.getUserByEmail", UserEmail);
                UserId = user.id;
            }

            Individual newIndividual = NewIndividual(enumeration);
            newIndividual.setCreatedBy(createdBy);
            newIndividual.setCreatedDate(DateTime());
            newIndividual.setUserId(UserId);
            newIndividual.setBusinessId(ProjectId);

            session.insert("Individual.Create", newIndividual);

        } catch (Exception e){
            throw new Exception(e.getMessage());
        } finally {
            session.commit();
            session.close();
            close(session);
        }

    }

    @Override
    public List<IndividualDto> GetIndividual(String userId) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {

//            return session.selectList("Individual.getIndividual", userId);
            List<IndividualDto> assess = session.selectList("Individual.getIndividual", userId);
            for (IndividualDto ase:
                    assess) {
                if(ase.enumBy != null && Integer.parseInt(ase.enumBy) > 0) {
                    ase.enumBy = GetUser(Integer.parseInt(ase.enumBy));
                }
            }
            return assess;

        } catch (Exception e){
            throw new Exception(e.getMessage());
        } finally {
            session.commit();
            session.close();
            close(session);
        }

    }



    @Override
    public List<AssessmentSummaryListDto> ListAssessmentSummary(int businessId, int mdaId, int officeId, Date startDate, Date endDate) throws Exception {
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

            return session.selectList("Assessment.assessSummaryList", pf);

        } catch (NullPointerException e){
            e.printStackTrace();
            return null;
        } catch (Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }  finally {
            session.commit();
            session.close();
            close(session);
        }
    }

    @Override
    public List<ObjSummaryListDto> ListObjSummary(int businessId, int mdaId, int officeId, Date startDate, Date endDate) throws Exception {
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

            List<ObjSummaryListDto> list = session.selectList("Assessment.objectionSummaryList", pf);


            for (ObjSummaryListDto item : list
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

                System.out.println(pfAlt.businessId);
                System.out.println(pfAlt.mdaId);
                System.out.println(pfAlt.officeId);

                item.unapproved = session.selectOne("Assessment.objectionSummaryListAlt", pfAlt);
                item.approved = item.count - item.unapproved;
            }

            return list;

        } catch (NullPointerException e){
            e.printStackTrace();
            return null;
        } catch (Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }  finally {
            session.commit();
            session.close();
            close(session);
        }
    }

    @Override
    public AssessmentInvoiceDto GetSingleAssessment(int id, int businessId, int mdaId, int userId) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            PageFinder pf = new PageFinder();
            pf.businessId = businessId;
            pf.mdaId = mdaId;
//            pf.officeId = officeId;
            pf.payerId = userId;
            pf.id = id;

            AssessmentInvoiceDto asi = new AssessmentInvoiceDto();

            asi.setAssessment(session.selectOne("Assessment.singleAssessAlt", pf));//
            asi.setInvoices(session.selectList("Invoice.invoiceByAssessment", pf));

            return asi;

        } catch (NullPointerException e){
            e.printStackTrace();
            return null;
        } catch (Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }  finally {
            session.commit();
            session.close();
            close(session);
        }
    }


    @Override
    public void ChangeUserDetail(NewChangeReqDto enumeration, int createdBy, int ProjectId) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            User user = session.selectOne("User.getUserById", enumeration.affectedUser);

            if(user == null) throw new Exception("Invalid UserId");

            ChangeReq newChange = new ChangeReq();
            newChange.setCreatedBy(createdBy);
            newChange.setCreatedDate(DateTime());
            newChange.setAffectedUser(enumeration.affectedUser);
            newChange.setBusinessId(ProjectId);
            newChange.setChangeItem(enumeration.changeItem);
            newChange.setOldValue(enumeration.oldValue);
            newChange.setNewValue(enumeration.newValue);

            session.insert("ChangeAudit.insertChange", newChange);

            LogAction("Change Request: "+enumeration.changeItem,"Request to change "+enumeration.changeItem+" from: "+ enumeration.oldValue+" to: "+enumeration.newValue, createdBy, enumeration.affectedUser, ProjectId, "0.0.0.0");


        } catch (Exception e){
            throw new Exception(e.getMessage());
        } finally {
            session.commit();
            session.close();
            close(session);
        }

    }

    @Override
    public PaginatedDto GetChangeReq(String sortBy, Integer pageNo, Integer pageSize, int businessId) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            int startFrom = 0;
            if (pageNo > 0 && pageSize > 0) {
                startFrom = (pageNo - 1) * pageSize;
            } else {
//                pageSize = 10;
                pageSize = 0;
            }

            PageFinder assessFInd = new PageFinder();
            assessFInd.from = startFrom;
            assessFInd.recordsPerPage = pageSize;
            assessFInd.orderBy = sortBy;

            assessFInd.businessId = businessId;

            List<ChangeReqDto> assess = session.selectList("ChangeAudit.getChanges", assessFInd);

            PaginatedDto paged = new PaginatedDto();
            paged.setStatus(200);
            if (startFrom == 0) {
                startFrom = 1;
            }
//            int total = session.selectOne("Mda.getCountMda");
            int total = session.selectOne("ChangeAudit.getChangesCount", assessFInd.businessId);
            int totalFound = assess.size();

            paged.setTotal(total);
            paged.setData(assess);
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
    public void UpdateChangeReq(UpdateChangeReqDto enumeration, int createdBy, int ProjectId) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            ChangeReq req = session.selectOne("ChangeAudit.getChangesById", enumeration.requestId);

            if(req == null) throw new Exception("Invalid Request");

            req.setUpdatedBy(createdBy);
            req.setUpdatedDate(DateTime());
            req.setPending(false);
            req.setApproved(enumeration.isApproved);

            if(req.isApproved()){
                User userUpdate = session.selectOne("User.getUserById", req.getAffectedUser());
                if(userUpdate == null) throw new Exception("User not found");

                if(userUpdate.getRole().equals(UserRoles.Individual)){
                    Individual upInd = session.selectOne("Individual.GetIndUpdate", userUpdate.getId());

//                    UPDATE `taxed_paysure`.`individuals`
//SET
//`id` = <{id: }>,
//`title` = <{title: }>,
//`userId` = <{userId: }>,
//`dateOfBirth` = <{dateOfBirth: }>,
//`maritalStatus` = <{maritalStatus: }>,
//`nationality` = <{nationality: }>,
//`residentialAddress` = <{residentialAddress: }>,
//`residenceLgaId` = <{residenceLgaId: }>,
//`residenceStateId` = <{residenceStateId: }>,
//`occupation` = <{occupation: }>,
//`officeAddress` = <{officeAddress: }>,
//`employerName` = <{employerName: }>,
//`temporaryTin` = <{temporaryTin: }>,
//`jtbTin` = <{jtbTin: }>,
//`businessId` = <{businessId: }>,
//`createdBy` = <{createdBy: }>,
//`createdDate` = <{createdDate: }>,
//`updatedBy` = <{updatedBy: }>,
//`updatedDate` = <{updatedDate: }>,
//`isActive` = <{isActive: 1}>
//WHERE `id` = <{expr}>;

//                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.ENGLISH);
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

                    switch (req.getChangeItem()){
                        case "title":
                            upInd.setTitle(req.getNewValue());
                            break;
                        case "dateOfBirth":
                            upInd.setDateOfBirth(new Timestamp(formatter.parse(req.getNewValue()).getTime()));
                            break;
                        case "maritalStatus":
                            upInd.setMaritalStatus(req.getNewValue());
                            break;
                        case "nationality":
                            upInd.setNationality(req.getNewValue());
                            break;
                        case "residentialAddress":
                            upInd.setResidentialAddress(req.getNewValue());
                            break;
                        case "residenceLgaId":
                            upInd.setResidenceLgaId(Integer.parseInt(req.getNewValue()));
                            break;
                        case "residenceStateId":
                            upInd.setResidenceStateId(Integer.parseInt(req.getNewValue()));
                            break;
                        case "occupation":
                            upInd.setOccupation(req.getNewValue());
                            break;
                        case "officeAddress":
                            upInd.setOfficeAddress(req.getNewValue());
                            break;
                        case "employerName":
                            upInd.setEmployerName(req.getNewValue());
                            break;
                        case "temporaryTin":
                            upInd.setTemporaryTin(req.getNewValue());
                            break;
                        case "jtbTin":
                            upInd.setJtbTin(req.getNewValue());
                            break;
                        case "businessId":
                            upInd.setBusinessId(Integer.parseInt(req.getNewValue()));
                            userUpdate.setBusinessId(Integer.parseInt(req.getNewValue()));
                            session.update("User.update", userUpdate);
                            break;
                    }
                    upInd.setUpdatedBy(req.createdBy);
                    upInd.setUpdatedDate(DateTime());

                    session.update("Individual.Edit", upInd);
                }

                if(userUpdate.getRole().equals(UserRoles.NonIndividual)){
                    NonIndividual upNonInd = session.selectOne("NonIndividual.GetNonIndUpdate", userUpdate.getId());

                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

//                    UPDATE `taxed_paysure`.`non_individuals`
//SET
//`id` = <{id: }>,
//`companyName` = <{companyName: }>,
//`cacRegNo` = <{cacRegNo: }>,
//`companyAddress` = <{companyAddress: }>,
//`payerId` = <{payerId: }>,
//`city` = <{city: }>,
//`lgaId` = <{lgaId: }>,
//`phoneNumber1` = <{phoneNumber1: }>,
//`phoneNumber2` = <{phoneNumber2: }>,
//`email` = <{email: }>,
//`website` = <{website: }>,
//`temporaryTin` = <{temporaryTin: }>,
//`jtbTin` = <{jtbTin: }>,
//`companyRegistrationDate` = <{companyRegistrationDate: }>,
//`companyCommencementDate` = <{companyCommencementDate: }>,
//`businessType` = <{businessType: }>,
//`businessId` = <{businessId: }>,
//`createdBy` = <{createdBy: }>,
//`createdDate` = <{createdDate: }>,
//`updatedBy` = <{updatedBy: }>,
//`updatedDate` = <{updatedDate: }>,
//`isActive` = <{isActive: 1}>
//WHERE `id` = <{expr}>;

                    switch (req.getChangeItem()){
                        case "companyName":
                            upNonInd.setCompanyName(req.getNewValue());
                            break;
                        case "cacRegNo":
                            upNonInd.setCacRegNo(req.getNewValue());
                            break;
                        case "companyAddress":
                            upNonInd.setCompanyAddress(req.getNewValue());
                            break;
                        case "city":
                            upNonInd.setCity(req.getNewValue());
                            break;
                        case "payerId":
                            upNonInd.setPayerId(Integer.parseInt(req.getNewValue()));
                            break;
                        case "lgaId":
                            upNonInd.setLgaId(Integer.parseInt(req.getNewValue()));
                            break;
                        case "phoneNumber1":
                            upNonInd.setPhoneNumber1(req.getNewValue());
                            break;
                        case "phoneNumber2":
                            upNonInd.setPhoneNumber2(req.getNewValue());
                            break;
                        case "email":
                            upNonInd.setEmail(req.getNewValue());
                            break;
                        case "website":
                            upNonInd.setWebsite(req.getNewValue());
                            break;
                        case "temporaryTin":
                            upNonInd.setTemporaryTin(req.getNewValue());
                            break;
                        case "jtbTin":
                            upNonInd.setJtbTin(req.getNewValue());
                            break;
                        case "companyRegistrationDate":
                            upNonInd.setCompanyRegistrationDate(new Timestamp(formatter.parse(req.getNewValue()).getTime()));
                            break;
                        case "companyCommencementDate":
                            upNonInd.setCompanyCommencementDate(new Timestamp(formatter.parse(req.getNewValue()).getTime()));
                            break;
                        case "businessType":
                            upNonInd.setBusinessType(req.getNewValue());
                            break;
                        case "businessId":
                            upNonInd.setBusinessId(Integer.parseInt(req.getNewValue()));
                            userUpdate.setBusinessId(Integer.parseInt(req.getNewValue()));
                            session.update("User.update", userUpdate);
                            break;
                    }
                    upNonInd.setUpdatedBy(req.createdBy);
                    upNonInd.setUpdatedDate(DateTime());

                    session.update("NonIndividual.Edit", upNonInd);
                }
            }

            session.update("ChangeAudit.updateChangeReq", req);

            String change = req.isApproved() ? "Change request approved and effected" : "Change request declined";

            LogAction("Change Request: "+ req.getChangeItem(),change, createdBy, req.getAffectedUser(), ProjectId, "0.0.0.0");


        } catch (Exception e){
            throw new Exception(e.getMessage());
        } finally {
            session.commit();
            session.close();
            close(session);
        }

    }

    private String  GetUser(int id) throws Exception {
        UserService User = new UserServiceImpl();
        UserDto us = User.SingleUser(id);
        if (us != null)
            return us.firstName + " " + us.lastName;
        else
            return "";
    }



    @Override
    public int CountChangeReq(int businessId) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {

            return session.selectOne("ChangeAudit.getChangesCount", businessId);
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
    private void LogAction(String action, String actionDesc, int createdBy, int affectedUser, int businessId, String ipAddress) throws Exception{
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            ActivityLog activityLog = new ActivityLog();
            activityLog.setAction(action);
            activityLog.setCreatedBy(createdBy);
            activityLog.setBusinessId(businessId);
            activityLog.setAffectedUser(affectedUser);
            activityLog.setActionDesc(actionDesc);
            activityLog.setCreatedDate(DateTime());
            activityLog.setTimeStamp(DateTime().getTime()/1000);
            session.insert("ChangeAudit.NewActivity", activityLog);

        } catch (NullPointerException e){
//            return null;
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }  finally {
            session.commit();
            session.close();
            close(session);
        }
    }
}
