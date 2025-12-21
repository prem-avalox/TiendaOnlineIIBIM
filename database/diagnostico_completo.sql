-- ================================================
-- SCRIPT DE DIAGNÓSTICO COMPLETO
-- Ejecuta este script en MySQL Workbench para verificar todo
-- ================================================

SELECT '======================================' AS '';
SELECT '  DIAGNÓSTICO DE TIENDA ONLINE' AS '';
SELECT '======================================' AS '';
SELECT '' AS '';

-- ================================================
-- 1. VERIFICAR SI EXISTE LA BASE DE DATOS
-- ================================================
SELECT '1. ¿EXISTE LA BASE DE DATOS?' AS '';
SELECT 
    CASE 
        WHEN COUNT(*) > 0 THEN '✅ SÍ - Base de datos encontrada'
        ELSE '❌ NO - Ejecuta INSTALL_DB.sql primero'
    END AS Estado
FROM INFORMATION_SCHEMA.SCHEMATA 
WHERE SCHEMA_NAME = 'tienda_online';

SELECT '' AS '';

-- ================================================
-- 2. USAR LA BASE DE DATOS
-- ================================================
USE tienda_online;

SELECT '2. TABLAS EN LA BASE DE DATOS' AS '';
SHOW TABLES;

SELECT '' AS '';

-- ================================================
-- 3. CONTAR REGISTROS EN CADA TABLA
-- ================================================
SELECT '3. CANTIDAD DE REGISTROS' AS '';

SELECT 'Usuarios' AS Tabla, COUNT(*) AS Cantidad FROM usuarios
UNION ALL
SELECT 'Prendas', COUNT(*) FROM prendas
UNION ALL
SELECT 'Tallas', COUNT(*) FROM tallas;

SELECT '' AS '';

-- ================================================
-- 4. VERIFICAR USUARIOS
-- ================================================
SELECT '4. USUARIOS EN EL SISTEMA' AS '';
SELECT 
    id_usuario AS ID,
    nombre_completo AS Nombre,
    correo AS Email,
    rol AS Rol,
    DATE_FORMAT(fecha_registro, '%d/%m/%Y %H:%i') AS Registrado
FROM usuarios
ORDER BY id_usuario;

SELECT '' AS '';

-- ================================================
-- 5. PRODUCTOS POR CATEGORÍA
-- ================================================
SELECT '5. PRODUCTOS POR CATEGORÍA' AS '';
SELECT 
    categoria AS Categoría,
    COUNT(*) AS Cantidad,
    CONCAT('$', FORMAT(MIN(precio), 2)) AS Precio_Mínimo,
    CONCAT('$', FORMAT(MAX(precio), 2)) AS Precio_Máximo,
    CONCAT('$', FORMAT(AVG(precio), 2)) AS Precio_Promedio
FROM prendas
GROUP BY categoria
ORDER BY categoria;

SELECT '' AS '';

-- ================================================
-- 6. INVENTARIO TOTAL
-- ================================================
SELECT '6. INVENTARIO Y STOCK' AS '';
SELECT 
    COUNT(DISTINCT id_prenda) AS Productos_con_Stock,
    COUNT(*) AS Tallas_Configuradas,
    SUM(stock) AS Total_Items_en_Stock,
    ROUND(AVG(stock), 2) AS Stock_Promedio_por_Talla
FROM tallas;

SELECT '' AS '';

-- ================================================
-- 7. LISTADO COMPLETO DE PRODUCTOS
-- ================================================
SELECT '7. CATÁLOGO COMPLETO DE PRODUCTOS' AS '';
SELECT 
    id_prenda AS ID,
    nombre AS Nombre,
    categoria AS Categoría,
    CONCAT('$', FORMAT(precio, 2)) AS Precio,
    color AS Color,
    tipo_ajuste AS 'Tipo de Ajuste',
    CASE 
        WHEN imagen_url IS NOT NULL AND imagen_url != '' THEN '✅ Sí'
        ELSE '❌ No'
    END AS '¿Tiene Imagen?'
FROM prendas
ORDER BY 
    CASE categoria
        WHEN 'CAMISAS' THEN 1
        WHEN 'PANTALONES' THEN 2
        WHEN 'CALZADO' THEN 3
        WHEN 'ACCESORIOS' THEN 4
    END,
    id_prenda;

SELECT '' AS '';

-- ================================================
-- 8. PRODUCTOS CON SUS TALLAS DISPONIBLES
-- ================================================
SELECT '8. PRODUCTOS CON TALLAS Y STOCK' AS '';
SELECT 
    p.nombre AS Producto,
    p.categoria AS Categoría,
    GROUP_CONCAT(
        CONCAT(t.talla, ' (', t.stock, ')') 
        ORDER BY 
            CASE t.talla
                WHEN 'XS' THEN 1
                WHEN 'S' THEN 2
                WHEN 'M' THEN 3
                WHEN 'L' THEN 4
                WHEN 'XL' THEN 5
                WHEN 'XXL' THEN 6
            END
        SEPARATOR ', '
    ) AS 'Tallas (Stock)'
FROM prendas p
LEFT JOIN tallas t ON p.id_prenda = t.id_prenda
GROUP BY p.id_prenda, p.nombre, p.categoria
ORDER BY p.categoria, p.nombre;

SELECT '' AS '';

