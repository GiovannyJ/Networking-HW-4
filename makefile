server:
	javac BotnetMultiServer.java BotnetServerThread.java Command.java CommandProtocol.java CommandQueue.java
runserver:
	java -cp . .\BotnetMultiServer.java 2002

client:
	javac BotnetClient.java Command.java CommandProtocol.java CommandQueue.java

runclient:
	java -cp . .\BotnetClient.java localhost 2002

runclient1:
	java -cp . .\BotnetClient.java localhost 2002