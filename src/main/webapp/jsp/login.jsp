<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Iniciar Sesión - Clothing Store</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Playfair+Display:wght@100;200;300;400;500;600;700;800;900&family=Roboto+Condensed:wght@100;200;300;400;500;600;700;800;900&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css">
</head>
<body>
    <div class="auth-container">
        <div class="auth-box">
            <div class="auth-form">
                <h2>INICIAR SESIÓN</h2>
                
                <!-- Mostrar mensajes de error -->
                <% if (request.getAttribute("error") != null) { %>
                    <div class="alert alert-error">
                        <%= request.getAttribute("error") %>
                    </div>
                <% } %>
                
                <!-- Mostrar mensajes de éxito -->
                <% if (request.getAttribute("mensaje") != null) { %>
                    <div class="alert alert-success">
                        <%= request.getAttribute("mensaje") %>
                    </div>
                <% } %>
                
                <form action="${pageContext.request.contextPath}/Login" method="post">
                    <div class="form-group">
                        <label for="correo">CORREO ELECTRÓNICO</label>
                        <input type="email" id="correo" name="correo" required>
                    </div>
                    
                    <div class="form-group">
                        <label for="contrasena">CONTRASEÑA</label>
                        <input type="password" id="contrasena" name="contrasena" required>
                    </div>
                    
                    <button type="submit" class="btn-primary">INICIAR SESIÓN</button>
                </form>
                
                <div class="auth-links">
                    <p>¿No tienes cuenta? 
                        <a href="${pageContext.request.contextPath}/Registro">Regístrate aquí</a>
                    </p>
                    <p>
                        <a href="${pageContext.request.contextPath}/Catalogo">Continuar sin iniciar sesión</a>
                    </p>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
