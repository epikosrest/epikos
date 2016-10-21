package core.domain.enums;


/**
 * Created by nitina on 8/20/16.
 */

public enum Method {

    DELETE("DELETE"),GET("GET"),PATCH("PATCH"),POST("POST"),PUT("PUT");

    Method(String methodName){
        this.methodName = methodName;
    }

    String methodName;

    public String getMethodName() {
        return methodName;
    }
}
