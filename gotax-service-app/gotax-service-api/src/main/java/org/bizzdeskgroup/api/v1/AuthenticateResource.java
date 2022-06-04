package org.bizzdeskgroup.api.v1;


import io.swagger.annotations.*;
import org.bizzdeskgroup.Dto.MPJWTToken;
import org.bizzdeskgroup.Dto.SuccessResponse;
import org.bizzdeskgroup.Dtos.Command.*;
import org.bizzdeskgroup.Dtos.Query.*;
import org.bizzdeskgroup.models.Pos;
import org.bizzdeskgroup.models.UserRoles;
import org.bizzdeskgroup.services.OrganisationService;
import org.bizzdeskgroup.services.PosService;
import org.bizzdeskgroup.services.PosTransactionService;
import org.bizzdeskgroup.services.UserService;
import org.bizzdeskgroup.services.impl.OrganisationServiceImpl;
import org.bizzdeskgroup.services.impl.PosServiceImpl;
import org.bizzdeskgroup.services.impl.PosTransactionServiceImpl;
import org.bizzdeskgroup.services.impl.UserServiceImpl;

import org.eclipse.microprofile.jwt.JsonWebToken;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import java.time.LocalDate;
import java.util.*;

import static org.bizzdeskgroup.Helpers.NotificationMixer.DateTime;
import static org.bizzdeskgroup.Mappers.UserMapper.*;
import static org.bizzdeskgroup.helper.UserTokenUtil.*;

@Path("auth")
@Api(value = "User Management")
@RequestScoped
@Produces("application/json")
@Consumes("application/json")
//@Log
public class AuthenticateResource {
    UserService User = null;
    OrganisationService Org = null;
    PosTransactionService PosTransaction = null;
    PosService Poses = null;


    @Inject
    JsonWebToken jwt;


//    @Inject(7)
//    @Claim("groups")
//    JsonArray jsonRoles;
//    @DeclareRoles({"User"})
//    JsonArray jsonRoles;

//    public AuthenticateResource() {
//    }

    @GET
    @Path("/Roles")
    @ApiOperation(value = "Get All Roless", notes = "Get All Roles")
//    @ApiOperation(value = "Get All Roless", notes = "Get All Roles", authorizations = {
//            @Authorization(value = "authorization")})
    public Response Roles(){

//        System.out.println("seeR0le" + jwt.getRawToken());
//        System.out.println("seeROle" + jwt.containsClaim("groups"));
//        System.out.println("seeROle" + jwt.claim("upn"));
//        System.out.println("seeROle" + jwt.getSubject());
//        System.out.println("seeROle" + jwt.getName());
//        System.out.println("seeROle" + jwt.getAudience());
//        System.out.println("seeROle" + jwt.getIssuer());
//        System.out.println("seeROle" + jwt.getTokenID());
//        System.out.println("seeROle" + jwt.getClaimNames());

        SuccessResponse success = new SuccessResponse();
        success.setStatus(200);
        success.setData(RoleList());
        return Response.ok(success).build();
    }

