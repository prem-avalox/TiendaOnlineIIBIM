<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Clothing Store - Catálogo</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/estilos.css">
<script src="${pageContext.request.contextPath}/js/catalogo.js"></script>
</head>

<body>

  <!-- Header -->
    <header>
        <h1>Clothing Store</h1>
    </header>
	
    <button type="button" onclick="toggleCategoria()">☰ Categorías</button>
    <button type="button" onclick="toggleBolsa()">🛍 Bolsa</button>

    <!-- Sidebar de categorías izquierda-->
	<%@ include file="sidebar_categoria.jsp" %>
    
    

    <!-- Barra de filtros (solo presentación) -->
    <nav>
        <form>
            <ul>
                <li>
                    <strong>TAMAÑO</strong><br>
                    <label><input type="radio" name="talla"> S</label><br>
                    <label><input type="radio" name="talla"> M</label><br>
                    <label><input type="radio" name="talla"> L</label>
                </li>

                <li>
                    <strong>COLOR</strong><br>
                    <label><input type="radio" name="color"> Negro</label><br>
                    <label><input type="radio" name="color"> Blanco</label><br>
                    <label><input type="radio" name="color"> Azul</label>
                </li>

                <li>
                    <strong>CORTE</strong><br>
                    <label><input type="radio" name="corte"> Slim</label><br>
                    <label><input type="radio" name="corte"> Regular</label>
                </li>

                <li>
                    <button type="button">Limpiar filtros</button>
                </li>
            </ul>
        </form>
    </nav>

    <!-- Sección de prendas -->
    <section class="contenedor-prendas">
        <c:forEach var="prenda" items="${prendas}">
            <div class="tarjeta-prenda">
                <img src="${pageContext.request.contextPath}/${prenda.imagen}" alt="${prenda.nombrePrenda}">
                <h3>${prenda.nombrePrenda}</h3>
                <p class="precio">$${prenda.precio}</p>
                <a href="VerCatalogoController?ruta=detalle&id=${prenda.idPrenda}" class="btn-detalle">Ver detalle</a>
            </div>
        </c:forEach>
    </section>
    
    
    <!-- Sidebar bolsa derecha-->
    <%@ include file="sidebar_bolsa.jsp" %>
    <!-- Footer -->
    <footer>
        <p>&copy; 2026 Clothing Store</p>
    </footer>
    
    
</body>
</html>