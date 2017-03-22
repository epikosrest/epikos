package core.dynamic.resources;

import core.exception.EpikosException;
import core.lib.Utility;

/**
 * Created by nitina on 12/24/16.
 */
final public class PutApi extends Api{

    @Override
    public boolean isValid() throws EpikosException{
        return Utility.isValidMethod(getMethod()) &&
                Utility.isValidPath(getPath()) &&
                Utility.isValidStatusCode(getStatus()) &&
                Utility.isValidContentType(getConsume()) &&
                Utility.isValidContentType(getProduce());
        //ToDo: do we need controller validation as well ? Investigate !
        //&& getController()==null?true:Utility.isValidClass(getController());
    }
}
