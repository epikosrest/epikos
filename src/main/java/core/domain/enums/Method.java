package core.domain.enums;

import lombok.Getter;

/**
 * Created by nitina on 8/20/16.
 */
@Getter
public enum Method {

    DELETE("DELETE"),GET("GET"),PATCH("PATCH"),POST("POST"),PUT("PUT");

    Method(String methodName){
        this.methodName = methodName;
    }

    String methodName;
}
