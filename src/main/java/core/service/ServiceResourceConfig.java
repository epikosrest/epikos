/*
Copyright (c) [2016] [epikosrest@gmail.com]

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

package core.service;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.wordnik.swagger.jaxrs.config.BeanConfig;
import core.domain.enums.Method;
import core.domain.enums.ServiceMode;
import core.domain.enums.Status;
import core.dynamic.resources.domain.*;
import core.engine.processor.*;
import core.error.EpikosError;
import core.filter.PostRequestFilter;
import core.filter.PreRequestFilter;
import core.intereceptor.RequestReaderIntereceptor;
import core.intereceptor.ResponseWriterInterceptor;
import core.service.handler.RequestHandler;
import core.spoof.Spoof;
import org.apache.commons.lang3.StringUtils;
import org.glassfish.jersey.moxy.json.MoxyJsonConfig;
import org.glassfish.jersey.process.Inflector;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.model.Resource;
import org.glassfish.jersey.server.model.ResourceMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.parser.ParserException;
import restserver.Service;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ContextResolver;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

/**
 * Created by nitina on 5/4/16.
 */
public  class ServiceResourceConfig extends ResourceConfig {

    IServiceMetaData metaData;
    final static Logger logger = LoggerFactory.getLogger(ServiceResourceConfig.class);

    public ServiceResourceConfig(IServiceMetaData metaData) throws Exception{
        this.metaData = metaData;

        List<Api> apiList = new ArrayList<>();
        try {

            apiList = loadDynamicResource(metaData.getDynamicResourceConfigLocation());
            buildAndRegisterDynamicResource(apiList, metaData.getServiceURI());

        }catch (IOException ioExp){
            buildAndRegisterInvalidDocInfo("The dynamic resource api list file is not available. Please check the value dynamic.resource.configuration in Application.configuration file and make sure the file exist !\n" + ioExp.getMessage());

        }catch (ParserException parserExp){
            buildAndRegisterInvalidDocInfo("The dynamic resource api list is an invalid yml file. Please check the api list and fix following issue \n" + parserExp.getMessage());
        }

        register(com.wordnik.swagger.jersey.listing.ApiListingResourceJSON.class);
        register(com.wordnik.swagger.jersey.listing.JerseyApiDeclarationProvider.class);
        register(com.wordnik.swagger.jersey.listing.JerseyResourceListingProvider.class);
        register(new CrossDomainFilter());
        register(createMoxyJsonResolver()); //JSON support
        register(RequestReaderIntereceptor.class);
        register(ResponseWriterInterceptor.class); //Register to support gzip encoding in response
        register(PreRequestFilter.class);
        register(PostRequestFilter.class);


        packages(metaData.getResourcePackageName());
        beanConfiguration(metaData);
    }

    /**
     * Created by nitina on 4/25/16.
     */
    //ToDo: Investigate and research and verify if anything need to be added/changed and why to implement
    public static class CrossDomainFilter implements ContainerResponseFilter {

        @Override
        public void filter(ContainerRequestContext creq, ContainerResponseContext cres) {
            cres.getHeaders().add("Access-Control-Allow-Origin", "");
            cres.getHeaders().add("Access-Control-Allow-Headers", "");
            cres.getHeaders().add("Access-Control-Allow-Credentials", "");
            cres.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT");
            cres.getHeaders().add("Access-Control-Max-Age", "");
            cres.getHeaders().add("Access-Control-Allow-Origin", "*");
        }
    }

    public static void beanConfiguration(IServiceMetaData metaData){
        BeanConfig beanConfig = new BeanConfig();
        beanConfig.setVersion("1.0");
        beanConfig.setScan(true);
        if(metaData.getResourcePackageName() != null) {
            for (String resourcePackage : metaData.getResourcePackageName()) {
                beanConfig.setResourcePackage(resourcePackage);
            }
        }
        beanConfig.setResourcePackage(Service.class.getPackage().getName());
        beanConfig.setBasePath(metaData.getServiceURI());
        beanConfig.setDescription("Resources");
        beanConfig.setTitle("Apis");
    }

