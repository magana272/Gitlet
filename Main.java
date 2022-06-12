import utils.CommitTreeController;
import utils.StagingAreaController;
public class Main {
    public static void main(String[] args) {
        String command;
        String[] commands = new String[]{"init","add","commit","rm","log","global-log","find","status","checkout","branch","rm-branch","reset","merge"}; 
        String output = "checking";
        if (args.length > 0){
            command  = args[0];
            System.out.println(command);}
        else{command = "None"; output = "Please enter a command.";}
        // TO-DO  : does .init exist ? arg[0] not init : unitalized git init. 
        switch(command){
        
        // Code to handle init
        // 
            case "init":
                output = CommitTreeController.init();
            break;
        // code to handle add 
            case "add":
                // check if file in curr dir 
                output = CommitTreeController.add(args[1]);
                // else sorry
            break;
        //  code to handle commit 
            case "commit":
            /// quick checks 
            /// args[1] == -m 
            /// arg [2] == "Some String"
            /// additionally we want wanto to make sure .gitlet/Stagedfile 
            output = CommitTreeController.commit(args[2]);
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

            default:
            output = "No command with that name exists.";
            break;
        }
        System.out.println(output);
        return;
    }
}