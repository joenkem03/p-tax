package org.bizzdeskgroup.services.impl;

import org.apache.ibatis.session.SqlSession;
import org.bizzdeskgroup.Dtos.Command.ActivatePosDto;
import org.bizzdeskgroup.Dtos.Command.AddPosDto;
import org.bizzdeskgroup.Dtos.Command.AddPosMetaDto;
import org.bizzdeskgroup.Dtos.Command.UpdatePosDto;
import org.bizzdeskgroup.Dtos.Query.*;
import org.bizzdeskgroup.factory.MyBatisConnectionFactory;
import org.bizzdeskgroup.models.ActivityLog;
import org.bizzdeskgroup.models.MdaOffice;
import org.bizzdeskgroup.models.Pos;
import org.bizzdeskgroup.models.PosMeta;
import org.bizzdeskgroup.services.PosService;

import java.util.List;

import static org.bizzdeskgroup.Helpers.NotificationMixer.DateTime;
import static org.bizzdeskgroup.Helpers.NotificationMixer.RandomNumbersString;
import static org.bizzdeskgroup.Mappers.PosMapper.NewPos;
import static org.bizzdeskgroup.Mappers.PosMapper.NewPosMeta;
import static org.eclipse.jetty.util.IO.close;

public class PosServiceImpl implements PosService {
    @Override
    public void CreatePos(AddPosDto addPosDto, int i) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try{
//            int findMdaOffice = session.selectOne("MdaOffice.getMdaOfficeByIdnMdaId", addPosDto);
//            if( findMdaOffice <= 0) throw new Exception("Mda/Mda Office mismatch");
            int Id = addPosDto.mdaOfficeId;
            MdaOffice findMdaOffice = session.selectOne("MdaOffice.getMdaOfficeById", Id);
            if( findMdaOffice != null && findMdaOffice.mdaId != addPosDto.mdaId) throw new Exception("Mda/Mda Office mismatch");
            Pos posCheck = session.selectOne("Pos.getPosByPosImeiOrSerial", addPosDto);
            // check if pos exists
            if (posCheck != null)
                throw new Exception("Duplicate Entry");
            int total = session.selectOne("Pos.getCountPos");
            Pos newPos = NewPos(addPosDto);
            newPos.setPosDeviceSerial(addPosDto.terminalId);
            newPos.setCreatedBy(i);
            newPos.setCreatedDate(DateTime());
//            newPos.setPosDeviceSerial("TAR00000" + (total+1));
            newPos.setActivationCode(RandomNumbersString(10, 0, 5));
            newPos.setAssigned(newPos.getMdaId() > 0);
            session.insert("Pos.insert", newPos);

            LogAction("New POS Terminal ","Add new POS terminal "+newPos.serialNo , i, 0, 0, "0.0.0.0");


        } catch (NullPointerException e){
//            return 0;
            throw new Exception("Error Adding POS");
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }  finally {
            session.commit();
            session.close();
            close(session);
        }

    }

    @Override
    public void CreatePosMeta(AddPosMetaDto addPosDto, int i) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try{
            PosMetaDto posCheck = session.selectOne("PosMeta.getPosMetaByByManModel", addPosDto);

            // check if pos exists
            if (posCheck != null)
                throw new Exception("Duplicate Entry");
            PosMeta newPos = NewPosMeta(addPosDto);
            newPos.setCreatedBy(i);
            newPos.setCreatedDate(DateTime());
            session.insert("PosMeta.insert", newPos);

            LogAction("New POS Template ","Add new POS template "+addPosDto.getModel(), i, 0, 0, "0.0.0.0");

        } catch (NullPointerException e){
//            return 0;
            throw new Exception("Error Adding POS Meta");
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }  finally {
            session.commit();
            session.close();
            close(session);
        }

    }

    @Override
    public void UpdatePos(UpdatePosDto updatePosDto, int i) throws Exception {
        if(updatePosDto.mdaId <= 0 && updatePosDto.mdaOfficeId <= 0) throw new Exception("Invalid Operation");
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            int Id = updatePosDto.mdaOfficeId;
            if(Id > 0 && updatePosDto.mdaId > 0) {
                MdaOffice findMdaOffice = session.selectOne("MdaOffice.getMdaOfficeById", Id);
                if (findMdaOffice != null && findMdaOffice.mdaId != updatePosDto.mdaId)
                    throw new Exception("Mda/Mda Office mismatch");
            }
            Pos pos = session.selectOne("Pos.getPosById", updatePosDto.posId);
            if(pos == null) throw new Exception("Pos not found");
            pos.setMdaId(updatePosDto.mdaId > 0 ? updatePosDto.mdaId : pos.mdaId);
            pos.setMdaOfficeId(updatePosDto.mdaOfficeId > 0 ? updatePosDto.mdaOfficeId : pos.mdaOfficeId);
            pos.setUpdatedBy(i);
            pos.setUpdatedDate(DateTime());
            pos.setActivationCode(RandomNumbersString(10, 0, 5));
            pos.setActive(false);
            session.update("Pos.update", pos);

            String change = "changed ";
            change += updatePosDto.mdaId > 0 ? pos.mdaId +" to "+updatePosDto.mdaId+";" : "";
            change += updatePosDto.mdaOfficeId > 0 ? pos.mdaOfficeId+" to "+updatePosDto.mdaOfficeId+";": "";


            LogAction("Update POS Terminal ",change, i, 0, 0, "0.0.0.0");

        } catch (NullPointerException e){
//            return 0;
            throw new Exception("Error Updating POS");
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }  finally {
            session.commit();
            session.close();
            close(session);
        }
    }
