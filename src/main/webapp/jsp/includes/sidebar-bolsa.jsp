<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="modelo.ItemBolsa" %>

<!-- Cart Sidebar - Estilo Clothing Store -->
<div class="cart-sidebar" id="cartSidebar">
    <div class="sidebar-header">
        <h2>MI BOLSA</h2>
        <button class="close-btn" onclick="toggleCartSidebar()">
            <i class="far fa-times"></i>
        </button>
    </div>
    
    <% 
    @SuppressWarnings("unchecked")
    Map<String, ItemBolsa> bolsa = (Map<String, ItemBolsa>) session.getAttribute("bolsa");
    
    if (bolsa != null && !bolsa.isEmpty()) {
        Collection<ItemBolsa> items = bolsa.values();
        BigDecimal subtotal = BigDecimal.ZERO;
    %>
        <div class="cart-items">
            <% for (ItemBolsa item : items) {
                subtotal = subtotal.add(item.getSubtotal());
            %>
                <div class="cart-item">
                    <% if (item.getImagenUrl() != null && !item.getImagenUrl().isEmpty()) { %>
                        <img src="${pageContext.request.contextPath}/<%= item.getImagenUrl() %>" 
                             alt="<%= item.getNombrePrenda() %>"
                             class="cart-item-image">
                    <% } %>
                    
                    <div class="cart-item-details">
                        <h4><%= item.getNombrePrenda() %></h4>
                        <p class="cart-item-size">Talla: <%= item.getTalla() %></p>
                        <p class="cart-item-price">$<%= item.getPrecioUnitario() %></p>
                        
                        <div class="cart-item-quantity">
                            <a href="${pageContext.request.contextPath}/Bolsa?accion=disminuir&key=<%= item.getKey() %>">
                                <i class="fas fa-minus"></i>
                            </a>
                            <span><%= item.getCantidad() %></span>
                            <a href="${pageContext.request.contextPath}/Bolsa?accion=aumentar&key=<%= item.getKey() %>">
                                <i class="fas fa-plus"></i>
                            </a>
                        </div>
                    </div>
                    
                    <a href="${pageContext.request.contextPath}/Bolsa?accion=eliminar&key=<%= item.getKey() %>" 
                       class="cart-item-remove" title="Eliminar">
                        <i class="far fa-trash-alt"></i>
                    </a>
                </div>
            <% } %>
        </div>
        
        <div class="cart-footer">
            <div class="cart-total">
                <span>SUBTOTAL:</span>
                <span>$<%= subtotal %></span>
            </div>
            
            <button class="cart-checkout-btn" onclick="alert('Funcionalidad de pago en desarrollo')">
                PROCEDER AL PAGO
            </button>
            
            <a href="${pageContext.request.contextPath}/Bolsa?accion=vaciar" 
               class="btn btn-secondary btn-block"
               style="margin-top: 10px; text-decoration: none; display: block; text-align: center; padding: 10px;"
               onclick="return confirm('¿Está seguro que desea vaciar la bolsa?')">
                Vaciar Bolsa
            </a>
        </div>
    <% } else { %>
        <div class="cart-empty">
            <p>Aún no ha agregado prendas a la bolsa</p>
            <a href="${pageContext.request.contextPath}/Catalogo" 
               class="btn btn-primary"
               style="margin-top: 20px; display: inline-block; text-decoration: none;">
                Ver Catálogo
            </a>
        </div>
    <% } %>
</div>

<!-- Overlay para el carrito -->
<div id="cartOverlay" class="overlay" onclick="toggleCartSidebar()"></div>
