package utils;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
public abstract class init {
    static String theInitPath = "./.gitlet";
    public static String INIT(){
        if (!new File(theInitPath).exists()){
            create();
            return "File create";
        }
        else{
            Path path = Paths.get(theInitPath);
            System.out.println(path);
            return "A Gitlet version-control system already exists in the current directory.";
        }
    }
    private static void create() {
        new File(theInitPath).mkdir();
        commit.commitFinal("Init");
    }
    
}
