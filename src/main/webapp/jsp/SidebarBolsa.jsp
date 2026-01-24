<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>

<!-- SIDEBAR DE LA BOLSA -->
<aside class="cart-sidebar" id="cartSidebar">

    <!-- HEADER -->
    <div class="cart-sidebar-header">
        <h3>MI BOLSA</h3>
        <label for="toggle-cart" class="close-cart-btn">
            <i class="fas fa-times"></i>
        </label>
    </div>

    <!-- CONTENIDO -->
    <div class="cart-sidebar-content">

        <!-- MENSAJE DE ERROR -->
        <c:if test="${not empty mensajeError}">
            <p class="mensaje-error">${mensajeError}</p>
        </c:if>

        <!-- BOLSA VACÍA -->
        <c:if test="${empty bolsa or empty bolsa.items}">
            <div class="empty-cart">
                <i class="fas fa-shopping-bag empty-icon"></i>
                <p class="empty-message">Tu bolsa está vacía</p>
                <p class="empty-submessage">Agrega prendas para comenzar</p>
                <label for="toggle-cart" class="continue-shopping-btn">
                    Seguir comprando
                </label>
            </div>
        </c:if>

        <!-- BOLSA CON ITEMS -->
        <c:if test="${not empty bolsa and not empty bolsa.items}">

            <div class="cart-items-list">
                <c:forEach var="item" items="${bolsa.items}">

                    <div class="cart-item">

                        <!-- IMAGEN -->
                        <div class="cart-item-image">
                            <img src="${item.prenda.imagen}"
                                 alt="${item.prenda.nombrePrenda}">
                        </div>

                        <!-- INFO -->
                        <div class="cart-item-info">

                            <h4 class="cart-item-name">
                                ${item.prenda.nombrePrenda}
                            </h4>

                            <p class="cart-item-size">
                                Talla: ${item.tallaSeleccionada}
                            </p>

                            <p class="cart-item-price">
                                <fmt:formatNumber value="${item.prenda.precio}"
                                    type="currency" currencySymbol="$"/>
                            </p>

                            <p class="cart-item-subtotal">
                                Subtotal:
                                <fmt:formatNumber
                                    value="${item.prenda.precio * item.cantidad}"
                                    type="currency" currencySymbol="$"/>
                            </p>

                            <!-- ACCIONES -->
                            <div class="cart-item-actions">

                                <!-- AJUSTAR CANTIDAD -->
                                <form action="VerBolsaController" method="post">
                                    <input type="hidden" name="ruta" value="ajustarCantidadItem">
                                    <input type="hidden" name="idItem" value="${item.idItem}">

                                    <input type="number"
                                           name="nuevaCantidad"
                                           value="${item.cantidad}"
                                           min="1"
                                           class="quantity-display">

                                    <button type="submit" class="quantity-btn">✔</button>
                                </form>

                                <!-- ELIMINAR -->
                                <form action="VerBolsaController" method="post">
                                    <input type="hidden" name="ruta" value="eliminarItem">
                                    <input type="hidden" name="idItem" value="${item.idItem}">

                                    <button type="submit" class="remove-btn">
                                        <i class="fas fa-trash"></i> Quitar
                                    </button>
                                </form>

                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>

            <!-- TOTAL -->
            <div class="cart-total">
                <p class="total-label">Total</p>
                <p class="total-amount">
                    <fmt:formatNumber value="${bolsa.precioTotal}"
                        type="currency" currencySymbol="$"/>
                </p>
            </div>

            <!-- ACCIONES -->
            <div class="cart-actions">
                <a href="CheckoutController?ruta=ingresar" class="checkout-btn">
                    Finalizar compra
                </a>
            </div>

        </c:if>

    </div>
</aside>
