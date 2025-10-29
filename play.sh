#!/bin/bash

echo "Lancement du jeu d'échecs..."

# Configuration de l'affichage
export DISPLAY=:0

# Utiliser Java 17 (qui a le support graphique complet)
JAVA_17="/usr/lib/jvm/java-17-openjdk-amd64/bin/java"

# Si Java 17 n'est pas disponible, utiliser la version par défaut
if [ ! -f "$JAVA_17" ]; then
    echo "Java 17 non trouvé, utilisation de la version par défaut"
    JAVA_17="java"
fi

# Lancer le jeu
$JAVA_17 -jar chess-game.jar

echo "Jeu terminé"

