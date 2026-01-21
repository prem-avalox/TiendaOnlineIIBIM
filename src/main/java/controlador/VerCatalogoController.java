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

	    try {
	        // 2. Hablar con el modelo 
	        PrendaDAO prendaDAO = new PrendaDAO();
	        List<Prenda> prendas = prendaDAO.getListaPrendas(nombre);

	        req.setAttribute("categorias", Categoria.values());
	        req.setAttribute("tallas", Talla.values());
	        req.setAttribute("colores", Color.values());
	        req.setAttribute("cortes", Corte.values());

	        if (prendas != null && !prendas.isEmpty()) {
	            req.setAttribute("prendas", prendas);
	        } else {
	            req.setAttribute("mensajeError", "No se encontraron prendas con el nombre ingresado");
	        }

	    } catch (Exception e) {
	        req.setAttribute("mensajeError", "Error interno al buscar: " + e.getMessage());
	    }

	    // 3. Llamar a la vista 
	    req.getRequestDispatcher("jsp/Catalogo.jsp").forward(req, resp);
	}

	private void seleccionarFiltros(HttpServletRequest req, HttpServletResponse resp)
	        throws ServletException, IOException {
	    // 1. Obtener los parametros del formulario (Paso 3 del diagrama)
	    String tallaStr = req.getParameter("talla");
	    String colorStr = req.getParameter("color");
	    String corteStr = req.getParameter("corte");

	    try {
	        Talla talla = (tallaStr != null && !tallaStr.isEmpty()) ? Talla.valueOf(tallaStr) : null;
	        Color color = (colorStr != null && !colorStr.isEmpty()) ? Color.valueOf(colorStr) : null;
	        Corte corte = (corteStr != null && !corteStr.isEmpty()) ? Corte.valueOf(corteStr) : null;

	        // 2. Hablar con el modelo
	        PrendaDAO prendaDAO = new PrendaDAO();
	        List<Prenda> prendas = prendaDAO.filtrarPrendas(talla, color, corte);

	        req.setAttribute("categorias", Categoria.values());
	        req.setAttribute("tallas", Talla.values());
	        req.setAttribute("colores", Color.values());
	        req.setAttribute("cortes", Corte.values());

	        if (prendas != null && !prendas.isEmpty()) {
	            req.setAttribute("prendas", prendas);
	        } else {
	            req.setAttribute("mensajeError", "No hay prendas que coincidan con los filtros seleccionados");
	        }

	    } catch (Exception e) {
	        req.setAttribute("mensajeError", "Error al aplicar filtros: " + e.getMessage());
	    }

	    // 3. Llamar a la vista 
	    req.getRequestDispatcher("jsp/Catalogo.jsp").forward(req, resp);
	}

	private void seleccionarCategoria(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String idCategoria = req.getParameter("idCategoria");

        try {
            Categoria categoriaSeleccionada = Categoria.valueOf(idCategoria);
            PrendaDAO prendaDAO = new PrendaDAO();
            List<Prenda> prendas = prendaDAO.getListaPrendas(categoriaSeleccionada);

            req.setAttribute("categorias", Categoria.values());
            req.setAttribute("tallas", Talla.values());
            req.setAttribute("colores", Color.values());
            req.setAttribute("cortes", Corte.values());

            if (prendas != null && !prendas.isEmpty()) {
                req.setAttribute("prendas", prendas);
            } else {
                req.setAttribute("mensajeError", "No hay prendas en la categoría seleccionada");
            }

        } catch (Exception e) {
            req.setAttribute("mensajeError", "Error al procesar la categoría: " + e.getMessage());
        }

        req.getRequestDispatcher("jsp/Catalogo.jsp").forward(req, resp);
    }

	private void seleccionarPrenda(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// 1. Obtener los parametros
		String idPrendaStr = req.getParameter("idPrenda");
		if (idPrendaStr == null || idPrendaStr.isEmpty()) {
			req.setAttribute("mensajeError", "ID de prenda no especificado");
			req.getRequestDispatcher("jsp/Catalogo.jsp").forward(req, resp);
			return;
		}

		try {
			int idPrenda = Integer.parseInt(idPrendaStr);
			PrendaDAO prendaDAO = new PrendaDAO();
			Prenda prenda = prendaDAO.getPrenda(idPrenda);
			if (prenda == null) {
				req.setAttribute("mensajeError", "Prenda no encontrada");
				req.getRequestDispatcher("jsp/Catalogo.jsp").forward(req, resp);
				return;
			}
			req.setAttribute("prenda", prenda);
			req.getRequestDispatcher("jsp/Prenda.jsp").forward(req, resp);
		} catch (Exception e) {
			req.setAttribute("mensajeError", "Error al cargar la prenda: " + e.getMessage());
			req.getRequestDispatcher("jsp/Catalogo.jsp").forward(req, resp);
		}
	}
}