#!/bin/bash

# ================================================
# SCRIPT DE INICIO RÁPIDO - TIENDA ONLINE
# ================================================

clear
echo "================================================"
echo "  🚀 CLOTHING STORE - TIENDA ONLINE"
echo "================================================"
echo ""

# Verificar que estamos en la carpeta correcta
if [ ! -f "pom.xml" ]; then
    echo "❌ Error: Ejecuta este script desde la carpeta tiendaOnline"
    exit 1
fi

# Verificar Java
echo "1️⃣ Verificando Java..."
if ! command -v java &> /dev/null; then
    echo "❌ Java no está instalado"
    echo "   Instalar: https://adoptium.net/"
    exit 1
fi
java -version 2>&1 | head -1
echo ""

# Verificar Maven
echo "2️⃣ Verificando Maven..."
if ! command -v mvn &> /dev/null; then
    echo "❌ Maven no está instalado"
    echo "   macOS: brew install maven"
    exit 1
fi
mvn -version | head -1
echo ""

# Verificar MySQL
echo "3️⃣ Verificando MySQL..."
if ! command -v mysql &> /dev/null; then
    echo "⚠️  MySQL no detectado en PATH"
    echo "   Asegúrate que MySQL esté corriendo"
else
    mysql --version
fi
echo ""

# Compilar y ejecutar
echo "4️⃣ Compilando y ejecutando aplicación..."
echo "   Esto puede tardar 1-2 minutos la primera vez..."
echo ""

mvn clean package cargo:run

# Si falla, mostrar ayuda
if [ $? -ne 0 ]; then
    echo ""
    echo "================================================"
    echo "  ❌ ERROR AL INICIAR"
    echo "================================================"
    echo ""
    echo "Posibles causas:"
    echo "1. MySQL no está corriendo"
    echo "2. Base de datos 'tienda_online' no existe"
    echo "3. Credenciales incorrectas en ConexionBD.java"
    echo "4. Puerto 8080 ya está en uso"
    echo ""
    echo "Soluciones:"
    echo "- Iniciar MySQL"
    echo "- Ejecutar: mysql -u root -p < database/INSTALL_DB.sql"
    echo "- Verificar ConexionBD.java"
    echo "- Liberar puerto: lsof -ti:8080 | xargs kill -9"
    echo ""
    exit 1
fi
