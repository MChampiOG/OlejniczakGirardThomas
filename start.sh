#!/bin/bash
echo "Compilation du projet..."
mvn clean compile

echo "Lancement du jeu..."
export DISPLAY=:0
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
/usr/lib/jvm/java-17-openjdk-amd64/bin/java -cp target/classes com.chess.ChessGame