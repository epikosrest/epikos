package core.dynamic.resources.domain;

import java.io.IOException;

/**
 * Created by nitina on 9/29/16.
 */
public interface IDynamicResourceControllerPUT {
    Object process(final IDynamicRequestPUT dynamicRequest) throws Exception;
}
