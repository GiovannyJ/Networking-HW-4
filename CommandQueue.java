import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CommandQueue implements Serializable {
    private List<Command> commandList = new ArrayList<>();

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

    public synchronized void put(Command command) {
        this.commandList.add(command);
        // System.out.println(this.commandList);
        notifyAll();
    }

    public synchronized void putBack(Command command) {
        this.commandList.remove(0);
        this.commandList.add(0, command);
        // System.out.println(this.commandList);
        notifyAll();
    }


    public void status_of_queue(){
        for (Command command : this.commandList) {
            System.out.println(command.getCommandName());
            System.out.println(command.getCurrentLife());
            System.out.println(command.isFinished());
        }
    }

    public synchronized boolean isEmpty() {
        return this.commandList.isEmpty();
    }

    public synchronized Command peek() {
        return this.commandList.isEmpty() ? null : this.commandList.get(0);
    }

    public synchronized void removeFirst(){
        this.commandList.remove(0);
    }

    public int size(){
        return this.commandList.size();
    }
}
