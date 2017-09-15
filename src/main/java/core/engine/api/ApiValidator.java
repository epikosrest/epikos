package core.engine.api;

import core.enums.ServiceMode;
import core.exception.EpikosException;
import lib.Utility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by nitina on 8/26/17.
 */
public class ApiValidator {

    final static Logger logger = LoggerFactory.getLogger(ApiValidator.class);

    /***
     * Validate dynamic resource meta data before creating resource out of it. The basic validation like type of request class,
     * response class and controller class will be done.
     * @param api
     * @param resourceDocumentBuilder
     */
    public static boolean validateDynamicResource(Api api ,ResourceDocumentBuilder resourceDocumentBuilder) throws EpikosException {

        //Will check some of mandatory attribute for api e.g. status, method and path and make sure all attributes are valid before proceeding for next step
        if(!api.isValid().isValidStatus()){
            buildInvalidInformation(api, resourceDocumentBuilder);
            return false;
        }

        //if(isExceptionalCase(api)){
        if(api.isExceptionalCase()){

            if(!Utility.isValidMethod(api.getMethod()).isValidStatus() || !Utility.isValidPath(api.getPath()).isValidStatus()) {
                buildInvalidInformation(api,resourceDocumentBuilder);
                return false;
            }
            return true;

        }

        //Otherwise validate if any one of component (request,response and controller) present and is valid
        boolean resourceFound = (Utility.doesResourceClassExist(api.getController(),
                IDynamicResourceController.class.getTypeName()+";"+
                        IDynamicResourceControllerGet.class.getTypeName() + ";" +
                        IDynamicResourceControllerPOST.class.getTypeName() + ";" +
                        IDynamicResourceControllerDELETE.class.getTypeName() + ";" +
                        IDynamicResourceControllerPUT.class.getTypeName() + ";" +
                        IDynamicResourceControllerPATCH.class.getTypeName()
                ,resourceDocumentBuilder));
        if(!resourceFound){
            buildInvalidInformation(api,resourceDocumentBuilder);
            resourceFound = false;
        }
        if(api.getRequest() != null && !Utility.doesResourceClassExist(api.getRequest()
                ,"NA",resourceDocumentBuilder)){
            if(!Utility.isResourceAJSONObject(api.getRequest())) {

                buildInvalidInformation(api, resourceDocumentBuilder);
                resourceFound = false;
            }
        }

        if(api.getResponse() != null && !Utility.doesResourceClassExist(api.getResponse()
                ,"NA",resourceDocumentBuilder)){
            if(!Utility.isResourceAJSONObject(api.getResponse())) {
                buildInvalidInformation(api, resourceDocumentBuilder);
                resourceFound = false;
            }
        }


        //If it is a valid api then will setup spoof response (if provided in json file form) for the api
        if(resourceFound){
            if(Utility.isResourceAJSONObject(api.getResponseSpoof())){
                api.setResponseSpoof(Utility.readFile(api.getResponseSpoof()));
            }

        }

        return resourceFound;
    }

    private static void buildInvalidInformation(Api api ,ResourceDocumentBuilder resourceDocumentBuilder){

        try{
            resourceDocumentBuilder.addResourceInvalidInformation(String.format("Failed to create resource, please fix following issue %s",api.isValid().getDescription()));
        }catch (EpikosException epicExp){

            logger.error("Unable to build invalid information \n" +epicExp.getMessage());
        }
    }

}
