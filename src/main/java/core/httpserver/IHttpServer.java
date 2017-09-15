package core.httpserver;

import org.glassfish.jersey.server.ResourceConfig;

/**
 * Created by nitina on 9/1/17.
 */
public interface IHttpServer {

    void start(String serviceURI, ResourceConfig resource, String serviceName,int portNumber);
    void stop();
}
