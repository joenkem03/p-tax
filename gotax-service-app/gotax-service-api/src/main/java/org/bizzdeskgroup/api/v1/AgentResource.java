package org.bizzdeskgroup.api.v1;

import io.swagger.annotations.*;
import org.bizzdeskgroup.Dto.SuccessResponse;
import org.bizzdeskgroup.Dto.SwaggerDocResponse.Remittances;
import org.bizzdeskgroup.Dto.SwaggerDocResponse.Services;
import org.bizzdeskgroup.Dto.SwaggerDocResponse.Transactions;
import org.bizzdeskgroup.Dtos.Command.ActivatePosDto;
import org.bizzdeskgroup.Dtos.Command.CreatePosTransactionDto;
import org.bizzdeskgroup.Dtos.Command.GenerateRemittanceDto;
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
import java.util.List;
import java.util.Locale;

import static org.bizzdeskgroup.helper.UserTokenUtil.ExtractTokenUserMdaId;

@Path("agent/pos")
@Api(value = "POS Agent")
@RequestScoped
@Produces("application/json")
@Consumes("application/json")
public class AgentResource {
    UserService User = null;
    PosService Pos = null;
    OrganisationService Org = null;
    PosTransactionService PosTransaction = null;
    TransactionService Transactions = null;
    AssessmentService Assess = null;

    @Inject
    JsonWebToken jwt;


