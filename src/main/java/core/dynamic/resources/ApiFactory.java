package core.dynamic.resources;

import core.exception.EpikosException;

/**
 * Created by nitina on 1/14/17.
 */
public class ApiFactory {

    public static Api constructApiByType(Api api) throws EpikosException{

        Api returnApi;

        switch (api.getMethod()){
            case "GET":
                returnApi = new GetApi();
                copyProperties(api,returnApi);
                break;
            case "POST":
                returnApi = new PostApi();
                copyProperties(api,returnApi);
                break;
            case "DELETE":
                returnApi = new DeleteApi();
                copyProperties(api,returnApi);
                break;
            case "PUT":
                returnApi = new PutApi();
                copyProperties(api,returnApi);
                break;
            case "PATCH":
                returnApi = new PatchApi();
                copyProperties(api,returnApi);
                break;
            default:
                throw new EpikosException(String.format("Invalid method type %s",api.getMethod()));

        }
        return returnApi;
    }


    private static void copyProperties(Api sourceApi, Api destinationApi){
        destinationApi.setConsume(sourceApi.getConsume());
        destinationApi.setController(sourceApi.getController());
        destinationApi.setMethod(sourceApi.getMethod());
        destinationApi.setPath(sourceApi.getPath());
        destinationApi.setProduce(sourceApi.getProduce());
        destinationApi.setRequest(sourceApi.getRequest());
        destinationApi.setResponse(sourceApi.getResponse());
        destinationApi.setResponseSpoof(sourceApi.getResponseSpoof());
        destinationApi.setServiceMode(sourceApi.getServiceMode());
        destinationApi.setStatus(sourceApi.getStatus());
    }
}
