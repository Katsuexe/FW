#!/bin/bash
# deploy.sh : Script pour construire les exécutables (JAR/WAR) et déployer sur Tomcat

# Configuration explicite de la version de Java (Java 21 requis pour le projet)
export JAVA_HOME="/usr/lib/jvm/java-21-openjdk"
export PATH="$JAVA_HOME/bin:$PATH"

echo "======================================"
echo "       Déploiement vers Tomcat        "
echo "======================================"

# Chemin Tomcat : pris depuis l'argument $1, la variable d'environnement, ou le chemin par défaut
if [ -n "$1" ]; then
    TOMCAT_WEBAPPS="$1"
elif [ -z "$TOMCAT_WEBAPPS" ]; then
    if [ -d "/var/lib/tomcat9/webapps" ]; then
        TOMCAT_WEBAPPS="/var/lib/tomcat9/webapps"
    elif [ -d "$HOME/Tomcat/tomcat/apache-tomcat-10.0.16/webapps" ]; then
        TOMCAT_WEBAPPS="$HOME/Tomcat/tomcat/apache-tomcat-10.0.16/webapps"
    else
        TOMCAT_WEBAPPS="$HOME/apache-tomcat/webapps"
    fi
fi
WAR_NAME="test-webapp.war"

# 1. Packaging et installation locale du framework
echo "[1/3] Packaging et installation du Framework (génération du .jar)..."
cd framework
mvn clean install -Dmaven.compiler.parameters=true -DskipTests
if [ $? -ne 0 ]; then
    echo "❌ Erreur lors de l'installation du framework"
    exit 1
fi
cd ..

# 2. Packaging du projet test en WAR
echo "[2/3] Packaging du projet de Test (génération du .war)..."
cd test-webapp
mvn clean package -Dmaven.compiler.parameters=true -DskipTests
if [ $? -ne 0 ]; then
    echo "❌ Erreur lors de la création du WAR de test"
    exit 1
fi
cd ..

# 3. Copie vers Tomcat
echo "[3/3] Déploiement vers Tomcat..."
if [ -d "$TOMCAT_WEBAPPS" ]; then
    cp test-webapp/target/*.war "$TOMCAT_WEBAPPS/$WAR_NAME"
    echo "✅ Déploiement réussi dans : $TOMCAT_WEBAPPS/$WAR_NAME"
    echo "Vous pouvez vérifier l'application sur : http://localhost:8080/test-webapp"
else
    echo "⚠️  ATTENTION : Le dossier Tomcat n'a pas été trouvé à l'emplacement :"
    echo "   $TOMCAT_WEBAPPS"
    echo "   Veuillez modifier la variable TOMCAT_WEBAPPS dans le script deploy.sh."
    exit 1
fi
