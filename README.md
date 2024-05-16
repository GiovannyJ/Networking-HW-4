# Botnet Server Client


## Current Status
#### Botnet Multi Server
* Create command to add to queue which will be passed to each server thread
* Commands:
    * open app:
        * will prompt user for app name and open it on client side
        * if the app does not exist client will close connection (on windows operating systems)
    * status:
        * returns ip address, operating system, java version, username, and status of client
    * exit:
        * will close connection on both ends
#### Botnet Server Thread
* Send commands to client(s) from the command queue
#### Botnet Client
* processes commands sent by server to return mutated command
#### Command Protocol
* mutates commands according to algorithm
#### Command
* object that holds properties, setters, and getters for execution
#### Command Queue
* object that holds a list of commands to be executed by the clients
* is a shared resource
## Features
* Ability to connect multiple clients
* Ability to send commands to multiple clients


## Future Enhancements
* Ability to keep connection persistent past 1 command
* Ability to add commands to queue in batch
* More commands


## How to run
* compile the server
    ```
    javac BotnetMultiServer.java BotnetServerThread.java Command.java CommandProtocol.java CommandQueue.java
    ```
    or using makefile
    ```
    make server
    ```
* compile the client
   ```
    javac BotnetClient.java Command.java CommandProtocol.java CommandQueue.java
    ```
    or using makefile
    ```
    make client
    ```
* run the server
    ```
    java BotnetMultiServer.java <PORTNUMBER>
    ```
* run the client
    ```
    java BotnetClient.java localhost <PORTNUMBER>
    ```
* using the server: send commands to the client(s)
    ```
    ------
    Enter a Command name 
    1)open app
    2)status
    3)exit
    ------

    > (ENTER COMMAND NUMBER HERE)
    ```
