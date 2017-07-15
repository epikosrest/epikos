package core.error;

import core.domain.enums.ApiValidationStatusCode;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by nitina on 7/14/17.
 */
@Data
public class ApiValidationStatus {
    ApiValidationStatusCode code;
    String description;

    public ApiValidationStatus(ApiValidationStatusCode code) {
        this(code,StringUtils.EMPTY);
    }

    public ApiValidationStatus(ApiValidationStatusCode code,String description){
        this.code = code;
        this.description = description;
    }


    public boolean isValidStatus(){
        return code.equals(ApiValidationStatusCode.Valid);
    }
}
