package utils;
import utils.DataStruct.Blob;
import utils.DataStruct.Commit;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;


public class CommitController {
    /// Main Idea is we are moving files from staging area to an actual commit and adding it to commit tree
    public static Commit createCommit(){
        Commit commit = new utils.DataStruct.Commit();
        return commit;
    }
    public static Commit createCommit(String mess){
        Commit commit = new utils.DataStruct.Commit(mess);
        return commit;
    }
    public static String sha1_contents(HashMap<String,String> blobs){
        String SHAthis = "";
        for ( Map.Entry<String, String> entry : blobs.entrySet()) {
            String key = entry.getKey();
            String tab = entry.getValue();
            SHAthis= SHAthis+tab;
            // do something with key and/or tab
        }
        try {
            return sha1(SHAthis);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "oops";
        }
    }
    public static void saveCommit(Commit commit){
        String filename;
        File outFile;
        String commitpath = ".gitlet/Commit";
        if(!new File(commitpath).exists())
            new File(commitpath).mkdir();
        try {
            filename = sha1_contents(commit.getBlobs());
            outFile = new File(commitpath+"/"+filename);
        } catch (Exception e) {
            filename = "init";
            outFile = new File(commitpath+"/"+filename);
        }
        try {
            ObjectOutputStream out =
                new ObjectOutputStream(new FileOutputStream(outFile));
            out.writeObject(commit);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Couldn't save commit");
        }
    }
    public static Commit getCommit(String hashcode){
        //should return commit
        Commit obj;
        File inFile = new File(".gitlet/Commit/"+hashcode);
        try {
            ObjectInputStream inp =
                new ObjectInputStream(new FileInputStream(inFile));
            obj = (Commit) inp.readObject();
            inp.close();
            return obj;
        } catch (IOException | ClassNotFoundException excp) {
        }
        return null;
    }
    public static void setblobs( Commit commit, HashMap<String,String> filesCommited){
        commit.setBlobs(filesCommited);
    }
    public static Commit getPrevCommit(Commit commit){
        return getCommit(commit.getPrev());
    }
    public static String sha1(Object object) throws Exception {
        if (object == null) {
            throw new Exception("Object is null.");
        }

        String input = String.valueOf(object);

        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA1");
        } catch (NoSuchAlgorithmException ex) {
            return null;
        }
        md.reset();

        byte[] buffer = input.getBytes();
        md.update(buffer);

        byte[] digest = md.digest();
        String hexStr = "";
        for (int i = 0; i < digest.length; i++) {
            hexStr += Integer.toString((digest[i] & 0xff) + 0x100, 16).substring(1);
        }
        return hexStr;
    }
}
