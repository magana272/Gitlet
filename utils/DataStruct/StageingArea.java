package utils.DataStruct;
import java.io.File;
import java.io.IOError;
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
        stagearea = this.getStage();
        this.stagearea.put(file, hash);
        return 0;
    }
    public int unstage(String file){
        //check if staged
        //if staged 
        if (this.stagearea.containsKey(file)){
            File unstage = new File(".gitlet/Stage/"+this.stagearea.get(file));
            unstage.delete();
            this.stagearea.remove(file);
            if(stagearea.keySet().toArray().length == 0){
                File filedir = new File(".gitlet/stage");
                File stagefile = new File(".gitlet/StageingArea");
                stagefile.delete();
                filedir .delete();
            }
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
