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
    private int timeToLive;

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
    public int getTimeToLive(){
        return this.timeToLive;
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
    public void setTimeToLive(int time){
        this.timeToLive = time;
    }


    public void reduceTimeToLive(){
        if (this.timeToLive >= 1){
            this.timeToLive -= 1;
        }else{
            this.timeToLive = 0;
        }
    }
}