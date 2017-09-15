package core.httpserver;

import core.enums.HttpServiceType;
import core.exception.EpikosException;

/**
 * Created by nitina on 9/1/17.
 */
public class ServerFactory {

    public static IHttpServer getHttpServer(HttpServiceType serviceType) throws EpikosException{
        if(HttpServiceType.GRIZZLY.equals(serviceType)){
            return new GrizzlyServer();
        }else if(HttpServiceType.JETTY.equals(serviceType)){
            return new JettyServer();
        }else{
            throw new EpikosException("Unknown http server type requested !");
        }
    }
}
