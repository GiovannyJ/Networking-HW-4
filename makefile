server:
	javac BotnetMultiServer.java BotnetServerThread.java Command.java CommandProtocol.java CommandQueue.java
rs:
	java -cp . .\BotnetMultiServer.java 2002

client:
	javac BotnetClient.java Command.java CommandProtocol.java CommandQueue.java

rc:
	java -cp . .\BotnetClient.java localhost 2002

runclient1:
	java -cp . .\BotnetClient.java localhost 2002

e: server client