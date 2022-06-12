package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
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
        area.stage(filename);
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
    public static HashMap<String, Blob> getStageBlobs(StageingArea area){ 
        return area.getStage();

    }

        
    }      