-- ================================================
-- 9. ALERTAS DE STOCK
-- ================================================
SELECT '9. ALERTAS DE INVENTARIO' AS '';
SELECT 
    p.nombre AS Producto,
    t.talla AS Talla,
    t.stock AS Stock,
    CASE 
        WHEN t.stock = 0 THEN '🔴 AGOTADO'
        WHEN t.stock < 5 THEN '🟡 STOCK CRÍTICO'
        WHEN t.stock < 10 THEN '🟠 STOCK BAJO'
        ELSE '🟢 OK'
    END AS Estado
FROM prendas p
JOIN tallas t ON p.id_prenda = t.id_prenda
WHERE t.stock < 10
ORDER BY t.stock, p.nombre;

SELECT '' AS '';

-- ================================================
-- 10. VERIFICAR IMÁGENES
-- ================================================
SELECT '10. VERIFICACIÓN DE IMÁGENES' AS '';
SELECT 
    COUNT(*) AS Total_Productos,
    SUM(CASE WHEN imagen_url IS NOT NULL AND imagen_url != '' THEN 1 ELSE 0 END) AS Con_Imagen,
    SUM(CASE WHEN imagen_url IS NULL OR imagen_url = '' THEN 1 ELSE 0 END) AS Sin_Imagen
FROM prendas;

SELECT '' AS '';

-- ================================================
-- 11. PRODUCTOS SIN IMAGEN (si los hay)
-- ================================================
SELECT '11. PRODUCTOS SIN IMAGEN' AS '';
SELECT 
    id_prenda AS ID,
    nombre AS Producto,
    categoria AS Categoría
FROM prendas
WHERE imagen_url IS NULL OR imagen_url = ''
LIMIT 5;

SELECT '' AS '';

-- ================================================
-- 12. TOP 5 PRODUCTOS MÁS CAROS
-- ================================================
SELECT '12. TOP 5 PRODUCTOS MÁS CAROS' AS '';
SELECT 
    nombre AS Producto,
    categoria AS Categoría,
    CONCAT('$', FORMAT(precio, 2)) AS Precio
FROM prendas
ORDER BY precio DESC
LIMIT 5;

SELECT '' AS '';

-- ================================================
-- 13. ESTRUCTURA DE TABLA USUARIOS
-- ================================================
SELECT '13. ESTRUCTURA DE TABLA: usuarios' AS '';
DESCRIBE usuarios;

SELECT '' AS '';

-- ================================================
-- 14. ESTRUCTURA DE TABLA PRENDAS
-- ================================================
SELECT '14. ESTRUCTURA DE TABLA: prendas' AS '';
DESCRIBE prendas;

SELECT '' AS '';

-- ================================================
-- 15. ESTRUCTURA DE TABLA TALLAS
-- ================================================
SELECT '15. ESTRUCTURA DE TABLA: tallas' AS '';
DESCRIBE tallas;

SELECT '' AS '';

-- ================================================
-- 16. RESUMEN FINAL
-- ================================================
SELECT '======================================' AS '';
SELECT '  RESUMEN FINAL DEL SISTEMA' AS '';
SELECT '======================================' AS '';

SELECT 
    'Base de Datos' AS Componente,
    'tienda_online' AS Valor,
    '✅ Operativa' AS Estado
UNION ALL
SELECT 
    'Usuarios',
    CAST((SELECT COUNT(*) FROM usuarios) AS CHAR),
    CASE WHEN (SELECT COUNT(*) FROM usuarios) >= 2 THEN '✅ OK' ELSE '⚠️  Verificar' END
UNION ALL
SELECT 
    'Productos',
    CAST((SELECT COUNT(*) FROM prendas) AS CHAR),
    CASE WHEN (SELECT COUNT(*) FROM prendas) >= 10 THEN '✅ OK' ELSE '⚠️  Verificar' END
UNION ALL
SELECT 
    'Tallas Configuradas',
    CAST((SELECT COUNT(*) FROM tallas) AS CHAR),
    CASE WHEN (SELECT COUNT(*) FROM tallas) >= 30 THEN '✅ OK' ELSE '⚠️  Verificar' END
UNION ALL
SELECT 
    'Stock Total',
    CAST((SELECT SUM(stock) FROM tallas) AS CHAR),
    CASE WHEN (SELECT SUM(stock) FROM tallas) > 0 THEN '✅ OK' ELSE '❌ Sin Stock' END;

SELECT '' AS '';

-- ================================================
-- 17. INSTRUCCIONES FINALES
-- ================================================
SELECT '======================================' AS '';
SELECT '  PRÓXIMOS PASOS' AS '';
SELECT '======================================' AS '';

SELECT '
✅ Si todo muestra datos correctos:

1. Configurar ConexionBD.java con tu contraseña MySQL
2. En Eclipse: Run → ConexionBD.main()
3. Debe decir "Conexión exitosa"
4. Ejecutar: mvn clean install
5. Run on Server en Eclipse
6. Acceder: http://localhost:8080/tiendaOnline/Catalogo

❌ Si hay problemas:

- Si no hay datos: Ejecutar INSTALL_DB.sql
- Si faltan tablas: Recrear la BD
- Ver errores en: Eclipse Console
' AS Instrucciones;

SELECT '' AS '';
SELECT '======================================' AS '';
SELECT '  ¡DIAGNÓSTICO COMPLETADO!' AS '';
SELECT '======================================' AS '';
