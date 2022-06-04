package org.bizzdeskgroup.services;

import org.bizzdeskgroup.Dtos.Command.*;
import org.bizzdeskgroup.Dtos.Query.*;

import java.util.List;

public interface OrganisationService {
    void CreateMda(int creator, CreateMdaDto mdaDto) throws  Exception;
    void UpdateMda(int updatedBy, UpdateMdaDto mdaDto) throws Exception;
    void CreateMdaOffice(int creator, CreateMdaOfficeDto mdaOfficeDto) throws  Exception;
    void CreateMdaService(int creator, CreateMdaServiceDto mdaServiceDto) throws  Exception;
    void CreateMdaUser(CreateMdaUserDto userDto, String userEmail, int createdBy, int userId) throws  Exception;
    List<MdaDto> AllMda(int Id) throws Exception;
    List<MdaDto> SerchedMda(String search, String sortBy) throws Exception;
    List<MdaDto> SerchedProjectMda(int Id, String search, String sortBy) throws Exception;
    PaginatedDto AllMdaPaged(String sortBy, int pageNo, int pageSize) throws Exception;
    PaginatedDto AllProjectMdaPaged(int Id, String sortBy, int pageNo, int pageSize) throws Exception;
    MdaDto SingleMda(int Id) throws Exception;
    List<MdaOfficeDto> AllMdaOffice(int Id) throws Exception;
    List<MdaOfficeDto> SearcheddaOffice(int Id, String search, String sortBy) throws Exception;
    PaginatedDto AllMdaOfficePaged(int Id, String sortBy, int pageNo, int pageSize) throws Exception;
    MdaOfficeDto SingleMdaOffice(int Id) throws Exception;
    List<MdaServiceDto> AllMdaService(int Id) throws Exception;

    List<MdaServiceDto> SearchedMdaService(int Id, String search, String sortBy, boolean isAgent, int buzz) throws Exception;

    PaginatedDto AllMdaServicePage(int Id, String sortBy, int pageNo, int pageSize, boolean isAgent, int businessId) throws Exception;
    MdaServiceDto SingleMdaService(int Id) throws Exception;

    int CountOffices(int businessId, int mdaId) throws Exception;
    int CountServices(int businessId, int mdaId) throws Exception;
    int CountAgents() throws Exception;
    int CountMda() throws Exception;
    int CountMdaOffices(int id) throws Exception;
    int CountMdaServices(int id) throws Exception;
    int CountMdaAgents(int id) throws Exception;

    UserMdaDto GetMdaUser(int id) throws Exception;

    List<StateDto> GetStates() throws Exception;

    List<StateDto> SearchStates(String name) throws Exception;

    List<LgaDto> GetLgas(int id) throws Exception;

    List<LgaDto> SearchLgas(int id, String name) throws Exception;

    void CreateBusiness(int creator, CreateBusinessDto businessDto) throws Exception;

    void BusinessStatus(int updatedBy, int businessId) throws Exception;

    void ModifyBusiness(int updatedBy, UpdateBusinessDto businessDto) throws Exception;

    BusinessDto SingleBusiness(Integer clientId, boolean isInternal) throws Exception;

    List<BusinessDto> GetAllBusiness() throws Exception;

    List<BusinessDto> SearchBusiness(String search, String sortBy) throws Exception;

    PaginatedDto GetAllBusinessPaged(String sortBy, int pageNo, int pageSize) throws Exception;

    void CreateBusinessUser(int id, String email) throws  Exception;

    UserProjectDto GetProjectUser(int id) throws Exception;

    int CountProjects() throws Exception;

    int CountProjectServices(int projId) throws  Exception;

    int CountProjectOffices(int projId) throws  Exception;

    int CountProjectAgents(int projId);

    int CountProjectMda(int projId) throws  Exception;

    void UpdateMdaService(int creator, UpdateMdaServiceDto mda) throws Exception;

    void UpdateMdaOffice(int creator, UpdateMdaOfficeDto mda) throws Exception;

    void UpdatePosUser(int id, int posId, boolean isSignOutReq, int reqBy) throws Exception;

    List<LgaDto> AllGetLgas() throws Exception;

    ProjectSummary BusinessPerformance(int id) throws Exception;

    ProjectSummary AllBusinessPerformance() throws Exception;

    int CountMdaAgentsAlt(int mdaId, boolean b, boolean isActive) throws Exception ;

    PaginatedDto ListActivityLogs(String sortBy, Integer pageNo, Integer pageSize, int filterId) throws Exception;

    List<PaymentChannelDto> AllPaymentChannels() throws Exception;

    void NewPaymentChannel(String name, int createdBy) throws Exception;

    void PaymentChannelStatus(int id, int createdBy) throws Exception;

    void BusPaymentChannelStatus(int id, int createdBy) throws Exception;

    List<BusinessPaymentChannelDto> AllBusPaymentChannels(int businessId) throws Exception;

    void NewBusPaymentChannel(CreateBusPayChannelDto cbd, int createdBy) throws Exception;

//    MdaDto SingleMdaByUserId(int id);


//    int CountAdmins() throws Exception;
//    int CountEnumeration() throws Exception;
//
//    int CountMdaAdmins(int id) throws Exception;
//    int CountMdaEnumeration(int id) throws Exception;

//    int CountTransactions() throws Exception;
//    int SumTransactions() throws Exception;
//    int SumAssessments() throws Exception;
//    int CountAssessments() throws Exception;
//
//    int CountMdaTransactions(int id) throws Exception;
//    int SumMdaTransactions(int id) throws Exception;
//    int SumMdaAssessments(int id) throws Exception;
//    int CountMdaAssessments(int id) throws Exception;








//    UserDto Update(int requestedByUserId, int userId, UserUpdateDto dto);
//    void RequestActivationCode(String type);
//    void Deactivate(int requestedByUserId, int userId);
//    void Activate(int requestedByUserId, int userId);
}
