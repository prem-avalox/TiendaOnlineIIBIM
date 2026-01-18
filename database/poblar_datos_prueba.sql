-- ================================================================
-- SCRIPT SQL PARA POBLAR BASE DE DATOS - CU11 VER BOLSA
-- ================================================================
-- INSTRUCCIONES:
-- 1. Asegúrate de que las tablas ya existan (ejecuta la app primero)
-- 2. Abre phpMyAdmin (http://localhost/phpmyadmin)
-- 3. Selecciona la base de datos 'tienda_online'
-- 4. Ve a la pestaña SQL
-- 5. Copia y pega este script
-- 6. Haz clic en "Ejecutar"
-- ================================================================

USE tienda_online;

-- Limpiar datos existentes (CUIDADO: borra todo)
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE ItemBolsa;
TRUNCATE TABLE Bolsa;
TRUNCATE TABLE StockTalla;
TRUNCATE TABLE Prenda;
TRUNCATE TABLE Usuario;
SET FOREIGN_KEY_CHECKS = 1;

-- ================================================================
-- 1. INSERTAR USUARIOS
-- ================================================================
INSERT INTO Usuario (nombreUsuario, correo, contrasenia, esAdministrador) VALUES
('martin', 'martin@example.com', 'password123', 0),
('testuser', 'test@example.com', 'password123', 0),
('admin', 'admin@example.com', 'admin123', 1);

-- ================================================================
-- 2. INSERTAR PRENDAS
-- ================================================================
INSERT INTO Prenda (imagenPrenda, nombrePrenda, precio, descripcion, color, corte, categoria) VALUES
('camisa-celeste.jpg', 'Camisa Celeste Slim Fit', 29.99, 'Camisa elegante de corte slim', 'CELESTE', 'SLIM', 'CAMISAS'),
('pantalon-negro.jpg', 'Pantalón Negro Regular', 39.99, 'Pantalón clásico de corte regular', 'NEGRO', 'REGULAR', 'PANTALONES'),
('camisa-blanca.jpg', 'Camisa Blanca Regular', 24.99, 'Camisa básica de algodón', 'BLANCO', 'REGULAR', 'CAMISAS'),
('loafers-negro.jpg', 'Loafers Negro Clásico', 59.99, 'Zapatos formales de cuero', 'NEGRO', 'REGULAR', 'CALZADO'),
('cinturon-negro.jpg', 'Cinturón Negro de Cuero', 19.99, 'Cinturón clásico de ante', 'NEGRO', 'REGULAR', 'ACCESORIOS');

-- ================================================================
-- 3. INSERTAR STOCK DE TALLAS
-- ================================================================
-- Stock para Camisa Celeste (idPrenda = 1)
INSERT INTO StockTalla (cantidad, talla, idPrenda) VALUES
(25, 'S', 1),
(50, 'M', 1),
(30, 'L', 1);

-- Stock para Pantalón Negro (idPrenda = 2)
INSERT INTO StockTalla (cantidad, talla, idPrenda) VALUES
(40, 'M', 2),
(35, 'L', 2);

-- Stock para Camisa Blanca (idPrenda = 3)
INSERT INTO StockTalla (cantidad, talla, idPrenda) VALUES
(60, 'M', 3);

-- Stock para Loafers (idPrenda = 4)
INSERT INTO StockTalla (cantidad, talla, idPrenda) VALUES
(20, 'L', 4);

-- Stock para Cinturón (idPrenda = 5)
INSERT INTO StockTalla (cantidad, talla, idPrenda) VALUES
(100, 'M', 5);

-- ================================================================
-- 4. CREAR BOLSA PARA MARTIN (usuario principal)
-- ================================================================
INSERT INTO Bolsa (precioTotal, idUsuario) VALUES
(0.0, 1);  -- martin

-- ================================================================
-- 5. AGREGAR ITEMS A LA BOLSA DE MARTIN
-- ================================================================
-- Item 1: Camisa Celeste x2 (Talla M)
INSERT INTO ItemBolsa (cantidad, subtotal, talla, idPrenda, idBolsa) VALUES
(2, 59.98, 'M', 1, 1);

-- Item 2: Pantalón Negro x1 (Talla M)
INSERT INTO ItemBolsa (cantidad, subtotal, talla, idPrenda, idBolsa) VALUES
(1, 39.99, 'M', 2, 1);

-- Item 3: Cinturón x1 (Talla M)
INSERT INTO ItemBolsa (cantidad, subtotal, talla, idPrenda, idBolsa) VALUES
(1, 19.99, 'M', 5, 1);

-- ================================================================
-- 6. ACTUALIZAR PRECIO TOTAL DE LA BOLSA
-- ================================================================
UPDATE Bolsa SET precioTotal = 119.96 WHERE idBolsa = 1;

-- ================================================================
-- 7. CREAR BOLSAS VACÍAS PARA OTROS USUARIOS
-- ================================================================
INSERT INTO Bolsa (precioTotal, idUsuario) VALUES
(0.0, 2),  -- testuser
(0.0, 3);  -- admin

-- ================================================================
-- VERIFICACIÓN: Ver datos insertados
-- ================================================================
SELECT 'USUARIOS' AS Tabla, COUNT(*) AS Total FROM Usuario
UNION ALL
SELECT 'PRENDAS', COUNT(*) FROM Prenda
UNION ALL
SELECT 'STOCK', COUNT(*) FROM StockTalla
UNION ALL
SELECT 'BOLSAS', COUNT(*) FROM Bolsa
UNION ALL
SELECT 'ITEMS', COUNT(*) FROM ItemBolsa;

-- Ver bolsa de martin con sus items
SELECT 
    b.idBolsa,
    u.nombreUsuario,
    b.precioTotal,
    COUNT(i.idItem) as CantidadItems
FROM Bolsa b
JOIN Usuario u ON b.idUsuario = u.idUsuario
LEFT JOIN ItemBolsa i ON b.idBolsa = i.idBolsa
WHERE u.nombreUsuario = 'martin'
GROUP BY b.idBolsa, u.nombreUsuario, b.precioTotal;

-- ================================================================
-- ✅ SCRIPT COMPLETADO
-- ================================================================
