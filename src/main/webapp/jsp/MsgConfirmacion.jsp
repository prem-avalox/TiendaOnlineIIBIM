<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Confirmar eliminación</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css">
</head>
<body>

<div class="modal-overlay" style="display:flex;">
    <div class="modal-content">
        <div class="modal-body">
            <h2 class="modal-title">¿Está seguro que desea eliminar la prenda?</h2><br>
            <p class="modal-text">
                Esta acción borrará el registro permanentemente del sistema.
            </p><br>

            <div class="button-group-admin" style="display:flex; gap:10px; justify-content:center;">
                <form method="post" action="GestionarPrendasController">
                    <input type="hidden" name="ruta" value="confirmarEliminar">
                    <input type="hidden" name="idPrenda" value="${idPrenda}">
                    <input type="hidden" name="respuesta" value="si">
                    <button type="submit" class="btn-delete-confirm" style="background-color: #e74c3c; color: white; padding: 10px 20px; border: none; border-radius: 4px;">SÍ, ELIMINAR</button>
                </form>

                <form method="get" action="GestionarPrendasController">
                    <input type="hidden" name="ruta" value="listar">
                    <button type="submit" class="btn-cancelar" style="background-color: #95a5a6; color: white; padding: 10px 20px; border: none; border-radius: 4px;">NO, CANCELAR</button>
                </form>
            </div>
        </div>
    </div>
</div>

</body>
</html>
