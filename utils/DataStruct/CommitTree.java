package utils.DataStruct;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import utils.CommitController;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class CommitTree implements Serializable  {
    private  HashMap<String, String> branches; //banch name and SHA-1
    private String currentBranch; // Branch name
    private ArrayList<String> removedFiles;
    public void setCurrentBranch(String currentBranch) {
        this.currentBranch = currentBranch;
        this.removedFiles =  new ArrayList<String>();
    }
    public CommitTree() {
            this.branches = new HashMap<String,String>(); 
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
    public HashMap<String, String> getBranches() {
        return this.branches;
    }
    public void putRemovefile(String filename){
        this.removedFiles.add(filename);
    }
    public ArrayList<String> getRemovefile(){
        return this.removedFiles;
    }
    public void newRemovefile(){
        this.removedFiles = new ArrayList<String>();
    }
    public void setBranches(HashMap<String, String> branches) {
        this.branches = branches;
    }
    public String getCurrentBranch() {
        return this.currentBranch;
    }

    //Directory Structure mapping to references to blobs and other trees
    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
