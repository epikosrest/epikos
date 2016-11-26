package core.service.handler;

import core.domain.enums.ServiceMode;
import core.domain.enums.Status;
import core.dynamic.resources.domain.Api;
import core.engine.processor.RequestProcessor;
import core.engine.processor.RequestProcessorFactory;
import core.engine.processor.SpoofRequestProcessor;
import core.error.EpikosError;
import core.spoof.Spoof;
import org.glassfish.jersey.process.Inflector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Response;

/**
 * Created by nitina on 11/17/16.
 */
public class RequestHandler implements Inflector<ContainerRequestContext, Response> {

    Api api;
    final static Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    public RequestHandler(Api api){
        this.api = api;
    }


    @Override
    public Response apply(ContainerRequestContext containerRequestContext) {
        metrics.Metrics metricsRecorder = null;
        Response response = Response.ok().build();
        RequestProcessor requestProcessor = null;

        try {

            final Class controller = api.getController() == null ? null : Class.forName(api.getController());
            metricsRecorder = controller == null ? api.getServiceMode() == ServiceMode.SPOOF ? new metrics.Metrics(Spoof.class) :null: new metrics.Metrics(controller);



            if (metricsRecorder != null) {
                metricsRecorder.updateRequestMetrics(containerRequestContext.getEntityStream().toString().length(), containerRequestContext.getUriInfo().getPath());
            }
            //Check request,response and controller configuration exist and hook it appropriately
            if (controller != null) {

                response = RequestProcessorFactory.buildRequestProcessor(containerRequestContext.getRequest().getMethod(),
                        controller,metricsRecorder,containerRequestContext,api.getProduce(),api.getStatus()).process();

            } else {
                //ToDo: add logic to construct processing request without controller. In this case either response or request and response must be provided

                //ToDo: currently responseSpoof property is only being used by Api builder resource to display Api doc
                //Need to identify good way to handle this  and get rid of responseData property !
                try {
                    if (metricsRecorder != null)
                        metricsRecorder.startTimerContext();
                    if(api.getServiceMode() == ServiceMode.SPOOF){
                        requestProcessor = new SpoofRequestProcessor(metricsRecorder,api.getProduce(),api.getResponseSpoof(),api.getStatus());
                        response = requestProcessor.process(); //Response.ok().entity(drmetaData.getResponseSpoof()).type(drmetaData.getProduce()).build();
                    }else {
                        response = Response.ok().entity(api.getResponse()).type(api.getProduce()).build();
                    }
                } finally {
                    if (metricsRecorder != null) {
                        metricsRecorder.updateResponseMetrics(response.getEntity().toString().length(), containerRequestContext.getUriInfo().getPath());
                        metricsRecorder.stopTimerContext();
                        metricsRecorder = null;
                    }
                }
            }

        } catch(Exception exp){
            logger.info("Exception " + exp.getMessage());
            EpikosError error = new EpikosError();
            error.setMessage(exp.getMessage());
            error.setId(Status.INTERNALSERVERERROR.getStatus());
            return Response.status(error.getId()).entity(error).type(api.getProduce()).build();
        } finally {
            if (metricsRecorder != null) {
                metricsRecorder.updateResponseMetrics(response.getEntity().toString().length(), containerRequestContext.getUriInfo().getPath());
                metricsRecorder.stopTimerContext();
            }
            return response;
        }
    }
}