    @POST
    @Path("/register")
//    @RolesAllowed({UserRoles.Admin, UserRoles.SubAdmin1})
//    @RolesAllowed({UserRoles.Admin})
//    @Secure
    @ApiOperation(value = "Tax payers Individuals and Non-individuals ", notes = "Tax payers ")
    public Response SelfRegister(@Valid CreateSelfUserDto userSelf) {
//        System.out.println("seeROle" + jsonRoles.get(0).toString());
        SuccessResponse success = new SuccessResponse();
        List<String> roles = RoleList();
        if (!RoleExist(roles, userSelf.getRole())){
            success.setStatus(400);
            success.setData("Invalid User Role");
            success.setMessage("Bad Request");
            return Response.status(400)
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .entity(success)
                    .build();
        }
        if ( userSelf.getRole().equals(UserRoles.NonIndividual) || userSelf.getRole().equals(UserRoles.Individual)) {

            CreateUserDto user = NewSelfUser(userSelf);

            user.setDefaultPass(false);

            try {
                User = new UserServiceImpl();
                User.CreateUser(user, 0, userSelf.getProject().projectId);
                success.setStatus(200);
                success.setData("Success");
                return Response.ok(success).build();
            } catch (Exception e) {
                System.out.println("Errr" + e.getMessage());
                e.printStackTrace();
//            throw new BadRequestException(e.getMessage());
                success.setStatus(400);
                success.setMessage("Bad Request");
                success.setData(e.getMessage());
                return Response.status(400)
                        .type(MediaType.APPLICATION_JSON_TYPE)
                        .entity(success)
                        .build();
            }
        } else {
            success.setStatus(400);
            success.setData("Wrong user role");
            success.setMessage("Bad Request");
            return Response.status(400)
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .entity(success)
                    .build();
        }
    }

//    @POST
//    @Path("/rr")
//    public Response Rrr(@Context SecurityContext ctx){
//
////        System.out.println("seeROle" + ctx.getUserPrincipal().getName());
////        System.out.println("seeR0le" + jwt.getRawToken());
////        System.out.println("seeROle" + jwt.containsClaim("roles"));
////        System.out.println("seeROle" + jwt.containsClaim("groups"));
////        System.out.println("seeROle" + ctx.isUserInRole(UserRoles.Admin));
////        System.out.println("seeROle" + jwt.claim("groups"));
////        System.out.println("seeROle" + jwt.getGroups());
////        System.out.println("seeROle" + jsonRoles.get(0).toString());
////        System.out.println("seeROle" + jwt.getClaim("group"));
//
//        SuccessResponse success = new SuccessResponse();
//        try {
//            success.setStatus(200);
////            success.setData(SendSms("test", "08069452749"));
////            SendMail("joenkem03@gmail.com", "Joe Nkem", "", "");
//            success.setData("");
//            return Response.ok(success).build();
//        } catch (Exception e) {
//            System.out.println("Errr" + e.getMessage());
//            e.printStackTrace();
////            throw new BadRequestException(e.getMessage());
//            success.setStatus(400);
//            success.setData(e.getMessage());
//            return Response.ok(success).build();
//        }
//    }

//    @POST
//    @Path("/xentest-email")
//    public Response eRrr(EbillsV2Email ph){
//
//        SuccessResponse success = new SuccessResponse();
//        try {
////            InfoEmailSmsUtil.SendMail( ph.phone, "J. N. Green", "Your phone confirmation code: 000000", "Send Mail");
//            InfoEmailSmsUtil.SendMail( ph.receiverEmail, ph.receiverName, ph.messageContent, ph.messageTitle);
//            success.setStatus(200);
//            success.setData("");
//            success.setData("");
//            return Response.ok(success).build();
//        } catch (Exception e) {
//            System.out.println("Errr" + e.getMessage());
//            e.printStackTrace();
////            throw new BadRequestException(e.getMessage());
//            success.setStatus(400);
//            success.setData(e.getMessage());
//            return Response.ok(success).build();
//        }
//    }
//
//    @POST
//    @Path("/test-phone")
//    public Response Rrr(PhoneConfirmRequestDto ph){
//
//        SuccessResponse success = new SuccessResponse();
//        try {
//            InfoEmailSmsUtil.SendSms("Your phone confirmation code: 000000", ph.phone);
//            success.setStatus(200);
//            success.setData("");
////            SendMail("joenkem03@gmail.com", "Joe Nkem", "", "");
//            success.setData("");
//            return Response.ok(success).build();
//        } catch (Exception e) {
//            System.out.println("Errr" + e.getMessage());
//            e.printStackTrace();
////            throw new BadRequestException(e.getMessage());
//            success.setStatus(400);
//            success.setData(e.getMessage());
//            return Response.ok(success).build();
//        }
//    }
//
//    @POST
//    @Path("/new-test-phone")
//    public Response Rrrx(PhoneConfirmRequestDto ph){
//
//        SuccessResponse success = new SuccessResponse();
//        try {
//            InfoEmailSmsUtil.SendSmsAlt("Your new implementation phone confirmation code: 000000", ph.phone);
//            success.setStatus(200);
//            success.setData("");
////            SendMail("joenkem03@gmail.com", "Joe Nkem", "", "");
////            success.setData("");
//            return Response.ok(success).build();
//        } catch (Exception e) {
//            System.out.println("Errr" + e.getMessage());
//            e.printStackTrace();
////            throw new BadRequestException(e.getMessage());
//            success.setStatus(400);
//            success.setData(e.getMessage());
//            return Response.ok(success).build();
//        }
//    }

