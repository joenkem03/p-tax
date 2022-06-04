package org.bizzdeskgroup.services.impl;

import org.apache.ibatis.session.SqlSession;
import org.bizzdeskgroup.Dtos.Command.*;
import org.bizzdeskgroup.Dtos.Query.*;
import org.bizzdeskgroup.factory.MyBatisConnectionFactory;
import org.bizzdeskgroup.models.*;
import org.bizzdeskgroup.services.OrganisationService;

import java.util.List;

import static org.bizzdeskgroup.Helpers.NotificationMixer.DateTime;
import static org.bizzdeskgroup.Mappers.MdaMapper.*;
import static org.eclipse.jetty.util.IO.close;

public class OrganisationServiceImpl implements OrganisationService {
    @Override
    public void CreateMda(int creator, CreateMdaDto mdaDto) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try{
            Mda mdaCheck = session.selectOne("Mda.getMdaByNameOrCode", mdaDto);

            // check if mda exists
            if (mdaCheck != null)
                throw new Exception("Duplicate Entry");
            Mda newMda = NewMda(mdaDto);
            newMda.setCreatedDate(DateTime());
            newMda.setCreatedBy(creator);
            session.insert("Mda.insert", newMda);

            LogAction("MDA Creation", "New MDA "+ mdaDto.name+" created", creator, 0, mdaDto.businessId, "0.0.0.0");


        } catch (NullPointerException e){
//            return 0;
            throw new Exception("Error adding MDA");
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }  finally {
            session.commit();
            session.close();
            close(session);
        }

    }

    @Override
    public void UpdateMda(int updatedBy, UpdateMdaDto mdaDto) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try{
            Mda mdaCheck = session.selectOne("Mda.getMdaById", mdaDto);

            // check if mda exists
            if (mdaCheck == null)
                throw new Exception("Invalid MDA");

            mdaCheck.setName(mdaDto.name.trim().isEmpty() ? mdaCheck.getName() : mdaDto.name);
            mdaCheck.setMdaCode(mdaDto.mdaCode.trim().isEmpty() ? mdaCheck.getMdaCode() : mdaDto.mdaCode);
            mdaCheck.setAbbreviation(mdaDto.abbreviation.trim().isEmpty() ? mdaCheck.getAbbreviation() : mdaDto.abbreviation);
            mdaCheck.setRetainingValue(mdaDto.retainingValue == 0 ? mdaCheck.getRetainingValue() : mdaDto.retainingValue);
            mdaCheck.setRetaining(mdaDto.isRetaining);
            mdaCheck.setRetainingByPercentage(mdaDto.isRetainingByPercentage);

            mdaCheck.setUpdatedDate(DateTime());
            mdaCheck.setUpdatedBy(updatedBy);

            session.update("Mda.update", mdaCheck);

            String change = "";
            change += mdaDto.name.trim().isEmpty() ? "" : mdaCheck.getName() +" to "+mdaDto.name+";";
            change += mdaDto.mdaCode.trim().isEmpty() ? "" : mdaCheck.getMdaCode()+" to "+mdaDto.mdaCode+";";
            change += mdaDto.abbreviation.trim().isEmpty() ? "" : mdaCheck.getAbbreviation()+" to "+mdaDto.abbreviation+";";
            change += mdaDto.retainingValue == 0 ? "": mdaCheck.getRetainingValue() +" to "+mdaDto.retainingValue+";";
            change += mdaCheck.isRetaining()+" to "+mdaDto.isRetaining+";";
            change += mdaCheck.isRetainingByPercentage() +" to "+mdaDto.isRetainingByPercentage+";";

            LogAction("Modify MDa", change, updatedBy, 0, mdaCheck.businessId, "0.0.0.0");

        } catch (NullPointerException e){
//            return 0;
            throw new Exception("Error updating MDA");
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }  finally {
            session.commit();
            session.close();
            close(session);
        }

    }


    @Override
    public void CreateMdaOffice(int creator, CreateMdaOfficeDto mdaOfficeDto) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try{

            Mda mda = session.selectOne("Mda.getMdaById", mdaOfficeDto.mdaId);
            if (mda == null)
                throw new Exception("Unknown MDA");

            mdaOfficeDto.businessId = mda.getBusinessId();


            List<MdaOffice> mdaCheck = session.selectList("MdaOffice.getMdaOfficeByNameOrCode", mdaOfficeDto);

            // check if mda exists
            if (mdaCheck.size() >= 1)
                throw new Exception("Duplicate Entry");

            MdaOffice newMdaOffice = NewMdaOffice(mdaOfficeDto);
            newMdaOffice.setCreatedDate(DateTime());
            newMdaOffice.setActive(true);
            newMdaOffice.setCreatedBy(creator);
            newMdaOffice.setBusinessId(mda.businessId);

//            System.out.println(newMdaOffice.getBusinessId());

            session.insert("MdaOffice.insert", newMdaOffice);

            LogAction("New MDA Office", "Create new office "+mdaOfficeDto.name+" under MDA ("+mda.getName()+")", creator, 0, mda.getBusinessId(), "0.0.0.0");


        } catch (NullPointerException e){
//            return 0;
            throw new Exception("Error Adding MDA Office");
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }  finally {
            session.commit();
            session.close();
            close(session);
        }
    }

    @Override
    public void CreateMdaService(int creator, CreateMdaServiceDto mdaServiceDto) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try{
            Mda mda = session.selectOne("Mda.getMdaById", mdaServiceDto.mdaId);
            if(mda == null) throw new Exception("Invalid MDA");

            mdaServiceDto.businessId = mda.getBusinessId();
            List<MdaService> mdaCheck = session.selectList("MdaService.getMdaServiceByNameOrCode", mdaServiceDto);

            // check if mda exists
            if (mdaCheck.size() >= 1)
                throw new Exception("Duplicate Entry");
            MdaService newMdaService = NewMdaService(mdaServiceDto);
            newMdaService.setCreatedDate(DateTime());
            newMdaService.setActive(true);
            newMdaService.setCreatedBy(creator);
            newMdaService.setBusinessId(mda.businessId);
            session.insert("MdaService.insert", newMdaService);

            LogAction("New MDA Service", "Create new service "+mdaServiceDto.name+" under MDA ("+mda.getName()+")", creator, 0, mda.getBusinessId(), "0.0.0.0");

        } catch (NullPointerException e){
//            return 0;
            throw new Exception("Error Adding Service");
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }  finally {
            session.commit();
            session.close();
            close(session);
        }
    }

    @Override
    public void CreateMdaUser(CreateMdaUserDto userDto, String userEmail, int createdBy, int userId) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try{
//            User user = session.selectOne("User.getUserByPhoneOrEmail", userEmail);

            Mda mda = session.selectOne("Mda.getMdaById", userDto.mdaId);
            if (mda == null) throw new Exception("invalid MDA");

            MdaOffice mdaOffice = session.selectOne("MdaOffice.getMdaOfficeByMdaId", userDto.mdaOfficeId);
            if (mdaOffice == null) throw new Exception("invalid office");

            User user = null;
            if(userId > 0) {
                user = session.selectOne("User.getUserById", userId);

                // check if mda exists
                if (user == null) throw new Exception("invalid user");
                if(user.role.equals(UserRoles.Agent) || user.role.equals(UserRoles.SubAdmin2)) {
                    MdaUser exists = session.selectOne("MdaUser.userId", user.id);
                    if(exists == null) throw new Exception("User has no MDA");
//                    MdaUser newMdaUser = NewMdaUser(userDto);
                    exists.userId = user.id;
                    exists.setUpdatedDate(DateTime());
                    exists.setCanView(false);
                    exists.setUpdatedBy(createdBy);
                    session.update("MdaUser.update", exists);

                    LogAction("Change User MDA/Office", "Add User to MDA:  "+mda.getName()+"("+mdaOffice.getName()+")", createdBy, userId, mda.getBusinessId(), "0.0.0.0");
                } else {
                    throw new Exception("User of role type cannot be added to MDA");
                }
            }else {
                user = session.selectOne("User.getUserByEmail", userEmail);

                // check if mda exists
                if (user == null) throw new Exception("invalid user");
                if(user.role.equals(UserRoles.Agent) || user.role.equals(UserRoles.SubAdmin2)) {
                    MdaUser exists = session.selectOne("MdaUser.userId", user.id);
                    if(exists != null) throw new Exception("User already assigned to MDA");
                    MdaUser newMdaUser = NewMdaUser(userDto);
                    newMdaUser.userId = user.id;
                    newMdaUser.setCreatedDate(DateTime());
                    newMdaUser.setCanView(false);
                    newMdaUser.setUpdatedBy(0);
                    newMdaUser.setUpdatedDate(null);
                    newMdaUser.setCreatedBy(createdBy);
                    session.insert("MdaUser.insert", newMdaUser);

                    LogAction("Add User to MDA/Office", "Add User to MDA:  "+mda.getName()+"("+mdaOffice.getName()+")", createdBy, userId, mda.getBusinessId(), "0.0.0.0");
                } else {
                    throw new Exception("User of role type cannot be added to MDA");
                }
            }

        } catch (NullPointerException e){
//            return 0;
            throw new Exception("Error Adding MDA User");
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }  finally {
            session.commit();
            session.close();
            close(session);
        }

    }

    @Override
    public List<MdaDto> AllMda(int Id) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try{
            //            return GetMda(mda);
            return session.selectList("Mda.getMdaById", Id);

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
    public List<MdaDto> SerchedMda(String search, String sortBy) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {

            Mda md = new Mda();
            md.setAbbreviation("%"+search+"%");
            md.setMdaCode("%"+search+"%");
            md.setName("%"+search+"%");

            //sort criteria
            sortBy = sortBy.isEmpty() ? null : sortBy;
            md.setSortBy(sortBy);
            return session.selectList("Mda.getMdasWhereLike", md);


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
    public List<MdaDto> SerchedProjectMda(int Id, String search, String sortBy) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {

            Mda md = new Mda();
            md.setAbbreviation("%"+search+"%");
            md.setMdaCode("%"+search+"%");
            md.setName("%"+search+"%");
            md.setBusinessId(Id);

            //sort criteria
            sortBy = sortBy.isEmpty() ? null : sortBy;
            md.setSortBy(sortBy);
            return session.selectList("Mda.getProjectMdasWhereLike", md);


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
    public PaginatedDto AllMdaPaged(String sortBy, int pageNo, int pageSize) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            int startFrom = 0;
            if (pageNo > 0 && pageSize > 0) {
                startFrom = (pageNo - 1) * pageSize;
            } else {
                //                pageSize = 10;
                pageSize = 0;
            }

            PageFinder mdaFInd = new PageFinder();
            mdaFInd.from = startFrom;
            mdaFInd.recordsPerPage = pageSize;
            mdaFInd.orderBy = sortBy;

            List<MdaDto> mdas = session.selectList("Mda.getAllPaged", mdaFInd);
            PaginatedDto paged = new PaginatedDto();
            paged.setStatus(200);
            if (startFrom == 0) {
                startFrom = 1;
            }
//            int total = session.selectOne("Mda.getCountMda");
            int total = session.selectOne("Mda.getCountMda");
            int totalFound = mdas.size();

            paged.setTotal(total);
            paged.setData(mdas);
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
    public PaginatedDto AllProjectMdaPaged(int Id, String sortBy, int pageNo, int pageSize) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            int startFrom = 0;
            if (pageNo > 0 && pageSize > 0) {
                startFrom = (pageNo - 1) * pageSize;
            } else {
                //                pageSize = 10;
                pageSize = 0;
            }

            PageFinder mdaFInd = new PageFinder();
            mdaFInd.from = startFrom;
            mdaFInd.recordsPerPage = pageSize;
            mdaFInd.orderBy = sortBy;
            mdaFInd.mdaId = Id;

            List<MdaDto> mdas = session.selectList("Mda.getAllProjectPaged", mdaFInd);
            PaginatedDto paged = new PaginatedDto();
            paged.setStatus(200);
            if (startFrom == 0) {
                startFrom = 1;
            }
//            int total = session.selectOne("Mda.getCountMda");
            int total = session.selectOne("Mda.getCountProjectMda", Id);
            int totalFound = mdas.size();

            paged.setTotal(total);
            paged.setData(mdas);
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
    public MdaDto SingleMda(int Id) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try{
            Mda mda = session.selectOne("Mda.getMdaById", Id);
            return GetMda(mda);

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
    public List<MdaOfficeDto> AllMdaOffice(int Id) throws Exception {
        return null;
    }

    @Override
    public List<MdaOfficeDto> SearcheddaOffice(int Id, String search, String sortBy) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {

            MdaOffice md = new MdaOffice();
            md.setOfficeCode("%"+search+"%");
//            md.setMdaCode("%"+search+"%");
            md.setName("%"+search+"%");
            md.setMdaId(Id);

            //sort criteria
            sortBy = sortBy.isEmpty() ? null : sortBy;
            md.setSortBy(sortBy);
            return session.selectList("MdaOffice.getMdaOfficesWhereLike", md);


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
    public PaginatedDto AllMdaOfficePaged(int id, String sortBy, int pageNo, int pageSize) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            int startFrom = 0;
            if (pageNo > 0 && pageSize > 0) {
                startFrom = (pageNo - 1) * pageSize;
            } else {
                //                pageSize = 10;
                pageSize = 0;
            }

            PageFinder mdaFInd = new PageFinder();
            mdaFInd.from = startFrom;
            mdaFInd.recordsPerPage = pageSize;
            mdaFInd.orderBy = sortBy;
            mdaFInd.mdaId = id;

            List<MdaOfficeDto> mdas = session.selectList("MdaOffice.getAllPaged", mdaFInd);
            PaginatedDto paged = new PaginatedDto();
            paged.setStatus(200);
            if (startFrom == 0) {
                startFrom = 1;
            }
            int total = session.selectOne("MdaOffice.getCountMdaOffices", id);
            int totalFound = mdas.size();

            paged.setTotal(total);
            paged.setData(mdas);
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
    public MdaOfficeDto SingleMdaOffice(int id) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try{
            MdaOffice mda = session.selectOne("MdaOffice.getMdaOfficeByMdaId", id);
            return GetMdaOffice(mda);

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
    public List<MdaServiceDto> AllMdaService(int Id) throws Exception {
        return null;
    }

    @Override
    public List<MdaServiceDto> SearchedMdaService(int Id, String search, String sortBy, boolean isAgent, int buzz) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {

            MdaService md = new MdaService();
            md.setCode("%"+search+"%");
//            md.setMdaCode("%"+search+"%");
            md.setName("%"+search+"%");
            md.setMdaId(isAgent? Id : 0);
            md.setFixedAmount(isAgent);

            System.out.println("sn: "+sortBy);

            //sort criteria
            sortBy = sortBy == null || sortBy.trim().isEmpty() ? "name" : sortBy;
            md.setSortBy(sortBy);
            System.out.println("bn: "+md.sortBy);
            return session.selectList("MdaService.getMdaServicesDynWhereLike", md);


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
    public PaginatedDto AllMdaServicePage(int id, String sortBy, int pageNo, int pageSize, boolean isAgent, int businessId) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            int startFrom = 0;
            if (pageNo > 0 && pageSize > 0) {
                startFrom = (pageNo - 1) * pageSize;
            } else {
                //                pageSize = 10;
                pageSize = 0;
            }

            PageFinder mdaFInd = new PageFinder();
            mdaFInd.from = startFrom;
            mdaFInd.recordsPerPage = pageSize;
            mdaFInd.orderBy = sortBy;
            mdaFInd.mdaId = isAgent? id : 0;
            mdaFInd.status = isAgent;
            mdaFInd.businessId = businessId;

//            List<MdaServiceDto> mdas = session.selectList("MdaServiceDto.getAllPaged", mdaFInd);
            List<MdaServiceDto> mdas = session.selectList("MdaService.getAllPagedDyn", mdaFInd);
            PaginatedDto paged = new PaginatedDto();
            paged.setStatus(200);
            if (startFrom == 0) {
                startFrom = 1;
            }
            int total = session.selectOne("MdaService.getCountMdaServicesDyn", mdaFInd);
            int totalFound = mdas.size();

            paged.setTotal(total);
            paged.setData(mdas);
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
    public MdaServiceDto SingleMdaService(int Id) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try{
            MdaService mda = session.selectOne("MdaService.getMdaServiceByIdMdaId", Id);
            return GetMdaService(mda);

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
    public int CountOffices(int businessId, int mdaId) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            PageFinder pf = new PageFinder();
            pf.businessId = businessId;
            pf.mdaId = mdaId;
            return session.selectOne("MdaOffice.getCountAllMdaOfficesMega", pf);

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
    public int CountServices(int businessId, int mdaId) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            PageFinder pf = new PageFinder();
            pf.businessId = businessId;
            pf.mdaId = mdaId;
            return session.selectOne("MdaService.getCountAllMdaServicesMega", pf);

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
    public int CountAgents() throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            return session.selectOne("MdaUser.countAllMdaUsers");

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
    public int CountMda() throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            return session.selectOne("Mda.getCountMda");

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
    public int CountMdaOffices(int id) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            return session.selectOne("MdaOffice.getCountMdaOffices", id);

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
    public int CountMdaServices(int id) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            return session.selectOne("MdaService.getCountMdaServices", id);

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
    public int CountMdaAgents(int id) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            return session.selectOne("MdaUser.countMdaUsers", id);

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
    public int CountMdaAgentsAlt(int id, boolean status, boolean isActive) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            MdaUser mu = new MdaUser();
            mu.setMdaId(id);
            mu.setCanCollect(status);
            mu.isActive = isActive;
            return session.selectOne("MdaUser.countMdaUsers", mu);

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
    public PaginatedDto ListActivityLogs(String sortBy, Integer pageNo, Integer pageSize, int filterId) throws Exception {

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

            assessFInd.businessId = filterId;

            List<ActivityLogDto> assess = session.selectList("ChangeAudit.getActivities", assessFInd);

            PaginatedDto paged = new PaginatedDto();
            paged.setStatus(200);
            if (startFrom == 0) {
                startFrom = 1;
            }
//            int total = session.selectOne("Mda.getCountMda");
            int total = session.selectOne("ChangeAudit.getActivitiesCount", assessFInd.businessId);
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
//            System.out.println(e.getCause());
//            e.printStackTrace();
            throw new Exception(e.getMessage());
        } finally {
            session.commit();
            session.close();
            close(session);
        }
    }


    @Override
    public UserMdaDto GetMdaUser(int id) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            MdaUser md = new MdaUser();
            md.setUserId(id);

            return session.selectOne("MdaUser.getByUserIdJoin", md);

        } catch (NullPointerException e){
//            return null;
            throw new Exception("User/MDA not found");
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }  finally {
            session.commit();
            session.close();
            close(session);
        }
    }

    @Override
    public UserProjectDto GetProjectUser(int id) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            MdaUser md = new MdaUser();
            md.setUserId(id);

            return session.selectOne("BusinessUser.getByUserIdJoin", md);

        } catch (NullPointerException e){
//            return null;
            throw new Exception("User/Project not found");
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }  finally {
            session.commit();
            session.close();
            close(session);
        }
    }

    @Override
    public List<StateDto> GetStates() throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            return session.selectList("Business.getStates");

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
    public List<StateDto> SearchStates(String name) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            StateDto md = new StateDto();
            md.name = "%"+name+"%";
            return session.selectList("Business.searchStates", md);

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
    public List<LgaDto> GetLgas(int id) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            LgaDto md = new LgaDto();
            md.id = id;
//            md.name = "%"+name+"%";

            return session.selectList("Business.getLgas", md);

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
    public List<LgaDto> SearchLgas(int id, String name) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            LgaDto md = new LgaDto();
            md.id = id;
            md.name = "%"+name+"%";

            return session.selectList("Business.searchLgas", md);

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
    public void CreateBusiness(int creator, CreateBusinessDto businessDto) throws Exception {
        if (businessDto.clientName.trim().isEmpty()) throw new Exception("Name is empty");
        if (businessDto.contactEmail.trim().isEmpty()) throw new Exception("Email is empty");
        if (businessDto.contactPhone.trim().isEmpty()) throw new Exception("Phone is empty");
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try{
            Business mdaCheck = session.selectOne("Business.businessCheck", businessDto);

            // check if mda exists
            if (mdaCheck != null)
                throw new Exception("Duplicate Entry");
            Business newMda = NewBusiness(businessDto);
            newMda.setCreatedDate(DateTime());
            newMda.setCreatedBy(creator);
            session.insert("Business.createBusiness", newMda);

            LogAction("Create New Project", "Add new state/project:  "+businessDto.clientName, creator, 0, newMda.getId(), "0.0.0.0");

            if(businessDto.paymentChannels.size() > 0){
                for (int channelId: businessDto.paymentChannels
                ) {

                    PaymentChannel pc = session.selectOne("Business.SinglePayChannel", channelId);

                    BusinessPaymentChannel bpc = new BusinessPaymentChannel();
                    bpc.setBusinessId(newMda.getId());
                    bpc.setChannelId(channelId);
                    bpc.setCreatedBy(creator);
                    bpc.setCreatedDate(DateTime());
                    session.insert("Business.createBusinessPayChannel", bpc);

                    LogAction("New Project Payment Channel", "Add new payment "+pc.getName()+" to project "+businessDto.clientName, creator, 0, newMda.getId(), "0.0.0.0");



                }
            }

        } catch (NullPointerException e){
//            return 0;
            throw new Exception("Error Adding Project");
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }  finally {
            session.commit();
            session.close();
            close(session);
        }

    }


    @Override
    public void BusinessStatus(int updatedBy, int businessId) throws Exception {

        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try{
            Business updateBusiness = session.selectOne("Business.businessById", businessId);

            // check if mda exists
            if (updateBusiness == null) throw new Exception("Invalid Entry");

//            updateBusiness.setActive(true);
            updateBusiness.setActive(!updateBusiness.isActive());
            updateBusiness.setUpdatedDate(DateTime());
            updateBusiness.setUpdatedBy(updatedBy);

            session.update("Business.UpdateBusiness", updateBusiness);

            String act = updateBusiness.isActive() ? "Active" : "Inactive";
            String actAlt = updateBusiness.isActive() ? "Activate" : "Deactivate";

            LogAction("Changed Project Status: "+act, actAlt+" project ("+updateBusiness.getClientName()+")", updatedBy, 0, businessId, "0.0.0.0");

        } catch (NullPointerException e){
//            return 0;
            throw new Exception("Error Updating Project Status");
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }  finally {
            session.commit();
            session.close();
            close(session);
        }

    }

    @Override
    public void ModifyBusiness(int updatedBy, UpdateBusinessDto businessDto) throws Exception {

        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try{
            Business updateBusiness = session.selectOne("Business.businessByIdz", businessDto.id);

            // check if mda exists
            if (updateBusiness == null) throw new Exception("Invalid Entry");

            updateBusiness.setClientName(businessDto.clientName.trim().isEmpty() ? updateBusiness.clientName : businessDto.clientName);
            updateBusiness.setContactEmail(businessDto.contactEmail.trim().isEmpty() ? updateBusiness.contactEmail : businessDto.contactEmail);
            updateBusiness.setContactPhone(businessDto.contactPhone.trim().isEmpty() ? updateBusiness.contactPhone : businessDto.contactPhone);
            updateBusiness.setContactPerson(businessDto.contactPerson.trim().isEmpty() ? updateBusiness.contactPerson : businessDto.contactPerson);
            updateBusiness.setStateLgaId(businessDto.stateLgaId > 0 ? businessDto.stateLgaId : updateBusiness.stateLgaId);
            updateBusiness.setRebateEnabled(businessDto.isRebateEnabled);
            updateBusiness.setUpdatedDate(DateTime());
            updateBusiness.setUpdatedBy(updatedBy);
            session.update("Business.UpdateBusiness", updateBusiness);

            String change = "";

            change += businessDto.clientName.trim().isEmpty() ? "" : updateBusiness.clientName +" to "+ businessDto.clientName+";";
            change += businessDto.contactEmail.trim().isEmpty() ? "" : updateBusiness.contactEmail+" to "+businessDto.contactEmail+";";
            change += businessDto.contactPhone.trim().isEmpty() ? "" : updateBusiness.contactPhone+" to "+businessDto.contactPhone+";";
            change += businessDto.contactPerson.trim().isEmpty() ? "" : updateBusiness.contactPerson+" to "+businessDto.contactPerson+";";
            change += businessDto.stateLgaId > 0 ? "" : updateBusiness.stateLgaId+" to "+businessDto.stateLgaId+";";
            change += updateBusiness.stateLgaId+" to "+businessDto.isRebateEnabled+";";


            LogAction("Update Project",change , updatedBy, 0, businessDto.id, "0.0.0.0");


        } catch (NullPointerException e){
//            return 0;
            throw new Exception("Error Updating Project");
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }  finally {
            session.commit();
            session.close();
            close(session);
        }

    }

    @Override
    public BusinessDto SingleBusiness(Integer clientId, boolean isInternal) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            PageFinder md = new PageFinder();
            md.mdaId = clientId;

            BusinessDto project = session.selectOne("Business.singleClients", md);

            ProjectSummary projectSummary = !isInternal ? session.selectOne("Business.businessRevenueSum", project.id) : null;
            project.revenueGen = !isInternal ? projectSummary.getTotalAmountPaid() : 0;

            return project;


//
////            List<MdaOfficeDto> mdas = session.selectList("MdaService.getAllPaged", mdaFInd);
//            List<BusinessDto> projects = session.selectList("Business.clients", mdaFInd);
//            System.out.println(projects.size());
//            for (BusinessDto project:
//                    projects) {
////                System.out.println(project.id);
//                ProjectSummary projectSummary = session.selectOne("Business.businessRevenueSum", project.id);
//                if(projectSummary != null)
//                    project.revenueGen = projectSummary.getTotalAmountPaid();
////                    System.out.println(project.revenueGen);
//            }


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
    public List<BusinessDto> GetAllBusiness() throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try{
            List<BusinessDto> projects = session.selectList("Business.clients");
//            for (BusinessDto project:
//                    projects) {
//                ProjectSummary projectSummary = session.selectOne("Business.businessRevenue", project.id);
//                project.revenueGen = projectSummary.getTotalAmountPaid();
//            }

            return projects;

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
    public List<BusinessDto> SearchBusiness(String search, String sortBy) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try{

            Business bd = new Business();
            bd.setClientName("%"+search+"%");
            bd.setContactPerson("%"+search+"%");
            bd.setContactPhone("%"+search+"%");
            bd.setContactEmail("%"+search+"%");

            //sort criteria
            sortBy = sortBy.isEmpty() ? null : sortBy;
            bd.setSortBy(sortBy);

            List<BusinessDto> projects = session.selectList("Business.clientsSearch", bd);
            for (BusinessDto project:
                    projects) {
                ProjectSummary projectSummary = session.selectOne("Business.businessRevenueSum", project.id);
                if(projectSummary != null)
                    project.revenueGen = projectSummary.getTotalAmountPaid();
            }
            return projects;
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
    public PaginatedDto GetAllBusinessPaged(String sortBy, int pageNo, int pageSize) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            int startFrom = 0;
            if (pageNo > 0 && pageSize > 0) {
                startFrom = (pageNo - 1) * pageSize;
            } else {
                //                pageSize = 10;
                pageSize = 0;
            }

            PageFinder mdaFInd = new PageFinder();
            mdaFInd.from = startFrom;
            mdaFInd.recordsPerPage = pageSize;
            mdaFInd.orderBy = sortBy;

//            List<MdaOfficeDto> mdas = session.selectList("MdaService.getAllPaged", mdaFInd);
            List<BusinessDto> projects = session.selectList("Business.clients", mdaFInd);
            System.out.println(projects.size());
            for (BusinessDto project:
                 projects) {
//                System.out.println(project.id);
                ProjectSummary projectSummary = session.selectOne("Business.businessRevenueSum", project.id);
                if(projectSummary != null)
                    project.revenueGen = projectSummary.getTotalAmountPaid();
//                    System.out.println(project.revenueGen);
            }
            PaginatedDto paged = new PaginatedDto();
            paged.setStatus(200);
            if (startFrom == 0) {
                startFrom = 1;
            }
            int total = session.selectOne("Business.getCountClient");
            int totalFound = projects.size();

            paged.setTotal(total);
            paged.setData(projects);
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
    public void CreateBusinessUser(int id, String email) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try{
            User user = session.selectOne("User.getUserByEmail", email);

            // check if mda exists
            if (user == null) throw new Exception("invalid user");
//            if(user.role.equals(UserRoles.Agent) || user.role.equals(UserRoles.SubAdmin2)) {
            if(user.role.equals(UserRoles.ProjectReport) || user.role.equals(UserRoles.SubAdmin1)) {
                BusinessUser exists = session.selectOne("BusinessUser.userId", user.id);
                if(exists != null) throw new Exception("User already assigned to Project");
                BusinessUser businessUser = new BusinessUser();
                businessUser.setUserId(user.id);
                businessUser.setBusinessId(id);

                session.insert("BusinessUser.insert", businessUser);
            } else {
                throw new Exception("User of role type cannot be added to Project");
            }

        } catch (NullPointerException e){
//            return 0;
            throw new Exception("Error Adding Project user");
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }  finally {
            session.commit();
            session.close();
            close(session);
        }
    }

    @Override
    public int CountProjects() throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            return session.selectOne("Business.getCountClient");

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
    public int CountProjectServices(int projId) throws Exception {
        return 0;
    }

    @Override
    public int CountProjectOffices(int projId) throws Exception {
        return 0;
    }

    @Override
    public int CountProjectAgents(int projId) {
        return 0;
    }

    @Override
    public int CountProjectMda(int projId) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
        return session.selectOne("Mda.getCountProjectMda", projId);

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
    public void UpdateMdaService(int updatedBy, UpdateMdaServiceDto mda)throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try{
            MdaService mdaService = session.selectOne("MdaService.getMdaServiceById", mda.id);

            // check if mda service exists
            if (mdaService == null) throw new Exception("Invalid Entry");

            mdaService.setName(mda.name.trim().isEmpty() ? mdaService.getName() : mda.name);
            mdaService.setCode(mda.code.trim().isEmpty() ? mdaService.getCode() : mda.code);
            mdaService.setAmount(mda.amount);
            mdaService.setRetaining(mda.isRetaining);
            mdaService.setFixedAmount(mda.isFixedAmount);
            mdaService.setRetainingValue(mda.retainingValue);
            mdaService.setAssessable(mda.isAssessable);
            mdaService.setRetainingByPercentage(mda.isRetainingByPercentage);
            mdaService.setUpdatedBy(updatedBy);
            mdaService.setUpdatedDate(DateTime());

            session.update("MdaService.update", mdaService);

        } catch (NullPointerException e){
//            return 0;
            throw new Exception("Error Updating Service");
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }  finally {
            session.commit();
            session.close();
            close(session);
        }
    }

    @Override
    public void UpdateMdaOffice(int creator, UpdateMdaOfficeDto mdaOfficeDto) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try{
            MdaOffice mdaOffice = session.selectOne("MdaOffice.getMdaOfficeById", mdaOfficeDto.id);

            // check if mda office exists
            if (mdaOffice == null) throw new Exception("Invalid Entry");

            mdaOffice.setName(mdaOfficeDto.name.trim().isEmpty() ? mdaOffice.getName() : mdaOfficeDto.name);
            mdaOffice.setOfficeCode(mdaOfficeDto.officeCode.trim().isEmpty() ? mdaOffice.getOfficeCode() : mdaOfficeDto.officeCode);
            mdaOffice.setHq(mdaOfficeDto.isHq);
            mdaOffice.setUpdatedBy(creator);
            mdaOffice.setUpdatedDate(DateTime());

            session.insert("MdaOffice.update", mdaOffice);

        } catch (NullPointerException e){
//            return 0;
            throw new Exception("Error Adding MDA Office");
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }  finally {
            session.commit();
            session.close();
            close(session);
        }
    }

    @Override
    public void UpdatePosUser(int id, int posId, boolean isSignOutReq, int reqBy) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            Pos userPreviousPos = session.selectOne("Pos.getPosByUserId", id);
//            if(userPreviousPos != null && userPreviousPos.id != posId) throw new Exception("Agent currently logged in on: " + userPreviousPos.serialNo +"(" + userPreviousPos.posDeviceSerial + ")");
            Pos pos = session.selectOne("Pos.getPosById", posId);
            if(pos == null) throw new Exception("Pos not found");

            if(isSignOutReq) {
                pos.setUpdatedBy(reqBy);
                pos.setLastLoggedInUser(id);
                pos.setLoggedInUser(0);
                pos.setUpdatedDate(DateTime());
            } else {
                pos.setUpdatedBy(id);
                pos.setLastLoggedInUser(pos.getLoggedInUser());
                pos.setLoggedInUser(id);
                pos.setUpdatedDate(DateTime());
            }

            session.update("Pos.update", pos);

        } catch (NullPointerException e){
//            return 0;
            throw new Exception("Error Updating POS User");
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }  finally {
            session.commit();
            session.close();
            close(session);
        }

    }

    @Override
    public List<LgaDto> AllGetLgas() throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {

            return session.selectList("Business.getAllLgas");

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
    public ProjectSummary BusinessPerformance(int id) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {

            return session.selectOne("Business.businessRevenueSum", id);

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
    public ProjectSummary AllBusinessPerformance() throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {

            return session.selectOne("Business.businessRevenueSumAll");

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
    public List<PaymentChannelDto> AllPaymentChannels() throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {

            return session.selectList("Business.AllPayChannel");

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
    public void NewPaymentChannel(String name, int createdBy) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            PaymentChannel pc = new PaymentChannel();
            pc.setName(name);
            pc.setActive(true);
            pc.setCreatedBy(1);
            pc.setCreatedDate(DateTime());
            session.insert("Business.NewPayChannel", pc);

            LogAction("New Payment Channel:"+ name,"Add new system payment channel" , createdBy, 0, 0, "0.0.0.0");

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

    @Override
    public void PaymentChannelStatus(int id, int createdBy) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            PaymentChannel pc = session.selectOne("Business.SinglePayChannel", id);
            pc.setActive(!pc.isActive());
            session.update("Business.ModifyChannelStatus", pc);

            String act = pc.isActive() ? "Active" : "Inactive";
            String actAlt = pc.isActive() ? "Activate" : "Deactivate";

            LogAction("Change Payment Channel Status: "+ act,actAlt+" system payment channel "+pc.getName() , createdBy, 0, 0, "0.0.0.0");


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

    @Override
    public void BusPaymentChannelStatus(int id, int createdBy) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            BusinessPaymentChannel pc = session.selectOne("Business.SingleBusPayChannel", id);
            pc.setActive(!pc.isActive());
            session.update("Business.UpdateBusinessPayChannel", pc);

            PaymentChannel pcMain = session.selectOne("Business.SinglePayChannel", pc.getChannelId());

            String act = pc.isActive() ? "Active" : "Inactive";
            String actAlt = pc.isActive() ? "Activate" : "Deactivate";

            LogAction("Change Project Payment Channel Status: "+ act,actAlt+" system payment channel "+pcMain.getName() , createdBy, 0, pc.getBusinessId(), "0.0.0.0");


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

    @Override
    public List<BusinessPaymentChannelDto> AllBusPaymentChannels(int businessId) throws Exception{
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {

            return session.selectList("Business.BusPayChannels", businessId);

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
    public void NewBusPaymentChannel(CreateBusPayChannelDto cbd, int createdBy) throws Exception{
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            BusinessPaymentChannel pc = new BusinessPaymentChannel();
            pc.setChannelId(cbd.channelId);
            pc.setBusinessId(cbd.businessId);
            pc.setActive(true);
            pc.setCreatedBy(1);
            pc.setCreatedDate(DateTime());
            session.insert("Business.createBusinessPayChannel", pc);

            PaymentChannel pcMain = session.selectOne("Business.SinglePayChannel", pc.getChannelId());
            Business updateBusiness = session.selectOne("Business.businessByIdz", cbd.businessId);

            LogAction("New Project Payment Channel ","Add  payment channel "+pcMain.getName()+" to project "+updateBusiness.getClientName() , createdBy, 0, pc.getBusinessId(), "0.0.0.0");


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
