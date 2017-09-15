package service.configuration.resource;

import core.engine.api.Api;
import core.exception.EpikosException;
import org.yaml.snakeyaml.parser.ParserException;

import java.io.IOException;
import java.util.List;

/**
 * Created by nitina on 8/26/17.
 */
public interface IResource {
    List<Api> getApiList(String resourceFileName) throws IOException,ParserException,EpikosException;
}