    @POST
    @Path("/createuser")
    @RolesAllowed({UserRoles.Admin, UserRoles.SubAdmin1})
//    @RolesAllowed({"Admin"})
//    @Secure
    @ApiOperation(value = "Admin creates all users, Sub-Admin1 creates Agents. \" UserMda \" optional if user being created is Sub-Admin2 or Agent ", notes = "Admin creates users, Sub-Admin1 creates Agents ", authorizations = {
            @Authorization(value = "authorization")})
    public Response Register( @Context SecurityContext ctx, AdminCreateUserDto userByAdmin) {

        CreateUserDto user = NewUserByAdmin(userByAdmin);

        user.setDefaultPass(true);

        SuccessResponse success = new SuccessResponse();
        List<String> roles = RoleList();
        if (!RoleExist(roles, user.getRole())){
            success.setStatus(400);
            success.setData("Invalid User Role");
            success.setMessage("Bad Request");
            return Response.status(400)
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .entity(success)
                    .build();
        }

        if (user.getRole().toUpperCase(Locale.ROOT).equals(UserRoles.Admin.toUpperCase(Locale.ROOT))){
            success.setStatus(400);
            success.setData("Cannot create user wth role Admin");
            success.setMessage("Bad Request");
            return Response.status(400)
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .entity(success)
                    .build();
        }

        if (!ctx.isUserInRole(UserRoles.Admin) && (user.getRole().equals(UserRoles.SubAdmin1) || user.getRole().equals(UserRoles.ProjectReport) || user.getRole().equals(UserRoles.SubAdmin2))){
            success.setStatus(400);
            success.setData("User with this role can not create an  account with role: " + UserRoles.Agent);
            success.setMessage("Bad Request");
            return Response.status(400)
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .entity(success)
                    .build();
        }

        try {
            String[] userMda = ExtractTokenUserMdaId(jwt);
            int createdBy = Integer.parseInt(userMda[0]);
            User = new UserServiceImpl();
            switch (user.getRole()) {
                case UserRoles.Agent :
                case UserRoles.SubAdmin2:
                    user.getUserMda().canCollect = user.getRole().equals(UserRoles.Agent);
                    user.getUserMda().collectionLimit = 0.00;
//                try {
                    Org = new OrganisationServiceImpl();
                    MdaDto mdaDto = Org.SingleMda(user.getUserMda().mdaId);
                    if(mdaDto == null) {
                        success.setStatus(400);
                        success.setData("MDA does not exist");
                        success.setMessage("Bad Request");
                        return Response.status(400)
                                .type(MediaType.APPLICATION_JSON_TYPE)
                                .entity(success)
                                .build();
                    } else {
                        try {

//                            User.CreateUser(user, createdBy);
                            try {
                                User.CreateUser(user, createdBy, userByAdmin.project.projectId);
                            } catch (Exception e) {
                                success.setMessage("Bad Request");
                                success.setData(e.getMessage());
                                success.setStatus(400);
                                return Response.status(400)
                                        .type(MediaType.APPLICATION_JSON_TYPE)
                                        .entity(success)
                                        .build();
                            }
                            Org.CreateMdaUser(user.getUserMda(), user.getEmail(), createdBy, 0);
                            success.setStatus(200);
                            success.setData("Success");
                            return Response.ok(success).build();
                        } catch (Exception e) {
                            success.setStatus(201);
                            success.setMessage(e.getMessage());
                            success.setData("User created. Proceed to assign to MDA");
                            return Response.ok(success).build();
                        }
                    }
//                    break;
                case UserRoles.ProjectReport:
                case UserRoles.SubAdmin1:
//                try {
                    Org = new OrganisationServiceImpl();
                    BusinessDto business = Org.SingleBusiness(user.getProject().projectId, true);
                    if(business == null) {
                        success.setStatus(400);
                        success.setData("Project does not exist");
                        success.setMessage("Bad Request");
                        return Response.status(400)
                                .type(MediaType.APPLICATION_JSON_TYPE)
                                .entity(success)
                                .build();
                    } else {
                        try {

                            try {
                                User.CreateUser(user, createdBy, userByAdmin.project.projectId);
                            } catch (Exception e) {
                                success.setMessage("Bad Request");
                                success.setData(e.getMessage());
                                success.setStatus(400);
                                return Response.status(400)
                                        .type(MediaType.APPLICATION_JSON_TYPE)
                                        .entity(success)
                                        .build();
                            }
                            Org.CreateBusinessUser(business.id, user.getEmail());
                            success.setStatus(200);
                            success.setData("Success");
                            return Response.ok(success).build();
                        } catch (Exception e) {
                            success.setStatus(201);
                            success.setMessage(e.getMessage());
                            success.setData("User created. Proceed to assign to Project");
                            return Response.ok(success).build();
                        }
                    }
//                    break;
                case UserRoles.Admin:
                    User.CreateUser(user, createdBy, userByAdmin.project.projectId);
                    success.setStatus(200);
                    success.setData("Success");
                    return Response.ok(success).build();
//                    break;
                default:
                    success.setStatus(400);
                    success.setData("Unknown user role specified");
                    success.setMessage("Bad Request");
                    return Response.status(400)
                            .type(MediaType.APPLICATION_JSON_TYPE)
                            .entity(success)
                            .build();

            }
//            if (user.role.equals(UserRoles.Agent) || user.role.equals(UserRoles.SubAdmin2))
//            {
//
////                } catch (Exception e) {
////                    success.setStatus(201);
////                    success.setData(e.getMessage());
////                    return Response.ok(success).build();
////                }
//            } else {
//            }
        } catch (Exception e) {
            System.out.println("Errr" + e.getMessage());
            e.printStackTrace();
//            throw new BadRequestException(e.getMessage());
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
    @Path("/login")
    @ApiOperation(value = " ", notes = "  ")
    @ApiResponses(value = {
            @ApiResponse(
                    message = "Successful authentication",
                    code = 200,
                    response = AuthenticatedUserDto.class)
    })
    public Response Login(AuthenticateUserDto user) {
        SuccessResponse success = new SuccessResponse();
        try {
            System.out.println("unCrashed");

            User = new UserServiceImpl();
            InternalUserDto Res = User.UserAuthentication(user);
//            SuccessResponse success = new SuccessResponse();
//            System.out.println("MyToken" + sc.getUserPrincipal());
            if(Res.role.equals(UserRoles.SubAdmin2)){
                try{
//                    System.out.println(Res.id);
                    Org = new OrganisationServiceImpl();
                    UserMdaDto mdaUserDto = Org.GetMdaUser(Res.id);
                    if (mdaUserDto == null){
                        success.setStatus(400);
                        success.setData("agent not assigned to MDA");
                        success.setMessage("Bad Request");
                        return Response.status(400)
                                .type(MediaType.APPLICATION_JSON_TYPE)
                                .entity(success)
                                .build();
                    }
                    if(!mdaUserDto.clientActive) throw new Exception("Project/Client not active");
                    if(!mdaUserDto.mdaActive) throw new Exception("MDA not active");
                    Res.mdaId = mdaUserDto.mdaId;
                    Res.mdaOfficeId = mdaUserDto.mdaOfficeId;
                    Res.projectId = mdaUserDto.businessId;
                    Res.project = mdaUserDto.project;
                    Res.mda = mdaUserDto.mda;
                    Res.clientLogo = mdaUserDto.clientLogo;
                } catch (Exception e){
                    success.setStatus(400);
//                    success.setData("No MDA assigned");
                    success.setData(e.getMessage());
                    success.setMessage("Bad Request");
                    return Response.status(400)
                            .type(MediaType.APPLICATION_JSON_TYPE)
                            .entity(success)
                            .build();
                }
            } else{
                Res.mdaId = 0;
            }
            if(Res.role.equals(UserRoles.SubAdmin1) || Res.role.equals(UserRoles.ProjectReport)){
                Org = new OrganisationServiceImpl();
                UserProjectDto projectUserDto = Org.GetProjectUser(Res.id);
                if (projectUserDto == null){
                    success.setStatus(400);
                    success.setData("User/Project not found");
                    success.setMessage("Bad Request");
                    return Response.status(400)
                            .type(MediaType.APPLICATION_JSON_TYPE)
                            .entity(success)
                            .build();
                }
                if(!projectUserDto.clientActive) throw new Exception("Project/Client not active");
                Res.projectId = projectUserDto.businessId;
                Res.project = projectUserDto.project;
                Res.clientLogo = projectUserDto.clientLogo;
            }

            if(Res.role.equals(UserRoles.Agent)){
//                success.setStatus(200);
//                success.setData("unauthorized login channel");
//                success.setMessage("Bad Request");
//                return Response.status(400)
//                        .type(MediaType.APPLICATION_JSON_TYPE)
//                        .entity(success)
//                        .build();

                Org = new OrganisationServiceImpl();
                UserMdaDto mdaUser = Org.GetMdaUser(Res.id);
                if (mdaUser == null){
                    success.setStatus(400);
                    success.setData("agent not assigned to MDA");
                    success.setMessage("Bad Request");
                    return Response.status(400)
                            .type(MediaType.APPLICATION_JSON_TYPE)
                            .entity(success)
                            .build();
                }
                if(!mdaUser.clientActive) throw new Exception("Project/Client not active");
                if(!mdaUser.mdaActive) throw new Exception("MDA not active");
                Res.mdaId = mdaUser.mdaId;
                Res.mdaOfficeId = mdaUser.mdaOfficeId;
                Res.projectId = mdaUser.businessId;
                Res.project = mdaUser.project;
                Res.mda = mdaUser.mda;
                Res.clientLogo = mdaUser.clientLogo;

                PosTransaction = new PosTransactionServiceImpl();
//                RemittanceResponseDto rrd = PosTransaction.CheckAgentOutStandingRemit(Res.id, agentDto.posId);
                RemittanceResponseDto rrd = PosTransaction.CheckAgentOutStandingRemit(Res.id, 1);//
                System.out.println(rrd);
                if (rrd != null){
                    System.out.println(rrd);
                    success.setStatus(202);
                    success.setData(rrd);
                    return Response.ok(success).build();
                }
                String[] date = DateTime().toString().split(" ");
//            if (PosTransaction.CheckAgentOutStandingCollect(Res.id, LocalDate.parse(date[0]).plusDays(-1).toString())){
                if (PosTransaction.CheckAgentOutStandingCollect(Res.id, date[0])){
                    System.out.println(PosTransaction.CheckAgentOutStandingCollect(Res.id, LocalDate.parse(date[0]).plusDays(-1).toString()));
                    GenerateRemittanceDto gen = new GenerateRemittanceDto();
                    gen.mdaId = mdaUser.mdaId;
//                    gen.posId = agentDto.posId;
                    gen.posId = 1;//

                    RemittanceResponseDto remittance = PosTransaction.NewRemittance(Res.id, gen, gen.mdaId, mdaUser.mdaOfficeId);
                    success.setStatus(202);
                    success.setData(remittance);
                    return Response.ok(success).build();
                }

//                MPJWTToken jwtContent = new MPJWTToken();
//                Res.token = generateUserTokenString(Res, jwtContent);
//                success.setStatus(200);
//                success.setData(LoginUser(Res));
//                return Response.ok(success).build();
            }

            if(Res.role.equals(UserRoles.Individual) || Res.role.equals(UserRoles.NonIndividual)){
                Org = new OrganisationServiceImpl();
                System.out.println(Res.businessId);
                BusinessDto business = Org.SingleBusiness(Res.businessId, true);
                Res.project = business.clientName;
//                Res.clientLogo = business.clientLogo;
                Res.projectId = Res.businessId;
            }

            MPJWTToken jwtContent = new MPJWTToken();
//            jwtContent.
            Res.token = generateUserTokenString(Res, jwtContent);
//            Res.Token = (JsonWebToken) generateUserTokenString(Res, jwtContent);
//            Res.TokenString = Res.Token.toString();

            if(Res.isDefaultPass){
                Res.role = UserRoles.Default;
            }

            if(Res.role.equals(UserRoles.Individual) || Res.role.equals(UserRoles.NonIndividual) || Res.role.equals(UserRoles.Agent)) {
                List<BusinessPaymentChannelDto> bpc = Org.AllBusPaymentChannels(Res.businessId);
                List<String> buzBpc = new LinkedList<>();
                for (BusinessPaymentChannelDto item : bpc
                ) {
                    buzBpc.add(item.name);
                }

                Res.projectPaymentChannels = buzBpc;
            }

            success.setStatus(200);
            success.setData(LoginUser(Res));
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
    @Path("/agent/login")
    @ApiOperation(value = "Agent Login on POS terminals ", notes = "  ")
//    @RolesAllowed({UserRoles.Agent, UserRoles.Admin})
    @ApiResponses(value = {
            @ApiResponse(
                    message = "Successful authentication",
                    code = 200,
                    response = AuthenticatedUserDto.class)
    })
    public Response AgentLogin(AuthenticateAgentDto agentDto) {
        SuccessResponse success = new SuccessResponse();
        try {
            AuthenticateUserDto user = new AuthenticateUserDto();
            user.username = agentDto.username;
            user.password = agentDto.password;

            User = new UserServiceImpl();
            InternalUserDto Res = User.UserAuthentication(user);

            if(!Res.role.equals(UserRoles.Agent)){
                success.setStatus(400);
                success.setData("unauthorized login channel");
                success.setMessage("Bad Request");
                return Response.status(400)
                        .type(MediaType.APPLICATION_JSON_TYPE)
                        .entity(success)
                        .build();
            }
            Org = new OrganisationServiceImpl();
            UserMdaDto mdaUser = Org.GetMdaUser(Res.id);
            if (mdaUser == null){
                success.setStatus(400);
                success.setData("agent not assigned to MDA");
                success.setMessage("Bad Request");
                return Response.status(400)
                        .type(MediaType.APPLICATION_JSON_TYPE)
                        .entity(success)
                        .build();
            }
            if(!mdaUser.clientActive) throw new Exception("Project/Client not active");
            if(!mdaUser.mdaActive) throw new Exception("MDA not active");

            Poses = new PosServiceImpl();
            Pos pos = Poses.SinglePos(agentDto.posId);
            if(pos.mdaId != mdaUser.mdaId) throw new Exception("POS/Agent not match");
            if(pos.loggedInUser > 0 && pos.loggedInUser != Res.id) throw new Exception("A user is currently logged on to this device");


            Pos posUser = Poses.PosUser(Res.id);
            if(posUser != null && posUser.id != pos.id) throw new Exception("This user is currently logged in to another terminal");

            //Update POS logged in user
            Org.UpdatePosUser(Res.id, agentDto.posId, false, 0);

            Res.mdaId = mdaUser.mdaId;
            Res.mdaOfficeId = mdaUser.mdaOfficeId;
            Res.projectId = mdaUser.businessId;
            Res.project = mdaUser.project;
            Res.mda = mdaUser.mda;
            Res.clientLogo = mdaUser.clientLogo;

            PosTransaction = new PosTransactionServiceImpl();
            RemittanceResponseDto rrd = PosTransaction.CheckAgentOutStandingRemit(Res.id, agentDto.posId);
            System.out.println(rrd);
            if (rrd != null){
                System.out.println(rrd);
                success.setStatus(202);
                success.setData(rrd);
                return Response.ok(success).build();
            }
            String[] date = DateTime().toString().split(" ");
//            if (PosTransaction.CheckAgentOutStandingCollect(Res.id, LocalDate.parse(date[0]).plusDays(-1).toString())){
            if (PosTransaction.CheckAgentOutStandingCollect(Res.id, date[0])){
                System.out.println(PosTransaction.CheckAgentOutStandingCollect(Res.id, LocalDate.parse(date[0]).plusDays(-1).toString()));
                GenerateRemittanceDto gen = new GenerateRemittanceDto();
                gen.mdaId = mdaUser.mdaId;
                gen.posId = agentDto.posId;
                
                RemittanceResponseDto remittance = PosTransaction.NewRemittance(Res.id, gen, gen.mdaId, mdaUser.mdaOfficeId);
                success.setStatus(202);
                success.setData(remittance);
                return Response.ok(success).build();
            }

            if(Res.isDefaultPass){
                Res.role = UserRoles.Default;
            }



            MPJWTToken jwtContent = new MPJWTToken();
            Res.token = generateUserTokenString(Res, jwtContent);

            List<BusinessPaymentChannelDto> bpc = Org.AllBusPaymentChannels(Res.businessId);
            List<String> buzBpc = new LinkedList<>();
            for (BusinessPaymentChannelDto item : bpc
            ) {
                buzBpc.add(item.name);
            }
            Res.projectPaymentChannels = buzBpc;

            success.setStatus(200);
            success.setData(LoginUser(Res));
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
    @Path("/agent/signout")
    @ApiOperation(value = "Agent Sign out on POS terminals ", notes = "  ")
    @RolesAllowed({UserRoles.Agent, UserRoles.Admin, UserRoles.SubAdmin1})
//    @ApiResponses(value = {
//            @ApiResponse(
//                    message = "Successful authentication",
//                    code = 200,
//                    response = AuthenticatedUserDto.class)
//    })
    public Response AgentSignOut(@QueryParam("agent_id")int agentId, @QueryParam("pos_id")int posId) {
        SuccessResponse success = new SuccessResponse();
        try {

            Poses = new PosServiceImpl();
            Pos posUser = null;
                    String[] userMda = ExtractTokenUserMdaId(jwt);

            Object[] roleCheck = jwt.getGroups().toArray();
            switch (roleCheck[0].toString()){
                case UserRoles.Admin:
                    posUser = Poses.SinglePos(Integer.parseInt(userMda[0]));
                    if(posUser == null ) throw new Exception("This user is not logged in to a terminal");
                    Org.UpdatePosUser(agentId, posId, true, Integer.parseInt(userMda[0]));
                    break;
                case UserRoles.SubAdmin1:
//                case UserRoles.ProjectReport:
//                case UserRoles.SubAdmin2:
                    Pos pos = Poses.SinglePos(posId);
                    if(pos.mdaId != Integer.parseInt(userMda[0])) throw new Exception("POS/MDA not match");

                    posUser = Poses.SinglePos(Integer.parseInt(userMda[0]));
                    if(posUser == null ) throw new Exception("This user is not logged in to a terminal");

                        Org.UpdatePosUser(agentId, posId, true, Integer.parseInt(userMda[0]));
                    break;
                case UserRoles.Agent:

//                    Pos pos = Poses.SinglePos(agentDto.posId);
//                    if(pos.mdaId != mdaUser.mdaId) throw new Exception("POS/Agent not match");
//                    if(pos.loggedInUser > 0 && pos.loggedInUser != Integer.parseInt(userMda[0])) throw new Exception("A user is currently logged on to this device");


                    posUser = Poses.SinglePos(Integer.parseInt(userMda[0]));
                    if(posUser == null ) throw new Exception("This user is not logged in to a terminal");

                    //Update POS logged in user
                    Org.UpdatePosUser(Integer.parseInt(userMda[0]), posId, true, Integer.parseInt(userMda[0]));
                    break;
            }


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


    @POST
    @Path("/confirmemail")
    @ApiOperation(value = " ", notes = "  ")
    public Response ConfrimEmail(EmailConfirmDto user) {
        SuccessResponse success = new SuccessResponse();
        try {
            User = new UserServiceImpl();
            User.ConfirmEmail(user);
//            SuccessResponse success = new SuccessResponse();
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

    @POST
    @Path("/confirmphone")
    @RolesAllowed({UserRoles.Admin, UserRoles.SubAdmin1, UserRoles.SubAdmin2, UserRoles.Agent, UserRoles.NonIndividual, UserRoles.Individual})
    @ApiOperation(value = " ", notes = "  ", authorizations = {
            @Authorization(value = "authorization")})
    public Response ConfrimPhone(PhoneConfirmDto user) {
        SuccessResponse success = new SuccessResponse();
        try {
            User = new UserServiceImpl();
            User.ConfirmPhone(user, jwt.getName());
//            SuccessResponse success = new SuccessResponse();
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


    @POST
    @Path("/confirmphonerequest")
    @RolesAllowed({UserRoles.Admin, UserRoles.SubAdmin1, UserRoles.SubAdmin2, UserRoles.Agent, UserRoles.NonIndividual, UserRoles.Individual})
    @ApiOperation(value = " ", notes = "  ", authorizations = {
            @Authorization(value = "authorization")})
    public Response ConfirmPhoneRequest(PhoneConfirmRequestDto user) {
        SuccessResponse success = new SuccessResponse();
        try {
//            InfoEmailSmsUtil.SendSms("Your phone confirmation code: " + "01254", user.phone);
            User = new UserServiceImpl();
            User.PhoneConfirmRequest(user);
//            SuccessResponse success = new SuccessResponse();
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


    @POST
    @Path("/forgotpassword")
    @ApiOperation(value = " ", notes = "  ")
    public Response ForgotPassword(PasswordResetRequestDto user) {
        SuccessResponse success = new SuccessResponse();
        try {
            User = new UserServiceImpl();
            User.PasswordResetRequest(user);
//            SuccessResponse success = new SuccessResponse();
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


    @POST
    @Path("/resetpassword")
    @ApiOperation(value = " ", notes = "  ")
    public Response ResetPassword(PasswordResetDto user) {
        SuccessResponse success = new SuccessResponse();
        try {
            //
            User = new UserServiceImpl();
            boolean Res = User.PasswordReset(user);
//            SuccessResponse success = new SuccessResponse();
            success.setStatus(200);
            success.setData(Res? "Success" : "Failed");
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
    @Path("/changepassword")
    @RolesAllowed({UserRoles.Admin, UserRoles.SubAdmin1, UserRoles.SubAdmin2, UserRoles.Agent, UserRoles.NonIndividual, UserRoles.Individual, UserRoles.Default})
    @ApiOperation(value = " ", notes = "  ", authorizations = {
            @Authorization(value = "authorization")})
    public Response ChangePassword(ChangePasswordDto user) {
        SuccessResponse success = new SuccessResponse();
        try {
            User = new UserServiceImpl();
            User.ChangePassword(user, jwt.getName());
//            SuccessResponse success = new SuccessResponse();
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
    @Path("/clients")
    @ApiOperation(value = "Admin", notes = "")
    @ApiResponses(value = {
            @ApiResponse(
                    message = "Paged list of Clients",
                    code = 200,
                    response = BusinessDto.class,
                    responseContainer = "List")
    })
//    @RolesAllowed({UserRoles.Individual, UserRoles.NonIndividual, UserRoles.Admin})
    public Response GetBusinesses(){
        SuccessResponse success = new SuccessResponse();
        try {
            Org = new OrganisationServiceImpl();
            List<BusinessDto> page = Org.GetAllBusiness();
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

    private List<String> RoleList(){
        List<String> Roles = new LinkedList<>();
        Roles.add(UserRoles.Admin);
        Roles.add(UserRoles.SubAdmin1);
        Roles.add(UserRoles.Agent);
        Roles.add(UserRoles.SubAdmin2);
        Roles.add(UserRoles.SubAdmin3);
        Roles.add(UserRoles.Individual);
        Roles.add(UserRoles.NonIndividual);
        Roles.add(UserRoles.ProjectReport);

        return Roles;
    }

//    https://github.com/kumuluz/kumuluzee-samples/blob/master/tutorial-microservice-config-discovery-faulttolerance-logs-metrics-security/README.md
//    @PermitAll
//    public Set<String> discoverRoles() {
//        Set<String> roleNames = new HashSet<>();
//        DeclareRoles declaredRoles = IsCallerInRoleDemoSessionBean.class.getAnnotation(DeclareRoles.class);
//        for (String roleName : declaredRoles.value())
//            if (sessionContext.isCallerInRole(roleName))
//                roleNames.add(roleName);
//        return roleNames;
//    }

    private boolean RoleExist(List<String> roles, String checkRole){
        boolean roleExist = false;
        for(String role : roles)
            if(checkRole.toUpperCase(Locale.ROOT).equals(role.toUpperCase(Locale.ROOT))) roleExist = true;
        return roleExist;
    }

}

