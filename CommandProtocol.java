import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;


/**
 * CommandProtocol Class:
 * Object used to process commands on the client side, manipulate command and return an output
 */
public class CommandProtocol {
    //STATES
    private static final int WAITING = 0;
    private static final int PROCESSING_COMMAND = 1;
    private static final String OPEN_COMMAND = "open";

    private int state = PROCESSING_COMMAND;


    /**
     * processCommand - run the command of the input and mutate the commands response
     * @param command - the command to process
     * @return - the mutated command 
     */
    public String processCommand(Command command){    
        Boolean isProcessed = false;
        String theOutput = null;

        if (state == WAITING){
            command.setResponse("Connected");;
            state = PROCESSING_COMMAND;
        }else if (state == PROCESSING_COMMAND){
            String instructions = command.getInstructions();
            
            switch (command.getCommandName()){
                case "openApp":
                    if (openApp(instructions)){
                        command.setResponse("App " + instructions + " opened");
                        isProcessed = true;
                    }else{
                        command.setResponse("App " + instructions + " not opened");
                    }
                    break;
                case "status":
                    String response = status_check();
                    if(response != ""){
                        command.setResponse(response);
                        isProcessed = true;
                    }else{
                        command.setResponse("Client inactive");
                    }
                    break;
                case "exit":
                    command.setResponse("exiting");
                    isProcessed = true;
                    break;
                default:
                    isProcessed = false;   
            }
            if (!isProcessed){
                state = WAITING;
            }
        }

        if (isProcessed){
            theOutput = command.getResponse();
            command.setIsExecutedTrue();
            command.setErrorFalse();
        }else{
            command.setIsExecutedTrue();
            command.setErrorTrue();
        }
        
        return theOutput;
    }

    /**
     * openApp - open the app of the commands instructions on the client side
     * @param appName - the name of the app to open
     * @return - true if executed false if not
     */
    private boolean openApp(String appName){
        try {
            String osName = System.getProperty("os.name").toLowerCase();
            ProcessBuilder pb;
            if (osName.contains("win")) {
                pb = new ProcessBuilder(appName);
            } else if (osName.contains("nix") || osName.contains("nux") || osName.contains("aix") || osName.contains("mac")) {
                pb = new ProcessBuilder(OPEN_COMMAND, "-a", appName);
            } else {
                System.out.println("Unsupported operating system: " + osName);
                return false;
            }

            pb.start();
            return true;
        } catch (IOException e) {
            System.err.println("[-]Error opening " + appName +": " + e.getMessage());
            return false;
        }
    }
    
    /**
     * status_check - sends the clients OS and uptime status to the server
     * @return - true if executed false if not
     */
    private String status_check(){
         try {
            InetAddress localhost = InetAddress.getLocalHost();
            String ipAddress = localhost.getHostAddress();
            String osName = System.getProperty("os.name");
            String javaVersion = System.getProperty("java.version");
            String userName = System.getProperty("user.name");
            String status = "\n\t[*]IP ADDRESS: "+ipAddress+"\n\t[*]OPERATING SYSTEM: "+osName+"\n\t[*]JAVA VERSION: "+javaVersion+"\n\t[*]USER NAME: "+userName+"\n\t[*]STATUS: Active";
            return status;
        } catch (UnknownHostException e) {
            System.err.println("[-]Unable to determine IP address: " + e.getMessage());
            return "";
        }
    }
    
}
