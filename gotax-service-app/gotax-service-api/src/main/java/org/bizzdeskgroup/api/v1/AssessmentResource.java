package org.bizzdeskgroup.api.v1;


import io.swagger.annotations.*;
import org.bizzdeskgroup.Dto.SuccessResponse;
import org.bizzdeskgroup.Dtos.Command.*;
import org.bizzdeskgroup.Dtos.Query.*;
import org.bizzdeskgroup.Helpers.InfoEmailSmsUtil;
import org.bizzdeskgroup.models.UserRoles;
import org.bizzdeskgroup.services.AssessmentService;
import org.bizzdeskgroup.services.OrganisationService;
import org.bizzdeskgroup.services.PosService;
import org.bizzdeskgroup.services.UserService;
import org.bizzdeskgroup.services.impl.AssessmentServiceImpl;
import org.bizzdeskgroup.services.impl.UserServiceImpl;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.joda.time.DateTime;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static org.bizzdeskgroup.Mappers.UserMapper.NewUserByAgent;
import static org.bizzdeskgroup.helper.UserTokenUtil.ExtractTokenUserMdaId;

@Path("enroll")
@Api(value = "Enumeration & Assessment", description = " ")
@RequestScoped
@Produces("application/json")
@Consumes("application/json")
public class AssessmentResource {
    UserService User = null;
    PosService Pos = null;
    OrganisationService Org = null;
    AssessmentService Assess = null;

    @Inject
    JsonWebToken jwt;



