<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>${prenda.nombrePrenda}-Detalle</title>

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/estilos.css">

<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
</head>

<body class="general">

	<input type="checkbox" id="toggle-cart" hidden>

	<!-- ================= HEADER ================= -->
	<div class="header">

		<a
			href="${pageContext.request.contextPath}/VerCatalogoController?ruta=iniciar"
			class="filter-bar-btn"> <i class="fas fa-arrow-left"></i> Volver
		</a>

		<div class="logo">
			<h1>DETALLE</h1>
		</div>

		<div class="button-container">
			<label for="toggle-cart" id="shopping-bag" class="icon-link"
				onclick="cargarBolsa()"> <i class="fas fa-briefcase"></i>
			</label>
		</div>
	</div>

	<!-- ================= DETALLE PRENDA ================= -->
	<div class="prenda-container-detalle">

		<!-- IZQUIERDA -->
		<div class="detalle-izquierda">
			<img src="${pageContext.request.contextPath}/${prenda.imagen}"
				alt="${prenda.nombrePrenda}" class="img-detalle">
		</div>

		<!-- DERECHA -->
		<div class="detalle-derecha">

			<h2 class="product-name-detalle">${prenda.nombrePrenda}</h2>
			<p class="precio-detalle">$${prenda.precio}</p>

			<!-- ================= TALLAS ================= -->
			<div class="talla-section">
				<p class="label-detalle">Talla</p>

				<ul class="cajas-talla-lista">
					<c:forEach var="t" items="${tallas}">
						<li><input type="radio" name="tallaRadio"
							id="talla_${t.name()}" value="${t.name()}"> <label
							for="talla_${t.name()}" class="talla-item"> ${t.talla} </label></li>
					</c:forEach>
				</ul>

				<c:if test="${not empty mensajeStock}">
					<div class="mensaje-error">⚠ ${mensajeStock}</div>
				</c:if>


				<p id="errorTalla" class="mensaje-error" style="display: none;">
					Debe seleccionar una talla antes de agregar a la bolsa</p>

				<div class="guia-tallas">
					<i class="fas fa-ruler-combined"></i> Guía de Tallas
				</div>
			</div>

			<!-- ================= FORM AGREGAR A BOLSA ================= -->
			<form
				action="${pageContext.request.contextPath}/AgregarItemBolsaController"
				method="post" onsubmit="return validarTallaSeleccionada();">

				<input type="hidden" name="ruta" value="agregarABolsa"> <input
					type="hidden" name="idPrenda" value="${prenda.idPrenda}"> <input
					type="hidden" name="cantidad" value="1"> <input
					type="hidden" name="idTalla" id="tallaSeleccionada">

				<c:choose>
					<c:when test="${not empty sessionScope.usuarioLogeado}">
						<button type="submit" class="btn-agregar-bolsa">Agregar a
							la bolsa</button>
					</c:when>

					<c:otherwise>
						<button type="button" class="btn-agregar-bolsa disabled" disabled>
							Agregar a la bolsa</button>

						<p class="mensaje-login-requerido">Debes iniciar sesión para
							agregar productos a la bolsa.</p>
					</c:otherwise>
				</c:choose>
			</form>

			<!-- ================= MENSAJE ÉXITO ================= -->
			<c:if test="${sessionScope.itemAgregado}">
				<div class="mensaje-exito">✔ El item ha sido agregado a la
					bolsa</div>

				<script>
                cargarBolsa();
            </script>

				<c:remove var="itemAgregado" scope="session" />
			</c:if>

			<!-- ================= DESCRIPCIÓN ================= -->
			<div class="descripcion-detalle">
				<p>${prenda.descripcion}</p>
			</div>

			<div class="info-adicional">
				<p>
					<strong>Categoría:</strong> ${prenda.categoria.nombreCategoria}
				</p>
				<p>
					<strong>Color:</strong> ${prenda.color}
				</p>
				<p>
					<strong>Corte:</strong> ${prenda.corte}
				</p>
			</div>

		</div>
	</div>

	<!-- ================= SIDEBAR BOLSA ================= -->
	<div id="cartContent">
		<%@ include file="SidebarBolsa.jsp"%>
	</div>

	<label id="cart-overlay" class="overlay" for="toggle-cart"></label>

	<!-- ================= SCRIPTS ================= -->
	<script>

function validarTallaSeleccionada() {

    const radio = document.querySelector(
        'input[name="tallaRadio"]:checked'
    );

    const error = document.getElementById("errorTalla");

    if (!radio) {
        error.style.display = "block";
        setTimeout(() => error.style.display = "none", 3000);
        return false;
    }

    document.getElementById("tallaSeleccionada").value = radio.value;
    return true;
}

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

function eliminarItem(idItem) {
    // Agregamos un log para depurar
    console.log("Eliminando item:", idItem);

    fetch('${pageContext.request.contextPath}/VerBolsaController', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        // Importante: que 'ruta' coincida con el switch del Controller
        body: 'ruta=eliminarItem&idItem=' + idItem
    })
    .then(r => r.text())
    .then(html => {
        // Esto actualiza el contenido del sidebar sin recargar toda la pantalla
        document.getElementById("cartContent").innerHTML = html;
    })
    .catch(err => {
        console.error(err);
        alert("Error al eliminar el item");
    });
}

//Función para cambiar cantidad con botones +/-
function cambiarCantidad(idItem, cambio) {
    const input = document.getElementById('qty_' + idItem);
    let nuevaCantidad = parseInt(input.value) + cambio;

    if (nuevaCantidad >= 1) {
        // Ejecuta el ajuste automáticamente sin necesidad de botón adicional
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
        // Al inyectar el nuevo HTML, el script de desaparición en el sidebar se activará
    });
}

// Lógica para que los mensajes en prenda.jsp desaparezcan
document.addEventListener("DOMContentLoaded", function() {
    // Para el mensaje de "agregado a la bolsa"
    const exitoMsg = document.querySelector('.mensaje-exito');
    if (exitoMsg) {
        setTimeout(() => exitoMsg.style.display = 'none', 3000);
    }

    // Para el mensaje de "no hay stock" en la selección de talla
    const stockMsg = document.querySelector('.mensaje-error');
    if (stockMsg) {
        setTimeout(() => stockMsg.style.display = 'none', 3000);
    }
});

</script>

</body>
</html>