    private Resource scanAndBuildResources(final Api drmetaData){
        final Resource.Builder resourceBuilder = Resource.builder();
        resourceBuilder.path(drmetaData.getPath());

        final ResourceMethod.Builder methodBuilder = resourceBuilder.addMethod(drmetaData.getMethod());
        methodBuilder.consumes(drmetaData.getConsume());

        final RequestHandler requestHandler = new RequestHandler(drmetaData);
        methodBuilder.produces(drmetaData.getProduce())
                .handledBy(requestHandler);

        return resourceBuilder.build();
    }


    /*
    * Load dynamic resource from config file. Note the config file can be anyfile defined in Application.properties file
    * as value of key dynamic.resource.configuration
     */
    private List<Api> loadDynamicResource(String dynamicResourceFileName) throws IOException,ParserException{

        //Load dynamic resource information from file specified as value of key dynamic.resource.configuration defined in Application.Configuraiton
        DynamicResourceContainer dynamicResource = new DynamicResourceContainer();

        dynamicResource = loadDynamicResourceFromYaml(dynamicResourceFileName);


        if(dynamicResource == null){
            return null;
        }
        return dynamicResource.getApiList();

    }

    private DynamicResourceContainer loadDynamicResourceFromYaml(String fileName) throws IOException,ParserException{

        Yaml yaml = new Yaml();
        DynamicResourceContainer config = null;
        final String fileSeparator = System.getProperty("file.separator");
        final  String DEFAULT_CONFIGURATION_FOLDER_NAME = "Config";
        String yamlFileFullPath = System.getProperty("user.dir") + fileSeparator
                + DEFAULT_CONFIGURATION_FOLDER_NAME + fileSeparator + fileName;

        try( InputStream in = Files.newInputStream( Paths.get(yamlFileFullPath) ) ) {
            config = yaml.loadAs( in, DynamicResourceContainer.class );

        }catch (IOException ioExp){
            logger.error(ioExp.getMessage());
            throw ioExp;
        }catch (ParserException parserExp){

            logger.error("Error reading dynamic resource file : " + parserExp.getMessage());
            throw parserExp;

        }

        return config;
    }

    private void buildAndRegisterDynamicResource(List<Api> apiList,String serviceURI) {
        ResourceDocumentBuilder resourceDocumentBuilder = new ResourceDocumentBuilder();
        List<Resource> validResourceList = new ArrayList<>();
        if(apiList != null) {
            Resource resource = null;
            for (Api api : apiList) {

                if (validateDynamicResource(api, resourceDocumentBuilder)) {
                    resource = scanAndBuildResources(api);
                    validResourceList.add(resource);
                    registerResources(resource);
                }
            }
            resourceDocumentBuilder.addResourceValidInformation(validResourceList,serviceURI);
        }


        buildAndRegisterApiDocument(resourceDocumentBuilder);
    }

    //This function is to add info/error if dynamic resource file is not present or is not a valid yaml file
    private void buildAndRegisterInvalidDocInfo(String invalidInfo){
        ResourceDocumentBuilder resourceDocumentBuilder = new ResourceDocumentBuilder();
        resourceDocumentBuilder.addResourceDocInvalidInfo(invalidInfo);
        buildAndRegisterApiDocument(resourceDocumentBuilder);
    }


