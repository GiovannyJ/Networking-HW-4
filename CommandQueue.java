import java.net.*;
import java.util.Arrays;
import java.io.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class CommandQueue implements Serializable{
    private List<Command> commandList = new ArrayList<>();
    // private Command command;
    private boolean emptyList = true;

    public synchronized Command take() {
        // System.out.println("in the take method");
        // System.out.println("CURRENT COMMAND LIST IN TAKE " + this.commandList);
        // System.out.println("empty list status " + emptyList);
        while (emptyList) {
            try {
                wait();
            } catch (InterruptedException e) {}
        }
        
        // System.out.println("empty list is true");
        emptyList = true;
        

        // System.out.println("notifying all");
        notifyAll();

        if (this.commandList.get(0).getIsExecuted() && this.commandList.size() >= 1){
            this.commandList.remove(0);
        }

        return this.commandList.get(0);
        
    }

    public synchronized void put(Command command){
        // System.out.println("in the put method");
        // System.out.println("CURRENT COMMAND LIST IN PUT "+this.commandList);
        // System.out.println("empty list status " + emptyList);
        
        while (!emptyList){
            try{
                wait();
            } catch (InterruptedException e){}
        }
        
        emptyList = false;
        
        if(command.getIsExecuted()){
            this.commandList.add(0,command);
        }else{
            this.commandList.add(command);
        }

        notifyAll();
    }
}
