package core.engine.request.processor;

import core.enums.Status;
import core.engine.api.DynamicRequest;
import core.engine.api.IDynamicResourceControllerDELETE;
import metrics.Metrics;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nitina on 10/15/16.
 */
public class DeleteRequestProcessor extends RequestProcessor {

    public DeleteRequestProcessor(Class controller,
                               Metrics metricsRecorder,
                               ContainerRequestContext containerRequestContext,
                               String mediaTypeToProduce,String status){
        super(controller,metricsRecorder,containerRequestContext,mediaTypeToProduce,status);

    }

    /*
    The function process DELETE request.
    Param status in this method determines what kind of response to return.
    As per RFC ref https://tools.ietf.org/html/rfc2616#section-9.6 It only supports NOCONTENT, ACCEPTED and OK
    If any other status is listed then it will return OK. In case of error BAD REQUEST and INTERNAL SERVICE ERROR
    with appropriate error message will return
     */
    @Override
    public Response process() throws IllegalAccessException, InstantiationException {
        final IDynamicResourceControllerDELETE cont = (IDynamicResourceControllerDELETE) controller.newInstance();
        final MultivaluedMap<String, String> pathParams = containerRequestContext.getUriInfo().getPathParameters();
        try {

            final DynamicRequest dynamicRequest = new DynamicRequest(containerRequestContext,pathParams);
            metricsRecorder.startTimerContext();

            Integer statusCode = Status.getStatusCode(status);

            List<Status> supportedStatus = getSupportedStatusListForDELETEMethod();
            verifyStatusIsSupportedForTheMethod(supportedStatus,statusCode);

            Object response = cont.process(dynamicRequest);
            return constructResponse(response,statusCode);

        }catch (Exception exp) {

            return constructErrorResponse(exp,mediaTypeToProduce);

        }finally
        {
            metricsRecorder.stopTimerContext();
        }
    }

    private final List<Status> getSupportedStatusListForDELETEMethod(){
        List<Status> supportedStatusList = new ArrayList<>();
        supportedStatusList.add(Status.OK);
        supportedStatusList.add(Status.NOCONTENT);
        supportedStatusList.add(Status.ACCEPTED);
        return supportedStatusList;
    }

}
