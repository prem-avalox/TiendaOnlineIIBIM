<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Clothing Store - Catálogo</title>
<script src="../js/catalogo.js"></script>
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

    <!-- Sección de productos -->
    <section>

        <!-- Tarjeta de producto -->
        <div>
            <img src="" alt="Camisa negra" width="200">
            <h3>Camisa Negra</h3>
            <p>$25.99</p>
        </div>

        <div>
            <img src="" alt="Pantalón azul" width="200">
            <h3>Pantalón Azul</h3>
            <p>$39.99</p>
        </div>

        <div>
            <img src="" alt="Chaqueta gris" width="200">
            <h3>Chaqueta Gris</h3>
            <p>$59.99</p>
        </div>

    </section>
    
    
    <!-- Sidebar bolsa derecha-->
    <%@ include file="sidebar_bolsa.jsp" %>
    <!-- Footer -->
    <footer>
        <p>&copy; 2026 Clothing Store</p>
    </footer>
    
    
</body>
</html>