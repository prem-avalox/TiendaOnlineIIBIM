<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${prenda.nombrePrenda} - Detalle</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/estilos.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
</head>
<body class="general">
    <input type="checkbox" id="toggle-cart" hidden>
    
    <div class="header">
        <a href="VerCatalogoController?ruta=listar" class="filter-bar-btn"><i class="fas fa-arrow-left"></i> Volver</a>
        <div class="logo"><h1>DETALLE</h1></div>
        
        <div class="button-container">
            <label for="toggle-cart" id="shopping-bag" class="icon-link" onclick="cargarBolsa()">
                <i class="fas fa-briefcase"></i>
            </label>
        </div>
    </div>

    <div class="prenda-container-detalle">
        <div class="detalle-izquierda">
            <img src="${pageContext.request.contextPath}/${prenda.imagen}" alt="${prenda.nombrePrenda}" class="img-detalle">
        </div>

        <div class="detalle-derecha">
            <h2 class="product-name-detalle">${prenda.nombrePrenda}</h2>
            <p class="precio-detalle">${prenda.precio}</p>
            
            <div class="talla-section">
                <p class="label-detalle">Talla</p>
                <ul class="cajas-talla-lista">
                    <li>
                        <input type="radio" name="talla" id="talla-xs" class="talla-radio" hidden>
                        <label for="talla-xs" class="talla-item">XS</label>
                    </li>
                    <li>
                        <input type="radio" name="talla" id="talla-s" class="talla-radio" hidden checked>
                        <label for="talla-s" class="talla-item">S</label>
                    </li>
                    <li>
                        <input type="radio" name="talla" id="talla-m" class="talla-radio" hidden>
                        <label for="talla-m" class="talla-item">M</label>
                    </li>
                    <li>
                        <input type="radio" name="talla" id="talla-l" class="talla-radio" hidden>
                        <label for="talla-l" class="talla-item">L</label>
                    </li>
                    <li>
                        <input type="radio" name="talla" id="talla-xl" class="talla-radio" hidden>
                        <label for="talla-xl" class="talla-item">XL</label>
                    </li>
                </ul>
                <div class="guia-tallas">
                    <i class="fas fa-ruler-combined"></i> Guía De Tallas
                </div>
            </div>

            <button type="button" class="btn-agregar-bolsa">
                Agregar a la bolsa
            </button>
            
            <div class="descripcion-detalle">
                <p>${prenda.descripcion}</p>
            </div>

            <div class="info-adicional">
                <p><strong>Categoría:</strong> ${prenda.categoria.nombreCategoria}</p>
                <p><strong>Color:</strong> ${prenda.color}</p>
                <p><strong>Corte:</strong> ${prenda.corte}</p>
            </div>
        </div>
    </div>
    
    <%@ include file="sidebar_bolsa.jsp" %>
    <label id="cart-overlay" class="overlay" for="toggle-cart"></label>
    
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