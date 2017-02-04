package core.dynamic.resources;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.MultivaluedMap;

/**
 * Created by nitina on 5/17/16.
 */
public interface IDynamicRequestGET {
    public MultivaluedMap<String, String> getPathParams();
    public ContainerRequestContext getRequestContext();
}
