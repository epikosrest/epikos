package core.dynamic.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import core.exception.EpikosException;

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
    public <T> T getRequest(final Class<T> typeOfRequest) throws EpikosException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            //Note: by this time the inputstream has been decoded by one of filter (PostRequestFilter/PreReqeustFilter)
            return (T) mapper.readValue(requestStream, typeOfRequest.newInstance().getClass());
        }catch (IOException ioExp){
            throw new EpikosException(ioExp.getMessage());

        }catch (InstantiationException instExp){
            throw new EpikosException(instExp.getMessage());

        }catch (IllegalAccessException illExp){
            throw new EpikosException(illExp.getMessage());
        }
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
