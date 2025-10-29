@echo off

echo Lancement du jeu d'échecs...

java -jar chess-game.jar

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo Erreur lors du lancement
    echo Assurez-vous que Java 17 ou supérieur est installé
    echo.
    pause
    exit /b 1
)

echo Jeu terminé

