#!/bin/bash
# build_and_deploy.sh : Appel successif de la compilation puis du déploiement

echo "🚀 Lancement du processus complet (Compilation + Déploiement)..."

# Rendre les scripts exécutables
chmod +x compile.sh deploy.sh

# 1. On appelle le script de compilation
./compile.sh
if [ $? -ne 0 ]; then
    echo "❌ Arrêt du processus suite à une erreur de compilation."
    exit 1
fi

echo "--------------------------------------"

# 2. On appelle le script de déploiement
./deploy.sh
if [ $? -ne 0 ]; then
    echo "❌ Arrêt du processus suite à une erreur de déploiement."
    exit 1
fi

echo "🎉 Processus complet terminé avec succès !"
