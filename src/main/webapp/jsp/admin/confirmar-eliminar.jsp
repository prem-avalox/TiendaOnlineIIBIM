<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="modelo.Prenda" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Confirmar Eliminación - Admin</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css">
</head>
<body>
    <div class="admin-container">
        <header class="admin-header">
            <h1>Confirmar Eliminación</h1>
            <div class="admin-nav">
                <a href="${pageContext.request.contextPath}/AdminPrendas" class="btn btn-secondary">← Volver</a>
            </div>
        </header>
        
        <div class="admin-content">
            <% 
            Prenda prenda = (Prenda) request.getAttribute("prenda");
            if (prenda != null) {
            %>
            
            <div class="confirmacion-box">
                <div class="warning-icon">⚠️</div>
                <h2>¿Está seguro que desea eliminar esta prenda?</h2>
                
                <div class="prenda-confirmacion">
                    <% if (prenda.getImagenUrl() != null && !prenda.getImagenUrl().isEmpty()) { %>
                        <img src="${pageContext.request.contextPath}/<%= prenda.getImagenUrl() %>" 
                             alt="<%= prenda.getNombre() %>" class="img-confirmacion">
                    <% } %>
                    
                    <div class="info-confirmacion">
                        <p><strong>ID:</strong> <%= prenda.getIdPrenda() %></p>
                        <p><strong>Nombre:</strong> <%= prenda.getNombre() %></p>
                        <p><strong>Categoría:</strong> <%= prenda.getCategoria() %></p>
                        <p><strong>Precio:</strong> $<%= prenda.getPrecio() %></p>
                    </div>
                </div>
                
                <div class="alert alert-warning">
                    <p><strong>Advertencia:</strong> Esta acción no se puede deshacer. 
                    Se eliminarán también todas las tallas asociadas a esta prenda.</p>
                </div>
                
                <form action="${pageContext.request.contextPath}/AdminPrendas" method="post" class="form-confirmacion">
                    <input type="hidden" name="accion" value="eliminarConfirmado">
                    <input type="hidden" name="id" value="<%= prenda.getIdPrenda() %>">
                    
                    <div class="form-actions">
                        <button type="submit" class="btn btn-danger">
                            Sí, Eliminar Prenda
                        </button>
                        <a href="${pageContext.request.contextPath}/AdminPrendas" class="btn btn-secondary">
                            Cancelar
                        </a>
                    </div>
                </form>
            </div>
            
            <% } else { %>
                <div class="alert alert-error">
                    <p>Prenda no encontrada.</p>
                    <a href="${pageContext.request.contextPath}/AdminPrendas" class="btn btn-primary">
                        Volver al Panel
                    </a>
                </div>
            <% } %>
        </div>
    </div>
</body>
</html>
