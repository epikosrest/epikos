package core.dynamic.resources.domain;

import core.exception.EpikosException;

/**
 * Created by nitina on 2/3/17.
 */
public interface IDynamicResourceControllerPATCH {
    Object process(final IDynamicRequestPATCH dynamicRequest) throws EpikosException;
}
