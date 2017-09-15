package service.configuration;

import java.util.Properties;

/**
 * Created by nitina on 8/26/17.
 */
public interface IConfiguration {
    Properties getProperties();
    public String getErrorMessage();
}
