<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<footer class="footer">
    <div class="footer-container">
        <div class="footer-section">
            <h3>Tienda Masculina</h3>
            <p>Tu tienda de confianza para ropa masculina de calidad</p>
        </div>
        
        <div class="footer-section">
            <h4>Categorías</h4>
            <ul>
                <li><a href="${pageContext.request.contextPath}/Catalogo?accion=categoria&cat=CAMISAS">Camisas</a></li>
                <li><a href="${pageContext.request.contextPath}/Catalogo?accion=categoria&cat=PANTALONES">Pantalones</a></li>
                <li><a href="${pageContext.request.contextPath}/Catalogo?accion=categoria&cat=CALZADO">Calzado</a></li>
                <li><a href="${pageContext.request.contextPath}/Catalogo?accion=categoria&cat=ACCESORIOS">Accesorios</a></li>
            </ul>
        </div>
        
        <div class="footer-section">
            <h4>Información</h4>
            <ul>
                <li><a href="#">Sobre Nosotros</a></li>
                <li><a href="#">Contacto</a></li>
                <li><a href="#">Términos y Condiciones</a></li>
                <li><a href="#">Política de Privacidad</a></li>
            </ul>
        </div>
        
        <div class="footer-section">
            <h4>Síguenos</h4>
            <div class="social-links">
                <a href="#">Facebook</a>
                <a href="#">Instagram</a>
                <a href="#">Twitter</a>
            </div>
        </div>
    </div>
    
    <div class="footer-bottom">
        <p>&copy; 2025 Tienda Masculina. Todos los derechos reservados.</p>
    </div>
</footer>

<script src="${pageContext.request.contextPath}/js/script.js"></script>
