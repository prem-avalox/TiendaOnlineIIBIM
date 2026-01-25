<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Clothing Store - Catálogo</title>

<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/estilos.css">

<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
</head>

<body class="general">
	<input type="checkbox" id="toggle-sidebar" ${menuAbierto} hidden>
	<input type="checkbox" id="toggle-cart" hidden>

	<div class="header">
		<!-- boton para abrir el sidebar categorias -->
		<label for="toggle-sidebar" class="filter-bar-btn"
			style="cursor: pointer;"> <i class="fas fa-bars"></i>
		</label> </a>

		<div class="logo">
			<h1>CLOTHING STORE</h1>
		</div>

		<div class="button-container">
			<!-- boton para buscar una prenda por el nombre -->
			<div id="search" class="search-container">
				<i class="fas fa-search"></i>
				<div class="search-form-wrapper">
					<form action="VerCatalogoController" method="get">
						<input type="hidden" name="ruta" value="buscar">
						<div class="search-inputs">
							<input type="text" name="nombre" placeholder="Buscar producto..."
								class="search-input">
							<button type="submit" class="search-btn">BUSCAR</button>
						</div>
					</form>
				</div>
			</div>

			<!-- icono usuario -->
			<div class="button-container">
				<div id="user-info" class="icon-link">
					<c:choose>
						<c:when test="${not empty sessionScope.usuarioLogeado}">
							<div class="user-menu-container">
								<span class="user-name-display">${sessionScope.usuarioLogeado.nombreUsuario}</span>
								<a href="CerrarSesionController" title="Cerrar Sesión"> <i
									class="fas fa-sign-out-alt"></i>
								</a>
							</div>
						</c:when>
						<c:otherwise>
							<a href="RegistrarseController?ruta=crear"> <i
								class="far fa-user"></i>
							</a>
						</c:otherwise>
					</c:choose>
				</div>

			</div>

			<label for="toggle-cart" id="shopping-bag" class="icon-link"
				onclick="cargarBolsa()"> <i class="fas fa-briefcase"></i>
			</label>
		</div>
	</div>

	<aside id="sidebar" class="sidebar">
		<div class="sidebar-header">
			<h2>CATEGORÍAS</h2>
			<label for="toggle-sidebar" class="close-btn"> <i
				class="fas fa-times"></i></label>
		</div>

		<ul class="sidebar-menu">
			<li><a href="VerCatalogoController?ruta=ingresar"
				class="active-category">Ver Todo</a></li>

			<c:forEach var="cat" items="${categorias}">
				<li><a
					href="VerCatalogoController?ruta=seleccionarCategoria&idCategoria=${cat.name()}">
						${cat.nombreCategoria} </a></li>
			</c:forEach>
		</ul>
	</aside>

	<label id="overlay" class="overlay" for="toggle-sidebar"></label>

	<!-- seccion para aplicar filtros -->
	<nav class="category-nav-bar">
		<form action="VerCatalogoController" method="get">
			<input type="hidden" name="ruta" value="aplicarFiltros">
			<ul class="cajas-filtro-lista">

				<li class="filtro-item"><input type="checkbox"
					id="filtro-tamano" class="filtro-toggle" hidden> <label
					for="filtro-tamano" class="filtro-label"> TALLA <i
						class="fas fa-chevron-down"></i>
				</label>
					<div class="filter-dropdown">
						<label><input type="radio" name="talla" value="" checked>
							Todas</label>
						<c:forEach var="t" items="${tallas}">
							<label><input type="radio" name="talla"
								value="${t.name()}"> ${t.talla}</label>
						</c:forEach>
					</div></li>

				<li class="filtro-item"><input type="checkbox"
					id="filtro-color" class="filtro-toggle" hidden> <label
					for="filtro-color" class="filtro-label"> COLOR <i
						class="fas fa-chevron-down"></i>
				</label>
					<div class="filter-dropdown">
						<label><input type="radio" name="color" value="" checked>
							Todos</label>
						<c:forEach var="c" items="${colores}">
							<label><input type="radio" name="color"
								value="${c.name()}"> ${c.nombreColor}</label>
						</c:forEach>
					</div></li>

				<li class="filtro-item"><input type="checkbox"
					id="filtro-corte" class="filtro-toggle" hidden> <label
					for="filtro-corte" class="filtro-label"> CORTE <i
						class="fas fa-chevron-down"></i>
				</label>
					<div class="filter-dropdown">
						<label><input type="radio" name="corte" value="" checked>
							Todos</label>
						<c:forEach var="co" items="${cortes}">
							<label><input type="radio" name="corte"
								value="${co.name()}"> ${co.nombreCorte}</label>
						</c:forEach>
					</div></li>

				<li class="filtro-item">
					<button type="submit" class="apply-filters-btn">APLICAR</button>
				</li>
			</ul>
		</form>
	</nav>

	<section class="product-section">
		<c:choose>
			<c:when test="${not empty mensajeError}">
				<div class="mensaje-error">
					<p>⚠️ ${mensajeError}</p>
					<button
						onclick="location.href='VerCatalogoController?ruta=ingresar'"
						class="retry-btn">Reintentar</button>
				</div>
			</c:when>

			<c:when test="${not empty prendas}">
				<c:forEach var="prenda" items="${prendas}">
					<div class="tarjeta-producto">
						<a
							href="VerCatalogoController?ruta=visualizarPrenda&idPrenda=${prenda.idPrenda}"
							class="product-link">
							<div class="product-image-container">
								<img class="product-image"
									src="${pageContext.request.contextPath}/${prenda.imagen}"
									alt="${prenda.nombrePrenda}">
							</div>
							<div class="product-info">
								<p class="product-name">${prenda.nombrePrenda}</p>
								<p class="product-price">$${prenda.precio}</p>
							</div>
						</a>
					</div>
				</c:forEach>
			</c:when>
		</c:choose>
	</section>

	<div id="cartContent">
		<%@ include file="SidebarBolsa.jsp"%>
	</div>

	<label id="cart-overlay" class="overlay" for="toggle-cart"></label>

	<footer class="footer">
		<p>&copy; 2026 Clothing Store. Imágenes cortesía de H&M.</p>
	</footer>

	<script>
