-- ================================================
-- INSTALACIÓN COMPLETA - TIENDA ONLINE
-- Sistema de tallas correcto por tipo de prenda
-- Ejecutar TODO el script en DBeaver o MySQL Workbench
-- ================================================

-- PASO 1: Eliminar base de datos existente
DROP DATABASE IF EXISTS tienda_online;

-- PASO 2: Crear base de datos nueva
CREATE DATABASE tienda_online CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE tienda_online;

-- PASO 3: Crear tabla usuarios
CREATE TABLE usuarios (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    nombre_completo VARCHAR(100) NOT NULL,
    correo VARCHAR(100) NOT NULL UNIQUE,
    contrasena VARCHAR(255) NOT NULL,
    rol ENUM('CLIENTE', 'ADMIN') DEFAULT 'CLIENTE',
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_correo (correo),
    INDEX idx_rol (rol)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- PASO 4: Crear tabla prendas
CREATE TABLE prendas (
    id_prenda INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    descripcion TEXT,
    categoria ENUM('CAMISAS', 'PANTALONES', 'CALZADO', 'ACCESORIOS') NOT NULL,
    precio DECIMAL(10, 2) NOT NULL,
    imagen_url VARCHAR(255),
    color VARCHAR(50),
    tipo_ajuste VARCHAR(50),
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_categoria (categoria),
    INDEX idx_precio (precio)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- PASO 5: Crear tabla tallas (VARCHAR para soportar todos los tipos)
CREATE TABLE tallas (
    id_talla INT AUTO_INCREMENT PRIMARY KEY,
    id_prenda INT NOT NULL,
    talla VARCHAR(20) NOT NULL,  -- Cambiado a VARCHAR para soportar: XS, M, L, 40, 42, 28/32, 30/32, Única
    stock INT DEFAULT 0,
    FOREIGN KEY (id_prenda) REFERENCES prendas(id_prenda) ON DELETE CASCADE,
    UNIQUE KEY unique_prenda_talla (id_prenda, talla),
    INDEX idx_stock (stock)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- PASO 6: Insertar usuarios
INSERT INTO usuarios (nombre_completo, correo, contrasena, rol) VALUES
('Administrador Sistema', 'admin@tienda.com', 'admin123', 'ADMIN'),
('Cliente Prueba', 'cliente@test.com', 'cliente123', 'CLIENTE');

-- PASO 7: Insertar prendas
INSERT INTO prendas (nombre, descripcion, categoria, precio, color, tipo_ajuste, imagen_url) VALUES
-- === CAMISAS (Tallas: XS, S, M, L, XL, XXL) ===
('Camisa Slim Fit Celeste', 'Camisa de algodón slim fit color celeste', 'CAMISAS', 45.99, 'Celeste', 'Slim', 'img/camisa-celeste-slim-easy.webp'),
('Camisa Negra Regular', 'Camisa negra de corte regular', 'CAMISAS', 42.99, 'Negro', 'Regular', 'img/camisa-negra-regular-easy.webp'),
('Camisa Slim Algodón', 'Camisa slim de algodón premium', 'CAMISAS', 49.99, 'Blanco', 'Slim', 'img/camisa-slim-algodon.webp'),
('Camisa Negra Slim', 'Camisa negra slim fit elegante', 'CAMISAS', 47.99, 'Negro', 'Slim', 'img/camisa-negra-slim-easy.webp'),

-- === PANTALONES (Tallas: 28/32, 30/32, 32/32, 34/32, 36/32) ===
('Pantalón Denim Loose Fit Gris', 'Pantalón denim gris loose fit', 'PANTALONES', 65.99, 'Gris', 'Loose', 'img/pantalon-denim-gris-loose-fit.webp'),
('Pantalón Denim Loose Fit Crudo', 'Pantalón denim crudo loose fit', 'PANTALONES', 64.99, 'Beige', 'Loose', 'img/pantalon-denim-crudo-loose-fit.webp'),

-- === CALZADO (Tallas europeas: 38, 39, 40, 41, 42, 43, 44, 45) ===
('Loafers Negro', 'Zapatos loafers elegantes en negro', 'CALZADO', 89.99, 'Negro', NULL, 'img/loafers-negro.webp'),

-- === ACCESORIOS (Talla Única) ===
('Gorra Blanca Sarga', 'Gorra deportiva blanca de sarga', 'ACCESORIOS', 25.99, 'Blanco', NULL, 'img/gorra-blanca-sarga.webp'),
('Bolso Cruzado Denim Gris', 'Bolso cruzado de denim gris', 'ACCESORIOS', 35.99, 'Gris', NULL, 'img/bolso-cruzado-denim-gris.webp'),
('Cinturón de Ante', 'Cinturón elegante de ante', 'ACCESORIOS', 29.99, 'Marrón', NULL, 'img/cinturon-ante.webp');

-- PASO 8: Insertar tallas según el tipo de prenda

-- === TALLAS PARA CAMISAS (XS, S, M, L, XL, XXL) ===
INSERT INTO tallas (id_prenda, talla, stock) VALUES
-- Camisa 1: Slim Fit Celeste
(1, 'XS', 8),
(1, 'S', 12),
(1, 'M', 15),
(1, 'L', 12),
(1, 'XL', 10),
(1, 'XXL', 5),

-- Camisa 2: Negra Regular
(2, 'XS', 6),
(2, 'S', 10),
(2, 'M', 14),
(2, 'L', 11),
(2, 'XL', 8),
(2, 'XXL', 4),

-- Camisa 3: Slim Algodón
(3, 'XS', 10),
(3, 'S', 15),
(3, 'M', 18),
(3, 'L', 14),
(3, 'XL', 12),
(3, 'XXL', 6),

-- Camisa 4: Negra Slim
(4, 'XS', 7),
(4, 'S', 11),
(4, 'M', 13),
(4, 'L', 10),
(4, 'XL', 9),
(4, 'XXL', 5);

-- === TALLAS PARA PANTALONES (Tallas americanas: cintura/largo) ===
INSERT INTO tallas (id_prenda, talla, stock) VALUES
-- Pantalón 1: Denim Gris
(5, '28/32', 8),
(5, '30/32', 12),
(5, '32/32', 15),
(5, '34/32', 12),
(5, '36/32', 8),
(5, '38/32', 5),

-- Pantalón 2: Denim Crudo
(6, '28/32', 7),
(6, '30/32', 11),
(6, '32/32', 14),
(6, '34/32', 11),
(6, '36/32', 7),
(6, '38/32', 4);

-- === TALLAS PARA CALZADO (Tallas europeas) ===
INSERT INTO tallas (id_prenda, talla, stock) VALUES
-- Zapato: Loafers Negro
(7, '38', 5),
(7, '39', 8),
(7, '40', 12),
(7, '41', 15),
(7, '42', 12),
(7, '43', 10),
(7, '44', 8),
(7, '45', 5);

-- === TALLAS PARA ACCESORIOS (Talla Única) ===
INSERT INTO tallas (id_prenda, talla, stock) VALUES
-- Gorra Blanca
(8, 'Única', 25),

-- Bolso Cruzado
(9, 'Única', 20),

-- Cinturón
(10, 'Única', 30);

-- PASO 9: Verificación
SELECT '=== VERIFICACIÓN FINAL ===' AS '';

SELECT 'Usuarios creados:' AS info, COUNT(*) AS cantidad FROM usuarios;
SELECT 'Prendas insertadas:' AS info, COUNT(*) AS cantidad FROM prendas;
SELECT 'Tallas configuradas:' AS info, COUNT(*) AS cantidad FROM tallas;

SELECT '' AS '';
SELECT '=== DISTRIBUCIÓN POR CATEGORÍA ===' AS '';
SELECT 
    categoria,
    COUNT(*) as cantidad_productos,
    CONCAT('$', ROUND(AVG(precio), 2)) as precio_promedio
FROM prendas 
GROUP BY categoria;

SELECT '' AS '';
SELECT '=== EJEMPLOS DE TALLAS POR CATEGORÍA ===' AS '';

-- Camisas
SELECT 'CAMISAS - Tallas disponibles:' AS info;
SELECT DISTINCT p.nombre, t.talla, t.stock
FROM prendas p
JOIN tallas t ON p.id_prenda = t.id_prenda
WHERE p.categoria = 'CAMISAS'
AND p.id_prenda = 1
ORDER BY t.talla;

-- Pantalones
SELECT 'PANTALONES - Tallas disponibles:' AS info;
SELECT DISTINCT p.nombre, t.talla, t.stock
FROM prendas p
JOIN tallas t ON p.id_prenda = t.id_prenda
WHERE p.categoria = 'PANTALONES'
AND p.id_prenda = 5
ORDER BY t.talla;

-- Calzado
SELECT 'CALZADO - Tallas disponibles:' AS info;
SELECT DISTINCT p.nombre, t.talla, t.stock
FROM prendas p
JOIN tallas t ON p.id_prenda = t.id_prenda
WHERE p.categoria = 'CALZADO'
ORDER BY CAST(t.talla AS UNSIGNED);

-- Accesorios
SELECT 'ACCESORIOS - Tallas disponibles:' AS info;
SELECT DISTINCT p.nombre, t.talla, t.stock
FROM prendas p
JOIN tallas t ON p.id_prenda = t.id_prenda
WHERE p.categoria = 'ACCESORIOS'
ORDER BY p.nombre;

SELECT '' AS '';
SELECT '✅ BASE DE DATOS CREADA EXITOSAMENTE' AS RESULTADO;
SELECT '✅ Sistema de tallas correcto implementado' AS RESULTADO;
SELECT '   - Camisas: XS, S, M, L, XL, XXL' AS DETALLE;
SELECT '   - Pantalones: 28/32, 30/32, 32/32, 34/32, 36/32, 38/32' AS DETALLE;
SELECT '   - Calzado: 38, 39, 40, 41, 42, 43, 44, 45 (EU)' AS DETALLE;
SELECT '   - Accesorios: Talla Única' AS DETALLE;
