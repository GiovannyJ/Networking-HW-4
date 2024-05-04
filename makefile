server:
	javac BotnetServer.java Command.java CommandProtocol.java
runserver:
	java -cp . .\BotnetServer.java 2002

client:
	javac BotnetClient.java Command.java CommandProtocol.java

runclient:
	java -cp . .\BotnetClient.java localhost 2002