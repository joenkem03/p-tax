package org.bizzdeskgroup.api.v1;


import io.swagger.annotations.*;
import org.bizzdeskgroup.Dto.SuccessResponse;
import org.bizzdeskgroup.Dto.SwaggerDocResponse.Transactions;
import org.bizzdeskgroup.Dtos.Command.CreateCardWebTransactionDto;
import org.bizzdeskgroup.Dtos.Command.CreatePosCardTransactionDto;
import org.bizzdeskgroup.Dtos.Command.CreateTransactionDto;
import org.bizzdeskgroup.Dtos.PayStack.TransactionVerificationResponseDto;
import org.bizzdeskgroup.Dtos.Query.*;
import org.bizzdeskgroup.models.UserRoles;
import org.bizzdeskgroup.services.AssessmentService;
import org.bizzdeskgroup.services.PosTransactionService;
import org.bizzdeskgroup.services.TransactionService;
import org.bizzdeskgroup.services.impl.AssessmentServiceImpl;
import org.bizzdeskgroup.services.impl.PosTransactionServiceImpl;
import org.bizzdeskgroup.services.impl.TransactionServiceImpl;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.joda.time.DateTime;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import static org.bizzdeskgroup.helper.UserTokenUtil.ExtractTokenUserMdaId;


@Path("notifications")
@Api(value = "Transactions Notification", description = "")
@RequestScoped
@Produces("application/json")
@Consumes("application/json")
public class TransactionResource {

    @Inject
    JsonWebToken jwt;

    TransactionService Transaction = null;
    AssessmentService Assess = null;
    PosTransactionService PosTransaction = null;

