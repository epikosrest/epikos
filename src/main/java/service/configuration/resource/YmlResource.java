package service.configuration.resource;

import core.engine.api.Api;
import core.engine.api.ApiFactory;
import core.engine.api.DynamicResourceContainer;
import core.exception.EpikosException;
import lib.Utility;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.parser.ParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nitina on 8/26/17.
 */
public class YmlResource implements IResource {

    @Override
    public List<Api> getApiList(String resourceFileName) throws IOException,ParserException,EpikosException {
        Yaml yaml = new Yaml();
        DynamicResourceContainer config = null;

        InputStream in = null;
        try {
            in = Utility.getFileStream(resourceFileName);
            config = yaml.loadAs(in, DynamicResourceContainer.class);
        }finally {
            if(in != null){
                in.close();
            }
        }

        final List<Api> concretApiList = new ArrayList<>();
        if(config.getApiList()!=null && !config.getApiList().isEmpty()){
            for(Api api : config.getApiList()){
                concretApiList.add(ApiFactory.constructApiByType(api));
            }
            config.setApiList(concretApiList);
        }

        return config.getApiList();
    }
}
