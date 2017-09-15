package external.swagger;

import net.openhft.compiler.CachedCompiler;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by nitina on 3/4/17.
 * This class load a template class which is basically a Swagger API decorator
 * We will use OpenHFT Java Runtime Compiler to compile class on the fly and put it under target/classes
 * (or inside corresponding package if package has been included)
 * folder so that Swagger can load/read it later
 * For more info //Ref: https://github.com/OpenHFT/Java-Runtime-Compiler
 * Also an issue was raised which has been resolved //Ref: https://github.com/OpenHFT/Java-Runtime-Compiler/issues/23
 */
public class SwaggerApiTemplateLoader {

    public final static void loadTemplate() throws Exception{

        String javaCode = loadSwaggerApiTemplate();
        String fqdnClassName = "external.swagger.template.SwaggerApiDocumentation";
        loadTemplate(javaCode,fqdnClassName);

    }

    public final static void loadTemplate(String classToLoad,String fqdnClassName) throws Exception{
        //If the class SwaggerApiDocumentation suppose to be in package then construct fqdn string i.e. pakcageName.SwaggerApiDocumentation
        //In this case the class will get compile and put under target/classes/external/swagger/template.
        //String fqdnClassName = "external.swagger.template.SwaggerApiDocumentation";
        CachedCompiler JCC = new CachedCompiler(null, new File("", System.getProperty("user.dir") +
                System.getProperty("file.separator") +"target" + System.getProperty("file.separator") +"classes"));
        JCC.loadFromJava(fqdnClassName,classToLoad);
        System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
    }

    private static String loadSwaggerApiTemplate(){
        String templateString="";
        try {


            String resourceBannerFullPath = System.getProperty("user.dir") + System.getProperty("file.separator") +
                    "src" + System.getProperty("file.separator") +
                    "main" + System.getProperty("file.separator") +
                    "resources"+ System.getProperty("file.separator") +"SwaggerApiTemplate";
            templateString = new String(Files.readAllBytes(Paths.get(resourceBannerFullPath)));

        }catch (IOException exp){
            System.out.println("Panic : failed to load SwaggerApiTemplate !");
        }finally {
            return templateString;
        }
    }

}
