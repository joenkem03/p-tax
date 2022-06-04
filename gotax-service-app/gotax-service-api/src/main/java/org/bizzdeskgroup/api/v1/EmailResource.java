package org.bizzdeskgroup.api.v1;


import io.swagger.annotations.*;
import org.bizzdeskgroup.Dto.MPJWTToken;
import org.bizzdeskgroup.Dto.SuccessResponse;
import org.bizzdeskgroup.Dtos.Command.*;
import org.bizzdeskgroup.Dtos.Query.*;
import org.bizzdeskgroup.Helpers.InfoEmailSmsUtil;
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
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import static org.bizzdeskgroup.Helpers.NotificationMixer.DateTime;
import static org.bizzdeskgroup.Mappers.UserMapper.*;
import static org.bizzdeskgroup.helper.UserTokenUtil.ExtractTokenUserMdaId;
import static org.bizzdeskgroup.helper.UserTokenUtil.generateUserTokenString;

@Path("mailer")
@Api(value = "User Management",hidden = true)
@RequestScoped
@Produces("application/json")
@Consumes("application/json")
//@Log
public class EmailResource {

    @Inject
    JsonWebToken jwt;

    @POST
    @Path("/xentest-email")
    public Response eRrr(EbillsV2Email ph){

        SuccessResponse success = new SuccessResponse();
        try {
//            InfoEmailSmsUtil.SendMail( ph.phone, "J. N. Green", "Your phone confirmation code: 000000", "Send Mail");
            InfoEmailSmsUtil.SendMail( ph.receiverEmail, ph.receiverName, ph.messageContent, ph.messageTitle);
            success.setStatus(200);
            success.setData("");
            return Response.ok(success).build();
        } catch (Exception e) {
            System.out.println("Errr" + e.getMessage());
            e.printStackTrace();
//            throw new BadRequestException(e.getMessage());
            success.setStatus(400);
            success.setData(e.getMessage());
            return Response.ok(success).build();
        }
    }

    @POST
    @Path("/test-phone")
    public Response Rrr(PhoneConfirmRequestDto ph){

        SuccessResponse success = new SuccessResponse();
        try {
            InfoEmailSmsUtil.SendSms("Your phone confirmation code: 000000", ph.phone);
            success.setStatus(200);
            success.setData("");
//            SendMail("joenkem03@gmail.com", "Joe Nkem", "", "");
            success.setData("");
            return Response.ok(success).build();
        } catch (Exception e) {
            System.out.println("Errr" + e.getMessage());
            e.printStackTrace();
//            throw new BadRequestException(e.getMessage());
            success.setStatus(400);
            success.setData(e.getMessage());
            return Response.ok(success).build();
        }
    }

    @POST
    @Path("/new-test-phone")
    public Response Rrrx(PhoneConfirmRequestDto ph){

        SuccessResponse success = new SuccessResponse();
        try {
            InfoEmailSmsUtil.SendSmsAlt("Your new implementation phone confirmation code: 000000", ph.phone);
            success.setStatus(200);
            success.setData("");
//            SendMail("joenkem03@gmail.com", "Joe Nkem", "", "");
//            success.setData("");
            return Response.ok(success).build();
        } catch (Exception e) {
            System.out.println("Errr" + e.getMessage());
            e.printStackTrace();
//            throw new BadRequestException(e.getMessage());
            success.setStatus(400);
            success.setData(e.getMessage());
            return Response.ok(success).build();
        }
    }
}

