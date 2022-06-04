package org.bizzdeskgroup.services;

import org.bizzdeskgroup.Dtos.Command.CreatePosTransactionDto;
import org.bizzdeskgroup.Dtos.Command.GenerateRemittanceDto;
import org.bizzdeskgroup.Dtos.Query.*;
import org.joda.time.DateTime;

import java.util.Date;
import java.util.List;

public interface PosTransactionService {
    PosTransactionResponseDto NewPosTransaction(int CreatedBy, CreatePosTransactionDto posTransactionDto, int project) throws Exception;
    PosTransactionResponseDto ReversePosTransaction(int CreatedBy, CreatePosTransactionDto posTransactionDto);
    PaginatedDto AgentPosTransactions(int businessId, int MdaId, int AgentId, int payerId, String sortBy, int pageNo, int pageSize, Date startDate, Date endDate) throws Exception;
    PaginatedDto MdaPosTransactions(int MdaId, String sortBy, int pageNo, int pageSize, Date startDate, Date endDate) throws Exception;
    PaginatedDto ProjectPosTransactions(int BusinessId, String sortBy, int pageNo, int pageSize, Date startDate, Date endDate) throws Exception;
    PaginatedDto AllPosTransactions(String sortBy, int pageNo, int pageSize, Date startDate, Date endDate) throws Exception;
//    PosTransactionDto SingleAgentPosTransaction(int AgentId, int MdaOfficeId, String Transaction);
    PosTransactionDto SingleAgentPosTransaction(int MdaId, String TransactionId);
    PosTransactionDto SinglePosTransaction(String TransactionId) throws Exception;
//    List<PosTransactionDto> SearchAgentPosTransaction(int AgentId, int MdaOfficeId, String sortBy, String Transaction);
    List<PosTransactionDto> SearchAgentPosTransaction(int businessId, int MdaId, int operatorId, int payerId, String sortBy, String TransactionId) throws Exception;
    List<PosTransactionDto> SearchPosTransaction(String sortBy, String TransactionId) throws Exception;


    RemittanceResponseDto NewRemittance(int CreatedBy, GenerateRemittanceDto posTransactionDto, int mdaId, int officeId) throws Exception;
    PaginatedDto AgentRemittance(int AgentId, int MdaOfficeId, String sortBy, int pageNo, int pageSize, Date startDate, Date endDate);
    PaginatedDto MdaRemittance(int MdaId, String sortBy, int pageNo, int pageSize, boolean remStatus, boolean applyFilter, Date startDate, Date endDate) throws Exception;
    PaginatedDto ProjectRemittance(int mdaId, String sortBy, int pageNo, int pageSize, boolean remStatus, boolean applyFilter, Date startDate, Date endDate) throws Exception;
    PaginatedDto AllRemittance(String sortBy, int pageNo, int pageSize, boolean remStatus, boolean applyFilter, Date startDate, Date endDate) throws Exception;
    PosTransactionDto SingleAgentRemittance(int MdaId, String TransactionId);
    List<RemittanceDto> SearchAgentRemittance(int MdaId, int AgentId, String sortBy, String search) throws Exception;
    SingleRemittanceDto SingleRemittance(String TransactionId) throws Exception;
    List<RemittanceDto> SearchRemittance(int ProjectId, int MdaId, int AgentId, String sortBy, String search) throws Exception;


    double AllAgentTransactionAmount(int agentId, int agentProjId) throws Exception;

    double AllAgentTransactionCount(int agentId, int agentProjId) throws Exception;

    List<MonthlyTransactionsDto> GetAgentMonthlySummary(int agentId, int mdaId) throws Exception;

    RemittanceResponseDto CheckAgentOutStandingRemit(int id, int posId) throws Exception;
    boolean CheckAgentOutStandingCollect(int id, String s) throws Exception;
    double MonthAgentTransactionAmount(int agentId, int mdaId, DateTime dateTime) throws Exception;
    double MonthAgentTransactionCount(int agentId, int mdaId, DateTime dateTime) throws Exception;
    double DayAgentTransactionAmount(int agentId, int mdaId, DateTime dateTime) throws Exception;
    double DayAgentTransactionCount(int agentId, int mdaId, DateTime today) throws Exception;

    AgentCash AgentCashAtHand(int agentId, int mdaId) throws Exception;
    PaginatedDto AgentCashAtHandList(String sortBy, int pageNo, int pageSize, String filterBy, int filterId) throws Exception;



    List<PosTransactionDto> AgentPosTransactionsDownload(int AgentId, int MdaId, String sortBy, int pageNo, int pageSize, Date startDate, Date endDate) throws Exception;
    List<PosTransactionDto> MdaPosTransactionsDownload(int MdaId, String sortBy, Date startDate, Date endDate) throws Exception;
    List<PosTransactionDto> ProjectPosTransactionsDownload(int BusinessId, String sortBy, Date startDate, Date endDate) throws Exception;
    List<PosTransactionDto> AllPosTransactionsDownload(String sortBy, Date startDate, Date endDate) throws Exception;


    List<RemittanceDto> AgentRemittanceDownload(int AgentId, int MdaOfficeId, String sortBy, int pageNo, int pageSize, Date startDate, Date endDate);
    List<RemittanceDto> MdaRemittanceDownload(int MdaId, String sortBy, boolean remStatus, boolean applyFilter, Date startDate, Date endDate) throws Exception;
    List<RemittanceDto> ProjectRemittanceDownload(int mdaId, String sortBy, boolean remStatus, boolean applyFilter, Date startDate, Date endDate) throws Exception;
    List<RemittanceDto> AllRemittanceDownload(String sortBy, boolean remStatus, boolean applyFilter, Date startDate, Date endDate) throws Exception;

    int CountRemittances(int businessId, int mdaId, int agentId, boolean isRemited, DateTime dateTime) throws Exception;
    double SumRemittances(int businessId, int mdaId, int agentId, boolean isRemited, DateTime dateTime) throws Exception;

    List<RemitSummaryListDto> ListRemitSummary(int businessId, int mdaId, int officeId, Date startDate, Date endDate) throws Exception;

    RemitSummaryListDto AgentRemittanceSum(int agentId, DateTime today) throws Exception;

    PaginatedDto MdaOfficeRemittance(int parseInt, String sortBy, Integer pageNo, Integer pageSize, boolean remStatus, boolean applyFilter, Date startDate, Date endDate) throws Exception;

//    List<RemitSummaryListDto> AllRemittanceSummaryList(int businessId, int mdaId, int officeId, Date startDate, Date endDate);
}
