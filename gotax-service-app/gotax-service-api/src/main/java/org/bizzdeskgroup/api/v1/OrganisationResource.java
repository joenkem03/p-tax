package org.bizzdeskgroup.api.v1;


import io.swagger.annotations.*;
import org.bizzdeskgroup.Dto.SuccessResponse;
import org.bizzdeskgroup.Dto.SwaggerDocResponse.*;
import org.bizzdeskgroup.Dtos.Command.*;
import org.bizzdeskgroup.Dtos.Query.*;
import org.bizzdeskgroup.models.UserRoles;
import org.bizzdeskgroup.services.OrganisationService;
import org.bizzdeskgroup.services.PosService;
import org.bizzdeskgroup.services.PosTransactionService;
import org.bizzdeskgroup.services.UserService;
import org.bizzdeskgroup.services.impl.*;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.joda.time.DateTime;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import static org.bizzdeskgroup.helper.UserTokenUtil.ExtractTokenUserMdaId;

@Path("mda")
@Api(value = "MDAs for sub-admin2. Except with specification")
@RequestScoped
@Produces("application/json")
@Consumes("application/json")
@RolesAllowed({UserRoles.Admin, UserRoles.SubAdmin1})
@ApiOperation(value = " ", notes = "", authorizations = {
        @Authorization(value = "authorization")})
public class OrganisationResource {
    @Inject
    JsonWebToken jwt;
    OrganisationService Org = null;
    PosService Pos = null;
    PosTransactionService PosTransaction = null;
    UserService User = null;


    @POST
    @Path("/addmda")
    @ApiOperation(value = "Admin, Sub-Admin1 ", notes = "", authorizations = {
            @Authorization(value = "authorization")})
    @RolesAllowed({UserRoles.Admin, UserRoles.SubAdmin1})
    public Response AddMda(CreateMdaDto mda) {
        SuccessResponse success = new SuccessResponse();
        try {
            String[] userMda = ExtractTokenUserMdaId(jwt);
            int creator = Integer.parseInt(userMda[0]);
            Org = new OrganisationServiceImpl();
            Org.CreateMda(creator, mda);
            success.setStatus(200);
            success.setData("Success");
            return Response.ok(success).build();
        } catch (Exception e) {
            System.out.println("Errr" + e.getMessage());
            e.printStackTrace();
            success.setStatus(400);
            success.setData(e.getMessage());
            success.setMessage("Bad Request");
            return Response.status(400)
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .entity(success)
                    .build();
        }
    }

    @POST
    @Path("/edit_mda")
    @ApiOperation(value = "Admin, Sub-Admin1 ", notes = "", authorizations = {
            @Authorization(value = "authorization")})
    @RolesAllowed({UserRoles.Admin, UserRoles.SubAdmin1})
    public Response EditMda(UpdateMdaDto mda) {
        SuccessResponse success = new SuccessResponse();
        try {
            String[] userMda = ExtractTokenUserMdaId(jwt);
            int creator = Integer.parseInt(userMda[0]);
            Org = new OrganisationServiceImpl();
            Org.UpdateMda(creator, mda);
            success.setStatus(200);
            success.setData("Success");
            return Response.ok(success).build();
        } catch (Exception e) {
            System.out.println("Errr" + e.getMessage());
            e.printStackTrace();
            success.setStatus(400);
            success.setData(e.getMessage());
            success.setMessage("Bad Request");
            return Response.status(400)
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .entity(success)
                    .build();
        }
    }


    @POST
    @Path("/addmdaoffice")
    @ApiOperation(value = " Admin, Sub-Admin1", notes = "", authorizations = {
            @Authorization(value = "authorization")})
    @RolesAllowed({UserRoles.Admin, UserRoles.SubAdmin1})
    public Response AddMdaOffice(CreateMdaOfficeDto mda) {
        SuccessResponse success = new SuccessResponse();
        try {
            String[] userMda = ExtractTokenUserMdaId(jwt);
            int creator = Integer.parseInt(userMda[0]);
            Org = new OrganisationServiceImpl();
            Org.CreateMdaOffice(creator, mda);
            success.setStatus(200);
            success.setData("Success");
            return Response.ok(success).build();
        } catch (Exception e) {
            System.out.println("Errr" + e.getMessage());
            e.printStackTrace();
            success.setStatus(400);
            success.setData(e.getMessage());
            success.setMessage("Bad Request");
            return Response.status(400)
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .entity(success)
                    .build();
        }
    }


    @POST
    @Path("/update_mdaoffice")
    @ApiOperation(value = " Admin, Sub-Admin1", notes = "", authorizations = {
            @Authorization(value = "authorization")})
    @RolesAllowed({UserRoles.Admin, UserRoles.SubAdmin1})
    public Response UpdateMdaOffice(UpdateMdaOfficeDto mda) {
        SuccessResponse success = new SuccessResponse();
        try {
            String[] userMda = ExtractTokenUserMdaId(jwt);
            int creator = Integer.parseInt(userMda[0]);
            Org = new OrganisationServiceImpl();
            Org.UpdateMdaOffice(creator, mda);
            success.setStatus(200);
            success.setData("Success");
            return Response.ok(success).build();
        } catch (Exception e) {
            System.out.println("Errr" + e.getMessage());
            e.printStackTrace();
            success.setStatus(400);
            success.setData(e.getMessage());
            success.setMessage("Bad Request");
            return Response.status(400)
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .entity(success)
                    .build();
        }
    }

    @POST
    @Path("/addservice")
    @ApiOperation(value = " Admin, Sub-Admin1", notes = "", authorizations = {
            @Authorization(value = "authorization")})
    @RolesAllowed({UserRoles.Admin, UserRoles.SubAdmin1})
    public Response AddService(CreateMdaServiceDto mda) {
        SuccessResponse success = new SuccessResponse();
        try {
            String[] userMda = ExtractTokenUserMdaId(jwt);
            int creator = Integer.parseInt(userMda[0]);
            Org = new OrganisationServiceImpl();
            Org.CreateMdaService(creator, mda);
            success.setStatus(200);
            success.setData("Success");
            return Response.ok(success).build();
        } catch (Exception e) {
            System.out.println("Errr" + e.getMessage());
            e.printStackTrace();
            success.setStatus(400);
            success.setData(e.getMessage());
            success.setMessage("Bad Request");
            return Response.status(400)
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .entity(success)
                    .build();
        }
    }



    @POST
    @Path("/update_service")
    @ApiOperation(value = " Admin, Sub-Admin1", notes = "", authorizations = {
            @Authorization(value = "authorization")})
    @RolesAllowed({UserRoles.Admin, UserRoles.SubAdmin1})
    public Response UpdateService(UpdateMdaServiceDto mda) {
        SuccessResponse success = new SuccessResponse();
        try {
            String[] userMda = ExtractTokenUserMdaId(jwt);
            int updator = Integer.parseInt(userMda[0]);
            Org = new OrganisationServiceImpl();
            Org.UpdateMdaService(updator, mda);
            success.setStatus(200);
            success.setData("Success");
            return Response.ok(success).build();
        } catch (Exception e) {
            System.out.println("Errr" + e.getMessage());
            e.printStackTrace();
            success.setStatus(400);
            success.setData(e.getMessage());
            success.setMessage("Bad Request");
            return Response.status(400)
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .entity(success)
                    .build();
        }
    }


