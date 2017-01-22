package core.dynamic.resources.domain;

import core.exception.EpikosException;
import core.lib.Utility;

/**
 * Created by nitina on 12/24/16.
 */
public class DeleteApi extends Api{

    @Override
    public boolean isValid() throws EpikosException {
        return Utility.isValidMethod(getMethod()) &&
                Utility.isValidPath(getPath()) &&
                Utility.isValidStatusCode(getStatus());
    }
}
