package utils.DataStruct;
import java.io.Serializable;
import java.util.HashMap;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class CommitTree implements Serializable  {
    private  HashMap<String,commit> branches;
    private commit prev;
    private String currentBranch; 
    public CommitTree() {
            branches = new HashMap<String,commit>(); 
            currentBranch = "main";
            branches.put(currentBranch,new commit());
    }
    public void save(String file){
        File outFile = new File(".gitlet"+"/"+file);
        try {
            ObjectOutputStream out =
                new ObjectOutputStream(new FileOutputStream(outFile));
            out.writeObject(this);
            out.close();
        } catch (IOException excp) {
            System.out.println("Couldn't do that");
        }
    }
    //Directory Structure mapping to references to blobs and other trees
    
}
