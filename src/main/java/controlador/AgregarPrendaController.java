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
		if (req.getSession().getAttribute("flash_ok") != null) {
		    req.setAttribute("registroExitoso", true);
		    req.getSession().removeAttribute("flash_ok"); // 👈 se borra solo
		}

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

		// Creamos la lista para almacenar solo las tallas que tienen stock ingresado
		List<StockTalla> stockTallas = new ArrayList<>();

		// Procesamos los arreglos de tallas y cantidades que vienen del formulario
		if (tallasStr != null && cantidadesStr != null && tallasStr.length == cantidadesStr.length) {
			for (int i = 0; i < tallasStr.length; i++) {
				try {
				    int cantidad = Integer.parseInt(cantidadesStr[i]);
				    Talla talla = Talla.valueOf(tallasStr[i]);

				    if (cantidad > 0) {
				        stockTallas.add(new StockTalla(0, cantidad, talla));
				    }
				} catch (Exception e) { 
				    // Usamos Exception para atrapar cualquier error de formato o de Enum
				    System.out.println("Error procesando talla o cantidad: " + e.getMessage());
				}
			}
		}

		// --- VALIDACIÓN: Tiene al menos una talla con stock
		if (stockTallas.isEmpty()) {
			// 1. Devolver los valores recibidos para que los campos no se limpien en la
			// vista
			req.setAttribute("nombrePrenda", nombre);
			req.setAttribute("descripcion", descripcion);
			req.setAttribute("imagen", imagen);
			req.setAttribute("color", color);
			req.setAttribute("corte", corte);
			req.setAttribute("precio", precio);
			req.setAttribute("categoriaSeleccionada", categoria.name());

			// 2. Enviamos el mensaje de error que detectará el JSP
			req.setAttribute("mensajeError",
					"Atención: Debe asignar stock a al menos una talla para registrar la prenda.");

			// 3. Reutilizamos el método agregarPrenda para cargar Enums (Categorías/Tallas)
			// y hacer el forward
			this.agregarPrenda(req, resp);
			return; // Detenemos la ejecución para que no llegue al DAO
		}

		// --- PERSISTENCIA: Si la validación pasó, guardamos en la Base de Datos ---
		try {
			PrendaDAO prendaDAO = new PrendaDAO();
			prendaDAO.guardar(nombre, descripcion, categoria, precio, stockTallas, imagen, color, corte);
		} catch (Exception e) {
			req.setAttribute("mensajeError", "Error técnico al guardar en la base de datos: " + e.getMessage());
			this.agregarPrenda(req, resp);
			return;
		}
		
		
		
		// 3. Llamar a la vista
		req.getSession().setAttribute("flash_ok", true);

		resp.sendRedirect(req.getContextPath()
		        + "/AgregarPrendaController?ruta=agregar");

	}

}
