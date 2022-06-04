package org.bizzdeskgroup.Helpers;

import org.bizzdeskgroup.Dtos.PayStack.TransactionVerificationResponseDto;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.enterprise.context.Dependent;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

//@Path("/users")
@RegisterRestClient
//@RegisterProvider(SensitiveDataResponseMapper.class)
@RegisterClientHeaders
@Dependent

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface RequestHandler {


    @POST
//    @Path("requestPath")
    Response doPostRequest(Object schedule);

//    @GET
//    Response doSmsGetRequest(@PathParam("user")String user, @PathParam("pass")String pass, @PathParam("sender")String sender, @PathParam("SMSText")String SMSText, @PathParam("GSM")String GSM);


    @GET
    Response doSmsGetRequest(@QueryParam("user") String user, @QueryParam("pass") String pass, @QueryParam("sender") String sender, @QueryParam("SMSText") String SMSText, @QueryParam("GSM") String GSM);


    @GET
    TransactionVerificationResponseDto validatePayStack(@QueryParam("trans_reference") String transReference);
}
