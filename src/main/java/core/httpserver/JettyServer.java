package core.httpserver;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by nitina on 9/1/17.
 */
public class JettyServer implements IHttpServer {

    final static Logger logger = LoggerFactory.getLogger(JettyServer.class);

    @Override
    public void start(String serviceURI, ResourceConfig resource, String serviceName,int portNumber) {

        //ResourceConfig config = new ResourceConfig();
        //config.packages("");
        ServletHolder servlet = new ServletHolder(new ServletContainer(resource));


        Server server = new Server(portNumber);
        ServletContextHandler context = new ServletContextHandler(server, "/*");
        context.addServlet(servlet, String.format("/%s/*",serviceName));

        /*//1.Creating the resource handler
        ResourceHandler resourceHandler= new ResourceHandler();
        //2.Setting Resource Base
        resourceHandler.setResourceBase("src/main/resources/swagger-ui/index.html");
        //3.Enabling Directory Listing
        resourceHandler.setDirectoriesListed(true);
        //4.Setting Context Source
        ContextHandler contextHandler= new ContextHandler("/docs");
        //5.Attaching Handlers
        contextHandler.setHandler(resourceHandler);
        server.setHandler(contextHandler);*/


        /*ResourceHandler resource_handler = new ResourceHandler();
        resource_handler.setDirectoriesListed(true);
        resource_handler.setWelcomeFiles(new String[]{ "src/main/resources/swagger-ui/index.html" });

        resource_handler.setResourceBase(".");

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[] { resource_handler, new DefaultHandler() });
        server.setHandler(handlers);*/


        try {
            server.start();
            System.out.println(server.dump());
            printServiceStatus(serviceName,serviceURI);
            logger.info("Press Ctrl+C to stop");
            server.join();
        }catch (Exception exp){
            System.out.println(String.format("Exception occured : %s",exp.getMessage()));
        }finally {
            server.destroy();
        }
    }

    @Override
    public void stop() {

    }

    private void printServiceStatus(String serviceName,String serviceURI){
        logger.info("\n\n*************************************************************************************************");
        logger.info(String.format("***** Jersey app started with WADL available at "
                + "%sapplication.wadl *****\n***** Hit enter to stop it... *****", serviceURI));
        String serverAddressLine = serviceName + " is up and running";
        logger.info("***** "+serverAddressLine +
                " *****\n***** " + serviceURI + " *****" +
                " *****\n***** " + serviceURI + "docs/" + " *****" + " for api documentation");
        logger.info("*************************************************************************************************\n\n");
    }
}
