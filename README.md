# Botnet Server Client

## How to run
* compile the server
    ```
    javac BotnetServer.java Command.java CommandProtocol.java CommandQueue.java
    ```
    or using makefile
    ```
    make server
    ```
* compile the client
   ```
    javac BotnetClient.java Command.java CommandProtocol.java
    ```
    or using makefile
    ```
    make client
    ```
* run the server
    ```
    java BotnetServer.java <PORTNUMBER>
    ```
* run the client
    ```
    java BotnetClient.java localhost <PORTNUMBER>
    ```
* using the server: send commands to the client
    ```
    ------
    Enter a Command name 
    1)open app
    2)status
    3)exit
    ------

    > (ENTER COMMAND NUMBER HERE)
    ```

## Usage
### Botnet Server
* Send commands to client
* Commands:
    * open app:
        * will prompt user for app name and open it on client side
        * if the app does not exist client will close connection (on windows operating systems)
    * status:
        * returns ip address, operating system, java version, username, and status of client
    * exit:
        * will close connection on both ends
### Botnet Client
* processes commands sent by server to return mutated command
### Command Protocol
* mutates commands according to algorithm
### Command
* object that hold properties, setters, and getters for execution