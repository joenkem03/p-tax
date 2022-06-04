package org.bizzdeskgroup.api.v1;


import io.swagger.annotations.*;
import org.bizzdeskgroup.Dto.SuccessResponse;
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

@Path("mda-report")
@Api(value = "Report(Admin, SubAdmin1, ProjectReport, SubAdmin2,", description = "")
@RequestScoped
@Produces("application/json")
@Consumes("application/json")
//@RolesAllowed({UserRoles.SubAdmin1, UserRoles.Agent, UserRoles.Admin})
public class UserReportResource {

    @Inject
    JsonWebToken jwt;

    OrganisationService Org = null;
    PosService Pos = null;
    TransactionService Trans = null;
    PosTransactionService PosTrans = null;
    AssessmentService Assess = null;
    UserService User = null;

//    count(mda_user_id), count(mda_service_id), count(mda_enum_id), count(mda_tax_payers),
//    count(mda_tax), sum(assessment) groupby mda_id

//    @POST
//    @Path("/summary")
//    @ApiOperation(value = "Get count for \"mda\", \"mdaOffice\" or  \"mdaService\" ", notes = "", authorizations = {
//            @Authorization(value = "authorization")})
//    public Response Dashboard(@Context SecurityContext ctx, @QueryParam("MdaId")Integer MdaId, @QueryParam("CountItem")String item_count){
//        SuccessResponse success = new SuccessResponse();
//            Org = new OrganisationServiceImpl();
//            try {
    
//                if (ctx.isUserInRole(UserRoles.SubAdmin2)){
//                    Org.CountMdaOffices(MdaId);
//                } else{
//
//                }
//                success.setStatus(200);
//                success.setData("");
//                return Response.ok(success).build();
//            } catch (Exception e) {
//                System.out.println("Errr" + e.getMessage());
//                e.printStackTrace();
//                success.setStatus(400);
//                success.setData(e.getMessage());
//                return Response.ok(success).build();
//            }
//
//    }

