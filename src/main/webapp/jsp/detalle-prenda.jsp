<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="modelo.entidades.Prenda" %>
<%@ page import="modelo.entidades.Talla" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Clothing Store - Detalle Prenda</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Playfair+Display:wght@100;200;300;400;500;600;700;800;900&family=Roboto+Condensed:wght@100;200;300;400;500;600;700;800;900&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
</head>
<body class="general">
    <!-- Header -->
    <%@ include file="/jsp/includes/header.jsp" %>
    
    <!-- Sidebar de categorías -->
    <%@ include file="/jsp/includes/sidebar.jsp" %>
    
    <!-- Sidebar de bolsa -->
    <%@ include file="/jsp/includes/sidebar-bolsa.jsp" %>
    
    <!-- Botón de regreso -->
    <a href="${pageContext.request.contextPath}/Catalogo" class="back-button">
        <i class="fas fa-arrow-left"></i>
        <span>Volver</span>
    </a>
    
    <%
    Prenda prenda = (Prenda) request.getAttribute("prenda");
    @SuppressWarnings("unchecked")
    List<Talla> tallas = (List<Talla>) request.getAttribute("tallas");
    
    if (prenda != null) {
    %>
    
    <!-- Sección de detalle del producto -->
    <section class="product-detail-section">
        <!-- Imagen del producto -->
        <div class="product-review-section">
            <% if (prenda.getImagenUrl() != null && !prenda.getImagenUrl().isEmpty()) { %>
                <img src="${pageContext.request.contextPath}/<%= prenda.getImagenUrl() %>" 
                     alt="<%= prenda.getNombre() %>">
            <% } else { %>
                <img src="${pageContext.request.contextPath}/img/placeholder.jpg" alt="Sin imagen">
            <% } %>
        </div>
        
        <!-- Detalles del producto -->
        <div class="detail-section">
            <h2><%= prenda.getNombre() %></h2>
            <p class="precio-detalle-producto">$<%= prenda.getPrecio() %></p>
            
            <div class="detalle-producto">
                <% if (prenda.getColor() != null && !prenda.getColor().isEmpty()) { %>
                <p class="texto-producto-detalle"><strong>Color:</strong> <%= prenda.getColor() %></p>
                <% } %>
                
                <% if (prenda.getTipoAjuste() != null && !prenda.getTipoAjuste().isEmpty()) { %>
                <p class="texto-producto-detalle"><strong>Tipo de ajuste:</strong> <%= prenda.getTipoAjuste() %></p>
                <% } %>
            </div>
            
            <!-- Selector de tallas -->
            <form action="${pageContext.request.contextPath}/Bolsa" method="post" id="formAgregarBolsa">
                <input type="hidden" name="accion" value="agregar">
                <input type="hidden" name="idPrenda" value="<%= prenda.getIdPrenda() %>">
                
                <div class="seleccion-talla-producto">
                    <p class="texto-producto-detalle"><strong>Selecciona tu talla:</strong></p>
                    <ul class="cajas-talla-lista">
                        <% 
                        if (tallas != null && !tallas.isEmpty()) {
                            for (Talla talla : tallas) {
                                if (talla.getStock() > 0) {
                        %>
                            <li>
                                <div class="talla-item" onclick="selectTalla(this, '<%= talla.getTalla() %>')">
                                    <%= talla.getTalla() %>
                                </div>
                            </li>
                        <%
                                }
                            }
                        }
                        %>
                    </ul>
                    <input type="hidden" name="talla" id="tallaSeleccionada" value="">
                </div>
                
                <button type="submit" class="agregar-bolsa" onclick="return validarTalla()">
                    AGREGAR A LA BOLSA
                </button>
            </form>
            
            <!-- Descripción -->
            <div class="descripcion">
                <p><%= prenda.getDescripcion() != null ? prenda.getDescripcion() : "Sin descripción disponible." %></p>
            </div>
            
            <!-- Mostrar mensajes -->
            <% if (request.getAttribute("error") != null) { %>
                <div class="alert alert-error" style="margin-top: 20px;">
                    <%= request.getAttribute("error") %>
                </div>
            <% } %>
            
            <% if (session.getAttribute("mensaje") != null) { %>
                <div class="cart-message" id="cartMessage">
                    <%= session.getAttribute("mensaje") %>
                </div>
                <% session.removeAttribute("mensaje"); %>
                <script>
                    setTimeout(function() {
                        var msg = document.getElementById('cartMessage');
                        if (msg) msg.style.display = 'none';
                    }, 3000);
                </script>
            <% } %>
        </div>
    </section>
    
    <% } else { %>
        <div style="text-align: center; padding: 100px 20px;">
            <h2 style="font-family: var(--fuente-texto); color: var(--color-primario);">Prenda no encontrada</h2>
            <a href="${pageContext.request.contextPath}/Catalogo" 
               class="btn btn-primary" 
               style="margin-top: 20px; display: inline-block; text-decoration: none;">
                Volver al Catálogo
            </a>
        </div>
    <% } %>
    
    <!-- Footer -->
    <%@ include file="/jsp/includes/footer.jsp" %>
    
    <script>
    let tallaSeleccionadaActual = null;
    
    function selectTalla(elemento, talla) {
        // Remover selección anterior
        document.querySelectorAll('.talla-item').forEach(item => {
            item.classList.remove('talla-seleccionada');
        });
        
        // Agregar selección actual
        elemento.classList.add('talla-seleccionada');
        
        // Guardar talla
        tallaSeleccionadaActual = talla;
        document.getElementById('tallaSeleccionada').value = talla;
    }
    
    function validarTalla() {
        if (!tallaSeleccionadaActual) {
            alert('Por favor, selecciona una talla antes de agregar a la bolsa.');
            return false;
        }
        return true;
    }
    </script>
</body>
</html>
