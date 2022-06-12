package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;

import utils.DataStruct.CommitTree;
import utils.DataStruct.StageingArea;

public abstract class CommitTreeController implements Serializable {
    static String theInitPath = "./.gitlet";
    public static String init(){
        if (!new File(theInitPath).exists()){
            createTree();
            return "File create";
        }
        else{
            Path path = Paths.get(theInitPath);
            System.out.println(path);
            return "A Gitlet version-control system already exists in the current directory.";
        }
    }
    public static String add(String filename){
        /// stages a file for commit 
        CommitTree mytree;
        mytree = getTree();
        if (mytree.getStage() == null){
            StageingArea stageArea  = StagingAreaController.createStageingArea();
            StagingAreaController.stagefile(stageArea , filename);
            mytree.setStage(String.valueOf(stageArea.hashCode()));
            StagingAreaController.saveStageingArea(stageArea);
            saveTree(mytree);
            return "File Staged";
        }
        else{
            StageingArea stageArea = StagingAreaController.getStageingArea();
            StagingAreaController.stagefile(stageArea , filename);
            mytree.setStage(String.valueOf(stageArea.hashCode()));
            StagingAreaController.saveStageingArea(stageArea);
            return "File Staged";
        }
    }
    private static void createTree() {

        new File(theInitPath).mkdir();
        CommitTree newCommitTree  = new CommitTree();
        saveTree(newCommitTree);

    }
    // private static CommitTree getTree(){
    // }
    private static void saveTree(CommitTree tree){
        File outFile = new File(".gitlet"+"/"+"tree");
        try {
            ObjectOutputStream out =
                new ObjectOutputStream(new FileOutputStream(outFile));
            out.writeObject(tree);
            out.close();
        } catch (IOException excp) {
            System.out.println("Couldn't do that");
        }
    }
    private static CommitTree getTree(){
        CommitTree obj;
        File inFile = new File("./.gitlet/tree");
        try {
            ObjectInputStream inp =
                new ObjectInputStream(new FileInputStream(inFile));
            obj = (CommitTree) inp.readObject();
            inp.close();
            return obj;
        } catch (IOException | ClassNotFoundException excp) {
            System.out.println("No tree");
        }
        return null;
    }
        
    }