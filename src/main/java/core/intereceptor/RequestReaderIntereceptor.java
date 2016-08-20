package core.intereceptor;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.ReaderInterceptor;
import javax.ws.rs.ext.ReaderInterceptorContext;
import java.io.IOException;

/**
 * Created by nitina on 6/11/16.
 */
@Provider
public class RequestReaderIntereceptor implements ReaderInterceptor {

    @Override
    public Object aroundReadFrom(ReaderInterceptorContext requestContext)
            throws IOException, WebApplicationException {
        //ToDo: implement inputstream decoding to gzip or any other format logic here instead of Filter (PostRequestFilter or PreReqeustFilter)
        //Note: This code is not being invoke regardless the Provider has been registered in ServiceResourceConfig
        // for some reason by Jersey. Regardless the functionality has no impact but need more investigation or possibly a bug !
        return requestContext.proceed();
    }
}

