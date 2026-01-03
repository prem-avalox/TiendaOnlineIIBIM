package controlador;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import modelo.dao.PrendaDAO;
import modelo.entidades.Categoria;
import modelo.entidades.Prenda;
import modelo.entidades.StockTalla;
import modelo.entidades.Talla;

@WebServlet("/AgregarPrendaController")
public class AgregarPrendaController extends HttpServlet {

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

		String ruta = (req.getParameter("ruta") != null) ? req.getParameter("ruta") : "agregar";

		switch (ruta) {
		case "agregar":
			this.agregarPrenda(req, resp);
			break;

		case "guardar":
			this.guardar(req, resp);
			break;

		default:
			this.agregarPrenda(req, resp);
			break;
		}
	}

	private void agregarPrenda(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("Entrando al agregar prenda del AgregarPrendaController");
		// 1. Obtener los parametros

		// 2. Hablar con el modelo
		req.setAttribute("categorias", Categoria.values());
		req.setAttribute("tallas", Talla.values());

		// 3. Llamar a la vista
		req.getRequestDispatcher("jsp/formulario_registro_prenda.jsp").forward(req, resp);
	}

	private void guardar(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		// 1. Obtener los parametros
		String nombre = req.getParameter("nombrePrenda");
		String descripcion = req.getParameter("descripcion");
		String imagen = req.getParameter("imagen");
		String color = req.getParameter("color");
		String corte = req.getParameter("corte");
		double precio = Double.parseDouble(req.getParameter("precio"));
		Categoria categoria = Categoria.valueOf(req.getParameter("categoria")); // Enum desde String
		String[] tallasStr = req.getParameterValues("talla");
		String[] cantidadesStr = req.getParameterValues("cantidad");


		// 2. Hablar con el modelo

		List<StockTalla> stockTallas = new ArrayList<>();

		if (tallasStr != null && cantidadesStr != null && tallasStr.length == cantidadesStr.length) {

			for (int i = 0; i < tallasStr.length; i++) {

				int cantidad = Integer.parseInt(cantidadesStr[i]);
				Talla talla = Talla.valueOf(tallasStr[i]);

				 // SOLO si hay stock
			    if (cantidad > 0) {
			        stockTallas.add(new StockTalla(0, cantidad, talla));
			    }

			}
		}
		PrendaDAO prendaDAO = new PrendaDAO();
		prendaDAO.guardar(nombre, descripcion, categoria, precio, stockTallas, imagen, color, corte);
		// 3. Llamar a la vista
		req.setAttribute("registroExitoso", true);
		req.setAttribute("categorias", Categoria.values());
		req.setAttribute("tallas", Talla.values());
		req.getRequestDispatcher("jsp/formulario_registro_prenda.jsp").forward(req, resp);
	}

}
