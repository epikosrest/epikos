package core.dynamic.resources.domain;


import core.domain.enums.ServiceMode;
import lombok.Data;

/**
 * Created by nitina on 5/8/16.
 */
@Data
public class Api {
    String consume;
    String produce;
    String path;
    String method;
    String request;
    String response;
    String controller;
    String responseSpoof;
    ServiceMode serviceMode;
}
