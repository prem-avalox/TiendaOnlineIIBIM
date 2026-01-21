-- ================================================================
-- SCRIPT SIMPLIFICADO: SOLO INSERTAR PRENDAS
-- ================================================================

USE tienda_online;

-- Limpiar datos existentes de prendas
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE StockTalla;
TRUNCATE TABLE Prenda;
SET FOREIGN_KEY_CHECKS = 1;

-- ================================================================
-- INSERTAR PRENDAS
-- ================================================================
INSERT INTO Prenda (imagenPrenda, nombrePrenda, precio, descripcion, color, corte, categoria) VALUES
('camisa-slim-algodon.webp', 'Camisa Slim Algodón', 29.99, 'Camisa elegante de algodón con corte slim', 'CELESTE', 'SLIM', 'CAMISAS'),
('camisa-negra-slim-easy.webp', 'Camisa Negra Slim Easy', 34.99, 'Camisa negra de corte slim fácil cuidado', 'NEGRO', 'SLIM', 'CAMISAS'),
('camisa-negra-regular-easy.webp', 'Camisa Negra Regular Easy', 32.99, 'Camisa negra regular fácil cuidado', 'NEGRO', 'REGULAR', 'CAMISAS'),
('camisa-celeste-slim-easy.webp', 'Camisa Celeste Slim Easy', 34.99, 'Camisa celeste slim fácil cuidado', 'CELESTE', 'SLIM', 'CAMISAS'),
('pantalon-denim-gris.webp', 'Pantalón Denim Gris Loose Fit', 49.99, 'Pantalón de mezclilla gris corte holgado', 'GRIS', 'HOLGADO', 'PANTALONES'),
('pantalon-chino-beige.webp', 'Pantalón Chino Beige Slim', 44.99, 'Pantalón tipo chino beige ajustado', 'BEIGE', 'SLIM', 'PANTALONES'),
('loafers-negro.webp', 'Loafers Negro Clásico', 59.99, 'Zapatos loafers formales de cuero negro', 'NEGRO', 'REGULAR', 'CALZADO'),
('sneakers-blanco.webp', 'Sneakers Blanco Deportivo', 54.99, 'Zapatillas deportivas blancas casual', 'BLANCO', 'REGULAR', 'CALZADO'),
('cinturon-ante.webp', 'Cinturón Negro de Ante', 19.99, 'Cinturón clásico de ante negro', 'NEGRO', 'REGULAR', 'ACCESORIOS'),
('gorra-blanca-sarga.webp', 'Gorra Blanca de Sarga', 14.99, 'Gorra deportiva blanca de sarga', 'BLANCO', 'REGULAR', 'ACCESORIOS');

-- ================================================================
-- INSERTAR STOCK DE TALLAS
-- ================================================================
-- Stock para Camisa Slim Algodón (idPrenda = 1)
INSERT INTO StockTalla (cantidad, talla, idPrenda) VALUES
(25, 'S', 1),
(50, 'M', 1),
(30, 'L', 1),
(15, 'XL', 1);

-- Stock para Camisa Negra Slim Easy (idPrenda = 2)
INSERT INTO StockTalla (cantidad, talla, idPrenda) VALUES
(20, 'S', 2),
(45, 'M', 2),
(35, 'L', 2),
(20, 'XL', 2);

-- Stock para Camisa Negra Regular Easy (idPrenda = 3)
INSERT INTO StockTalla (cantidad, talla, idPrenda) VALUES
(30, 'M', 3),
(40, 'L', 3),
(25, 'XL', 3);

-- Stock para Camisa Celeste Slim Easy (idPrenda = 4)
INSERT INTO StockTalla (cantidad, talla, idPrenda) VALUES
(35, 'S', 4),
(55, 'M', 4),
(40, 'L', 4);

-- Stock para Pantalón Denim Gris (idPrenda = 5)
INSERT INTO StockTalla (cantidad, talla, idPrenda) VALUES
(20, 'M', 5),
(30, 'L', 5),
(25, 'XL', 5);

-- Stock para Pantalón Chino Beige (idPrenda = 6)
INSERT INTO StockTalla (cantidad, talla, idPrenda) VALUES
(40, 'M', 6),
(35, 'L', 6);

-- Stock para Loafers Negro (idPrenda = 7)
INSERT INTO StockTalla (cantidad, talla, idPrenda) VALUES
(15, 'M', 7),
(20, 'L', 7),
(10, 'XL', 7);

-- Stock para Sneakers Blanco (idPrenda = 8)
INSERT INTO StockTalla (cantidad, talla, idPrenda) VALUES
(30, 'M', 8),
(25, 'L', 8);

-- Stock para Cinturón (idPrenda = 9)
INSERT INTO StockTalla (cantidad, talla, idPrenda) VALUES
(100, 'M', 9),
(80, 'L', 9);

-- Stock para Gorra (idPrenda = 10)
INSERT INTO StockTalla (cantidad, talla, idPrenda) VALUES
(60, 'M', 10);

-- ================================================================
-- VERIFICACIÓN
-- ================================================================
SELECT 'PRENDAS INSERTADAS' AS Info, COUNT(*) AS Total FROM Prenda
UNION ALL
SELECT 'STOCK INSERTADO', COUNT(*) FROM StockTalla;

-- Ver las prendas insertadas
SELECT idPrenda, nombrePrenda, precio, categoria, color FROM Prenda ORDER BY idPrenda;

-- ================================================================
-- ✅ SCRIPT COMPLETADO
-- ================================================================
