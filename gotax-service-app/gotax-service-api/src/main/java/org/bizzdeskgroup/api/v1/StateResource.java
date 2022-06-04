package org.bizzdeskgroup.api.v1;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.bizzdeskgroup.Dto.SuccessResponse;
import org.bizzdeskgroup.Dtos.Query.LgaDto;
import org.bizzdeskgroup.Dtos.Query.StateDto;
import org.bizzdeskgroup.models.UserRoles;
import org.bizzdeskgroup.services.OrganisationService;
import org.bizzdeskgroup.services.impl.OrganisationServiceImpl;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("agent/pos")
@Api(value = "State (ALL roles)")
@RequestScoped
@Produces("application/json")
@Consumes("application/json")
public class StateResource {

    OrganisationService Org = null;

    @GET
    @Path("/state")
    @ApiOperation(value = " ", notes = "  ", authorizations = {@Authorization(value = "authorization")})
    @RolesAllowed({UserRoles.Admin, UserRoles.SubAdmin1, UserRoles.SubAdmin2, UserRoles.Agent, UserRoles.NonIndividual, UserRoles.Individual})
    public Response GetStates(){
        SuccessResponse success = new SuccessResponse();
        Org = new OrganisationServiceImpl();
        try {
            List<StateDto> states = Org.GetStates();
            success.setStatus(200);
            success.setData(states);
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
    @Path("/state/search")
    @ApiOperation(value = "", notes = "  ", authorizations = {@Authorization(value = "authorization")})
    @RolesAllowed({UserRoles.Admin, UserRoles.SubAdmin1, UserRoles.SubAdmin2, UserRoles.Agent, UserRoles.NonIndividual, UserRoles.Individual})
    public Response SearchStates(@QueryParam("state_search")String StateName){
        SuccessResponse success = new SuccessResponse();
        Org = new OrganisationServiceImpl();
        try {
            List<StateDto> states = Org.SearchStates(StateName);
            success.setStatus(200);
            success.setData(states);
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
    @Path("/all-lga")
    @ApiOperation(value = "Get state LGAs", notes = "  ", authorizations = {@Authorization(value = "authorization")})
    @RolesAllowed({UserRoles.Admin, UserRoles.SubAdmin1, UserRoles.SubAdmin2, UserRoles.Agent, UserRoles.NonIndividual, UserRoles.Individual})
    public Response GetLgas(){
        SuccessResponse success = new SuccessResponse();
        Org = new OrganisationServiceImpl();
        try {
            List<LgaDto> lgas = Org.AllGetLgas();
            success.setStatus(200);
            success.setData(lgas);
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
    @Path("/lga")
    @ApiOperation(value = "Get state LGAs", notes = "  ", authorizations = {@Authorization(value = "authorization")})
    @RolesAllowed({UserRoles.Admin, UserRoles.SubAdmin1, UserRoles.SubAdmin2, UserRoles.Agent, UserRoles.NonIndividual, UserRoles.Individual})
    public Response GetLgasByStateId(@QueryParam("state_id")int StateId){
        SuccessResponse success = new SuccessResponse();
        Org = new OrganisationServiceImpl();
        try {
        List<LgaDto> lgas = Org.GetLgas(StateId);
        success.setStatus(200);
        success.setData(lgas);
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
    @Path("/lga/search")
    @ApiOperation(value = "Search state LGAs", notes = "  ", authorizations = {@Authorization(value = "authorization")})
    @RolesAllowed({UserRoles.Admin, UserRoles.SubAdmin1, UserRoles.SubAdmin2, UserRoles.Agent, UserRoles.NonIndividual, UserRoles.Individual})
    public Response SeachStateLgas(@QueryParam("state_id")int StateId, @QueryParam("lga_search")String LgaName){
        SuccessResponse success = new SuccessResponse();
        Org = new OrganisationServiceImpl();
        try {
            List<LgaDto> lgas = Org.SearchLgas(StateId, LgaName);
        success.setStatus(200);
        success.setData(lgas);
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
}
