package core.engine.processor;

import core.domain.enums.Status;
import core.dynamic.resources.domain.DynamicRequest;
import core.dynamic.resources.domain.IDynamicResourceControllerGet;
import core.exception.EpikosException;
import metrics.Metrics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

/**
 * Created by nitina on 10/15/16.
 */
public class GetRequestProcessor extends RequestProcessor {

    final static Logger logger = LoggerFactory.getLogger(GetRequestProcessor.class);

    public GetRequestProcessor(Class controller,
                               Metrics metricsRecorder,
                               ContainerRequestContext containerRequestContext,
                               String mediaTypeToProduce,String status){
        super(controller,metricsRecorder,containerRequestContext,mediaTypeToProduce,status);
    }

    @Override
    public Response process() throws IllegalAccessException, InstantiationException,EpikosException {
        final IDynamicResourceControllerGet cont = (IDynamicResourceControllerGet) controller.newInstance();
        final MultivaluedMap<String, String> pathParams = containerRequestContext.getUriInfo().getPathParameters();
        try {

            final DynamicRequest dynamicRequest = new DynamicRequest(containerRequestContext,pathParams);
            Integer statusCode = Status.getStatusCode(status);
            metricsRecorder.startTimerContext();
            Object response = cont.process(dynamicRequest);
            return constructResponse(response,statusCode);

        } finally {
            metricsRecorder.stopTimerContext();
        }
    }
}