    @POST
    @Path("/addmdauser")
    @ApiOperation(value = "Admin, Sub-Admin1 ", notes = " ", authorizations = {
            @Authorization(value = "authorization")})
    @RolesAllowed({UserRoles.Admin, UserRoles.SubAdmin1})
    public Response AddMdaUSer(AddMdaUserDto mdaAdd) {
        SuccessResponse success = new SuccessResponse();
        try {
            String[] userMda = ExtractTokenUserMdaId(jwt);
            CreateMdaUserDto mda = new CreateMdaUserDto();
            mda.mdaId = mdaAdd.mdaId;
            mda.canCollect = mdaAdd.canCollect;
            mda.collectionLimit = mdaAdd.collectionLimit;
            mda.mdaOfficeId = mdaAdd.mdaOfficeId;
            Org = new OrganisationServiceImpl();
            Org.CreateMdaUser(mda, mdaAdd.email, Integer.parseInt(userMda[0]), 0);
            success.setStatus(200);
            success.setData("Success");
            return Response.ok(success).build();
        } catch (Exception e) {
            System.out.println("Errr" + e.getMessage());
            e.printStackTrace();
            success.setStatus(400);
            success.setData(e.getMessage());
            success.setMessage("Bad Request");
            success.setData(e.getMessage());
            return Response.status(400)
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .entity(success)
                    .build();
        }
    }


    @POST
    @Path("/updatemdauser")
    @ApiOperation(value = "Admin, Sub-Admin1 ", notes = " ", authorizations = {
            @Authorization(value = "authorization")})
    @RolesAllowed({UserRoles.Admin, UserRoles.SubAdmin1})
    public Response UpdateMdaUSer(UpdateMdaUserDto mdaAdd) {
        SuccessResponse success = new SuccessResponse();
        try {
            String[] userMda = ExtractTokenUserMdaId(jwt);
            CreateMdaUserDto mda = new CreateMdaUserDto();
            mda.mdaId = mdaAdd.mdaId;
            mda.canCollect = mdaAdd.canCollect;
            mda.collectionLimit = mdaAdd.collectionLimit;
            mda.mdaOfficeId = mdaAdd.mdaOfficeId;
            Org = new OrganisationServiceImpl();
            Org.CreateMdaUser(mda, "", Integer.parseInt(userMda[0]), mdaAdd.userId);
            success.setStatus(200);
            success.setData("Success");
            return Response.ok(success).build();
        } catch (Exception e) {
            System.out.println("Errr" + e.getMessage());
            e.printStackTrace();
            success.setStatus(400);
            success.setData(e.getMessage());
            success.setMessage("Bad Request");
            success.setData(e.getMessage());
            return Response.status(400)
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .entity(success)
                    .build();
        }
    }

