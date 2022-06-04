package org.bizzdeskgroup.api.v1;

import io.swagger.annotations.*;
import org.bizzdeskgroup.Dto.SuccessResponse;
import org.bizzdeskgroup.Dto.SwaggerDocResponse.*;
import org.bizzdeskgroup.Dtos.Command.*;
import org.bizzdeskgroup.Dtos.Query.*;
import org.bizzdeskgroup.models.UserRoles;
import org.bizzdeskgroup.services.*;
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


@Path("admin")
@Api(value = "Administration", description = "")
@RequestScoped
@Produces("application/json")
@Consumes("application/json")
public class AdminResource {
    UserService User = null;
    PosService Pos = null;
    OrganisationService Org = null;
    PosTransactionService PosTransaction = null;
    AssessmentService Assess = null;

    @Inject
    JsonWebToken jwt;


    @GET
    @Path("/users")
//    @RolesAllowed({UserRoles.Admin, UserRoles.SubAdmin1, UserRoles.SubAdmin2})
    @RolesAllowed({UserRoles.Admin, UserRoles.SubAdmin1, UserRoles.SubAdmin2})
    @ApiResponses(value = {
            @ApiResponse(
                    message = "Paged list of users",
                    code = 200,
                    response = Users.class,
                    responseContainer = "")
    })
    @ApiOperation(value = "All - Select all users; Tax - Select Individual & Non-Individual users; All other available roles apply e,g: Admin, Sub-Admin1, etc. Seperate by coma \",\"", notes = "", authorizations = {@Authorization(value = "authorization")})
    public Response GetUsers(@QueryParam("multiple_filter")String roles, @QueryParam("sort")String sortBy, @QueryParam("page")Integer pageNo,
                             @QueryParam("per_page")Integer pageSize){
        SuccessResponse success = new SuccessResponse();
        try {
            String[] roleSplit = roles.split(",");
            List<String> roleList = new LinkedList<>();
            for(String role : roleSplit){
                if(role.toUpperCase(Locale.ROOT).equals("ALL")) {
                    roleList.add(UserRoles.Admin);
                    roleList.add(UserRoles.SubAdmin1);
                    roleList.add(UserRoles.SubAdmin2);
                    roleList.add(UserRoles.Agent);
                    roleList.add(UserRoles.Individual);
                    roleList.add(UserRoles.NonIndividual);
                } else if(role.toUpperCase(Locale.ROOT).equals("TAX")) {
                    roleList.add(UserRoles.Individual);
                    roleList.add(UserRoles.NonIndividual);
                } else {
                    roleList.add(role.trim());
                }
            }

            User = new UserServiceImpl();
            PaginatedDto page = User.AllUsers(roleList, sortBy, pageNo, pageSize, UserRoles.Admin, 0, true);
            success.setStatus(200);
            success.setData(page);
//            success.setData("page");
            return Response.ok(success).build();
        } catch (Exception e) {
            System.out.println("Errr" + e.getMessage());
            e.printStackTrace();
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
    @Path("/user/{Id}")
    @ApiResponses(value = {
            @ApiResponse(
                    message = "Single user",
                    code = 200,
                    response = UserDto.class,
                    responseContainer = "")
    })
    @RolesAllowed({UserRoles.Admin, UserRoles.SubAdmin1, UserRoles.SubAdmin2, UserRoles.Agent})
    @ApiOperation(value = "Get user by Id", notes = "", authorizations = {@Authorization(value = "authorization")})
    public Response UserInfo(@QueryParam("Id")int Id){
        SuccessResponse success = new SuccessResponse();
        try {

            User = new UserServiceImpl();
            UserDto page = User.SingleUser(Id);
            success.setStatus(200);
            success.setData(page);
//            success.setData("page");
            return Response.ok(success).build();
        } catch (Exception e) {
            System.out.println("Errr" + e.getMessage());
            e.printStackTrace();
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
    @Path("/user/search/{UserPhoneOrEmail}")
    @ApiResponses(value = {
            @ApiResponse(
                    message = "Single user",
                    code = 200,
                    response = UserDto.class,
                    responseContainer = "")
    })
    @RolesAllowed({UserRoles.Admin, UserRoles.SubAdmin1, UserRoles.SubAdmin2, UserRoles.Agent})
    @ApiOperation(value = "Get user by phone or email", notes = "", authorizations = {@Authorization(value = "authorization")})
    public Response UserInfoByEmailPhone(@QueryParam("UserPhoneOrEmail")String Id){
        SuccessResponse success = new SuccessResponse();
        try {

            User = new UserServiceImpl();
            UserDto page = User.SingleUserByPhoneOrEmail(Id);
            success.setStatus(200);
            success.setData(page);
            return Response.ok(success).build();
        } catch (Exception e) {
            System.out.println("Errr" + e.getMessage());
            e.printStackTrace();
            success.setStatus(400);
            success.setMessage("Bad Request");
            success.setData(e.getMessage());
            return Response.status(400)
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .entity(success)
                    .build();
        }

    }


//    @GET
//    @Path("/user/search/{UserPhoneOrEmail}")
//    @ApiResponses(value = {
//            @ApiResponse(
//                    message = "Single user",
//                    code = 200,
//                    response = UserDto.class,
//                    responseContainer = "")
//    })
//    @RolesAllowed({UserRoles.Admin, UserRoles.SubAdmin1, UserRoles.SubAdmin2, UserRoles.Agent})
//    @ApiOperation(value = "Get user by phone or email", notes = "", authorizations = {@Authorization(value = "authorization")})
//    public Response UserInfoByEmailPhone(@QueryParam("UserPhoneOrEmail")String Id){
//        SuccessResponse success = new SuccessResponse();
//        try {
//
//            User = new UserServiceImpl();
//            UserDto page = User.SingleUserByPhoneOrEmail(Id);
//            success.setStatus(200);
//            success.setData(page);
//            return Response.ok(success).build();
//        } catch (Exception e) {
//            System.out.println("Errr" + e.getMessage());
//            e.printStackTrace();
//            success.setStatus(400);
//            success.setMessage("Bad Request");
//            success.setData(e.getMessage());
//            return Response.status(400)
//                    .type(MediaType.APPLICATION_JSON_TYPE)
//                    .entity(success)
//                    .build();
//        }
//
//    }


    @POST
    @Path("/user/access")
    @RolesAllowed({UserRoles.Admin, UserRoles.SubAdmin1})
    @ApiOperation(value = "Activate or Deactivate user; set status to \"True\" to activate or \"False\" to deactivate", notes = "", authorizations = {@Authorization(value = "authorization")})
    public Response UserAccess(UserActiveDto userActiveDto){
        SuccessResponse success = new SuccessResponse();
        try {
            String[] userMda = ExtractTokenUserMdaId(jwt);
            User = new UserServiceImpl();
//            if(UserRoles.)
            if (userActiveDto.status){
                User.Activate(Integer.parseInt(userMda[0]), userActiveDto.userId);
            } else {
                User.Deactivate(Integer.parseInt(userMda[0]), userActiveDto.userId);
            }
            success.setStatus(200);
            success.setData("Success");
            return Response.ok(success).build();
        } catch (Exception e) {
            System.out.println("Errr" + e.getMessage());
            e.printStackTrace();
            success.setStatus(400);
            success.setMessage("Bad Request");
            success.setData(e.getMessage());
            return Response.status(400)
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .entity(success)
                    .build();
        }

    }


    @POST
    @Path("/edit_user")
    @RolesAllowed({UserRoles.Admin, UserRoles.SubAdmin1})
    @ApiOperation(value = " ", notes = "", authorizations = {@Authorization(value = "authorization")})
    public Response EditUser(UserUpdateDto updateDto){
        SuccessResponse success = new SuccessResponse();
        try {
            String[] userMda = ExtractTokenUserMdaId(jwt);
            User = new UserServiceImpl();
            User.Update(Integer.parseInt(userMda[0]), updateDto.id, updateDto);
            success.setStatus(200);
            success.setData("Success");
            return Response.ok(success).build();
        } catch (Exception e) {
            System.out.println("Errr" + e.getMessage());
            e.printStackTrace();
            success.setStatus(400);
            success.setMessage("Bad Request");
            success.setData(e.getMessage());
            return Response.status(400)
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .entity(success)
                    .build();
        }

    }

    @POST
    @Path("/posmeta")
    @RolesAllowed({UserRoles.Admin, UserRoles.SubAdmin1})
    @ApiOperation(value = "Admin/SubAdmin1 add POS Meta", notes = "", authorizations = {@Authorization(value = "authorization")})
    public Response AddPosMeta(AddPosMetaDto addPosDto){
        SuccessResponse success = new SuccessResponse();
        try {

            String[] userMda = ExtractTokenUserMdaId(jwt);
            Pos = new PosServiceImpl();
            Pos.CreatePosMeta(addPosDto, Integer.parseInt(userMda[0]));
            success.setStatus(200);
            success.setData("Success");
            return Response.ok(success).build();
        } catch (Exception e) {
            System.out.println("Errr" + e.getMessage());
            e.printStackTrace();
            success.setStatus(400);
            success.setMessage("Bad Request");
            success.setData(e.getMessage());
            return Response.status(400)
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .entity(success)
                    .build();
        }
    }

    @POST
    @Path("/pos")
    @RolesAllowed({UserRoles.Admin, UserRoles.SubAdmin1})
    @ApiOperation(value = "Admin/SubAdmin1 add POS", notes = "", authorizations = {@Authorization(value = "authorization")})
    public Response AddPos(AddPosDto addPosDto){
        SuccessResponse success = new SuccessResponse();
        try {

            String[] userMda = ExtractTokenUserMdaId(jwt);
            Pos = new PosServiceImpl();
            Pos.CreatePos(addPosDto, Integer.parseInt(userMda[0]));
            success.setStatus(200);
            success.setData("Success");
            return Response.ok(success).build();
        } catch (Exception e) {
            System.out.println("Errr" + e.getMessage());
            e.printStackTrace();
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
    @Path("/all-posmeta")
    @ApiResponses(value = {
            @ApiResponse(
                    message = "Paged list of POS Batches",
                    code = 200,
                    response = PosMetaDto.class,
                    responseContainer = "List")
    })
    @ApiOperation(value = "", notes = "", authorizations = {
            @Authorization(value = "authorization")}
    )
    @RolesAllowed({UserRoles.Admin, UserRoles.SubAdmin1})
    public Response GetAllPosMeta(){
        SuccessResponse success = new SuccessResponse();
        try {
            Pos = new PosServiceImpl();
            List<PosMetaDto> posMetaDto = Pos.AllPosMetaUnPaged();
            success.setStatus(200);
            success.setData(posMetaDto);
            return Response.ok(success).build();
        } catch (Exception e) {
            System.out.println("Errr" + e.getMessage());
            e.printStackTrace();
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
    @Path("/posmeta")
    @ApiResponses(value = {
            @ApiResponse(
                    message = "Paged list of POS Batches",
                    code = 200,
                    response = PosMetaDto.class,
                    responseContainer = "List")
    })
    @ApiOperation(value = "", notes = "", authorizations = {
            @Authorization(value = "authorization")}
    )
    @RolesAllowed({UserRoles.Admin, UserRoles.SubAdmin1})
    public Response GetPosMeta(@QueryParam("sort")String sortBy, @QueryParam("page")Integer pageNo,
                            @QueryParam("per_page")Integer pageSize){
        SuccessResponse success = new SuccessResponse();
        try {
            Pos = new PosServiceImpl();
            PaginatedDto page = Pos.AllPosMeta(sortBy, pageNo, pageSize);
            success.setStatus(200);
            success.setData(page);
            return Response.ok(success).build();
        } catch (Exception e) {
            System.out.println("Errr" + e.getMessage());
            e.printStackTrace();
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
    @Path("/pos")
    @ApiResponses(value = {
            @ApiResponse(
                    message = "Paged list of POSes",
                    code = 200,
                    response = Poses.class,
                    responseContainer = "")
    })
    @ApiOperation(value = "", notes = "", authorizations = {
            @Authorization(value = "authorization")}
    )
    @RolesAllowed({UserRoles.Admin, UserRoles.SubAdmin1})
    public Response GetPos(@QueryParam("sort")String sortBy, @QueryParam("page")Integer pageNo,
                            @QueryParam("per_page")Integer pageSize,
                           @QueryParam("search")String search){
        SuccessResponse success = new SuccessResponse();
        try {
            if(search == null || search.trim().isEmpty()) {
                Pos = new PosServiceImpl();
                PaginatedDto page = Pos.AllPos(sortBy, pageNo, pageSize);
                success.setStatus(200);
                success.setData(page);
            } else {
                Pos = new PosServiceImpl();
                List<PosDto> page = Pos.SearchPos(search, sortBy);
                success.setStatus(200);
                success.setData(page);
            }
            return Response.ok(success).build();
        } catch (Exception e) {
            System.out.println("Errr" + e.getMessage());
            e.printStackTrace();
            success.setStatus(400);
            success.setMessage("Bad Request");
            success.setData(e.getMessage());
            return Response.status(400)
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .entity(success)
                    .build();
        }

    }


    @POST
    @Path("/pos/access")
    @RolesAllowed({UserRoles.Admin, UserRoles.SubAdmin1})
    @ApiOperation(value = "Activate or Deactivate pos; set status to \"True\" to activate or \"False\" to deactivate", notes = "", authorizations = {@Authorization(value = "authorization")})
    public Response PosAccess(PosActiveDto posActiveDto){
        SuccessResponse success = new SuccessResponse();
        try {
            Pos = new PosServiceImpl();
            String[] userMda = ExtractTokenUserMdaId(jwt);
            if (posActiveDto.status){
                Pos.Activate(Integer.parseInt(userMda[0]), posActiveDto.posId);
            } else {
                Pos.Deactivate(Integer.parseInt(userMda[0]), posActiveDto.posId);
            }
            success.setStatus(200);
            success.setData("Success");
            return Response.ok(success).build();
        } catch (Exception e) {
            System.out.println("Errr" + e.getMessage());
            e.printStackTrace();
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
    @Path("/pos/search")
    @ApiResponses(value = {
            @ApiResponse(
                    message = "List of POSes",
                    code = 200,
                    response = PosDto.class,
                    responseContainer = "List")
    })
    @ApiOperation(value = " ", notes = " ", authorizations = {
            @Authorization(value = "authorization")})
    @RolesAllowed({UserRoles.Admin, UserRoles.SubAdmin1})
    public Response SearchPoses(@QueryParam("sort")String sortBy, @QueryParam("search")String search){
        SuccessResponse success = new SuccessResponse();
        try {
            Pos = new PosServiceImpl();
            List<PosDto> page = Pos.SearchPos(search, sortBy);
            success.setStatus(200);
            success.setData(page);
            return Response.ok(success).build();
        } catch (Exception e) {
            System.out.println("Errr" + e.getMessage());
            e.printStackTrace();
            success.setStatus(400);
            success.setMessage("Bad Request");
            success.setData(e.getMessage());
            return Response.status(400)
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .entity(success)
                    .build();
        }

    }

    @POST
    @Path("/pos/reassign")
    @ApiOperation(value = " ", notes = " ", authorizations = {
            @Authorization(value = "authorization")})
    @RolesAllowed({UserRoles.Admin, UserRoles.SubAdmin1})
    public Response ReAssignPos(UpdatePosDto updatePosDto){
        SuccessResponse success = new SuccessResponse();
        try {
            String[] userMda = ExtractTokenUserMdaId(jwt);
            Pos = new PosServiceImpl();
            Pos.UpdatePos(updatePosDto, Integer.parseInt(userMda[0]));
            success.setStatus(200);
            success.setData("Success");
            return Response.ok(success).build();
        } catch (Exception e) {
            System.out.println("Errr" + e.getMessage());
            e.printStackTrace();
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
    @Path("/pos/remittance")
    @ApiOperation(value = " ", notes = "  ", authorizations = {@Authorization(value = "authorization")})
    @ApiResponses(value = {
            @ApiResponse(
                    message = "Paged list of remittances",
                    code = 200,
                    response = Remittances.class,
                    responseContainer = "")
    })

    @RolesAllowed({UserRoles.Admin, UserRoles.SubAdmin1})
    public Response GetRemittance(@QueryParam("sort")String sortBy, @QueryParam("page")Integer pageNo, @QueryParam("Rem_Status")boolean remStatus,
                                  @QueryParam("apply_status_filter")boolean applyFilter, @QueryParam("per_page")Integer pageSize,
                                  @QueryParam("start_date")String formStartDate, @QueryParam("end_date")String formEndDate){
        SuccessResponse success = new SuccessResponse();
        try {

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

            DateTime today = new DateTime();
            Date endDate = formStartDate == null ? today.toDate() : formatter.parse(formEndDate);
            Date startDate = formEndDate == null ? today.minusMonths(3).toDate() : formatter.parse(formStartDate);

            PosTransaction = new PosTransactionServiceImpl();
            PaginatedDto Res = PosTransaction.AllRemittance(sortBy, pageNo, pageSize, remStatus, applyFilter, startDate, endDate);
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

    @POST
    @Path("/pos/remittance/{remNo}")
    @ApiResponses(value = {
            @ApiResponse(
                    message = "Single remittance",
                    code = 200,
                    response = SingleRemittanceDto.class,
                    responseContainer = "")
    })
    @ApiOperation(value = " ", notes = "  ", authorizations = {@Authorization(value = "authorization")})
    @RolesAllowed({UserRoles.Admin, UserRoles.SubAdmin1})
    public Response SingleRemittance(@QueryParam("remittanceNo")String remittanceNo){
        SuccessResponse success = new SuccessResponse();
        try {
            PosTransaction = new PosTransactionServiceImpl();
            SingleRemittanceDto Res = PosTransaction.SingleRemittance(remittanceNo);
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
    @Path("/pos/transaction")
    @ApiOperation(value = " ", notes = "  ", authorizations = {@Authorization(value = "authorization")})
    @RolesAllowed({UserRoles.Admin})
    @ApiResponses(value = {
            @ApiResponse(
                    message = "Paged list of POS transactions",
                    code = 200,
                    response = Transactions.class,
                    responseContainer = "")
    })
    public Response GetPosTransaction(@QueryParam("sort")String sortBy, @QueryParam("page")Integer pageNo,
                                      @QueryParam("per_page")Integer pageSize, @QueryParam("start_date")String formStartDate, @QueryParam("end_date")String formEndDate,
                                      @QueryParam("search")String search){
        SuccessResponse success = new SuccessResponse();
        try {

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

            if(search == null || search.trim().isEmpty()) {
                DateTime today = new DateTime();
                Date endDate = formStartDate == null ? today.toDate() : formatter.parse(formEndDate);
                Date startDate = formEndDate == null ? today.minusMonths(3).toDate() : formatter.parse(formStartDate);

                PosTransaction = new PosTransactionServiceImpl();
                PaginatedDto Res = PosTransaction.AllPosTransactions(sortBy, pageNo, pageSize, startDate, endDate);
                success.setStatus(200);
                success.setData(Res);
            } else {
                PosTransaction = new PosTransactionServiceImpl();
                List<PosTransactionDto> Res = PosTransaction.SearchPosTransaction(sortBy, search);
                success.setStatus(200);
                success.setData(Res);
            }
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
    @Path("/pos/transaction/search")
    @ApiOperation(value = " ", notes = "  ", authorizations = {@Authorization(value = "authorization")})
    @RolesAllowed({UserRoles.Admin})
    @ApiResponses(value = {
            @ApiResponse(
                    message = "List of POS transactions",
                    code = 200,
                    response = PosTransactionDto.class,
                    responseContainer = "List")
    })
    public Response SearchPosTransaction(@QueryParam("sort")String sortBy, @QueryParam("search")String search){
        SuccessResponse success = new SuccessResponse();
        try {
            PosTransaction = new PosTransactionServiceImpl();
            List<PosTransactionDto> Res = PosTransaction.SearchPosTransaction(sortBy, search);
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
    @Path("/pos/transaction/{TransactionId}")
    @ApiOperation(value = "Get transaction by transaction Id", notes = "  ", authorizations = {@Authorization(value = "authorization")})
    @RolesAllowed({UserRoles.Admin})
    @ApiResponses(value = {
            @ApiResponse(
                    message = "Single POS Transaction detail",
                    code = 200,
                    response = PosTransactionDto.class)
    })
    public Response PosTransactionById(@QueryParam("TransactionId")String TransactionId){
        SuccessResponse success = new SuccessResponse();
        try {
            PosTransaction = new PosTransactionServiceImpl();
            PosTransactionDto Res = PosTransaction.SinglePosTransaction(TransactionId);
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

    @POST
    @Path("/addclient")
    @ApiOperation(value = "Admin", notes = "", authorizations = {
            @Authorization(value = "authorization")})
    @RolesAllowed({UserRoles.Admin})
    public Response AddBusiness(CreateBusinessDto businessDto) {
        SuccessResponse success = new SuccessResponse();
        try {
            String[] userMda = ExtractTokenUserMdaId(jwt);
            int creator = Integer.parseInt(userMda[0]);
            Org = new OrganisationServiceImpl();
            Org.CreateBusiness(creator, businessDto);
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
    @Path("/update_role/")
    @ApiOperation(value = "Admin", notes = "", authorizations = {
            @Authorization(value = "authorization")})
    @RolesAllowed({UserRoles.Admin})
    public Response ChangeUserRole(UserRoleUpdateDto role) {
        SuccessResponse success = new SuccessResponse();
        try {
            String[] userMda = ExtractTokenUserMdaId(jwt);
            int updatedBy = Integer.parseInt(userMda[0]);
            User = new UserServiceImpl();
            User.UpdateRole(updatedBy, role.id, role.role);
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
    @Path("/update_client")
    @ApiOperation(value = "Admin", notes = "", authorizations = {
            @Authorization(value = "authorization")})
    @RolesAllowed({UserRoles.Admin})
    public Response EditBusiness(UpdateBusinessDto businessDto) {
        SuccessResponse success = new SuccessResponse();
        try {
            String[] userMda = ExtractTokenUserMdaId(jwt);
            int updatedBy = Integer.parseInt(userMda[0]);
            Org = new OrganisationServiceImpl();
            Org.ModifyBusiness(updatedBy, businessDto);
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
    @Path("/toggle_status/{client_id}")
    @ApiOperation(value = "Admin", notes = "", authorizations = {
            @Authorization(value = "authorization")})
    @RolesAllowed({UserRoles.Admin})
    public Response BusinessStatus(@QueryParam("client_id")Integer clientId) {
        SuccessResponse success = new SuccessResponse();
        try {
            String[] userMda = ExtractTokenUserMdaId(jwt);
            int updatedBy = Integer.parseInt(userMda[0]);
            Org = new OrganisationServiceImpl();
            Org.BusinessStatus(updatedBy, clientId);
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

    @GET
    @Path("/clients")
    @ApiOperation(value = "Admin", notes = "", authorizations = {
            @Authorization(value = "authorization")}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    message = "Paged list of Clients",
                    code = 200,
                    response = BusinessDto.class,
                    responseContainer = "List")
    })
    @RolesAllowed({UserRoles.Admin, UserRoles.SubAdmin1})
    public Response GetBusinesses(@QueryParam("sort")String sortBy, @QueryParam("page")Integer pageNo,
                                  @QueryParam("per_page")Integer pageSize,
                                  @QueryParam("search")String search){
        SuccessResponse success = new SuccessResponse();
        try {
            if(search == null || search.trim().isEmpty()){
            Org = new OrganisationServiceImpl();
            PaginatedDto page = Org.GetAllBusinessPaged(sortBy, pageNo, pageSize);
            success.setStatus(200);
            success.setData(page);
            } else{
                Org = new OrganisationServiceImpl();
                List<BusinessDto> Res = Org.SearchBusiness(search, sortBy);
                success.setStatus(200);
                success.setData(Res);
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
    @Path("/clients/search")
    @ApiOperation(value = " ", notes = "  ", authorizations = {@Authorization(value = "authorization")})
//    @RolesAllowed({UserRoles.Admin})
    @RolesAllowed({UserRoles.Admin, UserRoles.SubAdmin1})
    @ApiResponses(value = {
            @ApiResponse(
                    message = "List of Projects",
                    code = 200,
                    responseContainer = "List")
    })
    public Response SearchBusiness(@QueryParam("sort")String sortBy, @QueryParam("search")String search){
        SuccessResponse success = new SuccessResponse();
        try {
            Org = new OrganisationServiceImpl();
            List<BusinessDto> Res = Org.SearchBusiness(search, sortBy);
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
    @Path("/client")
    @ApiOperation(value = "Admin", notes = "", authorizations = {
            @Authorization(value = "authorization")}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    message = "Single Client",
                    code = 200,
                    response = BusinessDto.class,
                    responseContainer = "")
    })
    @RolesAllowed({UserRoles.Admin, UserRoles.SubAdmin1})
    public Response GetBusiness(@QueryParam("client_id")Integer ClientId){
        SuccessResponse success = new SuccessResponse();
        try {
            Org = new OrganisationServiceImpl();
            BusinessDto page = Org.SingleBusiness(ClientId, false);
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
    @Path("/states-summary")
    @ApiOperation(value = "Admin", notes = "", authorizations = {
            @Authorization(value = "authorization")}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    message = "States",
                    code = 200,
                    response = BusinessDto.class,
                    responseContainer = "")
    })
    @RolesAllowed({UserRoles.Admin, UserRoles.SubAdmin1})
    public Response GetBusinessAlt(){
        SuccessResponse success = new SuccessResponse();
        try {

            List<String> roleListAg = new LinkedList<>();
            roleListAg.add(UserRoles.Agent);

            List<String> roleListSb = new LinkedList<>();
            roleListSb.add(UserRoles.SubAdmin2);
            roleListSb.add(UserRoles.ProjectReport);

            Org = new OrganisationServiceImpl();
            User = new UserServiceImpl();

            AllProjectSummaryDto ps = new AllProjectSummaryDto();
            ps.states = Org.CountProjects();
            ps.agents = User.CountUsers(roleListAg, 0, 0, true);
            ps.subAdmins = User.CountUsers(roleListSb, 0, 0, false);
            ps.agentSubAdmins = ps.agents + ps.subAdmins;
            ps.revenueGen = Org.AllBusinessPerformance().getTotalAmountPaid();


            success.setStatus(200);
            success.setData(ps);
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
    @Path("/mdas-summary")
    @ApiOperation(value = "Admin", notes = "", authorizations = {
            @Authorization(value = "authorization")}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    message = "States",
                    code = 200,
                    response = BusinessDto.class,
                    responseContainer = "")
    })
    @RolesAllowed({UserRoles.Admin, UserRoles.SubAdmin1})
    public Response GetMdaAlt(@QueryParam("client_id")Integer ClientId, @QueryParam("Project_Id")Integer formProjectId, @QueryParam("Mda_Id")Integer formMdaId){
        SuccessResponse success = new SuccessResponse();
        try {

            List<String> roleListAg = new LinkedList<>();
            roleListAg.add(UserRoles.Agent);

            List<String> roleListSb = new LinkedList<>();
            roleListSb.add(UserRoles.SubAdmin2);
            roleListSb.add(UserRoles.ProjectReport);

            Org = new OrganisationServiceImpl();
            User = new UserServiceImpl();

            AllProjectSummaryDto ps = new AllProjectSummaryDto();
            ps.states = Org.CountProjects();
            ps.agents = User.CountUsers(roleListAg, 0, 0, true);
            ps.subAdmins = User.CountUsers(roleListSb, 0, 0, true);
            ps.agentSubAdmins = ps.agents + ps.subAdmins;
            ps.revenueGen = Org.AllBusinessPerformance().getTotalAmountPaid();


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

    @GET
    @Path("/list-audit-logs")
    @RolesAllowed({UserRoles.SubAdmin1, UserRoles.Admin})
    @ApiOperation(value = "Admin, Sub Admin1", notes = "", authorizations = {@Authorization(value = "authorization")})
    public Response ListChangeRequests(@QueryParam("sort")String sortBy, @QueryParam("page")Integer pageNo,
                                       @QueryParam("per_page")Integer pageSize, @QueryParam("business_id")int bizId) {
        SuccessResponse success = new SuccessResponse();
        try {

            String[] userMda = ExtractTokenUserMdaId(jwt);

//            Assess = new AssessmentServiceImpl();

            int MdaProjId = Integer.parseInt(userMda[2]);
            int filterId = 0;
            Org = new OrganisationServiceImpl();
            Object[] roleCheck = jwt.getGroups().toArray();
            switch (roleCheck[0].toString()){
                case UserRoles.Admin:
                    if(bizId > 0){
                        filterId = bizId;
                        break;
                    }
                    filterId = 0;
                    break;
                case UserRoles.SubAdmin1:
                    filterId = MdaProjId;
                    break;
            }

            PaginatedDto page = Org.ListActivityLogs(sortBy, pageNo, pageSize, filterId);
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



    @POST
    @Path("/new-payment-channels")
    @RolesAllowed({UserRoles.Admin})
    @ApiOperation(value = "Admin, Sub Admin1", notes = "", authorizations = {@Authorization(value = "authorization")})
    public Response AddPaymentChannel(String name) {
        SuccessResponse success = new SuccessResponse();
        try {
            String[] userMda = ExtractTokenUserMdaId(jwt);
            Org = new OrganisationServiceImpl();
            Org.NewPaymentChannel(name, Integer.parseInt(userMda[0]));

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

    @GET
    @Path("/payment-channel/change-status")
    @RolesAllowed({UserRoles.Admin})
    @ApiOperation(value = "Admin, Sub Admin1", notes = "", authorizations = {@Authorization(value = "authorization")})
    public Response PaymentChannelStatus(@QueryParam("id")int id, @QueryParam("business_id")int businessId) {
        SuccessResponse success = new SuccessResponse();
        try {
            String[] userMda = ExtractTokenUserMdaId(jwt);
            Org = new OrganisationServiceImpl();
            if(businessId > 0) {
                Org.PaymentChannelStatus(id, Integer.parseInt(userMda[0]));
            } else {
                Org.BusPaymentChannelStatus(id, Integer.parseInt(userMda[0]));
            }

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

    @GET
    @Path("/payment-channels")
    @RolesAllowed({UserRoles.Admin})
    @ApiOperation(value = "Admin, Sub Admin1", notes = "", authorizations = {@Authorization(value = "authorization")})
    public Response PaymentChannels(@QueryParam("business_id")int businessId) {
        SuccessResponse success = new SuccessResponse();
        try {
            Org = new OrganisationServiceImpl();
            if(businessId > 0) {
                success.setData(Org.AllBusPaymentChannels(businessId));
            } else {
                success.setData(Org.AllPaymentChannels());
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



    @POST
    @Path("/client/new-payment-channels")
    @RolesAllowed({UserRoles.Admin})
    @ApiOperation(value = "Admin, Sub Admin1", notes = "", authorizations = {@Authorization(value = "authorization")})
    public Response AddBusPaymentChannel(CreateBusPayChannelDto cbd) {
        SuccessResponse success = new SuccessResponse();
        try {
            String[] userMda = ExtractTokenUserMdaId(jwt);
            Org = new OrganisationServiceImpl();
            Org.NewBusPaymentChannel(cbd, Integer.parseInt(userMda[0]));
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

//    @GET
//    @Path("/client/payment-channel/change-status")
//    @RolesAllowed({UserRoles.Admin})
//    @ApiOperation(value = "Admin, Sub Admin1", notes = "", authorizations = {@Authorization(value = "authorization")})
//    public Response BusPaymentChannelStatus(@QueryParam("id")int id) {
//        SuccessResponse success = new SuccessResponse();
//        try {
//            Org = new OrganisationServiceImpl();
//            Org.PaymentChannelStatus(id);
//
//            success.setStatus(200);
//            success.setData("Success");
//            return Response.ok(success).build();
//        } catch (Exception e) {
//            System.out.println("Errr" + e.getMessage());
//            e.printStackTrace();
//            success.setStatus(400);
//            success.setData(e.getMessage());
//            success.setMessage("Bad Request");
//            return Response.status(400)
//                    .type(MediaType.APPLICATION_JSON_TYPE)
//                    .entity(success)
//                    .build();
//        }
//
//    }
//
//    @GET
//    @Path("/client/payment-channels")
//    @RolesAllowed({UserRoles.Admin})
//    @ApiOperation(value = "Admin, Sub Admin1", notes = "", authorizations = {@Authorization(value = "authorization")})
//    public Response BusPaymentChannels() {
//        SuccessResponse success = new SuccessResponse();
//        try {
//            Org = new OrganisationServiceImpl();
//
//            success.setStatus(200);
//            success.setData(Org.AllPaymentChannels());
//            return Response.ok(success).build();
//        } catch (Exception e) {
//            System.out.println("Errr" + e.getMessage());
//            e.printStackTrace();
//            success.setStatus(400);
//            success.setData(e.getMessage());
//            success.setMessage("Bad Request");
//            return Response.status(400)
//                    .type(MediaType.APPLICATION_JSON_TYPE)
//                    .entity(success)
//                    .build();
//        }
//
//    }


//    @GET
//    @Path("/mdas")
//    @ApiOperation(value = "Admin", notes = "", authorizations = {
//            @Authorization(value = "authorization")}
//    )
//    @ApiResponses(value = {
//            @ApiResponse(
//                    message = "Paged list of MDAs",
//                    code = 200,
//                    response = Mdas.class,
//                    responseContainer = "")
//    })
//    @RolesAllowed({UserRoles.Admin, UserRoles.SubAdmin1})
//    public Response GetMdas(@QueryParam("sort")String sortBy, @QueryParam("page")Integer pageNo,
//                            @QueryParam("per_page")Integer pageSize, @QueryParam("project_id")Integer ClientId){
//        SuccessResponse success = new SuccessResponse();
//        try {
//            Org = new OrganisationServiceImpl();
//            PaginatedDto page = Org.AllProjectMdaPaged(ClientId, sortBy, pageNo, pageSize);
//            success.setStatus(200);
//            success.setData(page);
//            return Response.ok(success).build();
//        } catch (Exception e) {
//            System.out.println("Errr" + e.getMessage());
//            e.printStackTrace();
//            success.setStatus(400);
//            success.setData(e.getMessage());
//            success.setMessage("Bad Request");
//            return Response.status(400)
//                    .type(MediaType.APPLICATION_JSON_TYPE)
//                    .entity(success)
//                    .build();
//        }
//
//    }
//
//    @GET
//    @Path("/mdaoffices")
//    @ApiResponses(value = {
//            @ApiResponse(
//                    message = "Paged list of MDA Offices",
//                    code = 200,
//                    response = Offices.class,
//                    responseContainer = "")
//    })
//    @ApiOperation(value = "Admin, Sub-Admin1, Sub-Admin2 ", notes = " ", authorizations = {
//            @Authorization(value = "authorization")})
//    @RolesAllowed({UserRoles.Admin, UserRoles.SubAdmin1, UserRoles.SubAdmin2})
//    public Response GetMdaOffices(@QueryParam("sort")String sortBy, @QueryParam("page")Integer pageNo,
//                                  @QueryParam("per_page")Integer pageSize, @QueryParam("mda_id")Integer mdaId){
//        SuccessResponse success = new SuccessResponse();
//        try {
//            String[] userMda = ExtractTokenUserMdaId(jwt);
//            int mda = mdaId <= 0  ? Integer.parseInt(userMda[1]) : mdaId;
//            Org = new OrganisationServiceImpl();
////            PaginatedDto page = Org.AllMdaOfficePaged(Integer.parseInt(userMda[1]), sortBy, pageNo, pageSize);
//            PaginatedDto page = Org.AllMdaOfficePaged(mda, sortBy, pageNo, pageSize);
//            success.setStatus(200);
//            success.setData(page);
//            return Response.ok(success).build();
//        } catch (Exception e) {
//            System.out.println("Errr" + e.getMessage());
//            e.printStackTrace();
//            success.setStatus(400);
//            success.setData(e.getMessage());
//            success.setMessage("Bad Request");
//            return Response.status(400)
//                    .type(MediaType.APPLICATION_JSON_TYPE)
//                    .entity(success)
//                    .build();
//        }
//
//    }
//
//    @GET
//    @Path("/mdaservices")
//    @ApiResponses(value = {
//            @ApiResponse(
//                    message = "Paged list of MDA Services",
//                    code = 200,
//                    response = Services.class,
//                    responseContainer = "")
//    })
//    @ApiOperation(value = " Admin, Sub-Admin1, Sub-Admin2", notes = " ", authorizations = {
//            @Authorization(value = "authorization")})
//    @RolesAllowed({UserRoles.Admin, UserRoles.SubAdmin1, UserRoles.SubAdmin2})
//    public Response GetMdaServices(@QueryParam("sort")String sortBy, @QueryParam("page")Integer pageNo,
//                                   @QueryParam("per_page")Integer pageSize, @QueryParam("mda_id")Integer mdaId){
//        SuccessResponse success = new SuccessResponse();
//        try {
//            String[] userMda = ExtractTokenUserMdaId(jwt);
//            int mda = mdaId <= 0  ? Integer.parseInt(userMda[1]) : mdaId;
//            Org = new OrganisationServiceImpl();
//            PaginatedDto page = Org.AllMdaServicePage(mda, sortBy, pageNo, pageSize);
//            success.setStatus(200);
//            success.setData(page);
//            return Response.ok(success).build();
//        } catch (Exception e) {
//            System.out.println("Errr" + e.getMessage());
//            e.printStackTrace();
//            success.setStatus(400);
//            success.setData(e.getMessage());
//            success.setMessage("Bad Request");
//            return Response.status(400)
//                    .type(MediaType.APPLICATION_JSON_TYPE)
//                    .entity(success)
//                    .build();
//        }
//
//    }
//
//
//    @GET
//    @Path("/mdas/search")
//    @ApiResponses(value = {
//            @ApiResponse(
//                    message = "list of MDAs",
//                    code = 200,
//                    response = MdaDto.class,
//                    responseContainer = "List")
//    })
//    @ApiOperation(value = "Admin, Sub-Admin1", notes = " ", authorizations = {
//            @Authorization(value = "authorization")})
//    @RolesAllowed({UserRoles.Admin, UserRoles.SubAdmin1})
//    public Response GetMdas(@QueryParam("sort")String sortBy, @QueryParam("search")String search, @QueryParam("project_id")Integer ClientId){
//        SuccessResponse success = new SuccessResponse();
//        try {
//            Org = new OrganisationServiceImpl();
//            List<MdaDto> page = Org.SerchedProjectMda(ClientId, search, sortBy);
//            success.setStatus(200);
//            success.setData(page);
//            return Response.ok(success).build();
//        } catch (Exception e) {
//            System.out.println("Errr" + e.getMessage());
//            e.printStackTrace();
//            success.setStatus(400);
//            success.setData(e.getMessage());
//            success.setMessage("Bad Request");
//            return Response.status(400)
//                    .type(MediaType.APPLICATION_JSON_TYPE)
//                    .entity(success)
//                    .build();
//        }
//
//    }
//
//    @GET
//    @Path("/mdaoffices/search")
//    @ApiResponses(value = {
//            @ApiResponse(
//                    message = "list of offices",
//                    code = 200,
//                    response = MdaOfficeDto.class,
//                    responseContainer = "List")
//    })
//    @ApiOperation(value = " Admin, Sub-Admin1, Sub-Admin2 ", notes = " ", authorizations = {
//            @Authorization(value = "authorization")})
//    @RolesAllowed({UserRoles.Admin, UserRoles.SubAdmin1, UserRoles.SubAdmin2})
//    public Response SearchMdaOffices(@QueryParam("sort")String sortBy, @QueryParam("search")String search, @QueryParam("mda_id")Integer mdaId){
//        SuccessResponse success = new SuccessResponse();
//        try {
//            String[] userMda = ExtractTokenUserMdaId(jwt);
//            int mda = mdaId <= 0  ? Integer.parseInt(userMda[1]) : mdaId;
//            Org = new OrganisationServiceImpl();
//            List<MdaOfficeDto> page = Org.SearcheddaOffice(mda, search, sortBy);
//            success.setStatus(200);
//            success.setData(page);
//            return Response.ok(success).build();
//        } catch (Exception e) {
//            System.out.println("Errr" + e.getMessage());
//            e.printStackTrace();
//            success.setStatus(400);
//            success.setData(e.getMessage());
//            success.setMessage("Bad Request");
//            return Response.status(400)
//                    .type(MediaType.APPLICATION_JSON_TYPE)
//                    .entity(success)
//                    .build();
//        }
//
//    }
//
//    @GET
//    @Path("/mdaservices/search")
//    @ApiResponses(value = {
//            @ApiResponse(
//                    message = "list of MDA Services",
//                    code = 200,
//                    response = MdaServiceDto.class,
//                    responseContainer = "List")
//    })
//    @ApiOperation(value = "Admin, Sub-Admin1, Sub-Admin2 ", notes = "", authorizations = {
//            @Authorization(value = "authorization")})
//    @RolesAllowed({UserRoles.Admin, UserRoles.SubAdmin1, UserRoles.SubAdmin2})
//    public Response SearchMdaServices(@QueryParam("sort")String sortBy, @QueryParam("search")String search, @QueryParam("mda_id")Integer mdaId){
//        SuccessResponse success = new SuccessResponse();
//        try {
//            String[] userMda = ExtractTokenUserMdaId(jwt);
//            int mda = mdaId <= 0  ? Integer.parseInt(userMda[1]) : mdaId;
//            Org = new OrganisationServiceImpl();
//            List<MdaServiceDto> page = Org.SearchedMdaService(mda, search, sortBy);
//            success.setStatus(200);
//            success.setData(page);
//            return Response.ok(success).build();
//        } catch (Exception e) {
//            System.out.println("Errr" + e.getMessage());
//            e.printStackTrace();
//            success.setStatus(400);
//            success.setData(e.getMessage());
//            success.setMessage("Bad Request");
//            return Response.status(400)
//                    .type(MediaType.APPLICATION_JSON_TYPE)
//                    .entity(success)
//                    .build();
//        }
//
//    }



}
