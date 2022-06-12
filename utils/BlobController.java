package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import utils.DataStruct.Blob;

public class BlobController {
    public static void saveBlob(Blob blob){
        String filename =  String.valueOf(blob.hashCode());
        File outFile = new File(".gitlet"+"/"+filename);
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
            File inFile = new File("./.gitlet/"+ hashcode);
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
}
