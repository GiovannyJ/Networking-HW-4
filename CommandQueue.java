import java.net.*;
import java.util.Arrays;
import java.io.*;
import java.io.Serializable;


public class CommandQueue implements Serializable{
    private Command[] commandList;
    private boolean emptyList = commandList.length > 0;

    public synchronized Command take() {
        while (emptyList) {
            try {
                wait();
            } catch (InterruptedException e) {
                // Handle interrupted exception
            }
        }

        emptyList = commandList.length == 1;
        notifyAll();

        Command firstCommand = commandList[0];

        System.arraycopy(commandList, 1, commandList, 0, commandList.length - 1);
        commandList = Arrays.copyOf(commandList, commandList.length - 1); // Resize the array

        return firstCommand;
    }

    public synchronized void put(Command command){
        while (!emptyList){
            try{
                wait();
            } catch (InterruptedException e){}
        }

        Command[] newList = new Command[commandList.length + 1];
        System.arraycopy(commandList, 0, newList, 0, commandList.length);
        newList[commandList.length] = command;
        commandList = newList;
        emptyList = commandList.length > 0;

        notifyAll();
    }
}
