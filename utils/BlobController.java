package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import utils.DataStruct.Blob;

public class BlobController {
    public static Blob createBlob(String filename){
        return new Blob(filename);
    }
    public static void saveBlob(Blob blob){
        String filename;
        try {
            filename = sha1(blob.getFileConents());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return;
        }
        File outFile = new File(".gitlet"+"/blob/"+filename);
        try {
            ObjectOutputStream out =
                new ObjectOutputStream(new FileOutputStream(outFile));
            out.writeObject(blob);
            out.close();
        } catch (IOException excp) {
            System.out.println("Couldn't do that");
        }
    }
    public Blob getBlob(String hashcode){
        Blob obj;
            File inFile = new File("./.gitlet/blob/"+ hashcode);
            try {
                ObjectInputStream inp =
                    new ObjectInputStream(new FileInputStream(inFile));
                obj = (Blob) inp.readObject();
                inp.close();
                return obj;
            } catch (IOException | ClassNotFoundException excp) {
                System.out.println("Couldn't find that file");
            }
            return null;
        }    
        public static void stageBlob(Blob blob){
            String filename;
            String stagePath = ".gitlet/stage/";
            if(! new File(stagePath).exists()){ new File(stagePath).mkdir();}
            try {
                filename = sha1(blob);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return;
            }
            File outFile = new File(stagePath+filename);
            try {
                ObjectOutputStream out =
                    new ObjectOutputStream(new FileOutputStream(outFile));
                out.writeObject(blob);
                out.close();
            } catch (IOException excp) {
                System.out.println("Couldn't do that");
            }
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
