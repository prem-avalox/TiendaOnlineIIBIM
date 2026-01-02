package servlets;

import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import modelo.dao.PrendaDAO;
import modelo.entidades.Prenda;

/**
 * Servlet para mostrar el catálogo de prendas
 */
@WebServlet("/Catalogo")
public class CatalogoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private PrendaDAO prendaDAO;
	
	public CatalogoServlet() {
		super();
		this.prendaDAO = new PrendaDAO();
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		String accion = request.getParameter("accion");
		
		if (accion == null) {
			accion = "listar";
		}
		
		switch (accion) {
			case "buscar":
				buscarPrendas(request, response);
				break;
			case "filtrar":
				filtrarPrendas(request, response);
				break;
			case "categoria":
				filtrarPorCategoria(request, response);
				break;
			default:
				listarPrendas(request, response);
				break;
		}
	}
	
	/**
	 * Lista todas las prendas del catálogo
	 */
	private void listarPrendas(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		System.out.println("🔍 CatalogoServlet.listarPrendas() - Iniciando...");
		
		List<Prenda> prendas = prendaDAO.listarPrendas();
		
		System.out.println("🔍 PrendaDAO retornó: " + (prendas != null ? prendas.size() + " prendas" : "NULL"));
		
		if (prendas != null && !prendas.isEmpty()) {
			System.out.println("🔍 Ejemplos de prendas:");
			for (int i = 0; i < Math.min(3, prendas.size()); i++) {
				Prenda p = prendas.get(i);
				System.out.println("   - " + p.getIdPrenda() + ": " + p.getNombre() + " ($" + p.getPrecio() + ")");
			}
		} else {
			System.out.println("⚠️ Lista de prendas está vacía o es null!");
		}
		
		request.setAttribute("prendas", prendas);
		request.setAttribute("titulo", "Catálogo Completo");
		
		System.out.println("🔍 Forwarding a catalogo.jsp...");
		request.getRequestDispatcher("/jsp/catalogo.jsp").forward(request, response);
	}
	
	/**
	 * Busca prendas por nombre
	 */
	private void buscarPrendas(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		String termino = request.getParameter("termino");
		
		if (termino == null || termino.trim().isEmpty()) {
			listarPrendas(request, response);
			return;
		}
		
		List<Prenda> prendas = prendaDAO.buscarPrendas(termino);
		
		request.setAttribute("prendas", prendas);
		request.setAttribute("titulo", "Resultados de búsqueda: " + termino);
		request.setAttribute("termino", termino);
		request.getRequestDispatcher("/jsp/catalogo.jsp").forward(request, response);
	}
	
	/**
	 * Filtra prendas por múltiples criterios
	 */
	private void filtrarPrendas(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		String color = request.getParameter("color");
		String tipoAjuste = request.getParameter("tipoAjuste");
		String categoria = request.getParameter("categoria");
		
		List<Prenda> prendas = prendaDAO.filtrarPrendas(categoria, color, tipoAjuste);
		
		request.setAttribute("prendas", prendas);
		request.setAttribute("titulo", "Prendas Filtradas");
		request.getRequestDispatcher("/jsp/catalogo.jsp").forward(request, response);
	}
	
	/**
	 * Filtra prendas por categoría específica
	 */
	private void filtrarPorCategoria(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		String categoria = request.getParameter("cat");
		
		if (categoria == null || categoria.trim().isEmpty()) {
			listarPrendas(request, response);
			return;
		}
		
		List<Prenda> prendas = prendaDAO.filtrarPrendas(categoria, null, null);
		
		request.setAttribute("prendas", prendas);
		request.setAttribute("titulo", "Categoría: " + categoria);
		request.setAttribute("categoriaSeleccionada", categoria);
		request.getRequestDispatcher("/jsp/catalogo.jsp").forward(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		doGet(request, response);
	}
}
