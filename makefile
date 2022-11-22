
build: Part2Main.class

Part2Main.class: Part2Main.java
	javac Part2Main.java

run: Part2Main.class
	java -cp .:mssql-jdbc-11.2.0.jre11.jar Part2Main

clean:
	rm Part2Main.class
