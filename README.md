# ChessMaster
Steps to run the project :-
1. Clone or download this git repo
2. cd into the directory where pom.xml of this project is present.
3. mvn clean
4. mvn clean install
5. java -jar target/ChessMaster-1.0-SNAPSHOT-jar-with-dependencies.jar 127.0.0.1 5000 6000 true
6. Open another terminal and type the following command
   
   java -jar target/ChessMaster-1.0-SNAPSHOT-jar-with-dependencies.jar 127.0.0.1 6000 5000 false