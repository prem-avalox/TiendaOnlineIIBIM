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
        <li><a href="VerCatalogoController?ruta=categoria&id=CAMISAS">Camisas</a></li>
        <li><a href="VerCatalogoController?ruta=categoria&id=PANTALONES">Pantalones</a></li>
        <li><a href="VerCatalogoController?ruta=categoria&id=CALZADO">Calzado</a></li>
        <li><a href="VerCatalogoController?ruta=categoria&id=ACCESORIOS">Accesorios</a></li>
    </ul>
</aside>