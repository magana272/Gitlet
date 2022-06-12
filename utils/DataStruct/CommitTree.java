package utils.DataStruct;
import java.io.Serializable;
import java.util.HashMap;

import utils.CommitController;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

//
//
//
//
// Inital branch is called main:
// ... must create an inital commit not files 
// ... 
//
//
//
//
//
//

public class CommitTree implements Serializable  {
    private  HashMap<String, String> branches; //banch name and SHA-1
    private String currentBranch; // Branch name
    private String stage; 
    public CommitTree() {
            branches = new HashMap<String,String>(); 
            currentBranch = "main";
            Commit initalCommit  =  new Commit();
            branches.put(currentBranch, String.valueOf(initalCommit.hashCode()));
            stage = null;

    }

    public void addBranch(String branchName, String commit){
        this.branches.put(branchName,commit);
    }
    public void removeBranch(String brachName){
        /// need to to find split point of the branch 
        /// get branch: get commit curr load prev delete curr until SHA-1
    }
    public String findSplit(String Branch_curr, String branch_other){
        ///
        return "SHA-1 of split point";
    }
    public String getStage(){
        return this.stage;
    }
    public void setStage(String stagehash){
        this.stage = stagehash;
    }
    //Directory Structure mapping to references to blobs and other trees
    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
