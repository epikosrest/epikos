package core.dynamic.resources;

import core.exception.EpikosException;

/**
 * Created by nitina on 5/18/16.
 */
public interface IDynamicResourceControllerPOST {
    Object process(IDynamicRequestPOST dynamicRequest) throws EpikosException;
}
