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


package service;

import core.enums.HttpServiceType;
import core.exception.EpikosException;
import core.httpserver.IHttpServer;
import core.httpserver.ServerFactory;
import service.configuration.ConfigurationFactory;
import service.configuration.ConfigurationType;
import service.configuration.IConfiguration;
import service.configuration.IServiceMetaData;
import service.configuration.ServiceMetaData;
import service.configuration.ServiceResourceConfig;
import org.glassfish.grizzly.http.server.HttpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Service{

	protected HttpServer grizzlyServer = null;

	final static Logger logger = LoggerFactory.getLogger(Service.class);

	public static void main(String[] args) throws Exception {

        run();

	}

	private final static void run() throws Exception {

        spitBannerText();
        IConfiguration appConfiguration = loadConfigurationProperties();
        IServiceMetaData metaData = new ServiceMetaData(appConfiguration);
        ServiceResourceConfig resource = new ServiceResourceConfig(metaData);
        //startServer(metaData.getServiceURI(),resource,metaData.getServiceName());
        //startJettyServer(resource,metaData.getServiceName(),metaData.getServiceURI(),Integer.parseInt(metaData.getPortNumber()==null?"8080":metaData.getPortNumber()));
        try {
            IHttpServer server = ServerFactory.getHttpServer(HttpServiceType.GRIZZLY);
            server.start(metaData.getServiceURI(),resource,metaData.getServiceName(),8080);
        }catch (EpikosException epikosExp){
            logger.error(epikosExp.getMessage());
            throw epikosExp;
        }
    }


	private static IConfiguration loadConfigurationProperties() {
        IConfiguration configuration = ConfigurationFactory.getConfiguration(ConfigurationType.APP);

		if (configuration.getProperties() == null) {
            logger.info(configuration.getErrorMessage());
		}
        logger.info("Number of properites : " + configuration.getProperties().size());
        return configuration;
	}


	private static void spitBannerText(){
        String bannerText="";
		logger.info("Loading .....");
		try {


            String resourceBannerFullPath = System.getProperty("user.dir") + System.getProperty("file.separator") +
                    "src" + System.getProperty("file.separator") +
                    "main" + System.getProperty("file.separator") +
            "resources"+ System.getProperty("file.separator") +"banner.txt";
            bannerText = new String(Files.readAllBytes(Paths.get(resourceBannerFullPath)));
            logger.info("\n" + bannerText + "\n");

            }catch (IOException exp){
			//We don't care if it failed !
		}
    }

}
