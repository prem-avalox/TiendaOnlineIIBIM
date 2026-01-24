<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Iniciar Sesion</title>

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
				<a href="IniciarSesionController?ruta=logear"
					class="auth-tab active">INICIAR SESIÓN</a> <a
					href="RegistrarseController?ruta=crear" class="auth-tab">CREAR
					CUENTA</a>
			</div>

			<form action="IniciarSesionController" method="POST"
				class="auth-form">
				<input type="hidden" name="ruta" value="ingresar">

				<h2>Bienvenido</h2>
				<c:if test="${not empty mensajeError}">
					<div class="auth-error">${mensajeError}</div>
				</c:if>

				<div class="form-group">
					<label for="login-email">Usuario o Correo</label> <input
						type="text" id="login-email" name="usuario" required>
				</div>

				<div class="form-group">
					<label for="login-password">Contraseña</label> <input
						type="password" id="login-password" name="contraseña" required>
				</div>

				<button type="submit" class="btn-primary">INICIAR SESIÓN</button>
			</form>
		</div>
	</div>
</body>
</html>