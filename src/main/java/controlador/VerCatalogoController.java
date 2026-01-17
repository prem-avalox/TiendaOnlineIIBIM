package controlador;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import modelo.dao.PrendaDAO;
import modelo.entidades.Categoria;
import modelo.entidades.Color;
import modelo.entidades.Corte;
import modelo.entidades.Prenda;
import modelo.entidades.Talla;

@WebServlet("/VerCatalogoController")
public class VerCatalogoController extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.ruteador(req, resp);

	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.ruteador(req, resp);
	}

	private void ruteador(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String ruta = (req.getParameter("ruta") != null) ? req.getParameter("ruta") : "ingresar";

		switch (ruta) {
		case "ingresar":
			this.verCatalogo(req, resp);
			break;

		case "buscar":
			this.buscarPrenda(req, resp);
			break;

		case "aplicarFiltros":
			this.seleccionarFiltros(req, resp);
			break;

		case "seleccionarCategoria":
			this.seleccionarCategoria(req, resp);
			break;

		case "visualizarPrenda":
			this.seleccionarPrenda(req, resp);
			break;

		default:
			this.verCatalogo(req, resp);
			break;
		}
	}

	private void verCatalogo(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	    // 1. Obtener los parametros (No se requieren para la vista inicial)

	    try {
	        // 2. Hablar con el modelo
	        PrendaDAO prendaDAO = new PrendaDAO();
	        List<Prenda> prendas = prendaDAO.getListaPrendas();

	        // Según el diagrama de secuencia 1.2 a 1.5: Obtener valores de Enums
	        req.setAttribute("prendas", prendas);
	        req.setAttribute("categorias", Categoria.values());
	        req.setAttribute("tallas", Talla.values());
	        req.setAttribute("colores", Color.values());
	        req.setAttribute("cortes", Corte.values());

	        // Flujo alterno 2.1: Catálogo vacío
	        if (prendas == null || prendas.isEmpty()) {
	            req.setAttribute("mensajeError", "No hay prendas");
	        }

	    } catch (Exception e) {
	        req.setAttribute("mensajeError", "Error al cargar el catálogo: " + e.getMessage());
	    }

	    // 3. Llamar a la vista (Paso 1.6 del diagrama)
	    req.getRequestDispatcher("jsp/Catalogo.jsp").forward(req, resp);
	}

	private void buscarPrenda(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 1. Obtener los parametros
		String nombre = req.getParameter("nombre");

		// 2. Hablar con el modelo

		// 3. Llamar a la vista
	}

	private void seleccionarFiltros(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// 1. Obtener los parametros
		String talla = req.getParameter("talla");
		String color = req.getParameter("color");
		String corte = req.getParameter("corte");

		// 2. Hablar con el modelo

		// 3. Llamar a la vista

	}

	private void seleccionarCategoria(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// 1. Obtener los parametros
		String idCategoria = req.getParameter("idCategoria");

		// 2. Hablar con el modelo

		// 3. Llamar a la vista
	}

	private void seleccionarPrenda(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// 1. Obtener los parametros
		String idPrenda = req.getParameter("idPrenda");
		// 2. Hablar con el modelo
		
		// 3. Llamar a la vista
	}
}
