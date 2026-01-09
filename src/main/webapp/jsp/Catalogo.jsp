<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Clothing Store - Catálogo</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/estilos.css">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
</head>

<body class="general">
    <input type="checkbox" id="toggle-sidebar" ${menuAbierto} hidden>
    <input type="checkbox" id="toggle-cart" hidden>

    <div class="header">
    	<!-- boton para abrir el sidebar categorias -->
        <a href="VerCatalogoController?ruta=categorias" class="filter-bar-btn">
        <i class="fas fa-bars"></i>
    </a>

        <div class="logo">
            <h1>CLOTHING STORE</h1>
        </div>

        <div class="button-container">
        	<!-- boton para buscar una prenda por el nombre -->
            <div id="search" class="search-container">
                <i class="fas fa-search"></i>
                <div class="search-form-wrapper">
                    <form action="VerCatalogoController" method="get">
                        <input type="hidden" name="ruta" value="buscar">
                        <div class="search-inputs">
                            <input type="text" name="nombre" placeholder="Buscar producto..." class="search-input">
                            <button type="submit" class="search-btn">BUSCAR</button>
                        </div>
                    </form>
                </div>
            </div>
            
            <div id="user-info" class="icon-link">
                <a href="#"><i class="far fa-user"></i></a>
            </div>
            
            <label for="toggle-cart" id="shopping-bag" class="icon-link" onclick="cargarBolsa()">
                <i class="fas fa-briefcase"></i>
            </label>
        </div>
    </div>

	<!-- seccion del sidebar de categorias -->
    <%@ include file="SidebarCategorias.jsp" %>
    <label id="overlay" class="overlay" for="toggle-sidebar"></label>

	<!-- seccion para aplicar filtros -->
    <nav class="category-nav-bar">
        <form action="VerCatalogoController" method="get">
            <input type="hidden" name="ruta" value="filtros">
            <ul class="cajas-filtro-lista">

                <li class="filtro-item">
                    <input type="checkbox" id="filtro-tamano" class="filtro-toggle" hidden>
                    <label for="filtro-tamano" class="filtro-label">
                        TALLA <i class="fas fa-chevron-down"></i>
                    </label>
                    <div class="filter-dropdown">
                        <label><input type="radio" name="talla" value="" checked> Todas</label>
                        <label><input type="radio" name="talla" value="XS"> XS</label>
                        <label><input type="radio" name="talla" value="S"> S</label>
                        <label><input type="radio" name="talla" value="M"> M</label>
                        <label><input type="radio" name="talla" value="L"> L</label>
                        <label><input type="radio" name="talla" value="XL"> XL</label>
                    </div>
                </li>

                <li class="filtro-item">
                    <input type="checkbox" id="filtro-color" class="filtro-toggle" hidden>
                    <label for="filtro-color" class="filtro-label">
                        COLOR <i class="fas fa-chevron-down"></i>
                    </label>
                    <div class="filter-dropdown">
                        <label><input type="radio" name="color" value="" checked> Todos</label>
                        <label><input type="radio" name="color" value="Negro"> Negro</label>
                        <label><input type="radio" name="color" value="Celeste"> Celeste</label>
                        <label><input type="radio" name="color" value="Blanco"> Blanco</label>
                        <label><input type="radio" name="color" value="Gris"> Gris</label>
                        <label><input type="radio" name="color" value="Crudo"> Crudo</label>
                    </div>
                </li>

                <li class="filtro-item">
                    <input type="checkbox" id="filtro-corte" class="filtro-toggle" hidden>
                    <label for="filtro-corte" class="filtro-label">
                        CORTE <i class="fas fa-chevron-down"></i>
                    </label>
                    <div class="filter-dropdown">
                        <label><input type="radio" name="corte" value="" checked> Todos</label>
                        <label><input type="radio" name="corte" value="Regular Fit"> Regular Fit</label>
                        <label><input type="radio" name="corte" value="Slim Fit"> Slim Fit</label>
                        <label><input type="radio" name="corte" value="Loose Fit"> Loose Fit</label>
                    </div>
                </li>
                
                <li class="filtro-item">
                    <button type="submit" class="apply-filters-btn">APLICAR</button>
                </li>
            </ul>
        </form>
    </nav>

    <section class="product-section">
        <c:choose>
            <c:when test="${not empty mensajeError}">
                <div class="mensaje-error">
                    <p>⚠️ ${mensajeError}</p>
                    <button onclick="location.href='VerCatalogoController?ruta=listar'" class="retry-btn">
                        Reintentar
                    </button>
                </div>
            </c:when>

            <c:when test="${not empty prendas}">
                <c:forEach var="prenda" items="${prendas}">
                    <div class="tarjeta-producto">
                        <a href="VerCatalogoController?ruta=detalle&id=${prenda.idPrenda}" class="product-link">
                            <div class="product-image-container">
                                <img class="product-image" src="${pageContext.request.contextPath}/${prenda.imagen}" alt="${prenda.nombrePrenda}">
                            </div>
                            <div class="product-info">
                                <p class="product-name">${prenda.nombrePrenda}</p>
                                <p class="product-price">$${prenda.precio}</p>
                            </div>
                        </a>
                    </div>
                </c:forEach>
            </c:when>
        </c:choose>
    </section>

    <%@ include file="SidebarBolsa.jsp" %>
    <label id="cart-overlay" class="overlay" for="toggle-cart"></label>

    <footer class="footer">
        <p>&copy; 2026 Clothing Store. Imágenes cortesía de H&M.</p>
    </footer>
    
    <script>
    // Función para cargar el contenido de la bolsa
    function cargarBolsa() {
        fetch('${pageContext.request.contextPath}/VerBolsaController?action=abrirBolsa')
            .then(response => response.text())
            .then(html => {
                // Extraer solo el contenido del cart-content-data
                const parser = new DOMParser();
                const doc = parser.parseFromString(html, 'text/html');
                const content = doc.querySelector('.cart-content-data');
                
                if (content) {
                    document.getElementById('cartContent').innerHTML = content.innerHTML;
                } else {
                    // Si no hay contenido específico, usar todo el HTML
                    document.getElementById('cartContent').innerHTML = html;
                }
            })
            .catch(error => {
                console.error('Error al cargar la bolsa:', error);
                document.getElementById('cartContent').innerHTML = `
                    <div class="empty-cart">
                        <i class="fas fa-exclamation-triangle empty-icon" style="color: #dc2626;"></i>
                        <p class="empty-message">Error al cargar la bolsa</p>
                        <button class="continue-shopping-btn" onclick="cargarBolsa()">Reintentar</button>
                    </div>
                `;
            });
    }
    
    // Función para cambiar la cantidad de un item
    function cambiarCantidad(idItem, cambio) {
        // TODO: Implementar endpoint para actualizar cantidad
        console.log("Cambiar cantidad del item " + idItem + " en " + cambio);
    }
    
    // Función para eliminar un item
    function eliminarItem(idItem) {
        if (confirm('¿Estás seguro de eliminar este artículo?')) {
            // TODO: Implementar endpoint para eliminar item
            console.log("Eliminar item " + idItem);
        }
    }
    </script>
</body>
</html>