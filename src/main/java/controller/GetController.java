package controller;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
import core.dynamic.resources.IDynamicRequestGET;
import core.dynamic.resources.IDynamicResourceControllerGet;
import core.exception.EpikosException;

import response.Response;
import restserver.Service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by nitina on 2/19/17.
 */
@Path("test")
@Api(value = "/test", description = "Example")
@Produces({"text/plain"})
public class GetController implements IDynamicResourceControllerGet{

    @Override
    @GET
    @Path("/test1")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "A test1 operation", notes = "More notes about this method 1", response = Service.class)
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Invalid ID supplied 1"),
            @ApiResponse(code = 500, message = "Server is down! 1")
    })
    public Response process(IDynamicRequestGET dynamicRequest) throws EpikosException {
        return new Response("first response 1");

    }


    @GET
    @Path("/test2")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "A test2 operation", notes = "More notes about this method 2", response = Service.class)
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Invalid ID supplied 2"),
            @ApiResponse(code = 500, message = "Server is down! 2")
    })
    public Response process2(IDynamicRequestGET dynamicRequest) throws EpikosException {
        return new Response("first response 2");
    }
}
