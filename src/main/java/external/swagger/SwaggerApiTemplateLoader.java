package external.swagger;

import com.sun.jna.platform.FileUtils;
import core.lib.Utility;
import io.swagger.models.Swagger;
import javassist.ClassPool;
import net.openhft.compiler.CachedCompiler;
import net.openhft.compiler.CompilerUtils;

import javax.tools.*;
import java.io.*;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;

/**
 * Created by nitina on 3/4/17.
 */
public class SwaggerApiTemplateLoader {

    public final static void loadTemplate() throws Exception{

        //Ref: https://github.com/OpenHFT/Java-Runtime-Compiler/issues/23

        String javaCode = loadSwaggerApiTemplate();
        String fqdnClassName = "external.swagger.SwaggerApiDocumentation";
        String parentPath = System.getProperty("user.dir") + "\\src\\main\\java\\external\\swagger";
        String classPath = System.getProperty("user.dir") + "\\target\\classes\\external\\swagger";

        //Ref: https://github.com/OpenHFT/Java-Runtime-Compiler
        CachedCompiler JCC = new CachedCompiler(null, new File("temp", ""));

        JCC.loadFromJava(fqdnClassName,javaCode);


        File sourceFile = new File(System.getProperty("user.dir") + "/temp/external/swagger/SwaggerApiDocumentation.class");
        File destinationFile = new File(System.getProperty("user.dir") + "/target/classes/external/swagger/SwaggerApiDocumentation.class");
        Files.move(sourceFile.toPath(),destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

        //Class aClass = CompilerUtils.CACHED_COMPILER.loadFromJava(fqdnClassName, javaCode);
        ////ClassLoader classLoader = SwaggerApiTemplateLoader.class.getClassLoader();m
        ////Class aClass = CompilerUtils.loadFromResource(fqdnClassName, javaCode);
        //String  name = aClass.getName();
        ////classLoader.loadClass(aClass.getName());
        //Runnable runnable = (Runnable)aClass.newInstance();
        //runnable.run();

        //JavaCompiler javac = ToolProvider.getSystemJavaCompiler();
        //int rc = javac.run(null, null, null, name);
        //System.out.println("\n$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$" + rc + " ############");


        //SwagerApiDocumentation runner = (SwagerApiDocumentation) aClass.newInstance();

        //final StringBuilder sbTemplate = new StringBuilder();
        //sbTemplate.append(template);


        //File sourceFile = File.createTempFile("/Users/nitina/Documents/Personal/Development/Services/EpikosRest/src/main/java/external/swagger/SwagerApiDocumentation", ".java");
        //sourceFile.deleteOnExit();

        // generate the source code, using the source filename as the class name
        //String classname = sourceFile.getName().split("\\.")[0];
        //String sourceCode = template;

        // write the source code into the source file
        //FileWriter writer = new FileWriter(sourceFile);
        //writer.write(sourceCode);
        //writer.close();

        // compile the source file
        /*JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
        File parentDirectory = sourceFile.getParentFile();
        fileManager.setLocation(StandardLocation.CLASS_OUTPUT, Arrays.asList(parentDirectory));
        Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjectsFromFiles(Arrays.asList(sourceFile));
        compiler.getTask(null, fileManager, null, null, null, compilationUnits).call();
        fileManager.close();*/

    }

    private static String loadSwaggerApiTemplate(){
        String bannerText="";
        //logger.info("Loading .....");
        try {


            String resourceBannerFullPath = System.getProperty("user.dir") + System.getProperty("file.separator") +
                    "src" + System.getProperty("file.separator") +
                    "main" + System.getProperty("file.separator") +
                    "resources"+ System.getProperty("file.separator") +"SwaggerApiTemplate";
            bannerText = new String(Files.readAllBytes(Paths.get(resourceBannerFullPath)));
            //logger.info("\n" + bannerText + "\n");

        }catch (IOException exp){
            System.out.println("Panic : failed to load SwaggerApiTemplate !");
        }finally {
            return bannerText;
        }
    }

}
