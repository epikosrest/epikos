package core.engine.api;

import core.exception.EpikosException;

/**
 * Created by nitina on 10/2/16.
 */
public interface IDynamicResourceControllerDELETE {

    Object process(final IDynamicRequestDELETE dynamicRequest) throws EpikosException;

}
