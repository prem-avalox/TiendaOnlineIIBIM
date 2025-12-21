-- ================================================
-- SCRIPT DE VERIFICACIÓN DE BASE DE DATOS
-- Ejecuta este script para verificar que todo está correcto
-- ================================================

USE tienda_online;

-- ================================================
-- 1. VERIFICAR TABLAS
-- ================================================
SELECT '1. VERIFICANDO TABLAS...' AS '';
SHOW TABLES;

-- ================================================
-- 2. CONTAR REGISTROS
-- ================================================
SELECT '2. CONTANDO REGISTROS...' AS '';

SELECT 'Usuarios' AS Tabla, COUNT(*) AS Total FROM usuarios
UNION ALL
SELECT 'Prendas' AS Tabla, COUNT(*) AS Total FROM prendas
UNION ALL
SELECT 'Tallas' AS Tabla, COUNT(*) AS Total FROM tallas;

-- ================================================
-- 3. VERIFICAR USUARIOS
-- ================================================
SELECT '3. USUARIOS EN EL SISTEMA...' AS '';
SELECT 
    id_usuario,
    nombre_completo,
    correo,
    rol,
    fecha_registro
FROM usuarios;

-- ================================================
-- 4. VERIFICAR PRENDAS POR CATEGORÍA
-- ================================================
SELECT '4. PRENDAS POR CATEGORÍA...' AS '';
SELECT 
    categoria,
    COUNT(*) AS cantidad,
    MIN(precio) AS precio_min,
    MAX(precio) AS precio_max,
    AVG(precio) AS precio_promedio
FROM prendas
GROUP BY categoria;

-- ================================================
-- 5. VERIFICAR STOCK TOTAL
-- ================================================
SELECT '5. INVENTARIO TOTAL...' AS '';
SELECT 
    SUM(stock) AS total_items,
    COUNT(DISTINCT id_prenda) AS prendas_con_stock
FROM tallas;

-- ================================================
-- 6. LISTAR TODAS LAS PRENDAS
-- ================================================
SELECT '6. LISTADO DE PRENDAS...' AS '';
SELECT 
    id_prenda,
    nombre,
    categoria,
    precio,
    color,
    tipo_ajuste
FROM prendas
ORDER BY categoria, id_prenda;

-- ================================================
-- 7. PRENDAS CON SUS TALLAS
-- ================================================
SELECT '7. PRENDAS CON TALLAS DISPONIBLES...' AS '';
SELECT 
    p.nombre,
    p.categoria,
    GROUP_CONCAT(CONCAT(t.talla, ' (', t.stock, ')') ORDER BY t.talla SEPARATOR ', ') AS tallas_disponibles
FROM prendas p
LEFT JOIN tallas t ON p.id_prenda = t.id_prenda
GROUP BY p.id_prenda, p.nombre, p.categoria
ORDER BY p.categoria, p.nombre;

-- ================================================
-- 8. VERIFICAR IMÁGENES
-- ================================================
SELECT '8. VERIFICANDO RUTAS DE IMÁGENES...' AS '';
SELECT 
    id_prenda,
    nombre,
    imagen_url,
    CASE 
        WHEN imagen_url IS NULL THEN '❌ SIN IMAGEN'
        WHEN imagen_url = '' THEN '❌ RUTA VACÍA'
        ELSE '✅ OK'
    END AS estado
FROM prendas;

-- ================================================
-- 9. PRODUCTOS CON POCO STOCK (< 5 unidades)
-- ================================================
SELECT '9. ALERTAS DE STOCK BAJO...' AS '';
SELECT 
    p.nombre,
    t.talla,
    t.stock,
    CASE 
        WHEN t.stock = 0 THEN '🔴 AGOTADO'
        WHEN t.stock < 5 THEN '🟡 STOCK BAJO'
        ELSE '🟢 OK'
    END AS alerta
FROM prendas p
JOIN tallas t ON p.id_prenda = t.id_prenda
WHERE t.stock < 5
ORDER BY t.stock, p.nombre;

-- ================================================
-- 10. RESUMEN FINAL
-- ================================================
SELECT '10. RESUMEN FINAL...' AS '';
SELECT 
    '✅ BASE DE DATOS OPERATIVA' AS Estado,
    (SELECT COUNT(*) FROM usuarios) AS Usuarios,
    (SELECT COUNT(*) FROM prendas) AS Prendas,
    (SELECT COUNT(*) FROM tallas) AS Tallas_Configuradas,
    (SELECT SUM(stock) FROM tallas) AS Items_en_Stock;

-- ================================================
-- COMANDOS ÚTILES PARA COPIAR/PEGAR
-- ================================================
SELECT '
COMANDOS ÚTILES:
----------------
-- Ver todas las prendas
SELECT * FROM prendas;

-- Ver una prenda específica con sus tallas
SELECT p.*, t.talla, t.stock 
FROM prendas p 
LEFT JOIN tallas t ON p.id_prenda = t.id_prenda 
WHERE p.id_prenda = 1;

-- Agregar stock a una talla
UPDATE tallas SET stock = stock + 10 WHERE id_prenda = 1 AND talla = "M";

-- Ver usuarios
SELECT * FROM usuarios;

-- Cambiar contraseña de un usuario
UPDATE usuarios SET contrasena = "nueva_contraseña" WHERE correo = "admin@tienda.com";
' AS '';
