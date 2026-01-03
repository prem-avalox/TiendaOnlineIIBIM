package controlador;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import modelo.dao.PrendaDAO;
import modelo.entidades.Prenda;

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

		String ruta = (req.getParameter("ruta") != null) ? req.getParameter("ruta") : "listar";

		switch (ruta) {
		case "listar":
			this.listar(req, resp);
			break;

		case "buscar":
			this.buscarPrenda(req, resp);
			break;

		case "filtros":
			this.seleccionarFiltros(req, resp);
			break;

		case "categorias":
			this.desplegarCategorias(req, resp);
			break;

		case "categoria":
			this.seleccionarCategoria(req, resp);
			break;

		case "detalle":
			this.seleccionarPrenda(req, resp);
			break;

		default:
			this.listar(req, resp);
			break;
		}
	}

	private void listar(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("Entrando al listar del VerCatalogoController");
		// 1. Obtener los parametros

		// 2. Hablar con el modelo
		PrendaDAO prendaDAO = new PrendaDAO();
		List<Prenda> prendas = prendaDAO.getListaPrendas();
		// 3. Llamar a la vista
		req.setAttribute("prendas", prendas);
		req.getRequestDispatcher("jsp/catalogo.jsp").forward(req, resp);
	}

	private void buscarPrenda(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("hola buscar prenda");
		// 1. Obtener los parametros
		// 2. Hablar con el modelo
		// 3. Llamar a la vista
		resp.sendRedirect("jsp/catalogo.jsp");
	}

	private void seleccionarFiltros(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// 1. Obtener los parametros
		// 2. Hablar con el modelo
		// 3. Llamar a la vista
		resp.sendRedirect("jsp/catalogo.jsp");
	}

	private void desplegarCategorias(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// 1. Obtener los parametros
		// 2. Hablar con el modelo
		// 3. Llamar a la vista
		resp.sendRedirect("jsp/sidebar_categoria.jsp");
	}

	private void seleccionarCategoria(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// 1. Obtener los parametros
		// 2. Hablar con el modelo
		// 3. Llamar a la vista
		resp.sendRedirect("jsp/catalogo.jsp");

	}

	private void seleccionarPrenda(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// 1. Obtener los parametros
		// 2. Hablar con el modelo
		// 3. Llamar a la vista
		resp.sendRedirect("jsp/prenda.jsp");
	}

}
