package core.engine.request.processor;

import core.enums.Method;
import metrics.Metrics;

import javax.ws.rs.container.ContainerRequestContext;

/**
 * Created by nitina on 10/15/16.
 */
public class RequestProcessorFactory {

    public static RequestProcessor buildRequestProcessor(String method,Class controller,
                                                  Metrics metricsRecorder,
                                                  ContainerRequestContext containerRequestContext,
                                                  String mediaTypeToProduce,String status){

        RequestProcessor requestProcessor = null;

        if (method.equals(Method.GET.getMethodName())) {

            requestProcessor = new GetRequestProcessor(controller,metricsRecorder,containerRequestContext,mediaTypeToProduce, status);

        } else if (method.equals(Method.POST.getMethodName())){

            requestProcessor = new PostRequestProcessor(controller,metricsRecorder,containerRequestContext,mediaTypeToProduce,status);

        }else if (method.equals(Method.PUT.getMethodName())){

            requestProcessor = new PutRequestProcessor(controller,metricsRecorder,containerRequestContext,mediaTypeToProduce,status);

        }else if (method.equals(Method.DELETE.getMethodName())){

            requestProcessor = new DeleteRequestProcessor(controller,metricsRecorder,containerRequestContext,mediaTypeToProduce,status);

        }else if (method.equals(Method.PATCH.getMethodName())){
            requestProcessor = new PatchRequestProcessor(controller,metricsRecorder,containerRequestContext,mediaTypeToProduce,status);
        }else{
            throw new RuntimeException("Panic: Unknown method");
        }

        return requestProcessor;

    }
}
