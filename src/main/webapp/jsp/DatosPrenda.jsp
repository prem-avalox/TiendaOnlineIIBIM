<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Editar Prenda</title>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/estilos.css">
</head>
<body class="general">
	<div class="header">
		<div class="logo">
			<h1>EDITAR PRENDA ID: ${p.idPrenda}</h1>
		</div>
	</div>

	<div class="formulario-registro">
		<form method="POST"
			action="${pageContext.request.contextPath}/GestionarPrendasController">
			<%-- Identificador de ruta para el ruteador del controlador --%>
			<input type="hidden" name="ruta" value="guardar">
			<%-- Campo oculto para el ID de la prenda (necesario para actualizar en la BD) --%>
			<input type="hidden" name="idPrenda" value="${p.idPrenda}">

			<%-- Insertar este bloque dentro del formulario en datos_prenda.jsp --%>
			<div class="form-group">
				<label for="imagen">Imagen (ruta o nombre del archivo):</label> <input
					type="text" id="imagen" name="imagen" value="${p.imagen}"
					class="form-control" required>
			</div>


			<div class="form-group">
				<label for="nombrePrenda">Nombre de la Prenda:</label> <input
					type="text" id="nombrePrenda" name="nombrePrenda"
					value="${p.nombrePrenda}" class="form-control" required>
			</div>

			<div class="form-group">
				<label for="precio">Precio:</label> <input type="number" id="precio"
					name="precio" step="0.01" value="${p.precio}" class="form-control"
					min="0" onkeypress="return impedirNegativos(event)"
					oninput="validarPositivo(this)" required>
			</div>

			<div class="form-group">
				<label for="categoria">Categoría:</label> <select id="categoria"
					name="categoria" class="form-control" required>
					<c:forEach var="cat" items="${categorias}">
						<option value="${cat.name()}"
							${cat == p.categoria ? 'selected' : ''}>${cat}</option>
					</c:forEach>
				</select>
			</div>

			<div class="form-group">
				<label for="descripcion">Descripción:</label>
				<textarea id="descripcion" name="descripcion" rows="4" 
					class="form-control" required>${p.descripcion}</textarea>
			</div>

			<div class="form-group">
				<label for="color">Color:</label> <select name="color"
					class="form-control" required>
					<c:forEach var="c" items="${colores}">
						<option value="${c.name()}" ${c == p.color ? 'selected' : ''}>
							${c.nombreColor}</option>
					</c:forEach>
				</select>
			</div>


			<select name="corte" class="form-control" required>
				<c:forEach var="co" items="${cortes}">
					<option value="${co.name()}" ${co == p.corte ? 'selected' : ''}>
						${co}</option>
				</c:forEach>
			</select>

			<style>
.talla-row {
	display: flex;
	justify-content: space-between;
	align-items: center;
	border-bottom: 1px solid #eee;
	padding: 20px 0;
	max-width: 600px; /* Para que no se estire a toda la pantalla */
}

.talla-row input {
	text-align: right; /* Número a la derecha */
	width: 50px;
}
</style>

			<div class="stock-container">
				<h3>Actualizar Stock por Talla</h3>

				<c:forEach var="tallaDisp" items="${tallasDisponibles}">
					<c:set var="cantidadActual" value="0" />
					<c:forEach var="st" items="${p.stockTallas}">
						<c:if test="${st.talla == tallaDisp}">
							<c:set var="cantidadActual" value="${st.cantidad}" />
						</c:if>
					</c:forEach>

					<div class="talla-row">
						<label>Stock ${tallaDisp}:</label> <input type="hidden"
							name="tallas" value="${tallaDisp}" /> <input type="number"
							name="cantidad_${tallaDisp}" min="0" value="${cantidadActual}"
							class="form-control" onkeypress="return impedirNegativos(event)"
							oninput="validarPositivo(this)" required />
					</div>
				</c:forEach>
			</div>


			<div class="button-group-admin">
				<button type="submit" class="btn-guardar">Actualizar
					Cambios</button>
				<a href="GestionarPrendasController?ruta=listar"
					class="btn-cancelar">Cancelar</a>
			</div>
		</form>
	</div>
</body>
<script>
    // 1. Evita que el usuario escriba el signo '-' o la letra 'e'
    function impedirNegativos(event) {
        if (event.key === '-' || event.key === 'e' || event.key === 'E') {
            return false;
        }
        return true;
    }

    // 2. Si el usuario logra pegar un número negativo, lo resetea a 0
    function validarPositivo(input) {
        if (input.value < 0) {
            input.value = 0;
        }
    }

    // 3. Temporizador para mensajes de error (si los hubiera)
    setTimeout(() => {
        const msg = document.querySelector('.mensaje-error');
        if (msg) msg.style.display = 'none';
    }, 3000);
</script>
</html>