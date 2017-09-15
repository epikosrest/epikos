package service.configuration.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import core.engine.api.Api;
import core.engine.api.DynamicResourceContainer;
import core.exception.EpikosException;
import lib.Utility;
import org.yaml.snakeyaml.parser.ParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by nitina on 8/26/17.
 */
public class JsonResource implements IResource {


    @Override
    public List<Api> getApiList(String resourceFileName) throws IOException,ParserException,EpikosException {
        DynamicResourceContainer config = null;
        InputStream in = null;

        try {
            in = Utility.getFileStream(resourceFileName);
            ObjectMapper mapper = new ObjectMapper();
            config = mapper.readValue(in, DynamicResourceContainer.class);
        }finally {
            if(in != null){
                in = null;
            }
        }

        return config.getApiList();
    }
}
