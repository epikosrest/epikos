package core.domain.enums;

import core.error.ApiValidationStatus;
import lombok.Getter;

/**
 * Created by nitina on 7/14/17.
 */
@Getter
public enum ApiValidationStatusCode {
    InvalidConsume(1),InvalidContentType(2),InvalidMethod(3),InvalidPath(4),InvalidPathParam(5),InvalidProduce(6),InvalidStatus(7),Unknown(0),Valid(999);

    int code;

    ApiValidationStatusCode(int code){
        this.code = code;
    }
}
