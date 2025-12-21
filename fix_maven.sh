#!/bin/bash

# ================================================
# SCRIPT: Forzar actualización de Maven en Eclipse
# ================================================

echo "🔧 Forzando actualización del proyecto Maven..."
echo ""

cd /Users/martin/eclipse-workspace/tiendaOnline

# 1. Limpiar target
echo "1️⃣ Limpiando carpeta target..."
rm -rf target/
echo "✅ Target eliminado"
echo ""

# 2. Limpiar configuración de Eclipse
echo "2️⃣ Limpiando configuración de Eclipse..."
rm -rf .settings/org.eclipse.m2e.core.prefs 2>/dev/null
echo "✅ Configuración limpiada"
echo ""

# 3. Ejecutar Maven
echo "3️⃣ Ejecutando Maven clean install..."
mvn clean install -U
echo ""

if [ $? -eq 0 ]; then
    echo "✅ Maven ejecutado exitosamente"
    echo ""
    echo "================================================"
    echo "  AHORA HAZ ESTO EN ECLIPSE:"
    echo "================================================"
    echo ""
    echo "1. Clic derecho en proyecto 'tiendaOnline'"
    echo "2. Refresh (F5)"
    echo "3. Si no aparece 'Maven' en el menú:"
    echo "   - File → Import → Maven → Existing Maven Projects"
    echo "   - Browse → Seleccionar tiendaOnline"
    echo "   - Finish"
    echo ""
    echo "4. Expandir proyecto → Buscar 'Maven Dependencies'"
    echo "5. Run ConexionBD.java"
    echo ""
    echo "================================================"
else
    echo "❌ Error en Maven"
    echo "Verifica que estés en la carpeta correcta"
fi
