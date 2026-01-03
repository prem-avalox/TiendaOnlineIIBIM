<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Registro de Prenda</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/estilos.css">

</head>
<body>

    <div class="formulario-registro">
        <h2>Registro de Prenda</h2>

        <form method="POST" action="${pageContext.request.contextPath}/AgregarPrendaController">
            <input type="hidden" name="ruta" value="guardar">

            <div class="form-group">
                <label for="imagen">Imagen (ruta o nombre del archivo):</label>
                <input type="text" id="imagen" name="imagen" class="form-control">
            </div>

            <div class="form-group">
                <label for="nombrePrenda">Nombre de la Prenda:</label>
                <input type="text" id="nombrePrenda" name="nombrePrenda" class="form-control" required>
            </div>

            <div class="form-group">
                <label for="precio">Precio:</label>
                <input type="number" id="precio" name="precio" step="0.01" class="form-control" required>
            </div>

            <div class="form-group">
                <label for="categoria">Categoría:</label>
                <select id="categoria" name="categoria" class="form-control" required>
                    <option value="">-- Seleccione --</option>
                    <c:forEach var="cat" items="${categorias}">
                        <option value="${cat.name()}">${cat}</option>
                    </c:forEach>
                </select>
            </div>

            <div class="form-group">
                <label for="descripcion">Descripción:</label>
                <textarea id="descripcion" name="descripcion" rows="4" class="form-control"></textarea>
            </div>

            <div class="form-group">
                <label for="color">Color:</label>
                <input type="text" id="color" name="color" class="form-control">
            </div>

            <div class="form-group">
                <label for="corte">Corte:</label>
                <input type="text" id="corte" name="corte" class="form-control">
            </div>

            <div class="stock-container">
                <h3>Stock por talla</h3>
                <c:forEach var="t" items="${tallas}">
                    <div class="talla-row">
                        <label>Stock ${t}:</label>
                        <input type="hidden" name="talla" value="${t}" />
                        <input type="number" name="cantidad" min="0" value="0" class="form-control" style="width: 100px;" required>
                    </div>
                </c:forEach>
            </div>

            <button type="submit" class="btn-guardar">Guardar</button>
        </form>
    </div>

    <c:if test="${registroExitoso}">
        <div id="modalExito" class="modal-overlay">
            <div class="modal-content">
                <span class="close-btn" onclick="cerrarModal()">&times;</span>
                <h1>¡Operación Exitosa!</h1>
                <p>La prenda ha sido registrada correctamente en el sistema.</p>
                <hr>
                <button type="button" class="btn-aceptar" onclick="cerrarModal()">Aceptar</button>
            </div>
        </div>
    </c:if>

    <script src="${pageContext.request.contextPath}/js/formulario.js"></script>

</body>
</html>