package org.bizzdeskgroup.services;

import org.bizzdeskgroup.Dtos.Command.*;
import org.bizzdeskgroup.Dtos.Query.*;
import org.joda.time.DateTime;

import java.util.Date;
import java.util.List;

public interface AssessmentService {
    ComplexTaxDto CreateAssessment(CustomerAssessmentDto userAssessmentDto, int CreatedBy, int i, int officeId) throws Exception;
    SimpleTaxDto CreateAssessmentAlt(CustomerAssessmentAltDto userAssessmentDto, int CreatedBy, int i, int officeId) throws Exception;

    void UpdateAssessment(CustomerAssessmentDto userAssessmentDto, int UpdatedBy);
    PaginatedDto AllAssessment(String sortBy, int pageNo, int pageSize) throws Exception;
    PaginatedDto AllMultiAssessment(String sortBy, int pageNo, int pageSize, String filterBy, int filterId, boolean isObjection, Date startDate, Date endDate) throws Exception;
    List<AssessmentDto> SearchAssessment(String search, String sortBy) throws Exception;
    PaginatedDto AllMdaAssessment(int MdaId, String sortBy, int pageNo, int pageSize) throws Exception;
    List<AssessmentDto> SearchMdaAssessment(int MdaId, String search, String sortBy) throws Exception;
    AssessmentDto SingleAssessment(int Id) throws Exception;

    void RequestAssessmentObjection(AssessmentObjectionRequestDto addDto, int parseInt) throws Exception;

    void SubmitAssessmentReport(AssessmentObjectionReportDto addDto, int parseInt) throws Exception;

    void ApproveDeclineAssessmentObjection(AssessmentObjectionDto addDto, int parseInt) throws Exception;

    InvoiceDto GeneratePaymentInvoice(GenerateInvoiceDto invoiceDto, int parseInt, int officeId) throws Exception;

    List<AssessmentDto> FilterSearchAssessment(String search, String sortBy, String filterBy, int filterId, boolean isObjection, boolean isSettled) throws Exception;

    PaginatedDto AllInvoice(String sortBy, Integer pageNo, Integer pageSize, String filterBy, int filterId, Date startDate, Date endDate) throws Exception;

    List<InvoiceDto> SearchInvoices(String search, String sortBy, String filterBy, int filterId) throws Exception;

    List<InvoiceDto> SearchPendingInvoices(String search, String sortBy, String filterBy, int filterId) throws Exception;

    void CreateNonIndividual(BaseNonIndividualEnumerationDto enumeration, int s, String UserEmail, int ProjectId) throws Exception;

    void CreateIndividual(BaseIndividualEnumerationDto enumeration, int s, String UserId, int ProjectId) throws Exception;

    List<IndividualDto> GetIndividual(String userId) throws Exception;

    List<NonIndividualDto> GetNonIndividual(String userId) throws Exception;

    PaginatedDto PaidInvoice(String sortBy, Integer pageNo, Integer pageSize, int filterId) throws Exception;

    PaginatedDto GetIndividualList(String sortBy, Integer pageNo, Integer pageSize, int businessId) throws Exception;

    PaginatedDto GetNonIndividualList(String sortBy, Integer pageNo, Integer pageSize, int businessId) throws Exception;

    List<IndividualDto> SearchIndividuals(String sortBy, String search, int businessId) throws Exception;

    List<NonIndividualDto> SearchNonIndividuals(String sortBy, String search, int businessId) throws Exception;

    int CountIndividuals(int businessId, int agentId) throws Exception;

    int CountNonIndividuals(int businessId, int agentId) throws Exception;

    int CountAssessments(int businessId, int mdaId, int agentId, int TaxPayerId) throws Exception;

    int CountAssessmentObjsAlt(int businessId, int mdaId, int agentId, int TaxPayerId, String filterBy) throws Exception;

    double GetAssessmentSum(int businessId, int mdaId, int agentId, int TaxPayerId) throws Exception;

    List<TransactionDto> SearchPaidInvoice(String search, String sortBy, int userId) throws Exception;

    List<AssessmentSummaryListDto> ListAssessmentSummary(int businessId, int mdaId, int officeId, Date startDate, Date endDate) throws Exception;

    List<ObjSummaryListDto> ListObjSummary(int businessId, int mdaId, int officeId, Date startDate, Date endDate) throws Exception;

    AssessmentInvoiceDto GetSingleAssessment(int id, int businessId, int mdaId, int userId) throws Exception;

    void ChangeUserDetail(NewChangeReqDto enumeration, int createdBy, int ProjectId) throws Exception;

    PaginatedDto GetChangeReq(String sortBy, Integer pageNo, Integer pageSize, int businessId) throws Exception;

    void UpdateChangeReq(UpdateChangeReqDto enumeration, int createdBy, int ProjectId) throws Exception;

    int CountChangeReq(int filterId) throws Exception;

    TransactionMonthlySummaryDto PayerPaidInvoiceSum(int filterId, DateTime today) throws Exception;
}
