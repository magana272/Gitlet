package utils;
import utils.DataStruct.Blob;
import utils.DataStruct.Commit;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

public class CommitController {
    /// Main Idea is we are moving files from staging area to an actual commit and adding it to commit tree
    public static Commit createCommit(){
        Commit commit = new utils.DataStruct.Commit();
        return commit;
    }
    public static void saveCommit(Commit commit){
        String filename =  String.valueOf(commit.hashCode());
        File outFile = new File(".gitlet"+"/"+filename);
        try {
            ObjectOutputStream out =
                new ObjectOutputStream(new FileOutputStream(outFile));
            out.writeObject(commit);
            out.close();
        } catch (IOException excp) {
            System.out.println("Couldn't save commit");
        }
    }
    public Commit getCommit(String hashcode){
        //should return commit
        Commit obj;
        File inFile = new File("./.gitlet/"+hashcode);
        try {
            ObjectInputStream inp =
                new ObjectInputStream(new FileInputStream(inFile));
            obj = (Commit) inp.readObject();
            inp.close();
            return obj;
        } catch (IOException | ClassNotFoundException excp) {
            System.out.println("No Commit");
        }
        return null;
    }
    public static void setblobs( Commit commit, HashMap<String,Blob> filesCommited){
        commit.setBlobs(filesCommited);
    }
}  
