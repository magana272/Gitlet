package utils.DataStruct;
import java.io.Serializable;
import java.sql.Blob;
import java.util.Date;
import java.util.HashMap;
public class commit implements Serializable{
    private Date date;
    public static int global_commit_id = 0;
    private HashMap <Integer,commit> connections;
    private HashMap <Integer, Blob> files;
    private String message;
    private commit prev;
    private int commit_id;
    ///init
    public commit(){
        ///init commit
        commit_id = global_commit_id; 
        global_commit_id++;
        date  = new Date(System.currentTimeMillis());
        message = "initcommit";
        prev = null;
        connections = null;
        files = null; 

    }
    public commit(String mess, StageingArea file) {
        // commit_id = serialize the stageing area
        // for now commit will just incement... 
        commit_id = global_commit_id; 
        global_commit_id++;
        date  = new Date(System.currentTimeMillis());
        message = mess;
        prev = null;
        connections = null;
        files = null; 
    }
    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    public HashMap<Integer, commit> getConnections() {
        return connections;
    }
    public void setConnections(HashMap<Integer, commit> connections) {
        this.connections = connections;
    }
    public HashMap<Integer, Blob> getFiles() {
        return files;
    }
    public void setFiles(HashMap<Integer, Blob> files) {
        this.files = files;
    }
    public int getCommit_id() {
        return commit_id;
    }
    public void setCommit_id(int commit_id) {
        this.commit_id = commit_id;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    //Combination of log messges,commit date author,
    // a reference to a tree and references to parent commits 
    
    
}
