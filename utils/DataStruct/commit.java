package utils.DataStruct;
import java.io.Serializable;
import utils.DataStruct.Blob;
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
    private HashMap <String, String > blobs;// filename and hash
    private String message;
    private String prev;
    private StageingArea area;
    ///init
    public Commit(){
        ///init commit
        this.date  = new Date(System.currentTimeMillis());
        this.message = "initcommit";
        this.prev = null;
        this.blobs= null; 
    }
    public Commit(String mess) {
        // commit_id = serialize the stageing area
        // for now commit will just incement... 
        this.date  = new Date(System.currentTimeMillis());
        this.message = mess;
        this.prev = null;
        this.blobs = new HashMap<String, String>(); 
    }
    public Date getDate() {
        return this.date;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    /// Might not need this ... might be able to just set blobs to blobs = stages //
    public void appendblob(String filename, String blob) {
        this.blobs.put(filename,blob);
    }

    public String getMessage() {
        return this.message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getPrev() {
        return this.prev;
    }
    public void setPrev(String prev) {
        this.prev = prev;
    }
    public HashMap<String, String> getBlobs() {
        return this.blobs;
    }
    public void setBlobs(HashMap<String, String> filesCommited) {
        this.blobs = filesCommited;
    }
    //Combination of log messges,commit date author,
    // a reference to a tree and references to parent commits 

    
}
