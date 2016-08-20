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

import core.service.IServiceMetaData;
import core.service.ServiceMetaData;
import core.service.ServiceResourceConfig;
import core.lib.configuration.Configuration;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.NetworkListener;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.net.URI;

public class Service implements IService{

	protected HttpServer grizzlyServer = null;

	public static void main(String[] args) throws Exception {

		Service service = new Service();
		service.start();

	}

	private final void run() throws Exception {

        Configuration config = loadConfigurationProperties();
        IServiceMetaData metaData = loadMetaData(config);
        ServiceResourceConfig resource = new ServiceResourceConfig(metaData);
        startServer(metaData.getServiceURI(),resource);
        printServiceStatus(metaData.getServiceName(),metaData.getServiceURI());
	}


	@Override
	public final void start() throws Exception{
		run();
		System.out.println("Press Enter to stop");
		System.in.read();
		stop();

	}

	@Override
	public final void stop() throws Exception{
		if (grizzlyServer == null) {
			System.out.println("Service already stopped !");
			return;
		}

		HttpServer tempGrizzlyServer = grizzlyServer;
		grizzlyServer = null;
		tempGrizzlyServer.shutdown();
		System.out.println("Server stopped");
	}

	private Configuration loadConfigurationProperties() {
        Configuration configuration = new Configuration();

		if (configuration.getProperties() == null) {
			System.out.println(configuration.getErrorMessage());
		}
		System.out.println("Number of properites : " + configuration.getProperties().size());
        return configuration;
	}

    private IServiceMetaData loadMetaData(Configuration configuration) throws Exception{
        IServiceMetaData metaData = new ServiceMetaData(configuration);
        return metaData;
    }

    private void startServer(String serviceURI, ResourceConfig resource) throws IOException{
        grizzlyServer = GrizzlyHttpServerFactory.createHttpServer(
                URI.create(serviceURI), resource, false);
        NetworkListener listener = grizzlyServer.getListener("grizzly");
		grizzlyServer.addListener(listener);
        grizzlyServer.getServerConfiguration().setJmxEnabled(true);
        grizzlyServer.start();
    }

    private void printServiceStatus(String serviceName,String serviceURI){
        System.out.println("\n\n*************************************************************************************************");
        System.out.println(String.format("***** Jersey app started with WADL available at "
                + "%sapplication.wadl *****\n***** Hit enter to stop it... *****", serviceURI));
        String serverAddressLine = serviceName + " is up and running";
        System.out.println("***** "+serverAddressLine +
				" *****\n***** " + serviceURI + " *****" +
				" *****\n***** " + serviceURI + "docs" + " *****" + " for api documentation");
        System.out.println("*************************************************************************************************\n\n");
    }

}
