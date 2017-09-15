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

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

final public class ServiceMetaData implements  IServiceMetaData{

    final static Logger logger = LoggerFactory.getLogger(ServiceMetaData.class);

    final String uriPath = "/";
    String hostName;
    String portNumber;
    String serviceName;
    String[] resourcePackageName;
    String serviceURI;
    List<String> dynamicResourceConfigLocation;

    final String DEFAULT_PORT_NUMBER = "8080";
    final String DEFAULT_SERVICE_NAME = "epikos";

    public ServiceMetaData(IConfiguration config) throws Exception{
        setMetaData(config);
    }

    @Override
    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    @Override
    public String getPortNumber() {
        return portNumber;
    }

    public void setPortNumber(String portNumber) {
        this.portNumber = portNumber;
    }

    @Override
    public String getUriPath() {
        return uriPath;
    }

    @Override
    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String[] getResourcePackageName() {
        return resourcePackageName;
    }

    public void setResourcePackageName(String[] resourcePackageName) {
        this.resourcePackageName = resourcePackageName;
    }

    public String getServiceURI() {
        return serviceURI;
    }

    public void setServiceURI(String serviceURI) {
        this.serviceURI = serviceURI;
    }

    @Override
    public List<String> getDynamicResourceConfigLocation() {
        return dynamicResourceConfigLocation;
    }

    /*public void setDynamicResourceConfigLocation(List<String> dynamicResourceConfigLocation) {
        this.dynamicResourceConfigLocation = dynamicResourceConfigLocation;
    }*/

    void setMetaData(IConfiguration config) throws Exception{
        if(config == null || config.getProperties()==null){
            throw new RuntimeException("Configuration must not be null. Please check the configuration file is present and properly configured");
        }
        String portNumber =
                config.getProperties().getProperty("port")!=null?
                config.getProperties().getProperty("port"):System.getenv("PORT") != null?
                System.getenv("PORT"): DEFAULT_PORT_NUMBER;

        hostName = "localhost";//InetAddress.getLocalHost().getHostName();
        //portNumber = portNumber==null?DEFAULT_PORT_NUMBER:portNumber;
        serviceName = config.getProperties().getProperty("service.name") == null ?DEFAULT_SERVICE_NAME:config.getProperties().getProperty("service.name");
        resourcePackageName = config.getProperties().getProperty("resource.package.name") == null?
                                null:config.getProperties().getProperty("resource.package.name").split(";");
        serviceURI = "http://" + hostName + ":" + portNumber +uriPath+ serviceName+ uriPath;

        String resourceConfigFileLocation = config.getProperties().getProperty("dynamic.resource.configuration");

        logger.info("\n#####################################################################################\n");
        logger.info("##### Service name : " + serviceName + " #####");
        logger.info("##### Service URI : " + serviceURI + " #####");
        logger.info("##### Resources being read/fetched from package(s)\n");
        if(resourcePackageName != null) {
            for (String resourcePkgName : resourcePackageName) {
                logger.info(resourcePkgName + "\t");
            }
        }
        if(!StringUtils.isBlank(resourceConfigFileLocation)){
            dynamicResourceConfigLocation = Arrays.asList( config.getProperties().getProperty("dynamic.resource.configuration").split(","));
            logger.info("\n##### Dynamic Resource Configuration file name : '" + resourceConfigFileLocation+ "' #####");
        }else {
            logger.warn("\n#### Resource File Location has not been provided hence service will not be able to create Dynamic API");
        }
        logger.info("\n#####################################################################################\n");

    }
}
