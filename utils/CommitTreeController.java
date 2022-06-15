package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.print.DocFlavor.STRING;
import javax.swing.tree.TreeNode;

import utils.DataStruct.Blob;
import utils.DataStruct.Commit;
import utils.DataStruct.CommitTree;
import utils.DataStruct.StageingArea;


public abstract class CommitTreeController implements Serializable {
    static String theInitPath = "./.gitlet";
    public static String init(){
        if (!new File(theInitPath).exists()){
            createTree();
            return "";
        }
        else{
            Path path = Paths.get(theInitPath);
            return "A Gitlet version-control system already exists in the current directory.";
        }
    }
    public static String add(String filename){
        /// stages a file for commit 
        if (StagingAreaController.getStageingArea() == null){
            StageingArea stageArea  = StagingAreaController.createStageingArea();
            StagingAreaController.stagefile(stageArea , filename);
            StagingAreaController.saveStageingArea(stageArea);
            return "";
        }
        else{
            /// if the file the same in current commit ? 
            /// Blob.getblob()
            StageingArea stageArea = StagingAreaController.getStageingArea();
            StagingAreaController.stagefile(stageArea , filename);
            StagingAreaController.saveStageingArea(stageArea);
            return "";
        }
    }
    private static void createTree() {
        new File(theInitPath).mkdir();
        CommitTree newCommitTree  = new CommitTree();
        Commit inital_commit = CommitController.createCommit();
        try {
            newCommitTree.addBranch("main", "init");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return;
        }
        newCommitTree.setCurrentBranch("main");
        inital_commit.setMessage("Init");
        CommitController.saveCommit(inital_commit);
        saveTree(newCommitTree);

    }
    // private static CommitTree getTree(){
    // }
    private static void saveTree(CommitTree tree){
        File outFile = new File(".gitlet"+"/"+"Tree");
        try {
            ObjectOutputStream out =
                new ObjectOutputStream(new FileOutputStream(outFile));
            out.writeObject(tree);
            out.close();
        } catch (IOException excp) {
            System.out.println("Couldn't do that");
        }
    }
    public static CommitTree getTree(){
        CommitTree obj;
        File inFile = new File("./.gitlet/Tree");
        try {
            ObjectInputStream inp =
                new ObjectInputStream(new FileInputStream(inFile));
            obj = (CommitTree) inp.readObject();
            inp.close();
            return obj;
        } catch (IOException | ClassNotFoundException excp) {
        }
        return null;
    }
    public static String commit(String messString){
        String currentbranch;
        String currentcommit;
        CommitTree mytree;
        HashMap<String,String> stage;
        StageingArea area = StagingAreaController.getStageingArea();
        if (area != null){
            Commit newcommit = new Commit(messString);
            System.out.println("mess : " + newcommit.getMessage());
            mytree = getTree();
            currentbranch = mytree.getCurrentBranch();
            currentcommit = mytree.getBranches().get(currentbranch);
            newcommit.setPrev(currentcommit);
            stage = StagingAreaController.getStageBlobs(area);
            CommitController.setblobs(newcommit ,(HashMap<String, String>) stage);
            newcommit.setMessage(messString);
            ///Move 
            try {
                mytree.getBranches().put(currentbranch, CommitController.sha1_contents(newcommit.getBlobs()));
            } catch (Exception e) {
                e.printStackTrace();
                return "Failed";
            }
            CommitController.saveCommit(newcommit);
            StagingAreaController.removeStageingArea();
            currentbranch = mytree.getCurrentBranch();
            currentcommit = mytree.getBranches().get(currentbranch);
            mytree.newRemovefile();
            CommitTreeController.saveTree(mytree);
            return "";
        }
        else{
            return "No changes added to the commit";
        }

    }
    public static String rm(String Filename){
        ///Unstage the file if it is currently staged for addition. 
        //If the file is tracked in the current commit, stage it for removal and
        // remove the file from the working directory if the user has not already done so 
        //(do not remove it unless it is tracked in the current commit).
        CommitTree mytree;
        StageingArea mystage;
        mytree = getTree();
        mystage = StagingAreaController.getStageingArea();
        int removedFromstage = 1;
        boolean removeFromDir = false;
        if (mystage != null){
            removedFromstage = mystage.unstage(Filename);
        }
        Commit curcommit = CommitController.getCommit(mytree.getBranches().get(mytree.getCurrentBranch()));
        if(curcommit== null){
            return "Gitlet not Initalized";
        }
        if(CommitController.getCommit(mytree.getBranches().get(mytree.getCurrentBranch())).getBlobs() == null){
            return "No reason to remove the file.";
        }
        if(CommitController.getCommit(mytree.getBranches().get(mytree.getCurrentBranch())).getBlobs().containsKey(Filename)){
            File obj = new File("./"+Filename);
            mytree.putRemovefile(Filename);
            removeFromDir = obj.delete();
            //System.out.println(CommitController.getCommit(mytree.getCurrentBranch()).getblobs().containsKey(Filename));
        }
        if (removedFromstage == 0 && removeFromDir == true){
            return "No reason to remove the file.";
        }
        return Filename + " removed";

    }
    public static void log(){
        //Starting at the current head commit, display information 
        //about each commit backwards along the commit tree until the initial commit, 
        //following the first parent commit links, ignoring any second parents found 
        //in merge commits. (In regular Git, this is what you get with git log --first-parent). 
        //This set of commit nodes is called the commit’s history. For every node in this history, 
        //the information it should display is the commit id, the time the commit was made, and the
        // commit message. Here is an example of the exact format it should follow:
        Commit currCommit;
        CommitTree myTree;
        String currCommitID; 
        myTree  = getTree();
        currCommit = CommitController.getCommit(myTree.getBranches().get(myTree.getCurrentBranch()));
        currCommitID = myTree.getBranches().get(myTree.getCurrentBranch());
        while(currCommitID != null){
            System.out.println("commit "+ currCommitID);
            System.out.println(currCommit.getDate());
            System.out.println("\t"+ currCommit.getMessage());
            System.out.println();
            currCommitID = currCommit.getPrev();
            currCommit = CommitController.getPrevCommit(currCommit);
        }
        return;
    }
    public static String find(String mess){
        Commit currCommit;
        CommitTree myTree;
        String currCommitID; 
        myTree  = getTree();
        Set<String> commitids =  new HashSet<String>();
        for(Object branch : myTree.getBranches().keySet().toArray()){
            currCommitID = myTree.getBranches().get(branch);
            currCommit = CommitController.getCommit(currCommitID);
            while(currCommit != null){
                if(currCommit.getMessage().compareTo(mess) == 0 ){
                    commitids.add(currCommitID);
                }
                currCommitID = currCommit.getPrev();
                currCommit = CommitController.getPrevCommit(currCommit);
            }
        }
        for(String id : commitids ){
            System.out.println(id);
        }
        //Prints out the ids of all commits that have the given
        // commit message, one per line. If there are multiple such commits,
        // it prints the ids out on separate lines. The commit message is a single operand;
        // to indicate a multiword message, put the operand in quotation marks, as for the commit 
        //command below. Hint: the hint for this command is the same as the one for global-log
        return "";
    }
    public static void status(){
        // Displays what branches currently exist, and marks the current
        // branch with a *. Also displays what files have been staged for
        // addition or removal. An example of the exact format it
        // should follow is as follows.
        /*
         === Branches ===
        *master
        other-branch
        
        === Staged Files ===
        wug.txt
        wug2.txt
        
        === Removed Files ===
        goodbye.txt
        
        === Modifications Not Staged For Commit ===
        junk.txt (deleted)
        wug3.txt (modified)
        
        === Untracked Files ===
        random.stuff
         */
        CommitTree mytree = getTree();
        String curbran = mytree.getCurrentBranch();
        StageingArea myStageingArea = StagingAreaController.getStageingArea();
        
        System.out.println("=== Branches ===");
        System.out.println("*"+ curbran);
        for(Object treename : mytree.getBranches().keySet().toArray()){
            if (!treename.equals(curbran)){
                System.out.println(treename);
            }
        }
        System.out.println("=== Staged Files ===");
        if (myStageingArea != null){
            HashMap<String,String> stagedfile = myStageingArea.getStage();
            if(stagedfile != null){
                    for(Object file : stagedfile.keySet().toArray()){
                        System.out.println(file);
                }
            }
        }

        System.out.println("=== Removed Files ===");
        for(Object removedfile : mytree.getRemovefile()){
                System.out.println(removedfile);
            }
        ///Files in previous commit: test.txt 
        System.out.println("=== Modifications Not Staged For Commit ===");
        Commit currCommit = CommitController.getCommit(mytree.getBranches().get(curbran));
        if (currCommit.getBlobs()!=null){
            for(Object filename : currCommit.getBlobs().keySet().toArray()){
                if (! new File("./"+filename).exists()){
                    System.out.println(filename + " (Deleted)");
                };
             try {
                  String currcontent = BlobController.sha1(String.join(System.lineSeparator(),Files.readAllLines(Paths.get("./"+filename), StandardCharsets.UTF_8)));
                  if(!currcontent.equals(currCommit.getBlobs().get(filename))){
                     System.out.println(filename +"(Modified)");}
             } catch (IOException e) {
                 // TODO Auto-generated catch block
                 
             } catch (Exception e) {
                 // TODO Auto-generated catch block
             }
     
         }
        }
        File folder = new File("./");
        ArrayList<String> files_curr  = listFilesForFolder(folder);
        System.out.println("=== Untracked Files ===");
        if(currCommit.getBlobs()==null && myStageingArea == null){
            for(String file: files_curr){
                        System.out.println(file);
            }
        }
        if(myStageingArea != null){
            HashMap<String,String> stagedfile = myStageingArea.getStage();
            stagedfile= myStageingArea.getStage();
            for(String file: files_curr){
                    if (!stagedfile.keySet().contains(file)){
                        System.out.println(file);
                    }
                }
            }
    }

