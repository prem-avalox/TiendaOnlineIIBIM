#!/bin/bash

# ================================================
# Script de Instalación para macOS/Linux
# Tienda Online - Clothing Store
# ================================================

echo ""
echo "================================================"
echo "  INSTALACIÓN DE DEPENDENCIAS - TIENDA ONLINE"
echo "================================================"
echo ""

# Colores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Verificar Maven
echo "[1/4] Verificando Maven..."
if ! command -v mvn &> /dev/null; then
    echo -e "${RED}[ERROR]${NC} Maven no está instalado"
    echo ""
    echo "Instalación:"
    echo "  macOS:  brew install maven"
    echo "  Linux:  sudo apt install maven"
    exit 1
else
    echo -e "${GREEN}[OK]${NC} Maven encontrado"
    mvn -version
fi

echo ""
echo "[2/4] Verificando Java..."
if ! command -v java &> /dev/null; then
    echo -e "${RED}[ERROR]${NC} Java no está instalado"
    echo ""
    echo "Instalación:"
    echo "  macOS:  brew install openjdk@17"
    echo "  Linux:  sudo apt install openjdk-17-jdk"
    exit 1
else
    echo -e "${GREEN}[OK]${NC} Java encontrado"
    java -version
fi

echo ""
echo "[3/4] Limpiando proyecto anterior..."
if [ -d "target" ]; then
    rm -rf target
fi
echo -e "${GREEN}[OK]${NC} Limpieza completada"

echo ""
echo "[4/4] Descargando dependencias con Maven..."
echo "Esto puede tardar unos minutos la primera vez..."
echo ""

mvn clean install

if [ $? -eq 0 ]; then
    echo ""
    echo "================================================"
    echo "  ✅ INSTALACIÓN COMPLETADA EXITOSAMENTE"
    echo "================================================"
    echo ""
    echo "Dependencias descargadas:"
    echo "  ✓ MySQL JDBC Driver 8.2.0"
    echo "  ✓ Jakarta Servlet API 6.0.0"
    echo "  ✓ JSTL 3.0.0"
    echo ""
    echo "================================================"
    echo "  PRÓXIMOS PASOS"
    echo "================================================"
    echo "1. Asegúrate que MySQL esté corriendo:"
    echo "   macOS: brew services start mysql"
    echo "   Linux: sudo systemctl start mysql"
    echo ""
    echo "2. Ejecuta el script de base de datos:"
    echo "   mysql -u root -p < database/INSTALL_DB.sql"
    echo ""
    echo "3. Configura ConexionBD.java con tu usuario/password"
    echo ""
    echo "4. En Eclipse:"
    echo "   File → Import → Maven → Existing Maven Projects"
    echo ""
    echo "5. Configura Tomcat 10.1+ en Eclipse"
    echo ""
    echo "6. Run → Run on Server"
    echo ""
    echo "7. Accede a: http://localhost:8080/tiendaOnline/Catalogo"
    echo "================================================"
else
    echo ""
    echo "================================================"
    echo "  ❌ ERROR EN LA INSTALACIÓN"
    echo "================================================"
    echo ""
    echo "Verifica:"
    echo "  1. Conexión a Internet activa"
    echo "  2. Maven configurado correctamente"
    echo "  3. Java 17+ instalado"
    echo ""
    echo "Intenta ejecutar manualmente: mvn clean install"
    echo "================================================"
    exit 1
fi

echo ""
