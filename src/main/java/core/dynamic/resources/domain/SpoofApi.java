package core.dynamic.resources.domain;

import core.exception.EpikosException;
import core.lib.Utility;

/**
 * Created by nitina on 1/14/17.
 */
final public class SpoofApi extends Api{
    @Override
    public boolean isValid() throws EpikosException {
        return Utility.isValidMethod(getMethod()) &&
                Utility.isValidPath(getPath()) &&
                Utility.isValidStatusCode(getStatus());
    }
}