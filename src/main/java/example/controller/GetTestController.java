package example.controller;

import com.wordnik.swagger.annotations.*;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
import core.engine.api.IDynamicRequestGET;
import core.error.EpikosError;
import core.exception.EpikosException;

import example.request.HelloRequest;
import example.response.Response;
import io.swagger.annotations.*;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * Created by nitina on 2/19/17.
 */
@Path("users")
@Api(value = "Test API", description = "Example")
public class GetTestController{

    //@Override
    @GET
    @Path("{userid}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "A user info based on user id", notes = "Returns user info of user id supplied", response = Response.class)
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Invalid userid",response= EpikosError.class),
            @ApiResponse(code = 500, message = "Server",response = EpikosError.class)
    })
    public Response getUserDetail(@ApiParam(value = "user id") @PathParam("userid") String userid) throws EpikosException {
        Response res =  new Response();
        res.setTest("first response 1");
        return res;

    }


    @GET
    @Path("{userid}/profile")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Get profile of user", notes = "Return profile of user", response = Response.class)
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Invalid user id",response= EpikosError.class),
            @ApiResponse(code = 500, message = "Server is down!",response= EpikosError.class)
    })
    public Response getProfile(@ApiParam(value = "user id") @PathParam("userid") String userid) throws EpikosException {
        Response res =  new Response();
        res.setTest("User profile info");
        return res;
    }


    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Create user", notes = "Return user created", response = Response.class)
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Server is down!",response= EpikosError.class)
    })
    public Response createUser(@ApiParam(name="Request to create User",value="User body") HelloRequest request){

        Response res =  new Response();
        res.setTest("User profile created");
        return res;

    }
}
