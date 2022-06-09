package utils;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class init {
    public static String INIT(){
        if (!new File("./.gitlet").exists()){
            create();
            return "File create";
        }
        else{
            Path path = Paths.get("../.init");
            System.out.println(path);
            return "A Gitlet version-control system already exists in the current directory.";
        }
    }
    private static void create() {
        new File("./.getlet").mkdir();
    }
    
}
