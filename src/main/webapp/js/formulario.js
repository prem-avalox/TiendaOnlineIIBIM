/**
 * Función para cerrar el modal de éxito
 */
function cerrarModal() {
    const modal = document.getElementById('modalExito');
    if (modal) {
        modal.style.display = 'none';
        
        // Esto limpia los parámetros de la URL (?ruta=guardar) 
        // para que si el usuario recarga, no salga el modal de nuevo.
        const url = new URL(window.location);
        url.searchParams.set('ruta', 'agregar');
        window.history.replaceState({}, '', url);
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