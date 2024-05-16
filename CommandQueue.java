import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * CommandQueue class:
 * class used to store commands, is shared by all threads
 */
public class CommandQueue implements Serializable {
    //property
    private List<Command> commandList = new ArrayList<>();

    /**
     * take: method used to get the first item from the queue
     * if there is nothing in the queue then the thread will wait
     * if the first command is finished it will remove it from the queue
     * @return: the first command in the list
     */
    public synchronized Command take() {
        while (this.commandList.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {}
        }

        if (this.commandList.get(0).isFinished()) {
            // System.out.println("[+]TEEHEE");
            this.commandList.remove(0);
            notifyAll();
            if (this.commandList.isEmpty()) {
                return null;
            }
        }

        return this.commandList.get(0);
    }

    /**
     * put: method used to add a command to the queue
     * @param command: command to add to the queue
     */
    public synchronized void put(Command command) {
        this.commandList.add(command);
        notifyAll();
    }

    /**
     * putBack: method used to put the command back in the front of queue
     * @param command the command to put back in the front of the queue
     */
    public synchronized void putBack(Command command) {
        this.commandList.remove(0);
        this.commandList.add(0, command);
        notifyAll();
    }

    /**
     * status_of_queue: method used to look at the name, current life, and finished status of the commands in the queue
     */
    public void status_of_queue(){
        for (Command command : this.commandList) {
            System.out.println(command.getCommandName());
            System.out.println(command.getCurrentLife());
            System.out.println(command.isFinished());
        }
    }
    
    /**
     * peek: method used to look at the first item in the list without removing it
     * @return: first item in the list without removing it
     */
    public synchronized Command peek() {
        return this.commandList.isEmpty() ? null : this.commandList.get(0);
    }
}
