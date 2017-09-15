package core.engine.api;

/**
 * Created by nitina on 2/4/17.
 * Note: This interface not being used currently. But we will use it to handle custom method type
 * once we figure out how Jersey internally intercept and handle http methods and validate and allow (if at all) custom
 * http method !
 * This interface has been introduce to support custom method type for dynamic API
 */
public interface IMethod {
    String getMethodName();
}
