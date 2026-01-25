/**
 * Función para cerrar el modal y redirigir
 */
function cerrarModal() {
    const modal = document.getElementById('modalExito');
    if (modal) {
        modal.style.display = 'none';
        // Redirección al controlador de gestión
        window.location.href = 'GestionarPrendasController?ruta=listar';
    }
}

window.onclick = function(event) {
    const modal = document.getElementById('modalExito');
    // Si el usuario hace clic exactamente en el overlay (fondo oscuro)
    if (event.target === modal) {
        cerrarModal(); // Reutilizamos la función de arriba que ya tiene la redirección
    }
}


document.addEventListener('DOMContentLoaded', function() {
    const form = document.querySelector('form');
    
    if (form) {
        form.addEventListener('submit', function(event) {
            const cantidades = document.querySelectorAll('input[name="cantidad"]');
            let totalStock = 0;

            cantidades.forEach(input => {
                totalStock += parseInt(input.value) || 0;
            });

            if (totalStock <= 0) {
                event.preventDefault(); 
                alert("Atención: Debe asignar stock a al menos una talla para registrar la prenda.");
            }
        });
    }
});
