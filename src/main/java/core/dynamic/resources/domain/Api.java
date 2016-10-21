package core.dynamic.resources.domain;


import core.domain.enums.ServiceMode;
import core.domain.enums.Status;

/**
 * Created by nitina on 5/8/16.
 */
public class Api {
    String consume;
    String produce;
    String path;
    String method;
    String request;
    String response;
    String controller;
    String status;
    String responseSpoof;
    ServiceMode serviceMode;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResponseSpoof() {
        return responseSpoof;
    }

    public void setResponseSpoof(String responseSpoof) {
        this.responseSpoof = responseSpoof;
    }

    public ServiceMode getServiceMode() {
        return serviceMode;
    }

    public void setServiceMode(ServiceMode serviceMode) {
        this.serviceMode = serviceMode;
    }
}
