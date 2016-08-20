package core.dynamicrestresources.domain;


/**
 * Created by nitina on 5/8/16.
 */
public class DynamicResourceMetaData {
    String consume;
    String produce;
    String path;
    String method;
    String request;
    String response;
    String controller;
    String responseSpoof;

    public String getConsume() {
        return consume;
    }

    public void setConsume(String consume) {
        this.consume = consume;
    }

    public String getProduce() {
        return produce;
    }

    public void setProduce(String produce) {
        this.produce = produce;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getController() {
        return controller;
    }

    public void setController(String controller) {
        this.controller = controller;
    }

    public String getResponseSpoof() {
        return responseSpoof;
    }

    public void setResponseSpoof(String responseSpoof) {
        this.responseSpoof = responseSpoof;
    }

}