//
//    @Override
//    public void UpdatePosUser(int user, int i) throws Exception {
//        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
//        try {
//            Pos pos = session.selectOne("Pos.getPosById", i);
//            if(pos == null) throw new Exception("Pos not found");
//            pos.setUpdatedBy(i);
//            pos.setLastLoggedInUser(pos.getLoggedInUser());
//            pos.setLoggedInUser(user);
//            pos.setUpdatedDate(DateTime());
//            session.update("Pos.update", pos);
//
//        } catch (NullPointerException e){
////            return 0;
//            throw new Exception("Error Updating POS");
//        } catch (Exception e){
//            throw new Exception(e.getMessage());
//        }  finally {
//            session.commit();
//            session.close();
//            close(session);
//        }
//    }

    @Override
    public void Deactivate(int requestedByUserId, int posId) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            Pos pos = session.selectOne("Pos.getPosById", posId);
            if (pos == null)
                throw new Exception("pos does not exist");
            if (!pos.isActive)
                throw new Exception("invalid operation");
            pos.setActive(false);
            pos.setActivationCode(RandomNumbersString(10, 0, 5));
            pos.setUpdatedBy(requestedByUserId);
            pos.setUpdatedDate(DateTime());
            session.update("Pos.update", pos);

            LogAction("Deactivate POS Terminal ","Deactivate POS terminal "+pos.serialNo , requestedByUserId, 0, 0, "0.0.0.0");

        } catch (NullPointerException e){
//            return 0;
            throw new Exception("POS not found");
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }  finally {
            session.commit();
            session.close();
            close(session);
        }

    }

    @Override
    public void Activate(int requestedByUserId, int posId) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            Pos pos = session.selectOne("Pos.getPosById", posId);
            if (pos == null)
                throw new Exception("pos does not exist");
            if (pos.isActive)
                throw new Exception("invalid operation");
            pos.setActive(true);
            pos.setUpdatedBy(requestedByUserId);
            pos.setUpdatedDate(DateTime());
            session.update("Pos.update", pos);

            LogAction("Activate POS Terminal ","Activate POS terminal "+pos.serialNo , requestedByUserId, 0, 0, "0.0.0.0");

        } catch (NullPointerException e){
//            return 0;
            throw new Exception("POS not found");
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }  finally {
            session.commit();
            session.close();
            close(session);
        }

    }

    @Override
    public PosActivationResponse ActivateByUser(int requestedByUserId, ActivatePosDto activatePosDto) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
