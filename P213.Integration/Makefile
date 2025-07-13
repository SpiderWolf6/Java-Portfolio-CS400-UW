startServer: 
	javac WebApp.java
	java WebApp 8000

runAllTests:
	javac -cp .:../junit5.jar BackendTests.java
	java -jar ../junit5.jar -cp . -c BackendTests

clean:
	rm *.class