    /***
     * Validate dynamic resource meta data before creating resource out of it. The basic validation like type of request class,
     * response class and controller class will be done.
     * @param api
     * @param resourceDocumentBuilder
     */
    private boolean validateDynamicResource(Api api ,ResourceDocumentBuilder resourceDocumentBuilder){

        if(isExceptionalCase(api)){
            if(!isValidMethod(api.getMethod()) || !isValidPath(api.getPath())) {
                buildInvalidInformation(api,resourceDocumentBuilder);
                return false;
            }
            return true;

        }

        //Othewise validate if any one of component (request,response and controller) present and is valid
        boolean resourceFound = (resourceClassExist(api.getController(),
                IDynamicResourceController.class.getTypeName()+";"+
                        IDynamicResourceControllerGet.class.getTypeName() + ";" +
                        IDynamicResourceControllerPOST.class.getTypeName()
                ,resourceDocumentBuilder));
        if(!resourceFound){
            buildInvalidInformation(api,resourceDocumentBuilder);
            resourceFound = false;
        }
        if(api.getRequest() != null && !resourceClassExist(api.getRequest()
                ,"NA",resourceDocumentBuilder)){
            if(!isResourceASONObject(api.getRequest(),null)) {

                buildInvalidInformation(api, resourceDocumentBuilder);
                resourceFound = false;
            }
        }

        if(api.getResponse() != null && !resourceClassExist(api.getResponse()
                ,"NA",resourceDocumentBuilder)){
            if(!isResourceASONObject(api.getResponse(),null)) {
                buildInvalidInformation(api, resourceDocumentBuilder);
                resourceFound = false;
            }
        }

        if(!isValidStatusCode(api.getStatus()) || !isValidMethod(api.getMethod()) || !isValidPath(api.getPath())){

            buildInvalidInformation(api, resourceDocumentBuilder);
            resourceFound = false;
        }

        return resourceFound;
    }

    private boolean isValidStatusCode(String status){
        if(StringUtils.isEmpty(status) || StringUtils.isBlank(status)){
            return false;
        }

        Integer statusCode = Status.getStatusCode(status);
        if(statusCode == null){
            try {
                Status statusToVerify = Status.valueOf(status);
            }catch (Exception exp){
                logger.error(exp.getMessage());
                return false;
            }

        }
        return true;
    }

    //This is to validate whether the Api signature qualify for exceptional case or not
    //For example if all three i.e. controller, request and response class are not provided and is empty then we don't care and
    //just register resource as is
    //If controller is not provided but request and response is then we will continue by registering resource as is where for
    //any request coming to the endpoint will be have response as provided
    private boolean isExceptionalCase(Api api){
        if(api.getController() == null &&
                api.getRequest() == null &&
                api.getResponse() == null){
            return true;
        //This is to support Spoof mode or Spoof functionality
        }else if(api.getController() == null &&
                api.getResponse() != null
                ){
            //Will only support JSON
            //ToDo add support for any other format like XML etc
            if(isResourceASONObject(api.getResponse(),api)) {
                return true;
            }

        }
        return false;
    }

    private void buildInvalidInformation(Api api ,ResourceDocumentBuilder resouceDocumentBuilder){

        String path = (StringUtils.isEmpty(api.getPath()) || StringUtils.isBlank(api.getPath()))?"(path has not defined)":api.getPath();
        String method = (StringUtils.isEmpty(api.getMethod()) || StringUtils.isBlank(api.getMethod()))?"(method has not defined)":api.getMethod();
        String consume = (StringUtils.isEmpty(api.getConsume()) || StringUtils.isBlank(api.getConsume()))?"(consume content type has not defined)":api.getConsume();
        String produce = (StringUtils.isEmpty(api.getProduce()) || StringUtils.isBlank(api.getProduce()))?"(produce content type has not defined)":api.getProduce();
        String status = (StringUtils.isEmpty(api.getStatus()) || StringUtils.isBlank(api.getStatus()))?"(status content type has not defined)":api.getStatus();

        resouceDocumentBuilder.addResourceInvalidInformation(String.format("Failed to create resource path: %s , supported verb: %s , consumed media type: %s, produce media type: %s , status code: %s",
                path,
                method,
                consume,
                produce,
                status));
    }

