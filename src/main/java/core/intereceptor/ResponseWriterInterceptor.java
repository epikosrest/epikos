package core.intereceptor;

import core.domain.constants.ENCODING_TYPE;

import javax.validation.constraints.NotNull;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.*;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.zip.GZIPOutputStream;

/**
 * Created by nitina on 6/2/16.
 */
@Provider
public class ResponseWriterInterceptor implements WriterInterceptor {

    private HttpHeaders httpHeaders;

    public ResponseWriterInterceptor(@Context @NotNull HttpHeaders httpHeaders) {
        this.httpHeaders = httpHeaders;
    }


    @Override
    public void aroundWriteTo(WriterInterceptorContext context)
            throws IOException, WebApplicationException {

        MultivaluedMap<String,String> requestHeaders = httpHeaders.getRequestHeaders();

        List<String> acceptEncoding = requestHeaders.get(HttpHeaders.ACCEPT_ENCODING);
        // Compress if client accepts gzip encoding
        if(acceptEncoding!=null) {

            if (acceptEncoding.stream().anyMatch(encode->encode.equalsIgnoreCase(ENCODING_TYPE.GZIP_ENCODING))) {

                MultivaluedMap<String, Object> headers = context.getHeaders();
                headers.add(HttpHeaders.CONTENT_ENCODING, ENCODING_TYPE.GZIP_ENCODING);

                final OutputStream outputStream = context.getOutputStream();
                context.setOutputStream(new GZIPOutputStream(outputStream));

            }

        }
        context.proceed();
    }

}
