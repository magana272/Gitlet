package utils.DataStruct;
import java.io.Serializable;
import java.util.HashMap;

import utils.CommitController;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class CommitTree implements Serializable  {
    private  HashMap<String, String> branches; //banch name and SHA-1
    private String currentBranch; // Branch name
    public void setCurrentBranch(String currentBranch) {
        this.currentBranch = currentBranch;
    }
    private String stage; 
    public CommitTree() {
            branches = new HashMap<String,String>(); 
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
    public HashMap<String, String> getBranches() {
        return branches;
    }

    public void setBranches(HashMap<String, String> branches) {
        this.branches = branches;
    }
    public String getCurrentBranch() {
        return currentBranch;
    }

    //Directory Structure mapping to references to blobs and other trees
    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
