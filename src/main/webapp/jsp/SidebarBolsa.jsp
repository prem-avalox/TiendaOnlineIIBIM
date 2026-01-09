<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
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
		<!-- Contenido se cargará dinámicamente -->
		<div class="empty-cart">
			<i class="fas fa-shopping-bag empty-icon"></i>
			<p class="empty-message">Cargando...</p>
		</div>
	</div>
</aside>
