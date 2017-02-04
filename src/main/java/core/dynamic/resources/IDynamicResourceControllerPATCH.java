package core.dynamic.resources;

import core.exception.EpikosException;

/**
 * Created by nitina on 2/3/17.
 */
public interface IDynamicResourceControllerPATCH {
    Object process(final IDynamicRequestPATCH dynamicRequest) throws EpikosException;
}
