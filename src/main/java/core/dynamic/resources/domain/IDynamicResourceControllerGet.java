package core.dynamic.resources.domain;

import core.exception.EpikosException;

/**
 * Created by nitina on 5/15/16.
 */
public interface IDynamicResourceControllerGet {
    Object process(final IDynamicRequestGET dynamicRequest) throws EpikosException;
}
