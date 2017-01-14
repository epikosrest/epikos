package core.dynamic.resources.domain;

import core.lib.Utility;

/**
 * Created by nitina on 12/24/16.
 */
final public class GetApi extends Api{

    @Override
    public boolean isValid() {
       return Utility.isValidMethod(getMethod()) &&
                       Utility.isValidPath(getPath()) &&
                       Utility.isValidStatusCode(getStatus());
    }
}
