import utils.CommitTreeController;
import utils.StagingAreaController;
public class Main {
    public static void main(String[] args) {
        String command;
        String[] commands = new String[]{"init","add","commit","rm","log","global-log","find","status","checkout","branch","rm-branch","reset","merge"}; 
        String output = "checking";
        if (args.length > 0){
            command  = args[0];
            }
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
            if(args.length != 3 ){
                output = "Wrong number of arguments";
            }
            if(!args[1].equals("-m")){
                output = "Please enter a commit message.";
            }
            else{
                output = CommitTreeController.commit(args[2]);
            }
            break;

        // code to handle rm
            case "rm":
            output = CommitTreeController.rm(args[1]);
            break;

        // code to handle log 
            case "log":
                CommitTreeController.log();
                return;
            // code to handle global-log
            case "global-log":
                CommitTreeController.global_log();
            break;

        //code to handle find
            case  "find":
            CommitTreeController.find(args[1]);
            break;

        //code to handle status
            case "status":
            CommitTreeController.status();
            break;

        // code to handle checkout 
            case "checkout":
            if (args.length == 2 ){
                CommitTreeController.checkout_branch(args[1]);

            }
            else if (args.length == 3){
                // checkout -- [file name]
                CommitTreeController.checkout_file(args[2]);

            }
            else if(args.length ==4){
                //checkout [commit id] -- [file name]
                CommitTreeController.checkout_file_commit(args[1], args[3]);
            }

            break;

        //code to handle branch 
            case "branch":
            CommitTreeController.branch(args[1]);
            break;

        //code to rm-branch 
            case "rm-branch":
            CommitTreeController.rm_branch(args[1]);
            break;

        //code to handle reset
            case "reset":
            CommitTreeController.reset(args[1]);
            break;

        //code to handle merge
            case "merge":
            CommitTreeController.merge(args[1]);
            break;

            default:
            output = "No command with that name exists.";
            break;
        }
        return;
    }
}