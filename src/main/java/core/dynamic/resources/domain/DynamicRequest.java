package core.dynamic.resources.domain;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.MultivaluedMap;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by nitina on 5/17/16.
 */
public final class DynamicRequest implements IDynamicRequest{

    ContainerRequestContext requestContext;
    InputStream requestStream;
    MultivaluedMap<String,String> pathParams;

    public DynamicRequest(ContainerRequestContext requestContext, MultivaluedMap<String,String> pathParams){
        this.requestContext = requestContext;
        this.requestStream = requestContext.getEntityStream();
        this.pathParams = pathParams;
    }

    @Override
    public <T> T getReqeust(final Class<T> typeOfRequest) throws IOException,InstantiationException,IllegalAccessException {
        ObjectMapper mapper = new ObjectMapper();
        //Note: by this time the inputstream has been decoded by one of filter (PostRequestFilter/PreReqeustFilter)
        return (T) mapper.readValue(requestStream, typeOfRequest.newInstance().getClass());
    }

    @Override
    public MultivaluedMap<String, String> getPathParams() {
        return pathParams;
    }

    @Override
    public ContainerRequestContext getRequestContext() {
        return requestContext;
    }


}
