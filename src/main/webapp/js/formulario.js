/**
 * Función para cerrar el modal de éxito
 */
function cerrarModal() {
    const modal = document.getElementById('modalExito');
    if (modal) {
        modal.style.display = 'none';
    }
}

/**
 * Opcional: Cerrar el modal si el usuario hace clic fuera de la caja blanca
 */
window.onclick = function(event) {
    const modal = document.getElementById('modalExito');
    if (event.target == modal) {
        modal.style.display = "none";
    }
}