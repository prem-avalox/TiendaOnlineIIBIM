#!/bin/bash

# ================================================
# Script de instalación automática del JDBC Driver
# Para macOS/Linux
# ================================================

echo "================================================"
echo "  INSTALACIÓN DE MySQL JDBC DRIVER"
echo "================================================"
echo ""

# Variables
JDBC_VERSION="8.2.0"
DOWNLOAD_URL="https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/${JDBC_VERSION}/mysql-connector-j-${JDBC_VERSION}.jar"
PROJECT_DIR="/Users/martin/eclipse-workspace/tiendaOnline"
LIB_DIR="${PROJECT_DIR}/src/main/webapp/WEB-INF/lib"
JAR_FILE="mysql-connector-j-${JDBC_VERSION}.jar"

# Crear directorio lib si no existe
echo "📁 Creando directorio lib..."
mkdir -p "${LIB_DIR}"

# Verificar si ya existe
if [ -f "${LIB_DIR}/${JAR_FILE}" ]; then
    echo "✅ El archivo ${JAR_FILE} ya existe"
    echo "   Ruta: ${LIB_DIR}/${JAR_FILE}"
    read -p "¿Deseas descargarlo nuevamente? (s/n): " respuesta
    if [ "$respuesta" != "s" ]; then
        echo "Operación cancelada"
        exit 0
    fi
    rm -f "${LIB_DIR}/${JAR_FILE}"
fi

# Descargar el JAR
echo "⬇️  Descargando MySQL JDBC Driver ${JDBC_VERSION}..."
echo "   URL: ${DOWNLOAD_URL}"
echo ""

if command -v curl &> /dev/null; then
    curl -L -o "${LIB_DIR}/${JAR_FILE}" "${DOWNLOAD_URL}"
elif command -v wget &> /dev/null; then
    wget -O "${LIB_DIR}/${JAR_FILE}" "${DOWNLOAD_URL}"
else
    echo "❌ Error: No se encontró curl ni wget"
    echo "   Instala uno de estos:"
    echo "   - brew install curl"
    echo "   - brew install wget"
    exit 1
fi

# Verificar descarga
if [ -f "${LIB_DIR}/${JAR_FILE}" ]; then
    FILE_SIZE=$(du -h "${LIB_DIR}/${JAR_FILE}" | cut -f1)
    echo ""
    echo "✅ Descarga completada"
    echo "   Archivo: ${JAR_FILE}"
    echo "   Tamaño: ${FILE_SIZE}"
    echo "   Ubicación: ${LIB_DIR}"
    echo ""
    echo "================================================"
    echo "  PRÓXIMOS PASOS"
    echo "================================================"
    echo "1. Abre Eclipse"
    echo "2. Clic derecho en el proyecto → Refresh (F5)"
    echo "3. Clic derecho en el proyecto → Properties"
    echo "4. Java Build Path → Libraries → Add JARs..."
    echo "5. Selecciona: src/main/webapp/WEB-INF/lib/${JAR_FILE}"
    echo "6. Apply and Close"
    echo "7. Clean y rebuild el proyecto"
    echo ""
    echo "✅ ¡Listo para usar MySQL!"
    echo "================================================"
else
    echo ""
    echo "❌ Error en la descarga"
    echo "   Descarga manualmente desde:"
    echo "   https://dev.mysql.com/downloads/connector/j/"
    echo ""
    echo "   Y copia el JAR a:"
    echo "   ${LIB_DIR}"
fi