    @GET
    @Path("/mdasummary")
    @ApiResponses(value = {
            @ApiResponse(
                    message = "Mda Summary Count",
                    code = 200,
                    response = SummaryDto.class)
    })
    @RolesAllowed({UserRoles.SubAdmin2, UserRoles.Admin, UserRoles.SubAdmin1, UserRoles.ProjectReport, UserRoles.Agent})
    @ApiOperation(value = "", notes = "", authorizations = {
            @Authorization(value = "authorization")})
    public Response MdaSummary(@QueryParam("Project_Id")Integer formProjectId, @QueryParam("Mda_Id")Integer formMdaId, @QueryParam("last_month")boolean isLastMonth){
//        public Response MdaSummary(@Context SecurityContext ctx, @QueryParam("MdaId")Integer MdaId){
        SuccessResponse success = new SuccessResponse();
        try {
            String[] userMda = ExtractTokenUserMdaId(jwt);
            Org = new OrganisationServiceImpl();
//            int mdaOffices = Org.CountMdaOffices(MdaId);
//            int mdaServices = Org.CountMdaServices(MdaId);
//            int mdaAgents = Org.CountMdaAgents(MdaId);



            List<String> roleListAg = new LinkedList<>();
            roleListAg.add(UserRoles.Agent);

            List<String> roleListSb = new LinkedList<>();
            roleListSb.add(UserRoles.SubAdmin2);
            roleListSb.add(UserRoles.ProjectReport);


            DateTime today = new DateTime();
            if (isLastMonth) today = today.minusMonths(1);

            Pos = new PosServiceImpl();
            Assess = new AssessmentServiceImpl();
            User = new UserServiceImpl();
//            int mdaPoses = Pos.CountMdaPoses(MdaId);
//            SummaryDto summary = new SummaryDto();
            Object[] roleCheck = jwt.getGroups().toArray();
            switch (roleCheck[0].toString()){
                case UserRoles.Admin:
                    if (formProjectId != null && formProjectId > 0) {
                        if(formMdaId != null && formMdaId > 0 ){
//                            int MdaId = Integer.parseInt(userMda[1]);
//                            int MdaProjId = Integer.parseInt(userMda[2]);
                            MdaSummaryDto mdaSummary = new MdaSummaryDto();
                            mdaSummary.services = Org.CountMdaServices(formMdaId);
                            mdaSummary.offices = Org.CountMdaOffices(formMdaId);
                            mdaSummary.agents = Org.CountMdaAgents(formMdaId);

                            mdaSummary.agents = User.CountUsers(roleListAg, 0, formMdaId, true);
                            mdaSummary.subAdmins = User.CountUsers(roleListSb, 0, formMdaId, false);
                            mdaSummary.agentSubAdmins = mdaSummary.agents + mdaSummary.subAdmins;

                            mdaSummary.poses = Pos.CountMdaPoses(formMdaId);
//                            mdaSummary.assessedAmount = 0;
//                            mdaSummary.assessedAmountPaid = 0;
                            mdaSummary.individualsEnum = Assess.CountIndividuals(formProjectId, 0);
                            mdaSummary.corpEnum = Assess.CountNonIndividuals(formProjectId, 0);
                            mdaSummary.totalEnum = mdaSummary.individualsEnum + mdaSummary.corpEnum;
                            mdaSummary.assessment = Assess.CountAssessments(formProjectId, 0, 0, 0);

                            success.setData(mdaSummary);
                        } else {
                            ProjectSummaryDto projectSummary = new ProjectSummaryDto();
                            projectSummary.services = Org.CountProjectServices(formProjectId);
                            projectSummary.offices = Org.CountProjectOffices(formProjectId);
                            projectSummary.agents = Org.CountProjectAgents(formProjectId);

                            projectSummary.agents = User.CountUsers(roleListAg, formProjectId, 0, true);
                            projectSummary.subAdmins = User.CountUsers(roleListSb, formProjectId, 0, false);
                            projectSummary.agentSubAdmins = projectSummary.agents + projectSummary.subAdmins;

                            projectSummary.poses = Pos.CountProjectPoses(formProjectId);
                            projectSummary.mdas = Org.CountProjectMda(formProjectId);
//                            projectSummary.assessedAmount = 0;
//                            projectSummary.assessedAmountPaid = 0;
                            projectSummary.individualsEnum = Assess.CountIndividuals(formProjectId, 0);
                            projectSummary.corpEnum = Assess.CountNonIndividuals(formProjectId, 0);
                            projectSummary.totalEnum = projectSummary.individualsEnum + projectSummary.corpEnum;
                            projectSummary.assessment = Assess.CountAssessments(formProjectId, 0, 0, 0);
                            success.setData(projectSummary);
                        }
                    } else {
                        SummaryDto summary = new SummaryDto();
                        summary.services = Org.CountServices(0, 0);
                        summary.offices = Org.CountOffices(0, 0);
//                        summary.agents = Org.CountAgents();

                        summary.agents = User.CountUsers(roleListAg, 0, 0, true);
                        summary.subAdmins = User.CountUsers(roleListSb, 0, 0, false);
                        summary.agentSubAdmins = summary.agents + summary.subAdmins;

                        summary.poses = Pos.CountPoses();
                        summary.projects = Org.CountProjects();
                        summary.mdas = Org.CountMda();
//                        summary.assessedAmount = 0;
//                        summary.assessedAmountPaid = 0;
                        summary.individualsEnum = Assess.CountIndividuals(0, 0);
                        summary.corpEnum = Assess.CountNonIndividuals(0, 0);
                        summary.totalEnum = summary.individualsEnum + summary.corpEnum;
                        summary.assessment = Assess.CountAssessments(0, 0, 0, 0);
                        success.setData(summary);
                    }
                    break;
                case UserRoles.SubAdmin1:
                case UserRoles.ProjectReport:
                    if (formMdaId != null && formMdaId > 0) {
                        MdaSummaryDto mdaSummary = new MdaSummaryDto();
                        mdaSummary.services = Org.CountMdaServices(formMdaId);
                        mdaSummary.offices = Org.CountMdaOffices(formMdaId);
                        mdaSummary.agents = Org.CountMdaAgents(formMdaId);
                        mdaSummary.poses = Pos.CountMdaPoses(formMdaId);
//                        mdaSummary.assessedAmount = 0;
//                        mdaSummary.assessedAmountPaid = 0;
                        mdaSummary.individualsEnum = Assess.CountIndividuals(Integer.parseInt(userMda[2]), 0);
                        mdaSummary.corpEnum = Assess.CountNonIndividuals(Integer.parseInt(userMda[2]), 0);
                        mdaSummary.totalEnum = mdaSummary.individualsEnum + mdaSummary.corpEnum;
                        mdaSummary.assessment = Assess.CountAssessments(Integer.parseInt(userMda[2]), 0, 0, 0);
                        success.setData(mdaSummary);
                    } else {
                        ProjectSummaryDto projectSummary = new ProjectSummaryDto();
                        int ProjId = Integer.parseInt(userMda[2]);
                        projectSummary.services = Org.CountProjectServices(ProjId);
                        projectSummary.offices = Org.CountProjectOffices(ProjId);
                        projectSummary.agents = Org.CountProjectAgents(ProjId);
                        projectSummary.poses = Pos.CountProjectPoses(ProjId);
                        projectSummary.mdas = Org.CountProjectMda(ProjId);
//                        projectSummary.assessedAmount = 0;
//                        projectSummary.assessedAmountPaid = 0;
                        projectSummary.individualsEnum = Assess.CountIndividuals(ProjId, 0);
                        projectSummary.corpEnum = Assess.CountNonIndividuals(ProjId, 0);
                        projectSummary.totalEnum = projectSummary.individualsEnum + projectSummary.corpEnum;
                        projectSummary.assessment = Assess.CountAssessments(ProjId, 0, 0, 0);
                        success.setData(projectSummary);
                    }
                    break;
                case UserRoles.SubAdmin2:
                    int MdaId = Integer.parseInt(userMda[1]);
                    int MdaProjId = Integer.parseInt(userMda[2]);
                    MdaSummaryDto mdaSummary = new MdaSummaryDto();
                    mdaSummary.services = Org.CountMdaServices(MdaId);
                    mdaSummary.offices = Org.CountMdaOffices(MdaId);
                    mdaSummary.agents = Org.CountMdaAgents(MdaId);
                    mdaSummary.poses = Pos.CountMdaPoses(MdaId);
//                    mdaSummary.assessedAmount = 0;
//                    mdaSummary.assessedAmountPaid = 0;
                    mdaSummary.individualsEnum = Assess.CountIndividuals(MdaProjId, 0);
                    mdaSummary.corpEnum = Assess.CountNonIndividuals(MdaProjId, 0);
                    mdaSummary.totalEnum = mdaSummary.individualsEnum + mdaSummary.corpEnum;
                    mdaSummary.assessment = Assess.CountAssessments(MdaId, 0, 0, 0);
                    success.setData(mdaSummary);
                    break;

            }

//            summary.services = mdaServices;
//            summary.offices = mdaOffices;
//            summary.agents = mdaAgents;
//            summary.poses = mdaPoses;

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
    @Path("/transactionsummary")
    @ApiResponses(value = {
            @ApiResponse(
                    message = "Transactions Summary ",
                    code = 200,
                    response = TransactionSummaryDto.class)
    })
    @ApiOperation(value = "Transaction summary for All ROles. Drill down to project for Admin; MDA for SubAdmin1/ProjectReport; Agent for SubAdmin2 ", notes = "", authorizations = {
            @Authorization(value = "authorization")})
    @RolesAllowed({UserRoles.SubAdmin2, UserRoles.Admin, UserRoles.SubAdmin1, UserRoles.ProjectReport, UserRoles.Agent})
//    public Response TransactionSummary(@Context SecurityContext ctx, @QueryParam("MdaId")Integer MdaId){
//    public Response TransactionSummary(@QueryParam("Id")Integer Id, @QueryParam("Report_Level")String ReportLevel){
    public Response TransactionSummary(@QueryParam("Project_Id")Integer formProjectId, @QueryParam("Mda_Id")Integer formMdaId,
                                       @QueryParam("Agent_Id")Integer formAgentId){
        SuccessResponse success = new SuccessResponse();
        try {
            String[] userMda = ExtractTokenUserMdaId(jwt);

            Trans = new TransactionServiceImpl();

            Org = new OrganisationServiceImpl();

            Pos = new PosServiceImpl();

            PosTrans = new PosTransactionServiceImpl();
//            int mdaPoses = Pos.CountMdaPoses(MdaId);
            DateTime today = new DateTime();

            TransactionSummaryDto summary = new TransactionSummaryDto();
            Object[] roleCheck = jwt.getGroups().toArray();
            switch (roleCheck[0].toString()){
                case UserRoles.Admin:
                    if (formProjectId != null && formProjectId > 0) {
                        if(formMdaId != null && formMdaId > 0 ){
                            summary.ytdAmount = Trans.AllMdaTransactionAmount(formMdaId, formProjectId);
                            summary.ytdCount = Trans.AllMdaTransactionCount(formMdaId, formProjectId);
                            summary.lastMonthAmount = Trans.MonthMdaTransactionAmount(formMdaId, formProjectId, today.minusMonths(1));
                            summary.lastMonthCount = Trans.MonthMdaTransactionCount(formMdaId, formProjectId, today.minusMonths(1));
                            summary.thisMonthAmount = Trans.MonthMdaTransactionAmount(formMdaId, formProjectId, today);
                            summary.thisMonthCount = Trans.MonthMdaTransactionCount(formMdaId, formProjectId, today);
                            summary.todayAmount = Trans.DayMdaTransactionAmount(formMdaId, formProjectId, today);
                            summary.todayCount = Trans.DayMdaTransactionCount(formMdaId, formProjectId, today);
                            summary.yesterdayAmount = Trans.DayMdaTransactionAmount(formMdaId, formProjectId, today.minusDays(1));
                            summary.yesterdayCount = Trans.DayMdaTransactionCount(formMdaId, formProjectId, today.minusDays(1));
                        }
                        else if(formAgentId != null && formAgentId > 0){
                            summary.ytdAmount = PosTrans.AllAgentTransactionAmount(formAgentId, formProjectId);
                            summary.ytdCount = PosTrans.AllAgentTransactionCount(formAgentId,formProjectId);
                            summary.lastMonthAmount = 0;
                            summary.lastMonthCount = 0;
                            summary.thisMonthAmount = 0;
                            summary.thisMonthCount = 0;
                            summary.todayAmount = 0;
                            summary.todayCount = 0;
                            summary.yesterdayAmount = 0;
                            summary.yesterdayCount = 0;
                        } else {
                            summary.ytdAmount = Trans.AllProjectTransactionAmount(formProjectId);
                            summary.ytdCount = Trans.AllProjectTransactionCount(formProjectId);
                            summary.lastMonthAmount = Trans.MonthProjectTransactionAmount(formProjectId, today.minusMonths(1));
                            summary.lastMonthCount = Trans.MonthProjectTransactionCount(formProjectId, today.minusMonths(1));
                            summary.thisMonthAmount = Trans.MonthProjectTransactionAmount(formProjectId, today);
                            summary.thisMonthCount = Trans.MonthProjectTransactionCount(formProjectId, today);
                            summary.todayAmount = Trans.DayProjectTransactionAmount(formProjectId, today);
                            summary.todayCount = Trans.DayProjectTransactionCount(formProjectId, today);
                            summary.yesterdayAmount = Trans.DayProjectTransactionAmount(formProjectId, today.minusDays(1));
                            summary.yesterdayCount = Trans.DayProjectTransactionCount(formProjectId, today.minusDays(1));
                        }

                    }
//                    else if(formTaxPayerId != null && formTaxPayerId > 0){
//                        summary.ytdAmount = 0;
//                        summary.ytdCount = 0;
//                        summary.lastMonthAmount = 0;
//                        summary.lastMonthCount = 0;
//                        summary.thisMonthAmount = 0;
//                        summary.thisMonthCount = 0;
//                        summary.todayAmount = 0;
//                        summary.todayCount = 0;
//                        summary.yesterdayAmount = 0;
//                        summary.yesterdayCount = 0;
//                    }
                    else {
                        summary.ytdAmount = Trans.AllTransactionAmount();
                        summary.ytdCount = Trans.AllTransactionCount();
                        summary.lastMonthAmount = Trans.MonthTransactionAmount(today.minusMonths(1));
                        summary.lastMonthCount = Trans.MonthTransactionCount(today.minusMonths(1));
                        summary.thisMonthAmount = Trans.MonthTransactionAmount(today);
                        summary.thisMonthCount = Trans.MonthTransactionCount(today);
                        summary.todayAmount = Trans.DayTransactionAmount(today);
                        summary.todayCount = Trans.DayTransactionCount(today);
                        summary.yesterdayAmount = Trans.DayTransactionAmount(today.minusDays(1));
                        summary.yesterdayCount = Trans.DayTransactionCount(today.minusDays(1));
                    }
                    break;
                case UserRoles.SubAdmin1:
                case UserRoles.ProjectReport:
                    if (formMdaId != null && formMdaId > 0) {
                        int IdProjId = Integer.parseInt(userMda[2]);

                        if (formAgentId != null && formAgentId > 0) {
//                                int IdProjId = Integer.parseInt(userMda[2]);
                            summary.ytdAmount = PosTrans.AllAgentTransactionAmount(formAgentId, IdProjId);
                            summary.ytdCount = PosTrans.AllAgentTransactionCount(formAgentId,IdProjId);
                            summary.lastMonthAmount = 0;
                            summary.lastMonthCount = 0;
                            summary.thisMonthAmount = 0;
                            summary.thisMonthCount = 0;
                            summary.todayAmount = 0;
                            summary.todayCount = 0;
                            summary.yesterdayAmount = 0;
                            summary.yesterdayCount = 0;
                        } else {
                            summary.ytdAmount = Trans.AllMdaTransactionAmount(formMdaId, IdProjId);
                            summary.ytdCount = Trans.AllMdaTransactionCount(formMdaId, IdProjId);
                            summary.lastMonthAmount = Trans.MonthMdaTransactionAmount(formMdaId, IdProjId, today.minusMonths(1));
                            summary.lastMonthCount = Trans.MonthMdaTransactionCount(formMdaId, IdProjId, today.minusMonths(1));
                            summary.thisMonthAmount = Trans.MonthMdaTransactionAmount(formMdaId, IdProjId, today);
                            summary.thisMonthCount = Trans.MonthMdaTransactionCount(formMdaId, IdProjId, today);
                            summary.todayAmount = Trans.DayMdaTransactionAmount(formMdaId, IdProjId, today);
                            summary.todayCount = Trans.DayMdaTransactionCount(formMdaId, IdProjId, today);
                            summary.yesterdayAmount = Trans.DayMdaTransactionAmount(formMdaId, IdProjId, today.minusDays(1));
                            summary.yesterdayCount = Trans.DayMdaTransactionCount(formMdaId, IdProjId, today.minusDays(1));
                        }
                    } else {
                        int ProjId = Integer.parseInt(userMda[2]);
                        summary.ytdAmount = Trans.AllProjectTransactionAmount(ProjId);
                        summary.ytdCount = Trans.AllProjectTransactionCount(ProjId);
                        summary.lastMonthAmount = Trans.MonthProjectTransactionAmount(ProjId, today.minusMonths(1));
                        summary.lastMonthCount = Trans.MonthProjectTransactionCount(ProjId, today.minusMonths(1));
                        summary.thisMonthAmount = Trans.MonthProjectTransactionAmount(ProjId, today);
                        summary.thisMonthCount = Trans.MonthProjectTransactionCount(ProjId, today);
                        summary.todayAmount = Trans.DayProjectTransactionAmount(ProjId, today);
                        summary.todayCount = Trans.DayProjectTransactionCount(ProjId, today);
                        summary.yesterdayAmount = Trans.DayProjectTransactionAmount(ProjId, today.minusDays(1));
                        summary.yesterdayCount = Trans.DayProjectTransactionCount(ProjId, today.minusDays(1));
                    }
                    break;
                case UserRoles.SubAdmin2:
                    if (formAgentId != null &&  formAgentId > 0) {
//                            int IdProjId = Integer.parseInt(userMda[2]);
                        int IdAgentProjId = Integer.parseInt(userMda[2]);
                        summary.ytdAmount = PosTrans.AllAgentTransactionAmount(formAgentId, IdAgentProjId);
                        summary.ytdCount = PosTrans.AllAgentTransactionCount(formAgentId,IdAgentProjId);
                        summary.lastMonthAmount = 0;
                        summary.lastMonthCount = 0;
                        summary.thisMonthAmount = 0;
                        summary.thisMonthCount = 0;
                        summary.todayAmount = 0;
                        summary.todayCount = 0;
                        summary.yesterdayAmount = 0;
                        summary.yesterdayCount = 0;
                    } else {
                        int MdaId = Integer.parseInt(userMda[1]);
                        int MdaProjId = Integer.parseInt(userMda[2]);
                        summary.ytdAmount = Trans.AllMdaTransactionAmount(MdaId, MdaProjId);
                        summary.ytdCount = Trans.AllMdaTransactionCount(MdaId, MdaProjId);
                        summary.lastMonthAmount = Trans.MonthMdaTransactionAmount(MdaId, MdaProjId, today.minusMonths(1));
                        summary.lastMonthCount = Trans.MonthMdaTransactionCount(MdaId, MdaProjId, today.minusMonths(1));
                        summary.thisMonthAmount = Trans.MonthMdaTransactionAmount(MdaId, MdaProjId, today);
                        summary.thisMonthCount = Trans.MonthMdaTransactionCount(MdaId, MdaProjId, today);
                        summary.todayAmount = Trans.DayMdaTransactionAmount(MdaId, MdaProjId, today);
                        summary.todayCount = Trans.DayMdaTransactionCount(MdaId, MdaProjId, today);
                        summary.yesterdayAmount = Trans.DayMdaTransactionAmount(MdaId, MdaProjId, today.minusDays(1));
                        summary.yesterdayCount = Trans.DayMdaTransactionCount(MdaId, MdaProjId, today.minusDays(1));
                    }
                    break;
                case UserRoles.Agent:
                    int AgentId = Integer.parseInt(userMda[0]);
                    int AgentMdajId = Integer.parseInt(userMda[1]);
                    int AgentProjId = Integer.parseInt(userMda[2]);
                    summary.ytdAmount = PosTrans.AllAgentTransactionAmount(AgentId, AgentMdajId);
                    summary.ytdCount = PosTrans.AllAgentTransactionCount(AgentId,AgentMdajId);
                    summary.lastMonthAmount = PosTrans.MonthAgentTransactionAmount(AgentId, AgentMdajId, today.minusMonths(1));
                    summary.lastMonthCount = PosTrans.MonthAgentTransactionCount(AgentId, AgentMdajId, today.minusMonths(1));
                    summary.thisMonthAmount = PosTrans.MonthAgentTransactionAmount(AgentId, AgentMdajId, today);
                    summary.thisMonthCount = PosTrans.MonthAgentTransactionCount(AgentId, AgentMdajId, today);
                    summary.todayAmount = PosTrans.DayAgentTransactionAmount(AgentId, AgentMdajId, today);
                    summary.todayCount = PosTrans.DayAgentTransactionCount(AgentId, AgentMdajId, today);
                    summary.yesterdayAmount = PosTrans.DayAgentTransactionAmount(AgentId, AgentMdajId, today.minusDays(1));
                    summary.yesterdayCount = PosTrans.DayAgentTransactionCount(AgentId, AgentMdajId, today.minusDays(1));
                    break;
//                case UserRoles.Individual:
//                case UserRoles.NonIndividual:
//                    int UserId = Integer.parseInt(userMda[0]);
//                    summary.ytdAmount = Trans.AllUserTransactionAmount(UserId);
//                    summary.ytdCount = Trans.AllUserTransactionCount(UserId);
//                    summary.lastMonthAmount = 0;
//                    summary.lastMonthCount = 0;
//                    summary.thisMonthAmount = 0;
//                    summary.thisMonthCount = 0;
//                    summary.todayAmount = 0;
//                    summary.todayCount = 0;
//                    summary.yesterdayAmount = 0;
//                    summary.yesterdayCount = 0;
//                    break;
//                default:
////                    if(roleCheck[0].toString().equals(UserRoles.Admin) && projectId > 0) {
//                    if(roleCheck[0].toString().equals(UserRoles.Admin)) {
//                    }
//                    if((roleCheck[0].toString().equals(UserRoles.SubAdmin1) || roleCheck[0].toString().equals(UserRoles.ProjectReport))){
////                        int MdaId = Integer.parseInt(userMda[1]);
//                    }
//                    if(roleCheck[0].toString().equals(UserRoles.SubAdmin2)){
////                        int AgentId = Integer.parseInt(userMda[0]);
//                    }
            }

            success.setStatus(200);
            success.setData(summary);
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
    @Path("/transactionsummary-monthly")
    @ApiResponses(value = {
            @ApiResponse(
                    message = "Transactions Summary ",
                    code = 200,
                    response = TransactionMonthlySummaryDto.class)
    })
    @ApiOperation(value = "Transaction summary for All ROles. Drill down to project for Admin; MDA for SubAdmin1/ProjectReport; Agent for SubAdmin2 ", notes = "", authorizations = {
            @Authorization(value = "authorization")})
    @RolesAllowed({UserRoles.SubAdmin2, UserRoles.Admin, UserRoles.SubAdmin1, UserRoles.ProjectReport, UserRoles.Individual, UserRoles.NonIndividual, UserRoles.Agent})
//    public Response TransactionSummary(@Context SecurityContext ctx, @QueryParam("MdaId")Integer MdaId){
//    public Response TransactionSummary(@QueryParam("Id")Integer Id, @QueryParam("Report_Level")String ReportLevel){
    public Response TransactionSummaryMonthly(@QueryParam("Project_Id")Integer formProjectId, @QueryParam("Mda_Id")Integer formMdaId,
                                       @QueryParam("Agent_Id")Integer formAgentId, @QueryParam("Taxpayer_Id")Integer formTaxPayerId,
                                              @QueryParam("last_month")boolean isLastMonth){
        SuccessResponse success = new SuccessResponse();
        try {
            String[] userMda = ExtractTokenUserMdaId(jwt);

            Trans = new TransactionServiceImpl();

            Org = new OrganisationServiceImpl();

            Pos = new PosServiceImpl();

            PosTrans = new PosTransactionServiceImpl();
//            int mdaPoses = Pos.CountMdaPoses(MdaId);
            DateTime today = new DateTime();
            if (isLastMonth) today = today.minusMonths(1);

            TransactionMonthlySummaryDto summary = new TransactionMonthlySummaryDto();


            Object[] roleCheck = jwt.getGroups().toArray();
            switch (roleCheck[0].toString()){
                case UserRoles.Admin:
                    if (formProjectId != null && formProjectId > 0) {
                        if(formMdaId != null && formMdaId > 0 ){
                            summary = Trans.AltMonthMdaTransactionAmount(formMdaId, formProjectId, today);
                        }
                        else if(formAgentId != null && formAgentId > 0){
                            summary = null;
                        } else {
                            summary = Trans.AltMonthProjectTransactionAmount(formProjectId, today);
                        }

                    } else if(formTaxPayerId != null && formTaxPayerId > 0){
                        summary = null;
                    } else {
                        summary = Trans.AltMonthTransactionAmount(today);
                    }
                    break;
                case UserRoles.SubAdmin1:
                case UserRoles.ProjectReport:
                    if (formMdaId != null && formMdaId > 0) {
                        int IdProjId = Integer.parseInt(userMda[2]);

                        if (formAgentId != null && formAgentId > 0) {
                            summary = null;
                        } else {
                            summary = Trans.AltMonthMdaTransactionAmount(formMdaId, IdProjId, today);
                        }
                    } else {
                        int ProjId = Integer.parseInt(userMda[2]);
                        summary = Trans.AltMonthProjectTransactionAmount(ProjId, today);
                    }
                    break;
                case UserRoles.SubAdmin2:
                    if (formAgentId != null &&  formAgentId > 0) {
//                            int IdProjId = Integer.parseInt(userMda[2]);
                        int IdAgentProjId = Integer.parseInt(userMda[2]);
                        summary = null;
                    } else {
                        int MdaId = Integer.parseInt(userMda[1]);
                        int MdaProjId = Integer.parseInt(userMda[2]);
                        summary = Trans.AltMonthMdaTransactionAmount(MdaId, MdaProjId, today);
                    }
                    break;
                case UserRoles.Agent:
                    int AgentId = Integer.parseInt(userMda[0]);
                    int AgentMdajId = Integer.parseInt(userMda[1]);
                    int AgentProjId = Integer.parseInt(userMda[2]);
//                    summary = Trans.AltMonthAgentTransactionAmount(AgentId, AgentMdajId, today);
                    summary = Trans.AltMonthTransactionAmountAgent(today, Integer.parseInt(userMda[0]));
//                    summary = null;
                    break;
                case UserRoles.Individual:
                case UserRoles.NonIndividual:
                    int UserId = Integer.parseInt(userMda[0]);
                    summary = Assess.PayerPaidInvoiceSum(Integer.parseInt(userMda[0]), today);
                    break;
//                default:
////                    if(roleCheck[0].toString().equals(UserRoles.Admin) && projectId > 0) {
//                    if(roleCheck[0].toString().equals(UserRoles.Admin)) {
//                    }
//                    if((roleCheck[0].toString().equals(UserRoles.SubAdmin1) || roleCheck[0].toString().equals(UserRoles.ProjectReport))){
////                        int MdaId = Integer.parseInt(userMda[1]);
//                    }
//                    if(roleCheck[0].toString().equals(UserRoles.SubAdmin2)){
////                        int AgentId = Integer.parseInt(userMda[0]);
//                    }
            }

            success.setStatus(200);
            if(summary == null){
                TransactionMonthlySummaryDto summa = new TransactionMonthlySummaryDto();
                summa.count =0;
                summa.totalRev =0;
                summa.income =0;
                summa.remitted =0;

                success.setData(summa);
            } else {
                success.setData(summary);
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
    @Path("/transactionsummary-singlechannel-monthly")
    @ApiResponses(value = {
            @ApiResponse(
                    message = "Transactions Summary ",
                    code = 200,
                    response = TransactionMonthlySummaryDto.class)
    })
    @ApiOperation(value = "Transaction summary for All ROles. Drill down to project for Admin; MDA for SubAdmin1/ProjectReport; Agent for SubAdmin2 ", notes = "", authorizations = {
            @Authorization(value = "authorization")})
    @RolesAllowed({UserRoles.SubAdmin2, UserRoles.Admin, UserRoles.SubAdmin1, UserRoles.ProjectReport, UserRoles.Individual, UserRoles.NonIndividual, UserRoles.Agent})
//    public Response TransactionSummary(@Context SecurityContext ctx, @QueryParam("MdaId")Integer MdaId){
//    public Response TransactionSummary(@QueryParam("Id")Integer Id, @QueryParam("Report_Level")String ReportLevel){
    public Response TransactionSummaryMonthlySingleChannel(@QueryParam("Project_Id")Integer formProjectId, @QueryParam("Mda_Id")Integer formMdaId,
                                              @QueryParam("Agent_Id")Integer formAgentId, @QueryParam("Office_Id")Integer formOfficeId,
                                              @QueryParam("last_month")boolean isLastMonth, @QueryParam("channel")String channel){
        SuccessResponse success = new SuccessResponse();
        try {
            String[] userMda = ExtractTokenUserMdaId(jwt);

            Trans = new TransactionServiceImpl();

            Org = new OrganisationServiceImpl();

            Pos = new PosServiceImpl();

            PosTrans = new PosTransactionServiceImpl();
//            int mdaPoses = Pos.CountMdaPoses(MdaId);
            DateTime today = new DateTime();
            if (isLastMonth) today = today.minusMonths(1);

            TransactionChannelSummaryDto summary = new TransactionChannelSummaryDto();
            Object[] roleCheck = jwt.getGroups().toArray();
            switch (roleCheck[0].toString()){
                case UserRoles.Admin:

                        if(formMdaId != null && formMdaId > 0 ){
                            summary = Trans.SingleChannelMonthlySummary(0, formMdaId, 0, 0, today, channel);
                            break;
                        }
                        if(formAgentId != null && formAgentId > 0){
                            summary = Trans.SingleChannelMonthlySummary(0, 0, 0, formAgentId, today, channel);
                            break;
                        }

                        if(formOfficeId != null && formOfficeId > 0){
                        summary = Trans.SingleChannelMonthlySummary(0, 0, formOfficeId, 0, today, channel);
                            break;
                        }

                    if(formProjectId != null && formProjectId > 0){
                        summary = Trans.SingleChannelMonthlySummary(formProjectId, 0, 0, 0, today, channel);
                        break;
                    }
                    summary = Trans.SingleChannelMonthlySummary(0, 0, 0, 0, today, channel);
                    break;
                case UserRoles.SubAdmin1:
                case UserRoles.ProjectReport:
                    if(formMdaId != null && formMdaId > 0 ){
                        summary = Trans.SingleChannelMonthlySummary(0, formMdaId, 0, 0, today, channel);
                        break;
                    }
                    if(formAgentId != null && formAgentId > 0){
                        summary = Trans.SingleChannelMonthlySummary(0, 0, 0, formAgentId, today, channel);
                        break;
                    }

                    if(formOfficeId != null && formOfficeId > 0){
                        summary = Trans.SingleChannelMonthlySummary(0, 0, formOfficeId, 0, today, channel);
                        break;
                    }
                    summary = Trans.SingleChannelMonthlySummary(Integer.parseInt(userMda[2]), 0, 0, 0, today, channel);
                    break;
                case UserRoles.SubAdmin2:
                    if(formAgentId != null && formAgentId > 0){
                        summary = Trans.SingleChannelMonthlySummary(0, 0, 0, formAgentId, today, channel);
                        break;
                    }

                    if(formOfficeId != null && formOfficeId > 0){
                        summary = Trans.SingleChannelMonthlySummary(0, 0, formOfficeId, 0, today, channel);
                        break;
                    }
                    summary = Trans.SingleChannelMonthlySummary(0, Integer.parseInt(userMda[1]), 0, 0, today, channel);
                    break;
                case UserRoles.SubAdmin3:
//                    int UserId = Integer.parseInt(userMda[0]);
                    summary = Trans.SingleChannelMonthlySummary(0, 0, Integer.parseInt(userMda[3]), 0, today, channel);
                    break;
                case UserRoles.Agent:
                    int AgentId = Integer.parseInt(userMda[0]);
                    int AgentMdajId = Integer.parseInt(userMda[1]);
                    int AgentProjId = Integer.parseInt(userMda[2]);
                    summary = Trans.SingleChannelMonthlySummary(0, 0, 0, Integer.parseInt(userMda[0]), today, channel);
                    break;
//                case UserRoles.Individual:
//                case UserRoles.NonIndividual:
//                    int UserId = Integer.parseInt(userMda[0]);
//                    summary = Assess.PayerPaidInvoiceSum(Integer.parseInt(userMda[0]), today);
//                    break;
//                default:
////                    if(roleCheck[0].toString().equals(UserRoles.Admin) && projectId > 0) {
//                    if(roleCheck[0].toString().equals(UserRoles.Admin)) {
//                    }
//                    if((roleCheck[0].toString().equals(UserRoles.SubAdmin1) || roleCheck[0].toString().equals(UserRoles.ProjectReport))){
////                        int MdaId = Integer.parseInt(userMda[1]);
//                    }
//                    if(roleCheck[0].toString().equals(UserRoles.SubAdmin2)){
////                        int AgentId = Integer.parseInt(userMda[0]);
//                    }
            }

            success.setStatus(200);
            success.setData(summary);
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
    @Path("/transactionsummary-monthly-split")
    @ApiResponses(value = {
            @ApiResponse(
                    message = "Transactions Summary ",
                    code = 200,
                    response = TransactionMonthlySummaryDto.class)
    })
    @ApiOperation(value = "Transaction summary for All ROles. Drill down to project for Admin; MDA for SubAdmin1/ProjectReport; Agent for SubAdmin2 ", notes = "", authorizations = {
            @Authorization(value = "authorization")})
    @RolesAllowed({UserRoles.SubAdmin2, UserRoles.Admin, UserRoles.SubAdmin1, UserRoles.ProjectReport})
//    public Response TransactionSummary(@Context SecurityContext ctx, @QueryParam("MdaId")Integer MdaId){
//    public Response TransactionSummary(@QueryParam("Id")Integer Id, @QueryParam("Report_Level")String ReportLevel){
    public Response TransactionSummaryMonthlyPie(@QueryParam("Project_Id")Integer formProjectId, @QueryParam("Mda_Id")Integer formMdaId,
                                              @QueryParam("last_month")boolean isLastMonth){
        SuccessResponse success = new SuccessResponse();
        try {
            String[] userMda = ExtractTokenUserMdaId(jwt);

            Trans = new TransactionServiceImpl();

            Org = new OrganisationServiceImpl();

            Pos = new PosServiceImpl();

            PosTrans = new PosTransactionServiceImpl();
//            int mdaPoses = Pos.CountMdaPoses(MdaId);
            DateTime today = new DateTime();
            if (isLastMonth) today = today.minusMonths(1);


            TransactionMonthlyPieDto summaryPie = new TransactionMonthlyPieDto();
            TransactionMonthlySummaryDto summary = new TransactionMonthlySummaryDto();
            Object[] roleCheck = jwt.getGroups().toArray();
            switch (roleCheck[0].toString()){
                case UserRoles.Admin:
                    if (formProjectId != null && formProjectId > 0) {
                        if(formMdaId != null && formMdaId > 0 ){
                            summary = Trans.AltMonthMdaTransactionAmount(formMdaId, formProjectId, today);
                        }
//                        else if(formAgentId != null && formAgentId > 0){
//                            summary = null;
//                        }
                        else {
                            summary = Trans.AltMonthProjectTransactionAmount(formProjectId, today);
                        }

                    }
//                    else if(formTaxPayerId != null && formTaxPayerId > 0){
//                        summary = null;
//                    }
                    else {
                        summary = Trans.AltMonthTransactionAmount(today);
                    }
                    break;
                case UserRoles.SubAdmin1:
                case UserRoles.ProjectReport:
                    if (formMdaId != null && formMdaId > 0) {
                        int IdProjId = Integer.parseInt(userMda[2]);

//                        if (formAgentId != null && formAgentId > 0) {
//                            summary = null;
//                        }
//                        else {
                            summary = Trans.AltMonthMdaTransactionAmount(formMdaId, IdProjId, today);
//                        }
                    } else {
                        int ProjId = Integer.parseInt(userMda[2]);
                        summary = Trans.AltMonthProjectTransactionAmount(ProjId, today);
                    }
                    break;
                case UserRoles.SubAdmin2:
//                    if (formAgentId != null &&  formAgentId > 0) {
////                            int IdProjId = Integer.parseInt(userMda[2]);
//                        int IdAgentProjId = Integer.parseInt(userMda[2]);
//                        summary = null;
//                    }
//                    if {
                        int MdaId = Integer.parseInt(userMda[1]);
                        int MdaProjId = Integer.parseInt(userMda[2]);
//                        System.out.println("--mda--");
//                        System.out.println(MdaId);
                        summary = Trans.AltMonthMdaTransactionAmount(MdaId, MdaProjId, today);
//                    }
                    break;
                case UserRoles.Agent:
                    int AgentId = Integer.parseInt(userMda[0]);
                    int AgentMdajId = Integer.parseInt(userMda[1]);
                    int AgentProjId = Integer.parseInt(userMda[2]);
//                    summary = PosTrans.AltMonthAgentTransactionAmount(AgentId, AgentMdajId, today);
                    summary = null;
                    break;
//                case UserRoles.Individual:
//                case UserRoles.NonIndividual:
//                    int UserId = Integer.parseInt(userMda[0]);
//                    summary.ytdAmount = Trans.AllUserTransactionAmount(UserId);
//                    summary.ytdCount = Trans.AllUserTransactionCount(UserId);
//                    summary.lastMonthAmount = 0;
//                    summary.lastMonthCount = 0;
//                    summary.thisMonthAmount = 0;
//                    summary.thisMonthCount = 0;
//                    summary.todayAmount = 0;
//                    summary.todayCount = 0;
//                    summary.yesterdayAmount = 0;
//                    summary.yesterdayCount = 0;
//                    break;
//                default:
////                    if(roleCheck[0].toString().equals(UserRoles.Admin) && projectId > 0) {
//                    if(roleCheck[0].toString().equals(UserRoles.Admin)) {
//                    }
//                    if((roleCheck[0].toString().equals(UserRoles.SubAdmin1) || roleCheck[0].toString().equals(UserRoles.ProjectReport))){
////                        int MdaId = Integer.parseInt(userMda[1]);
//                    }
//                    if(roleCheck[0].toString().equals(UserRoles.SubAdmin2)){
////                        int AgentId = Integer.parseInt(userMda[0]);
//                    }
            }

//            assert summary != null;
            if(summary != null) {
                summaryPie.bizzdesk = summary.income;
                summaryPie.state = summary.remitted;
                summaryPie.total = summary.totalRev;
            }

            success.setStatus(200);
            success.setData(summaryPie);
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
    @Path("/transactionsummary-monthly-channel")
    @ApiResponses(value = {
            @ApiResponse(
                    message = "Transactions Summary ",
                    code = 200,
                    response = TransactionMonthlySummaryDto.class)
    })
    @ApiOperation(value = "Transaction summary for All ROles. Drill down to project for Admin; MDA for SubAdmin1/ProjectReport; Agent for SubAdmin2 ", notes = "", authorizations = {
            @Authorization(value = "authorization")})
    @RolesAllowed({UserRoles.SubAdmin2, UserRoles.Admin, UserRoles.SubAdmin1, UserRoles.ProjectReport})
//    public Response TransactionSummary(@Context SecurityContext ctx, @QueryParam("MdaId")Integer MdaId){
//    public Response TransactionSummary(@QueryParam("Id")Integer Id, @QueryParam("Report_Level")String ReportLevel){
    public Response TransactionSummaryChannelPie(@QueryParam("Project_Id")Integer formProjectId, @QueryParam("Mda_Id")Integer formMdaId,
                                                 @QueryParam("last_month")boolean isLastMonth){
        SuccessResponse success = new SuccessResponse();
        try {
            String[] userMda = ExtractTokenUserMdaId(jwt);

            Trans = new TransactionServiceImpl();

            Org = new OrganisationServiceImpl();

            Pos = new PosServiceImpl();

            PosTrans = new PosTransactionServiceImpl();
//            int mdaPoses = Pos.CountMdaPoses(MdaId);
            DateTime today = new DateTime();
            if (isLastMonth) today = today.minusMonths(1);

            List<TransactionChannelSummaryDto> summary = new LinkedList<TransactionChannelSummaryDto>();
            Object[] roleCheck = jwt.getGroups().toArray();
            switch (roleCheck[0].toString()){
                case UserRoles.Admin:
                    if (formProjectId != null && formProjectId > 0) {
                        if(formMdaId != null && formMdaId > 0 ){
                            summary = Trans.MonthMdaTransactionAmountChannel(formMdaId, formProjectId, today);
                        }
//                        else if(formAgentId != null && formAgentId > 0){
//                            summary = null;
//                        }
                        else {
                            summary = Trans.MonthProjectTransactionAmountChannel(formProjectId, today);
                        }

                    }
//                    else if(formTaxPayerId != null && formTaxPayerId > 0){
//                        summary = null;
//                    }
                    else {
                        summary = Trans.MonthTransactionAmountChannel(today);
                    }
                    break;
                case UserRoles.SubAdmin1:
                case UserRoles.ProjectReport:
                    if (formMdaId != null && formMdaId > 0) {
                        int IdProjId = Integer.parseInt(userMda[2]);

//                        if (formAgentId != null && formAgentId > 0) {
//                            summary = null;
//                        } else {
                            summary = Trans.MonthMdaTransactionAmountChannel(formMdaId, IdProjId, today);
//                        }
                    } else {
                        int ProjId = Integer.parseInt(userMda[2]);
                        summary = Trans.MonthProjectTransactionAmountChannel(ProjId, today);
                    }
                    break;
                case UserRoles.SubAdmin2:
//                    if (formAgentId != null &&  formAgentId > 0) {
////                            int IdProjId = Integer.parseInt(userMda[2]);
//                        int IdAgentProjId = Integer.parseInt(userMda[2]);
//                        summary = null;
//                    }
//                    if {
                        int MdaId = Integer.parseInt(userMda[1]);
                        int MdaProjId = Integer.parseInt(userMda[2]);
                        summary = Trans.MonthMdaTransactionAmountChannel(MdaId, MdaProjId, today);
//                    }
                    break;
                case UserRoles.Agent:
                    int AgentId = Integer.parseInt(userMda[0]);
                    int AgentMdajId = Integer.parseInt(userMda[1]);
                    int AgentProjId = Integer.parseInt(userMda[2]);
//                    summary = PosTrans.AltMonthAgentTransactionAmount(AgentId, AgentMdajId, today);
                    summary = null;
                    break;
//                case UserRoles.Individual:
//                case UserRoles.NonIndividual:
//                    int UserId = Integer.parseInt(userMda[0]);
//                    summary.ytdAmount = Trans.AllUserTransactionAmount(UserId);
//                    summary.ytdCount = Trans.AllUserTransactionCount(UserId);
//                    summary.lastMonthAmount = 0;
//                    summary.lastMonthCount = 0;
//                    summary.thisMonthAmount = 0;
//                    summary.thisMonthCount = 0;
//                    summary.todayAmount = 0;
//                    summary.todayCount = 0;
//                    summary.yesterdayAmount = 0;
//                    summary.yesterdayCount = 0;
//                    break;
//                default:
////                    if(roleCheck[0].toString().equals(UserRoles.Admin) && projectId > 0) {
//                    if(roleCheck[0].toString().equals(UserRoles.Admin)) {
//                    }
//                    if((roleCheck[0].toString().equals(UserRoles.SubAdmin1) || roleCheck[0].toString().equals(UserRoles.ProjectReport))){
////                        int MdaId = Integer.parseInt(userMda[1]);
//                    }
//                    if(roleCheck[0].toString().equals(UserRoles.SubAdmin2)){
////                        int AgentId = Integer.parseInt(userMda[0]);
//                    }
            }

            success.setStatus(200);
            success.setData(summary);
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
    @Path("/twelve-months-summary")
    @ApiResponses(value = {
            @ApiResponse(
                    message = "Mda Transactions Summary ",
                    code = 200,
                    response = MonthlyTransactionsDto.class,
                    responseContainer = "List")
    })
    @ApiOperation(value = "Monthly Summary for Admin, Sub-Admin1/Project-Report, Sub-Admin2. Drill down to project for Admin; MDA for SubAdmin1/ProjectReport, ", notes = "", authorizations = {
            @Authorization(value = "authorization")})
    public Response TwelveMonthsSummary(@QueryParam("Project_Id")Integer formProjectId, @QueryParam("Mda_Id")Integer formMdaId){
        SuccessResponse success = new SuccessResponse();
        try {
            String[] userMda = ExtractTokenUserMdaId(jwt);
            Trans = new TransactionServiceImpl();
            List<MonthlyTransactionsDto> summary = new LinkedList<MonthlyTransactionsDto>();
            Object[] roleCheck = jwt.getGroups().toArray();
            switch (roleCheck[0].toString()){
                case UserRoles.Admin:
                    summary = formProjectId == null || formProjectId <= 0 ? Trans.GetAllMonthlySummary() :
                                formMdaId == null || formMdaId <= 0 ? Trans.GetProjectMonthlySummary(formProjectId) : Trans.GetMdaMonthlySummary(formMdaId, formMdaId);
                    break;
                case UserRoles.SubAdmin1:
                case UserRoles.ProjectReport:
                    int ProjId = Integer.parseInt(userMda[2]);
                    summary = formMdaId == null || formMdaId <= 0 ? Trans.GetProjectMonthlySummary(ProjId) : Trans.GetMdaMonthlySummary(ProjId, formMdaId);
                    break;
                case UserRoles.SubAdmin2:
                    int MdaId = Integer.parseInt(userMda[1]);
                    int MdaProjId = Integer.parseInt(userMda[2]);
                    summary = Trans.GetMdaMonthlySummary(MdaProjId, MdaId);
                    break;
                case UserRoles.Agent:
                    int AgentMdaId = Integer.parseInt(userMda[1]);
                    int AgentId = Integer.parseInt(userMda[0]);
                    summary = PosTrans.GetAgentMonthlySummary(AgentId, AgentMdaId);
                    break;
            }

            success.setStatus(200);
            success.setData(summary);
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
    @Path("/remittance")
    @ApiOperation(value = " ", notes = "  ", authorizations = {@Authorization(value = "authorization")})
    @ApiResponses(value = {
            @ApiResponse(
                    message = "Agent remittance",
                    code = 200,
                    response = RemitSummaryListDto.class)
    })

    @RolesAllowed({UserRoles.Agent, UserRoles.SubAdmin2, UserRoles.SubAdmin1, UserRoles.ProjectReport, UserRoles.Admin})
    public Response GetRemittanceSummary(@QueryParam("agent_id")int AgentId, @QueryParam("is_last_month") boolean isLastMonth){
        SuccessResponse success = new SuccessResponse();
        try {


//            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            DateTime today = new DateTime();
            if (isLastMonth) today = today.minusMonths(1);

            String[] userMda = ExtractTokenUserMdaId(jwt);
            PosTrans = new PosTransactionServiceImpl();
            Object[] roleCheck = jwt.getGroups().toArray();
            switch (roleCheck[0].toString()){
                case UserRoles.Admin:
                case UserRoles.SubAdmin1:
                case UserRoles.ProjectReport:
                case UserRoles.SubAdmin2:
                    success.setData(PosTrans.AgentRemittanceSum(AgentId, today));
                    break;
                case UserRoles.Agent:

                    success.setData(PosTrans.AgentRemittanceSum(Integer.parseInt(userMda[0]), today));
                    break;
            }
//            PaginatedDto Res = PosTrans.AgentRemittanceSum(Integer.parseInt(userMda[0]), DateTime today);
            success.setStatus(200);
//            success.setData(PosTrans.AgentRemittanceSum(Integer.parseInt(userMda[0]), DateTime today));
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
    @Path("/mdas-summary")
    @ApiOperation(value = "Admin", notes = "", authorizations = {
            @Authorization(value = "authorization")}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    message = "States",
                    code = 200,
                    response = AllMdaSummaryDto.class,
                    responseContainer = "")
    })
    @RolesAllowed({UserRoles.Admin, UserRoles.SubAdmin1, UserRoles.ProjectReport, UserRoles.SubAdmin2})
    public Response AllMdaSummary(@QueryParam("business_id")int businessId, @QueryParam("mda_id")int formMda){
        SuccessResponse success = new SuccessResponse();
        try {
            String[] userMda = ExtractTokenUserMdaId(jwt);
            Object[] roleCheck = jwt.getGroups().toArray();

            List<String> roleListAg = new LinkedList<>();
            roleListAg.add(UserRoles.Agent);

            List<String> roleListSb = new LinkedList<>();
            roleListSb.add(UserRoles.SubAdmin2);
            roleListSb.add(UserRoles.ProjectReport);

            Org = new OrganisationServiceImpl();
            User = new UserServiceImpl();

            AllMdaSummaryDto ps = new AllMdaSummaryDto();

            switch (roleCheck[0].toString()) {
                case UserRoles.Admin:
                    if(formMda > 0){
                        ps.level = "mda";
                        ps.title = Org.SingleMda(formMda).name;
                        ps.projectData = Org.SingleBusiness(businessId, true);
                        ps.levelData = Org.SingleMda(formMda);
                        ps.offices = Org.CountOffices(0, formMda);
                        ps.agents = Org.CountMdaAgentsAlt(formMda, true, true);
                        ps.subAdmins = Org.CountMdaAgentsAlt(formMda, false, true);
//                        ps.agentSubAdmins = Org.CountMdaAgents(formMda);
                        ps.agentSubAdmins = ps.subAdmins + ps.agents;
                        ps.revenueHeads = Org.CountServices(0, formMda);
                        break;
                    }
                    if(businessId > 0){

                        ps.level = "state_project";
                        ps.title = Org.SingleBusiness(businessId, true).clientName;
                        ps.levelData = Org.SingleBusiness(businessId, true);
                        ps.mdas = Org.CountProjectMda(businessId);
                        ps.offices = Org.CountOffices(businessId, 0);
                        ps.agents = User.CountUsers(roleListAg, businessId, 0, true);
                        ps.subAdmins = User.CountUsers(roleListSb, businessId, 0, true);
                        ps.agentSubAdmins = ps.agents + ps.subAdmins;
                        ps.revenueHeads = Org.CountServices(businessId, 0);
                        break;

                    }

                    ps.level = "admin";
                    ps.title = " ";
                    ps.levelData = null;
                    ps.mdas = Org.CountMda();
                    ps.offices = Org.CountOffices(0, 0);
                    ps.agents = User.CountUsers(roleListAg, businessId, 0, true);
                    ps.subAdmins = User.CountUsers(roleListSb, businessId, 0, true);
                    ps.agentSubAdmins = ps.agents + ps.subAdmins;
                    ps.revenueHeads = Org.CountServices(0, 0);
                    break;
                case UserRoles.SubAdmin1:
                case UserRoles.ProjectReport:
                    int ProjId = Integer.parseInt(userMda[2]);

                    if(formMda > 0){
                        ps.level = "mda";
                        ps.title = Org.SingleMda(formMda).name;
                        ps.projectData = Org.SingleBusiness(ProjId, true);
                        ps.levelData = Org.SingleMda(formMda);
                        ps.offices = Org.CountOffices(0, formMda);
                        ps.agents = Org.CountMdaAgentsAlt(formMda, true, true);
                        ps.subAdmins = Org.CountMdaAgentsAlt(formMda, false, true);
//                        ps.agentSubAdmins = Org.CountMdaAgents(formMda);
                        ps.agentSubAdmins = ps.subAdmins + ps.agents;
                        ps.revenueHeads = Org.CountServices(0, formMda);
                        break;
                    }

                    ps.level = "state_project";
                    ps.title = Org.SingleBusiness(ProjId, true).clientName;
                    ps.projectData = Org.SingleBusiness(ProjId, true);
                    ps.levelData = Org.SingleBusiness(ProjId, true);
                    ps.mdas = Org.CountProjectMda(ProjId);
                    ps.offices = Org.CountOffices(ProjId, 0);
                    ps.agents = User.CountUsers(roleListAg, ProjId, 0, true);
                    ps.subAdmins = User.CountUsers(roleListSb, ProjId, 0, true);
                    ps.agentSubAdmins = ps.agents + ps.subAdmins;
                    ps.revenueHeads = Org.CountServices(ProjId, 0);

                    break;
                case UserRoles.SubAdmin2:
                    int MdaId = Integer.parseInt(userMda[1]);
                    int MdaProjId = Integer.parseInt(userMda[2]);

                    ps.level = "mda";
                    ps.title = Org.SingleMda(formMda).name;
                    ps.projectData = Org.SingleBusiness(MdaProjId, true);
                    ps.levelData = Org.SingleMda(formMda);
                    ps.offices = Org.CountOffices(0, MdaId);
                    ps.agents = Org.CountMdaAgentsAlt(MdaId, true, true);
                    ps.subAdmins = Org.CountMdaAgentsAlt(MdaId, false, true);
//                    ps.agentSubAdmins = Org.CountMdaAgents(MdaId);
                    ps.agentSubAdmins = ps.subAdmins + ps.agents;
                    ps.revenueHeads = Org.CountServices(0, MdaId);
                    break;
            }
            success.setStatus(200);
            success.setData(ps);

            return Response.ok(ps).build();
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
    @Path("/rev-officers-summary")
    @ApiOperation(value = "Admin", notes = "", authorizations = {
            @Authorization(value = "authorization")}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    message = "Revenue Officers",
                    code = 200,
                    response = AllOfficersSummaryDto.class,
                    responseContainer = "")
    })
    @RolesAllowed({UserRoles.Admin, UserRoles.SubAdmin1, UserRoles.ProjectReport, UserRoles.SubAdmin2})
    public Response AllAgentsSummary(@QueryParam("business_id")int businessId, @QueryParam("mda_id")int formMda){
        SuccessResponse success = new SuccessResponse();
        try {
            String[] userMda = ExtractTokenUserMdaId(jwt);
            Object[] roleCheck = jwt.getGroups().toArray();

            List<String> roleListAg = new LinkedList<>();
            roleListAg.add(UserRoles.Agent);

//            List<String> roleListSb = new LinkedList<>();
//            roleListSb.add(UserRoles.SubAdmin2);
//            roleListSb.add(UserRoles.ProjectReport);

            Org = new OrganisationServiceImpl();
            User = new UserServiceImpl();

            AllOfficersSummaryDto ps = new AllOfficersSummaryDto();

            switch (roleCheck[0].toString()) {
                case UserRoles.Admin:
                    if(formMda > 0){
                        ps.level = "mda";
                        ps.title = Org.SingleMda(formMda).name;
                        ps.levelData = Org.SingleMda(formMda);
                        ps.active = Org.CountMdaAgentsAlt(formMda, true, true);
                        ps.inactive = Org.CountMdaAgentsAlt(formMda, true, false);
                        ps.all =  ps.active + ps.inactive;
                        break;
                    }
                    if(businessId > 0){

                        ps.level = "state_project";
                        ps.title = Org.SingleBusiness(businessId, true).clientName;
                        ps.levelData = Org.SingleBusiness(businessId, true);
                        ps.active = User.CountUsers(roleListAg, businessId, 0, true);
                        ps.inactive = User.CountUsers(roleListAg, businessId, 0, false);
                        ps.all =  ps.active + ps.inactive;

                    }

                    ps.level = "admin";
                    ps.title = " ";
                    ps.active = User.CountUsers(roleListAg, 0, 0, true);
                    ps.inactive = User.CountUsers(roleListAg, 0, 0, false);
                    ps.all = ps.active + ps.inactive;
                    break;
                case UserRoles.SubAdmin1:
                case UserRoles.ProjectReport:
                    int ProjId = Integer.parseInt(userMda[2]);

                    if(formMda > 0){
                        ps.level = "mda";
                        ps.title = Org.SingleMda(formMda).name;
                        ps.levelData = Org.SingleMda(formMda);
                        ps.active = Org.CountMdaAgentsAlt(formMda, true, true);
                        ps.inactive = Org.CountMdaAgentsAlt(formMda, true, false);
                        ps.all = ps.active + ps.inactive;
                        break;
                    }

                    ps.level = "state_project";
                    ps.title = Org.SingleBusiness(ProjId, true).clientName;
                    ps.levelData = Org.SingleBusiness(businessId, true);
                    ps.inactive = User.CountUsers(roleListAg, ProjId, 0, false);
                    ps.active = User.CountUsers(roleListAg, ProjId, 0, true);
                    ps.all = ps.inactive + ps.active;

                    break;
                case UserRoles.SubAdmin2:
                    int MdaId = Integer.parseInt(userMda[1]);
                    int MdaProjId = Integer.parseInt(userMda[2]);

                    ps.level = "mda";
                    ps.title = Org.SingleMda(MdaId).name;
                    ps.levelData = Org.SingleMda(MdaId);
                    ps.active = Org.CountMdaAgentsAlt(MdaId, true, true);
                    ps.inactive = Org.CountMdaAgentsAlt(MdaId, true, false);
                    ps.all = ps.active + ps.inactive;
                    break;
            }
            success.setStatus(200);
            success.setData(ps);

            return Response.ok(ps).build();
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
    @Path("/poses-summary")
    @ApiOperation(value = "Admin", notes = "", authorizations = {
            @Authorization(value = "authorization")}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    message = "Pos",
                    code = 200,
                    response = AllOfficersSummaryDto.class,
                    responseContainer = "")
    })
    @RolesAllowed({UserRoles.Admin, UserRoles.SubAdmin1, UserRoles.ProjectReport, UserRoles.SubAdmin2, })
    public Response AllPosesSummary(@QueryParam("business_id")int businessId, @QueryParam("mda_id")int formMda){
        SuccessResponse success = new SuccessResponse();
        try {
            String[] userMda = ExtractTokenUserMdaId(jwt);
            Object[] roleCheck = jwt.getGroups().toArray();

            Org = new OrganisationServiceImpl();
            User = new UserServiceImpl();
            Pos = new PosServiceImpl();

            AllOfficersSummaryDto ps = new AllOfficersSummaryDto();

            switch (roleCheck[0].toString()) {
                case UserRoles.Admin:
                    if(formMda > 0){
                        ps.level = "mda";
                        ps.title = Org.SingleMda(formMda).name;
                        ps.levelData = Org.SingleMda(formMda);
                        ps.active = Pos.CountPosesAll(0, formMda, true);
                        ps.inactive = Pos.CountPosesAll(0, formMda, false);
                        ps.all =  ps.active + ps.inactive;
                        break;
                    }
                    if(businessId > 0){

                        ps.level = "state_project";
                        ps.title = Org.SingleBusiness(businessId, true).clientName;
                        ps.levelData = Org.SingleBusiness(businessId, true);
                        ps.active = Pos.CountPosesAll(businessId, 0, true);
                        ps.inactive = Pos.CountPosesAll(businessId, 0, false);
                        ps.all =  ps.active + ps.inactive;

                    }

                    ps.level = "admin";
                    ps.title = " ";
                    ps.active = Pos.CountPosesAll(0, 0, true);
                    ps.inactive = Pos.CountPosesAll(0, 0, false);
                    ps.all =  ps.active + ps.inactive;
                    break;
                case UserRoles.SubAdmin1:
                case UserRoles.ProjectReport:
                    int ProjId = Integer.parseInt(userMda[2]);

                    if(formMda > 0){
                        ps.level = "mda";
                        ps.title = Org.SingleMda(formMda).name;
                        ps.levelData = Org.SingleMda(formMda);
                        ps.active = Pos.CountPosesAll(0, formMda, true);
                        ps.inactive = Pos.CountPosesAll(0, formMda, false);
                        ps.all =  ps.active + ps.inactive;
                        break;
                    }

                    ps.level = "state_project";
                    ps.title = Org.SingleBusiness(ProjId, true).clientName;
                    ps.active = Pos.CountPosesAll(ProjId, 0, true);
                    ps.inactive = Pos.CountPosesAll(ProjId, 0, false);
                    ps.all =  ps.active + ps.inactive;

                    break;
                case UserRoles.SubAdmin2:
                    int MdaId = Integer.parseInt(userMda[1]);
                    int MdaProjId = Integer.parseInt(userMda[2]);

                    ps.level = "mda";
                    ps.title = Org.SingleMda(MdaId).name;
                    ps.levelData = Org.SingleMda(MdaId);
                    ps.active = Pos.CountPosesAll(0, MdaId, true);
                    ps.inactive = Pos.CountPosesAll(0, MdaId, false);
                    ps.all =  ps.active + ps.inactive;
                    break;
            }
            success.setStatus(200);
            success.setData(ps);

            return Response.ok(ps).build();
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
    @Path("/enumeration-summary")
    @ApiOperation(value = "Admin", notes = "", authorizations = {
            @Authorization(value = "authorization")}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    message = "Enumeration; Admins & Agent",
                    code = 200,
                    response = AllEnumerationSummaryDto.class,
                    responseContainer = "")
    })
    @RolesAllowed({UserRoles.Admin, UserRoles.SubAdmin1, UserRoles.ProjectReport, UserRoles.SubAdmin2, UserRoles.Agent})
    public Response AllEnumerationSummary(@QueryParam("business_id")int businessId, @QueryParam("agent_id")int formAgent){
        SuccessResponse success = new SuccessResponse();
        try {
            String[] userMda = ExtractTokenUserMdaId(jwt);
            Object[] roleCheck = jwt.getGroups().toArray();


            List<String> roleListAg = new LinkedList<>();
            roleListAg.add(UserRoles.Agent);

            Org = new OrganisationServiceImpl();
            User = new UserServiceImpl();
            Pos = new PosServiceImpl();
            Assess = new AssessmentServiceImpl();

            AllEnumerationSummaryDto ps = new AllEnumerationSummaryDto();

            switch (roleCheck[0].toString()) {
                case UserRoles.Admin:
                    if(formAgent > 0){
                        UserDto us = User.SingleUser(formAgent);

                        ps.level = "agent";
                        ps.title = us.firstName+ " "+ us.lastName ;
                        ps.levelData = us;

                        ps.corporate = Assess.CountNonIndividuals(0, formAgent);
                        ps.individual = Assess.CountIndividuals(0, formAgent);
                        ps.all =  ps.corporate + ps.individual;
                        break;

                    }
                    if(businessId > 0){

                        ps.level = "state_project";
                        ps.title = Org.SingleBusiness(businessId, true).clientName;
                        ps.levelData = Org.SingleBusiness(businessId, true);


                        ps.corporate = Assess.CountNonIndividuals(businessId, 0);
                        ps.individual = Assess.CountIndividuals(businessId, 0);
                        ps.all =  ps.corporate + ps.individual;
                        ps.enumerators = User.CountUsers(roleListAg, businessId, 0, true);

                        break;

                    }

                    ps.level = "admin";
                    ps.title = " ";

                    ps.corporate = Assess.CountNonIndividuals(0, 0);
                    ps.individual = Assess.CountIndividuals(0, 0);
                    ps.all =  ps.corporate + ps.individual;
                    ps.enumerators = User.CountUsers(roleListAg, 0, 0, true);
                    break;
                case UserRoles.SubAdmin1:
                case UserRoles.ProjectReport:

                case UserRoles.SubAdmin2:
                    if(formAgent > 0){
                        UserDto us = User.SingleUser(formAgent);

                        ps.level = "agent";
                        ps.title = us.firstName+ " "+ us.lastName ;
                        ps.levelData = us;

                        ps.corporate = Assess.CountNonIndividuals(0, formAgent);
                        ps.individual = Assess.CountIndividuals(0, formAgent);
                        ps.all =  ps.corporate + ps.individual;
                        break;

                    }

                    int ProjId = Integer.parseInt(userMda[2]);
//                    int MdaId = Integer.parseInt(userMda[1]);
//                    int MdaProjId = Integer.parseInt(userMda[2]);

                    ps.level = "state_project";
                    ps.title = Org.SingleBusiness(ProjId, true).clientName;
                    ps.levelData = Org.SingleBusiness(ProjId, true);

                    ps.corporate = Assess.CountNonIndividuals(ProjId, 0);
                    ps.individual = Assess.CountIndividuals(ProjId, 0);
                    ps.all =  ps.corporate + ps.individual;
                    ps.enumerators = User.CountUsers(roleListAg, ProjId, 0, true);
                    break;

                case UserRoles.Agent:
                    int AgentId = Integer.parseInt(userMda[0]);
//                    int MdaId = Integer.parseInt(userMda[1]);
//                    int MdaProjId = Integer.parseInt(userMda[2]);
                    UserDto us = User.SingleUser(AgentId);

                    ps.level = "agent";
                    ps.title = us.firstName+ " "+ us.lastName ;
                    ps.levelData = us;

                    ps.corporate = Assess.CountNonIndividuals(0, AgentId);
                    ps.individual = Assess.CountIndividuals(0, AgentId);
                    ps.all =  ps.corporate + ps.individual;
                    break;
            }
            success.setStatus(200);
            success.setData(ps);

            return Response.ok(ps).build();
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
    @Path("/assessment-summary")
    @ApiOperation(value = "Admin", notes = "", authorizations = {
            @Authorization(value = "authorization")}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    message = "Assessment; Admins & Agent",
                    code = 200,
                    response = AllAssessmentSummaryDto.class,
                    responseContainer = "")
    })
    @RolesAllowed({UserRoles.Admin, UserRoles.SubAdmin1, UserRoles.ProjectReport, UserRoles.SubAdmin2, UserRoles.Agent, UserRoles.Individual, UserRoles.NonIndividual})
    public Response AllAssessmentSummary(@QueryParam("business_id")int businessId, @QueryParam("mda_id")int formMda, @QueryParam("agent_id")int formAgent, @QueryParam("payer_id")int formPayer){
        SuccessResponse success = new SuccessResponse();
        try {
            String[] userMda = ExtractTokenUserMdaId(jwt);
            Object[] roleCheck = jwt.getGroups().toArray();


            List<String> roleListAg = new LinkedList<>();
            roleListAg.add(UserRoles.Agent);

            Org = new OrganisationServiceImpl();
            User = new UserServiceImpl();
            Pos = new PosServiceImpl();
            Assess = new AssessmentServiceImpl();

            AllAssessmentSummaryDto ps = new AllAssessmentSummaryDto();

            switch (roleCheck[0].toString()) {
                case UserRoles.Admin:
                    if (formPayer > 0){
                        UserDto user = User.SingleUser(formPayer);

                        ps.level = "Tax Payer";
                        ps.title = user.firstName+ " "+ user.lastName ;
                        ps.levelData = user;

                        ps.count = Assess.CountAssessments(0, 0, 0, formPayer);
                        ps.volume = Assess.GetAssessmentSum(0, 0, 0, formPayer);
                        break;
                    }
                    if(formAgent > 0) {
                        UserDto us = User.SingleUser(formAgent);

                        ps.level = "agent";
                        ps.title = us.firstName + " " + us.lastName;
                        ps.levelData = us;

                        ps.count = Assess.CountAssessments(0, 0, formAgent, 0);
                        ps.volume = Assess.GetAssessmentSum(0, 0, formAgent, 0);
                        break;
                    }
                    if(formMda > 0){

                        ps.level = "mda";
                        ps.title = Org.SingleMda(formMda).name;
                        ps.levelData = Org.SingleMda(formMda);

                        ps.count = Assess.CountAssessments(0, formMda, 0, 0);
                        ps.volume = Assess.GetAssessmentSum(0, formMda, 0, 0);
                        break;

                    }
                    if(businessId > 0){

                        ps.level = "state_project";
                        ps.title = Org.SingleBusiness(businessId, true).clientName;
                        ps.levelData = Org.SingleBusiness(businessId, true);

                        ps.count = Assess.CountAssessments(businessId, 0, 0, 0);
                        ps.volume = Assess.GetAssessmentSum(businessId, 0, 0, 0);
                        break;

                    }

                    ps.level = "admin";
                    ps.title = " ";

                    ps.count = Assess.CountAssessments(0, 0, 0, 0);
                    ps.volume = Assess.GetAssessmentSum(0, 0, 0, 0);
                    break;
                case UserRoles.SubAdmin1:
                case UserRoles.ProjectReport:
                    if (formPayer > 0){
                        UserDto user = User.SingleUser(formPayer);

                        ps.level = "Tax Payer";
                        ps.title = user.firstName+ " "+ user.lastName ;
                        ps.levelData = user;

                        ps.count = Assess.CountAssessments(0, 0, 0, formPayer);
                        ps.volume = Assess.GetAssessmentSum(0, 0, 0, formPayer);
                        break;
                    }
                    int ProjId = Integer.parseInt(userMda[2]);
                    if(formAgent > 0) {
                        UserDto us = User.SingleUser(formAgent);

                        ps.level = "agent";
                        ps.title = us.firstName + " " + us.lastName;
                        ps.levelData = us;

                        ps.count = Assess.CountAssessments(0, 0, formAgent, 0);
                        ps.volume = Assess.GetAssessmentSum(0, 0, formAgent, 0);
                        break;
                    }
                    if(formMda > 0){

                        ps.level = "mda";
                        ps.title = Org.SingleMda(formMda).name;
                        ps.levelData = Org.SingleMda(formMda);

                        ps.count = Assess.CountAssessments(0, formMda, 0, 0);
                        ps.volume = Assess.GetAssessmentSum(0, formMda, 0, 0);
                        break;

                    }

                    ps.level = "state_project";
                    ps.title = Org.SingleBusiness(ProjId, true).clientName;
                    ps.levelData = Org.SingleBusiness(ProjId, true);

                    ps.count = Assess.CountAssessments(ProjId, 0, 0, 0);
                    ps.volume = Assess.GetAssessmentSum(ProjId, 0, 0, 0);
                    break;

                case UserRoles.SubAdmin2:
                    if (formPayer > 0){
                        UserDto user = User.SingleUser(formPayer);

                        ps.level = "Tax Payer";
                        ps.title = user.firstName+ " "+ user.lastName ;
                        ps.levelData = user;

                        ps.count = Assess.CountAssessments(0, 0, 0, formPayer);
                        ps.volume = Assess.GetAssessmentSum(0, 0, 0, formPayer);
                        break;
                    }
                    if(formAgent > 0) {
                        UserDto us = User.SingleUser(formAgent);

                        ps.level = "agent";
                        ps.title = us.firstName + " " + us.lastName;
                        ps.levelData = us;

                        ps.count = Assess.CountAssessments(0, 0, formAgent, 0);
                        ps.volume = Assess.GetAssessmentSum(0, 0, formAgent, 0);
                        break;
                    }
//                    int ProjId = Integer.parseInt(userMda[2]);
                    int MdaId = Integer.parseInt(userMda[1]);
                    int MdaProjId = Integer.parseInt(userMda[2]);

                    ps.level = "mda";
                    ps.title = Org.SingleMda(MdaId).name;
                    ps.levelData = Org.SingleMda(MdaId);

                    ps.count = Assess.CountAssessments(0, MdaId, 0, 0);
                    ps.volume = Assess.GetAssessmentSum(0, MdaId, 0, 0);
                    break;

                case UserRoles.Agent:
                    if (formPayer > 0){
                        UserDto user = User.SingleUser(formPayer);

                        ps.level = "Tax Payer";
                        ps.title = user.firstName+ " "+ user.lastName ;
                        ps.levelData = user;

                        ps.count = Assess.CountAssessments(0, 0, 0, formPayer);
                        ps.volume = Assess.GetAssessmentSum(0, 0, 0, formPayer);
                        break;
                    }
                    int AgentId = Integer.parseInt(userMda[0]);
//                    int MdaId = Integer.parseInt(userMda[1]);
//                    int MdaProjId = Integer.parseInt(userMda[2]);
                    UserDto us = User.SingleUser(AgentId);

                    ps.level = "agent";
                    ps.title = us.firstName+ " "+ us.lastName ;
                    ps.levelData = us;

                    ps.count = Assess.CountAssessments(0, 0, AgentId, 0);
                    ps.volume = Assess.GetAssessmentSum(0, 0, AgentId, 0);
                    break;

                case UserRoles.Individual:
                case UserRoles.NonIndividual:
                    int UserId = Integer.parseInt(userMda[0]);
//                    int MdaId = Integer.parseInt(userMda[1]);
//                    int MdaProjId = Integer.parseInt(userMda[2]);
                    UserDto user = User.SingleUser(UserId);

                    ps.level = "Tax Payer";
                    ps.title = user.firstName+ " "+ user.lastName ;
                    ps.levelData = user;

                    ps.count = Assess.CountAssessments(0, 0, 0, UserId);
                    ps.volume = Assess.GetAssessmentSum(0, 0, 0, UserId);
                    break;
            }
            success.setStatus(200);
            success.setData(ps);

            return Response.ok(ps).build();
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
    @Path("/objections-summary")
    @ApiOperation(value = "Admin", notes = "", authorizations = {
            @Authorization(value = "authorization")}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    message = "Objections; Admins & Agent",
                    code = 200,
                    response = AllObjectionSummaryDto.class,
                    responseContainer = "")
    })
    @RolesAllowed({UserRoles.Admin, UserRoles.SubAdmin1, UserRoles.ProjectReport, UserRoles.SubAdmin2, UserRoles.Agent,
            UserRoles.Individual, UserRoles.NonIndividual})
    public Response AllObjsSummary(@QueryParam("business_id")int businessId, @QueryParam("mda_id")int formMda,
                                   @QueryParam("agent_id")int formAgent, @QueryParam("payer_id")int formPayer){
        SuccessResponse success = new SuccessResponse();
        try {
            String[] userMda = ExtractTokenUserMdaId(jwt);
            Object[] roleCheck = jwt.getGroups().toArray();

            Org = new OrganisationServiceImpl();
            User = new UserServiceImpl();
            Assess = new AssessmentServiceImpl();

            AllObjectionSummaryDto ps = new AllObjectionSummaryDto();

            switch (roleCheck[0].toString()) {
                case UserRoles.Admin:
                    if(formPayer > 0) {
                        UserDto user = User.SingleUser(formPayer);

                        ps.level = "Tax Payer";
                        ps.title = user.firstName + " " + user.lastName;
                        ps.levelData = user;

                        ps.approved = Assess.CountAssessmentObjsAlt(0, 0, 0, formPayer, "approved");
                        ps.rejected = Assess.CountAssessmentObjsAlt(0, 0, 0, formPayer, "reject");
                        ps.pending = Assess.CountAssessmentObjsAlt(0, 0, 0, formPayer, "raised");
                        ps.total = ps.approved + ps.pending + ps.rejected;
                        break;
                    }
                    if(formAgent > 0) {
                        UserDto us = User.SingleUser(formAgent);

                        ps.level = "agent";
                        ps.title = us.firstName + " " + us.lastName;
                        ps.levelData = us;

                        ps.approved = Assess.CountAssessmentObjsAlt(0, 0, formAgent, 0, "approved");
                        ps.rejected = Assess.CountAssessmentObjsAlt(0, 0, formAgent, 0, "reject");
                        ps.pending = Assess.CountAssessmentObjsAlt(0, 0, formAgent, 0, "raised");
                        ps.total = ps.approved + ps.pending + ps.rejected;
                        break;
                    }
                    if(formMda > 0){
                        ps.level = "mda";
                        ps.title = Org.SingleMda(formMda).name;
                        ps.levelData = Org.SingleMda(formMda);
                        ps.approved = Assess.CountAssessmentObjsAlt(0, formMda, 0, 0, "approved");
                        ps.rejected = Assess.CountAssessmentObjsAlt(0, formMda, 0, 0, "reject");
                        ps.pending = Assess.CountAssessmentObjsAlt(0, formMda, 0, 0, "raised");
                        ps.total =  ps.approved + ps.pending + ps.rejected;
                        break;
                    }
                    if(businessId > 0){

                        ps.level = "state_project";
                        ps.title = Org.SingleBusiness(businessId, true).clientName;
                        ps.levelData = Org.SingleBusiness(businessId, true);
                        ps.approved = Assess.CountAssessmentObjsAlt(businessId, 0, 0, 0, "approved");
                        ps.rejected = Assess.CountAssessmentObjsAlt(businessId, 0, 0, 0, "reject");
                        ps.pending = Assess.CountAssessmentObjsAlt(businessId, 0, 0, 0, "raised");
                        ps.total =  ps.approved + ps.pending + ps.rejected;
                        break;

                    }

                    ps.level = "admin";
                    ps.title = " ";
                    ps.approved = Assess.CountAssessmentObjsAlt(0, 0, 0, 0, "approved");
                    ps.rejected = Assess.CountAssessmentObjsAlt(0, 0, 0, 0, "reject");
                    ps.pending = Assess.CountAssessmentObjsAlt(0, 0, 0, 0, "raised");
                    ps.total =  ps.approved + ps.pending + ps.rejected;
                    break;
                case UserRoles.SubAdmin1:
                case UserRoles.ProjectReport:
                    if(formPayer > 0) {
                        UserDto user = User.SingleUser(formPayer);

                        ps.level = "Tax Payer";
                        ps.title = user.firstName + " " + user.lastName;
                        ps.levelData = user;

                        ps.approved = Assess.CountAssessmentObjsAlt(0, 0, 0, formPayer, "approved");
                        ps.rejected = Assess.CountAssessmentObjsAlt(0, 0, 0, formPayer, "reject");
                        ps.pending = Assess.CountAssessmentObjsAlt(0, 0, 0, formPayer, "raised");
                        ps.total = ps.approved + ps.pending + ps.rejected;
                        break;
                    }
                    if(formAgent > 0) {
                        UserDto us = User.SingleUser(formAgent);

                        ps.level = "agent";
                        ps.title = us.firstName + " " + us.lastName;
                        ps.levelData = us;

                        ps.approved = Assess.CountAssessmentObjsAlt(0, 0, formAgent, 0, "approved");
                        ps.rejected = Assess.CountAssessmentObjsAlt(0, 0, formAgent, 0, "reject");
                        ps.pending = Assess.CountAssessmentObjsAlt(0, 0, formAgent, 0, "raised");
                        ps.total = ps.approved + ps.pending + ps.rejected;
                        break;
                    }
                    int ProjId = Integer.parseInt(userMda[2]);

                    if(formMda > 0){
                        ps.level = "mda";
                        ps.title = Org.SingleMda(formMda).name;
                        ps.levelData = Org.SingleMda(formMda);
                        ps.approved = Assess.CountAssessmentObjsAlt(0, formMda, 0, 0, "approved");
                        ps.rejected = Assess.CountAssessmentObjsAlt(0, formMda, 0, 0, "reject");
                        ps.pending = Assess.CountAssessmentObjsAlt(0, formMda, 0, 0, "raised");
                        ps.total =  ps.approved + ps.pending + ps.rejected;
                        break;
                    }

                    ps.level = "state_project";
                    ps.title = Org.SingleBusiness(ProjId, true).clientName;
                    ps.levelData = Org.SingleBusiness(ProjId, true);
                    ps.approved = Assess.CountAssessmentObjsAlt(ProjId, 0, 0, 0, "approved");
                    ps.rejected = Assess.CountAssessmentObjsAlt(ProjId, 0, 0, 0, "reject");
                    ps.pending = Assess.CountAssessmentObjsAlt(ProjId, 0, 0, 0, "raised");
                    ps.total =  ps.approved + ps.pending + ps.rejected;

                    break;
                case UserRoles.SubAdmin2:
                    if(formPayer > 0) {
                        UserDto user = User.SingleUser(formPayer);

                        ps.level = "Tax Payer";
                        ps.title = user.firstName + " " + user.lastName;
                        ps.levelData = user;

                        ps.approved = Assess.CountAssessmentObjsAlt(0, 0, 0, formPayer, "approved");
                        ps.rejected = Assess.CountAssessmentObjsAlt(0, 0, 0, formPayer, "reject");
                        ps.pending = Assess.CountAssessmentObjsAlt(0, 0, 0, formPayer, "raised");
                        ps.total = ps.approved + ps.pending + ps.rejected;
                        break;
                    }
                    if(formAgent > 0) {
                        UserDto us = User.SingleUser(formAgent);

                        ps.level = "agent";
                        ps.title = us.firstName + " " + us.lastName;
                        ps.levelData = us;

                        ps.approved = Assess.CountAssessmentObjsAlt(0, 0, formAgent, 0, "approved");
                        ps.rejected = Assess.CountAssessmentObjsAlt(0, 0, formAgent, 0, "reject");
                        ps.pending = Assess.CountAssessmentObjsAlt(0, 0, formAgent, 0, "raised");
                        ps.total = ps.approved + ps.pending + ps.rejected;
                        break;
                    }
                    int MdaId = Integer.parseInt(userMda[1]);
                    int MdaProjId = Integer.parseInt(userMda[2]);

                    ps.level = "mda";
                    ps.title = Org.SingleMda(MdaId).name;
                    ps.levelData = Org.SingleMda(MdaId);
                    ps.approved = Assess.CountAssessmentObjsAlt(0, MdaId, 0, 0, "approved");
                    ps.rejected = Assess.CountAssessmentObjsAlt(0, MdaId, 0, 0, "reject");
                    ps.pending = Assess.CountAssessmentObjsAlt(0, MdaId, 0, 0, "raised");
                    ps.total =  ps.approved + ps.pending + ps.rejected;
                    break;

                case UserRoles.Agent:
                    if(formPayer > 0) {
                        UserDto user = User.SingleUser(formPayer);

                        ps.level = "Tax Payer";
                        ps.title = user.firstName + " " + user.lastName;
                        ps.levelData = user;

                        ps.approved = Assess.CountAssessmentObjsAlt(0, 0, 0, formPayer, "approved");
                        ps.rejected = Assess.CountAssessmentObjsAlt(0, 0, 0, formPayer, "reject");
                        ps.pending = Assess.CountAssessmentObjsAlt(0, 0, 0, formPayer, "raised");
                        ps.total = ps.approved + ps.pending + ps.rejected;
                        break;
                    }
                    int AgentId = Integer.parseInt(userMda[0]);
//                    int MdaId = Integer.parseInt(userMda[1]);
//                    int MdaProjId = Integer.parseInt(userMda[2]);
                    UserDto us = User.SingleUser(AgentId);

                    ps.level = "agent";
                    ps.title = us.firstName+ " "+ us.lastName ;
                    ps.levelData = us;

                    ps.approved = Assess.CountAssessmentObjsAlt(0, 0, AgentId, 0, "approved");
                    ps.rejected = Assess.CountAssessmentObjsAlt(0, 0, AgentId, 0, "reject");
                    ps.pending = Assess.CountAssessmentObjsAlt(0, 0, AgentId, 0, "raised");
                    ps.total =  ps.approved + ps.pending + ps.rejected;
                    break;

                case UserRoles.Individual:
                case UserRoles.NonIndividual:
                    int UserId = Integer.parseInt(userMda[0]);
//                    int MdaId = Integer.parseInt(userMda[1]);
//                    int MdaProjId = Integer.parseInt(userMda[2]);
                    UserDto user = User.SingleUser(UserId);

                    ps.level = "Tax Payer";
                    ps.title = user.firstName+ " "+ user.lastName ;
                    ps.levelData = user;

                    ps.approved = Assess.CountAssessmentObjsAlt(0, 0, 0, UserId, "approved");
                    ps.rejected = Assess.CountAssessmentObjsAlt(0, 0, 0, UserId, "reject");
                    ps.pending = Assess.CountAssessmentObjsAlt(0, 0, 0, UserId, "raised");
                    ps.total =  ps.approved + ps.pending + ps.rejected;
                    break;
            }
            success.setStatus(200);
            success.setData(ps);

            return Response.ok(ps).build();
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
    @Path("/remittance-summary")
    @ApiOperation(value = "Admin", notes = "", authorizations = {
            @Authorization(value = "authorization")}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    message = "Remittance; Admins & Agent",
                    code = 200,
                    response = AllRemittanceSummaryDto.class,
                    responseContainer = "")
    })
    @RolesAllowed({UserRoles.Admin, UserRoles.SubAdmin1, UserRoles.ProjectReport, UserRoles.SubAdmin2, UserRoles.Agent})
    public Response AllRemSummary(@QueryParam("business_id")int businessId, @QueryParam("mda_id")int formMda, @QueryParam("agent_id")int formAgent, @QueryParam("last_month")boolean isLastMonth){
        SuccessResponse success = new SuccessResponse();
        try {
            String[] userMda = ExtractTokenUserMdaId(jwt);
            Object[] roleCheck = jwt.getGroups().toArray();

            Org = new OrganisationServiceImpl();
            User = new UserServiceImpl();
//            Assess = new AssessmentServiceImpl();
            PosTrans = new PosTransactionServiceImpl();

            AllRemittanceSummaryDto ps = new AllRemittanceSummaryDto();


            DateTime today = new DateTime();
            if (isLastMonth) today = today.minusMonths(1);
            today = today.minusMonths(2);

            switch (roleCheck[0].toString()) {
                case UserRoles.Admin:
                    if(formAgent > 0) {
                        UserDto us = User.SingleUser(formAgent);

                        ps.level = "agent";
                        ps.title = us.firstName + " " + us.lastName;
                        ps.levelData = us;

                        ps.count = PosTrans.CountRemittances(0, 0, formAgent, false, today);
                        ps.volume = PosTrans.SumRemittances(0, 0, formAgent, false, today);
                        ps.remitCount = PosTrans.CountRemittances(0, 0, formAgent, true, today);
                        ps.remitVolume = PosTrans.SumRemittances(0, 0, formAgent, true, today);
                        break;
                    }
                    if(formMda > 0){
                        ps.level = "mda";
                        ps.title = Org.SingleMda(formMda).name;
                        ps.levelData = Org.SingleMda(formMda);

                        ps.count = PosTrans.CountRemittances(0, formMda, 0, false, today);
                        ps.volume = PosTrans.SumRemittances(0, formMda, 0, false, today);
                        ps.remitCount = PosTrans.CountRemittances(0, formMda, 0, true, today);
                        ps.remitVolume = PosTrans.SumRemittances(0, formMda, 0, true, today);
                        break;
                    }
                    if(businessId > 0){

                        ps.level = "state_project";
                        ps.title = Org.SingleBusiness(businessId, true).clientName;
                        ps.levelData = Org.SingleBusiness(businessId, true);

                        ps.count = PosTrans.CountRemittances(businessId, 0, 0, false, today);
                        ps.volume = PosTrans.SumRemittances(businessId, 0, 0, false, today);
                        ps.remitCount = PosTrans.CountRemittances(businessId, 0, 0, true, today);
                        ps.remitVolume = PosTrans.SumRemittances(businessId, 0, 0, true, today);

                        break;

                    }

                    ps.level = "admin";
                    ps.title = " ";

                    ps.count = PosTrans.CountRemittances(0, 0, 0, false, today);
                    ps.volume = PosTrans.SumRemittances(0, 0, 0, false, today);
                    ps.remitCount = PosTrans.CountRemittances(0, 0, 0, true, today);
                    ps.remitVolume = PosTrans.SumRemittances(0, 0, 0, true, today);
                    break;
                case UserRoles.SubAdmin1:
                case UserRoles.ProjectReport:
                    if(formAgent > 0) {
                        UserDto us = User.SingleUser(formAgent);

                        ps.level = "agent";
                        ps.title = us.firstName + " " + us.lastName;
                        ps.levelData = us;

                        ps.count = PosTrans.CountRemittances(0, 0, formAgent, false, today);
                        ps.volume = PosTrans.SumRemittances(0, 0, formAgent, false, today);
                        ps.remitCount = PosTrans.CountRemittances(0, 0, formAgent, true, today);
                        ps.remitVolume = PosTrans.SumRemittances(0, 0, formAgent, true, today);
                        break;
                    }
                    int ProjId = Integer.parseInt(userMda[2]);

                    if(formMda > 0){
                        ps.level = "mda";
                        ps.title = Org.SingleMda(formMda).name;
                        ps.levelData = Org.SingleMda(formMda);

                        ps.count = PosTrans.CountRemittances(0, formMda, 0, false, today);
                        ps.volume = PosTrans.SumRemittances(0, formMda, 0, false, today);
                        ps.remitCount = PosTrans.CountRemittances(0, formMda, 0, true, today);
                        ps.remitVolume = PosTrans.SumRemittances(0, formMda, 0, true, today);
                        break;
                    }

                    ps.level = "state_project";
                    ps.title = Org.SingleBusiness(ProjId, true).clientName;
                    ps.levelData = Org.SingleBusiness(ProjId, true);

                    ps.count = PosTrans.CountRemittances(ProjId, 0, 0, false, today);
                    ps.volume = PosTrans.SumRemittances(ProjId, 0, 0, false, today);
                    ps.remitCount = PosTrans.CountRemittances(ProjId, 0, 0, true, today);
                    ps.remitVolume = PosTrans.SumRemittances(ProjId, 0, 0, true, today);

                    break;
                case UserRoles.SubAdmin2:
                    if(formAgent > 0) {
                        UserDto us = User.SingleUser(formAgent);

                        ps.level = "agent";
                        ps.title = us.firstName + " " + us.lastName;
                        ps.levelData = us;

                        ps.count = PosTrans.CountRemittances(0, 0, formAgent, false, today);
                        ps.volume = PosTrans.SumRemittances(0, 0, formAgent, false, today);
                        ps.remitCount = PosTrans.CountRemittances(0, 0, formAgent, true, today);
                        ps.remitVolume = PosTrans.SumRemittances(0, 0, formAgent, true, today);
                        break;
                    }
                    int MdaId = Integer.parseInt(userMda[1]);
                    int MdaProjId = Integer.parseInt(userMda[2]);

                    ps.level = "mda";
                    ps.title = Org.SingleMda(MdaId).name;
                    ps.levelData = Org.SingleMda(MdaId);

                    ps.count = PosTrans.CountRemittances(0, MdaId, 0, false, today);
                    ps.volume = PosTrans.SumRemittances(0, MdaId, 0, false, today);
                    ps.remitCount = PosTrans.CountRemittances(0, MdaId, 0, true, today);
                    ps.remitVolume = PosTrans.SumRemittances(0, MdaId, 0, true, today);
                    break;

                case UserRoles.Agent:
                    int AgentId = Integer.parseInt(userMda[0]);
//                    int MdaId = Integer.parseInt(userMda[1]);
//                    int MdaProjId = Integer.parseInt(userMda[2]);
                    UserDto us = User.SingleUser(AgentId);

                    ps.level = "agent";
                    ps.title = us.firstName+ " "+ us.lastName ;
                    ps.levelData = us;

                    ps.count = PosTrans.CountRemittances(0, 0, AgentId, false, today);
                    ps.volume = PosTrans.SumRemittances(0, 0, AgentId, false, today);
                    ps.remitCount = PosTrans.CountRemittances(0, 0, AgentId, true, today);
                    ps.remitVolume = PosTrans.SumRemittances(0, 0, AgentId, true, today);
                    break;
            }
            success.setStatus(200);
            success.setData(ps);

            return Response.ok(ps).build();
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
    @Path("/eight-days-summary")
    @ApiResponses(value = {
            @ApiResponse(
                    message = "Daily Transactions Summary ",
                    code = 200,
                    response = DailyTransactionsDto.class,
                    responseContainer = "List")
    })
    @ApiOperation(value = "Monthly Summary for Admin, Sub-Admin1/Project-Report, Sub-Admin2. Drill down to project for Admin; MDA for SubAdmin1/ProjectReport, ", notes = "", authorizations = {
            @Authorization(value = "authorization")})
    public Response DaysListSummary(@QueryParam("Project_Id")Integer formProjectId, @QueryParam("Mda_Id")Integer formMdaId, @QueryParam("Previous_Week")boolean lastWeek){
        SuccessResponse success = new SuccessResponse();
        try {
            String[] userMda = ExtractTokenUserMdaId(jwt);
            Trans = new TransactionServiceImpl();
            List<DailyTransactionsDto> summary = new LinkedList<DailyTransactionsDto>();
            Object[] roleCheck = jwt.getGroups().toArray();

            DateTime today = new DateTime();
            if (lastWeek) today = today.minusDays(7);

            switch (roleCheck[0].toString()){
                case UserRoles.Admin:
                    if (formProjectId != null && formProjectId > 0) {
                        if(formMdaId != null && formMdaId > 0 ){
                            summary = Trans.DailyTransactionsSummaryList(today, UserRoles.SubAdmin2, formMdaId);

                        } else {
                            summary = Trans.DailyTransactionsSummaryList(today, UserRoles.SubAdmin1, formProjectId);

                        }

                    } else {
                        summary = Trans.DailyTransactionsSummaryList(today, "", 0);
                    }
                    break;
                case UserRoles.SubAdmin1:
                case UserRoles.ProjectReport:
                    if (formMdaId != null && formMdaId > 0) {
//                        int IdProjId = Integer.parseInt(userMda[2]);
                        summary = Trans.DailyTransactionsSummaryList(today, roleCheck[0].toString(), formMdaId);
                    } else {
                        int ProjId = Integer.parseInt(userMda[2]);
                        summary = Trans.DailyTransactionsSummaryList(today, roleCheck[0].toString(), ProjId);
                    }
                    break;
                case UserRoles.SubAdmin2:
                case UserRoles.Agent:
                        int MdaId = Integer.parseInt(userMda[1]);
//                        int MdaProjId = Integer.parseInt(userMda[2]);
                    summary = Trans.DailyTransactionsSummaryList(today, roleCheck[0].toString(), MdaId);
                    break;
//                case UserRoles.Agent:
//                    int AgentId = Integer.parseInt(userMda[0]);
//                    int AgentMdajId = Integer.parseInt(userMda[1]);
//                    int AgentProjId = Integer.parseInt(userMda[2]);
//                    summary.ytdAmount = PosTrans.AllAgentTransactionAmount(AgentId, AgentMdajId);
//                    summary.ytdCount = PosTrans.AllAgentTransactionCount(AgentId,AgentMdajId);
//                    summary.lastMonthAmount = PosTrans.MonthAgentTransactionAmount(AgentId, AgentMdajId, today.minusMonths(1));
//                    summary.lastMonthCount = PosTrans.MonthAgentTransactionCount(AgentId, AgentMdajId, today.minusMonths(1));
//                    summary.thisMonthAmount = PosTrans.MonthAgentTransactionAmount(AgentId, AgentMdajId, today);
//                    summary.thisMonthCount = PosTrans.MonthAgentTransactionCount(AgentId, AgentMdajId, today);
//                    summary.todayAmount = PosTrans.DayAgentTransactionAmount(AgentId, AgentMdajId, today);
//                    summary.todayCount = PosTrans.DayAgentTransactionCount(AgentId, AgentMdajId, today);
//                    summary.yesterdayAmount = PosTrans.DayAgentTransactionAmount(AgentId, AgentMdajId, today.minusDays(1));
//                    summary.yesterdayCount = PosTrans.DayAgentTransactionCount(AgentId, AgentMdajId, today.minusDays(1));
//                    break;
//                case UserRoles.Individual:
//                case UserRoles.NonIndividual:
//                    int UserId = Integer.parseInt(userMda[0]);
//                    summary.ytdAmount = Trans.AllUserTransactionAmount(UserId);
//                    summary.ytdCount = Trans.AllUserTransactionCount(UserId);
//                    summary.lastMonthAmount = 0;
//                    summary.lastMonthCount = 0;
//                    summary.thisMonthAmount = 0;
//                    summary.thisMonthCount = 0;
//                    summary.todayAmount = 0;
//                    summary.todayCount = 0;
//                    summary.yesterdayAmount = 0;
//                    summary.yesterdayCount = 0;
//                    break;
//                default:
////                    if(roleCheck[0].toString().equals(UserRoles.Admin) && projectId > 0) {
//                    if(roleCheck[0].toString().equals(UserRoles.Admin)) {
//                    }
//                    if((roleCheck[0].toString().equals(UserRoles.SubAdmin1) || roleCheck[0].toString().equals(UserRoles.ProjectReport))){
////                        int MdaId = Integer.parseInt(userMda[1]);
//                    }
//                    if(roleCheck[0].toString().equals(UserRoles.SubAdmin2)){
////                        int AgentId = Integer.parseInt(userMda[0]);
//                    }
            }


            success.setStatus(200);
            success.setData(summary);
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
    @Path("/pos-list-summary")
    @ApiOperation(value = "Admin", notes = "", authorizations = {
            @Authorization(value = "authorization")}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    message = "POS List Summary",
                    code = 200,
                    response = AllRemittanceSummaryDto.class,
                    responseContainer = "")
    })
    @RolesAllowed({UserRoles.Admin, UserRoles.SubAdmin1, UserRoles.ProjectReport, UserRoles.SubAdmin2})
    public Response POSListSummary(@QueryParam("business_id")int businessId, @QueryParam("mda_id")int formMda){
        SuccessResponse success = new SuccessResponse();
        try {
            String[] userMda = ExtractTokenUserMdaId(jwt);
            Object[] roleCheck = jwt.getGroups().toArray();


            Pos = new PosServiceImpl();

            List<PosSummaryListDto> ps = new LinkedList<>();


            DateTime today = new DateTime();
//            if (isLastMonth) today = today.minusMonths(1);

            switch (roleCheck[0].toString()) {
                case UserRoles.Admin:
//                    if(formOffice > 0){
//                        ps = Pos.ListPosSummary(0, 0, formOffice);
//                        break;
//                    }
                    if(formMda > 0){
                        ps = Pos.ListPosSummary(0, formMda, 0);
                        break;
                    }
                    if(businessId > 0){
                        ps = Pos.ListPosSummary(businessId, 0, 0);

                        break;

                    }

                    ps = Pos.ListPosSummary(0, 0, 0);
                    break;
                case UserRoles.SubAdmin1:
                case UserRoles.ProjectReport:
                    int ProjId = Integer.parseInt(userMda[2]);
//                    if(formOffice > 0){
//                        ps = Pos.ListPosSummary(0, 0, formOffice);
//                        break;
//                    }
                    if(formMda > 0){
                        ps = Pos.ListPosSummary(0, formMda, 0);
                        break;
                    }

                    ps = Pos.ListPosSummary(ProjId, 0, 0);

                    break;
                case UserRoles.SubAdmin2:
                    int MdaId = Integer.parseInt(userMda[1]);
                    int MdaProjId = Integer.parseInt(userMda[2]);
//                    if(formOffice > 0){
//                        ps = Pos.ListPosSummary(0, 0, formOffice);
//                        break;
//                    }

                    ps = Pos.ListPosSummary(0, MdaId, 0);
                    break;
//                case UserRoles.SubAdmin3:
//                    int OfficeId = Integer.parseInt(userMda[3]);
//
//                    ps = Pos.ListPosSummary(0, 0, OfficeId);
//                    break;
            }
            success.setStatus(200);
            success.setData(ps);

            return Response.ok(ps).build();
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
    @Path("/assessment-list-summary")
    @ApiOperation(value = "Admin", notes = "", authorizations = {
            @Authorization(value = "authorization")}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    message = "Assessment List Summary",
                    code = 200,
                    response = AllRemittanceSummaryDto.class,
                    responseContainer = "")
    })
    @RolesAllowed({UserRoles.Admin, UserRoles.SubAdmin1, UserRoles.ProjectReport, UserRoles.SubAdmin2})
    public Response AssessListSummary(@QueryParam("business_id")int businessId, @QueryParam("mda_id")int formMda, @QueryParam("office_id")int formOffice,
                                      @QueryParam("start_date")String formStartDate, @QueryParam("end_date")String formEndDate){
        SuccessResponse success = new SuccessResponse();
        try {
            String[] userMda = ExtractTokenUserMdaId(jwt);
            Object[] roleCheck = jwt.getGroups().toArray();

                        Assess = new AssessmentServiceImpl();

            List<AssessmentSummaryListDto> ps = new LinkedList<>();


//            DateTime today = new DateTime();
//            if (isLastMonth) today = today.minusMonths(1);



            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

            DateTime today = new DateTime();
            Date endDate = formStartDate == null ? today.toDate() : formatter.parse(formEndDate);
            Date startDate = formEndDate == null ? today.minusMonths(3).toDate() : formatter.parse(formStartDate);

            switch (roleCheck[0].toString()) {
                case UserRoles.Admin:
                    if(formOffice > 0){
                        ps = Assess.ListAssessmentSummary(0, 0, formOffice, startDate, endDate);
                        break;
                    }
                    if(formMda > 0){
                        ps = Assess.ListAssessmentSummary(0, formMda, 0, startDate, endDate);
                        break;
                    }
                    if(businessId > 0){
                        ps = Assess.ListAssessmentSummary(businessId, 0, 0, startDate, endDate);

                        break;

                    }

                    ps = Assess.ListAssessmentSummary(0, 0, 0, startDate, endDate);
                    break;
                case UserRoles.SubAdmin1:
                case UserRoles.ProjectReport:
                    int ProjId = Integer.parseInt(userMda[2]);
                    if(formOffice > 0){
                        ps = Assess.ListAssessmentSummary(0, 0, formOffice, startDate, endDate);
                        break;
                    }
                    if(formMda > 0){
                        ps = Assess.ListAssessmentSummary(0, formMda, 0, startDate, endDate);
                        break;
                    }

                    ps = Assess.ListAssessmentSummary(ProjId, 0, 0, startDate, endDate);

                    break;
                case UserRoles.SubAdmin2:
                    int MdaId = Integer.parseInt(userMda[1]);
                    int MdaProjId = Integer.parseInt(userMda[2]);
                    if(formOffice > 0){
                        ps = Assess.ListAssessmentSummary(0, 0, formOffice, startDate, endDate);
                        break;
                    }

                    ps = Assess.ListAssessmentSummary(0, MdaId, 0, startDate, endDate);
                    break;
                case UserRoles.SubAdmin3:
                    int OfficeId = Integer.parseInt(userMda[3]);

                    ps = Assess.ListAssessmentSummary(0, 0, OfficeId, startDate, endDate);
                    break;
            }
            success.setStatus(200);
            success.setData(ps);

            return Response.ok(ps).build();
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
    @Path("/objection-list-summary")
    @ApiOperation(value = "Admin", notes = "", authorizations = {
            @Authorization(value = "authorization")}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    message = "Assessment List Summary",
                    code = 200,
                    response = AllRemittanceSummaryDto.class,
                    responseContainer = "")
    })
    @RolesAllowed({UserRoles.Admin, UserRoles.SubAdmin1, UserRoles.ProjectReport, UserRoles.SubAdmin2, UserRoles.SubAdmin3})
    public Response ObjListSummary(@QueryParam("business_id")int businessId, @QueryParam("mda_id")int formMda, @QueryParam("office_id")int formOffice,
                                      @QueryParam("start_date")String formStartDate, @QueryParam("end_date")String formEndDate){
        SuccessResponse success = new SuccessResponse();
        try {
            String[] userMda = ExtractTokenUserMdaId(jwt);
            Object[] roleCheck = jwt.getGroups().toArray();

            Assess = new AssessmentServiceImpl();

            List<ObjSummaryListDto> ps = new LinkedList<>();


//            DateTime today = new DateTime();
//            if (isLastMonth) today = today.minusMonths(1);



            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

            DateTime today = new DateTime();
            Date endDate = formStartDate == null ? today.toDate() : formatter.parse(formEndDate);
            Date startDate = formEndDate == null ? today.minusMonths(3).toDate() : formatter.parse(formStartDate);

            switch (roleCheck[0].toString()) {
                case UserRoles.Admin:
                    if(formOffice > 0){
                        ps = Assess.ListObjSummary(0, 0, formOffice, startDate, endDate);
                        break;
                    }
                    if(formMda > 0){
                        ps = Assess.ListObjSummary(0, formMda, 0, startDate, endDate);
                        break;
                    }
                    if(businessId > 0){
                        ps = Assess.ListObjSummary(businessId, 0, 0, startDate, endDate);

                        break;

                    }

                    ps = Assess.ListObjSummary(0, 0, 0, startDate, endDate);
                    break;
                case UserRoles.SubAdmin1:
                case UserRoles.ProjectReport:
                    int ProjId = Integer.parseInt(userMda[2]);
                    if(formOffice > 0){
                        ps = Assess.ListObjSummary(0, 0, formOffice, startDate, endDate);
                        break;
                    }
                    if(formMda > 0){
                        ps = Assess.ListObjSummary(0, formMda, 0, startDate, endDate);
                        break;
                    }

                    ps = Assess.ListObjSummary(ProjId, 0, 0, startDate, endDate);

                    break;
                case UserRoles.SubAdmin2:
                    int MdaId = Integer.parseInt(userMda[1]);
                    int MdaProjId = Integer.parseInt(userMda[2]);
                    if(formOffice > 0){
                        ps = Assess.ListObjSummary(0, 0, formOffice, startDate, endDate);
                        break;
                    }

                    ps = Assess.ListObjSummary(0, MdaId, 0, startDate, endDate);
                    break;
                case UserRoles.SubAdmin3:
                    int OfficeId = Integer.parseInt(userMda[3]);

                    ps = Assess.ListObjSummary(0, 0, OfficeId, startDate, endDate);
                    break;
            }
            success.setStatus(200);
            success.setData(ps);

            return Response.ok(ps).build();
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
    @Path("/remittance-list-summary")
    @ApiOperation(value = "Admin", notes = "", authorizations = {
            @Authorization(value = "authorization")}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    message = "Assessment List Summary",
                    code = 200,
                    response = AllRemittanceSummaryDto.class,
                    responseContainer = "")
    })
    @RolesAllowed({UserRoles.Admin, UserRoles.SubAdmin1, UserRoles.ProjectReport, UserRoles.SubAdmin2, UserRoles.SubAdmin3})
    public Response RemListSummary(@QueryParam("business_id")int businessId, @QueryParam("mda_id")int formMda, @QueryParam("office_id")int formOffice,
                                   @QueryParam("start_date")String formStartDate, @QueryParam("end_date")String formEndDate){
        SuccessResponse success = new SuccessResponse();
        try {
            String[] userMda = ExtractTokenUserMdaId(jwt);
            Object[] roleCheck = jwt.getGroups().toArray();

//            Assess = new AssessmentServiceImpl();
            PosTrans = new PosTransactionServiceImpl();

            List<RemitSummaryListDto> ps = new LinkedList<>();

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

            DateTime today = new DateTime();
            Date endDate = formStartDate == null ? today.toDate() : formatter.parse(formEndDate);
            Date startDate = formEndDate == null ? today.minusMonths(3).toDate() : formatter.parse(formStartDate);

            switch (roleCheck[0].toString()) {
                case UserRoles.Admin:
                    if(formOffice > 0){
                        ps = PosTrans.ListRemitSummary(0, 0, formOffice, startDate, endDate);
                        break;
                    }
                    if(formMda > 0){
                        ps = PosTrans.ListRemitSummary(0, formMda, 0, startDate, endDate);
                        break;
                    }
                    if(businessId > 0){
                        ps = PosTrans.ListRemitSummary(businessId, 0, 0, startDate, endDate);

                        break;

                    }

                    ps = PosTrans.ListRemitSummary(0, 0, 0, startDate, endDate);
                    break;
                case UserRoles.SubAdmin1:
                case UserRoles.ProjectReport:
                    int ProjId = Integer.parseInt(userMda[2]);
                    if(formOffice > 0){
                        ps = PosTrans.ListRemitSummary(0, 0, formOffice, startDate, endDate);
                        break;
                    }
                    if(formMda > 0){
                        ps = PosTrans.ListRemitSummary(0, formMda, 0, startDate, endDate);
                        break;
                    }

                    ps = PosTrans.ListRemitSummary(ProjId, 0, 0, startDate, endDate);

                    break;
                case UserRoles.SubAdmin2:
                    int MdaId = Integer.parseInt(userMda[1]);
                    int MdaProjId = Integer.parseInt(userMda[2]);
                    if(formOffice > 0){
                        ps = PosTrans.ListRemitSummary(0, 0, formOffice, startDate, endDate);
                        break;
                    }

                    ps = PosTrans.ListRemitSummary(0, MdaId, 0, startDate, endDate);
                    break;
                case UserRoles.SubAdmin3:
                    int OfficeId = Integer.parseInt(userMda[3]);

                    ps = PosTrans.ListRemitSummary(0, 0, OfficeId, startDate, endDate);
                    break;
            }
            success.setStatus(200);
            success.setData(ps);

            return Response.ok(ps).build();
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
//    @Path("/poses-summary")
//    @ApiOperation(value = "Admin", notes = "", authorizations = {
//            @Authorization(value = "authorization")}
//    )
//    @ApiResponses(value = {
//            @ApiResponse(
//                    message = "States",
//                    code = 200,
//                    response = BusinessDto.class,
//                    responseContainer = "")
//    })
//    @RolesAllowed({UserRoles.Admin, UserRoles.SubAdmin1})
    public Response xAllPosesSummary(@QueryParam("business_id")int businessId, @QueryParam("mda_id")int formMda){
        SuccessResponse success = new SuccessResponse();
        try {
            String[] userMda = ExtractTokenUserMdaId(jwt);
            Object[] roleCheck = jwt.getGroups().toArray();

            Org = new OrganisationServiceImpl();
            User = new UserServiceImpl();
            Pos = new PosServiceImpl();

            AllOfficersSummaryDto ps = new AllOfficersSummaryDto();

            switch (roleCheck[0].toString()) {
                case UserRoles.Admin:
                    if(formMda > 0){
                        ps.level = "mda";
                        ps.title = Org.SingleMda(formMda).name;
                        ps.active = Pos.CountPosesAll(0, formMda, true);
                        ps.inactive = Pos.CountPosesAll(0, formMda, false);
                        ps.all =  ps.active + ps.inactive;
                        break;
                    }
                    if(businessId > 0){

                        ps.level = "state_project";
                        ps.title = Org.SingleBusiness(businessId, true).clientName;
                        ps.active = Pos.CountPosesAll(businessId, 0, true);
                        ps.inactive = Pos.CountPosesAll(businessId, 0, false);
                        ps.all =  ps.active + ps.inactive;

                    }

                    ps.level = "admin";
                    ps.title = " ";
                    ps.active = Pos.CountPosesAll(0, 0, true);
                    ps.inactive = Pos.CountPosesAll(0, 0, false);
                    ps.all =  ps.active + ps.inactive;
                    break;
                case UserRoles.SubAdmin1:
                case UserRoles.ProjectReport:
                    int ProjId = Integer.parseInt(userMda[2]);

                    if(formMda > 0){
                        ps.level = "mda";
                        ps.title = Org.SingleMda(formMda).name;
                        ps.active = Pos.CountPosesAll(0, formMda, true);
                        ps.inactive = Pos.CountPosesAll(0, formMda, false);
                        ps.all =  ps.active + ps.inactive;
                        break;
                    }

                    ps.level = "state_project";
                    ps.title = Org.SingleBusiness(ProjId, true).clientName;
                    ps.active = Pos.CountPosesAll(ProjId, 0, true);
                    ps.inactive = Pos.CountPosesAll(ProjId, 0, false);
                    ps.all =  ps.active + ps.inactive;

                    break;
                case UserRoles.SubAdmin2:
                    int MdaId = Integer.parseInt(userMda[1]);
                    int MdaProjId = Integer.parseInt(userMda[2]);

                    ps.level = "mda";
                    ps.title = Org.SingleMda(MdaId).name;
                    ps.active = Pos.CountPosesAll(0, MdaId, true);
                    ps.inactive = Pos.CountPosesAll(0, MdaId, false);
                    ps.all =  ps.active + ps.inactive;
                    break;
            }
            success.setStatus(200);
            success.setData(ps);

            return Response.ok(ps).build();
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
}



//Report Template
//    public Response AllMdaSummary(){
//        SuccessResponse success = new SuccessResponse();
//        try {
//            String[] userMda = ExtractTokenUserMdaId(jwt);
//            Object[] roleCheck = jwt.getGroups().toArray();
//            success.setStatus(200);
//            success.setData("");
//
//            switch (roleCheck[0].toString()) {
//                case UserRoles.Admin:
//
//                    break;
//                case UserRoles.SubAdmin1:
//                case UserRoles.ProjectReport:
//                    int ProjId = Integer.parseInt(userMda[2]);
//
//                    break;
//                case UserRoles.SubAdmin2:
//                    int MdaId = Integer.parseInt(userMda[1]);
//                    int MdaProjId = Integer.parseInt(userMda[2]);
//
//                    break;
//                case UserRoles.Agent:
//                    int AgentMdaId = Integer.parseInt(userMda[1]);
//                    int AgentId = Integer.parseInt(userMda[0]);
//
//                    break;
//            }
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
//    }

