package utils.DataStruct;

import java.io.Serializable;
import java.util.ArrayList;

public class StageingArea implements Serializable {
    ArrayList<String> staged;
    public StageingArea(){
        String[] staged = null ;
    }
    public int stage(String file){
        // check if staged if not and exist
        this.staged.add(file);
        return 0;
    }
    public int unstage(String file){
        //check if staged
        //if staged 
        this.staged.remove(file);
        return 0; 
    }


}
