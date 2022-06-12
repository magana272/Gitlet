package utils.DataStruct;
import java.io.Serializable;
import java.util.HashMap;
// The stageing is a commit that is not appended to the tree... can still add files...
//
//
//
//
//
//
//
//
public class StageingArea implements Serializable {
    private HashMap<String,Blob> stagearea;
    public StageingArea(){
        HashMap<String,Blob> stagearea = new HashMap<String,Blob>();
    }
    public int stage(String file){
        // check if staged if not and exist
        Blob statgedblob  =  new Blob(file);
        HashMap<String,Blob> stagearea = this.getStage();
        stagearea.put(file,statgedblob);
        return 0;
    }
    public int unstage(String file){
        //check if staged
        //if staged 
        this.stagearea.remove(file);
        return 0; 
    }
    public HashMap<String, Blob> getStage(){
        return this.stagearea;

    }
    public void setNewStage(){
        this.stagearea = new HashMap<String,Blob>();
    }
    @Override
    public int hashCode() {
        return super.hashCode();
    }

}
