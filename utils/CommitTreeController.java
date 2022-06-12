package utils;

import java.io.Serializable;

public abstract class CommitTreeController implements Serializable {
    
    public static String init(){
        static String theInitPath = "./.gitlet";
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
}
