import java.net.*;
import java.io.*;

/**
 * BotnetServer Class:
 * Handles communication between client and server as a subsection of the server
 */
public class BotnetServerThread extends Thread{
    // Properties
    private Socket socket;
    private CommandQueue queue;
    

    //Constructor
    public BotnetServerThread(Socket socket){
        super();
        this.socket = socket;
    }

    // getter
    public CommandQueue returnQueue(){
        return this.queue;
    }

    //setter
    public void updateQueue(CommandQueue queue){
        this.queue = queue;
    }

    
    /**
     * processQueue: method used to process the queue contained within the server thread
     * takes out the first command from the queue, sends it to the client for processing, reads it back, and adds it back to the queue
     */
    public void processQueue() {
        //get the in and out streams
        try (ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {
            //if there is a queue
            if(this.queue != null){
                //take the first command
                Command command = this.queue.take();
                //write it to the client
                out.writeObject(command);
                out.flush();

                //read it back from the client
                Command inCommand = (Command) in.readObject();
                //put the command back into the queue
                this.queue.putBack(inCommand);
                    
            }
        //catch errors
        }catch (EOFException e) {
            System.err.println("[-] Client closed the connection.");
        }catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }
    
    //run method of the thread
    public void run(CommandQueue command) {
        while (true) {
            processQueue();
        }
    }

}