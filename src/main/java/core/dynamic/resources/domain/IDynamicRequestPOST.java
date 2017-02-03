package core.dynamic.resources.domain;

import core.exception.EpikosException;

import java.io.IOException;

/**
 * Created by nitina on 5/17/16.
 */
public interface IDynamicRequestPOST extends IDynamicRequestGET{
    public <T> T getRequest(final Class<T> typeOfRequest) throws EpikosException;
}
