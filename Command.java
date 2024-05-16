import java.io.Serializable;

/**
 * Command Class:
 * Serializable Object used to contain information for execution of commands
 */
public class Command implements Serializable{
    /**
     * Properties
     */
    private String commandName;
    private String instructions = "";
    private String response = "";
    private Boolean error = false;
    private Boolean isExecuted = false;
    private Boolean finished = false;
    private int maxLife = 0;
    private int currentLife = 0;

    /**
     * Constructor
     */
    public Command(String commandName){
        this.commandName = commandName;
    }

    /**
    *Getters
     */
    public String getCommandName(){
        return this.commandName;
    }
    public String getInstructions(){
        return this.instructions;
    }
    public String getResponse(){
        return this.response;
    }
    public Boolean getErrorStatus(){
        return this.error;
    }
    public Boolean getIsExecuted(){
        return this.isExecuted;
    }
    public Boolean isFinished(){
        return this.finished;
    }
    public int getMaxLife(){
        return this.maxLife;
    }
    public int getCurrentLife(){
        return this.currentLife;
    }



    /**
     * Setters
     */
    public void setCommand(String commandName){
        this.commandName = commandName;
    }
    public void setInstructions(String instructions){
        this.instructions = instructions;
    }
    public void setResponse(String response){
        this.response = response;
    }
    public void setErrorTrue(){
        this.error = true;
    }
    public void setErrorFalse(){
        this.error = false;
    }
    public void setIsExecutedTrue(){
        this.isExecuted = true;
    }
    public void setIsExecutedFalse(){
        this.isExecuted=false;
    }
    public void setIsFinishedTrue(){
        this.finished=true;
    }
    public void setIsFinishedFalse(){
        this.finished=false;
    }
    public void setMaxLife(int life){
        this.maxLife = life;
    }
    public void increaseCurrentLife(){
        this.currentLife++;
    }
}