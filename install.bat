@echo off
REM ================================================
REM Script de Instalación para Windows
REM Tienda Online - Clothing Store
REM ================================================

echo.
echo ================================================
echo   INSTALACION DE DEPENDENCIAS - TIENDA ONLINE
echo ================================================
echo.

REM Verificar si Maven está instalado
echo [1/4] Verificando Maven...
where mvn >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Maven no está instalado
    echo.
    echo Descarga Maven desde: https://maven.apache.org/download.cgi
    echo Y agrega al PATH de Windows
    pause
    exit /b 1
) else (
    echo [OK] Maven encontrado
    mvn -version
)

echo.
echo [2/4] Verificando Java...
where java >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Java no está instalado
    echo.
    echo Descarga JDK 17+ desde: https://adoptium.net/
    pause
    exit /b 1
) else (
    echo [OK] Java encontrado
    java -version
)

echo.
echo [3/4] Limpiando proyecto anterior...
if exist target rmdir /s /q target
echo [OK] Limpieza completada

echo.
echo [4/4] Descargando dependencias con Maven...
echo Esto puede tardar unos minutos la primera vez...
echo.

call mvn clean install

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ================================================
    echo   INSTALACION COMPLETADA EXITOSAMENTE
    echo ================================================
    echo.
    echo Dependencias descargadas:
    echo - MySQL JDBC Driver 8.2.0
    echo - Jakarta Servlet API 6.0.0
    echo - JSTL 3.0.0
    echo.
    echo ================================================
    echo   PROXIMOS PASOS
    echo ================================================
    echo 1. Asegurate que MySQL este corriendo
    echo 2. Ejecuta el script: database\INSTALL_DB.sql
    echo 3. Configura ConexionBD.java con tu usuario/password
    echo 4. En Eclipse: Import -^> Maven -^> Existing Maven Projects
    echo 5. Configura Tomcat 10.1+ en Eclipse
    echo 6. Run ^> Run on Server
    echo.
    echo Accede a: http://localhost:8080/tiendaOnline/Catalogo
    echo ================================================
) else (
    echo.
    echo ================================================
    echo   ERROR EN LA INSTALACION
    echo ================================================
    echo.
    echo Verifica:
    echo 1. Conexion a Internet activa
    echo 2. Maven configurado correctamente
    echo 3. Java 17+ instalado
    echo.
    echo Intenta ejecutar manualmente: mvn clean install
    echo ================================================
)

echo.
pause
