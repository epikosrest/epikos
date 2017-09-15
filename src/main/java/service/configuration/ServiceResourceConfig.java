/*
Copyright (c) [2017] [epikosrest@gmail.com]

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

package service.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wordnik.swagger.jaxrs.config.BeanConfig;
import com.wordnik.swagger.jersey.listing.ApiListingResourceJSON;
import com.wordnik.swagger.jersey.listing.JerseyApiDeclarationProvider;
import com.wordnik.swagger.jersey.listing.JerseyResourceListingProvider;
import core.enums.ServiceMode;
import core.engine.api.*;
import core.enums.SupportedResourceFileExtension;
import core.exception.EpikosException;
import core.filter.PostRequestFilter;
import core.filter.PreRequestFilter;
import core.intereceptor.RequestReaderIntereceptor;
import core.intereceptor.ResponseWriterInterceptor;
import lib.Utility;
import service.configuration.resource.IResource;
import service.configuration.resource.ResourceFactory;
import service.handler.RequestHandler;
import external.swagger.EpikosApiToSwaggerApiDocGenerator;
import org.apache.commons.lang3.StringUtils;
import org.glassfish.jersey.moxy.json.MoxyJsonConfig;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.model.Resource;
import org.glassfish.jersey.server.model.ResourceMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.parser.ParserException;

import javax.validation.constraints.NotNull;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.ContextResolver;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import io.swagger.jaxrs.config.BeanConfig;

/**
 * Created by nitina on 5/4/16.
 */
public  class ServiceResourceConfig extends ResourceConfig {

    IServiceMetaData metaData;
    final static Logger logger = LoggerFactory.getLogger(ServiceResourceConfig.class);
    final String SPOOF_URI = "/spoof";

    public ServiceResourceConfig(IServiceMetaData metaData) throws Exception{
        this.metaData = metaData;
        List<Api> apiList = new ArrayList<>();

        try {

            apiList = loadDynamicResource(metaData.getDynamicResourceConfigLocation());
            buildAndRegisterDynamicResource(apiList, metaData.getServiceURI());
            //SwaggerApiTemplateLoader.loadTemplate(); //Load and Construct Swagger API doc on the fly


        }catch (IOException ioExp){
            buildAndRegisterInvalidDocInfo("The dynamic resource api list file is not available. Please check the value dynamic.resource.configuration in Application.configuration file and make sure the file exist !\n" + ioExp.getMessage());

        }catch (ParserException parserExp){
            buildAndRegisterInvalidDocInfo("The dynamic resource api list is an invalid yml file. Please check the api list and fix following issue \n" + parserExp.getMessage());
        }

        register(new CrossDomainFilter());
        register(createMoxyJsonResolver()); //JSON support
        register(RequestReaderIntereceptor.class);
        register(ResponseWriterInterceptor.class); //Register to support gzip encoding in response
        register(PreRequestFilter.class);
        register(PostRequestFilter.class);
        register(JerseyApiDeclarationProvider.class);
        register(JerseyResourceListingProvider.class);
        register(ApiListingResourceJSON.class);
        packages(metaData.getResourcePackageName());
        beanConfiguration(metaData);
        EpikosApiToSwaggerApiDocGenerator.constructSwaggerApiDoc(apiList);
    }

    /**
     * Created by nitina on 4/25/16.
     */
    //ToDo: Investigate and research and verify if anything need to be added/changed and why to implement
    public static class CrossDomainFilter implements ContainerResponseFilter {

        @Override
        public void filter(ContainerRequestContext creq, ContainerResponseContext cres) {
            cres.getHeaders().add("Access-Control-Allow-Origin", "*");
            cres.getHeaders().add("Access-Control-Allow-Headers", "Content-Type, api_key, Authorization");
            cres.getHeaders().add("Access-Control-Allow-Credentials", "");
            cres.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, PATCH, OPTIONS");
            cres.getHeaders().add("Access-Control-Max-Age", "");
        }
    }

