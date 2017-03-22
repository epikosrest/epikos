package example.dynamic_controller;

import core.dynamic.resources.*;
import core.exception.EpikosException;
import example.request.HelloRequest;
import example.response.HelloResponse;

import javax.ws.rs.core.MultivaluedMap;

/**
 * Created by nitina on 5/11/16.
 */
public class DynamicHelloController implements IDynamicResourceController {

    public Object process(IDynamicRequestPOST dynamicRequest) throws EpikosException {
        try {
            HelloRequest helloRequest = dynamicRequest.getRequest(HelloRequest.class);
            MultivaluedMap<String, String> pathParam = dynamicRequest.getPathParams();

            HelloResponse resp = new HelloResponse();
            resp.setResponseString("Hello " + helloRequest.getName());
            return resp;
        }catch (Exception ioExp){
            //ToDo: log and return proper error msg
        }
        return  null;  //ToDO: need better way to handle and return value
    }

    public Object process(IDynamicRequestGET dynamicRequestGET) {

        HelloResponse getResponse = new HelloResponse();
        getResponse.setResponseString("This is response entity for GET request from Swagger for Dynamic API example");

        return getResponse;
    }

    @Override
    public Object process(IDynamicRequestDELETE dynamicRequest) throws EpikosException {
        return null;
    }

    @Override
    public Object process(IDynamicRequestPUT dynamicRequest) throws EpikosException {
        return null;
    }


    @Override
    public Object process(IDynamicRequestPATCH dynamicRequest) throws EpikosException {
        return null;
    }
}
