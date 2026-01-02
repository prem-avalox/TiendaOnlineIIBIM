<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
    <title>Registro de Prenda</title>
</head>
<body>

	 <h2>Registro de Prenda</h2>

    <form method="POST" action="../AgregarPrendaController" >

        <label for="idPrenda">ID Prenda:</label><br>
        <input type="number" id="idPrenda" name="idPrenda" required><br><br>

        <label for="imagen">Imagen (ruta o nombre del archivo):</label><br>
        <input type="text" id="imagen" name="imagen"><br><br>

        <label for="nombrePrenda">Nombre de la Prenda:</label><br>
        <input type="text" id="nombrePrenda" name="nombrePrenda" required><br><br>

        <label for="precio">Precio:</label><br>
        <input type="number" id="precio" name="precio" step="0.01" required><br><br>

        <label for="descripcion">Descripción:</label><br>
        <textarea id="descripcion" name="descripcion" rows="4" cols="30"></textarea><br><br>

        <label for="color">Color:</label><br>
        <input type="text" id="color" name="color"><br><br>

        <label for="corte">Corte:</label><br>
        <input type="text" id="corte" name="corte"><br><br>

        <button type="submit">Registrar Prenda</button>

    </form>

	

</body>
</html>