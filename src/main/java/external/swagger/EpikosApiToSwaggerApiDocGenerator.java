package external.swagger;

import core.dynamic.resources.Api;

import java.util.List;

/**
 * Created by nitina on 3/12/17.
 * ToDo: Investigate and replace it with more elegant solution like using template e.g. freemarker or velocity framework
 */
public class EpikosApiToSwaggerApiDocGenerator {

    final static StringBuilder classContstruct = new StringBuilder();

    final static String LineBreaker = "\n";

        final static String PACKAGE = "package";

        final static String PACKAGE_NAME = "external.swagger";


        final static String IMPORT_FIELDS = " import com.wordnik.swagger.annotations.Api;\n" +
                "import com.wordnik.swagger.annotations.ApiOperation;\n" +
                "import com.wordnik.swagger.annotations.ApiResponse;\n" +
                "import com.wordnik.swagger.annotations.ApiResponses;\n" +
                "import core.error.EpikosError;\n" +
                "import example.response.Response;\n" +
                "import javax.ws.rs.Path;\n" +
                "import javax.ws.rs.Produces;\n" +
                "import javax.ws.rs.core.MediaType;";

        final static String IMPORT_GET = "\n" + "import javax.ws.rs.GET;\n";
        final static String IMPORT_POST = "\n" + "import javax.ws.rs.POST;\n";
        final static String IMPORT_PUT = "\n" + "import javax.ws.rs.PUT;\n";
        //final static String IMPORT_PATCH = "\n" + "import javax.ws.rs.PATCH;\n";
        final static String IMPORT_DELETE = "\n" + "import javax.ws.rs.DELETE;\n";

        final static String classHeader = "@Api(value = \"Api Doc\", description = \"Swagger documentation for API(s)\")";
        final static String classDefenition = "public class %s";

        final static String get = "@GET";
        final static String post = "@POST";
        final static String put ="@PUT";
        final static String patch = "@PATCH";
        final static String delete = "@DELETE";

        final static String path = "@Path(\"%s\")";
        final static String produce = "@Produces(\"%s\")";
        final static String apiOperation = "@ApiOperation";
        final static String apiOperationValue ="value = \"%s\"";
        final static String apiOperationNotes = ",notes = \"%s\"";
        final static String getApiOperationResposne = ",response = %s";

        final static String apiRespnose = "@ApiResponses(value = {\n" +
                "            @ApiResponse(code = 400, message = \"Invalid ID supplied 1\", response = EpikosError.class),\n" +
                "            @ApiResponse(code = 500, message = \"Server is down! 1\", response = EpikosError.class)})";

        final static String openCurly = "{";
        final static String closeCurly = "}";
        final static String openBracket = "(";
        final static String closeBracket = ")";

        final static String methodBody = "public void process%s() {\n}";

    public static void constructSwaggerApiDoc(List<Api> epikosApi){
        if(epikosApi == null || epikosApi.size() ==0){
            //ToDo :log something here
            return;
        }

        StringBuilder classStrcture = createClassSkleton("SwaggerApiDocumentation");
        int iteration = 0;
        for(Api api : epikosApi){
            classStrcture.append(LineBreaker);
            appendMethod(api.getMethod(),classStrcture);
            classStrcture.append(LineBreaker);
            classStrcture.append(String.format(path,api.getPath()));
            classStrcture.append(LineBreaker);
            classStrcture.append(String.format(produce,api.getProduce()));
            classStrcture.append(LineBreaker);
            classStrcture.append(apiOperation);
            classStrcture.append(openBracket);
            classStrcture.append(String.format(apiOperationValue,"First API test " + iteration));
            classStrcture.append(String.format(apiOperationNotes,"First Note" + iteration));
            classStrcture.append(String.format(getApiOperationResposne,"Response.class"));
            classStrcture.append(closeBracket);
            classStrcture.append(LineBreaker);
            classStrcture.append(String.format(methodBody,iteration));

            classStrcture.append("\n");
            iteration++;

        }

        classStrcture.append(closeCurly);
        try {

            SwaggerApiTemplateLoader.loadTemplate(classStrcture.toString(),PACKAGE_NAME + "." + "SwaggerApiDocumentation"); //Load and Construct Swagger API doc on the fly
        }catch (Exception exp){
            System.out.println(exp.getMessage());
        }

    }

    private  static StringBuilder createClassSkleton(String classNamePrefix){
        String className = classNamePrefix;
        //classContstruct = new StringBuilder();
        classContstruct.append(PACKAGE  + " " + PACKAGE_NAME + ";");
        classContstruct.append(IMPORT_FIELDS);
        classContstruct.append(IMPORT_POST);
        //classContstruct.append(IMPORT_PATCH);
        classContstruct.append(IMPORT_PUT);
        classContstruct.append(IMPORT_DELETE);
        classContstruct.append(IMPORT_GET);

        classContstruct.append(classHeader);
        classContstruct.append(LineBreaker);
        classContstruct.append(String.format(classDefenition,className));
        classContstruct.append(openCurly);
        return classContstruct;
    }

    private static void appendMethod(String method,StringBuilder methodAttribute){
        if("GET".equals(method)){
            methodAttribute.append(get);
        }else if("PUT".equals(method)){
            methodAttribute.append(put);
        }else if("POST".equals(method)){
            methodAttribute.append(post);
        }else if("DELETE".equals(method)){
            methodAttribute.append(delete);
        }else if("PATCH".equals(method)){
            methodAttribute.append(patch);
        }

    }

}
