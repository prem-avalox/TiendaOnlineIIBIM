<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Bienvenido - Tienda Online</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css">
</head>
<body>
    <div class="landing-container">
        <div class="landing-content">
            <h1>Bienvenido a Tienda Masculina</h1>
            <p class="subtitle">Tu destino para ropa masculina de calidad</p>
            
            <div class="landing-buttons">
                <a href="${pageContext.request.contextPath}/Catalogo" class="btn btn-primary btn-large">
                    Ver Catálogo
                </a>
                <a href="${pageContext.request.contextPath}/Login" class="btn btn-secondary btn-large">
                    Iniciar Sesión
                </a>
            </div>
            
            <div class="categorias-preview">
                <h2>Nuestras Categorías</h2>
                <div class="categorias-grid">
                    <a href="${pageContext.request.contextPath}/Catalogo?accion=categoria&cat=CAMISAS" class="categoria-card">
                        <div class="categoria-icon">👔</div>
                        <h3>Camisas</h3>
                    </a>
                    <a href="${pageContext.request.contextPath}/Catalogo?accion=categoria&cat=PANTALONES" class="categoria-card">
                        <div class="categoria-icon">👖</div>
                        <h3>Pantalones</h3>
                    </a>
                    <a href="${pageContext.request.contextPath}/Catalogo?accion=categoria&cat=CALZADO" class="categoria-card">
                        <div class="categoria-icon">👞</div>
                        <h3>Calzado</h3>
                    </a>
                    <a href="${pageContext.request.contextPath}/Catalogo?accion=categoria&cat=ACCESORIOS" class="categoria-card">
                        <div class="categoria-icon">⌚</div>
                        <h3>Accesorios</h3>
                    </a>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
