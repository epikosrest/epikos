package core.dynamic.resources;

import core.domain.enums.ApiValidationStatusCode;
import core.error.ApiValidationStatus;
import core.exception.EpikosException;
import core.lib.Utility;

/**
 * Created by nitina on 12/24/16.
 */
final public class PostApi extends Api{
    @Override
    public ApiValidationStatus isValid() throws EpikosException{

        ApiValidationStatus apiValidity = new ApiValidationStatus(ApiValidationStatusCode.Valid);

        ApiValidationStatus valid = Utility.isValidMethod(getMethod());
        if(!valid.isValidStatus()) {
            apiValidity.setDescription(valid.getDescription());
        }
        valid = Utility.isValidPath(getPath());
        if(!valid.isValidStatus()){
            apiValidity.setDescription(apiValidity.getDescription() + "\n" + valid.getDescription());
        }

        valid = Utility.isValidStatusCode(getStatus());
        if(!valid.isValidStatus()){
            apiValidity.setDescription(apiValidity.getDescription() + "\n" + valid.getDescription());
        }

        valid = Utility.isValidContentType(getConsume());
        if(!valid.isValidStatus()){
            apiValidity.setDescription(apiValidity.getDescription() + "\n" + valid.getDescription());
        }

        valid = Utility.isValidContentType(getProduce());
        if(!valid.isValidStatus()){
            apiValidity.setDescription(apiValidity.getDescription() + "\n" + valid.getDescription());
        }

        valid = Utility.doesPathParamsMatchWithApiPathParam(getPath(),getApiParamList());
        if(!valid.isValidStatus()){
            apiValidity.setDescription(apiValidity.getDescription() + "\n" + valid.getDescription());
        }

        return apiValidity;
        //ToDo: do we need controller validation as well ? Investigate !
        //&& getController()==null?true:Utility.isValidClass(getController());
    }
}
