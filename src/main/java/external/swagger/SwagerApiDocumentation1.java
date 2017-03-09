/*
package external.swagger;

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

*/
/**
 * Created by nitina on 2/19/17.
 *//*

@Path("swagger")
//@Api(value = "%ApiValue%", description = "%ApiDescription%")
@Api(value = "Api Doc", description = "Swagger documentation for API(s)")
public class SwagerApiDocumentation1 {


    @GET
    @Path("/doc1")
    @Produces(MediaType.APPLICATION_JSON)
    */
/*@ApiOperation(value = "First API", notes = "More notes about this method 1", response = Response.class)
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Invalid ID supplied 1"),
            @ApiResponse(code = 500, message = "Server is down! 1")*//*

    //<ApiOperation>@ApiOperation(value = "%ApiOperationValue%", notes = "%ApiOperationNotes%", response = "%ApiOperationResponse%")</ApiOperation>
    */
/*@ApiResponses(value = {
            @ApiResponse(code = 400, message = "Invalid ID supplied 1"),
            @ApiResponse(code = 500, message = "Server is down! 1")
    })*//*

    public Response process0() throws EpikosException {
        Response res =  new Response();
        res.setTest("first response 1");
        return res;

    }


    */
/*@GET
    @Path("/doc2")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "A test2 operation", notes = "More notes about this method 2", response = Service.class)
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Invalid ID supplied 2"),
            @ApiResponse(code = 500, message = "Server is down! 2")
    })
    public Response process1(IDynamicRequestGET dynamicRequest) throws EpikosException {
        Response res =  new Response();
        res.setTest("first response 2");
        return res;
    }*//*

}
*/
