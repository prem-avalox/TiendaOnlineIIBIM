-- ================================================
-- PRUEBA RÁPIDA DE CONEXIÓN MYSQL
-- Ejecuta estos comandos en MySQL Workbench
-- ================================================

-- 1. Ver todas las bases de datos
SHOW DATABASES;

-- 2. Verificar si existe tienda_online
SELECT SCHEMA_NAME 
FROM INFORMATION_SCHEMA.SCHEMATA 
WHERE SCHEMA_NAME = 'tienda_online';

-- 3. Si existe, usarla
USE tienda_online;

-- 4. Ver todas las tablas
SHOW TABLES;

-- 5. Contar registros
SELECT 'Usuarios' AS Tabla, COUNT(*) AS Total FROM usuarios
UNION ALL
SELECT 'Prendas', COUNT(*) FROM prendas
UNION ALL
SELECT 'Tallas', COUNT(*) FROM tallas;

-- Si ves resultados, ¡la BD está lista! ✅
-- Si no existe, continúa con el siguiente paso...