    @GET
    @Path("/mdas")
    @ApiOperation(value = "Admin, Sub-Admin1", notes = "", authorizations = {
            @Authorization(value = "authorization")}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    message = "Paged list of MDAs",
                    code = 200,
                    response = Mdas.class,
                    responseContainer = "")
    })
    @RolesAllowed({UserRoles.Admin, UserRoles.SubAdmin1, UserRoles.Individual, UserRoles.NonIndividual})
    public Response GetMdas(@QueryParam("sort")String sortBy, @QueryParam("page")Integer pageNo,
                                      @QueryParam("per_page")Integer pageSize, @QueryParam("project_id")Integer ClientId,
                            @QueryParam("search")String search){
        SuccessResponse success = new SuccessResponse();
        try {
            if(search == null || search.trim().isEmpty()) {
                String[] userMda = ExtractTokenUserMdaId(jwt);
                int client = ClientId == null || ClientId <= 0 ? Integer.parseInt(userMda[2]) : ClientId;

                Org = new OrganisationServiceImpl();
                PaginatedDto page = Org.AllProjectMdaPaged(client, sortBy, pageNo, pageSize);
                success.setStatus(200);
                success.setData(page);
            }
            else {
                String[] userMda = ExtractTokenUserMdaId(jwt);
                int client = ClientId == null || ClientId <= 0 ? Integer.parseInt(userMda[2]) : ClientId;

                Org = new OrganisationServiceImpl();
                List<MdaDto> page = Org.SerchedProjectMda(client, search, sortBy);
                success.setStatus(200);
                success.setData(page);
            }
            return Response.ok(success).build();
        } catch (Exception e) {
            System.out.println("Errr" + e.getMessage());
            e.printStackTrace();
            success.setStatus(400);
            success.setData(e.getMessage());
            success.setMessage("Bad Request");
            return Response.status(400)
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .entity(success)
                    .build();
        }

    }

    @GET
    @Path("/mdaoffices")
    @ApiResponses(value = {
            @ApiResponse(
                    message = "Paged list of MDA Offices",
                    code = 200,
                    response = Offices.class,
                    responseContainer = "")
    })
    @ApiOperation(value = "Admin, Sub-Admin1, Sub-Admin2 ", notes = " ", authorizations = {
            @Authorization(value = "authorization")})
    @RolesAllowed({UserRoles.Admin, UserRoles.SubAdmin1, UserRoles.SubAdmin2})
    public Response GetMdaOffices(@QueryParam("sort")String sortBy, @QueryParam("page")Integer pageNo,
                            @QueryParam("per_page")Integer pageSize, @QueryParam("mda_id")Integer mdaId,
                                  @QueryParam("search")String search){
        SuccessResponse success = new SuccessResponse();
        try {
            if(search == null || search.trim().isEmpty()) {
                String[] userMda = ExtractTokenUserMdaId(jwt);
                int mda = mdaId == null || mdaId <= 0 ? Integer.parseInt(userMda[1]) : mdaId;
                Org = new OrganisationServiceImpl();
//            PaginatedDto page = Org.AllMdaOfficePaged(Integer.parseInt(userMda[1]), sortBy, pageNo, pageSize);
                PaginatedDto page = Org.AllMdaOfficePaged(mda, sortBy, pageNo, pageSize);
                success.setStatus(200);
                success.setData(page);
            } else {
                String[] userMda = ExtractTokenUserMdaId(jwt);
                int mda = mdaId == null || mdaId <= 0 ? Integer.parseInt(userMda[1]) : mdaId;
                Org = new OrganisationServiceImpl();
                List<MdaOfficeDto> page = Org.SearcheddaOffice(mda, search, sortBy);
                success.setStatus(200);
                success.setData(page);
            }
            return Response.ok(success).build();
        } catch (Exception e) {
            System.out.println("Errr" + e.getMessage());
            e.printStackTrace();
            success.setStatus(400);
            success.setData(e.getMessage());
            success.setMessage("Bad Request");
            return Response.status(400)
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .entity(success)
                    .build();
        }

    }

    @GET
    @Path("/mdaservices")
    @ApiResponses(value = {
            @ApiResponse(
                    message = "Paged list of MDA Services",
                    code = 200,
                    response = Services.class,
                    responseContainer = "")
    })
    @ApiOperation(value = " Admin, Sub-Admin1, Sub-Admin2", notes = " ", authorizations = {
            @Authorization(value = "authorization")})
    @RolesAllowed({UserRoles.Admin, UserRoles.SubAdmin1, UserRoles.SubAdmin2})
    public Response GetMdaServices(@QueryParam("sort")String sortBy, @QueryParam("page")Integer pageNo,
                                  @QueryParam("per_page")Integer pageSize, @QueryParam("mda_id")Integer mdaId,
                                   @QueryParam("search")String search){
        SuccessResponse success = new SuccessResponse();
        try {
            if(search == null || search.trim().isEmpty()) {
                String[] userMda = ExtractTokenUserMdaId(jwt);
                int mda = mdaId == null || mdaId <= 0 ? Integer.parseInt(userMda[1]) : mdaId;
                Org = new OrganisationServiceImpl();
                PaginatedDto page = Org.AllMdaServicePage(mda, sortBy, pageNo, pageSize, true, 0);
                success.setStatus(200);
                success.setData(page);
            }
            else {
                String[] userMda = ExtractTokenUserMdaId(jwt);
                int mda = mdaId == null || mdaId <= 0 ? Integer.parseInt(userMda[1]) : mdaId;
                Org = new OrganisationServiceImpl();
                List<MdaServiceDto> page = Org.SearchedMdaService(mda, search, sortBy, true, 0);
                success.setStatus(200);
                success.setData(page);
            }
            return Response.ok(success).build();
        } catch (Exception e) {
            System.out.println("Errr" + e.getMessage());
            e.printStackTrace();
            success.setStatus(400);
            success.setData(e.getMessage());
            success.setMessage("Bad Request");
            return Response.status(400)
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .entity(success)
                    .build();
        }

    }


    @GET
    @Path("/mdas/search")
    @ApiResponses(value = {
            @ApiResponse(
                    message = "list of MDAs",
                    code = 200,
                    response = MdaDto.class,
                    responseContainer = "List")
    })
    @ApiOperation(value = "Admin, Sub-Admin1", notes = " ", authorizations = {
            @Authorization(value = "authorization")})
    @RolesAllowed({UserRoles.Admin, UserRoles.SubAdmin1, UserRoles.ProjectReport})
    public Response SearchMdas(@QueryParam("sort")String sortBy, @QueryParam("search")String search, @QueryParam("project_id")Integer ClientId){
        SuccessResponse success = new SuccessResponse();
        try {
            String[] userMda = ExtractTokenUserMdaId(jwt);
            int client = ClientId == null || ClientId <= 0  ? Integer.parseInt(userMda[2]) : ClientId;

            Org = new OrganisationServiceImpl();
            List<MdaDto> page = Org.SerchedProjectMda(client, search, sortBy);
            success.setStatus(200);
            success.setData(page);
            return Response.ok(success).build();
        } catch (Exception e) {
            System.out.println("Errr" + e.getMessage());
            e.printStackTrace();
            success.setStatus(400);
            success.setData(e.getMessage());
            success.setMessage("Bad Request");
            return Response.status(400)
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .entity(success)
                    .build();
        }

    }

    @GET
    @Path("/mdaoffices/search")
    @ApiResponses(value = {
            @ApiResponse(
                    message = "list of offices",
                    code = 200,
                    response = MdaOfficeDto.class,
                    responseContainer = "List")
    })
    @ApiOperation(value = " Admin, Sub-Admin1, Sub-Admin2 ", notes = " ", authorizations = {
            @Authorization(value = "authorization")})
    @RolesAllowed({UserRoles.Admin, UserRoles.SubAdmin1, UserRoles.SubAdmin2, UserRoles.ProjectReport})
    public Response SearchMdaOffices(@QueryParam("sort")String sortBy, @QueryParam("search")String search, @QueryParam("mda_id")Integer mdaId){
        SuccessResponse success = new SuccessResponse();
        try {
            String[] userMda = ExtractTokenUserMdaId(jwt);
            int mda = mdaId == null || mdaId <= 0  ? Integer.parseInt(userMda[1]) : mdaId;
            Org = new OrganisationServiceImpl();
            List<MdaOfficeDto> page = Org.SearcheddaOffice(mda, search, sortBy);
            success.setStatus(200);
            success.setData(page);
            return Response.ok(success).build();
        } catch (Exception e) {
            System.out.println("Errr" + e.getMessage());
            e.printStackTrace();
            success.setStatus(400);
            success.setData(e.getMessage());
            success.setMessage("Bad Request");
            return Response.status(400)
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .entity(success)
                    .build();
        }

    }

    @GET
    @Path("/mdaservices/search")
    @ApiResponses(value = {
            @ApiResponse(
                    message = "list of MDA Services",
                    code = 200,
                    response = MdaServiceDto.class,
                    responseContainer = "List")
    })
    @ApiOperation(value = "Admin, Sub-Admin1, Sub-Admin2 ", notes = "", authorizations = {
            @Authorization(value = "authorization")})
    @RolesAllowed({UserRoles.Admin, UserRoles.SubAdmin1, UserRoles.SubAdmin2, UserRoles.ProjectReport})
    public Response SearchMdaServices(@QueryParam("sort")String sortBy, @QueryParam("search")String search, @QueryParam("mda_id")Integer mdaId){
        SuccessResponse success = new SuccessResponse();
        try {
            String[] userMda = ExtractTokenUserMdaId(jwt);
            int mda = mdaId == null || mdaId <= 0  ? Integer.parseInt(userMda[1]) : mdaId;
            Org = new OrganisationServiceImpl();
            List<MdaServiceDto> page = Org.SearchedMdaService(mda, search, sortBy, true, 0 );
            success.setStatus(200);
            success.setData(page);
            return Response.ok(success).build();
        } catch (Exception e) {
            System.out.println("Errr" + e.getMessage());
            e.printStackTrace();
            success.setStatus(400);
            success.setData(e.getMessage());
            success.setMessage("Bad Request");
            return Response.status(400)
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .entity(success)
                    .build();
        }

    }

    @GET
    @Path("/mdapos")
    @ApiResponses(value = {
            @ApiResponse(
                    message = "Paged list of Mda POSes",
                    code = 200,
                    response = Poses.class,
                    responseContainer = "")
    })
    @ApiOperation(value = " ", notes = " ", authorizations = {
            @Authorization(value = "authorization")})
    @RolesAllowed({UserRoles.SubAdmin2, UserRoles.Admin, UserRoles.SubAdmin1, UserRoles.ProjectReport})
    public Response GetMdaPoses(@QueryParam("sort")String sortBy, @QueryParam("page")Integer pageNo,
                                  @QueryParam("per_page")Integer pageSize, @QueryParam("Project_Id")Integer formProjectId, @QueryParam("Mda_Id")Integer formMdaId,
                                @QueryParam("search")String search){
        SuccessResponse success = new SuccessResponse();
        try {
            if(search == null || search.trim().isEmpty()) {
                String[] userMda = ExtractTokenUserMdaId(jwt);
                PaginatedDto page = null;
                Pos = new PosServiceImpl();
                Object[] roleCheck = jwt.getGroups().toArray();
                switch (roleCheck[0].toString()) {
                    case UserRoles.Admin:
                        if (formMdaId != null && formMdaId > 0) {
                            page = Pos.AllMdaPos(formMdaId, sortBy, pageNo, pageSize);
                        }
                        if (formProjectId != null && formProjectId > 0) {
//                            if (formMdaId != null && formMdaId > 0) {
//                                page = Pos.AllMdaPos(formMdaId, sortBy, pageNo, pageSize);
//                            } else {
                                page = Pos.AllProjectPos(formProjectId, sortBy, pageNo, pageSize);
//                            }
                        } else {
                            page = Pos.AllPos(sortBy, pageNo, pageSize);
                        }
                        break;
                    case UserRoles.SubAdmin1:
                    case UserRoles.ProjectReport:
                        if (formMdaId != null && formMdaId > 0) {
                            page = Pos.AllMdaPos(formMdaId, sortBy, pageNo, pageSize);
                        } else {
                            page = Pos.AllProjectPos(Integer.parseInt(userMda[2]), sortBy, pageNo, pageSize);
                        }
                        break;
                    case UserRoles.SubAdmin2:
//                case UserRoles.Agent:
                        page = Pos.AllMdaPos(Integer.parseInt(userMda[1]), sortBy, pageNo, pageSize);
                        break;
                }
                success.setStatus(200);
                success.setData(page);
            }
            else {
                String[] userMda = ExtractTokenUserMdaId(jwt);
                Pos = new PosServiceImpl();
//            List<PosDto> page = Pos.SearchMdaPos(Integer.parseInt(userMda[1]), search, sortBy);
                List<PosDto> page = new LinkedList<>();

                Object[] roleCheck = jwt.getGroups().toArray();
                switch (roleCheck[0].toString()) {
                    case UserRoles.Admin:
                        if (formProjectId != null && formProjectId > 0) {
                            if (formMdaId != null && formMdaId > 0) {
                                page = Pos.SearchMdaPos(formMdaId, search, sortBy);
                            } else {
                                page = Pos.SearchProjectPos(formProjectId, search, sortBy);
                            }
                        } else {
                            page = Pos.SearchPos(search, sortBy);
                        }
                        break;
                    case UserRoles.SubAdmin1:
                    case UserRoles.ProjectReport:
                        if (formMdaId != null && formMdaId > 0) {
                            page = Pos.SearchMdaPos(formMdaId, search, sortBy);
                        } else {
                            page = Pos.SearchProjectPos(Integer.parseInt(userMda[2]), search, sortBy);
                        }
                        break;
                    case UserRoles.SubAdmin2:
//                case UserRoles.Agent:
                        page = Pos.SearchMdaPos(Integer.parseInt(userMda[1]), search, sortBy);
                        break;
                }

                success.setStatus(200);
                success.setData(page);
            }
            return Response.ok(success).build();
        } catch (Exception e) {
            System.out.println("Errr" + e.getMessage());
            e.printStackTrace();
            success.setStatus(400);
            success.setData(e.getMessage());
            success.setMessage("Bad Request");
            return Response.status(400)
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .entity(success)
                    .build();
        }

    }

    @GET
    @Path("/mdapos/search")
    @ApiOperation(value = " ", notes = " ", authorizations = {
            @Authorization(value = "authorization")})
    @ApiResponses(value = {
            @ApiResponse(
                    message = "list of MDA POSes",
                    code = 200,
                    response = PosDto.class,
                    responseContainer = "List")
    })
    @RolesAllowed({UserRoles.SubAdmin2, UserRoles.Admin, UserRoles.SubAdmin1, UserRoles.ProjectReport})
    public Response SearchMdaPoses(@QueryParam("sort")String sortBy, @QueryParam("search")String search,
                                   @QueryParam("Project_Id")Integer formProjectId, @QueryParam("Mda_Id")Integer formMdaId){
        SuccessResponse success = new SuccessResponse();
        try {
            String[] userMda = ExtractTokenUserMdaId(jwt);
            Pos = new PosServiceImpl();
//            List<PosDto> page = Pos.SearchMdaPos(Integer.parseInt(userMda[1]), search, sortBy);
            List<PosDto> page = new LinkedList<>();

            Object[] roleCheck = jwt.getGroups().toArray();
            switch (roleCheck[0].toString()){
                case UserRoles.Admin:
                    if (formProjectId != null && formProjectId > 0) {
                        if (formMdaId != null && formMdaId > 0) {
                            page = Pos.SearchMdaPos(formMdaId, search, sortBy);
                        } else {
                            page = Pos.SearchProjectPos(formProjectId, search, sortBy);
                        }
                    } else {
                        page = Pos.SearchPos(search, sortBy);
                    }
                    break;
                case UserRoles.SubAdmin1:
                case UserRoles.ProjectReport:
                    if (formMdaId != null && formMdaId > 0) {
                        page = Pos.SearchMdaPos(formMdaId, search, sortBy);
                    } else {
                        page = Pos.SearchProjectPos(Integer.parseInt(userMda[2]), search, sortBy);
                    }
                    break;
                case UserRoles.SubAdmin2:
//                case UserRoles.Agent:
                    page = Pos.SearchMdaPos(Integer.parseInt(userMda[1]), search, sortBy);
                    break;
            }

            success.setStatus(200);
            success.setData(page);
            return Response.ok(success).build();
        } catch (Exception e) {
            System.out.println("Errr" + e.getMessage());
            e.printStackTrace();
            success.setStatus(400);
            success.setData(e.getMessage());
            success.setMessage("Bad Request");
            return Response.status(400)
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .entity(success)
                    .build();
        }

    }

    @GET
    @Path("/pos/transaction")
    @ApiOperation(value = " ", notes = "  ", authorizations = {@Authorization(value = "authorization")})
    @RolesAllowed({UserRoles.SubAdmin2, UserRoles.SubAdmin1, UserRoles.ProjectReport, UserRoles.Admin})
    @ApiResponses(value = {
            @ApiResponse(
                    message = "Paged list of POS transactions",
                    code = 200,
                    response = Transactions.class,
                    responseContainer = "")
    })
    public Response GetPosTransaction(@QueryParam("sort")String sortBy, @QueryParam("page")Integer pageNo,
                                      @QueryParam("per_page")Integer pageSize, @QueryParam("Project_Id")Integer formProjectId,
                                      @QueryParam("Mda_Id")Integer formMdaId, @QueryParam("start_date")String formStartDate, @QueryParam("end_date")String formEndDate,
                                      @QueryParam("search")String search){
        SuccessResponse success = new SuccessResponse();
        try {
            if(search == null || search.trim().isEmpty()) {

                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

                DateTime today = new DateTime();
                Date endDate = formStartDate == null ? today.toDate() : formatter.parse(formEndDate);
                Date startDate = formEndDate == null ? today.minusMonths(3).toDate() : formatter.parse(formStartDate);

                String[] userMda = ExtractTokenUserMdaId(jwt);
                PosTransaction = new PosTransactionServiceImpl();
//            PaginatedDto Res = PosTransaction.MdaPosTransactions(Integer.parseInt(userMda[1]), sortBy, pageNo, pageSize);
                PaginatedDto Res = new PaginatedDto();

                Object[] roleCheck = jwt.getGroups().toArray();
                switch (roleCheck[0].toString()) {
                    case UserRoles.Admin:
                        if (formProjectId != null && formProjectId > 0) {
                            if (formMdaId != null && formMdaId > 0) {
                                Res = PosTransaction.MdaPosTransactions(formMdaId, sortBy, pageNo, pageSize, startDate, endDate);

                            } else {
                                Res = PosTransaction.ProjectPosTransactions(formProjectId, sortBy, pageNo, pageSize, startDate, endDate);

                            }

                        } else {
                            Res = PosTransaction.AllPosTransactions(sortBy, pageNo, pageSize, startDate, endDate);
                        }
                        break;
                    case UserRoles.SubAdmin1:
                    case UserRoles.ProjectReport:
                        if (formMdaId != null && formMdaId > 0) {
                            Res = PosTransaction.MdaPosTransactions(formMdaId, sortBy, pageNo, pageSize, startDate, endDate);
                        } else {
                            int ProjId = Integer.parseInt(userMda[2]);
                            Res = PosTransaction.ProjectPosTransactions(ProjId, sortBy, pageNo, pageSize, startDate, endDate);
                        }
                        break;
                    case UserRoles.SubAdmin2:
                        Res = PosTransaction.MdaPosTransactions(Integer.parseInt(userMda[1]), sortBy, pageNo, pageSize, startDate, endDate);
                        break;
//                case UserRoles.Individual:
//                case UserRoles.NonIndividual:
////                    filterId = UserId;
//                    break;
                }

                success.setStatus(200);
                success.setData(Res);
            }
            else {
                String[] userMda = ExtractTokenUserMdaId(jwt);
                int mda = formMdaId == null || formMdaId <= 0 ? Integer.parseInt(userMda[1]) : formMdaId;
                PosTransaction = new PosTransactionServiceImpl();
                List<PosTransactionDto> Res = PosTransaction.SearchAgentPosTransaction(0, mda, 0, 0, sortBy, search);
                success.setStatus(200);
                success.setData(Res);
            }
            return Response.ok(success).build();
        } catch (Exception e) {
            e.printStackTrace();
//            throw new BadRequestException(e);
            success.setStatus(400);
            success.setData(e.getMessage());
            success.setMessage("Bad Request");
            return Response.status(400)
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .entity(success)
                    .build();
        }
    }

    @GET
    @Path("/pos/transaction/search")
    @ApiOperation(value = " ", notes = "  ", authorizations = {@Authorization(value = "authorization")})
    @RolesAllowed({UserRoles.Agent, UserRoles.SubAdmin2, UserRoles.SubAdmin1, UserRoles.ProjectReport, UserRoles.Admin})
    @ApiResponses(value = {
            @ApiResponse(
                    message = "list of POS transactions",
                    code = 200,
                    response = PosTransactionDto.class,
                    responseContainer = "")
    })
    public Response SearchPosTransaction(@QueryParam("sort")String sortBy, @QueryParam("search")String search, @QueryParam("mda_id")Integer mdaId){
        SuccessResponse success = new SuccessResponse();
        try {
            String[] userMda = ExtractTokenUserMdaId(jwt);
            int mda = mdaId == null || mdaId <= 0  ? Integer.parseInt(userMda[1]) : mdaId;
            PosTransaction = new PosTransactionServiceImpl();
            List<PosTransactionDto> Res = PosTransaction.SearchAgentPosTransaction(0, mda, 0, 0, sortBy, search);
            success.setStatus(200);
            success.setData(Res);
            return Response.ok(success).build();
        } catch (Exception e) {
            e.printStackTrace();
//            throw new BadRequestException(e);
            success.setStatus(400);
            success.setData(e.getMessage());
            success.setMessage("Bad Request");
            return Response.status(400)
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .entity(success)
                    .build();
        }
    }

    @GET
    @Path("/pos/transaction/{TransactionId}")
    @ApiResponses(value = {
            @ApiResponse(
                    message = "Single POS transactions",
                    code = 200,
                    response = PosTransactionDto.class,
                    responseContainer = "")
    })
    @ApiOperation(value = "Get transaction by transaction Id", notes = "  ", authorizations = {@Authorization(value = "authorization")})
    @RolesAllowed({UserRoles.Agent, UserRoles.SubAdmin2})
    public Response PosTransactionById(@QueryParam("TransactionId")String TransactionId){
        SuccessResponse success = new SuccessResponse();
        try {
            String[] userMda = ExtractTokenUserMdaId(jwt);
            PosTransaction = new PosTransactionServiceImpl();
            PosTransactionDto Res = PosTransaction.SingleAgentPosTransaction(Integer.parseInt(userMda[1]), TransactionId);
            success.setStatus(200);
            success.setData(Res);
            return Response.ok(success).build();
        } catch (Exception e) {
            e.printStackTrace();
//            throw new BadRequestException(e);
            success.setStatus(400);
            success.setData(e.getMessage());
            success.setMessage("Bad Request");
            return Response.status(400)
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .entity(success)
                    .build();
        }
    }

    @GET
    @Path("/pos/remittance")
    @ApiOperation(value = " ", notes = "  ", authorizations = {@Authorization(value = "authorization")})
    @RolesAllowed({UserRoles.Agent, UserRoles.SubAdmin2, UserRoles.SubAdmin1, UserRoles.ProjectReport, UserRoles.Admin})
    @ApiResponses(value = {
            @ApiResponse(
                    message = "Paged list of remittances",
                    code = 200,
                    response = Remittances.class,
                    responseContainer = "")
    })
    public Response GetRemittance(@QueryParam("sort")String sortBy, @QueryParam("page")Integer pageNo, @QueryParam("Rem_Status")boolean remStatus,
                                  @QueryParam("apply_status_filter")boolean applyFilter, @QueryParam("per_page")Integer pageSize,
                                  @QueryParam("Project_Id")Integer formProjectId, @QueryParam("Office_Id")Integer formMdaId, @QueryParam("Mda_Id")Integer formOfficeId,
                                  @QueryParam("start_date")String formStartDate, @QueryParam("end_date")String formEndDate,
                                  @QueryParam("search")String search, @QueryParam("Agent_Id")Integer formAgentId){
        SuccessResponse success = new SuccessResponse();
        try {
            if(search == null || search.trim().isEmpty()) {

                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

                DateTime today = new DateTime();
                Date endDate = formStartDate == null ? today.toDate() : formatter.parse(formEndDate);
                Date startDate = formEndDate == null ? today.minusMonths(3).toDate() : formatter.parse(formStartDate);

                String[] userMda = ExtractTokenUserMdaId(jwt);
                PosTransaction = new PosTransactionServiceImpl();
//            PaginatedDto Res = PosTransaction.MdaRemittance(Integer.parseInt(userMda[1]), sortBy, pageNo, pageSize);
                PaginatedDto Res = new PaginatedDto();

                Object[] roleCheck = jwt.getGroups().toArray();
                switch (roleCheck[0].toString()) {
                    case UserRoles.Admin:
                        if (formOfficeId != null && formOfficeId > 0) {
                            Res = PosTransaction.MdaRemittance(formOfficeId, sortBy, pageNo, pageSize, remStatus, applyFilter, startDate, endDate);
                            break;
                        }
                        if (formMdaId != null && formMdaId > 0) {
                            Res = PosTransaction.MdaRemittance(formMdaId, sortBy, pageNo, pageSize, remStatus, applyFilter, startDate, endDate);
                            break;

                        }
                        if (formProjectId != null && formProjectId > 0) {
//                            else {
                                Res = PosTransaction.ProjectRemittance(formProjectId, sortBy, pageNo, pageSize, remStatus, applyFilter, startDate, endDate);
                                break;

//                            }

                        }
//                        else {
                            Res = PosTransaction.AllRemittance(sortBy, pageNo, pageSize, remStatus, applyFilter, startDate, endDate);
                            break;
//                        }
                    case UserRoles.SubAdmin1:
                    case UserRoles.ProjectReport:
                        if (formOfficeId != null && formOfficeId > 0) {
                            Res = PosTransaction.MdaRemittance(formOfficeId, sortBy, pageNo, pageSize, remStatus, applyFilter, startDate, endDate);
                            break;
                        }
                        if (formMdaId != null && formMdaId > 0) {
                            Res = PosTransaction.MdaRemittance(formMdaId, sortBy, pageNo, pageSize, remStatus, applyFilter, startDate, endDate);
                            break;
                        }
//                        else {
                            int ProjId = Integer.parseInt(userMda[2]);
                            Res = PosTransaction.ProjectRemittance(ProjId, sortBy, pageNo, pageSize, remStatus, applyFilter, startDate, endDate);
//                        }
                        break;
                    case UserRoles.SubAdmin2:
                        if (formOfficeId != null && formOfficeId > 0) {
                            Res = PosTransaction.MdaRemittance(formOfficeId, sortBy, pageNo, pageSize, remStatus, applyFilter, startDate, endDate);
                            break;
                        }
                        Res = PosTransaction.MdaRemittance(Integer.parseInt(userMda[1]), sortBy, pageNo, pageSize, remStatus, applyFilter, startDate, endDate);
                        break;
                    case UserRoles.SubAdmin3:
                        Res = PosTransaction.MdaOfficeRemittance(Integer.parseInt(userMda[3]), sortBy, pageNo, pageSize, remStatus, applyFilter, startDate, endDate);
                        break;
//                case UserRoles.Individual:
//                case UserRoles.NonIndividual:
////                    filterId = UserId;
//                    break;
                }
                success.setStatus(200);
                success.setData(Res);
            }
            else {

                PosTransaction = new PosTransactionServiceImpl();

                String[] userMda = ExtractTokenUserMdaId(jwt);
                List<RemittanceDto> Res = new LinkedList<>();

                Object[] roleCheck = jwt.getGroups().toArray();
                switch (roleCheck[0].toString()) {
                    case UserRoles.Admin:
                        if (formAgentId != null && formAgentId > 0) {
                            Res = PosTransaction.SearchRemittance(formProjectId, formMdaId, formAgentId, sortBy, search);
                            break;
                        }
                        if (formMdaId != null && formMdaId > 0) {
//                                else {
                            Res = PosTransaction.SearchRemittance(formProjectId, formMdaId, 0, sortBy, search);
//                                }
                            break;

                        }
                        if (formProjectId != null && formProjectId > 0) {
//                            else {
                                Res = PosTransaction.SearchRemittance(formProjectId, 0, 0, sortBy, search);
//                            }
                            break;
                        }
//                        else {
                            Res = PosTransaction.SearchRemittance(0, 0, 0, sortBy, search);
//                        }
                        break;
                    case UserRoles.SubAdmin1:
                    case UserRoles.ProjectReport:
                        int ProjId = Integer.parseInt(userMda[2]);
                        if (formAgentId != null && formAgentId > 0) {
                            Res = PosTransaction.SearchRemittance(formProjectId, formMdaId, formAgentId, sortBy, search);
                            break;
                        }
                        if (formMdaId != null && formMdaId > 0) {
//                            else {
                                Res = PosTransaction.SearchRemittance(ProjId, formMdaId, 0, sortBy, search);
//                            }
                            break;
                        }
//                        else {
                            Res = PosTransaction.SearchRemittance(ProjId, 0, 0, sortBy, search);
//                        }
                        break;
                    case UserRoles.SubAdmin2:
                    case UserRoles.SubAdmin3:
//                case UserRoles.Agent:
                        int sProjId = Integer.parseInt(userMda[2]);
                        int mProjId = Integer.parseInt(userMda[1]);
                        if (formAgentId != null && formAgentId > 0) {
                            Res = PosTransaction.SearchRemittance(sProjId, mProjId, formAgentId, sortBy, search);
                        } else {
                            Res = PosTransaction.SearchRemittance(sProjId, mProjId, 0, sortBy, search);
                        }

                        break;


                }
                success.setStatus(200);
                success.setData(Res);
            }
            return Response.ok(success).build();
        } catch (Exception e) {
            e.printStackTrace();
//            throw new BadRequestException(e);
            success.setStatus(400);
            success.setData(e.getMessage());
            success.setMessage("Bad Request");
            return Response.status(400)
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .entity(success)
                    .build();
        }
    }



    @GET
    @Path("/search/users")
    @ApiOperation(value = " ", notes = "  ", authorizations = {@Authorization(value = "authorization")})
    @RolesAllowed({UserRoles.Agent, UserRoles.SubAdmin2, UserRoles.SubAdmin1, UserRoles.ProjectReport, UserRoles.Admin})
    @ApiResponses(value = {
            @ApiResponse(
                    message = "Paged list of users",
                    code = 200,
                    response = Users.class,
                    responseContainer = "")
    })
    public Response SearchUsers(@QueryParam("role")String roleList, @QueryParam("sort")String sortBy, @QueryParam("search")String searchBy,
                             @QueryParam("Project_Id")Integer formProjectId, @QueryParam("Mda_Id")Integer formMdaId){
        SuccessResponse success = new SuccessResponse();
        try {

//            String[] roleSplit = roles.split(",");
//            List<String> roleList = new LinkedList<>();
//            for(String role : roleSplit){
//                if(role.toUpperCase(Locale.ROOT).equals("ALL")) {
//                    roleList.add(UserRoles.Admin);
//                    roleList.add(UserRoles.SubAdmin1);
//                    roleList.add(UserRoles.SubAdmin2);
//                    roleList.add(UserRoles.Agent);
//                    roleList.add(UserRoles.Individual);
//                    roleList.add(UserRoles.NonIndividual);
//                } else if(role.toUpperCase(Locale.ROOT).equals("TAX")) {
//                    roleList.add(UserRoles.Individual);
//                    roleList.add(UserRoles.NonIndividual);
//                } else {
//                    roleList.add(role.trim());
//                }
//            }

            User = new UserServiceImpl();

            String[] userMda = ExtractTokenUserMdaId(jwt);
            List<UserDto> Res = new LinkedList<>();

            Object[] roleCheck = jwt.getGroups().toArray();
            switch (roleCheck[0].toString()){
                case UserRoles.Admin:
                    if (formProjectId != null && formProjectId > 0) {
                        if(formMdaId != null && formMdaId > 0 ){
                            Res = User.SearchUsers(roleList, sortBy, searchBy, UserRoles.SubAdmin2, formProjectId, true );

                        } else {
                            Res = User.SearchUsers(roleList, sortBy, searchBy, UserRoles.SubAdmin1, formProjectId, true);

                        }

                    } else {
                        Res = User.SearchUsers(roleList, sortBy, searchBy, UserRoles.Admin, 0, true);
                    }
                    break;
                case UserRoles.SubAdmin1:
                    int ProjIdA = Integer.parseInt(userMda[2]);
                    if (formMdaId != null && formMdaId > 0) {
                        Res = User.SearchUsers(roleList, sortBy, searchBy, UserRoles.SubAdmin2, ProjIdA, true);
                    } else {
                        Res= User.SearchUsers(roleList, sortBy, searchBy, UserRoles.SubAdmin1, ProjIdA, true);
                    }
                    break;
                case UserRoles.ProjectReport:
                    int ProjId = Integer.parseInt(userMda[2]);
                    if (formMdaId != null && formMdaId > 0) {
                        Res = User.SearchUsers(roleList, sortBy, searchBy, UserRoles.SubAdmin2, ProjId, false);
                    } else {
                        Res= User.SearchUsers(roleList, sortBy, searchBy, UserRoles.SubAdmin1, ProjId, false);
                    }
                    break;
                case UserRoles.SubAdmin2:
                case UserRoles.Agent:
                    Res = User.SearchUsers(roleList, sortBy, searchBy, UserRoles.SubAdmin2, Integer.parseInt(userMda[2]), false);
                    break;
//                case UserRoles.Individual:
//                case UserRoles.NonIndividual:
////                    filterId = UserId;
//                    break;
            }
            success.setStatus(200);
            success.setData(Res);
            return Response.ok(success).build();
        } catch (Exception e) {
            e.printStackTrace();
//            throw new BadRequestException(e);
            success.setStatus(400);
            success.setData(e.getMessage());
            success.setMessage("Bad Request");
            return Response.status(400)
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .entity(success)
                    .build();
        }
    }

    @GET
    @Path("/users")
    @ApiOperation(value = " ", notes = "  ", authorizations = {@Authorization(value = "authorization")})
    @RolesAllowed({UserRoles.Agent, UserRoles.SubAdmin2, UserRoles.SubAdmin1, UserRoles.ProjectReport, UserRoles.Admin})
    @ApiResponses(value = {
            @ApiResponse(
                    message = "Paged list of users",
                    code = 200,
                    response = Users.class,
                    responseContainer = "")
    })
    public Response GetUsers(@QueryParam("multiple_filter")String roles, @QueryParam("sort")String sortBy, @QueryParam("page")Integer pageNo,
                             @QueryParam("per_page")Integer pageSize, @QueryParam("Project_Id")Integer formProjectId, @QueryParam("Mda_Id")Integer formMdaId,
                             @QueryParam("search")String searchBy, @QueryParam("role")String singleRole){
        SuccessResponse success = new SuccessResponse();
        try {
            if(searchBy == null || searchBy.trim().isEmpty()) {
                String[] roleSplit = roles.split(",");
                List<String> roleList = new LinkedList<>();
                for (String role : roleSplit) {
                    if (role.toUpperCase(Locale.ROOT).equals("ALL")) {
                        roleList.add(UserRoles.Admin);
                        roleList.add(UserRoles.SubAdmin1);
                        roleList.add(UserRoles.SubAdmin2);
                        roleList.add(UserRoles.Agent);
                        roleList.add(UserRoles.Individual);
                        roleList.add(UserRoles.NonIndividual);
                    } else if (role.toUpperCase(Locale.ROOT).equals("TAX")) {
                        roleList.add(UserRoles.Individual);
                        roleList.add(UserRoles.NonIndividual);
                    } else {
                        roleList.add(role.trim());
                    }
                }

                User = new UserServiceImpl();

                String[] userMda = ExtractTokenUserMdaId(jwt);
                PaginatedDto Res = new PaginatedDto();

                Object[] roleCheck = jwt.getGroups().toArray();
                switch (roleCheck[0].toString()) {
                    case UserRoles.Admin:
                        if (formProjectId != null && formProjectId > 0) {
                            if (formMdaId != null && formMdaId > 0) {
                                Res = User.AllUsers(roleList, sortBy, pageNo, pageSize, UserRoles.SubAdmin2, formProjectId, true);

                            } else {
                                Res = User.AllUsers(roleList, sortBy, pageNo, pageSize, UserRoles.SubAdmin1, formProjectId, true);

                            }

                        } else {
                            Res = User.AllUsers(roleList, sortBy, pageNo, pageSize, UserRoles.Admin, 0, true);
                        }
                        break;
                    case UserRoles.SubAdmin1:
                        int ProjIdA = Integer.parseInt(userMda[2]);
                        if (formMdaId != null && formMdaId > 0) {
                            Res = User.AllUsers(roleList, sortBy, pageNo, pageSize, UserRoles.SubAdmin2, ProjIdA, true);
                        } else {
                            Res = User.AllUsers(roleList, sortBy, pageNo, pageSize, UserRoles.SubAdmin1, ProjIdA, true);
                        }
                        break;
                    case UserRoles.ProjectReport:
                        int ProjId = Integer.parseInt(userMda[2]);
                        if (formMdaId != null && formMdaId > 0) {
                            Res = User.AllUsers(roleList, sortBy, pageNo, pageSize, UserRoles.SubAdmin2, ProjId, false);
                        } else {
                            Res = User.AllUsers(roleList, sortBy, pageNo, pageSize, UserRoles.SubAdmin1, ProjId, false);
                        }
                        break;
                    case UserRoles.SubAdmin2:
                    case UserRoles.Agent:
                        Res = User.AllUsers(roleList, sortBy, pageNo, pageSize, UserRoles.SubAdmin2, Integer.parseInt(userMda[2]), false);
                        break;
//                case UserRoles.Individual:
//                case UserRoles.NonIndividual:
////                    filterId = UserId;
//                    break;
                }
                success.setStatus(200);
                success.setData(Res);
            }
            else {
                User = new UserServiceImpl();

                String[] userMda = ExtractTokenUserMdaId(jwt);
                List<UserDto> Res = new LinkedList<>();

                Object[] roleCheck = jwt.getGroups().toArray();
                switch (roleCheck[0].toString()) {
                    case UserRoles.Admin:
                        if (formProjectId != null && formProjectId > 0) {
                            if (formMdaId != null && formMdaId > 0) {
                                Res = User.SearchUsers(singleRole, sortBy, searchBy, UserRoles.SubAdmin2, formProjectId, true);

                            } else {
                                Res = User.SearchUsers(singleRole, sortBy, searchBy, UserRoles.SubAdmin1, formProjectId, true);

                            }

                        } else {
                            Res = User.SearchUsers(singleRole, sortBy, searchBy, UserRoles.Admin, 0, true);
                        }
                        break;
                    case UserRoles.SubAdmin1:
                        int ProjIdA = Integer.parseInt(userMda[2]);
                        if (formMdaId != null && formMdaId > 0) {
                            Res = User.SearchUsers(singleRole, sortBy, searchBy, UserRoles.SubAdmin2, ProjIdA, true);
                        } else {
                            Res = User.SearchUsers(singleRole, sortBy, searchBy, UserRoles.SubAdmin1, ProjIdA, true);
                        }
                        break;
                    case UserRoles.ProjectReport:
                        int ProjId = Integer.parseInt(userMda[2]);
                        if (formMdaId != null && formMdaId > 0) {
                            Res = User.SearchUsers(singleRole, sortBy, searchBy, UserRoles.SubAdmin2, ProjId, false);
                        } else {
                            Res = User.SearchUsers(singleRole, sortBy, searchBy, UserRoles.SubAdmin1, ProjId, false);
                        }
                        break;
                    case UserRoles.SubAdmin2:
                    case UserRoles.Agent:
                        Res = User.SearchUsers(singleRole, sortBy, searchBy, UserRoles.SubAdmin2, Integer.parseInt(userMda[2]), false);
                        break;
//                case UserRoles.Individual:
//                case UserRoles.NonIndividual:
////                    filterId = UserId;
//                    break;
                }
                success.setStatus(200);
                success.setData(Res);
            }
            return Response.ok(success).build();
        } catch (Exception e) {
            e.printStackTrace();
//            throw new BadRequestException(e);
            success.setStatus(400);
            success.setData(e.getMessage());
            success.setMessage("Bad Request");
            return Response.status(400)
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .entity(success)
                    .build();
        }
    }



    @GET
    @Path("/remittance/search")
    @ApiOperation(value = " ", notes = "  ", authorizations = {@Authorization(value = "authorization")})
    @RolesAllowed({UserRoles.Admin, UserRoles.SubAdmin1, UserRoles.SubAdmin2, UserRoles.ProjectReport})
    @ApiResponses(value = {
            @ApiResponse(
                    message = "list of Agent remittance",
                    code = 200,
                    response = org.bizzdeskgroup.models.PosTransaction.class,
                    responseContainer = "List")
    })
    public Response SearchRemittance(@QueryParam("sort")String sortBy, @QueryParam("search")String search,  @QueryParam("Project_Id")Integer formProjectId,
                                     @QueryParam("Mda_Id")Integer formMdaId, @QueryParam("Agent_Id")Integer formAgentId){
        SuccessResponse success = new SuccessResponse();
        try {

            PosTransaction = new PosTransactionServiceImpl();

            String[] userMda = ExtractTokenUserMdaId(jwt);
            List<RemittanceDto> Res = new LinkedList<>();

            Object[] roleCheck = jwt.getGroups().toArray();
            switch (roleCheck[0].toString()){
                case UserRoles.Admin:
                    if (formProjectId != null && formProjectId > 0) {
                        if(formMdaId != null && formMdaId > 0 ){
                            if (formAgentId != null && formAgentId > 0) {
                                Res = PosTransaction.SearchRemittance(formProjectId, formMdaId, formAgentId, sortBy, search);
                            } else {
                                Res = PosTransaction.SearchRemittance(formProjectId, formMdaId, 0, sortBy, search);
                            }

                        } else {
                            Res = PosTransaction.SearchRemittance(formProjectId, 0, 0, sortBy, search);

                        }

                    } else {
                        Res = PosTransaction.SearchRemittance(0, 0, 0, sortBy, search);
                    }
                    break;
                case UserRoles.SubAdmin1:
                case UserRoles.ProjectReport:
                    int ProjId = Integer.parseInt(userMda[2]);
                    if (formMdaId != null && formMdaId > 0) {
                        if (formAgentId != null && formAgentId > 0) {
                            Res = PosTransaction.SearchRemittance(formProjectId, formMdaId, formAgentId, sortBy, search);
                        } else {
                            Res = PosTransaction.SearchRemittance(ProjId, formMdaId, 0, sortBy, search);
                        }
                    } else {
                        Res = PosTransaction.SearchRemittance(ProjId, 0, 0, sortBy, search);
                    }
                    break;
                case UserRoles.SubAdmin2:
//                case UserRoles.Agent:
                    int sProjId = Integer.parseInt(userMda[2]);
                    int mProjId = Integer.parseInt(userMda[1]);
                    if (formAgentId != null && formAgentId > 0) {
                        Res = PosTransaction.SearchRemittance(sProjId, mProjId, formAgentId, sortBy, search);
                    } else {
                        Res = PosTransaction.SearchRemittance(sProjId, mProjId, 0, sortBy, search);
                    }

                    break;


            }
            success.setStatus(200);
            success.setData(Res);
            return Response.ok(success).build();
        } catch (Exception e) {
            e.printStackTrace();
//            throw new BadRequestException(e);
            success.setStatus(400);
            success.setMessage("Bad Request");
            success.setData(e.getMessage());
            return Response.status(400)
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .entity(success)
                    .build();
        }
    }




    @GET
    @Path("/mda")
    @ApiOperation(value = "Admin", notes = "", authorizations = {
            @Authorization(value = "authorization")}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    message = "Single Mda",
                    code = 200,
                    response = BusinessDto.class,
                    responseContainer = "")
    })
    @RolesAllowed({UserRoles.Admin, UserRoles.SubAdmin1})
    public Response GetSingleMda(@QueryParam("Project_Id")Integer formProjectId, @QueryParam("Mda_Id")Integer formMdaId){
        SuccessResponse success = new SuccessResponse();
        try {

            String[] userMda = ExtractTokenUserMdaId(jwt);

            Object[] roleCheck = jwt.getGroups().toArray();
            switch (roleCheck[0].toString()) {
                case UserRoles.Admin:
                    if (formProjectId != null && formProjectId > 0) {
                        if (formMdaId != null && formMdaId > 0) {

                        } else {
                            MdaDto mdaDto = Org.SingleMda(formMdaId);

                        }
                    } else {
                        MdaDto mdaDto = Org.SingleMda(formMdaId);
                    }
                    break;
                case UserRoles.SubAdmin1:
                case UserRoles.ProjectReport:
                    int ProjId = Integer.parseInt(userMda[2]);
                    if (formMdaId != null && formMdaId > 0) {

                    } else {


                    }

                    break;
                case UserRoles.SubAdmin2:
                    int MdaId = Integer.parseInt(userMda[1]);
                    int MdaProjId = Integer.parseInt(userMda[2]);

                    break;
            }


            success.setStatus(200);
            return Response.ok(success).build();
        } catch (Exception e) {
            System.out.println("Errr" + e.getMessage());
            e.printStackTrace();
            success.setStatus(400);
            success.setData(e.getMessage());
            success.setMessage("Bad Request");
            return Response.status(400)
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .entity(success)
                    .build();
        }

    }


//    @GET
//    @Path("/state")
//    @ApiOperation(value = "ALL roles ", notes = "  ", authorizations = {@Authorization(value = "authorization")})
//    @RolesAllowed({UserRoles.Admin, UserRoles.SubAdmin1, UserRoles.SubAdmin2, UserRoles.Agent, UserRoles.NonIndividual, UserRoles.Individual})
//    public Response GetStates(){
//        SuccessResponse success = new SuccessResponse();
//        success.setStatus(200);
//        success.setData("");
//        return Response.ok(success).build();
//
//    }
//
//    @GET
//    @Path("/state")
//    @ApiOperation(value = "Get state LGAs (ALL roles)", notes = "  ", authorizations = {@Authorization(value = "authorization")})
//    @RolesAllowed({UserRoles.Admin, UserRoles.SubAdmin1, UserRoles.SubAdmin2, UserRoles.Agent, UserRoles.NonIndividual, UserRoles.Individual})
//    public Response GetLgasByStateId(@QueryParam("state_id")int StateId){
//        SuccessResponse success = new SuccessResponse();
//        success.setStatus(200);
//        success.setData("");
//        return Response.ok(success).build();
//
//    }

}
