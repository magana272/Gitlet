package utils.DataStruct;
import java.io.Serializable;
import java.sql.Blob;
import java.util.Date;
import java.util.HashMap;
public class Commit implements Serializable{
    ///
    //
    //which must include the file (blob) references of its files, parent reference, log message, and commit time
    //
    // the the tree controller can set the ID and get ID;
    ///
    //
    //
    private Date date;
    private HashMap <Integer,Commit> connections;
    private HashMap <Integer, Blob> files;
    private String message;
    private Commit prev;
    private String prev_id; 
    private String commit_id; // does this need to know it's own ID ?
    ///init
    public Commit(){
        ///init commit
        date  = new Date(System.currentTimeMillis());
        message = "initcommit";
        prev = null;
        connections = null;
        files = null; 

    }
    public Commit(String mess, StageingArea file) {
        // commit_id = serialize the stageing area
        // for now commit will just incement... 
        commit_id = null; 
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
    public HashMap<Integer,Commit> getConnections() {
        return connections;
    }
    public void setConnections(HashMap<Integer, Commit> connections) {
        this.connections = connections;
    }
    public HashMap<Integer, Blob> getFiles() {
        return files;
    }
    public void setFiles(HashMap<Integer, Blob> files) {
        this.files = files;
    }
    public String getCommit_id() {
        return commit_id;
    }
    public void setCommit_id(String commit_id) {
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
    @Override
    public int hashCode() {
        return super.hashCode();
    }
    
}
