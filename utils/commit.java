package utils;

import utils.DataStruct.CommitTree;

public abstract class commit {
    public static void create_init(){
    }
    public static void commitFinal(String path){
        CommitTree COMMIT = new CommitTree();
        COMMIT.save(path);
    }

}
