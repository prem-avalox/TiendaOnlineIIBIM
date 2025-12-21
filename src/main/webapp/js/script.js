// Toggle sidebar de categorías
function toggleSidebar() {
    const sidebar = document.getElementById('sidebar');
    const overlay = document.getElementById('overlay');
    const cartSidebar = document.getElementById('cartSidebar');
    const cartOverlay = document.getElementById('cartOverlay');
    
    // Cerrar el carrito si está abierto
    if (cartSidebar && cartSidebar.classList.contains('active')) {
        cartSidebar.classList.remove('active');
        if (cartOverlay) cartOverlay.classList.remove('active');
    }
    
    if (sidebar && overlay) {
        sidebar.classList.toggle('active');
        overlay.classList.toggle('active');
    }
}

// Toggle carrito de compras (sidebar-bolsa)
function toggleCartSidebar() {
    const cartSidebar = document.getElementById('cartSidebar');
    const cartOverlay = document.getElementById('cartOverlay');
    const sidebar = document.getElementById('sidebar');
    const overlay = document.getElementById('overlay');
    
    // Cerrar el sidebar de categorías si está abierto
    if (sidebar && sidebar.classList.contains('active')) {
        sidebar.classList.remove('active');
        if (overlay) overlay.classList.remove('active');
    }
    
    if (cartSidebar && cartOverlay) {
        cartSidebar.classList.toggle('active');
        cartOverlay.classList.toggle('active');
    }
}

// Toggle dropdowns de filtros
function toggleDropdown(dropdownId) {
    const dropdown = document.getElementById(dropdownId);
    const allDropdowns = document.querySelectorAll('.filter-dropdown');
    
    // Cerrar otros dropdowns
    allDropdowns.forEach(d => {
        if (d.id !== dropdownId) {
            d.style.display = 'none';
        }
    });
    
    // Toggle el dropdown actual
    if (dropdown) {
        dropdown.style.display = dropdown.style.display === 'none' ? 'block' : 'none';
    }
}

// Cerrar dropdowns al hacer click fuera
document.addEventListener('click', function(event) {
    if (!event.target.closest('.cajas-filtro-lista')) {
        document.querySelectorAll('.filter-dropdown').forEach(d => {
            d.style.display = 'none';
        });
    }
});

// Función para limpiar filtros
function limpiarFiltros() {
    window.location.href = window.location.pathname;
}

// Hover en búsqueda
document.addEventListener('DOMContentLoaded', function() {
    const searchContainer = document.querySelector('#search');
    const searchForm = document.querySelector('.search-form-dropdown');

    if (searchContainer && searchForm) {
        searchContainer.addEventListener('mouseenter', function() {
            searchForm.style.opacity = '1';
            searchForm.style.visibility = 'visible';
        });

        searchContainer.addEventListener('mouseleave', function() {
            searchForm.style.opacity = '0';
            searchForm.style.visibility = 'hidden';
        });

        searchForm.addEventListener('mouseenter', function() {
            this.style.opacity = '1';
            this.style.visibility = 'visible';
        });

        searchForm.addEventListener('mouseleave', function() {
            this.style.opacity = '0';
            this.style.visibility = 'hidden';
        });
    }
    
    // Hacer tallas seleccionables en página de detalle
    const tallasItems = document.querySelectorAll('.talla-item');
    
    tallasItems.forEach(item => {
        item.addEventListener('click', function() {
            tallasItems.forEach(t => t.classList.remove('talla-seleccionada'));
            this.classList.add('talla-seleccionada');
        });
    });
});
