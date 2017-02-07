package core.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by nitina on 11/2/16.
 */
public class EpikosException extends Exception {

    Logger logger = LoggerFactory.getLogger(EpikosException.class);
    public EpikosException(String message){
        super(message);
        logger.error(message);
    }

    public EpikosException(String message,Exception cause){
        super(message,cause);
        logger.error(message,cause);
    }
}
