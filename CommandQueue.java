import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class CommandQueue implements Serializable{
    private List<Command> commandList = new ArrayList<>();
    

    public synchronized Command take() {
        // System.out.println("in the take method");
        // System.out.println("CURRENT COMMAND LIST IN TAKE " + this.commandList);
        while (this.commandList.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {}
        }
        
        notifyAll();

        // if(this.commandList.get(0).getIsExecuted() && this.commandList.size() > 1){
        //     this.commandList.remove(0);
        // }

        //FIND A WAY TO SYNC THE COMMANDS SO THAT IT GOES THROUGH ALL CLIENTS AND THEN GETS REMOVED
        if(this.commandList.get(0).isFinished()){
            this.commandList.remove(0);
        }
        return this.commandList.get(0);
        
    }

    public synchronized void put(Command command){
        // System.out.println("in the put method");
        // System.out.println("CURRENT COMMAND LIST IN PUT "+this.commandList);
        // System.out.println("empty list status " + emptyList);
        
        // while (!this.commandList.isEmpty()){
        //     try{
        //         wait();
        //     } catch (InterruptedException e){}
        // }

        
        this.commandList.add(command);
        // System.out.println("COMMANDS IN THE QUEUE: " + this.commandList);
        notifyAll();
    }
}
