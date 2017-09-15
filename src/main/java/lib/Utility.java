/*
Copyright (c) [2017] [epikosrest@gmail.com]

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

package lib;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.enums.ApiValidationStatusCode;
import core.enums.Method;
import core.enums.Status;
import core.engine.api.ApiParam;
import core.engine.api.IMethod;
import core.engine.api.ResourceDocumentBuilder;
import core.error.ApiValidationStatus;
import core.exception.EpikosException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.parser.ParserException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

public final class Utility {

	final static Logger logger = LoggerFactory.getLogger(Utility.class);


	final static String CONFIGURATION_FILE = "config.file.name";
	final static String CONFIGURATION_FOLDER = "config.file.location";
	final static String DEFAULT_CONFIGURATION_FOLDER_NAME = "Config";
	static String configFileName;
	static String configFolder;



	public static String getConfigFileFullPath() {
		final String fileSeparator = System.getProperty("file.separator");
		StringBuilder configFileNameAndFolderPath = new StringBuilder();
		// We will try to get name of configuration file if it's been supplied
		// as argument while starting service
		configFileName = System.getProperty(CONFIGURATION_FILE);
		if (configFileName == null) {
			// Assign default configuration name
			configFileName = "Application.configuration";
		}
		configFileNameAndFolderPath.append("Config file name : "
				+ configFileName);

		configFolder = System.getProperty(CONFIGURATION_FOLDER);
		if (configFolder == null) {
			configFolder = System.getProperty("user.dir") + fileSeparator
					+ DEFAULT_CONFIGURATION_FOLDER_NAME;
		} else {
			configFolder = System.getProperty("user.dir") + fileSeparator
					+ configFolder;
		}
		configFileNameAndFolderPath.append("\nConfig folder name : "
				+ configFolder);
        logger.info(configFileNameAndFolderPath.toString());
		return configFolder + fileSeparator + configFileName;
	}

	public static String getConfigFileName() {
		if (configFileName == null) {
			configFileName = System.getProperty(CONFIGURATION_FILE);
			if (configFileName == null) {
				// Assign default configuration name
				configFileName = "Application.configuration";
			}
		}
		return configFileName;
	}

	public static String getConfigFolder() {
		if (configFolder == null) {
			final String fileSeparator = System.getProperty("file.separator");
			configFolder = System.getProperty(CONFIGURATION_FOLDER);
			if (configFolder == null) {
				configFolder = System.getProperty("user.dir") + fileSeparator
						+ DEFAULT_CONFIGURATION_FOLDER_NAME;
			} else {
				configFolder = System.getProperty("user.dir") + fileSeparator
						+ configFolder;
			}
		}
		return configFolder;
	}
	
	public static String getTimeStamp(long milliSeconds) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(milliSeconds);
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss:SSS");
		return formatter.format(cal.getTime());
	}

	public static String readFile(String filename) {
		String result = "";
		final String fileSeparator = System.getProperty("file.separator");
		final String baseDir = System.getProperty("user.dir");
		final String spoofFilePath = baseDir + fileSeparator + filename;
		try {
			BufferedReader br = new BufferedReader(new FileReader(spoofFilePath));
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();
			while (line != null) {
				sb.append(line);
				line = br.readLine();
			}
			result = sb.toString();
		} catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static boolean isResourceAJSONObject(String resourceData){

		if(resourceData == null){
			return false;
		}

		try {
			final String fileSeparator = System.getProperty("file.separator");
			final String baseDir = System.getProperty("user.dir");
			final String spoofFilePath = baseDir + fileSeparator + resourceData;

			ObjectMapper mapper = new ObjectMapper();
			JsonFactory jfactory = new MappingJsonFactory();
			JsonParser jParser = jfactory.createJsonParser(new File(spoofFilePath));

			return true;
		}catch (IOException ioExp){
			return false;
		}

	}

	public static ApiValidationStatus isValidStatusCode(String status) throws EpikosException{
        ApiValidationStatus apiValidationStatus = new ApiValidationStatus(ApiValidationStatusCode.Valid);

		if(StringUtils.isEmpty(status) || StringUtils.isBlank(status)){
			return  new ApiValidationStatus(ApiValidationStatusCode.InvalidStatus,"Empty status code is not valid");
		}

		Integer statusCode = Status.getStatusCode(status);
		if(statusCode == null){
			try {
				Status statusToVerify = Status.valueOf(status);
			}catch (Exception exp){
				logger.error(exp.getMessage());
				return  new ApiValidationStatus(ApiValidationStatusCode.InvalidStatus,String.format("Status code %s is not valid",status));
			}

		}
		return apiValidationStatus;
	}

	public static ApiValidationStatus isValidMethod(String method){
        ApiValidationStatus methodIsValid = doesMethodExist(method);
        //Will check if it is a custom method or not
        if(!methodIsValid.getCode().equals(ApiValidationStatusCode.Valid)){
            boolean isAValidCustomMethod = Utility.hasResourceTypeImplementInterfaceListed(method, IMethod.class.getTypeName());
            if(!isAValidCustomMethod){
                methodIsValid = new ApiValidationStatus(ApiValidationStatusCode.InvalidMethod,String.format("Method : %s is invalid as it has been defined as custom method but has not implemented interface IMethod",method));
            }
        }

		return methodIsValid;
	}

    private static ApiValidationStatus doesMethodExist(String method){

        if(!(StringUtils.isEmpty(method) && StringUtils.isBlank(method))) {
            for (Method m : Method.values()) {
                if (method.equalsIgnoreCase(m.name())) {
                    return new ApiValidationStatus(ApiValidationStatusCode.Valid);
                }
            }
        }
        return new ApiValidationStatus(ApiValidationStatusCode.InvalidMethod,String.format("Method : %s is invalid",method));
    }

	public static ApiValidationStatus isValidPath(String path){
		if(StringUtils.isEmpty(path) || StringUtils.isBlank(path)){
			return new ApiValidationStatus(ApiValidationStatusCode.InvalidPath,String.format("Empty Path is not valid"));
		}
		return new ApiValidationStatus(ApiValidationStatusCode.Valid);
	}

	/*
	This method validate consume and produce filed of api. It can be either application/json or application/xml
	 */
	public static ApiValidationStatus isValidContentType(String contentType){

		if(StringUtils.isEmpty(contentType) || StringUtils.isBlank(contentType)){
            return new ApiValidationStatus(ApiValidationStatusCode.InvalidContentType,String.format("Content type %s provided is not valid",contentType));
		}else if("application/json".equalsIgnoreCase(contentType) || "application/xml".equalsIgnoreCase(contentType)){
            return new ApiValidationStatus(ApiValidationStatusCode.Valid);
		}

        return new ApiValidationStatus(ApiValidationStatusCode.InvalidContentType,String.format("Content type %s provided is not valid and is not supported",contentType));

	}

	public static ApiValidationStatus doesPathParamsMatchWithApiPathParam(String apiPath, List<ApiParam> apiParmList){

		//If pathParms is null or empty then don't care for further validation
        if(apiParmList == null || apiParmList.isEmpty()){
            return new ApiValidationStatus(ApiValidationStatusCode.Valid);
        }

        List<String> pathParams = apiParmList.stream().filter(p->p.getParam()!=null).map(ApiParam::getParam).collect(Collectors.toList());

        //Lets first parse all param in api path
        List<String> parameterInPath = new ArrayList<>();
        int startIndex =0;
        int endIndex =0;
        boolean foundOpeningBracket = false;
        boolean foundClosingBracket = false;

        for(int ind =0;ind<apiPath.length();ind++){
            startIndex = endIndex;
            if(apiPath.charAt(ind) == '{'){
                foundOpeningBracket = true;
                foundClosingBracket = false;
                startIndex = ind;
                do {
                    if(apiPath.charAt(ind) == '}'){
                        foundClosingBracket = true;
                        endIndex = ind;
                        parameterInPath.add(apiPath.substring(startIndex+1,endIndex));
                        break;
                    }
                }while(++ind < apiPath.length());
                endIndex =ind;

                if(endIndex >= apiPath.length()){
                    break;
                }

                if(foundOpeningBracket && !foundClosingBracket){
                    return new ApiValidationStatus(ApiValidationStatusCode.InvalidPathParam,String.format("Invalid api path : The path has not been constructed properly and missing closing '}' bracket"));
                }

                //reset
                foundOpeningBracket=false;
                foundClosingBracket = false;

            }
        }

        if(foundOpeningBracket && !foundClosingBracket){
            return new ApiValidationStatus(ApiValidationStatusCode.InvalidPathParam,String.format("Invalid api path %s : The path has not been constructed properly and missing closing '}' bracket",apiPath));
        }

        if(!parameterInPath.isEmpty()){
            if(parameterInPath.size() != pathParams.size()){
                //Number Path param parsed and path param included in api mismatched
                return new ApiValidationStatus(ApiValidationStatusCode.InvalidPathParam,String.format("Invalid api path %s : The path has not been constructed properly and missing closing '}' bracket",apiPath));

            }
            for(int ind =0;ind<pathParams.size();ind++){
                if(!pathParams.get(ind).equals(parameterInPath.get(ind))){
                    //Path param parsed and path param included in api mismatched
                    return new ApiValidationStatus(ApiValidationStatusCode.InvalidPathParam,String.format("Path parameter in api path %s miss match with api document path list",apiPath));
                }
            }
        }

        return new ApiValidationStatus(ApiValidationStatusCode.Valid);
    }

	public static boolean doesResourceClassExist(String className, String resourceType, ResourceDocumentBuilder resouceDocumentBuilder){
		Class classToVerify = null;
		logger.info("Looking for interface " + resourceType);
		try{
			if(className == null || className.length()==0){
				resouceDocumentBuilder.updateResourceInvalidInformation(String.format("Resource class name %s is either empty or not defined !",className));
				return false;
			}

            // will pass the check for the time being if resourceType is "NA" but need a better way to handle and implement it !
            if(resourceType != null && resourceType.equals("NA")) {
                return true;
            }

			classToVerify = verifyAndReturnResourceClass(className);
			if(hasClassImplementedInterface(classToVerify,resourceType)){
                return true;
            }
			//If not that means the resource doesn't implement the expected interface hence will log invalid information and return false
			resouceDocumentBuilder.updateResourceInvalidInformation(String.format("Resource class name %s don't implement any one of %s interface hence this resource can not be hooked up while constructing resource ! \nPlease implement at least one of the interface in the controller  !",className,resourceType));
			return false;

		}catch (ClassNotFoundException cnfExp){
			resouceDocumentBuilder.updateResourceInvalidInformation(String.format("Resource class name %s doesn't exist !",
					className));
			return false;
		}
	}


	/***
	 * This function is to handle custom http method in future (at this time it doesn't do any validation as is not
	 * supported). Once Jersey processing of http method figured out or in future Jersey allow custom http method
	 * other than standard http method e.g. GET, POST , PUT , PATCH, DELETE etc.
	 * @param resourceClassName
	 * @param interfaceName
     * @return
     */
    public static boolean hasResourceTypeImplementInterfaceListed(String resourceClassName,String interfaceName){
        boolean classImplementInterface = false;
        try {
            Class classToVerify = verifyAndReturnResourceClass(resourceClassName);
            classImplementInterface = hasClassImplementedInterface(classToVerify,interfaceName);

        }catch (ClassNotFoundException cnfExp){
            //Will not do anything except logging it as error which is being taken care by private methods
        }finally {
            return classImplementInterface;
        }

    }

	public static boolean isValidClass(String className) {
		try {
			Class classToVerify = verifyAndReturnResourceClass(className);
			return classToVerify == null ? false : true;
		}catch (ClassNotFoundException classNotFoundExp){
			return false;
		}
	}

	private static Class verifyAndReturnResourceClass(String className) throws ClassNotFoundException{
		Class classToVerify = null;
		try {
			classToVerify = Class.forName(className);
		}catch (ClassNotFoundException cnfExp){
			logger.info(String.format("%s class name not found !\n",className) + cnfExp.getMessage());
            throw cnfExp;
		}finally {
			return classToVerify;
		}
	}

	private static boolean hasClassImplementedInterface(Class className,String interfaceName){
		boolean classHasImplementedInterface = false;
        Class[] interfaceImplemented = className.getInterfaces();
        for (Class interfaceImp : interfaceImplemented) {
            logger.info("Interface : " + interfaceImp.getName());

            if (interfaceName.contains(interfaceImp.getName())) {
                classHasImplementedInterface = true;
                break;
            }
        }

		return classHasImplementedInterface;
	}

	/***
	 * This function is to parse api path and get path param enclosed in { } bracket as list
	 * @param apiPath
	 * @return array list of path param parsing api path
     */
	public static ArrayList<String> parseAndGetPathParams(String apiPath){
		String[] params = null;
		ArrayList<String> paramList = new ArrayList<>();
		if (apiPath != null){
			//Parse path and get list of path param
			String pathParamOpeningBracket = "\\{";
			String pathParamClosingBracket = "}";
			params = apiPath.split(pathParamOpeningBracket);
			for(String p : params){
				if(p.contains("}")){
					int indexOfClosingBracket = p.indexOf(pathParamClosingBracket);

					paramList.add(p.substring(0,indexOfClosingBracket));
				}
			}
		}
		return paramList;
	}

	public static InputStream getFileStream(String fileName) throws IOException,ParserException {
		final String fileSeparator = System.getProperty("file.separator");
		final  String DEFAULT_CONFIGURATION_FOLDER_NAME = "Config";
		String yamlFileFullPath = System.getProperty("user.dir") + fileSeparator
				+ DEFAULT_CONFIGURATION_FOLDER_NAME + fileSeparator + fileName;

		try {
			return  Files.newInputStream( Paths.get(yamlFileFullPath) );

		}catch (IOException ioExp){
			logger.error(ioExp.getMessage());
			throw ioExp;
		}catch (ParserException parserExp){

			logger.error("Error reading dynamic resource file : " + parserExp.getMessage());
			throw parserExp;

		}
	}
}
