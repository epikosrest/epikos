package core.filter;

import core.constants.ENCODING_TYPE;
import org.glassfish.jersey.spi.ContentEncoder;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import java.io.IOException;
import java.util.List;

import static javax.ws.rs.HttpMethod.*;

/**
 * Created by nitina on 6/11/16.
 */
//Ref: https://jersey.java.net/documentation/latest/filters-and-interceptors.html
public class PostRequestFilter implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext requestContext)
            throws IOException {

        //ToDo: This is not exactly right place to do content-decoding for inputstream e.g. gzip or anyother format.
        // This should have been tackled by implementing ReaderInterceptor in RequestResponseInterceptor as suggested in Jersey doc
        //ref:https://jersey.java.net/documentation/latest/filters-and-interceptors.html

        ContentEncoder encoder = new org.glassfish.jersey.message.GZipEncoder();
        List<String> contentEncoding = requestContext.getHeaders().get("content-encoding");
        if (!requestContext.getMethod().equalsIgnoreCase(GET) && contentEncoding != null && contentEncoding.stream().anyMatch(encode->encode.equals(ENCODING_TYPE.GZIP_ENCODING))){
            requestContext.setEntityStream(encoder.decode(ENCODING_TYPE.GZIP_ENCODING, requestContext.getEntityStream()));
        }
        //ToDo: implement security feature
        /*final SecurityContext securityContext =
                requestContext.getSecurityContext();
        if (securityContext == null ||
                !securityContext.isUserInRole("privileged")) {

            requestContext.abortWith(Response
                    .status(Response.Status.UNAUTHORIZED)
                    .entity("User cannot access the resource.")
                    .build());
        }*/
    }
}