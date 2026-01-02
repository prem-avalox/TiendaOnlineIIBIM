<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">

<header class="header">
    <div class="filter-bar" onclick="toggleSidebar()">
        <i class="far fa-bars" style="font-size: 24px;"></i>
    </div>
    
    <div class="logo">
        <a href="${pageContext.request.contextPath}/Catalogo">
            <h1>CLOTHING STORE</h1>
        </a>
    </div>
    
    <div class="button-container">
        <!-- Búsqueda -->
        <div id="search" class="search-container">
            <i class="far fa-search"></i>
            <form method="GET" action="${pageContext.request.contextPath}/Catalogo" class="search-form-dropdown">
                <div style="display: flex; flex-direction: column; gap: 10px;">
                    <input type="hidden" name="accion" value="buscar">
                    <input type="text" name="termino" placeholder="Buscar producto..." 
                           style="padding: 10px 15px; border: 1px solid var(--color-acento); width: 250px; font-family: var(--fuente-texto); font-size: 14px; outline: none;">
                    <button type="submit" style="padding: 10px 20px; background-color: var(--color-primario); color: white; border: none; cursor: pointer; font-family: var(--fuente-texto); font-size: 14px; font-weight: 600;">
                        BUSCAR
                    </button>
                </div>
            </form>
        </div>
        
        <!-- Usuario -->
        <div id="user-info">
            <% 
            String nombreUsuario = (String) session.getAttribute("nombreUsuario");
            String rol = (String) session.getAttribute("rol");
            
            if (nombreUsuario != null) {
            %>
                <div class="user-dropdown">
                    <i class="far fa-user"></i>
                    <div class="user-menu">
                        <p class="user-name"><%= nombreUsuario %></p>
                        <% if ("ADMIN".equals(rol)) { %>
                            <a href="${pageContext.request.contextPath}/AdminPrendas">Panel Admin</a>
                        <% } %>
                        <a href="${pageContext.request.contextPath}/Logout">Cerrar Sesión</a>
                    </div>
                </div>
            <% } else { %>
                <a href="${pageContext.request.contextPath}/Registro"><i class="far fa-user"></i></a>
            <% } %>
        </div>
        
        <!-- Bolsa -->
        <div id="shopping-bag" onclick="toggleCartSidebar()">
            <i class="far fa-shopping-bag"></i>
            <%
            @SuppressWarnings("unchecked")
                        java.util.Map<String, modelo.entidades.ItemBolsa> bolsaSession = 
                            (java.util.Map<String, modelo.entidades.ItemBolsa>) session.getAttribute("bolsa");
                        int cantidadItems = (bolsaSession != null) ? bolsaSession.size() : 0;
                        if (cantidadItems > 0) {
            %>
                <span class="cart-badge"><%= cantidadItems %></span>
            <% } %>
        </div>
    </div>
</header>
