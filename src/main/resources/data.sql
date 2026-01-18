-- Script SQL que JPA puede ejecutar automáticamente al iniciar
-- Solo se ejecuta si la propiedad está configurada en persistence.xml

-- USUARIOS
INSERT INTO Usuario (nombreUsuario, correo, contrasenia, esAdministrador) 
VALUES ('testuser', 'test@example.com', 'password123', 0);

INSERT INTO Usuario (nombreUsuario, correo, contrasenia, esAdministrador) 
VALUES ('admin', 'admin@example.com', 'admin123', 1);

-- PRENDAS
INSERT INTO Prenda (imagenPrenda, nombrePrenda, precio, descripcion, color, corte, categoria) 
VALUES ('camisa-celeste.jpg', 'Camisa Celeste Slim Fit', 29.99, 'Camisa elegante de corte slim', 'CELESTE', 'SLIM', 'CAMISAS');

INSERT INTO Prenda (imagenPrenda, nombrePrenda, precio, descripcion, color, corte, categoria) 
VALUES ('pantalon-negro.jpg', 'Pantalón Negro Regular', 39.99, 'Pantalón clásico de corte regular', 'NEGRO', 'REGULAR', 'PANTALONES');

-- STOCK (asumiendo IDs auto-incrementales)
INSERT INTO StockTalla (cantidad, talla, idPrenda) VALUES (50, 'M', 1);
INSERT INTO StockTalla (cantidad, talla, idPrenda) VALUES (40, 'M', 2);

-- BOLSA
INSERT INTO Bolsa (precioTotal, idUsuario) VALUES (0.0, 1);

-- ITEMS
INSERT INTO ItemBolsa (cantidad, subtotal, talla, idPrenda, idBolsa) VALUES (2, 59.98, 'M', 1, 1);
INSERT INTO ItemBolsa (cantidad, subtotal, talla, idPrenda, idBolsa) VALUES (1, 39.99, 'M', 2, 1);

-- ACTUALIZAR TOTAL
UPDATE Bolsa SET precioTotal = 99.97 WHERE idBolsa = 1;
