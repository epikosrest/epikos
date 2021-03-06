package core.engine.api;


import core.enums.ApiValidationStatusCode;
import core.enums.ServiceMode;
import core.error.ApiValidationStatus;
import core.exception.EpikosException;
import lib.Utility;
import lombok.Data;

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
    List<ApiParam> apiParamList = new ArrayList();


    /***
     * This will get override by API specific implementation i.e. API classes that extend Api class and will determine
     * or define valid definition of it's own e.g. GetApi, PostApi etc classes extends API class and override this function
     * @return
     * @throws EpikosException
     */
    public ApiValidationStatus isValid() throws EpikosException{
        return new ApiValidationStatus(ApiValidationStatusCode.Valid);
    }


    /***
     * This is to validate whether the Api signature qualify for exceptional case or not
     * For example if all three controller, request and response class are not provided and is empty then we don't care and
     * just register resource as is
     * If controller is not provided but request and response is, then we will continue by registering resource as is where for
     * any request coming to the endpoint will be have response as provided
     * @return
     */
    public boolean isExceptionalCase(){
        if(controller == null && request == null && response == null && responseSpoof == null){
            return true;
            //This is to support Spoof mode or Spoof functionality
        }else if(controller == null &&
                (response != null || responseSpoof !=null)
                ){
            //Will only support JSON
            //ToDo add support for any other format like XML etc
            if(Utility.isResourceAJSONObject(response == null?responseSpoof:response)) {
                setResponseSpoof(Utility.readFile(response == null?responseSpoof:response));
                setServiceMode(ServiceMode.SPOOF);
                return true;
            }

        }
        return false;
    }
}
