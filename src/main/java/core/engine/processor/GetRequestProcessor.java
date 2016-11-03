package core.engine.processor;

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
            metricsRecorder.startTimerContext();
            Object response = cont.process(dynamicRequest);
            if(response instanceof Response){
                Response respToReturn = (Response)response;
                if(!mediaTypeToProduce.toLowerCase().equals(respToReturn.getMediaType().toString().toLowerCase())){
                    logger.warn(String.format("Panic : media type to produce is mismatching ! Expected to produce %s but returned %s",mediaTypeToProduce,respToReturn.getMediaType()));
                }

                return respToReturn;
            }

            return Response.ok().entity(response).type(mediaTypeToProduce).build();

        } finally {
            metricsRecorder.stopTimerContext();
        }
    }
}