    @GET
    @Path("/tax-payer/details")
    @RolesAllowed({UserRoles.Admin, UserRoles.Agent, UserRoles.SubAdmin1, UserRoles.SubAdmin2, UserRoles.ProjectReport, UserRoles.Individual, UserRoles.NonIndividual})
    @ApiOperation(value = "Individual Enumeration by Agent", notes = "", authorizations = {@Authorization(value = "authorization")})
    public Response TaxPayerDetails(@QueryParam("payer_id")String payerId, @QueryParam("is_individual")boolean isIndividual){
        SuccessResponse success = new SuccessResponse();
        try {

            String[] userMda = ExtractTokenUserMdaId(jwt);

            Assess = new AssessmentServiceImpl();

//            int UserId = Integer.parseInt(userMda[0]);
            String UserId = userMda[0];
            Object[] roleCheck = jwt.getGroups().toArray();
            switch (roleCheck[0].toString()){
                case UserRoles.Individual:
                case UserRoles.NonIndividual:
                    if (isIndividual) {
                        success.setData(Assess.GetIndividual(UserId));
                    } else {
                        success.setData(Assess.GetNonIndividual(UserId));
                    }
                    break;
                default:
                    if (isIndividual) {
                        success.setData(Assess.GetIndividual(payerId));
                    } else {
                        success.setData(Assess.GetNonIndividual(payerId));
                    }
            }

            success.setStatus(200);
//            success.setData("Success");
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
    @Path("/tax-payer/list")
    @RolesAllowed({UserRoles.Admin, UserRoles.Agent, UserRoles.SubAdmin1, UserRoles.SubAdmin2, UserRoles.ProjectReport, UserRoles.Individual, UserRoles.NonIndividual})
    @ApiOperation(value = "Tax Payers", notes = "", authorizations = {@Authorization(value = "authorization")})
    public Response TaxPayerList(@QueryParam("sort")String sortBy, @QueryParam("page")Integer pageNo, @QueryParam("per_page")Integer pageSize, @QueryParam("business_id")int payerId, @QueryParam("is_individual")boolean isIndividual){
        SuccessResponse success = new SuccessResponse();
        try {

            String[] userMda = ExtractTokenUserMdaId(jwt);

            Assess = new AssessmentServiceImpl();

//            int UserId = Integer.parseInt(userMda[0]);
            String UserId = userMda[0];
            int businessId = 0;
            Object[] roleCheck = jwt.getGroups().toArray();
            switch (roleCheck[0].toString()){
                case UserRoles.Admin:
                    businessId = payerId > 0 ? payerId : businessId;
                    if (isIndividual) {
                        success.setData(Assess.GetIndividualList(sortBy, pageNo, pageSize, businessId));
                    } else {
                        success.setData(Assess.GetNonIndividualList(sortBy, pageNo, pageSize, businessId));
                    }
                    break;
                case UserRoles.SubAdmin1:
                case UserRoles.SubAdmin2:
                case UserRoles.Agent:
                    businessId = Integer.parseInt(userMda[2]);
                    if (isIndividual) {
                        success.setData(Assess.GetIndividualList(sortBy, pageNo, pageSize, businessId));
                    } else {
                        success.setData(Assess.GetNonIndividualList(sortBy, pageNo, pageSize, businessId));
                    }
                    break;
            }

            success.setStatus(200);
//            success.setData("Success");
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
    @Path("/tax-payer/search")
    @RolesAllowed({UserRoles.Admin, UserRoles.Agent, UserRoles.SubAdmin1, UserRoles.SubAdmin2, UserRoles.ProjectReport, UserRoles.Individual, UserRoles.NonIndividual})
    @ApiOperation(value = "Tax Payers", notes = "", authorizations = {@Authorization(value = "authorization")})
    public Response TaxPayerSearch(@QueryParam("sort")String sortBy, @QueryParam("search")String search, @QueryParam("business_id")int payerId, @QueryParam("is_individual")boolean isIndividual){
        SuccessResponse success = new SuccessResponse();
        try {

            String[] userMda = ExtractTokenUserMdaId(jwt);

            Assess = new AssessmentServiceImpl();

//            int UserId = Integer.parseInt(userMda[0]);
            String UserId = userMda[0];
            int businessId = 0;
            Object[] roleCheck = jwt.getGroups().toArray();
            switch (roleCheck[0].toString()){
                case UserRoles.Admin:
                    businessId = payerId > 0 ? payerId : businessId;
                    if (isIndividual) {
                        success.setData(Assess.SearchIndividuals(sortBy, search, businessId));
                    } else {
                        success.setData(Assess.SearchNonIndividuals(sortBy, search, businessId));
                    }
                    break;
                case UserRoles.SubAdmin1:
                case UserRoles.SubAdmin2:
                case UserRoles.Agent:
                    businessId = Integer.parseInt(userMda[2]);
                    if (isIndividual) {
                        success.setData(Assess.SearchIndividuals(sortBy, search, businessId));
                    } else {
                        success.setData(Assess.SearchNonIndividuals(sortBy, search, businessId));
                    }
                    break;
            }

            success.setStatus(200);
//            success.setData("Success");
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
    @Path("/enumeration/individual")
    @RolesAllowed({UserRoles.Agent})
    @ApiOperation(value = "Individual Enumeration by Agent", notes = "", authorizations = {@Authorization(value = "authorization")})
    public Response IndividualEnumeration(AgentIndividualEnumerationDto enumeration){
        SuccessResponse success = new SuccessResponse();
        try {

//            {"customerBioData":{"email":"","firstName":"JayStin","lastName":"Joe","phone":"04063589607","role":"Individual"},"employerName":"Self","isSentToServer":false,"latitude":0.0,"longitude":0.0,"maritalStatus":"Single","nationality":"Nigerian","occupation":"Enterprenuer","officeAddress":"London","residenceLga":734,"residenceState":35,"residentialAddress":"London","temporaryTin":"9053682666573","title":"Mr","jtbTin":"1010101010"}
//            {"customerBioData":
//                  {"email":"","firstName":"JayStin","lastName":"Joe","phone":"04063589607","role":"Individual"},
//                  "employerName":"Self",
//                  "isSentToServer":false,
//                  "latitude":0.0,
//                  "longitude":0.0,
//                  "maritalStatus":"Single",
//                  "nationality":"Nigerian",
//                  "occupation":"Enterprenuer",
//                  "officeAddress":"London",
//                  "residenceLga":734,
//                  "residenceState":35,
//                  "residentialAddress":"London",
//                  "temporaryTin":"9053682666573",
//                  "title":"Mr"}

            if (enumeration.customer.phone.trim().isEmpty()) throw new Exception("Phone required");
            if(enumeration.customer.email.trim().isEmpty()){
                enumeration.customer.email = enumeration.customer.phone+"@paysure.digi";
            }

            String[] userMda = ExtractTokenUserMdaId(jwt);
            CreateUserDto user = NewUserByAgent(enumeration.customer);

            user.setDefaultPass(true);

            User = new UserServiceImpl();
            User.CreateUser(user, Integer.parseInt(userMda[0]), Integer.parseInt(userMda[2]));

            Assess = new AssessmentServiceImpl();
            Assess.CreateIndividual(enumeration, Integer.parseInt(userMda[0]), enumeration.customer.email, Integer.parseInt(userMda[2]));


            InfoEmailSmsUtil.SendSmsAlt("Your temporary TIN is: "+enumeration.temporaryTin, enumeration.customer.phone);
            if(enumeration.customer.email.trim().isEmpty()){
//                InfoEmailSmsUtil.SendMail( enumeration.customer.phone, enumeration.customer.firstName+" "+enumeration.customer.lastName,
//                        "<html><body>Dear "+ enumeration.customer.firstName +",<br><br>Kindly click <a href='"+link+"'>here</a> verify your account. Or copy the link below and paste in a browser<br><br>"+link+"  </body></html>", "Send Mail");
                InfoEmailSmsUtil.SendMail( enumeration.customer.phone, enumeration.customer.firstName+" "+enumeration.customer.lastName,
                        "<html><body>Dear "+ enumeration.customer.firstName +",<br><br>Your temporary TIN is: "+enumeration.temporaryTin+" </body></html>", "Taraba Tax System Enumeration");
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
    @Path("/enumeration/nonindividual")
    @RolesAllowed({UserRoles.Agent})
    @ApiOperation(value = "NonIndividual Enumeration by Agent", notes = "", authorizations = {@Authorization(value = "authorization")})
    public Response NonIndividualEnumeration(AgentNonIndividualEnumerationDto enumeration){
        SuccessResponse success = new SuccessResponse();
        try {

            if (enumeration.customer.phone.trim().isEmpty()) throw new Exception("Phone required");
            if(enumeration.customer.email.trim().isEmpty()){
                enumeration.customer.email = enumeration.customer.phone+"@paysure.digi";
            }
            CreateUserDto user = NewUserByAgent(enumeration.customer);

            user.setDefaultPass(true);

            String[] userMda = ExtractTokenUserMdaId(jwt);
            User = new UserServiceImpl();
            User.CreateUser(user, Integer.parseInt(userMda[0]), Integer.parseInt(userMda[2]));


            Assess = new AssessmentServiceImpl();
            Assess.CreateNonIndividual(enumeration, Integer.parseInt(userMda[0]), enumeration.customer.email, Integer.parseInt(userMda[2]));


            InfoEmailSmsUtil.SendSmsAlt("Your temporary TIN is: "+enumeration.temporaryTin, enumeration.customer.phone);
            if(enumeration.customer.email.trim().isEmpty()){
//                InfoEmailSmsUtil.SendMail( enumeration.customer.phone, enumeration.customer.firstName+" "+enumeration.customer.lastName,
//                        "<html><body>Dear "+ enumeration.customer.firstName +",<br><br>Kindly click <a href='"+link+"'>here</a> verify your account. Or copy the link below and paste in a browser<br><br>"+link+"  </body></html>", "Send Mail");
                InfoEmailSmsUtil.SendMail( enumeration.customer.phone, enumeration.customer.firstName+" "+enumeration.customer.lastName,
                        "<html><body>Dear "+ enumeration.customer.firstName +",<br><br>Your temporary TIN is: "+enumeration.temporaryTin+" </body></html>", "Taraba Tax System Enumeration");
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
    @Path("/self-enumeration/individual")
//    @RolesAllowed({UserRoles.Individual, UserRoles.NonIndividual})
    @RolesAllowed({UserRoles.Individual})
    @ApiOperation(value = "Self-service Individual Enumeration", notes = "", authorizations = {@Authorization(value = "authorization")})
    public Response IndividualSelfEnumeration(BaseIndividualEnumerationDto enumeration){
        SuccessResponse success = new SuccessResponse();
        try {

            String[] userMda = ExtractTokenUserMdaId(jwt);

            User = new UserServiceImpl();

            Assess = new AssessmentServiceImpl();
            Assess.CreateIndividual(enumeration, Integer.parseInt(userMda[0]), "", Integer.parseInt(userMda[2]));

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
    @Path("/self-enumeration/nonindividual")
//    @RolesAllowed({UserRoles.Individual, UserRoles.NonIndividual})
    @RolesAllowed({UserRoles.NonIndividual})
    @ApiOperation(value = "Self-service NonIndividual Enumeration ", notes = "", authorizations = {@Authorization(value = "authorization")})
    public Response NonIndividualSelfEnumeration(BaseNonIndividualEnumerationDto enumeration){
        SuccessResponse success = new SuccessResponse();
        try {

            String[] userMda = ExtractTokenUserMdaId(jwt);

            Assess = new AssessmentServiceImpl();
            Assess.CreateNonIndividual(enumeration, Integer.parseInt(userMda[0]), "", Integer.parseInt(userMda[2]));

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

//    @GET
//    @Path("/compute-assessment")
//    @Produces(MediaType.APPLICATION_XML)
//    @Consumes(MediaType.APPLICATION_XML)
////    @RolesAllowed({UserRoles.Agent})
//    @ApiOperation(value = "Test Compute Tax ", notes = "")
//    public Response ComputeAssessment(@QueryParam("grossIncome")double grossIncomeAmount){
//        SuccessResponse success = new SuccessResponse();
//        try {
////            double grossIncome;
//            int numberOfChildrenBelow18 = 2;
//            int numberOfDependants = 1;
//            ComplexTaxDto computedTax = new ComplexTaxDto();
//            ComputeTax computeTax = new ComputeTax(grossIncomeAmount, numberOfChildrenBelow18, numberOfDependants, "Non-Individual");
//            if (grossIncomeAmount != 0 && grossIncomeAmount < 100000) throw new Exception("Invalid gross");
//            if (grossIncomeAmount <= 300000){
//                computedTax = computeTax.SimpleTax();
//            } else {
//                computedTax = computeTax.ComplexTax();
//            }
//
//            success.setStatus(200);
//            success.setData(computedTax);
////            success.setData("");
//            return Response.ok(success).build();
//        } catch (Exception e) {
//            System.out.println("Errr" + e.getMessage());
//            e.printStackTrace();
//            success.setStatus(400);
//            success.setMessage("Bad Request");
////            success.setData(e.getMessage());
//            success.setData(null);
//            return Response.status(400)
//                    .type(MediaType.APPLICATION_JSON_TYPE)
//                    .entity(success)
//                    .build();
//        }
//    }

    @POST
    @Path("/other-assessment")
//    @RolesAllowed({UserRoles.Agent, UserRoles.Individual, UserRoles.NonIndividual})
    @RolesAllowed({UserRoles.Agent})
    @ApiOperation(value = "Other Assessment by Agent or Self-service", notes = "", authorizations = {@Authorization(value = "authorization")})
    public Response AddDirectAssessment(CustomerAssessmentAltDto addDto){
        SuccessResponse success = new SuccessResponse();
        try {
            String[] userMda = ExtractTokenUserMdaId(jwt);
            User = new UserServiceImpl();
//            UserDto user = User.SingleUser(addDto.payerId);
            Assess = new AssessmentServiceImpl();
//            Assess.CreateAssessment(addDto, Integer.parseInt(userMda[0]), user.role);
            SimpleTaxDto ret = Assess.CreateAssessmentAlt(addDto, Integer.parseInt(userMda[0]), Integer.parseInt(userMda[2]), Integer.parseInt(userMda[3]));
            success.setStatus(200);
            success.setData(ret);
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
    @Path("/assess")
//    @RolesAllowed({UserRoles.Agent, UserRoles.Individual, UserRoles.NonIndividual})
    @RolesAllowed({UserRoles.Agent})
    @ApiOperation(value = "Direct Assessment by Agent or Self-service", notes = "", authorizations = {@Authorization(value = "authorization")})
    public Response AddAssessment(CustomerAssessmentDto addDto){
        SuccessResponse success = new SuccessResponse();
        try {
            String[] userMda = ExtractTokenUserMdaId(jwt);
            User = new UserServiceImpl();
//            UserDto user = User.SingleUser(addDto.payerId);
            Assess = new AssessmentServiceImpl();
//            Assess.CreateAssessment(addDto, Integer.parseInt(userMda[0]), user.role);
            ComplexTaxDto ret = Assess.CreateAssessment(addDto, Integer.parseInt(userMda[0]), Integer.parseInt(userMda[2]), Integer.parseInt(userMda[3]));
            success.setStatus(200);
            success.setData(ret);
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
    @Path("/assessment/request-objection")
    @RolesAllowed({UserRoles.Individual, UserRoles.NonIndividual})
    @ApiOperation(value = "Direct Assessment tax objection by Self-service", notes = "", authorizations = {@Authorization(value = "authorization")})
    public Response AssessmentObjectionRequest(AssessmentObjectionRequestDto addDto){
        SuccessResponse success = new SuccessResponse();
        try {
            String[] userMda = ExtractTokenUserMdaId(jwt);
            Assess = new AssessmentServiceImpl();
            Assess.RequestAssessmentObjection(addDto, Integer.parseInt(userMda[0]));
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
    @Path("/assessment/objection-report")
    @RolesAllowed({UserRoles.Agent})
    @ApiOperation(value = "Direct Assessment report on objection by Agent", notes = "", authorizations = {@Authorization(value = "authorization")})
    public Response AssessmentObjectionInspection(AssessmentObjectionReportDto addDto){
        SuccessResponse success = new SuccessResponse();
        try {
            String[] userMda = ExtractTokenUserMdaId(jwt);
            Assess = new AssessmentServiceImpl();
            Assess.SubmitAssessmentReport(addDto, Integer.parseInt(userMda[0]));
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
    @Path("/assessment/objection-assessment")
    @RolesAllowed({UserRoles.SubAdmin2})
    @ApiOperation(value = "Approve or decline objection SubAdmin2", notes = "", authorizations = {@Authorization(value = "authorization")})
    public Response ObjectAssessment(AssessmentObjectionDto addDto){
        SuccessResponse success = new SuccessResponse();
        try {
            String[] userMda = ExtractTokenUserMdaId(jwt);
            Assess = new AssessmentServiceImpl();
            Assess.ApproveDeclineAssessmentObjection(addDto, Integer.parseInt(userMda[0]));
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
    @Path("/generate-invoice")
    @RolesAllowed({UserRoles.Agent, UserRoles.Individual, UserRoles.NonIndividual})
    @ApiOperation(value = "Generate Invoice", notes = "", authorizations = {@Authorization(value = "authorization")})@ApiResponses(value = {
            @ApiResponse(
                    message = "Successful Invoice generation",
                    code = 200,
                    response = InvoiceDto.class)
    })
    public Response GenerateInvoice(GenerateInvoiceDto invoiceDto) {
        SuccessResponse success = new SuccessResponse();
        try {
            String[] userMda = ExtractTokenUserMdaId(jwt);
            Assess = new AssessmentServiceImpl();
//            Assess.GeneratePaymentInvoice(invoiceDto, Integer.parseInt(userMda[0]));
//            InvoiceDto inv = Assess.GeneratePaymentInvoice(invoiceDto, 1);


            Object[] roleCheck = jwt.getGroups().toArray();
            int office = 0;

            if(roleCheck[0].toString().equals(UserRoles.Agent)){
                invoiceDto.setMdaId(Integer.parseInt(userMda[1]));
                office = Integer.parseInt(userMda[3]);
            }

            if(roleCheck[0].toString().equals(UserRoles.Individual) || roleCheck[0].toString().equals(UserRoles.NonIndividual)){
                invoiceDto.setUserId(Integer.parseInt(userMda[0]));
            }

            InvoiceDto inv = Assess.GeneratePaymentInvoice(invoiceDto, Integer.parseInt(userMda[0]), office);
            
            success.setStatus(200);
            success.setData(inv);
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

    /////////////
    @GET
    @Path("/list-assessments")
    @RolesAllowed({UserRoles.SubAdmin2, UserRoles.SubAdmin1, UserRoles.ProjectReport, UserRoles.Individual, UserRoles.NonIndividual, UserRoles.Agent, UserRoles.Admin})
    @ApiOperation(value = "Sub Admin2, Sub Admin1, Project, Tax Payers", notes = "", authorizations = {@Authorization(value = "authorization")})
    public Response ListAssessment(@QueryParam("is_objections")boolean isObjected, @QueryParam("sort")String sortBy, @QueryParam("page")Integer pageNo,
                                   @QueryParam("per_page")Integer pageSize, @QueryParam("Project_Id")Integer formProjectId, @QueryParam("Mda_Id")Integer formMdaId, @QueryParam("Office_Id")Integer formOfficeId,
                                   @QueryParam("Agent_Id")Integer formAgentId, @QueryParam("start_date")String formStartDate, @QueryParam("end_date")String formEndDate,
                                   @QueryParam("search")String search) {
        SuccessResponse success = new SuccessResponse();
        try {
            if (search == null || search.trim().isEmpty()){

                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

                DateTime today = new DateTime();
                Date endDate = formStartDate == null ? today.toDate() : formatter.parse(formEndDate);
                Date startDate = formEndDate == null ? today.minusMonths(3).toDate() : formatter.parse(formStartDate);

                String[] userMda = ExtractTokenUserMdaId(jwt);
                int UserId = Integer.parseInt(userMda[0]);
                int MdaId = Integer.parseInt(userMda[1]);
                int OfficeId = Integer.parseInt(userMda[3]);
                int MdaProjId = Integer.parseInt(userMda[2]);
                int filterId = 0;
                Assess = new AssessmentServiceImpl();
                Object[] roleCheck = jwt.getGroups().toArray();
                switch (roleCheck[0].toString()){
                    case UserRoles.Admin:
                        if (formProjectId != null && formProjectId > 0) filterId = formProjectId;
                        if (formMdaId != null && formMdaId > 0) filterId = formMdaId;
                        if (formOfficeId != null && formOfficeId > 0) filterId = formOfficeId;
                        if (formAgentId != null && formAgentId > 0) filterId = formAgentId;
                        break;
                    case UserRoles.SubAdmin1:
                    case UserRoles.ProjectReport:
                        filterId = MdaProjId;
                        if (formMdaId != null && formMdaId > 0) filterId = formMdaId;
                        if (formOfficeId != null && formOfficeId > 0) filterId = formOfficeId;
                        if (formAgentId != null && formAgentId > 0) filterId = formAgentId;
                        break;
                    case UserRoles.SubAdmin2:
                        filterId = MdaId;
                        if (formOfficeId != null && formOfficeId > 0) filterId = formOfficeId;
                        if (formAgentId != null && formAgentId > 0) filterId = formAgentId;
                        break;
                    case UserRoles.SubAdmin3:
                        filterId = OfficeId;
//                        if (formOfficeId != null && formOfficeId > 0) filterId = formOfficeId;
                        if (formAgentId != null && formAgentId > 0) filterId = formAgentId;
                        break;
                    case UserRoles.Agent:
//                    filterId = UserId;
//                    break;
                    case UserRoles.Individual:
                    case UserRoles.NonIndividual:
                        filterId = UserId;
                        break;
                }
                PaginatedDto page = Assess.AllMultiAssessment(sortBy, pageNo, pageSize, roleCheck[0].toString(), filterId, isObjected, startDate, endDate);
                success.setStatus(200);
                success.setData(page);
//                return Response.ok(success).build();
            } else{
                String[] userMda = ExtractTokenUserMdaId(jwt);

                int UserId = Integer.parseInt(userMda[0]);
                int MdaId = Integer.parseInt(userMda[1]);
                int MdaProjId = Integer.parseInt(userMda[2]);
                int OfficeId = Integer.parseInt(userMda[3]);
                int filterId = 0;
//            String filterBy = "";
                Assess = new AssessmentServiceImpl();
                Object[] roleCheck = jwt.getGroups().toArray();
                switch (roleCheck[0].toString()){
                    case UserRoles.Admin:
                        if (formProjectId != null && formProjectId > 0) filterId = formProjectId;
                        if (formMdaId != null && formMdaId > 0) filterId = formMdaId;
                        if (formOfficeId != null && formOfficeId > 0) filterId = formOfficeId;
                        if (formAgentId != null && formAgentId > 0) filterId = formAgentId;
                        break;
                    case UserRoles.SubAdmin1:
                    case UserRoles.ProjectReport:
//                    filterBy = "Project";
                        filterId = MdaProjId;
                        if (formMdaId != null && formMdaId > 0) filterId = formMdaId;
                        if (formOfficeId != null && formOfficeId > 0) filterId = formOfficeId;
                        if (formAgentId != null && formAgentId > 0) filterId = formAgentId;
                        break;
                    case UserRoles.SubAdmin2:
                        filterId = MdaId;
                        if (formOfficeId != null && formOfficeId > 0) filterId = formOfficeId;
                        if (formAgentId != null && formAgentId > 0) filterId = formAgentId;
                        break;
                    case UserRoles.SubAdmin3:
                        filterId = OfficeId;
//                        if (formOfficeId != null && formOfficeId > 0) filterId = formOfficeId;
                        if (formAgentId != null && formAgentId > 0) filterId = formAgentId;
                        break;
                    case UserRoles.Agent:
                    case UserRoles.Individual:
                    case UserRoles.NonIndividual:
//                    filterBy = "Payer";
                        filterId = UserId;
                        break;
                }
                List<AssessmentDto> page = Assess.FilterSearchAssessment(search, sortBy, roleCheck[0].toString(), filterId, isObjected, true);
                success.setStatus(200);
                success.setData(page);
//                return Response.ok(success).build();
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
    @Path("/search-assessment")
    @RolesAllowed({UserRoles.SubAdmin2, UserRoles.SubAdmin1, UserRoles.ProjectReport, UserRoles.Individual, UserRoles.NonIndividual, UserRoles.Agent, UserRoles.Admin})
    @ApiOperation(value = "Sub Admin2, Sub Admin1, Project, Tax Payers", notes = "", authorizations = {@Authorization(value = "authorization")})
    public Response SearchAssessment(@QueryParam("is_objections")boolean isObjected, @QueryParam("sort")String sortBy, @QueryParam("search")String search,
                                     @QueryParam("Project_Id")Integer formProjectId, @QueryParam("Mda_Id")Integer formMdaId, @QueryParam("Agent_Id")Integer formAgentId) {
        SuccessResponse success = new SuccessResponse();
        try {
            String[] userMda = ExtractTokenUserMdaId(jwt);

            int UserId = Integer.parseInt(userMda[0]);
            int MdaId = Integer.parseInt(userMda[1]);
            int MdaProjId = Integer.parseInt(userMda[2]);
            int filterId = 0;
//            String filterBy = "";
            Assess = new AssessmentServiceImpl();
            Object[] roleCheck = jwt.getGroups().toArray();
            switch (roleCheck[0].toString()){
                case UserRoles.Admin:
                    if (formProjectId != null && formProjectId > 0) filterId = formProjectId;
                    if (formMdaId != null && formMdaId > 0) filterId = formMdaId;
                    if (formAgentId != null && formAgentId > 0) filterId = formAgentId;
                    break;
                case UserRoles.SubAdmin1:
                case UserRoles.ProjectReport:
//                    filterBy = "Project";
                    filterId = MdaProjId;
                    if (formMdaId != null && formMdaId > 0) filterId = formMdaId;
                    if (formAgentId != null && formAgentId > 0) filterId = formAgentId;
                    break;
                case UserRoles.SubAdmin2:
//                    filterBy = "Mda";
                    filterId = MdaId;
                    if (formAgentId != null && formAgentId > 0) filterId = formAgentId;
                    break;
                case UserRoles.Agent:
                case UserRoles.Individual:
                case UserRoles.NonIndividual:
//                    filterBy = "Payer";
                    filterId = UserId;
                    break;
            }
            List<AssessmentDto> page = Assess.FilterSearchAssessment(search, sortBy, roleCheck[0].toString(), filterId, isObjected, true);
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
    @Path("/pending/search-assessment")
    @RolesAllowed({UserRoles.SubAdmin2, UserRoles.SubAdmin1, UserRoles.ProjectReport, UserRoles.Individual, UserRoles.NonIndividual, UserRoles.Agent, UserRoles.Admin})
    @ApiOperation(value = "Sub Admin2, Sub Admin1, Project, Tax Payers", notes = "", authorizations = {@Authorization(value = "authorization")})
    public Response SearchUnpaidAssessment(@QueryParam("is_objections")boolean isObjected, @QueryParam("sort")String sortBy, @QueryParam("search")String search,
                                     @QueryParam("Project_Id")Integer formProjectId, @QueryParam("Mda_Id")Integer formMdaId, @QueryParam("Agent_Id")Integer formAgentId) {
        SuccessResponse success = new SuccessResponse();
        try {
            String[] userMda = ExtractTokenUserMdaId(jwt);

            int UserId = Integer.parseInt(userMda[0]);
            int MdaId = Integer.parseInt(userMda[1]);
            int MdaProjId = Integer.parseInt(userMda[2]);
            int filterId = 0;
//            String filterBy = "";
            Assess = new AssessmentServiceImpl();
            Object[] roleCheck = jwt.getGroups().toArray();
            switch (roleCheck[0].toString()){
                case UserRoles.Admin:
                    if (formProjectId != null && formProjectId > 0) filterId = formProjectId;
                    if (formMdaId != null && formMdaId > 0) filterId = formMdaId;
                    if (formAgentId != null && formAgentId > 0) filterId = formAgentId;
                    break;
                case UserRoles.SubAdmin1:
                case UserRoles.ProjectReport:
//                    filterBy = "Project";
                    filterId = MdaProjId;
                    if (formMdaId != null && formMdaId > 0) filterId = formMdaId;
                    if (formAgentId != null && formAgentId > 0) filterId = formAgentId;
                    break;
                case UserRoles.SubAdmin2:
//                    filterBy = "Mda";
                    filterId = MdaId;
                    if (formAgentId != null && formAgentId > 0) filterId = formAgentId;
                    break;
                case UserRoles.Agent:
                case UserRoles.Individual:
                case UserRoles.NonIndividual:
//                    filterBy = "Payer";
                    filterId = UserId;
                    break;
            }
            List<AssessmentDto> page = Assess.FilterSearchAssessment(search, sortBy, roleCheck[0].toString(), filterId, isObjected, false);
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
    @Path("/assessment-details")
    @RolesAllowed({UserRoles.SubAdmin2, UserRoles.SubAdmin1, UserRoles.ProjectReport, UserRoles.Individual, UserRoles.NonIndividual, UserRoles.Agent})
    @ApiOperation(value = "Generate Invoice", notes = "", authorizations = {@Authorization(value = "authorization")})
    public Response SingleAssessment(@QueryParam("assessmentId")int id){
        SuccessResponse success = new SuccessResponse();
        try {
            String[] userMda = ExtractTokenUserMdaId(jwt);

            Assess = new AssessmentServiceImpl();

            int UserId = Integer.parseInt(userMda[0]);
            int MdaId = Integer.parseInt(userMda[1]);
            int MdaProjId = Integer.parseInt(userMda[2]);
            int filterId = 0;
            Assess = new AssessmentServiceImpl();
            Object[] roleCheck = jwt.getGroups().toArray();
            switch (roleCheck[0].toString()){
                case UserRoles.Admin:
                    success.setData(Assess.GetSingleAssessment(id, 0, 0, 0));
                    break;
                case UserRoles.SubAdmin1:
                case UserRoles.ProjectReport:
                    filterId = MdaProjId;
                    success.setData(Assess.GetSingleAssessment(id, filterId, 0, 0));
                    break;
                case UserRoles.SubAdmin2:
                case UserRoles.Agent:
                    filterId = MdaId;
                    success.setData(Assess.GetSingleAssessment(id, 0, filterId, 0));
                    break;
                case UserRoles.Individual:
                case UserRoles.NonIndividual:
                    filterId = UserId;
                    success.setData(Assess.GetSingleAssessment(id, 0, 0, filterId));
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

    @GET
    @Path("/list-invoices")
    @RolesAllowed({UserRoles.SubAdmin2, UserRoles.SubAdmin1, UserRoles.ProjectReport, UserRoles.Individual, UserRoles.NonIndividual, UserRoles.Agent})
    @ApiOperation(value = "Sub Admin2, Sub Admin1, Project, Tax Payers", notes = "", authorizations = {@Authorization(value = "authorization")})
    public Response ListInvoices(@QueryParam("sort")String sortBy, @QueryParam("page")Integer pageNo,
                                 @QueryParam("per_page")Integer pageSize, @QueryParam("start_date")String formStartDate, @QueryParam("end_date")String formEndDate,
                                 @QueryParam("search")String search) {
        SuccessResponse success = new SuccessResponse();
        try {
            if(search == null || search.trim().isEmpty()) {

                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

                DateTime today = new DateTime();
                Date endDate = formStartDate == null ? today.toDate() : formatter.parse(formEndDate);
                Date startDate = formEndDate == null ? today.minusMonths(3).toDate() : formatter.parse(formStartDate);

                String[] userMda = ExtractTokenUserMdaId(jwt);

                Assess = new AssessmentServiceImpl();
                int UserId = Integer.parseInt(userMda[0]);
                int MdaId = Integer.parseInt(userMda[1]);
                int MdaProjId = Integer.parseInt(userMda[2]);
                int filterId = 0;
                Assess = new AssessmentServiceImpl();
                Object[] roleCheck = jwt.getGroups().toArray();
                switch (roleCheck[0].toString()) {
                    case UserRoles.SubAdmin1:
                    case UserRoles.ProjectReport:
                        filterId = MdaProjId;
                        break;
                    case UserRoles.SubAdmin2:
                    case UserRoles.Agent:
                        filterId = MdaId;
                        break;
                    case UserRoles.Individual:
                    case UserRoles.NonIndividual:
                        filterId = UserId;
                        break;
                }
                PaginatedDto page = Assess.AllInvoice(sortBy, pageNo, pageSize, roleCheck[0].toString(), filterId, startDate, endDate);
                success.setStatus(200);
                success.setData(page);
            } else {
                String[] userMda = ExtractTokenUserMdaId(jwt);

                Assess = new AssessmentServiceImpl();

                int UserId = Integer.parseInt(userMda[0]);
                int MdaId = Integer.parseInt(userMda[1]);
                int MdaProjId = Integer.parseInt(userMda[2]);
                int filterId = 0;
                Assess = new AssessmentServiceImpl();
                Object[] roleCheck = jwt.getGroups().toArray();
                switch (roleCheck[0].toString()){
                    case UserRoles.SubAdmin1:
                    case UserRoles.ProjectReport:
                        filterId = MdaProjId;
                        break;
                    case UserRoles.SubAdmin2:
                    case UserRoles.Agent:
                        filterId = MdaId;
                        break;
                    case UserRoles.Individual:
                    case UserRoles.NonIndividual:
                        filterId = UserId;
                        break;
                }
                List<InvoiceDto> page = Assess.SearchInvoices(search, sortBy, roleCheck[0].toString(), filterId);
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
    @Path("/search-invoice")
    @RolesAllowed({UserRoles.SubAdmin2, UserRoles.SubAdmin1, UserRoles.ProjectReport, UserRoles.Individual, UserRoles.NonIndividual, UserRoles.Agent})
    @ApiOperation(value = "Sub Admin2, Sub Admin1, Project, Tax Payers", notes = "", authorizations = {@Authorization(value = "authorization")})
    public Response SearchInvoices(@QueryParam("sort")String sortBy, @QueryParam("search")String search) {
        SuccessResponse success = new SuccessResponse();
        try {
            String[] userMda = ExtractTokenUserMdaId(jwt);

            Assess = new AssessmentServiceImpl();

            int UserId = Integer.parseInt(userMda[0]);
            int MdaId = Integer.parseInt(userMda[1]);
            int MdaProjId = Integer.parseInt(userMda[2]);
            int filterId = 0;
            Assess = new AssessmentServiceImpl();
            Object[] roleCheck = jwt.getGroups().toArray();
            switch (roleCheck[0].toString()){
                case UserRoles.SubAdmin1:
                case UserRoles.ProjectReport:
                    filterId = MdaProjId;
                    break;
                case UserRoles.SubAdmin2:
                case UserRoles.Agent:
                    filterId = MdaId;
                    break;
                case UserRoles.Individual:
                case UserRoles.NonIndividual:
                    filterId = UserId;
                    break;
            }
            List<InvoiceDto> page = Assess.SearchInvoices(search, sortBy, roleCheck[0].toString(), filterId);
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
    @Path("/pending/search-invoice")
    @RolesAllowed({UserRoles.SubAdmin2, UserRoles.SubAdmin1, UserRoles.ProjectReport, UserRoles.Individual, UserRoles.NonIndividual, UserRoles.Agent})
    @ApiOperation(value = "Sub Admin2, Sub Admin1, Project, Tax Payers", notes = "", authorizations = {@Authorization(value = "authorization")})
    public Response SearchPendingInvoices(@QueryParam("sort")String sortBy, @QueryParam("search")String search) {
        SuccessResponse success = new SuccessResponse();
        try {
            String[] userMda = ExtractTokenUserMdaId(jwt);

            Assess = new AssessmentServiceImpl();

            int UserId = Integer.parseInt(userMda[0]);
            int MdaId = Integer.parseInt(userMda[1]);
            int MdaProjId = Integer.parseInt(userMda[2]);
            int filterId = 0;
            Assess = new AssessmentServiceImpl();
            Object[] roleCheck = jwt.getGroups().toArray();
            switch (roleCheck[0].toString()){
                case UserRoles.SubAdmin1:
                case UserRoles.ProjectReport:
                    filterId = MdaProjId;
                    break;
                case UserRoles.SubAdmin2:
                case UserRoles.Agent:
                    filterId = MdaId;
                    break;
                case UserRoles.Individual:
                case UserRoles.NonIndividual:
                    filterId = UserId;
                    break;
            }
            List<InvoiceDto> page = Assess.SearchPendingInvoices(search, sortBy, roleCheck[0].toString(), filterId);
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

//    @GET
//    @Path("/invoice")
//    @RolesAllowed({UserRoles.SubAdmin2, UserRoles.SubAdmin1, UserRoles.ProjectReport, UserRoles.Individual, UserRoles.NonIndividual})
//    @ApiOperation(value = "Generate Invoice", notes = "", authorizations = {@Authorization(value = "authorization")})
//    public Response SingleInvoice(){
//        SuccessResponse success = new SuccessResponse();
//        try {
//            String[] userMda = ExtractTokenUserMdaId(jwt);
//
//            Assess = new AssessmentServiceImpl();
//
//            int UserId = Integer.parseInt(userMda[0]);
//            int MdaId = Integer.parseInt(userMda[1]);
//            int MdaProjId = Integer.parseInt(userMda[2]);
//            int filterId = 0;
//            Assess = new AssessmentServiceImpl();
//            Object[] roleCheck = jwt.getGroups().toArray();
//            switch (roleCheck[0].toString()){
//                case UserRoles.SubAdmin1:
//                case UserRoles.ProjectReport:
//                    filterId = MdaProjId;
//                    break;
//                case UserRoles.SubAdmin2:
//                case UserRoles.Agent:
//                    filterId = MdaId;
//                    break;
//                case UserRoles.Individual:
//                case UserRoles.NonIndividual:
//                    filterId = UserId;
//                    break;
//            }
//            success.setStatus(200);
//            success.setData("page");
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


    @GET
    @Path("/temp-tin")
    @RolesAllowed({UserRoles.Individual, UserRoles.NonIndividual, UserRoles.Agent})
    @ApiOperation(value = "Generate Temp TIN", notes = "", authorizations = {@Authorization(value = "authorization")})
    public Response GenerateTempTIn(@QueryParam("count")int count){
        SuccessResponse success = new SuccessResponse();
        try {
            if(count <= 0) count = 1;
            System.out.println(count);
            String[] userMda = ExtractTokenUserMdaId(jwt);

            Assess = new AssessmentServiceImpl();

            int UserId = Integer.parseInt(userMda[0]);
            int MdaId = Integer.parseInt(userMda[1]);
            int MdaProjId = Integer.parseInt(userMda[2]);

            String[] tinArray = new String[count];
            for (int counter = 0; counter < count; counter++) {

                Calendar calendar = Calendar.getInstance();
                java.util.Date currentTime = calendar.getTime();
                long time = currentTime.getTime()/1000;
                long newTin = UserId+MdaId+MdaProjId+time;
                String newTinStr = userMda[2] + userMda[1] + userMda[0] + newTin + counter;

                tinArray[counter] = newTinStr;

            }


            success.setStatus(200);//
//            success.setData(String.format("%06d", newTinStr));
//            success.setData(newTinStr);
            success.setData(tinArray);
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
    @Path("/enumeration/edit")
//    @RolesAllowed({UserRoles.Individual, UserRoles.NonIndividual})
    @RolesAllowed({UserRoles.Individual, UserRoles.NonIndividual, UserRoles.Agent})
    @ApiOperation(value = "Self-service Individual Enumeration", notes = "", authorizations = {@Authorization(value = "authorization")})
    public Response ChangeRequest(NewChangeReqDto enumeration){
        SuccessResponse success = new SuccessResponse();
        try {

            String[] userMda = ExtractTokenUserMdaId(jwt);

//            User = new UserServiceImpl();

            Assess = new AssessmentServiceImpl();
            Assess.ChangeUserDetail(enumeration, Integer.parseInt(userMda[0]), Integer.parseInt(userMda[2]));

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
    @Path("/enumeration/update")
//    @RolesAllowed({UserRoles.Individual, UserRoles.NonIndividual})
    @RolesAllowed({UserRoles.SubAdmin2, UserRoles.SubAdmin1, UserRoles.Admin})
    @ApiOperation(value = "Self-service Individual Enumeration", notes = "", authorizations = {@Authorization(value = "authorization")})
    public Response UpdateChangeRequest(UpdateChangeReqDto enumeration){
        SuccessResponse success = new SuccessResponse();
        try {

            String[] userMda = ExtractTokenUserMdaId(jwt);

//            User = new UserServiceImpl();

            Assess = new AssessmentServiceImpl();
            Assess.UpdateChangeReq(enumeration, Integer.parseInt(userMda[0]), Integer.parseInt(userMda[2]));

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
    @Path("/list-change-req")
    @RolesAllowed({UserRoles.SubAdmin2, UserRoles.SubAdmin1, UserRoles.Admin})
    @ApiOperation(value = "Sub Admin2, Sub Admin1, Project, Tax Payers", notes = "", authorizations = {@Authorization(value = "authorization")})
//    public Response ListChangeRequests(@QueryParam("sort")String sortBy, @QueryParam("page")Integer pageNo,
//                                       @QueryParam("per_page")Integer pageSize, @QueryParam("start_date")String formStartDate, @QueryParam("end_date")String formEndDate) {
    public Response ListChangeRequests(@QueryParam("sort")String sortBy, @QueryParam("page")Integer pageNo,
                                 @QueryParam("per_page")Integer pageSize, @QueryParam("business_id")int bizId) {
        SuccessResponse success = new SuccessResponse();
        try {

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

//            DateTime today = new DateTime();
//            Date endDate = formStartDate == null ? today.toDate() : formatter.parse(formEndDate);
//            Date startDate = formEndDate == null ? today.minusMonths(3).toDate() : formatter.parse(formStartDate);

            String[] userMda = ExtractTokenUserMdaId(jwt);

            Assess = new AssessmentServiceImpl();
            int UserId = Integer.parseInt(userMda[0]);
            int MdaId = Integer.parseInt(userMda[1]);
            int MdaProjId = Integer.parseInt(userMda[2]);
            int filterId = 0;
            Assess = new AssessmentServiceImpl();
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
                case UserRoles.ProjectReport:
                case UserRoles.SubAdmin2:
                    filterId = MdaProjId;
                    break;
//                case UserRoles.Agent:
//                    filterId = MdaId;
//                    break;
//                case UserRoles.Individual:
//                case UserRoles.NonIndividual:
//                    filterId = UserId;
//                    break;
            }

            PaginatedDto page = Assess.GetChangeReq(sortBy, pageNo, pageSize, filterId);
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
    @Path("/count-change-req")
    @RolesAllowed({UserRoles.SubAdmin2, UserRoles.SubAdmin1, UserRoles.Admin})
    @ApiOperation(value = "Sub Admin2, Sub Admin1, Project, Tax Payers", notes = "", authorizations = {@Authorization(value = "authorization")})
    public Response CountChangeRequests(@QueryParam("business_id")int bizId) {
        SuccessResponse success = new SuccessResponse();
        try {

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

//            DateTime today = new DateTime();
//            Date endDate = formStartDate == null ? today.toDate() : formatter.parse(formEndDate);
//            Date startDate = formEndDate == null ? today.minusMonths(3).toDate() : formatter.parse(formStartDate);

            String[] userMda = ExtractTokenUserMdaId(jwt);

            Assess = new AssessmentServiceImpl();
            int UserId = Integer.parseInt(userMda[0]);
            int MdaId = Integer.parseInt(userMda[1]);
            int MdaProjId = Integer.parseInt(userMda[2]);
            int filterId = 0;
            Assess = new AssessmentServiceImpl();
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
                case UserRoles.ProjectReport:
                case UserRoles.SubAdmin2:
                    filterId = MdaProjId;
                    break;
//                case UserRoles.Agent:
//                    filterId = MdaId;
//                    break;
//                case UserRoles.Individual:
//                case UserRoles.NonIndividual:
//                    filterId = UserId;
//                    break;
            }

            success.setStatus(200);
            success.setData(Assess.CountChangeReq(filterId));
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
    @Path("/edit-profile")
    @RolesAllowed({UserRoles.Individual, UserRoles.NonIndividual, UserRoles.SubAdmin1, UserRoles.Agent})
    @ApiOperation(value = " ", notes = "", authorizations = {@Authorization(value = "authorization")})
    public Response EditUser(UserUpdateDto updateDto){
        SuccessResponse success = new SuccessResponse();
        try {
            String[] userMda = ExtractTokenUserMdaId(jwt);
            updateDto.id = Integer.parseInt(userMda[0]);
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


}