//            Pos pos = session.selectOne("Pos.getPosByActivationCode", activatePosDto);
            Pos pos = session.selectOne("Pos.getPosByActivationSerial", activatePosDto);
            if (pos == null) throw new Exception("pos does not exist");
            if (pos.isActive) throw new Exception("invalid operation");
            pos.setActive(true);
            pos.setUpdatedBy(requestedByUserId);
            pos.setUpdatedDate(DateTime());
            session.update("Pos.update", pos);

            LogAction("Activate POS Terminal ","Activate POS terminal "+pos.serialNo , requestedByUserId, 0, 0, "0.0.0.0");


            PosActivationResponse activePos = new PosActivationResponse();
            activePos.posId = pos.id;
            activePos.mdaOfficeId = pos.mdaOfficeId;
            activePos.status = pos.isActive;
            activePos.mdaId = pos.mdaId;

            return activePos;

        } catch (NullPointerException e){
//            return 0;
            throw new Exception("POS not found");
        } catch (Exception e){
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }  finally {
            session.commit();
            session.close();
            close(session);
        }

    }

    @Override
    public PaginatedDto AllPosMeta(String sortBy, int pageNo, int pageSize) throws Exception {

        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            int startFrom = 0;
            if (pageNo > 0 && pageSize > 0) {
                startFrom = (pageNo - 1) * pageSize;
            } else {
                pageSize = 0;
            }

            PageFinder posMetaFind = new PageFinder();
            posMetaFind.from = startFrom;
            posMetaFind.recordsPerPage = pageSize;
            posMetaFind.orderBy = sortBy;

            List<PosMetaDto> posMetas = session.selectList("PosMeta.selectAllPosMetasPaged", posMetaFind);
            PaginatedDto paged = new PaginatedDto();
            paged.setStatus(200);
            if (startFrom == 0) {
                startFrom = 1;
            }
//            int total = session.selectOne("Mda.getCountMda");
            int total = session.selectOne("PosMeta.getCountPosMeta");
            int totalFound = posMetas.size();

            paged.setTotal(total);
            paged.setData(posMetas);
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
    public PaginatedDto AllPos(String sortBy, int pageNo, int pageSize) throws Exception {
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

            List<PosDto> pos = session.selectList("Pos.selectAllPosesPaged", posFind);
            PaginatedDto paged = new PaginatedDto();
            paged.setStatus(200);
            if (startFrom == 0) {
                startFrom = 1;
            }
//            int total = session.selectOne("Mda.getCountMda");
            int total = session.selectOne("Pos.getCountPos");
            int totalFound = pos.size();

            paged.setTotal(total);
            paged.setData(pos);
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
    public Pos SinglePos(int posId) throws Exception {
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

    @Override
    public Pos PosUser(int userId) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
//            return session.selectOne("Pos.getPosById", posId);
            return session.selectOne("Pos.getPosByUserId", userId);

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
    public List<PosDto> SearchPos(String search, String sortBy) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {

            PosDto md = new PosDto();
            md.os = "%"+search+"%";
            md.osVer = "%"+search+"%";
            md.man = "%"+search+"%";
            md.model = "%"+search+"%";
            md.posImei = "%"+search+"%";
            md.serialNo = "%"+search+"%";

            //sort criteria
//            sortBy = sortBy.isEmpty() ? null : sortBy;
//            md.setSortBy(sortBy);
            return session.selectList("Pos.searchPos", md);


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
    public PaginatedDto AllMdaPos(int mdaId, String sortBy, int pageNo, int pageSize) throws Exception {
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

            List<PosDto> pos = session.selectList("Pos.selectAllMdPosesPaged", posFind);
            PaginatedDto paged = new PaginatedDto();
            paged.setStatus(200);
            if (startFrom == 0) {
                startFrom = 1;
            }
//            int total = session.selectOne("Mda.getCountMda");
            int total = session.selectOne("Pos.getCountMdaPos", mdaId);
            int totalFound = pos.size();

            paged.setTotal(total);
            paged.setData(pos);
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
    public List<PosDto> SearchMdaPos(int MdaId, String search, String sortBy) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {

            PosDto md = new PosDto();
            md.os = "%"+search+"%";
            md.osVer = "%"+search+"%";
            md.man = "%"+search+"%";
            md.model = "%"+search+"%";
            md.posImei = "%"+search+"%";
            md.serialNo = "%"+search+"%";
            md.mdaId = MdaId;

            //sort criteria
//            sortBy = sortBy.isEmpty() ? null : sortBy;
//            md.setSortBy(sortBy);
            return session.selectList("Pos.searchMdaPos", md);


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
    public List<PosDto> SearchProjectPos(int ProjectId, String search, String sortBy) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {

            PosDto md = new PosDto();
            md.os = "%"+search+"%";
            md.osVer = "%"+search+"%";
            md.man = "%"+search+"%";
            md.model = "%"+search+"%";
            md.posImei = "%"+search+"%";
            md.serialNo = "%"+search+"%";
            md.businessId = ProjectId;

            //sort criteria
//            sortBy = sortBy.isEmpty() ? null : sortBy;
//            md.setSortBy(sortBy);
            return session.selectList("Pos.searchProjectPos", md);


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
    public int CountPoses() throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            return session.selectOne("Pos.getCountAllMdaPos");

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
    public int CountMdaPoses(int mdaId) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            return session.selectOne("Pos.getCountMdaPos", mdaId);

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
    public int CountProjectPoses(int projId) throws Exception {
        return 0;
    }

    @Override
    public List<PosMetaDto> AllPosMetaUnPaged() throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
        return session.selectList("PosMeta.selectAllPosMeta");

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
    public PaginatedDto AllProjectPos(int businessId, String sortBy, Integer pageNo, Integer pageSize) throws Exception {
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

            List<PosDto> pos = session.selectList("Pos.selectAllProPosesPaged", posFind);
            PaginatedDto paged = new PaginatedDto();
            paged.setStatus(200);
            if (startFrom == 0) {
                startFrom = 1;
            }
//            int total = session.selectOne("Mda.getCountMda");
            int total = session.selectOne("Pos.getCountProjectPos", businessId);
            int totalFound = pos.size();

            paged.setTotal(total);
            paged.setData(pos);
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
    public int CountPosesAll(int businessId, int mdaId, boolean isActive) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            PageFinder pf = new PageFinder();
            pf.businessId = businessId;
            pf.mdaId = mdaId;
            pf.status = isActive;
            return session.selectOne("Pos.getCountAllMdaPosMega", pf);

        } catch (NullPointerException e){
            e.printStackTrace();
            return 0;
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
    public List<PosSummaryListDto> ListPosSummary(int businessId, int mdaId, int officeId) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            PageFinder pf = new PageFinder();
            pf.businessId = businessId;
            pf.mdaId = mdaId;
            pf.officeId = officeId;
            pf.status = businessId == 0 && mdaId == 0 && officeId == 0;
//            pf.status = isActive;



//            PageFinder pfAlt = new PageFinder();
//            pfAlt.businessId = 0;
//            pfAlt.mdaId = 0;
//            pfAlt.officeId = 0;

            List<PosSummaryListDto> list = session.selectList("Pos.posSummaryList", pf);

            for (PosSummaryListDto item : list
                 ) {
                PageFinder pfAlt = new PageFinder();

                if(businessId > 0){
                    pfAlt.businessId = 0;
                    pfAlt.mdaId = item.mdaId;
                    pfAlt.officeId = 0;
//                    pfAlt.agentId = 0;
                }
                if(mdaId > 0){

                    pfAlt.businessId = 0;
                    pfAlt.mdaId = 0;
                    pfAlt.officeId = item.officeId;
//                    pfAlt.agentId = 0;
                }
                if(officeId > 0){

                    pfAlt.businessId = 0;
                    pfAlt.mdaId = 0;
                    pfAlt.officeId = 0;
//                    pfAlt.agentId = item.agentId;
                }
                if(businessId == 0 && mdaId == 0 && officeId == 0){
                    pfAlt.businessId = item.businessId;
                    pfAlt.mdaId = 0;
                    pfAlt.officeId = 0;
                }

                System.out.println(pfAlt.businessId);
                System.out.println(pfAlt.mdaId);
                System.out.println(pfAlt.officeId);
//                System.out.println(pfAlt.agentId);

                item.active = session.selectOne("Pos.posSummaryListAlt", pfAlt);
                item.inactive = item.total - item.active;
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
