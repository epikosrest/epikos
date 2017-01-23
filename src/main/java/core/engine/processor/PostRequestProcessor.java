package core.engine.processor;

import core.domain.enums.Status;
import core.dynamic.resources.domain.DynamicRequest;
import core.dynamic.resources.domain.IDynamicResourceControllerPOST;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nitina on 10/15/16.
 */
public class PostRequestProcessor extends RequestProcessor{



    public PostRequestProcessor(final Class controller,
                                final metrics.Metrics metricsRecorder,
                                final ContainerRequestContext containerRequestContext,
                                String mediaTypeToProduce,String status){
        super(controller,metricsRecorder,containerRequestContext,mediaTypeToProduce,status);

    }
    @Override
    public Response process() throws IllegalAccessException, InstantiationException {
        final IDynamicResourceControllerPOST cont = (IDynamicResourceControllerPOST) controller.newInstance();
        final MultivaluedMap<String, String> pathParams = containerRequestContext.getUriInfo().getPathParameters();

        try {

            final DynamicRequest dynamicRequest = new DynamicRequest(containerRequestContext,pathParams);
            metricsRecorder.startTimerContext();

            Integer statusCode = Status.getStatusCode(status);

            List<Status> supportedStatus = getSupportedStatusListForPOSTMethod();
            verifyStatusIsSupportedForTheMethod(supportedStatus,statusCode);

            Object response = cont.process(dynamicRequest);
            return constructResponse(response,statusCode);

        }catch (Exception exp){
            return constructErrorResponse(exp,mediaTypeToProduce);
        }
        finally {
            metricsRecorder.stopTimerContext();
        }
    }


    private final List<Status> getSupportedStatusListForPOSTMethod(){
        List<Status> supportedStatusList = new ArrayList<>();
        supportedStatusList.add(Status.OK);
        supportedStatusList.add(Status.NOCONTENT);
        supportedStatusList.add(Status.CREATED);
        return supportedStatusList;
    }
}
