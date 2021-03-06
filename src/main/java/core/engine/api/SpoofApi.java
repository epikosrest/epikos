package core.engine.api;

import core.enums.ApiValidationStatusCode;
import core.error.ApiValidationStatus;
import core.exception.EpikosException;
import lib.Utility;

/**
 * Created by nitina on 1/14/17.
 */
final public class SpoofApi extends Api{
    @Override
    public ApiValidationStatus isValid() throws EpikosException {
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
    }
}
