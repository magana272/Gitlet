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
import java.io.FileWriter;
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

    public static void deletefile(String filename){
            File check  = new File(filename);
            if (check.exists()){
                check.delete();
            }
    }

    public static void removeFilesFromFolder(File folder) {
        for (File fileEntry : folder.listFiles()) {
                if(!fileEntry.getName().contains(".java") && !fileEntry.getName().contains(".class") && !fileEntry.getName().contains(".gitlet") && !fileEntry.getName().contains(".git") && !fileEntry.isDirectory())
                fileEntry.delete();
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
    
    public static Boolean check_untracked(String dir){
       CommitTree myTree = CommitTreeController.getTree();
       Commit commit_branch = CommitController.getCommit(myTree.getBranches().get(myTree.getCurrentBranch()));
       HashMap<String, String>commit_files = commit_branch.getBlobs();
       ArrayList<String> files= listFilesForFolder(new File (dir));
       for(String file : files){
           System.out.println(file);
            if(!commit_files.containsKey(file)){

            return false;
            }
        }
       return true;
    }
    public static void checkout_branch(String branch_name){
        /* 3.java gitlet.Main checkout [branch name]
        Takes all files in the commit at the head of the given branch, 
        and puts them in the working directory, overwriting the versions of
        the files that are already there if they exist. Also, at the end of this command, 
        the given branch will now be considered the current branch (HEAD). 
        Any files that are tracked in the current branch but are not present in the 
        checked-out branch are deleted. The staging area is cleared, unless the checked-out
        branch is the current branch (see Failure cases below).
         */
        CommitTree myTree  = CommitTreeController.getTree();
        if (myTree == null){
            System.out.println("Start a tree. 'java Main init");
            return;
        }
        HashMap<String,String> branches  = myTree.getBranches();
        if(branches ==null){
            System.out.println("No Branches");
            return;
        }
        Boolean untrackfile =  check_untracked("./");
        if(!untrackfile){
            System.out.println("There is an untracked file in the way; delete it or add it first.");
            return;
        }


        if(branches.containsKey(branch_name)){
            myTree.setCurrentBranch(branch_name);
            HashMap<String, String> current_commmit_blobs = CommitController.getCommit(myTree.getBranches().get(myTree.getCurrentBranch())).getBlobs();
            removeFilesFromFolder(new File("./"));
            current_commmit_blobs.forEach((key, value) -> {
                try {
                    FileWriter myWriter = new FileWriter(key);
                    Blob file_blob = BlobController.getBlob(value);
                    String file_con = file_blob.getFileConents();
                    myWriter.write(file_con);
                    myWriter.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                };
              });
              if(StagingAreaController.getStageingArea() !=null){
                StagingAreaController.removeStageingArea();
              }
              myTree.setCurrentBranch(branch_name);
              CommitTreeController.saveTree(myTree);
        }
       

    }
    public static void checkout_file_commit(String commit_id, String filename){
        /*2.java gitlet.Main checkout [commit id] -- [file name]
         * Takes the version of the file as it exists in the commit with the given id, 
         * and puts it in the working directory, overwriting the version of the file that’s
         * already there if there is one. The new version of the file is not staged.
         */
        Commit commit = CommitController.getCommit(commit_id);
        if(commit == null){
            System.out.print("Commit does not exist.");
            return;
        }
        HashMap<String, String> commit_blobs  =  commit.getBlobs();
        if(commit_blobs ==null){
            return ;
        }
        if(!commit_blobs.containsKey(filename)){
            System.out.println("File does not exist in that commit.");
            return;
        }
        Blob file_blob = BlobController.getBlob(commit_blobs.get(filename));

        try {
            File check = new File(filename);
            if(check.exists()){
                check.delete();
            }
            FileWriter myWriter = new FileWriter(new File(filename));
            myWriter.write(file_blob.getFileConents());
            myWriter.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }
    
    public static void checkout_file(String filename){
        
        /* 
         * Usages:

        1.java gitlet.Main checkout -- [file name]
        /// if file name exist in head commit 
        1.
        Takes the version of the file as it exists in the head commit 
        and puts it in the working directory, overwriting the version of 
        the file that’s already there if there is one. 
        The new version of the file is not staged.
         */
        CommitTree myTree = CommitTreeController.getTree();
        if(myTree == null){
            return;
        }
        Commit currentHead =  CommitController.getCommit(myTree.getBranches().get(myTree.getCurrentBranch()));
        if(!currentHead.getBlobs().containsKey(filename)){
            System.out.println("File does not exist in that commit.");
            return;
        }
        File move_file = new File("./"+filename);
        if (move_file.exists()){
            move_file.delete();
        }
        String file_id = currentHead.getBlobs().get(filename);
        Blob file_blob = BlobController.getBlob(file_id);
        try {
            FileWriter newfile = new FileWriter(move_file);
            newfile.write(file_blob.getFileConents());
            newfile.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        

    }
    public static String branch(String newBranchName){
        /*
        Creates a new branch with the given name, 
        and points it at the current head commit. A branch is nothing more
         than a name for a reference (a SHA-1 identifier) to a commit node. 
         This command does NOT immediately switch to the newly created branch (just as in real Git). 
         Before you ever call branch, your code should be running with a default branch called “master”.
         */
        CommitTree myTree  = getTree();
        myTree.addBranch(newBranchName,myTree.getBranches().get(myTree.getCurrentBranch()));
        CommitTreeController.saveTree(myTree);
        return "";

    }
    public static String rm_branch(String brachName){
        /*
         Deletes the branch with the given name. This only means 
         to delete the pointer associated with the branch;
         it does not mean to delete all commits that were created
         under the branch, or anything like that.
         */
        CommitTree myTree  = getTree();
        if(myTree.getBranches().values().contains(brachName)){
            myTree.removeBranch(brachName);
            CommitTreeController.saveTree(myTree);

        }
        return "";

    }
    public static void reset(String commitid){
        /*
         * Checks out all the files tracked by the given commit. Removes tracked files
         *  that are not present in that commit. Also moves the current branch’s head 
         * to that commit node. See the intro for an example of what happens to 
         * the head pointer after using reset. The [commit id] may be abbreviated as
         *  for checkout. The staging area is cleared. The command is essentially checkout 
         * of an arbitrary commit that also changes the current branch head.
         * 
         * 
         */
        Commit commit = CommitController.getCommit(commitid);
        CommitTree myTree  = CommitTreeController.getTree();
        if(myTree == null){
            System.out.println("Gitlet doesn't esit: please use 'gitlet init'");
        }
        if (commit ==null){
            System.out.println("No Commit with that name exist");
            return;
        }
        else{
            removeFilesFromFolder(new File("./"));
            HashMap<String, String> current_commmit_blobs = CommitController.getCommit(myTree.getBranches().get(myTree.getCurrentBranch())).getBlobs();
            current_commmit_blobs.forEach((key, value) -> {
                try {
                    FileWriter myWriter = new FileWriter(key);
                    Blob file_blob = BlobController.getBlob(value);
                    String file_con = file_blob.getFileConents();
                    myWriter.write(file_con);
                    myWriter.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                };
              });
              if(StagingAreaController.getStageingArea() !=null){
                StagingAreaController.removeStageingArea();
              }
            
        }        
        return;
    }
    public static void merge(String branch_name){
        /* Merges files from the given branch into the current branch.*/
        CommitTree myTree = CommitTreeController.getTree();
        HashMap<String,String> bran =  myTree.getBranches();
        if(!bran.containsKey(branch_name)){
            System.out.println("No Branch with that name");
        }
        else if(myTree.getCurrentBranch() == branch_name ){
            System.out.println("Cannot merge a branch with itself.");
        }
        else{
            String splitpoint =  Find_Split(myTree.getBranches().get(myTree.getCurrentBranch()),myTree.getBranches().get(branch_name));
            if(splitpoint == myTree.getBranches().get(myTree.getCurrentBranch())){
                System.out.println("Given branch is an ancestor of the current branch.");
            }
            else{
                Commit Splitpoint_Commit =  CommitController.getCommit(splitpoint);
                Commit currCommit = CommitController.getCommit(bran.get(myTree.getCurrentBranch()));
                Commit branchCommit  = CommitController.getCommit(bran.get(branch_name));
                File folder = new File("./");
                ArrayList<String> files_curr  = listFilesForFolder(folder);
                HashMap<String, String> blobs_at_split = Splitpoint_Commit.getBlobs();
                HashMap<String, String> blobs_at_current_commit = currCommit.getBlobs();
                HashMap<String, String> blobs_at_branch_commit = branchCommit.getBlobs();
                HashSet<String> unique_file = new HashSet<String>();
                blobs_at_split.keySet().forEach((key)-> {
                    unique_file.add(key);
                });
                blobs_at_current_commit.keySet().forEach((key)-> {
                    unique_file.add(key);
                });
                blobs_at_branch_commit.keySet().forEach((key)-> {
                    unique_file.add(key);
                });
                for(String file: unique_file){
                    
                    if (blobs_at_split.containsKey(file) && blobs_at_current_commit.containsKey(file) && blobs_at_branch_commit.containsKey(file)){
                        ///1.
                        if((blobs_at_current_commit.get(file)== blobs_at_split.get(file)) && blobs_at_branch_commit.get(file) != blobs_at_split.get(file)){
                            checkout_file_commit(myTree.getBranches().get(branch_name), file);
                            add(file);
                        }
                        // 2.
                        else if ((blobs_at_current_commit.get(file) != blobs_at_split.get(file)) && blobs_at_branch_commit.get(file) == blobs_at_split.get(file)){
                            //don't do anything
                        }
                        // 3.
                        else if ((blobs_at_current_commit.get(file) != blobs_at_branch_commit.get(file)) && (blobs_at_current_commit.get(file) != blobs_at_split.get(file)) &&blobs_at_branch_commit.get(file) != blobs_at_split.get(file)){
                            // don't do anything 
                        }

                    }

                    // 4.Any files that were not present at the split point and are present only in the 
                    // current branch should remain as they are.
                    else if((!blobs_at_split.containsKey(file)) && (blobs_at_current_commit.containsKey(file)) && (!blobs_at_branch_commit.containsKey(file))){
                        /// don't do anything file should be kept the same
                    }
                    // 5.Any files that were not present at the split point and are present only in the 
                    // given branch should be checked out and staged.
                    else if((!blobs_at_split.containsKey(file)) && (!blobs_at_current_commit.containsKey(file)) && (blobs_at_branch_commit.containsKey(file))){
                        checkout_file_commit(myTree.getBranches().get(branch_name), file);
                        add(file);}

                    // 6.Any files present at the split point, unmodified in the current branch, and 
                    // absent in the given branch should be removed (and untracked).    
                    else if(blobs_at_split.containsKey(file) && blobs_at_current_commit.containsKey(file) && !blobs_at_branch_commit.containsKey(file) && blobs_at_split.get(file)==blobs_at_current_commit.get(file)){
                        rm(file);
                    }
                    else if(blobs_at_split.containsKey(file) && !blobs_at_current_commit.containsKey(file) && blobs_at_branch_commit.containsKey(file) && blobs_at_split.get(file)== blobs_at_branch_commit.get(file)){
                        // file remain absent
                    }
                    else if(blobs_at_current_commit.containsKey(file) && blobs_at_branch_commit.containsKey(file) && (blobs_at_current_commit.get(file) != blobs_at_branch_commit.get(file))){
  
                        deletefile(file);
                        Blob current_file = BlobController.getBlob(blobs_at_current_commit.get(file));
                        Blob branch_file  = BlobController.getBlob(blobs_at_branch_commit.get(file));
                        FileWriter fw;
                        try {
                            fw = new FileWriter(file);
                            fw.write("<<<<<<< HEAD\n"+current_file.getFileConents()+"in current"+myTree.getCurrentBranch()+"\n=======\n"+branch_file.getFileConents()+branch_name+"\n>>>>>>>\n");
                            fw.close();

                        } catch (IOException e1) {
                            //fiel
                            e1.printStackTrace();
                    }
                }
                commit("Merge " +branch_name +"into " + myTree.getCurrentBranch());
            }
        }
    }
}



            // 7.Any files present at the split point, unmodified in the given branch, and absent in 
            // the current branch should remain absent.

            // 8.Any files modified in different ways in the current and given branches are in conflict.
            // “Modified in different ways” can mean that the contents of both are changed and different 
            // from other, or the contents of one are changed and the other file is deleted, or the file was
            // absent at the split point and has different contents in the given and current branches. 
            // In this case, replace the contents of the conflicted file with







    public static String Find_Split(String curcommitid, String branchid)
    {
        // define hashset
        HashSet<String> hs = new HashSet<String>();
        while (curcommitid != null) {
            hs.add(curcommitid);
            curcommitid = CommitController.getCommit(curcommitid).getPrev();
        }
        while (branchid!= null) {
            if (hs.contains(branchid)) {
                return branchid;
            }
            branchid = CommitController.getCommit(branchid).getPrev();
        }
        return null;
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