    @POST
    @Path("/activate")
//    @RolesAllowed({UserRoles.Agent})
//    @ApiOperation(value = "Activate Pos", notes = "", authorizations = {@Authorization(value = "authorization")})
    @ApiOperation(value = "Activate Pos", notes = " ")
    @ApiResponses(value = {
            @ApiResponse(
                    message = "Successful POS transaction",
                    code = 200,
                    response = PosActivationResponse.class)
    })
    public Response PosAccess(ActivatePosDto posActiveDto){
        SuccessResponse success = new SuccessResponse();
        try {
            Pos = new PosServiceImpl();
//            Pos.ActivateByUser(ExtractTokenUserId(jwt), posActiveDto);
            PosActivationResponse res = Pos.ActivateByUser(1, posActiveDto);
            success.setStatus(200);
            success.setData(res);
            return Response.ok(success).build();
        } catch (Exception e) {
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
    @Path("/card_transaction")
    @ApiOperation(value = " ", notes = "  ", authorizations = {@Authorization(value = "authorization")})
    @RolesAllowed({UserRoles.Agent})
    @ApiResponses(value = {
            @ApiResponse(
                    message = "Paged list of Agent's Card POS transactions",
                    code = 200,
                    response = Transactions.class)
    })
    public Response GetCardPosTransaction(@QueryParam("sort")String sortBy, @QueryParam("page")Integer pageNo,
                                          @QueryParam("apply_filter")boolean applyFilter, @QueryParam("filter_by")String filterBy,
                                          @QueryParam("filter")String filterValue,
                                      @QueryParam("per_page")Integer pageSize, @QueryParam("start_date")String formStartDate, @QueryParam("end_date")String formEndDate,
                                          @QueryParam("search")String search){
        SuccessResponse success = new SuccessResponse();
        try {

            if(search == null || search.trim().isEmpty()) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

                DateTime today = new DateTime();
                Date endDate = formStartDate == null ? today.toDate() : formatter.parse(formEndDate);
                Date startDate = formEndDate == null ? today.minusMonths(3).toDate() : formatter.parse(formStartDate);

                String[] userMda = ExtractTokenUserMdaId(jwt);
                Transactions = new TransactionServiceImpl();
                PaginatedDto Res = Transactions.AllAgentTransactions(Integer.parseInt(userMda[0]), Integer.parseInt(userMda[1]), Integer.parseInt(userMda[2]), sortBy, pageNo, pageSize, applyFilter, filterBy, filterValue, startDate, endDate);
                success.setStatus(200);
                success.setData(Res);
            } else {
                String[] userMda = ExtractTokenUserMdaId(jwt);
                Transactions = new TransactionServiceImpl();
                List<TransactionDto> Res = Transactions.SearchAgentTransactions(Integer.parseInt(userMda[0]), Integer.parseInt(userMda[1]), Integer.parseInt(userMda[2]), search, sortBy);
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
    @Path("/card_transaction/search")
    @ApiOperation(value = " ", notes = "  ", authorizations = {@Authorization(value = "authorization")})
    @RolesAllowed({UserRoles.Agent})
    @ApiResponses(value = {
            @ApiResponse(
                    message = "list of Agent's Card POS transactions",
                    code = 200,
                    response = org.bizzdeskgroup.models.PosTransaction.class,
                    responseContainer = "List")
    })
    public Response SearchCardPosTransaction(@QueryParam("sort")String sortBy, @QueryParam("search")String search){
        SuccessResponse success = new SuccessResponse();
        try {
            String[] userMda = ExtractTokenUserMdaId(jwt);
            Transactions = new TransactionServiceImpl();
            List<TransactionDto> Res = Transactions.SearchAgentTransactions(Integer.parseInt(userMda[0]), Integer.parseInt(userMda[1]),Integer.parseInt(userMda[2]), search, sortBy);
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
    @Path("/card_transaction/{TransactionId}")
    @ApiResponses(value = {
            @ApiResponse(
                    message = "Single Agent's Card POS transactions",
                    code = 200,
                    response = PosTransactionDto.class)
    })
    @ApiOperation(value = "Get transaction by transaction Id", notes = "  ", authorizations = {@Authorization(value = "authorization")})
    @RolesAllowed({UserRoles.Agent})
    public Response CardPosTransactionById(@QueryParam("TransactionId")String TransactionId){
        SuccessResponse success = new SuccessResponse();
        try {
            String[] userMda = ExtractTokenUserMdaId(jwt);
            Transactions = new TransactionServiceImpl();
            TransactionDto Res = Transactions.SingleAgentTransactions(TransactionId, Integer.parseInt(userMda[2]), Integer.parseInt(userMda[1]),Integer.parseInt(userMda[0]));
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
    @Path("/transaction")
    @ApiOperation(value = " ", notes = "  ", authorizations = {@Authorization(value = "authorization")})
    @RolesAllowed({UserRoles.Agent})
    @ApiResponses(value = {
            @ApiResponse(
                    message = "Successful POS transaction",
                    code = 200,
                    response = PosTransactionResponseDto.class)
    })
    public Response PosTransaction(CreatePosTransactionDto posTransactionDto){
        SuccessResponse success = new SuccessResponse();

        try {
            String[] userMda = ExtractTokenUserMdaId(jwt);

            PosTransaction = new PosTransactionServiceImpl();
            PosTransactionResponseDto Res = PosTransaction.NewPosTransaction(Integer.parseInt(userMda[0]), posTransactionDto, Integer.parseInt(userMda[2]));
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
    @Path("/transaction/cash-at-hand")
    @ApiOperation(value = " ", notes = "  ", authorizations = {@Authorization(value = "authorization")})
    @ApiResponses(value = {
            @ApiResponse(
                    message = "",
                    code = 200,
                    response = AgentCash.class)
    })
    @RolesAllowed({UserRoles.Agent})
    public Response GetPendingAgentSum(){
        SuccessResponse success = new SuccessResponse();
        try {
            String[] userMda = ExtractTokenUserMdaId(jwt);
            PosTransaction = new PosTransactionServiceImpl();
            AgentCash Res = PosTransaction.AgentCashAtHand(Integer.parseInt(userMda[0]), Integer.parseInt(userMda[1]));
            success.setStatus(200);
            if(Res == null) {
                AgentCash empty = new AgentCash();
                empty.setAmount(0.00);
                empty.setCount(0);

                success.setData(empty);
            } else {
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
    @Path("/transaction/agents/cash-at-hand")
    @ApiOperation(value = "If request is from Admin, project ID is required ", notes = "  ", authorizations = {@Authorization(value = "authorization")})
    @ApiResponses(value = {
            @ApiResponse(
                    message = "",
                    code = 200,
                    response = AgentCash.class)
    })
    @RolesAllowed({UserRoles.Agent})
    public Response GetAgentsPendingAgentSum(@QueryParam("sort")String sortBy, @QueryParam("page")Integer pageNo,
                                             @QueryParam("per_page")Integer pageSize, @QueryParam("project_id")Integer formProject){
        SuccessResponse success = new SuccessResponse();
        try {
//            String[] userMda = ExtractTokenUserMdaId(jwt);

            String[] userMda = ExtractTokenUserMdaId(jwt);

            int UserId = Integer.parseInt(userMda[0]);
            int MdaId = Integer.parseInt(userMda[1]);
            int MdaProjId = Integer.parseInt(userMda[2]);
            int filterId = 0;
//            String filterBy = "";
            Object[] roleCheck = jwt.getGroups().toArray();
            switch (roleCheck[0].toString()){
                case UserRoles.SubAdmin1:
                case UserRoles.ProjectReport:
//                    filterBy = "Project";
                    filterId = MdaProjId;
                    break;
                case UserRoles.SubAdmin2:
                case UserRoles.Agent:
//                    filterBy = "Mda";
                    filterId = MdaId;
                    break;
                case UserRoles.Admin:
//                    filterBy = "Payer";
                    filterId = formProject;
                    break;
            }
            PosTransaction = new PosTransactionServiceImpl();
            PaginatedDto Res = PosTransaction.AgentCashAtHandList(sortBy, pageNo, pageSize, roleCheck[0].toString(), filterId);

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
    @Path("/transaction")
    @ApiOperation(value = " ", notes = "  ", authorizations = {@Authorization(value = "authorization")})
    @RolesAllowed({UserRoles.Agent})
    @ApiResponses(value = {
            @ApiResponse(
                    message = "Paged list of POS transactions",
                    code = 200,
                    response = Transactions.class)
    })
    public Response GetPosTransaction(@QueryParam("sort")String sortBy, @QueryParam("page")Integer pageNo,
                                      @QueryParam("per_page")Integer pageSize, @QueryParam("start_date")String formStartDate, @QueryParam("end_date")String formEndDate,
                                      @QueryParam("search")String search){
        SuccessResponse success = new SuccessResponse();
        try {


            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

            if (search == null || search.trim().isEmpty()) {
                DateTime today = new DateTime();
                Date endDate = formStartDate == null ? today.toDate() : formatter.parse(formEndDate);
                Date startDate = formEndDate == null ? today.minusMonths(3).toDate() : formatter.parse(formStartDate);

                String[] userMda = ExtractTokenUserMdaId(jwt);
                PosTransaction = new PosTransactionServiceImpl();


                PaginatedDto Res = null;
                Object[] roleCheck = jwt.getGroups().toArray();
                switch (roleCheck[0].toString()) {
                    case UserRoles.SubAdmin1:
                    case UserRoles.ProjectReport:
                        Res = PosTransaction.AgentPosTransactions(Integer.parseInt(userMda[2]), 0, 0, 0, sortBy, pageNo, pageSize, startDate, endDate);
                        break;
                    case UserRoles.SubAdmin2:
                        Res = PosTransaction.AgentPosTransactions(0, Integer.parseInt(userMda[1]), 0, 0, sortBy, pageNo, pageSize, startDate, endDate);
                        break;
                    case UserRoles.Agent:
                        Res = PosTransaction.AgentPosTransactions(0, 0, Integer.parseInt(userMda[0]), 0, sortBy, pageNo, pageSize, startDate, endDate);
                        break;
                    case UserRoles.Individual:
                    case UserRoles.NonIndividual:
                        Res = PosTransaction.AgentPosTransactions(0, 0, 0, Integer.parseInt(userMda[0]), sortBy, pageNo, pageSize, startDate, endDate);
                        break;
                    case UserRoles.Admin:
                        Res = PosTransaction.AgentPosTransactions(0, 0, 0, 0, sortBy, pageNo, pageSize, startDate, endDate);
                        break;
                }

                success.setStatus(200);
                success.setData(Res);
            } else {
                String[] userMda = ExtractTokenUserMdaId(jwt);
                PosTransaction = new PosTransactionServiceImpl();
                List<PosTransactionDto> Res = null;

                Object[] roleCheck = jwt.getGroups().toArray();
                switch (roleCheck[0].toString()) {
                    case UserRoles.SubAdmin1:
                    case UserRoles.ProjectReport:
                        Res = PosTransaction.SearchAgentPosTransaction(Integer.parseInt(userMda[2]), 0, 0, 0, sortBy, search);
                        break;
                    case UserRoles.SubAdmin2:
                        Res = PosTransaction.SearchAgentPosTransaction(0, Integer.parseInt(userMda[1]), 0, 0, sortBy, search);
                        break;
                    case UserRoles.Agent:
                        Res = PosTransaction.SearchAgentPosTransaction(0, 0, Integer.parseInt(userMda[1]),  0, sortBy, search);
                        break;
                    case UserRoles.Individual:
                    case UserRoles.NonIndividual:
                        Res = PosTransaction.SearchAgentPosTransaction(0, 0, 0, Integer.parseInt(userMda[1]), sortBy, search);
                        break;
                    case UserRoles.Admin:
                        Res = PosTransaction.SearchAgentPosTransaction(0, 0, 0, 0, sortBy, search);
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
            success.setMessage("Bad Request");
            success.setData(e.getMessage());
            return Response.status(400)
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .entity(success)
                    .build();
        }
    }

    @GET
    @Path("/transaction/search")
    @ApiOperation(value = " ", notes = "  ", authorizations = {@Authorization(value = "authorization")})
    @RolesAllowed({UserRoles.Agent})
    @ApiResponses(value = {
            @ApiResponse(
                    message = "list of POS transactions",
                    code = 200,
                    response = org.bizzdeskgroup.models.PosTransaction.class,
                    responseContainer = "List")
    })
    public Response SearchPosTransaction(@QueryParam("sort")String sortBy, @QueryParam("search")String search){
        SuccessResponse success = new SuccessResponse();
        try {
            String[] userMda = ExtractTokenUserMdaId(jwt);
            PosTransaction = new PosTransactionServiceImpl();
            List<PosTransactionDto> Res = PosTransaction.SearchAgentPosTransaction(0, Integer.parseInt(userMda[1]), 0, 0, sortBy, search);
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
    @Path("/transaction/{TransactionId}")
    @ApiResponses(value = {
            @ApiResponse(
                    message = "Single POS transactions",
                    code = 200,
                    response = PosTransactionDto.class)
    })
    @ApiOperation(value = "Get transaction by transaction Id", notes = "  ", authorizations = {@Authorization(value = "authorization")})
    @RolesAllowed({UserRoles.Agent})
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
            success.setMessage("Bad Request");
            success.setData(e.getMessage());
            return Response.status(400)
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .entity(success)
                    .build();
        }
    }

    @POST
    @Path("/remittance")
    @ApiOperation(value = " ", notes = "  ", authorizations = {@Authorization(value = "authorization")})
    @RolesAllowed({UserRoles.Agent})
    @ApiResponses(value = {
            @ApiResponse(
                    message = "Successful remittance generation",
                    code = 200,
                    response = RemittanceResponseDto.class)
    })
    public Response GenerateRemittance(GenerateRemittanceDto posTransactionDto){
        SuccessResponse success = new SuccessResponse();
        try {
            String[] userMda = ExtractTokenUserMdaId(jwt);
            PosTransaction = new PosTransactionServiceImpl();
            RemittanceResponseDto Res = PosTransaction.NewRemittance(Integer.parseInt(userMda[0]), posTransactionDto, Integer.parseInt(userMda[1]), Integer.parseInt(userMda[3]));
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
    @Path("/remittance")
    @ApiOperation(value = " ", notes = "  ", authorizations = {@Authorization(value = "authorization")})
    @ApiResponses(value = {
            @ApiResponse(
                    message = "Paged list of remittance",
                    code = 200,
                    response = Remittances.class)
    })
    @RolesAllowed({UserRoles.Agent})
    public Response GetRemittance(@QueryParam("sort")String sortBy, @QueryParam("page")Integer pageNo,
                                      @QueryParam("per_page")Integer pageSize, @QueryParam("start_date")String formStartDate, @QueryParam("end_date")String formEndDate,
                                  @QueryParam("search")String search){
        SuccessResponse success = new SuccessResponse();
        try {


            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

            if(search == null || search.trim().isEmpty()) {
                DateTime today = new DateTime();
                Date endDate = formStartDate == null ? today.toDate() : formatter.parse(formEndDate);
                Date startDate = formEndDate == null ? today.minusMonths(3).toDate() : formatter.parse(formStartDate);

                String[] userMda = ExtractTokenUserMdaId(jwt);
                PosTransaction = new PosTransactionServiceImpl();
                PaginatedDto Res = PosTransaction.AgentRemittance(Integer.parseInt(userMda[0]), Integer.parseInt(userMda[1]), sortBy, pageNo, pageSize, startDate, endDate);
                success.setStatus(200);
                success.setData(Res);
            } else {
                String[] userMda = ExtractTokenUserMdaId(jwt);
                PosTransaction = new PosTransactionServiceImpl();
                List<RemittanceDto> Res = PosTransaction.SearchAgentRemittance(Integer.parseInt(userMda[1]), Integer.parseInt(userMda[0]), sortBy, search);
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
    @Path("/remittance/search")
    @ApiOperation(value = " ", notes = "  ", authorizations = {@Authorization(value = "authorization")})
    @RolesAllowed({UserRoles.Agent})
    @ApiResponses(value = {
            @ApiResponse(
                    message = "list of Agent remittance",
                    code = 200,
                    response = org.bizzdeskgroup.models.PosTransaction.class,
                    responseContainer = "List")
    })
    public Response SearchRemittance(@QueryParam("sort")String sortBy, @QueryParam("search")String search){
        SuccessResponse success = new SuccessResponse();
        try {
            String[] userMda = ExtractTokenUserMdaId(jwt);
            PosTransaction = new PosTransactionServiceImpl();
            List<RemittanceDto> Res = PosTransaction.SearchAgentRemittance(Integer.parseInt(userMda[1]), Integer.parseInt(userMda[0]), sortBy, search);
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
    @Path("/mdaservices")
    @ApiResponses(value = {
            @ApiResponse(
                    message = "Paged list of MDA Services",
                    code = 200,
                    response = Services.class,
                    responseContainer = "")
    })
    @RolesAllowed({UserRoles.Agent, UserRoles.Individual, UserRoles.NonIndividual})
    @ApiOperation(value = " ", notes = " ", authorizations = {
            @Authorization(value = "authorization")})
    public Response GetMdaServices(@QueryParam("sort")String sortBy, @QueryParam("page")Integer pageNo,
                                   @QueryParam("per_page")Integer pageSize,
                                   @QueryParam("search")String search){
        SuccessResponse success = new SuccessResponse();
        try {
            if(search == null || search.trim().isEmpty()) {
                String[] userMda = ExtractTokenUserMdaId(jwt);
                boolean isAgent = false;
                int buzz = 0;
                Object[] roleCheck = jwt.getGroups().toArray();

                if (roleCheck[0].toString().equals(UserRoles.Agent)) {
                    isAgent = true;
                }
                if (roleCheck[0].toString().equals(UserRoles.Individual) || roleCheck[0].toString().equals(UserRoles.NonIndividual)) {
                    buzz = Integer.parseInt(userMda[2]);
                }
                Org = new OrganisationServiceImpl();
                PaginatedDto page = Org.AllMdaServicePage(Integer.parseInt(userMda[1]), sortBy, pageNo, pageSize, isAgent, buzz);
                success.setStatus(200);
                success.setData(page);
            } else {
                String[] userMda = ExtractTokenUserMdaId(jwt);

                boolean isAgent = false;
                Object[] roleCheck = jwt.getGroups().toArray();
                int buzz = 0;

                if (roleCheck[0].toString().equals(UserRoles.Agent)) {
                    isAgent = true;
                }
                if (roleCheck[0].toString().equals(UserRoles.Individual) || roleCheck[0].toString().equals(UserRoles.NonIndividual)) {
                    buzz = Integer.parseInt(userMda[2]);
                }

                Org = new OrganisationServiceImpl();
                List<MdaServiceDto> page = Org.SearchedMdaService(Integer.parseInt(userMda[1]), search, sortBy, isAgent, buzz);
                success.setStatus(200);
                success.setData(page);
            }
            return Response.ok(success).build();
        } catch (Exception e) {
            System.out.println("Errr" + e.getMessage());
            e.printStackTrace();
            success.setMessage("Bad Request");
            success.setStatus(400);
            success.setData(e.getMessage());
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
    @RolesAllowed({UserRoles.Agent, UserRoles.Individual, UserRoles.NonIndividual})
    @ApiOperation(value = " ", notes = "", authorizations = {
            @Authorization(value = "authorization")})
    public Response SearchMdaServices(@QueryParam("sort")String sortBy, @QueryParam("search")String search){
        SuccessResponse success = new SuccessResponse();
        try {
            String[] userMda = ExtractTokenUserMdaId(jwt);

            boolean isAgent = false;
            Object[] roleCheck = jwt.getGroups().toArray();
            int buzz = 0;

            if(roleCheck[0].toString().equals(UserRoles.Agent)){
                isAgent = true;
            }
            if(roleCheck[0].toString().equals(UserRoles.Individual) || roleCheck[0].toString().equals(UserRoles.NonIndividual)){
                buzz = Integer.parseInt(userMda[2]);
            }

            Org = new OrganisationServiceImpl();
            List<MdaServiceDto> page = Org.SearchedMdaService(Integer.parseInt(userMda[1]), search, sortBy, isAgent, buzz);
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
    @Path("/dashboard")
    @ApiOperation(value = " ", notes = "  ", authorizations = {@Authorization(value = "authorization")})
    @RolesAllowed({UserRoles.Agent})
    @ApiResponses(value = {
            @ApiResponse(
                    message = "Agent Dashboard",
                    code = 200,
                    response = org.bizzdeskgroup.models.PosTransaction.class,
                    responseContainer = "List")
    })
    public Response AgentDash(@QueryParam("sort")String sortBy, @QueryParam("search")String search){
        SuccessResponse success = new SuccessResponse();
        try {
            String[] userMda = ExtractTokenUserMdaId(jwt);

            TransactionMonthlySummaryDto summary = new TransactionMonthlySummaryDto();
            AgentDash ps = new AgentDash();

            Transactions = new TransactionServiceImpl();
            Assess = new AssessmentServiceImpl();

            DateTime today = new DateTime();
            summary = Transactions.AltMonthTransactionAmountAgent(today, Integer.parseInt(userMda[0]));
            ps.enumCorp = Assess.CountNonIndividuals(0, Integer.parseInt(userMda[0]));
            ps.enumInd = Assess.CountIndividuals(0, Integer.parseInt(userMda[0]));
            ps.revGen = summary.totalRev;
            ps.revCount = summary.count;
            success.setStatus(200);
            success.setData(ps);
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

}
