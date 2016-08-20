package core.dynamicrestresources;

import javax.ws.rs.core.MultivaluedMap;

/**
 * Created by nitina on 5/15/16.
 */
public interface IDynamicResourceControllerGet {
    Object process(final IDynamicRequestGET dynamicRequest);
}
