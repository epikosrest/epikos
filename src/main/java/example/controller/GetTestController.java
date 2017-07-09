package example.controller;

import com.wordnik.swagger.annotations.*;
import core.dynamic.resources.IDynamicRequestGET;
import core.dynamic.resources.IDynamicResourceControllerGet;
import core.error.EpikosError;
import core.exception.EpikosException;

import example.response.Response;

import javax.annotation.Resource;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by nitina on 2/19/17.
 */
@Path("test")
@Api(value = "Test API", description = "Example")
public class GetTestController{

    //@Override
    @GET
    @Path("test1/{userid}")
    @Produces(MediaType.APPLICATION_JSON)

    @ApiOperation(value = "A test1 operation", notes = "More notes about this method 1", response = Response.class)
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Invalid ID supplied 1",response= EpikosError.class),
            @ApiResponse(code = 500, message = "Server is down! 1",response = EpikosError.class)
    })
    public Response process(@ApiParam(value = "user id") @PathParam("userid") String userid, IDynamicRequestGET dynamicRequest) throws EpikosException {
        Response res =  new Response();
        res.setTest("first response 1");
        return res;

    }


    /*@GET
    @Path("/test2")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "A test2 operation", notes = "More notes about this method 2", response = Response.class)
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Invalid ID supplied 2",response= EpikosError.class),
            @ApiResponse(code = 500, message = "Server is down! 2",response= EpikosError.class)
    })
    public Response process(IDynamicRequestGET dynamicRequest) throws EpikosException {
        Response res =  new Response();
        res.setTest("first response 2");
        return res;
    }*/
}
