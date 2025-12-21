@echo off
REM ================================================
REM SCRIPT DE INICIO RÁPIDO - TIENDA ONLINE (Windows)
REM ================================================

cls
echo ================================================
echo   CLOTHING STORE - TIENDA ONLINE
echo ================================================
echo.

REM Verificar que estamos en la carpeta correcta
if not exist "pom.xml" (
    echo ERROR: Ejecuta este script desde la carpeta tiendaOnline
    pause
    exit /b 1
)

REM Verificar Java
echo 1. Verificando Java...
where java >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Java no esta instalado
    echo   Instalar: https://adoptium.net/
    pause
    exit /b 1
)
java -version 2>&1 | findstr "version"
echo.

REM Verificar Maven
echo 2. Verificando Maven...
where mvn >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Maven no esta instalado
    echo   Descargar: https://maven.apache.org/download.cgi
    pause
    exit /b 1
)
mvn -version | findstr "Apache Maven"
echo.

REM Verificar MySQL
echo 3. Verificando MySQL...
where mysql >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo ADVERTENCIA: MySQL no detectado en PATH
    echo   Asegurate que MySQL este corriendo
) else (
    mysql --version
)
echo.

REM Compilar y ejecutar
echo 4. Compilando y ejecutando aplicacion...
echo    Esto puede tardar 1-2 minutos la primera vez...
echo.

call mvn clean package cargo:run

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo ================================================
    echo   ERROR AL INICIAR
    echo ================================================
    echo.
    echo Posibles causas:
    echo 1. MySQL no esta corriendo
    echo 2. Base de datos 'tienda_online' no existe
    echo 3. Credenciales incorrectas en ConexionBD.java
    echo 4. Puerto 8080 ya esta en uso
    echo.
    echo Soluciones:
    echo - Iniciar MySQL desde XAMPP o servicios
    echo - Ejecutar: mysql -u root -p ^< database\INSTALL_DB.sql
    echo - Verificar ConexionBD.java
    echo - Liberar puerto: netstat -ano ^| findstr :8080
    echo.
    pause
    exit /b 1
)
