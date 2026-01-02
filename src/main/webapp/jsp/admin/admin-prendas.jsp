<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="modelo.entidades.Prenda" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Panel Administrador - Tienda Online</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css">
</head>
<body>
    <div class="admin-container">
        <header class="admin-header">
            <h1>Panel de Administración</h1>
            <div class="admin-nav">
                <a href="${pageContext.request.contextPath}/Catalogo" class="btn btn-secondary">Ver Tienda</a>
                <a href="${pageContext.request.contextPath}/Logout" class="btn btn-danger">Cerrar Sesión</a>
            </div>
        </header>
        
        <div class="admin-content">
            <div class="page-header">
                <h2>Gestión de Prendas</h2>
                <a href="${pageContext.request.contextPath}/AdminPrendas?accion=nuevo" class="btn btn-primary">
                    + Agregar Nueva Prenda
                </a>
            </div>
            
            <!-- Mostrar mensajes -->
            <% if (request.getAttribute("mensaje") != null) { %>
                <div class="alert alert-success">
                    <%= request.getAttribute("mensaje") %>
                </div>
            <% } %>
            
            <% if (request.getAttribute("error") != null) { %>
                <div class="alert alert-error">
                    <%= request.getAttribute("error") %>
                </div>
            <% } %>
            
            <!-- Tabla de prendas -->
            <div class="table-container">
                <table class="admin-table">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Imagen</th>
                            <th>Nombre</th>
                            <th>Categoría</th>
                            <th>Precio</th>
                            <th>Color</th>
                            <th>Tipo Ajuste</th>
                            <th>Acciones</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% 
                        @SuppressWarnings("unchecked")
                        List<Prenda> prendas = (List<Prenda>) request.getAttribute("prendas");
                        
                        if (prendas != null && !prendas.isEmpty()) {
                            for (Prenda prenda : prendas) {
                        %>
                            <tr>
                                <td><%= prenda.getIdPrenda() %></td>
                                <td class="td-imagen">
                                    <% if (prenda.getImagenUrl() != null && !prenda.getImagenUrl().isEmpty()) { %>
                                        <img src="${pageContext.request.contextPath}/<%= prenda.getImagenUrl() %>" 
                                             alt="<%= prenda.getNombre() %>" class="table-img">
                                    <% } else { %>
                                        <div class="no-img-table">Sin imagen</div>
                                    <% } %>
                                </td>
                                <td><%= prenda.getNombre() %></td>
                                <td><%= prenda.getCategoria() %></td>
                                <td>$<%= prenda.getPrecio() %></td>
                                <td><%= prenda.getColor() != null ? prenda.getColor() : "-" %></td>
                                <td><%= prenda.getTipoAjuste() != null ? prenda.getTipoAjuste() : "-" %></td>
                                <td class="td-acciones">
                                    <a href="${pageContext.request.contextPath}/AdminPrendas?accion=editar&id=<%= prenda.getIdPrenda() %>" 
                                       class="btn btn-sm btn-edit" title="Editar">
                                        ✏️ Editar
                                    </a>
                                    <a href="${pageContext.request.contextPath}/AdminPrendas?accion=eliminar&id=<%= prenda.getIdPrenda() %>" 
                                       class="btn btn-sm btn-delete" title="Eliminar">
                                        🗑️ Eliminar
                                    </a>
                                </td>
                            </tr>
                        <%
                            }
                        } else {
                        %>
                            <tr>
                                <td colspan="8" class="text-center">No hay prendas registradas</td>
                            </tr>
                        <%
                        }
                        %>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</body>
</html>
