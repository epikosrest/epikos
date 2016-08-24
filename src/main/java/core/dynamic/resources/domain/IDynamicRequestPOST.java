package core.dynamic.resources.domain;

import java.io.IOException;

/**
 * Created by nitina on 5/17/16.
 */
public interface IDynamicRequestPOST extends IDynamicRequestGET{
    public <T> T getReqeust(final Class<T> typeOfRequest) throws IOException,InstantiationException,IllegalAccessException;
}
