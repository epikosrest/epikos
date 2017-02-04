package core.engine.processor;

import core.domain.enums.Status;
import core.dynamic.resources.domain.DynamicRequest;
import core.dynamic.resources.domain.IDynamicResourceControllerPATCH;
import core.dynamic.resources.domain.IDynamicResourceControllerPUT;
import core.exception.EpikosException;
import metrics.Metrics;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nitina on 2/3/17.
 */
public class PatchRequestProcessor extends RequestProcessor{

    public PatchRequestProcessor(Class controller,
                               Metrics metricsRecorder,
                               ContainerRequestContext containerRequestContext,
                               String mediaTypeToProduce,String status){
        super(controller,metricsRecorder,containerRequestContext,mediaTypeToProduce,status);

    }

    @Override
    public Response process() throws IllegalAccessException, InstantiationException, EpikosException {
        final IDynamicResourceControllerPATCH processor = (IDynamicResourceControllerPATCH) controller.newInstance();
        final MultivaluedMap<String, String> pathParams = containerRequestContext.getUriInfo().getPathParameters();

        try {

            final DynamicRequest dynamicRequest = new DynamicRequest(containerRequestContext,pathParams);
            metricsRecorder.startTimerContext();

            Integer statusCode = Status.getStatusCode(status);

            List<Status> supportedStatus = getSupportedStatusListForPatchMethod();
            verifyStatusIsSupportedForTheMethod(supportedStatus,statusCode);

            Object response = processor.process(dynamicRequest);
            return constructResponse(response,statusCode);

        }catch (Exception exp) {

            return constructErrorResponse(exp,mediaTypeToProduce);

        }finally
        {
            metricsRecorder.stopTimerContext();
        }
    }

    private final List<Status> getSupportedStatusListForPatchMethod(){
        List<Status> supportedStatusList = new ArrayList<>();
        supportedStatusList.add(Status.OK);
        supportedStatusList.add(Status.NOCONTENT);
        supportedStatusList.add(Status.CREATED);
        return supportedStatusList;
    }
}
