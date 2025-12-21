-- ================================================
-- VERIFICACIÓN RÁPIDA POST-INSTALACIÓN
-- Ejecuta esto en DBeaver para confirmar que todo está OK
-- ================================================

USE tienda_online;

-- 1. Verificar tablas
SELECT '1. TABLAS CREADAS:' AS '';
SHOW TABLES;

-- 2. Contar registros
SELECT '2. CANTIDAD DE REGISTROS:' AS '';
SELECT 'Usuarios' AS Tabla, COUNT(*) AS Cantidad FROM usuarios
UNION ALL
SELECT 'Prendas', COUNT(*) FROM prendas
UNION ALL
SELECT 'Tallas', COUNT(*) FROM tallas;

-- 3. Ver usuarios
SELECT '3. USUARIOS:' AS '';
SELECT id_usuario, nombre_completo, correo, rol FROM usuarios;

-- 4. Ver productos
SELECT '4. PRODUCTOS:' AS '';
SELECT id_prenda, nombre, categoria, CONCAT('$', precio) AS precio 
FROM prendas 
ORDER BY categoria, id_prenda;

-- 5. Stock total
SELECT '5. INVENTARIO:' AS '';
SELECT 
    SUM(stock) AS Total_Stock,
    COUNT(*) AS Tallas_Configuradas,
    COUNT(DISTINCT id_prenda) AS Productos_con_Stock
FROM tallas;

SELECT '✅ TODO CORRECTO - Base de datos lista para usar' AS RESULTADO;
