package core.engine.api;

import core.exception.EpikosException;

/**
 * Created by nitina on 5/17/16.
 */
public interface IDynamicRequestPOST extends IDynamicRequestGET{
    public <T> T getRequest(final Class<T> typeOfRequest) throws EpikosException;
}
