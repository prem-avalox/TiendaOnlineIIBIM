-- ================================================
-- PASO 1: EJECUTAR ESTO PRIMERO EN DBEAVER
-- Copia y pega en DBeaver, luego Ctrl+Alt+X (Mac: Cmd+Alt+X)
-- ================================================

-- Eliminar base de datos si existe (para empezar limpio)
DROP DATABASE IF EXISTS tienda_online;

-- Crear la base de datos
CREATE DATABASE tienda_online
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

-- Usar la base de datos
USE tienda_online;

-- Verificar que estamos en la BD correcta
SELECT DATABASE() AS 'Base de Datos Actual';

-- Mostrar mensaje de éxito
SELECT '✅ Base de datos creada correctamente' AS Status;
