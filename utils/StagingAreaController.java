package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import utils.DataStruct.Blob;
import utils.DataStruct.StageingArea;

public abstract class StagingAreaController {
    public static StageingArea createStageingArea(){
        StageingArea newStageingArea = new StageingArea();
        newStageingArea.setNewStage();
        return newStageingArea;
    }
    public static void stagefile(StageingArea area,String filename){
        Blob stageBlob = BlobController.createBlob(filename);
        try {
            area.stage(filename,sha1(stageBlob.getFileConents()));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return;
        }
        BlobController.stageBlob(stageBlob);
    }
    public static StageingArea getStageingArea(){
        StageingArea obj;
        File inFile = new File(".gitlet/"+"StageingArea");
        try {
            ObjectInputStream inp =
                new ObjectInputStream(new FileInputStream(inFile));
            obj = (StageingArea) inp.readObject();
            inp.close();
            return obj;
        } catch (IOException | ClassNotFoundException excp) {
            System.out.println("No Stageingarea");
        }
        return null;
    }
    public static void saveStageingArea(StageingArea area){
        File outFile = new File(".gitlet/StageingArea");
        try {
            ObjectOutputStream out =
                new ObjectOutputStream(new FileOutputStream(outFile));
            out.writeObject(area);
            out.close();
        } catch (IOException excp) {
            System.out.println("Couldn't do that");
        }
    }
    public static HashMap<String, String> getStageBlobs(StageingArea area){ 
        return area.getStage();

    }
    public static void removeStageingArea(){
        File outFile = new File(".gitlet/StageingArea");
        outFile.delete();
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
