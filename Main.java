import utils.add;  //1
import utils.init; //2
import utils.commit; //3
import utils.rm; //4
import utils.log; //5
import utils.global_log; //6
import utils.find; //7
import utils.status; //8
import utils.checkout; //9
import utils.branch; //10
import utils.rm_branch;//11
import utils.reset;//12
import utils.merge; //13




public class Main {
    public static void main(String[] args) {
        String command;
        String[] commands = new String[]{"init","add","commit","rm","log","global-log","find","status","checkout","branch","rm-branch","reset","merge"}; 
        String output = "checking";
        if (args.length > 0){
            command  = args[0];}
        else{command = "None"; output = "Provide and input";}
        switch(command){
        
        // Code to handle init
        // 
            case "init":
                output =  utils.init.INIT();
            break;
        // code to handle add 
            case "add":

            break;
        //  code to handle commit 
            case "commit":

            break;

        // code to handle rm
            case "rm":
            break;

        // code to handle log 
            case "log":

            break;
        // code to handle global-log
            case "global-log":

            break;

        //code to handle find
            case  "find":
            break;

        //code to handle status
            case "status":
            break;

        // code to handle checkout 
            case "checkout":
            break;

        //code to handle branch 
            case "banch":
            break;

        //code to rm-branch 
            case "rm-branch":
            break;

        //code to handle reset
            case "reset":
            break;

        //code to handle merge
            case "merge":
            break;
        }
        System.out.println(output);
    }
}