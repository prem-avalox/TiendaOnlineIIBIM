<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- Sidebar de Categorías -->
<div class="sidebar" id="sidebar">
    <div class="sidebar-header">
        <h2>CATEGORÍAS</h2>
        <button class="close-btn" onclick="toggleSidebar()">
            <i class="far fa-times"></i>
        </button>
    </div>
    <ul class="sidebar-menu">
        <li><a href="${pageContext.request.contextPath}/Catalogo">TODAS</a></li>
        <li><a href="${pageContext.request.contextPath}/Catalogo?accion=categoria&cat=CAMISAS">CAMISAS</a></li>
        <li><a href="${pageContext.request.contextPath}/Catalogo?accion=categoria&cat=PANTALONES">PANTALONES</a></li>
        <li><a href="${pageContext.request.contextPath}/Catalogo?accion=categoria&cat=CALZADO">CALZADO</a></li>
        <li><a href="${pageContext.request.contextPath}/Catalogo?accion=categoria&cat=ACCESORIOS">ACCESORIOS</a></li>
    </ul>
</div>

<!-- Overlay -->
<div class="overlay" id="overlay" onclick="toggleSidebar()"></div>
