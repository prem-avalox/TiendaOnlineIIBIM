<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html lang="es">
<head>
<title>Crear Cuenta - Clothing Store</title>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/estilos.css">
</head>
<body class="general">

	<a href="VerCatalogoController?ruta=ingresar" class="back-button">
		<i class="fas fa-chevron-left"></i> <span>Volver a la tienda</span>
	</a>

	<div class="login-container">
		<div class="auth-box">
			<div class="auth-tabs">
				<a href="IniciarSesionController?ruta=logear" class="auth-tab">INICIAR
					SESIÓN</a> <a href="RegistrarseController?ruta=crear"
					class="auth-tab active">CREAR CUENTA</a>
			</div>

			<form action="RegistrarseController" method="POST" class="auth-form">
				<input type="hidden" name="ruta" value="enviarFormulario">

				<h2>Crear Cuenta</h2>
				<c:if test="${not empty mensajeError}">
					<p style="color: red; text-align: center;">${mensajeError}</p>
				</c:if>

				<div class="form-group">
					<label for="register-nombre">Nombre Completo</label> <input
						type="text" id="register-nombre" name="nombre" required>
				</div>

				<div class="form-group">
					<label for="register-email">Correo Electrónico</label> <input
						type="email" id="register-email" name="correo" required>
				</div>

				<div class="form-group">
					<label for="register-password">Contraseña</label> <input
						type="password" id="register-password" name="contraseña" required
						minlength="6">
				</div>

				<button type="submit" class="btn-primary">CREAR CUENTA</button>
			</form>
		</div>
	</div>
</body>
</html>