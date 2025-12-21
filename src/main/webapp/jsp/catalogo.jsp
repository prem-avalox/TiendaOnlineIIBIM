<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="modelo.Prenda" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Clothing Store - <%= request.getAttribute("titulo") != null ? request.getAttribute("titulo") : "Catálogo" %></title>
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
    
    <!-- Barra de filtros -->
    <nav class="category-nav-bar">
        <form method="GET" action="${pageContext.request.contextPath}/Catalogo" id="filter-form">
            <ul class="cajas-filtro-lista">
                <li>
                    <div onclick="toggleDropdown('talla-dropdown')">
                        TAMAÑO <i class="fas fa-chevron-down"></i>
                    </div>
                    <div id="talla-dropdown" class="filter-dropdown" style="display: none;">
                        <label><input type="radio" name="talla" value=""> Todas</label>
                    </div>
                </li>
                <li>
                    <div onclick="toggleDropdown('color-dropdown')">
                        COLOR <i class="fas fa-chevron-down"></i>
                    </div>
                    <div id="color-dropdown" class="filter-dropdown" style="display: none;">
                        <label><input type="radio" name="color" value=""> Todos</label>
                    </div>
                </li>
                <li>
                    <div onclick="toggleDropdown('corte-dropdown')">
                        CORTE <i class="fas fa-chevron-down"></i>
                    </div>
                    <div id="corte-dropdown" class="filter-dropdown" style="display: none;">
                        <label><input type="radio" name="corte" value=""> Todos</label>
                    </div>
                </li>
                <li>
                    <div>
                        <button type="button" onclick="limpiarFiltros()" style="background: none; border: none; cursor: pointer; font: inherit; color: inherit;">
                            LIMPIAR FILTROS
                        </button>
                    </div>
                </li>
            </ul>
        </form>
    </nav>
    
    <!-- Sección de productos -->
    <section class="product-section">
        <% 
        @SuppressWarnings("unchecked")
        List<Prenda> prendas = (List<Prenda>) request.getAttribute("prendas");
        
        if (prendas != null && !prendas.isEmpty()) {
            for (Prenda prenda : prendas) {
        %>
            <div class="tarjeta-producto">
                <a href="${pageContext.request.contextPath}/DetallePrenda?id=<%= prenda.getIdPrenda() %>">
                    <div class="imagen-producto">
                        <% if (prenda.getImagenUrl() != null && !prenda.getImagenUrl().isEmpty()) { %>
                            <img src="${pageContext.request.contextPath}/<%= prenda.getImagenUrl() %>" 
                                 alt="<%= prenda.getNombre() %>">
                        <% } else { %>
                            <img src="${pageContext.request.contextPath}/img/placeholder.jpg" 
                                 alt="Sin imagen">
                        <% } %>
                    </div>
                    <div class="info-producto">
                        <h3><%= prenda.getNombre() %></h3>
                        <p>$<%= prenda.getPrecio() %></p>
                    </div>
                </a>
            </div>
        <%
            }
        } else {
        %>
            <div style="grid-column: 1 / -1; text-align: center; padding: 60px 20px;">
                <p style="font-family: var(--fuente-texto); font-size: 18px; color: var(--texto-inactivo);">
                    No hay prendas disponibles
                </p>
                <a href="${pageContext.request.contextPath}/Catalogo" 
                   class="btn btn-primary" 
                   style="margin-top: 20px; display: inline-block; text-decoration: none;">
                    Ver todas las prendas
                </a>
            </div>
        <%
        }
        %>
    </section>
    
    <!-- Footer -->
    <%@ include file="/jsp/includes/footer.jsp" %>
</body>
</html>
