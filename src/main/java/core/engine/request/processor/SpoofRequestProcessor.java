package core.engine.request.processor;

import metrics.Metrics;

import javax.ws.rs.core.Response;

/**
 * Created by nitina on 10/15/16.
 */

public class SpoofRequestProcessor extends RequestProcessor{

    String responseSpoof;

    public SpoofRequestProcessor(Metrics metricsRecorder, String mediaTypeToProduce,String responseSpoof,String status){
        super(null,metricsRecorder,null,mediaTypeToProduce,status);
        this.responseSpoof = responseSpoof;
    }

    @Override
    public Response process() throws IllegalAccessException, InstantiationException {
        return Response.ok().entity(responseSpoof).type(mediaTypeToProduce).build();
    }

    public String getResponseSpoof() {
        return responseSpoof;
    }

    public void setResponseSpoof(String responseSpoof) {
        this.responseSpoof = responseSpoof;
    }
}
