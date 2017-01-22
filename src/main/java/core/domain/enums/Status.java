package core.domain.enums;


import core.exception.EpikosException;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by nitina on 9/30/16.
 */
@Getter
public enum Status {

    //Ref:https://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html
    OK(200),CREATED(201),ACCEPTED(202),NONAUTHORITATIVE(203),NOCONTENT(204),RESETCONTENT(205),PARTIALCONTENT(206),
    MOVEDPERMANENTLY(301),FOUND(302),SEEOTHER(303),NOTMODIFIED(304),USEPROXY(305),UNUSED(306),TEMPORARYREDIRECT(307),
    BADREQUEST(400),UNAUTHORIZED(401),PAYMENTREQUIRED(402),FORBIDDEN(403),NOTFOUND(404),METHODNOTALLOWED(405),NOTACCEPTABLE(406),
    PROXYAUTHINTACTIONREQUIRED(407),REQUESTTIMEOUT(408),CONFLICT(409),GONE(410),LENGTHREQUIRED(411),PRECONDITIONFAILED(412),
    REQUESTENTITYTOOLARGE(413),REQUESTURITOOLONG(414),UNSUPPORTEDMEDIATYPE(415),REQUESTEDRANGENOTSATISFIABLE(416), EXPECTATIONFAILED(417),
    INTERNALSERVERERROR(500),NOTIMPLEMENTED(501),BADGATEWAY(502),SERVICEUNAVAILABLE(503),GATEWAYTIMEOUT(504),
    HTTPVERSIONNOTSUPPORTED(505),UNKNOWN(999);

    int status;
    Status(int status){
        this.status = status;
    }

    static Map<String,Status> lookup = new HashMap<>();

    static {
        for(Status key : values()){
            lookup.put(key.name(),key);
        }
    }

    public static Integer getStatusCode(String status) throws EpikosException{
       try {
           Integer intStatus = Integer.parseInt(status);
           if (intStatus>=0) {
                return intStatus;
           }
       }catch (NumberFormatException nfExp){
           return lookup.get(status).getStatus();
       }

        throw new EpikosException(String.format("Status %s is not a valid status code",status));

    }
}
