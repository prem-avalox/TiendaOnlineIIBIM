-- ================================================
-- VERIFICACIÓN FINAL ANTES DE EJECUTAR
-- Ejecuta esto en DBeaver para confirmar que todo está listo
-- ================================================

USE tienda_online;

-- 1. Contar productos por categoría
SELECT 
    '📊 PRODUCTOS POR CATEGORÍA' AS '',
    categoria AS Categoría,
    COUNT(*) AS Cantidad
FROM prendas
GROUP BY categoria
ORDER BY categoria;

-- 2. Verificar usuarios
SELECT 
    '👥 USUARIOS' AS '',
    id_usuario AS ID,
    nombre_completo AS Nombre,
    correo AS Email,
    rol AS Rol
FROM usuarios;

-- 3. Stock disponible
SELECT 
    '📦 STOCK TOTAL' AS '',
    SUM(stock) AS Total_Unidades,
    COUNT(*) AS Tallas_Configuradas,
    COUNT(DISTINCT id_prenda) AS Productos_con_Stock
FROM tallas;

-- 4. Productos con imágenes
SELECT 
    '🖼️ IMÁGENES' AS '',
    COUNT(*) AS Total_Productos,
    SUM(CASE WHEN imagen_url IS NOT NULL AND imagen_url != '' THEN 1 ELSE 0 END) AS Con_Imagen,
    SUM(CASE WHEN imagen_url IS NULL OR imagen_url = '' THEN 1 ELSE 0 END) AS Sin_Imagen
FROM prendas;

-- 5. Primeros 3 productos (para ver en el catálogo)
SELECT 
    '🎯 PRIMEROS 3 PRODUCTOS' AS '',
    id_prenda AS ID,
    nombre AS Nombre,
    CONCAT('$', precio) AS Precio,
    categoria AS Categoría,
    imagen_url AS Imagen
FROM prendas
LIMIT 3;

-- Resultado final
SELECT 
    '✅ SISTEMA LISTO PARA EJECUTAR' AS Status,
    CONCAT(
        'Productos: ', (SELECT COUNT(*) FROM prendas),
        ' | Usuarios: ', (SELECT COUNT(*) FROM usuarios),
        ' | Stock: ', (SELECT SUM(stock) FROM tallas)
    ) AS Resumen;
