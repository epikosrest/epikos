package core.engine.processor;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import core.domain.enums.Status;
import core.error.EpikosError;

import metrics.Metrics;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by nitina on 10/15/16.
 */

public abstract class RequestProcessor {

    final static Logger logger = LoggerFactory.getLogger(RequestProcessor.class);
    Class controller;
    Metrics metricsRecorder;
    ContainerRequestContext containerRequestContext;
    String mediaTypeToProduce;
    String status;

    public RequestProcessor(Class controller,
                            Metrics metricsRecorder,
                            ContainerRequestContext containerRequestContext,
                            String mediaTypeToProduce,
                            String status){
        this.controller = controller;
        this.metricsRecorder = metricsRecorder;
        this.containerRequestContext = containerRequestContext;
        this.mediaTypeToProduce = mediaTypeToProduce;
        this.status = status;

    }

    public abstract Response process() throws IllegalAccessException, InstantiationException;

    protected void verifyStatusIsSupportedForTheMethod(List<Status> supportedStatusList, Integer statusCode){
        //We will log error if status code is not supported
        if(!supportedStatusList.stream().anyMatch(p->p.getStatus()==statusCode)){
            logger.error(String.format("Panic: Status code %s is not supported hence status OK will be returned. Please verify status code is supported for PUT method !"));
        }
    }

    protected Response constructErrorResponse(Exception exp,String mediaTypeToProduce){

        EpikosError error = new EpikosError();
        String errorMessage = "";
        Status status = Status.UNKNOWN;

        if(exp instanceof UnrecognizedPropertyException){

            errorMessage = String.format("Invalid input data : %s", ((UnrecognizedPropertyException)exp).getPropertyName());
            status = Status.BADREQUEST;

        }else {

            errorMessage = exp.getMessage();
            status = Status.INTERNALSERVERERROR;

        }

        error.setMessage(errorMessage);
        error.setId(status.getStatus());
        logger.error(errorMessage);
        logger.error(exp.getMessage());
        return Response.status(status.getStatus()).entity(error).type(mediaTypeToProduce).build();

    }

    public static Logger getLogger() {
        return logger;
    }

    public Class getController() {
        return controller;
    }

    public void setController(Class controller) {
        this.controller = controller;
    }

    public Metrics getMetricsRecorder() {
        return metricsRecorder;
    }

    public void setMetricsRecorder(Metrics metricsRecorder) {
        this.metricsRecorder = metricsRecorder;
    }

    public ContainerRequestContext getContainerRequestContext() {
        return containerRequestContext;
    }

    public void setContainerRequestContext(ContainerRequestContext containerRequestContext) {
        this.containerRequestContext = containerRequestContext;
    }

    public String getMediaTypeToProduce() {
        return mediaTypeToProduce;
    }

    public void setMediaTypeToProduce(String mediaTypeToProduce) {
        this.mediaTypeToProduce = mediaTypeToProduce;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
