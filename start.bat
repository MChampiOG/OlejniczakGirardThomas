@echo off
echo Compilation du projet...
mvn clean compile
if %errorlevel% neq 0 (
    echo Erreur lors de la compilation
    pause
    exit /b 1
)

echo Execution du programme...
mvn exec:java -Dexec.mainClass="com.chess.ChessGame"
pause
