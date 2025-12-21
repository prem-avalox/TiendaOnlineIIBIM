-- ================================================
-- SCRIPT DE INSTALACIÓN - TIENDA ONLINE
-- Base de Datos: MySQL 8.0+
-- ================================================

-- Eliminar base de datos si existe (para reinstalación limpia)
DROP DATABASE IF EXISTS tienda_online;

-- Crear la base de datos
CREATE DATABASE tienda_online
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

-- Usar la base de datos
USE tienda_online;

-- ================================================
-- TABLA: usuarios
-- ================================================
CREATE TABLE IF NOT EXISTS usuarios (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    nombre_completo VARCHAR(100) NOT NULL,
    correo VARCHAR(100) NOT NULL UNIQUE,
    contrasena VARCHAR(255) NOT NULL,
    rol ENUM('CLIENTE', 'ADMIN') DEFAULT 'CLIENTE',
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_correo (correo),
    INDEX idx_rol (rol)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ================================================
-- TABLA: prendas
-- ================================================
CREATE TABLE IF NOT EXISTS prendas (
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

-- ================================================
-- TABLA: tallas
-- ================================================
CREATE TABLE IF NOT EXISTS tallas (
    id_talla INT AUTO_INCREMENT PRIMARY KEY,
    id_prenda INT NOT NULL,
    talla ENUM('XS', 'S', 'M', 'L', 'XL', 'XXL') NOT NULL,
    stock INT DEFAULT 0,
    FOREIGN KEY (id_prenda) REFERENCES prendas(id_prenda) ON DELETE CASCADE,
    UNIQUE KEY unique_prenda_talla (id_prenda, talla),
    INDEX idx_stock (stock)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ================================================
-- DATOS INICIALES - USUARIOS
-- ================================================

-- Insertar usuario administrador
-- Correo: admin@tienda.com
-- Contraseña: admin123
INSERT INTO usuarios (nombre_completo, correo, contrasena, rol) VALUES
('Administrador Sistema', 'admin@tienda.com', 'admin123', 'ADMIN');

-- Insertar usuario cliente de prueba
-- Correo: cliente@example.com
-- Contraseña: cliente123
INSERT INTO usuarios (nombre_completo, correo, contrasena, rol) VALUES
('Cliente Prueba', 'cliente@example.com', 'cliente123', 'CLIENTE');

-- ================================================
-- DATOS DE PRUEBA: Prendas (Migrado desde Clothing Store)
-- ================================================

-- Camisas
INSERT INTO prendas (nombre, descripcion, categoria, precio, imagen_url, color, tipo_ajuste) VALUES
('Camisa Negra Manga Larga Regular Fit Easy Iron', 'Esta es nuestra camisa en negro con corte regular fit. Elaborada en Ecuador con tejido de algodón que combina comodidad y practicidad. El acabado easy care minimiza el mantenimiento. Presenta una silueta relajada perfecta para el día a día. Construcción clásica con costuras reforzadas. Este producto es apto para lavadora.', 'CAMISAS', 79.00, 'img/camisa-negra-regular-easy.webp', 'Negro', 'Regular Fit'),
('Camisa Negra Manga Larga Slim Fit Easy Iron', 'Esta es nuestra camisa en negro con acabado easy care. Confeccionada en Ecuador utilizando algodón premium con tratamiento anti-arrugas. Diseñada con corte slim fit que define una línea limpia y moderna. Presenta detalles minimalistas y construcción duradera. Ideal para ocasiones formales o casuales. Este producto es apto para lavadora.', 'CAMISAS', 85.00, 'img/camisa-negra-slim-easy.webp', 'Negro', 'Slim Fit'),
('Camisa Celeste Manga Larga Slim Fit Easy Iron', 'Esta es nuestra camisa en tonalidad celeste con acabado easy care. Elaborada en Ecuador con algodón de alta calidad que requiere mínimo planchado. El corte slim fit ofrece una silueta moderna y favorecedora. Presenta cuello clásico y cierre frontal con botones. Material resistente a las arrugas para uso diario. Este producto es apto para lavadora.', 'CAMISAS', 85.00, 'img/camisa-celeste-slim-easy.webp', 'Celeste', 'Slim Fit'),
('Camisa Algodón Manga Larga Regular Fit', 'Esta es nuestra camisa clásica en algodón 100%. Confeccionada en Ecuador con acabados cuidadosamente elaborados. Diseñada con corte regular fit para una silueta contemporánea. Presenta costuras reforzadas y botones de resina natural. El tejido es suave al tacto y transpirable. Este producto es apto para lavadora.', 'CAMISAS', 79.00, 'img/camisa-slim-algodon.webp', 'Blanco', 'Regular Fit');

-- Pantalones
INSERT INTO prendas (nombre, descripcion, categoria, precio, imagen_url, color, tipo_ajuste) VALUES
('Pantalón Denim Crudo Loose Fit', 'Este es nuestro pantalón de denim en tono crudo. Confeccionado en Ecuador con mezclilla 100% algodón que mejora con el uso. El corte loose fit ofrece comodidad y estilo contemporáneo. Presenta bolsillos delanteros curvados para un acabado más suave, con bolsillo de parche en la parte posterior. Este producto es apto para lavadora.', 'PANTALONES', 95.00, 'img/pantalon-denim-crudo-loose-fit.webp', 'Crudo', 'Loose Fit'),
('Pantalón Denim Gris Loose Fit', 'Este es nuestro pantalón de denim en gris con corte loose fit. Confeccionado en Ecuador utilizando mezclilla premium con lavado artesanal. La silueta relajada refleja el diseño contemporáneo. Presenta construcción robusta con costuras de doble pespunte. Teñido en prenda para un acabado único. Este producto es apto para lavadora.', 'PANTALONES', 95.00, 'img/pantalon-denim-gris-loose-fit.webp', 'Gris', 'Loose Fit'),
('Pantalón Mezcla Lino Relaxed Fit', 'Este es nuestro pantalón en negro fabricado con mezcla de 80% algodón y 20% lino. Elaborado en Ecuador con un tejido compacto excelente para todo el año. El corte relaxed fit proporciona libertad de movimiento. Presenta bolsillos laterales inclinados y cintura ajustable. Material transpirable ideal para climas cálidos. Este producto es apto para lavadora.', 'PANTALONES', 99.00, 'img/pantalon-negro-mezcla-lino-relaxed-fit.webp', 'Negro', 'Relaxed Fit'),
('Pantalón de Traje Regular Fit', 'Este es nuestro pantalón de vestir con corte regular fit. Elaborado en Ecuador con tejido de lana premium que ofrece caída impecable. Presenta pliegues frontales y líneas limpias para un aspecto refinado. Diseño atemporal con acabados cuidadosos. Construcción que combina tradición artesanal con estética moderna. Este producto requiere limpieza en seco.', 'PANTALONES', 125.00, 'img/pantalon-traje-regular-fit.webp', 'Negro', 'Regular Fit');

-- Calzado
INSERT INTO prendas (nombre, descripcion, categoria, precio, imagen_url, color, tipo_ajuste) VALUES
('Loafers Negro Clásico', 'Estos son nuestros loafers en negro con diseño minimalista. Confeccionados en Ecuador con cuero suave y suela flexible. Presentan construcción sin cordones para facilitar el uso. Plantilla ergonómica que brinda comodidad durante todo el día. Acabado pulido con detalles discretos. Perfectos para un estilo smart casual contemporáneo.', 'CALZADO', 139.00, 'img/loafers-negro.webp', 'Negro', 'Clásico'),
('Zapatos de Vestir', 'Estos son nuestros zapatos de vestir elaborados artesanalmente en Ecuador. Fabricados en cuero genuino con suela de cuero duradero. Presentan construcción tradicional con costuras visibles. Diseño minimalista que se adapta a ocasiones formales. Plantilla acolchada para mayor comodidad. Requieren cuidado regular con productos específicos para cuero.', 'CALZADO', 159.00, 'img/zapatos-vestir.webp', 'Negro', 'Formal');

-- Accesorios
INSERT INTO prendas (nombre, descripcion, categoria, precio, imagen_url, color, tipo_ajuste) VALUES
('Bolso Cruzado Denim', 'Este es nuestro bolso cruzado en denim gris. Elaborado en Ecuador con mezclilla resistente y herrajes metálicos de acabado plateado. Presenta compartimento principal con cierre y bolsillos interiores organizadores. Correa ajustable para llevar cruzado. Diseño funcional que combina practicidad con estética urbana. El material es duradero y envejece naturalmente.', 'ACCESORIOS', 69.00, 'img/bolso-cruzado-denim-gris.webp', 'Gris', 'Único'),
('Cinturón de Ante', 'Este es nuestro cinturón de ante con hebilla minimalista. Elaborado en Ecuador con cuero suave de textura afelpada. Presenta hebilla en acabado mate y construcción de una sola pieza. Ancho clásico de 3.5 cm apto para uso diario. El material desarrolla una pátina natural con el tiempo. Disponible en talla única ajustable.', 'ACCESORIOS', 49.00, 'img/cinturon-ante.webp', 'Marrón', 'Único'),
('Gorra Blanca Sarga', 'Esta es nuestra gorra en sarga de algodón color blanco. Confeccionada en Ecuador con tejido resistente de alta densidad. Presenta panel frontal estructurado y visera curva. Cierre ajustable en la parte posterior para adaptarse a diferentes tallas. Diseño limpio y versátil. Este producto es apto para lavadora.', 'ACCESORIOS', 35.00, 'img/gorra-blanca-sarga.webp', 'Blanco', 'Único');

-- ================================================
-- DATOS DE PRUEBA: Tallas para Camisas (IDs 1-4)
-- ================================================
INSERT INTO tallas (id_prenda, talla, stock) VALUES
-- Camisa Negra Regular Fit
(1, 'XS', 10), (1, 'S', 15), (1, 'M', 20), (1, 'L', 18), (1, 'XL', 12),
-- Camisa Negra Slim Fit
(2, 'XS', 8), (2, 'S', 15), (2, 'M', 22), (2, 'L', 20), (2, 'XL', 10),
-- Camisa Celeste Slim Fit
(3, 'XS', 5), (3, 'S', 12), (3, 'M', 18), (3, 'L', 15), (3, 'XL', 8),
-- Camisa Algodón Regular Fit
(4, 'XS', 10), (4, 'S', 18), (4, 'M', 25), (4, 'L', 22), (4, 'XL', 14);

-- ================================================
-- DATOS DE PRUEBA: Tallas para Pantalones (IDs 5-8)
-- ================================================
INSERT INTO tallas (id_prenda, talla, stock) VALUES
-- Pantalón Denim Crudo
(5, 'S', 12), (5, 'M', 20), (5, 'L', 18), (5, 'XL', 14),
-- Pantalón Denim Gris
(6, 'S', 10), (6, 'M', 22), (6, 'L', 20), (6, 'XL', 15),
-- Pantalón Mezcla Lino
(7, 'S', 8), (7, 'M', 18), (7, 'L', 22), (7, 'XL', 16),
-- Pantalón de Traje
(8, 'S', 10), (8, 'M', 20), (8, 'L', 25), (8, 'XL', 18);

-- ================================================
-- DATOS DE PRUEBA: Tallas para Calzado (IDs 9-10)
-- ================================================
INSERT INTO tallas (id_prenda, talla, stock) VALUES
-- Loafers Negro Clásico
(9, 'M', 15), (9, 'L', 20), (9, 'XL', 12),
-- Zapatos de Vestir
(10, 'M', 12), (10, 'L', 18), (10, 'XL', 10);

-- ================================================
-- DATOS DE PRUEBA: Tallas para Accesorios (IDs 11-13)
-- ================================================
INSERT INTO tallas (id_prenda, talla, stock) VALUES
-- Bolso Cruzado Denim (talla única)
(11, 'M', 50),
-- Cinturón de Ante (S, M, L, XL)
(12, 'S', 15), (12, 'M', 25), (12, 'L', 30), (12, 'XL', 20),
-- Gorra Blanca Sarga (talla única)
(13, 'M', 40);

-- ================================================
-- VERIFICACIÓN FINAL
-- ================================================

-- Mostrar resumen de la instalación
SELECT 
    'INSTALACIÓN COMPLETADA' AS Estado,
    (SELECT COUNT(*) FROM usuarios) AS Usuarios,
    (SELECT COUNT(*) FROM prendas) AS Prendas,
    (SELECT COUNT(*) FROM tallas) AS Tallas;

-- Mostrar usuarios creados
SELECT 'USUARIOS CREADOS:' AS Info;
SELECT id_usuario, nombre_completo, correo, rol FROM usuarios;

-- Mostrar prendas por categoría
SELECT 'PRENDAS POR CATEGORÍA:' AS Info;
SELECT categoria, COUNT(*) AS cantidad FROM prendas GROUP BY categoria;

-- Mostrar total de stock
SELECT 'INVENTARIO:' AS Info;
SELECT SUM(stock) AS total_items_stock FROM tallas;

-- ================================================
-- ¡BASE DE DATOS LISTA PARA USAR!
-- ================================================
