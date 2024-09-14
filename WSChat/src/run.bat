@ECHO OFF

javac Server.java
javac Client.java

start cmd /c "java Server"

start cmd /c "java Client"
start cmd /c "java Client"
start cmd /c "java Client"