    /***
     * Function to integrate swagger api doc . The server uri + /api-docs provide the detail of swagger for api
     * ToDo:  at this time it is not working properly and need fix !
     * @param metaData
     */
    public static void beanConfiguration(IServiceMetaData metaData){
        BeanConfig beanConfig = new BeanConfig();
        beanConfig.setVersion("1.0");

        beanConfig.setResourcePackage("");
        beanConfig.setBasePath(metaData.getServiceURI());
        beanConfig.setDescription("The following document shows list API(s) that has been configured and will get handled by the service");
        beanConfig.setTitle("Document : List of API(s)");
        beanConfig.setScan(true);
        //System.out.println("base path " +beanConfig.getBasePath());
        //System.out.println("resource package " +beanConfig.getResourcePackage());
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
    private List<Api> loadDynamicResource(@NotNull List<String> dynamicResourceFileNameList) throws IOException,ParserException,EpikosException{

        List<Api> dynamicApi = new ArrayList<>();
        for(String dynamicResourceFileName : dynamicResourceFileNameList){

            //Load dynamic resource information from file specified as value of key dynamic.resource.configuration defined in Application.Configuraiton
            SupportedResourceFileExtension fileExtensionConfigured = SupportedResourceFileExtension.getFileExtension(dynamicResourceFileName);
            if(fileExtensionConfigured.equals(SupportedResourceFileExtension.UNKNOWN)){
                logger.warn(String.format("File extension %s is not supported",dynamicResourceFileName));
                continue;
            }


            IResource resourceToExtractApiFrom = ResourceFactory.getResource(fileExtensionConfigured);
            List<Api> dynamicApiConfigured = resourceToExtractApiFrom.getApiList(dynamicResourceFileName);

            if(dynamicApiConfigured !=null){
                dynamicApi.addAll(dynamicApiConfigured);
            }else{
                logger.warn(String.format("#### File name %s does not contain api",dynamicResourceFileName));
            }
        }

        return dynamicApi;

    }

    private void buildAndRegisterDynamicResource(List<Api> apiList,String serviceURI) throws EpikosException{
        ResourceDocumentBuilder resourceDocumentBuilder = new ResourceDocumentBuilder();
        List<Resource> validResourceList = new ArrayList<>();
        if(apiList != null) {
            Resource resource;
            for (Api api : apiList) {

                if (ApiValidator.validateDynamicResource(api, resourceDocumentBuilder)) {
                    resource = scanAndBuildResources(api);
                    validResourceList.add(resource);
                    registerResources(resource);

                    //we will register package of each controller so that Jersey can route the request to this controller !
                    String fqdnController = api.getController();
                    if(fqdnController!= null) {
                        int indexOfClassNameStaringPoint = fqdnController.lastIndexOf('.');
                        packages(fqdnController.substring(0,indexOfClassNameStaringPoint));
                    }
                    //We will construct spoof end point for same api
                    //Will check if the end point created is already a Spoof api or not. If yes then we will not create
                    //spoof api . Also will check whether spoof response (JSON file) has been provided or not.
                    // If not then will skip
                    if(api.getServiceMode() != ServiceMode.SPOOF && api.getResponseSpoof() != null){
                        Api spoofApi = new SpoofApi();
                        spoofApi.setConsume(api.getConsume());
                        spoofApi.setMethod(api.getMethod());
                        spoofApi.setPath(api.getPath() + SPOOF_URI);
                        spoofApi.setProduce(api.getProduce());
                        spoofApi.setRequest(api.getRequest());
                        spoofApi.setResponse(api.getResponse());
                        spoofApi.setResponseSpoof(api.getResponseSpoof());
                        spoofApi.setStatus(api.getStatus());

                        spoofApi.setServiceMode(ServiceMode.SPOOF);
                        resource = scanAndBuildResources(spoofApi);
                        validResourceList.add(resource);
                        registerResources(resource);
                    }
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
     * Build Api Document
     * @param resourceDocumentBuilder
     */
    private void buildAndRegisterApiDocument(ResourceDocumentBuilder resourceDocumentBuilder){
        Api apiDocumentResource = new GetApi();
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

}