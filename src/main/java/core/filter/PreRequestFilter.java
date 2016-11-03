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
        //ToDo: at this time we have not implemented any specific functionality using pre ContainerReqeustFilter
        //At minimum will just match PUT to POST internally and route all PUT request to POST method

        if (requestContext.getMethod().equalsIgnoreCase(HttpMethod.PUT)) {
            requestContext.setMethod(HttpMethod.POST);
        }
    }
}
