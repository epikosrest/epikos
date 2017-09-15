package service.configuration.resource;

import core.enums.SupportedResourceFileExtension;
import org.apache.commons.lang3.NotImplementedException;

/**
 * Created by nitina on 8/26/17.
 */
public class ResourceFactory {

    public static IResource getResource(SupportedResourceFileExtension supportedResourceFileExtension){
        if(SupportedResourceFileExtension.JSON.equals(supportedResourceFileExtension)){
            return new JsonResource();
        }else if(SupportedResourceFileExtension.YML.equals(supportedResourceFileExtension)){
            return new YmlResource();
        }else{
            throw new NotImplementedException(String.format("The file extension %s is not supported !",supportedResourceFileExtension));
        }
    }
}
