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
        <a href="VerCatalogoController?ruta=iniciar" class="filter-bar-btn"><i class="fas fa-arrow-left"></i> Volver</a>
        <div class="logo"><h1>DETALLE</h1></div>
        
        <div class="button-container">
            <label for="toggle-cart" id="shopping-bag" class="icon-link" onclick="cargarBolsa()">
                <i class="fas fa-briefcase"></i>
            </label>
        </div>
    </div>

    <div class="prenda-container-detalle">
        <div class="detalle-izquierda">
            <img src="${pageContext.request.contextPath}/img/${prenda.imagen}" 
                 alt="${prenda.nombrePrenda}" 
                 class="img-detalle"
                 onerror="this.src='${pageContext.request.contextPath}/img/placeholder.jpg'">
        </div>

        <div class="detalle-derecha">
            <h2 class="product-name-detalle">${prenda.nombrePrenda}</h2>
            <p class="precio-detalle">$${prenda.precio}</p>
            
            <div class="talla-section">
                <p class="label-detalle">Talla</p>
                <ul class="cajas-talla-lista">
                    <!-- Mostrar solo tallas con stock disponible -->
                    <c:set var="hayStockDisponible" value="false" />
                    <c:forEach var="stock" items="${prenda.stockTallas}">
                        <c:if test="${stock.cantidad > 0}">
                            <c:set var="hayStockDisponible" value="true" />
                            <li>
                                <input type="radio" name="talla" id="talla-${stock.talla.talla}" 
                                       class="talla-radio" value="${stock.talla.ordinal()}" hidden>
                                <label for="talla-${stock.talla.talla}" class="talla-item">
                                    ${stock.talla.talla}
                                </label>
                            </li>
                        </c:if>
                    </c:forEach>
                </ul>
                
                <!-- Mostrar mensaje si no hay tallas disponibles -->
                <c:if test="${!hayStockDisponible}">
                    <p style="color: #dc2626; margin-top: 10px;">
                        <i class="fas fa-exclamation-circle"></i> Sin stock disponible
                    </p>
                </c:if>
                
                <div class="guia-tallas">
                    <i class="fas fa-ruler-combined"></i> Guía De Tallas
                </div>
            </div>

            <button type="button" class="btn-agregar-bolsa" onclick="agregarABolsa()">
                Agregar a la bolsa
            </button>
            
            <div class="descripcion-detalle">
                <p>${prenda.descripcion}</p>
            </div>

            <div class="info-adicional">
                <p><strong>Categoría:</strong> ${prenda.categoria}</p>
                <p><strong>Color:</strong> ${prenda.color}</p>
                <p><strong>Corte:</strong> ${prenda.corte}</p>
            </div>
        </div>
    </div>
    
    <%@ include file="SidebarBolsa.jsp" %>
    <label id="cart-overlay" class="overlay" for="toggle-cart"></label>
    
    <script>
    // Función para cargar el contenido de la bolsa
    function cargarBolsa() {
        fetch('${pageContext.request.contextPath}/VerBolsaController?action=abrirBolsa')
            .then(response => response.text())
            .then(html => {
                // Extraer el contenido del sidebar
                const parser = new DOMParser();
                const doc = parser.parseFromString(html, 'text/html');
                const content = doc.querySelector('.cart-sidebar-content');
                
                if (content) {
                    document.getElementById('cartContent').innerHTML = content.innerHTML;
                } else {
                    // Si no hay contenido específico, usar todo el HTML
                    document.getElementById('cartContent').innerHTML = html;
                }
            })
            .catch(error => {
                console.error('Error al cargar la bolsa:', error);
                const errorHtml = '<div class="empty-cart">' +
                    '<i class="fas fa-exclamation-triangle empty-icon" style="color: #dc2626;"></i>' +
                    '<p class="empty-message">Error al cargar la bolsa</p>' +
                    '<button class="continue-shopping-btn" onclick="cargarBolsa()">Reintentar</button>' +
                    '</div>';
                document.getElementById('cartContent').innerHTML = errorHtml;
            });
    }
    
    /**
     * Función para agregar prenda a la bolsa
     * Implementa CU10 - Agregar prenda a la bolsa
     */
    function agregarABolsa() {
        // 1. Obtener la talla seleccionada
        const tallaSeleccionada = document.querySelector('input[name="talla"]:checked');
        
        // FLUJO ALTERNO 2.1: Validar que una talla esté seleccionada
        if (!tallaSeleccionada) {
            mostrarMensaje('Por favor selecciona una talla', 'error');
            return;
        }
        
        const idPrenda = ${prenda.idPrenda};
        const talla = tallaSeleccionada.value;
        const cantidad = 1; // Por defecto agregar 1 unidad
        
        console.log('📦 Agregando a bolsa:', { idPrenda, talla, cantidad });
        
        // Mostrar indicador de carga
        const boton = document.querySelector('.btn-agregar-bolsa');
        const textoOriginal = boton.textContent;
        boton.disabled = true;
        boton.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Agregando...';
        
        // 2. Enviar petición al servidor
        fetch('${pageContext.request.contextPath}/AgregarPrendaBolsaController', {
             method: 'POST',
             headers: {
                 'Content-Type': 'application/x-www-form-urlencoded',
             },
            body: 'idPrenda=' + idPrenda + '&talla=' + talla + '&cantidad=' + cantidad
         })
        .then(response => {
            console.log('📡 Respuesta recibida, status:', response.status);
            if (!response.ok) {
                throw new Error('Error HTTP: ' + response.status);
            }
            return response.json();
        })
        .then(data => {
            console.log('✅ Datos recibidos:', data);
            // Restaurar botón
            boton.disabled = false;
            boton.textContent = textoOriginal;
            
            if (data.success) {
                // FLUJO BÁSICO PASO 4: Mostrar mensaje de éxito
                mostrarMensaje(data.message, 'success');
                
                // Opcional: Actualizar contador de items en el carrito
                if (data.cantidadItems) {
                    actualizarContadorCarrito(data.cantidadItems);
                }
                
                // ✅ IMPORTANTE: Recargar el contenido de la bolsa para reflejar los cambios
                cargarBolsa();
                
                // ✅ Abrir automáticamente el sidebar para mostrar la bolsa actualizada
                document.getElementById('toggle-cart').checked = true;
                
                // Opcional: Limpiar selección de talla
                if (tallaSeleccionada) {
                    tallaSeleccionada.checked = false;
                }
            } else {
                // FLUJOS ALTERNOS: Mostrar mensaje de error
                console.warn('⚠️ Error del servidor:', data.message);
                mostrarMensaje(data.message, 'error');
            }
        })
        .catch(error => {
            // Error de conexión
            console.error('❌ Error en fetch:', error);
            boton.disabled = false;
            boton.textContent = textoOriginal;
            console.error('Error:', error);
            mostrarMensaje('Error al agregar la prenda. Por favor intenta de nuevo.', 'error');
        });
    }
    
    /**
     * Muestra un mensaje flotante al usuario
     * @param mensaje Texto del mensaje
     * @param tipo 'success' o 'error'
     */
    function mostrarMensaje(mensaje, tipo) {
        // Crear elemento del mensaje
        const mensajeDiv = document.createElement('div');
        mensajeDiv.className = 'mensaje-flotante ' + tipo;
        
        // Determinar el icono según el tipo
        const icono = tipo === 'success' ? 'check-circle' : 'exclamation-circle';
        
        mensajeDiv.innerHTML = '<i class="fas fa-' + icono + '"></i>' +
                               '<span>' + mensaje + '</span>';
        
        // Agregar al body
        document.body.appendChild(mensajeDiv);
        
        // Animación de entrada
        setTimeout(() => {
            mensajeDiv.classList.add('show');
        }, 10);
        
        // Remover después de 3 segundos
        setTimeout(() => {
            mensajeDiv.classList.remove('show');
            setTimeout(() => {
                document.body.removeChild(mensajeDiv);
            }, 300);
        }, 3000);
    }
    
    /**
     * Actualiza el contador de items en el carrito (si existe)
     */
    function actualizarContadorCarrito(cantidad) {
        const contador = document.querySelector('.cart-count');
        if (contador) {
            contador.textContent = cantidad;
            contador.style.display = cantidad > 0 ? 'block' : 'none';
        }
    }
    
    /**
     * Función para actualizar la cantidad de un item en la bolsa
     * Según UML: Bolsa.actualizarCantidad(idItem, cantidad)
     */
    function actualizarCantidad(idItem, nuevaCantidad) {
        console.log('🔄 Actualizando cantidad del item ' + idItem + ' a ' + nuevaCantidad);
        
        // Validar que la cantidad sea válida
        if (nuevaCantidad < 0) {
            console.warn('Cantidad inválida: ' + nuevaCantidad);
            return;
        }
        
        // Mostrar indicador de carga
        const itemElement = document.querySelector('[data-item-id="' + idItem + '"]');
        if (itemElement) {
            itemElement.style.opacity = '0.5';
        }
        
        // Enviar petición al servidor
        fetch('${pageContext.request.contextPath}/VerBolsaController?action=actualizarCantidad&idItem=' + idItem + '&cantidad=' + nuevaCantidad, {
            method: 'GET'
        })
        .then(response => response.text())
        .then(html => {
            console.log('✅ Cantidad actualizada');
            // Recargar el contenido de la bolsa
            cargarBolsa();
        })
        .catch(error => {
            console.error('❌ Error al actualizar cantidad:', error);
            // Restaurar opacidad
            if (itemElement) {
                itemElement.style.opacity = '1';
            }
            alert('Error al actualizar la cantidad. Por favor intenta de nuevo.');
        });
    }
    
    /**
     * Función para eliminar un item de la bolsa
     * Según UML: Bolsa.eliminarItem(idItem)
     */
    function eliminarItem(idItem) {
        if (confirm('¿Estás seguro de eliminar este artículo?')) {
            console.log('🗑️ Eliminando item ' + idItem);
            
            // Mostrar indicador de carga
            const itemElement = document.querySelector('[data-item-id="' + idItem + '"]');
            if (itemElement) {
                itemElement.style.opacity = '0.5';
            }
            
            // Enviar petición al servidor
            fetch('${pageContext.request.contextPath}/VerBolsaController?action=eliminarItem&idItem=' + idItem, {
                method: 'GET'
            })
            .then(response => response.text())
            .then(html => {
                console.log('✅ Item eliminado');
                // Recargar el contenido de la bolsa
                cargarBolsa();
            })
            .catch(error => {
                console.error('❌ Error al eliminar item:', error);
                // Restaurar opacidad
                if (itemElement) {
                    itemElement.style.opacity = '1';
                }
                alert('Error al eliminar el artículo. Por favor intenta de nuevo.');
            });
        }
    }
    </script>
    
    <style>
    /* Estilos para el mensaje flotante */
    .mensaje-flotante {
        position: fixed;
        top: 20px;
        right: 20px;
        background: white;
        padding: 15px 20px;
        border-radius: 8px;
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
        display: flex;
        align-items: center;
        gap: 10px;
        z-index: 10000;
        opacity: 0;
        transform: translateX(400px);
        transition: all 0.3s ease;
    }
    
    .mensaje-flotante.show {
        opacity: 1;
        transform: translateX(0);
    }
    
    .mensaje-flotante.success {
        border-left: 4px solid #10b981;
    }
    
    .mensaje-flotante.success i {
        color: #10b981;
        font-size: 20px;
    }
    
    .mensaje-flotante.error {
        border-left: 4px solid #dc2626;
    }
    
    .mensaje-flotante.error i {
        color: #dc2626;
        font-size: 20px;
    }
    
    .mensaje-flotante span {
        color: #333;
        font-size: 14px;
    }
    
    /* Botón deshabilitado */
    .btn-agregar-bolsa:disabled {
        opacity: 0.6;
        cursor: not-allowed;
    }
    </style>
</body>
</html>