package org.bizzdeskgroup.services;

import org.bizzdeskgroup.Dtos.Command.CreateCardWebTransactionDto;
import org.bizzdeskgroup.Dtos.Command.CreatePosCardTransactionDto;
import org.bizzdeskgroup.Dtos.Command.CreateTransactionDto;
import org.bizzdeskgroup.Dtos.Query.*;
import org.joda.time.DateTime;

import java.util.Date;
import java.util.List;

public interface TransactionService {
    CreateTransactionDto NewTransaction(CreateCardWebTransactionDto posTransactionDto, int userId, int businessId) throws Exception;

    CreateTransactionDto NewTestTransaction(CreateCardWebTransactionDto posTransactionDto, int userId, int businessId) throws Exception;

    void NewPosCardTransaction(CreatePosCardTransactionDto transactionDto, int userId, int businessId) throws Exception;

    PaginatedDto AllTransactions(String sortBy, int pageNo, int pageSize, boolean applyFilter, String filterBy, String filterValue, Date startDate, Date endDate) throws Exception;
    PaginatedDto AllProjectTransactions(int projectId, String sortBy, int pageNo, int pageSize, boolean applyFilter, String filterBy, String filterValue, Date startDate, Date endDate) throws Exception;
    PaginatedDto AllMdaTransactions(int mdaId, int projectId, String sortBy, int pageNo, int pageSize, boolean applyFilter, String filterBy, String filterValue, Date startDate, Date endDate) throws Exception;
    PaginatedDto AllAgentTransactions(int AgentId, int mdaId, int projectId, String sortBy, int pageNo, int pageSize, boolean applyFilter, String filterBy, String filterValue, Date startDate, Date endDate) throws Exception;


    List<AdminTransactionDto> AllTransactionsDownload(String sortBy, boolean applyFilter, String filterBy, String filterValue, Date startDate, Date endDate) throws Exception;
    List<AdminTransactionDto> AllProjectTransactionsDownload(int projectId, String sortBy, boolean applyFilter, String filterBy, String filterValue, Date startDate, Date endDate) throws Exception;
    List<TransactionDto> AllMdaTransactionsDownload(int mdaId, int projectId, String sortBy, boolean applyFilter, String filterBy, String filterValue, Date startDate, Date endDate) throws Exception;
    List<TransactionDto> AllAgentTransactionsDownload(int AgentId, int mdaId, int projectId, String sortBy, boolean applyFilter, String filterBy, String filterValue, Date startDate, Date endDate) throws Exception;

    List<AdminTransactionDto> SearchTransactions(String search, String sortBy) throws Exception;
    List<TransactionDto> SearchMdaTransactions(int MdaId, int projectId, String search, String sortBy) throws Exception;
    List<TransactionDto> SearchAgentTransactions(int AgentId, int MdaId, int projectId, String search, String sortBy) throws Exception;
    List<TransactionDto> SearchProjectTransactions(int projectId, String search, String sortBy) throws Exception;

    AdminTransactionDto SingleTransactions(String id) throws Exception;
    TransactionDto SingleProjectTransactions(String id, int projectId) throws Exception;
    TransactionDto SingleMdaTransactions(String id, int projectId, int MdaId) throws Exception;
    TransactionDto SingleAgentTransactions(String id, int projectId, int MdaId, int AgentId) throws Exception;

    double AllTransactionAmount() throws Exception;

    double AllTransactionCount() throws Exception;

    double AllProjectTransactionAmount(int projId) throws Exception;

    double AllProjectTransactionCount(int projId) throws Exception;

    double AllMdaTransactionAmount(int mdaId, int mdaProjId) throws Exception;

    double AllMdaTransactionCount(int mdaId, int mdaProjId) throws Exception;


    double MonthTransactionAmount(DateTime dateTime) throws Exception;

    double MonthTransactionCount(DateTime dateTime) throws Exception;

    double MonthProjectTransactionAmount(int projId, DateTime dateTime) throws Exception;

    double MonthProjectTransactionCount(int projId, DateTime dateTime) throws Exception;

    double MonthMdaTransactionAmount(int mdaId, int mdaProjId, DateTime dateTime) throws Exception;

    double MonthMdaTransactionCount(int mdaId, int mdaProjId, DateTime dateTime) throws Exception;


    double DayTransactionAmount(DateTime dateTime) throws Exception;

    double DayTransactionCount(DateTime today) throws Exception;

    double DayProjectTransactionAmount(int projId, DateTime today) throws Exception;

    double DayProjectTransactionCount(int projId, DateTime today) throws Exception;

    double DayMdaTransactionAmount(int mdaId, int mdaProjId, DateTime today) throws Exception;

    double DayMdaTransactionCount(int mdaId, int mdaProjId, DateTime today) throws Exception;


    double AllUserTransactionAmount(int userId) throws Exception;

    double AllUserTransactionCount(int userId) throws Exception;

    List<MonthlyTransactionsDto> GetAllMonthlySummary() throws Exception;

    List<MonthlyTransactionsDto> GetProjectMonthlySummary(int projId) throws Exception;

    List<MonthlyTransactionsDto> GetMdaMonthlySummary(int mdaProjId, int mdaId) throws Exception;

    List<DailyTransactionsDto> DailyTransactionsSummaryList(DateTime today, String filterBy, int filterId) throws Exception;

    TransactionMonthlySummaryDto AltMonthMdaTransactionAmount(Integer formMdaId, Integer formProjectId, DateTime today)throws Exception;

    TransactionMonthlySummaryDto AltMonthProjectTransactionAmount(Integer formProjectId, DateTime today)throws Exception;

    TransactionMonthlySummaryDto AltMonthTransactionAmount(DateTime today)throws Exception;


    List<TransactionChannelSummaryDto> MonthMdaTransactionAmountChannel(Integer formMdaId, Integer formProjectId, DateTime today)throws Exception;

    List<TransactionChannelSummaryDto> MonthProjectTransactionAmountChannel(Integer formProjectId, DateTime today)throws Exception;

    List<TransactionChannelSummaryDto> MonthTransactionAmountChannel(DateTime today)throws Exception;

    TransactionMonthlySummaryDto AltMonthTransactionAmountAgent(DateTime today, int cr) throws Exception;

    TransactionChannelSummaryDto SingleChannelMonthlySummary(int businessId, int mdaId, int officeId, int agentId, DateTime today, String channel) throws Exception;
}
