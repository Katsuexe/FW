#!/bin/bash
# compile.sh : Script pour compiler le framework et le projet de test

# Configuration explicite de la version de Java (Java 21 requis pour le projet)
export JAVA_HOME="/usr/lib/jvm/java-21-openjdk"
export PATH="$JAVA_HOME/bin:$PATH"

echo "======================================"
echo "    Compilation du projet complet     "
echo "======================================"

# 1. Compilation et installation du Framework
echo "[1/2] Compilation et installation locale du Framework..."
cd framework
mvn clean install -Dmaven.compiler.parameters=true -DskipTests
if [ $? -ne 0 ]; then
    echo "❌ Erreur lors de la compilation du framework"
    exit 1
fi
cd ..

# 2. Compilation du projet de test
echo "[2/2] Compilation du projet de test..."
cd test-webapp
mvn clean compile -Dmaven.compiler.parameters=true
if [ $? -ne 0 ]; then
    echo "❌ Erreur lors de la compilation du projet de test"
    exit 1
fi
cd ..

echo "✅ Compilation terminée avec succès !"
