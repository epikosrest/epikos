package core.dynamic.resources.domain;

import core.exception.EpikosException;

/**
 * Created by nitina on 10/20/16.
 */
public interface IDynamicResourceControllerPUT {
    Object process(final IDynamicRequestPUT dynamicRequest) throws EpikosException;
}
