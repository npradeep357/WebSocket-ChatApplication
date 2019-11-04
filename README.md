[![Build Status](https://travis-ci.com/npradeep357/WebSocket-ChatApplication.svg?branch=master)](https://travis-ci.com/npradeep357/WebSocket-ChatApplication)

# WebSocket-ChatApplication

Chat application built based on web socket protocol using embedded jetty.

The WebSocket protocol reference: https://www.w3.org/TR/websockets/

Embedded Jetty: https://www.eclipse.org/jetty/documentation/current/embedding-jetty.html

# pre-requisites
1) JAVA SE 11 (oracle or openjdk) installed and JAVA_HOME, PATH variables should be configured
2) MAVEN >= 3.6.0 installed and M2_HOME, PATH variables are configured

# Usage
1) clone or download the repository
2) Build the source code using "mvn clean install"
3) Goto target directory in the source code location.
3) open shell/cmd and run the application using "java -jar chat-ws-jar-with-dependencies.jar"

Now you can connect to the websocket server using ws://localhost:8080/chat/ url. You can use "Simple WebSocket Client" chrome or firefox plugin for simple tests. or write your own HTML+JS client.

4) To stop the server either use keyboard shortcut on the cmd/shell - "ctrl+c" or give a POST request to "http://localhost:8080/shutdown?token="

You are welcome to modify the code as you need after clonning/downloading to your local machine.
