package core.dynamic.resources;


import core.domain.enums.ServiceMode;
import core.domain.enums.Status;
import core.exception.EpikosException;
import core.lib.Utility;
import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nitina on 5/8/16.
 */
@Data
public class Api{
    String consume;
    String produce;
    String path;
    String method;
    String request;
    String response;
    String controller;
    String status;
    String responseSpoof;
    ServiceMode serviceMode;
    List<ApiResponse> responseList = new ArrayList();


    /***
     * This will get override by API specific implementation i.e. API classes that extend Api class and will determine
     * or define valid definition of it's own e.g. GetApi, PostApi etc classes extends API class and override this function
     * @return
     * @throws EpikosException
     */
    public boolean isValid() throws EpikosException{
        return true;
    }


    /***
     * This is to validate whether the Api signature qualify for exceptional case or not
     * For example if all three i.e. controller, request and response class are not provided and is empty then we don't care and
     * just register resource as is
     * If controller is not provided but request and response is then we will continue by registering resource as is where for
     * any request coming to the endpoint will be have response as provided
     * @return
     */
    public boolean isExceptionalCase(){
        if(controller == null && request == null && response == null){
            return true;
            //This is to support Spoof mode or Spoof functionality
        }else if(controller == null &&
                response != null
                ){
            //Will only support JSON
            //ToDo add support for any other format like XML etc
            if(Utility.isResourceAJSONObject(response)) {
                setResponseSpoof(Utility.readFile(response));
                setServiceMode(ServiceMode.SPOOF);
                return true;
            }

        }
        return false;
    }
}
