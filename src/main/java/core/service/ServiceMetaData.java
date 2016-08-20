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

import core.lib.configuration.Configuration;

import java.net.InetAddress;

final public class ServiceMetaData implements  IServiceMetaData{

    final String uriPath = "/";
    String hostName;
    String portNumber;
    String serviceName;
    String[] resourcePackageName;
    String serviceURI;
    String dynamicResourceConfigLocation;

    public ServiceMetaData(Configuration config) throws Exception{
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
    public String getDynamicResourceConfigLocation() {
        return dynamicResourceConfigLocation;
    }

    public void setDynamicResourceConfigLocation(String dynamicResourceConfigLocation) {
        this.dynamicResourceConfigLocation = dynamicResourceConfigLocation;
    }

    void setMetaData(Configuration config) throws Exception{
        if(config == null){
            throw new RuntimeException("Configuration must not be null. Please check the configuration file is present and properly configured");
        }
        String portNumber = config.getProperties().getProperty("port");
        hostName = InetAddress.getLocalHost().getHostName();
        portNumber = portNumber==null?"8085":portNumber;
        serviceName = config.getProperties().getProperty("service.name");
        resourcePackageName = config.getProperties().getProperty("resource.package.name").split(";");
        serviceURI = "http://" + hostName + ":" + portNumber + uriPath;
        dynamicResourceConfigLocation = config.getProperties().getProperty("dynamic.resource.configuration");

        System.out.println("\n#####################################################################################\n");
        System.out.println("##### Service name : " + serviceName + " #####");
        System.out.println("##### Service URI : " + serviceURI + " #####");
        System.out.println("##### Resources being read/fetched from package(s)\n");
        for (String resourcePkgName:resourcePackageName) {
            System.out.print(resourcePkgName + "\t");
        }
        System.out.println("\n##### Dynamic Resource Configuration file name : '" + dynamicResourceConfigLocation+ "' #####");
        System.out.println("\n#####################################################################################\n");

    }
}
