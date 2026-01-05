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
			action="${pageContext.request.contextPath}/VerListaCompletaController">
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
					required>
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
					class="form-control">${p.descripcion}</textarea>
			</div>

			<div class="form-group">
				<label for="color">Color:</label> <input type="text" id="color"
					name="color" value="${p.color}" class="form-control">
			</div>

			<div class="form-group">
				<label for="corte">Corte:</label> <input type="text" id="corte"
					name="corte" value="${p.corte}" class="form-control">
			</div>

			<div class="stock-container">
				<h3>Actualizar Stock por Talla</h3>
				<c:forEach var="tallaDisp" items="${tallasDisponibles}">
					<div class="talla-row">
						<label>Stock ${tallaDisp}:</label> <input type="hidden"
							name="talla" value="${tallaDisp}" />

						<%-- Buscamos si la prenda ya tiene stock definido para esta talla --%>
						<c:set var="cantidadActual" value="0" />
						<c:forEach var="st" items="${p.stockTallas}">
							<c:if test="${st.talla == tallaDisp}">
								<c:set var="cantidadActual" value="${st.cantidad}" />
							</c:if>
						</c:forEach>

						<input type="number" name="cantidad" min="0"
							value="${cantidadActual}" class="form-control"
							style="width: 100px;" required>
					</div>
				</c:forEach>
			</div>

			<div class="button-group-admin">
				<button type="submit" class="btn-guardar">Actualizar
					Cambios</button>
				<a href="VerListaCompletaController?ruta=guardar"
					class="btn-cancelar">Cancelar</a>
			</div>
		</form>
	</div>
</body>
</html>