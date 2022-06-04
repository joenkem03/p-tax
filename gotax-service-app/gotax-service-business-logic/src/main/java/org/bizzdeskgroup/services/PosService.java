package org.bizzdeskgroup.services;

import org.bizzdeskgroup.Dtos.Command.ActivatePosDto;
import org.bizzdeskgroup.Dtos.Command.AddPosDto;
import org.bizzdeskgroup.Dtos.Command.AddPosMetaDto;
import org.bizzdeskgroup.Dtos.Command.UpdatePosDto;
import org.bizzdeskgroup.Dtos.Query.*;
import org.bizzdeskgroup.models.Pos;

import java.util.List;

public interface PosService {
    void CreatePos(AddPosDto addPosDto, int i) throws Exception;
    void CreatePosMeta(AddPosMetaDto addPosDto, int i) throws Exception;
    void UpdatePos(UpdatePosDto updatePosDto, int i) throws Exception;
    void Deactivate(int requestedByUserId, int posId) throws Exception;
    void Activate(int requestedByUserId, int posId) throws Exception;
    PosActivationResponse ActivateByUser(int requestedByUserId, ActivatePosDto activatePosDto) throws Exception;

    PaginatedDto AllPosMeta(String sortBy, int pageNo, int pageSize) throws Exception;
    PaginatedDto AllPos(String sortBy, int pageNo, int pageSize) throws Exception;
    Pos SinglePos(int posId) throws Exception;
    Pos PosUser(int userId) throws Exception;
    List<PosDto> SearchPos(String search, String sortBy) throws Exception;
    PaginatedDto AllMdaPos(int MdaId, String sortBy, int pageNo, int pageSize) throws Exception;
    List<PosDto> SearchMdaPos(int MdaId, String search, String sortBy) throws Exception;
    List<PosDto> SearchProjectPos(int ProjectId, String search, String sortBy) throws Exception;
//    AssessmentDto SingleAssessment(int Id);

    int CountPoses () throws Exception;
    int CountMdaPoses(int id) throws Exception;

    int CountProjectPoses(int projId) throws Exception;

    List<PosMetaDto> AllPosMetaUnPaged() throws Exception;

    PaginatedDto AllProjectPos(int parseInt, String sortBy, Integer pageNo, Integer pageSize) throws Exception;

    int CountPosesAll(int businessId, int mdaId, boolean isActive) throws Exception;

    List<PosSummaryListDto> ListPosSummary(int businessId, int mdaId, int officeId) throws Exception;
}
