package core.enums;

/**
 * Created by nitina on 8/26/17.
 */
public enum SupportedResourceFileExtension {
    JSON,YML,UNKNOWN;

    public static SupportedResourceFileExtension getFileExtension(String fileName){
       if(fileName.toLowerCase().contains(JSON.name().toLowerCase())){
           return JSON;
       }else if(fileName.toLowerCase().contains(YML.name().toLowerCase())){
           return YML;
       }else{
           return UNKNOWN;
       }
    }
}
