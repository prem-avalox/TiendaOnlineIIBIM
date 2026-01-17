<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Administración de Prendas</title>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/estilos.css">
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
</head>
<body class="general">
	<div class="header">
		<div class="logo">
			<h1>GESTIÓN DE PRENDAS</h1>
		</div>
		<div class="button-container">
			<a href="AgregarPrendaController?ruta=agregarPrenda"
				class="icon-link"> <i class="fas fa-plus-circle"></i> Agregar
			</a> <a href="VerCatalogoController?ruta=listar" class="icon-link"> <i
				class="fas fa-eye"></i> Ver Catálogo
			</a>
		</div>
	</div>

	<div class="admin-container">
		<div class="table-wrapper">
			<table class="admin-table">
				<thead>
					<tr>
						<th>Imagen</th>
						<th>Nombre Prenda</th>
						<th>Precio</th>
						<th>Actualizar</th>
						<th>Eliminar</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="p" items="${prendas}">
						<tr>
							<td><img
								src="${pageContext.request.contextPath}/${p.imagen}"
								class="img-mini" alt="${p.nombrePrenda}"></td>
							<td>${p.nombrePrenda}</td>
							<td>$${p.precio}</td>
							<td><a
								href="GestionarPrendasController?ruta=editarPrenda&id=${p.idPrenda}"
								class="btn-edit"> <i class="fas fa-edit"></i>
							</a></td>
							<td><a
								href="GestionarPrendasController?ruta=eliminarPrenda&id=${p.idPrenda}"
								class="btn-delete"> <i class="fas fa-trash"></i>
							</a></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>



	<c:if test="${registroExitoso}">
		<div id="modalExito" class="modal-overlay">
			<div class="modal-content">
				<span class="close-btn-minimal"
					onclick="this.parentElement.parentElement.style.display='none'">&times;</span>
				<div class="modal-body">
					<h2 class="modal-title">¡Operación Exitosa!</h2>
					<p class="modal-text">La prenda ha sido editada correctamente.</p>
				</div>
			</div>
		</div>
	</c:if>

	<c:if test="${not empty mensajeExito2}">
		<div id="modalExito2" class="modal-overlay">
			<div class="modal-content">
				<span class="close-btn-minimal"
					onclick="this.parentElement.parentElement.style.display='none'">&times;</span>
				<div class="modal-body">
					<h2 class="modal-title">¡Eliminado!</h2>
					<p class="modal-text">${mensajeExito2}</p>
				</div>
			</div>
		</div>
	</c:if>

</body>
</html>