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


package restserver;

import core.lib.Utility;
import core.service.IServiceMetaData;
import core.service.ServiceMetaData;
import core.service.ServiceResourceConfig;
import core.lib.configuration.Configuration;
import org.glassfish.grizzly.http.server.CLStaticHttpHandler;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.NetworkListener;
import org.glassfish.grizzly.http.server.StaticHttpHandler;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class Service implements IService{

	protected HttpServer grizzlyServer = null;

	final static Logger logger = LoggerFactory.getLogger(Service.class);

	public static void main(String[] args) throws Exception {

        try {
            Service service = new Service();
            service.start();
        }catch (Exception exp){
            System.out.println("Service crashed : " + exp.getMessage());
            exp.printStackTrace();
        }

	}

	private final void run() throws Exception {

        spitBannerText();
        Configuration config = loadConfigurationProperties();
        IServiceMetaData metaData = loadMetaData(config);
        ServiceResourceConfig resource = new ServiceResourceConfig(metaData);
        startServer(metaData.getServiceURI(),resource);
        printServiceStatus(metaData.getServiceName(),metaData.getServiceURI());
	}


	@Override
	public final void start() throws Exception{
		run();
        //logger.info("Press Enter to stop");
		System.in.read();
		//stop();

	}

	@Override
	public final void stop() throws Exception{
		if (grizzlyServer == null) {
            logger.info("Service already stopped !");
			return;
		}

		HttpServer tempGrizzlyServer = grizzlyServer;
		grizzlyServer = null;
		tempGrizzlyServer.shutdown();
        logger.info("Server stopped");
	}

	private Configuration loadConfigurationProperties() {
        Configuration configuration = new Configuration();

		if (configuration.getProperties() == null) {
            logger.info(configuration.getErrorMessage());
		}
        logger.info("Number of properites : " + configuration.getProperties().size());
        return configuration;
	}

    private IServiceMetaData loadMetaData(Configuration configuration) throws Exception{
        IServiceMetaData metaData = new ServiceMetaData(configuration);
        return metaData;
    }

    private void startServer(String serviceURI, ResourceConfig resource) throws IOException{

        //String[] pack = {"controller", "com.wordnik.swagger.jersey.listing"};
        //resource.packages(pack);

        grizzlyServer = GrizzlyHttpServerFactory.createHttpServer(
                URI.create(serviceURI), resource, false);
        NetworkListener listener = grizzlyServer.getListener("grizzly");
		grizzlyServer.addListener(listener);
        grizzlyServer.getServerConfiguration().setJmxEnabled(true);


        //Few ways to display static content
        //1. Following code won't work for .jar , need investigation
        //CLStaticHttpHandler staticHttpHandler = new CLStaticHttpHandler(Service.class.getClassLoader(), "swagger-ui/");
        //Bug: Grizzly currently has a bug as how it handles paths for static resources
        // It need trailing slash at the end to create static handler e.g. /docs/ as shown below
        //Also you can put "/" to map swagger-ui to root of url e.g. to map just http://localhost:8080
        //grizzlyServer.getServerConfiguration().addHttpHandler(staticHttpHandler, "/docs/");

        //2. Following technique won't work for .jar , although jar under target folder has been provided, need investigation
        /*
        grizzlyServer.getServerConfiguration().addHttpHandler(
                new CLStaticHttpHandler(new URLClassLoader(new URL[] {
                        new File("target/EpikosRestService-0.0.7.1-jar-with-dependencies.jar").toURI().toURL()}), "swagger-ui/"),
                "/docs");
                */

        //3. StaticHttpHandler is working while running using target/bin/Service script (which is being generated by plugin in pom)
        // but this still doesn't work when jar is executed directly (need investigation). Since it is working for script option
        // will keep this method for the time being.
        grizzlyServer.getServerConfiguration().addHttpHandler(
                new StaticHttpHandler("src/main/resources/swagger-ui/"), "/docs/");


        grizzlyServer.start();

    }

    private void printServiceStatus(String serviceName,String serviceURI){
        logger.info("\n\n*************************************************************************************************");
        logger.info(String.format("***** Jersey app started with WADL available at "
                + "%sapplication.wadl *****\n***** Hit enter to stop it... *****", serviceURI));
        String serverAddressLine = serviceName + " is up and running";
        logger.info("***** "+serverAddressLine +
				" *****\n***** " + serviceURI + " *****" +
				" *****\n***** " + serviceURI + "docs" + " *****" + " for api documentation");
        logger.info("*************************************************************************************************\n\n");
    }

	private void spitBannerText(){
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