    public static ArrayList<String> listFilesForFolder(File folder) {
        ArrayList<String> files = new ArrayList<String>();
        for (File fileEntry : folder.listFiles()) {
                if(!fileEntry.getName().contains(".java") && !fileEntry.getName().contains(".class") && !fileEntry.getName().contains(".gitlet") && !fileEntry.getName().contains(".git") && !fileEntry.isDirectory())
                files.add(fileEntry.getName());
        }
        return files;
    }
    
    

    public static String checkout(){
        /*
         * 
         * Usages:

        1.java gitlet.Main checkout -- [file name]

        2.java gitlet.Main checkout [commit id] -- [file name]

        3.java gitlet.Main checkout [branch name]

        Descriptions:

        1.
        Takes the version of the file as it exists in the head commit 
        and puts it in the working directory, overwriting the version of 
        the file that’s already there if there is one. 
        The new version of the file is not staged.

        2.
        Takes the version of the file as it exists in the commit with the given id, 
        and puts it in the working directory, overwriting the version of the file that’s
        already there if there is one. The new version of the file is not staged.

        3. Takes all files in the commit at the head of the given branch, 
        and puts them in the working directory, overwriting the versions of
        the files that are already there if they exist. Also, at the end of this command, 
        the given branch will now be considered the current branch (HEAD). 
        Any files that are tracked in the current branch but are not present in the 
        checked-out branch are deleted. The staging area is cleared, unless the checked-out
         branch is the current branch (see Failure cases below).
         */
        return "CHECKOUT";
    }
    public static String branch(){
        /*
        Creates a new branch with the given name, 
        and points it at the current head commit. A branch is nothing more
         than a name for a reference (a SHA-1 identifier) to a commit node. 
         This command does NOT immediately switch to the newly created branch (just as in real Git). 
         Before you ever call branch, your code should be running with a default branch called “master”.
         */
        return "branch";

    }
    public static String rm_branch(){
        /*
         Deletes the branch with the given name. This only means 
         to delete the pointer associated with the branch;
         it does not mean to delete all commits that were created
         under the branch, or anything like that.
         */
        return "branch";

    }
    public static String reset(){
        /*
         * Checks out all the files tracked by the given commit. Removes tracked files
         *  that are not present in that commit. Also moves the current branch’s head 
         * to that commit node. See the intro for an example of what happens to 
         * the head pointer after using reset. The [commit id] may be abbreviated as
         *  for checkout. The staging area is cleared. The command is essentially checkout 
         * of an arbitrary commit that also changes the current branch head.
         */
        return "branch";
    }
    public static String merge(){
        /* Merges files from the given branch into the current branch.*/
        return  "merge";
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