    private boolean resourceClassExist(String className, String resourceType, ResourceDocumentBuilder resouceDocumentBuilder){
        Class classToVerify = null;
        logger.info("Looking for interface " + resourceType);
        try{
            if(className == null || className.length()==0){
                resouceDocumentBuilder.updateResourceInvalidInformation(String.format("Resource class name %s is either empty or not defined !",className));
                return false;
            }
            classToVerify = Class.forName(className);
            Class[] interfaceImplemented = classToVerify.getInterfaces();

            // will pass the check for the timebeing if resourceType is "NA" but need a better way to handle and implement it !
            if(resourceType.equals("NA")) {
                return true;
            }
            //Check if the resource class has implemented correct interface i.e. IDynamicController/Get/POST
            for (Class interfaceImp : interfaceImplemented) {
                logger.info("Interface : " + interfaceImp.getName());

                if (resourceType.contains(interfaceImp.getName())) {
                    return true;
                }
            }
            //If not that means the resource doesn't implement the expected interface hence will log invalid information and reutrn false
            resouceDocumentBuilder.updateResourceInvalidInformation(String.format("Resource class name %s don't implement any one of %s interface hence this resource can not be hooked up while constructing resource ! \nPlease implement at least one of the interface in the controller  !",className,resourceType));
            return false;

        }catch (ClassNotFoundException cnfExp){
            resouceDocumentBuilder.updateResourceInvalidInformation(String.format("Resource class name %s doesn't exist !",
                    className));
            return false;
        }
    }

    private boolean isResourceASONObject(String resourceData,Api api){
        try {
            final String fileSeparator = System.getProperty("file.separator");
            final String baseDir = System.getProperty("user.dir");
            final String spoofFilePath = baseDir + fileSeparator + resourceData;
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jfactory = new MappingJsonFactory();
            JsonParser jParser = jfactory.createJsonParser(new File(spoofFilePath));
            String value = readFile(spoofFilePath);
            if(api != null){
                api.setResponseSpoof(value);
                api.setServiceMode(ServiceMode.SPOOF);
            }
            return true;
        }catch (IOException ioExp){
            return false;
        }

    }

    public static String readFile(String filename) {
        String result = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while (line != null) {
                sb.append(line);
                line = br.readLine();
            }
            result = sb.toString();
        } catch(Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /***
     * Build Api Document
     * @param resourceDocumentBuilder
     */
    private void buildAndRegisterApiDocument(ResourceDocumentBuilder resourceDocumentBuilder){
        Api apiDocumentResource = new Api();
        apiDocumentResource.setConsume("application/json");
        apiDocumentResource.setProduce("text/html");
        apiDocumentResource.setPath("docs");
        apiDocumentResource.setMethod("GET");
        String apiDocInfo = resourceDocumentBuilder.getResourceDocument();
        if(StringUtils.isEmpty(apiDocInfo) || apiDocInfo.equals(ResourceDocumentBuilder.docHeader + ResourceDocumentBuilder.docFooter)){
            StringBuilder sb = new StringBuilder();
            sb.append(apiDocInfo.replace(ResourceDocumentBuilder.docFooter,""));
            sb.append("<tr><td colspan=\"4\">There are no Api Listed in configuration file. Please refer to dynamic.resource.configuration section of Application.configuration file for more detail</tr></td>");
            sb.append(ResourceDocumentBuilder.docFooter);
            apiDocumentResource.setResponse(sb.toString());
        }else {
            apiDocumentResource.setResponse(apiDocInfo);
        }

        Resource resource = scanAndBuildResources(apiDocumentResource);
        registerResources(resource);
    }

    public static ContextResolver<MoxyJsonConfig> createMoxyJsonResolver() {
        final MoxyJsonConfig moxyJsonConfig = new MoxyJsonConfig();
        Map<String, String> namespacePrefixMapper = new HashMap<String, String>(1);
        namespacePrefixMapper.put(
                       "http://www.w3.org/2001/XMLSchema-instance", "xsi");
        moxyJsonConfig.setNamespacePrefixMapper(namespacePrefixMapper)
                        .setNamespaceSeparator(':');
        return moxyJsonConfig.resolver();
    }

    private boolean isValidMethod(String method){
        if(StringUtils.isEmpty(method) || StringUtils.isBlank(method)){
            return false;
        }
        for(Method m : Method.values()){
            if(method.equalsIgnoreCase(m.name())){
                return true;
            }
        }
        return false;
    }

    private boolean isValidPath(String path){
        if(StringUtils.isEmpty(path) || StringUtils.isBlank(path)){
            return false;
        }
        return true;
    }


}