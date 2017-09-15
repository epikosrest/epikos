package service.configuration;

/**
 * Created by nitina on 8/26/17.
 */
public class ConfigurationFactory {

    public static IConfiguration getConfiguration(ConfigurationType configurationType){
        if(ConfigurationType.APP.equals(configurationType)){
            return AppConfiguration.getAppConfiguration;
        }


        return null;
    }
}
