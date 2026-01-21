<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>

<!-- SIDEBAR DE LA BOLSA -->
<aside class="cart-sidebar" id="cartSidebar">
	<div class="cart-sidebar-header">
		<h3>MI BOLSA</h3>
		<label for="toggle-cart" class="close-cart-btn">
			<i class="fas fa-times"></i>
		</label>
	</div>
	
	<div class="cart-sidebar-content" id="cartContent">
		<c:choose>
			<c:when test="${items == null}">
				<div class="empty-cart">
					<i class="fas fa-shopping-bag empty-icon"></i>
					<p class="empty-message">Cargando...</p>
				</div>
			</c:when>
			<c:when test="${bolsaVacia == true || empty items || items.size() == 0}">
				<div class="empty-cart">
					<i class="fas fa-shopping-bag empty-icon"></i>
					<p class="empty-message">No hay items en la bolsa</p>
					<p class="empty-subtitle">Agrega productos para empezar</p>
				</div>
			</c:when>
			<c:otherwise>
				<div class="cart-items">
					<c:forEach var="item" items="${items}">
						<div class="cart-item" data-item-id="${item.idItem}">
							<div class="cart-item-image">
								<img src="${pageContext.request.contextPath}/img/${item.prenda.imagen}" 
								     alt="${item.prenda.nombrePrenda}"
								     onerror="this.src='${pageContext.request.contextPath}/img/placeholder.jpg'">
							</div>
							
							<div class="cart-item-details">
								<h4 class="cart-item-name">${item.prenda.nombrePrenda}</h4>
								<p class="cart-item-info">
									<span class="cart-item-size">Talla: ${item.tallaSeleccionada}</span>
								</p>
								<p class="cart-item-price">
									<fmt:formatNumber value="${item.prenda.precio}" type="currency" currencySymbol="$" minFractionDigits="2" maxFractionDigits="2"/>
								</p>
								
								<%-- Control de cantidad según UML: Bolsa.actualizarCantidad() --%>
								<div class="cart-item-quantity-controls">
									<button class="quantity-btn" onclick="actualizarCantidad(${item.idItem}, ${item.cantidad - 1})">-</button>
									<span class="quantity-display">${item.cantidad}</span>
									<button class="quantity-btn" onclick="actualizarCantidad(${item.idItem}, ${item.cantidad + 1})">+</button>
								</div>
								
								<p class="cart-item-subtotal">
									Subtotal: <fmt:formatNumber value="${item.calcularSubtotal()}" type="currency" currencySymbol="$" minFractionDigits="2" maxFractionDigits="2"/>
								</p>
								
								<%-- Botón eliminar según UML: Bolsa.eliminarItem() --%>
								<button class="remove-btn" onclick="eliminarItem(${item.idItem})">
									<i class="fas fa-trash"></i> Eliminar
								</button>
							</div>
						</div>
					</c:forEach>
				</div>
				
				<div class="cart-total">
					<div class="cart-total-row">
						<span class="cart-total-label">Subtotal:</span>
						<span class="cart-total-value">
							<fmt:formatNumber value="${montoTotal}" type="currency" currencySymbol="$" minFractionDigits="2" maxFractionDigits="2"/>
						</span>
					</div>
					<div class="cart-total-row cart-total-final">
						<span class="cart-total-label">Total:</span>
						<span class="cart-total-value">
							<fmt:formatNumber value="${montoTotal}" type="currency" currencySymbol="$" minFractionDigits="2" maxFractionDigits="2"/>
						</span>
					</div>
				</div>
			</c:otherwise>
		</c:choose>
	</div>
</aside>