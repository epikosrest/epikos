package core.dynamic.resources.domain;

/**
 * Created by nitina on 5/18/16.
 */
public interface IDynamicResourceControllerPOST {
    Object process(IDynamicRequestPOST dynamicRequest) throws Exception;
}