// 1. Cargar la bolsa al abrir el sidebar
function cargarBolsa() {
    fetch('${pageContext.request.contextPath}/VerBolsaController?ruta=abrirBolsa')
        .then(r => r.text())
        .then(html => {
            document.getElementById("cartContent").innerHTML = html;
        })
        .catch(() => {
            document.getElementById("cartContent").innerHTML = 
                '<div class="empty-cart">Error al cargar la bolsa</div>';
        });
}

// 2. Lógica de botones + y -
function cambiarCantidad(idItem, cambio) {
    const input = document.getElementById('qty_' + idItem);
    let cantidadActual = parseInt(input.value);
    let nuevaCantidad = cantidadActual + cambio;

    if (nuevaCantidad >= 1) {
        // Actualización inmediata en el servidor
        ajustarCantidadServidor(idItem, nuevaCantidad);
    }
}

function ajustarCantidadServidor(idItem, nuevaCantidad) {
    fetch('${pageContext.request.contextPath}/VerBolsaController', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: 'ruta=ajustarCantidadItem&idItem=' + idItem + '&nuevaCantidad=' + nuevaCantidad
    })
    .then(r => r.text())
    .then(html => {
        document.getElementById("cartContent").innerHTML = html;
        // El script de desaparición automática ya está en el SidebarBolsa.jsp
    })
    .catch(err => console.error("Error:", err));
}

// 3. Eliminar Item
function eliminarItem(idItem) {
    fetch('${pageContext.request.contextPath}/VerBolsaController', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: 'ruta=eliminarItem&idItem=' + idItem
    })
    .then(r => r.text())
    .then(html => {
        document.getElementById("cartContent").innerHTML = html;
    })
    .catch(err => alert("Error al eliminar item"));
}

// 4. Temporizadores para mensajes (si existen al cargar la página)
document.addEventListener("DOMContentLoaded", function() {
    setTimeout(() => {
        const msgs = document.querySelectorAll('.mensaje-error, .mensaje-exito');
        msgs.forEach(m => m.style.display = 'none');
    }, 3000);
});
</script>
</body>
</html>