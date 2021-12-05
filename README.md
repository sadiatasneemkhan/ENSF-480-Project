# setup

1. make sure MySQL is running. enter interactive mysql (`mysql -uroot -p` in terminal), then run `source setup.sql`.
2. install maven https://maven.apache.org/install.html
3. run `mvn package` to install dependencies and compile
4. run `mvn exec:java` to run everything in App.main