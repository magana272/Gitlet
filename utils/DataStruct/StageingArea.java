package utils.DataStruct;
import java.io.Serializable;
import java.util.HashMap;
// The stageing is a commit that is not appended to the tree... can still add files...

public class StageingArea implements Serializable {
    private HashMap<String,String> stagearea;
    public StageingArea(){
        this.stagearea = new HashMap<String,String>();
    }
    public int stage(String file, String hash){
        // check if staged if not and exist
        HashMap<String,String> stagearea = this.getStage();
        this.stagearea.put(file, hash);
        return 0;
    }
    public int unstage(String file){
        //check if staged
        //if staged 
        if (this.stagearea.containsKey(file)){
            this.stagearea.remove(file);
            return 0;
        }
        else{
            return 1;
        }
       
       
    }
    public HashMap<String,String> getStage(){
        return this.stagearea;

    }
    public void setNewStage(){
        this.stagearea = new HashMap<String,String>();
    }
    @Override
    public int hashCode() {
        return super.hashCode();
    }

}