    @POST
    @Path("/middle-push")
//    @RolesAllowed({UserRoles.Individual, UserRoles.NonIndividual, UserRoles.Agent})
//    @RolesAllowed({UserRoles.Individual, UserRoles.NonIndividual})
    @PermitAll
    @ApiOperation(value = " ", notes = "  ")
    public Response MiddlePush(InputStream is){
        SuccessResponse success = new SuccessResponse();

        try {
//            String readInput = "";
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder out = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                out.append(line);
            }
            System.out.println(out.toString());   //Prints the string content read from input stream
//            readInput = out.toString();
            reader.close();

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

    @POST
    @Path("/web-transaction")
//    @RolesAllowed({UserRoles.Individual, UserRoles.NonIndividual, UserRoles.Agent})
    @RolesAllowed({UserRoles.Individual, UserRoles.NonIndividual})
    @ApiOperation(value = " ", notes = "  ")
    public Response PosWebTransaction(CreateCardWebTransactionDto transactionDto){
        SuccessResponse success = new SuccessResponse();

        try {

            String[] userMda = ExtractTokenUserMdaId(jwt);
            Transaction = new TransactionServiceImpl();
            CreateTransactionDto reSi = Transaction.NewTransaction(transactionDto, Integer.parseInt(userMda[0]), Integer.parseInt(userMda[2]));
//            CreateTransactionDto reSi = Transaction.NewTransaction(transactionDto, 1000, 10);
            success.setStatus(200);
//            success.setData("Success");
            success.setData(reSi);
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



    @POST
    @Path("/test/web-transaction")
//    @RolesAllowed({UserRoles.Individual, UserRoles.NonIndividual, UserRoles.Agent})
    @RolesAllowed({UserRoles.Individual, UserRoles.NonIndividual})
    @ApiOperation(value = " ", notes = "  ")
    public Response PosWebTestTransaction(CreateCardWebTransactionDto transactionDto){
        SuccessResponse success = new SuccessResponse();

        try {

            String[] userMda = ExtractTokenUserMdaId(jwt);
            Transaction = new TransactionServiceImpl();
            CreateTransactionDto reSi = Transaction.NewTestTransaction(transactionDto, Integer.parseInt(userMda[0]), Integer.parseInt(userMda[2]));
//            CreateTransactionDto reSi = Transaction.NewTransaction(transactionDto, 1000, 10);
            success.setStatus(200);
//            success.setData("Success");
            success.setData(reSi);
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


    @POST
    @Path("/pos-transaction")
    @ApiOperation(value = " ", notes = "  ")
    public Response PosTransaction(CreatePosCardTransactionDto transactionDto){
        SuccessResponse success = new SuccessResponse();

        try {

            String[] userMda = ExtractTokenUserMdaId(jwt);

            Transaction = new TransactionServiceImpl();
            Transaction.NewPosCardTransaction(transactionDto, Integer.parseInt(userMda[0]), Integer.parseInt(userMda[2]));
            success.setStatus(200);
            success.setData("Success");
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
    @Path("/transaction")
    @ApiOperation(value = " ", notes = "  ", authorizations = {@Authorization(value = "authorization")})
    @RolesAllowed({UserRoles.SubAdmin1, UserRoles.SubAdmin2, UserRoles.Admin, UserRoles.ProjectReport, UserRoles.Individual, UserRoles.NonIndividual, UserRoles.Agent})
    @ApiResponses(value = {
            @ApiResponse(
                    message = "Paged list of transactions",
                    code = 200,
                    response = AdminTransactionDto.class,
                    responseContainer = "List")
    })
    public Response GetTransaction(@QueryParam("sort")String sortBy, @QueryParam("page")Integer pageNo, @QueryParam("per_page")Integer pageSize,
                                   @QueryParam("Project_Id")Integer formProjectId, @QueryParam("Mda_Id")Integer formMdaId,
                                   @QueryParam("apply_filter")boolean applyFilter, @QueryParam("filter_by")String filterBy,
                                   @QueryParam("filter")String filterValue, @QueryParam("start_date")String formStartDate, @QueryParam("end_date")String formEndDate,
                                   @QueryParam("search")String search){
        SuccessResponse success = new SuccessResponse();
        try {
            if(search == null || search.trim().isEmpty()) {

                String[] userMda = ExtractTokenUserMdaId(jwt);
                Transaction = new TransactionServiceImpl();
                PaginatedDto Res = new PaginatedDto();

//            System.out.println(formStartDate);

                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

                DateTime today = new DateTime();
                Date endDate = formStartDate == null ? today.toDate() : formatter.parse(formEndDate);
                Date startDate = formEndDate == null ? today.minusMonths(3).toDate() : formatter.parse(formStartDate);

//            System.out.println(endDate);

                Object[] roleCheck = jwt.getGroups().toArray();
                switch (roleCheck[0].toString()) {
                    case UserRoles.Admin:
                        if (formProjectId != null && formProjectId > 0) {
                            if (formMdaId != null && formMdaId > 0) {
                                Res = Transaction.AllMdaTransactions(formMdaId, formProjectId, sortBy, pageNo, pageSize, applyFilter, filterBy, filterValue, startDate, endDate);
                            } else {
                                Res = Transaction.AllProjectTransactions(formProjectId, sortBy, pageNo, pageSize, applyFilter, filterBy, filterValue, startDate, endDate);
                            }
                        } else {
                            Res = Transaction.AllTransactions(sortBy, pageNo, pageSize, applyFilter, filterBy, filterValue, startDate, endDate);
                        }
                        break;
                    case UserRoles.SubAdmin1:
                    case UserRoles.ProjectReport:
                        int ProjId = Integer.parseInt(userMda[2]);
                        if (formMdaId != null && formMdaId > 0) {
                            Res = Transaction.AllMdaTransactions(formMdaId, ProjId, sortBy, pageNo, pageSize, applyFilter, filterBy, filterValue, startDate, endDate);
                        } else {
                            Res = Transaction.AllProjectTransactions(ProjId, sortBy, pageNo, pageSize, applyFilter, filterBy, filterValue, startDate, endDate);
                        }
                        break;
                    case UserRoles.SubAdmin2:
                        int MdaId = Integer.parseInt(userMda[1]);
                        int MdaProjId = Integer.parseInt(userMda[2]);
                        Res = Transaction.AllMdaTransactions(MdaId, MdaProjId, sortBy, pageNo, pageSize, applyFilter, filterBy, filterValue, startDate, endDate);
                        break;
                    case UserRoles.Agent:
//                    int AgentMdaId = Integer.parseInt(userMda[1]);
                        int AgentId = Integer.parseInt(userMda[0]);
                        Assess = new AssessmentServiceImpl();
                        Res =  Transaction.AllAgentTransactions(Integer.parseInt(userMda[0]), Integer.parseInt(userMda[1]), Integer.parseInt(userMda[2]), sortBy, pageNo, pageSize, applyFilter, filterBy, filterValue, startDate, endDate);
                        break;
                    case UserRoles.Individual:
                    case UserRoles.NonIndividual:
//                    int AgentMdaId = Integer.parseInt(userMda[1]);
                        int UserId = Integer.parseInt(userMda[0]);
                        Assess = new AssessmentServiceImpl();
                        Res = Assess.PaidInvoice(sortBy, pageNo, pageSize, UserId);
                        break;
                }

                success.setStatus(200);
                success.setData(Res);
            }
            else {
                String[] userMda = ExtractTokenUserMdaId(jwt);
                Transaction = new TransactionServiceImpl();
                List<TransactionDto> Res = new LinkedList<TransactionDto>();

                Object[] roleCheck = jwt.getGroups().toArray();
                switch (roleCheck[0].toString()) {
                    case UserRoles.Admin:
                        List<AdminTransactionDto> AdminRes = Transaction.SearchTransactions(search, sortBy);
                        success.setData(AdminRes);
                        break;
                    case UserRoles.SubAdmin1:
                    case UserRoles.ProjectReport:
                        int ProjId = Integer.parseInt(userMda[2]);
                        Res = Transaction.SearchProjectTransactions(ProjId, search, sortBy);
                        success.setData(Res);
                        break;
                    case UserRoles.SubAdmin2:
                        int MdaId = Integer.parseInt(userMda[1]);
                        int MdaProjId = Integer.parseInt(userMda[2]);
                        Res = Transaction.SearchMdaTransactions(MdaId, MdaProjId, search, sortBy);
                        success.setData(Res);
                        break;
                    case UserRoles.Individual:
                    case UserRoles.NonIndividual:
//                    int AgentMdaId = Integer.parseInt(userMda[1]);
                        int UserId = Integer.parseInt(userMda[0]);
                        Assess = new AssessmentServiceImpl();
                        Res = Assess.SearchPaidInvoice(search, sortBy, UserId);
                        success.setData(Res);
                        break;
                }
                success.setStatus(200);
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
    @Path("/cash-transaction")
    @ApiOperation(value = " ", notes = "  ", authorizations = {@Authorization(value = "authorization")})
    @RolesAllowed({UserRoles.Individual, UserRoles.NonIndividual, UserRoles.Agent, UserRoles.SubAdmin3, UserRoles.SubAdmin2, UserRoles.SubAdmin1, UserRoles.Admin})
    @ApiResponses(value = {
            @ApiResponse(
                    message = "Paged list of POS transactions",
                    code = 200,
                    response = Transactions.class)
    })
    public Response GetPosTransaction(@QueryParam("sort")String sortBy, @QueryParam("page")Integer pageNo,
                                      @QueryParam("per_page")Integer pageSize, @QueryParam("start_date")String formStartDate, @QueryParam("end_date")String formEndDate,
                                      @QueryParam("search")String search,
                                      @QueryParam("Project_Id")Integer formProjectId, @QueryParam("Mda_Id")Integer formMdaId, @QueryParam("Agent_Id")Integer formAgentId){
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
                        if (formAgentId != null && formAgentId > 0) {
                            Res = PosTransaction.AgentPosTransactions(0, 0, formAgentId, 0, sortBy, pageNo, pageSize, startDate, endDate);
                            break;
                        }
                        if (formMdaId != null && formMdaId > 0) {
                            Res = PosTransaction.AgentPosTransactions(0, formMdaId, 0, 0, sortBy, pageNo, pageSize, startDate, endDate);
                            break;
                        }
                        Res = PosTransaction.AgentPosTransactions(Integer.parseInt(userMda[2]), 0, 0, 0, sortBy, pageNo, pageSize, startDate, endDate);
                        break;
                    case UserRoles.SubAdmin2:
                        if (formAgentId != null && formAgentId > 0) {
                            Res = PosTransaction.AgentPosTransactions(0, 0, formAgentId, 0, sortBy, pageNo, pageSize, startDate, endDate);
                            break;
                        }
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
                        if (formAgentId != null && formAgentId > 0) {
                            Res = PosTransaction.AgentPosTransactions(0, 0, formAgentId, 0, sortBy, pageNo, pageSize, startDate, endDate);
                            break;
                        }
                        if (formMdaId != null && formMdaId > 0) {
                            Res = PosTransaction.AgentPosTransactions(0, formMdaId, 0, 0, sortBy, pageNo, pageSize, startDate, endDate);
                            break;
                        }
                        if (formProjectId != null && formProjectId > 0) {
                            Res = PosTransaction.AgentPosTransactions(formProjectId, 0, 0, 0, sortBy, pageNo, pageSize, startDate, endDate);
                            break;
                        }
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
    @RolesAllowed({UserRoles.SubAdmin1, UserRoles.SubAdmin2, UserRoles.Admin, UserRoles.ProjectReport, UserRoles.Individual, UserRoles.NonIndividual})
    @ApiResponses(value = {
            @ApiResponse(
                    message = "list of transactions",
                    code = 200,
                    response = AdminTransactionDto.class,
                    responseContainer = "List")
    })
    public Response SearchTransaction(@QueryParam("sort")String sortBy, @QueryParam("search")String search){
        SuccessResponse success = new SuccessResponse();
        try {
            String[] userMda = ExtractTokenUserMdaId(jwt);
            Transaction = new TransactionServiceImpl();
            List<TransactionDto> Res = new LinkedList<TransactionDto>();

            Object[] roleCheck = jwt.getGroups().toArray();
            switch (roleCheck[0].toString()) {
                case UserRoles.Admin:
                    List<AdminTransactionDto> AdminRes = Transaction.SearchTransactions(search, sortBy);
                    success.setData(AdminRes);
                    break;
                case UserRoles.SubAdmin1:
                case UserRoles.ProjectReport:
                    int ProjId = Integer.parseInt(userMda[2]);
                    Res = Transaction.SearchProjectTransactions(ProjId, search, sortBy);
                    success.setData(Res);
                    break;
                case UserRoles.SubAdmin2:
                    int MdaId = Integer.parseInt(userMda[1]);
                    int MdaProjId = Integer.parseInt(userMda[2]);
                    Res = Transaction.SearchMdaTransactions(MdaId, MdaProjId, search, sortBy);
                    success.setData(Res);
                    break;
                case UserRoles.Individual:
                case UserRoles.NonIndividual:
//                    int AgentMdaId = Integer.parseInt(userMda[1]);
                    int UserId = Integer.parseInt(userMda[0]);
                    Assess = new AssessmentServiceImpl();
                    Res = Assess.SearchPaidInvoice(search, sortBy, UserId);
                    success.setData(Res);
                    break;
            }
            success.setStatus(200);
//            success.setData(Res);
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
                    response = AdminTransactionDto.class)
    })
    @ApiOperation(value = "Get transaction by transaction Id", notes = "  ", authorizations = {@Authorization(value = "authorization")})
    @RolesAllowed({UserRoles.SubAdmin1, UserRoles.SubAdmin2, UserRoles.Admin, UserRoles.ProjectReport})
    public Response TransactionById(@QueryParam("TransactionId")String TransactionId){
        SuccessResponse success = new SuccessResponse();
        try {
            String[] userMda = ExtractTokenUserMdaId(jwt);
            Transaction = new TransactionServiceImpl();
            TransactionDto Res = new TransactionDto();

            Object[] roleCheck = jwt.getGroups().toArray();
            switch (roleCheck[0].toString()) {
                case UserRoles.Admin:
                    AdminTransactionDto AdminRes = Transaction.SingleTransactions(TransactionId);
                    success.setData(AdminRes);
                    break;
                case UserRoles.SubAdmin1:
                case UserRoles.ProjectReport:
                    int ProjId = Integer.parseInt(userMda[2]);
                    Res = Transaction.SingleProjectTransactions(TransactionId, ProjId);
                    success.setData(Res);
                    break;
                case UserRoles.SubAdmin2:
                    int MdaId = Integer.parseInt(userMda[1]);
                    int MdaProjId = Integer.parseInt(userMda[2]);
                    Res = Transaction.SingleMdaTransactions(TransactionId, MdaProjId, MdaId);
                    success.setData(Res);
                    break;
            }
            success.setStatus(200);
//            success.setData(Res);
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
