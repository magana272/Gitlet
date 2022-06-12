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
    private HashMap <String, Blob> blobs;
    private String message;
    private String prev;
    private StageingArea area;
    ///init
    public Commit(){
        ///init commit
        date  = new Date(System.currentTimeMillis());
        message = "initcommit";
        prev = null;
        blobs= null; 
        area = null;

    }
    public Commit(String mess) {
        // commit_id = serialize the stageing area
        // for now commit will just incement... 
        date  = new Date(System.currentTimeMillis());
        message = mess;
        prev = null;
        HashMap <String, Blob> blobs = new HashMap<String, Blob>(); 
    }
    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    public HashMap <String, Blob> getblobs() {
        return blobs;
    }
    /// Might not need this ... might be able to just set blobs to blobs = stages //
    public void appendblob(String filename, Blob blob) {
        this.blobs.put(filename,blob);
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getPrev() {
        return prev;
    }
    public void setPrev(String prev) {
        this.prev = prev;
    }
    public void setArea(StageingArea stage){
        this.area = stage;
    }
    public HashMap<String, Blob> getBlobs() {
        return blobs;
    }
    public void setBlobs(HashMap<String, Blob> filesCommited) {
        this.blobs = filesCommited;
    }
    //Combination of log messges,commit date author,
    // a reference to a tree and references to parent commits 
    @Override
    public int hashCode() {
        return super.hashCode();
    }
    
}
