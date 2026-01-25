<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>

<!-- SIDEBAR DE LA BOLSA -->
<aside class="cart-sidebar" id="cartSidebar">

	<!-- HEADER -->
	<div class="cart-sidebar-header">
		<h3>MI BOLSA</h3>
		<label for="toggle-cart" class="close-cart-btn"> <i
			class="fas fa-times"></i>
		</label>
	</div>

	<!-- CONTENIDO -->
	<div class="cart-sidebar-content cart-content-data">

		<!-- MENSAJE DE ERROR -->
		<c:if test="${not empty mensajeError}">
			<p class="mensaje-error">${mensajeError}</p>
		</c:if>

		<!-- BOLSA VACÍA -->
		<c:if test="${not empty bolsa and empty bolsa.items}">

			<div class="empty-cart">
				<i class="fas fa-shopping-bag empty-icon"></i>
				<p class="empty-message">Aún no ha agregado items a la bolsa</p>
				<p class="empty-submessage">Agrega prendas para comenzar</p>
				<label for="toggle-cart" class="continue-shopping-btn">
					Seguir comprando </label>
			</div>
		</c:if>

		<!-- BOLSA CON ITEMS -->
		<c:if test="${not empty bolsa and not empty bolsa.items}">

			<div class="cart-items-list">
				<c:forEach var="item" items="${bolsa.items}">

					<div class="cart-item">

						<!-- IMAGEN -->
						<div class="cart-item-image">
							<img
								src="${pageContext.request.contextPath}/${item.prenda.imagen}"
								alt="${item.prenda.nombrePrenda}">

						</div>

						<!-- INFO -->
						<div class="cart-item-info">

							<h4 class="cart-item-name">${item.prenda.nombrePrenda}</h4>

							<p class="cart-item-size">Talla: ${item.tallaSeleccionada}</p>

							<p class="cart-item-price">
								<fmt:formatNumber value="${item.prenda.precio}" type="currency"
									currencySymbol="$" />
							</p>

							<p class="cart-item-subtotal">
								Subtotal:
								<fmt:formatNumber value="${item.prenda.precio * item.cantidad}"
									type="currency" currencySymbol="$" />
							</p>

							<!-- ACCIONES -->
							<div class="cart-item-actions"
								style="display: flex; align-items: center; justify-content: 建设; gap: 15px; margin-top: 10px;">

								<div style="display: flex; align-items: center; gap: 5px;">
									<button type="button"
										onclick="cambiarCantidad(${item.idItem}, -1)"
										style="background-color: #007bff; color: white; border: none; border-radius: 4px; width: 28px; height: 28px; cursor: pointer; display: flex; align-items: center; justify-content: center; font-weight: bold;">
										<i class="fas fa-minus"></i>
									</button>

									<input type="text" id="qty_${item.idItem}"
										value="${item.cantidad}" readonly
										style="width: 35px; text-align: center; border: 1px solid #ccc; border-radius: 4px; height: 24px; font-size: 14px;">

									<button type="button"
										onclick="cambiarCantidad(${item.idItem}, 1)"
										style="background-color: #007bff; color: white; border: none; border-radius: 4px; width: 28px; height: 28px; cursor: pointer; display: flex; align-items: center; justify-content: center; font-weight: bold;">
										<i class="fas fa-plus"></i>
									</button>
								</div>

								<button type="button" class="remove-btn"
									onclick="eliminarItem(${item.idItem})"
									style="color: #dc3545; border: none; background: none; cursor: pointer; font-size: 14px; display: flex; align-items: center; gap: 5px;">
									<i class="fas fa-trash"></i> Quitar
								</button>
							</div>

							<script>
    setTimeout(() => {
        const errorMsg = document.querySelector('.mensaje-error');
        if (errorMsg) errorMsg.style.display = 'none';
    }, 3000);
</script>
						</div>
					</div>
				</c:forEach>
			</div>

			<!-- TOTAL -->
			<div class="cart-total">
				<p class="total-label">Total</p>
				<p class="total-amount">
					<fmt:formatNumber value="${bolsa.precioTotal}" type="currency"
						currencySymbol="$" />
				</p>
			</div>

			<!-- ACCIONES -->
			

		</c:if>

	</div>
</aside>
