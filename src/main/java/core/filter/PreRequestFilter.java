package core.filter;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import java.io.IOException;

/**
 * Created by nitina on 6/11/16.
 */

@PreMatching
public class PreRequestFilter implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext requestContext)
            throws IOException {
        //Here we can override behavior if we want to handle/process incoming request method as we want
        //e.g. below a PUT method is being overridden by POST. The consequence ? PUT method request might be redirected to POST
        //handler ! Note: this is just an example what can be done here !
        /*if (requestContext.getMethod().equals(HttpMethod.PUT)) {
            requestContext.setMethod(HttpMethod.POST);
        }*/
    }
}
