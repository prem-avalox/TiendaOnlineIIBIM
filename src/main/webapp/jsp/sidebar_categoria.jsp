<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<aside id="sidebar" class="sidebar">
    <div class="sidebar-header">
        <h2>CATEGORÍAS</h2>
        <label for="toggle-sidebar" class="close-btn">
            <i class="fas fa-times"></i>
        </label>
    </div>
    
    <ul class="sidebar-menu">
    <li><a href="VerCatalogoController?ruta=listar" class="active-category">Ver Todo</a></li>
    
    <c:forEach var="cat" items="${categorias}">
        <li>
            <a href="VerCatalogoController?ruta=categoria&id=${cat}">
                ${cat.nombreCategoria}
            </a>
        </li>
    </c:forEach>
</ul>
</aside>