<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="modelo.Prenda" %>
<%@ page import="modelo.Talla" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><%= request.getAttribute("accion").equals("guardar") ? "Nueva Prenda" : "Editar Prenda" %> - Admin</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css">
</head>
<body>
    <div class="admin-container">
        <header class="admin-header">
            <h1><%= request.getAttribute("accion").equals("guardar") ? "Agregar Nueva Prenda" : "Editar Prenda" %></h1>
            <div class="admin-nav">
                <a href="${pageContext.request.contextPath}/AdminPrendas" class="btn btn-secondary">← Volver</a>
            </div>
        </header>
        
        <div class="admin-content">
            <!-- Mostrar errores -->
            <% if (request.getAttribute("error") != null) { %>
                <div class="alert alert-error">
                    <%= request.getAttribute("error") %>
                </div>
            <% } %>
            
            <% 
            Prenda prenda = (Prenda) request.getAttribute("prenda");
            String accion = (String) request.getAttribute("accion");
            boolean esEdicion = "actualizar".equals(accion);
            %>
            
            <form action="${pageContext.request.contextPath}/AdminPrendas" method="post" class="form-admin">
                <input type="hidden" name="accion" value="<%= accion %>">
                
                <% if (esEdicion && prenda != null) { %>
                    <input type="hidden" name="id" value="<%= prenda.getIdPrenda() %>">
                <% } %>
                
                <div class="form-row">
                    <div class="form-group">
                        <label for="nombre">Nombre de la Prenda: *</label>
                        <input type="text" id="nombre" name="nombre" required
                               value="<%= esEdicion && prenda != null ? prenda.getNombre() : "" %>">
                    </div>
                    
                    <div class="form-group">
                        <label for="categoria">Categoría: *</label>
                        <select id="categoria" name="categoria" required>
                            <option value="">Seleccione...</option>
                            <option value="CAMISAS" 
                                <%= esEdicion && prenda != null && "CAMISAS".equals(prenda.getCategoria()) ? "selected" : "" %>>
                                Camisas
                            </option>
                            <option value="PANTALONES" 
                                <%= esEdicion && prenda != null && "PANTALONES".equals(prenda.getCategoria()) ? "selected" : "" %>>
                                Pantalones
                            </option>
                            <option value="CALZADO" 
                                <%= esEdicion && prenda != null && "CALZADO".equals(prenda.getCategoria()) ? "selected" : "" %>>
                                Calzado
                            </option>
                            <option value="ACCESORIOS" 
                                <%= esEdicion && prenda != null && "ACCESORIOS".equals(prenda.getCategoria()) ? "selected" : "" %>>
                                Accesorios
                            </option>
                        </select>
                    </div>
                </div>
                
                <div class="form-group">
                    <label for="descripcion">Descripción:</label>
                    <textarea id="descripcion" name="descripcion" rows="4"><%= esEdicion && prenda != null && prenda.getDescripcion() != null ? prenda.getDescripcion() : "" %></textarea>
                </div>
                
                <div class="form-row">
                    <div class="form-group">
                        <label for="precio">Precio: *</label>
                        <input type="number" id="precio" name="precio" step="0.01" min="0" required
                               value="<%= esEdicion && prenda != null ? prenda.getPrecio() : "" %>">
                    </div>
                    
                    <div class="form-group">
                        <label for="color">Color:</label>
                        <input type="text" id="color" name="color"
                               value="<%= esEdicion && prenda != null && prenda.getColor() != null ? prenda.getColor() : "" %>">
                    </div>
                </div>
                
                <div class="form-row">
                    <div class="form-group">
                        <label for="tipoAjuste">Tipo de Ajuste:</label>
                        <select id="tipoAjuste" name="tipoAjuste">
                            <option value="">Seleccione...</option>
                            <option value="Slim" 
                                <%= esEdicion && prenda != null && "Slim".equals(prenda.getTipoAjuste()) ? "selected" : "" %>>
                                Slim
                            </option>
                            <option value="Regular" 
                                <%= esEdicion && prenda != null && "Regular".equals(prenda.getTipoAjuste()) ? "selected" : "" %>>
                                Regular
                            </option>
                            <option value="Oversize" 
                                <%= esEdicion && prenda != null && "Oversize".equals(prenda.getTipoAjuste()) ? "selected" : "" %>>
                                Oversize
                            </option>
                        </select>
                    </div>
                    
                    <div class="form-group">
                        <label for="imagenUrl">URL de la Imagen:</label>
                        <input type="text" id="imagenUrl" name="imagenUrl" placeholder="images/nombre-imagen.jpg"
                               value="<%= esEdicion && prenda != null && prenda.getImagenUrl() != null ? prenda.getImagenUrl() : "" %>">
                    </div>
                </div>
                
                <% if (esEdicion && prenda != null) { %>
                <div class="info-tallas">
                    <h3>Tallas Actuales:</h3>
                    <p class="note">Para modificar las tallas, use el módulo de gestión de inventario.</p>
                    <% 
                    @SuppressWarnings("unchecked")
                    List<Talla> tallas = (List<Talla>) request.getAttribute("tallas");
                    if (tallas != null && !tallas.isEmpty()) {
                    %>
                        <div class="tallas-lista">
                            <% for (Talla talla : tallas) { %>
                                <span class="talla-badge">
                                    <%= talla.getTalla() %>: <%= talla.getStock() %> unidades
                                </span>
                            <% } %>
                        </div>
                    <% } else { %>
                        <p>No hay tallas registradas para esta prenda.</p>
                    <% } %>
                </div>
                <% } %>
                
                <div class="form-actions">
                    <button type="submit" class="btn btn-primary">
                        <%= esEdicion ? "Actualizar Prenda" : "Guardar Prenda" %>
                    </button>
                    <a href="${pageContext.request.contextPath}/AdminPrendas" class="btn btn-secondary">
                        Cancelar
                    </a>
                </div>
                
                <p class="note">* Campos obligatorios</p>
            </form>
        </div>
    </div>
</body>
</html>
