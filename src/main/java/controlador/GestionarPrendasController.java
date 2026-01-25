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
import modelo.entidades.Prenda;
import modelo.entidades.StockTalla;
import modelo.entidades.Talla;

@WebServlet("/GestionarPrendasController")
public class GestionarPrendasController extends HttpServlet {

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

		case "editarPrenda":
			this.editarPrenda(req, resp);
			break;

		case "guardar":
			this.guardar(req, resp);
			break;

		case "eliminarPrenda":
			this.eliminarPrenda(req, resp);
			break;

		case "confirmarEliminar":
			this.enviarConfirmacion(req, resp);
			break;

		default:
			this.listar(req, resp);
			break;
		}

	}

	private void listar(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("Entrando al listar del ver lista completa controller");

		// 1. Obtener parámetros

		try {
			// 2. Hablar con el modelo
			PrendaDAO prendaDAO = new PrendaDAO();
			List<Prenda> lista = prendaDAO.getListaPrendas();

			req.setAttribute("prendas", lista);

		} catch (Exception e) {
			if (req.getAttribute("mensajeError") == null) {
				req.setAttribute("mensajeError", "Error al cargar la lista: " + e.getMessage());
			}
		}

		// 3. Llamar a la vista listar_prendas.jsp
		req.getRequestDispatcher("jsp/ListaPrendas.jsp").forward(req, resp);
	}

	private void eliminarPrenda(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		// 1. Obtener parámetro
		String idStr = req.getParameter("id");

		if (idStr == null) {
			req.setAttribute("mensajeError", "ID de prenda no válido");
			listar(req, resp);
			return;
		}

		// 2. Hablar con el modelo
		// (no se elimina nada todavía, solo se prepara la confirmación)

		// 3. Llamar a la vista
		req.setAttribute("idPrenda", idStr);
		req.getRequestDispatcher("jsp/MsgConfirmacion.jsp").forward(req, resp);
	}

	private void enviarConfirmacion(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		// 1. Obtener parámetros
		String respuesta = req.getParameter("respuesta");
		String idStr = req.getParameter("idPrenda");

		// 2. Hablar con el modelo
		if ("si".equals(respuesta)) {
			PrendaDAO dao = new PrendaDAO();
			dao.eliminar(Integer.parseInt(idStr));
			req.setAttribute("mensajeExito", "Prenda eliminada correctamente.");
		}

		// 3. Llamar a la vista
		listar(req, resp);
	}

	private void editarPrenda(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		// 1. Obtener parámetros
		String idStr = req.getParameter("id");

		// 2. Hablar con el modelo
		int idPrenda = Integer.parseInt(idStr);

		PrendaDAO prendaDAO = new PrendaDAO();
		Prenda prenda = prendaDAO.getPrenda(idPrenda);

		req.setAttribute("p", prenda);
		req.setAttribute("categorias", modelo.entidades.Categoria.values());
		req.setAttribute("colores", modelo.entidades.Color.values());
		req.setAttribute("cortes", modelo.entidades.Corte.values());
		req.setAttribute("tallasDisponibles", modelo.entidades.Talla.values());

		// 3. Llamar a la vista
		req.getRequestDispatcher("jsp/DatosPrenda.jsp").forward(req, resp);
	}

	private void guardar(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		// 1. Obtener parámetros
		String idStr = req.getParameter("idPrenda");
		String imagen = req.getParameter("imagen");
		String nombre = req.getParameter("nombrePrenda");
		String descripcion = req.getParameter("descripcion");
		String precioStr = req.getParameter("precio");
		String categoriaStr = req.getParameter("categoria");
		String colorStr = req.getParameter("color");
		String corteStr = req.getParameter("corte");
		String[] nombreTallas = req.getParameterValues("tallas");

		try {

			int idPrenda = Integer.parseInt(idStr);
			double precio = Double.parseDouble(precioStr);

			// 2. Hablar con el modelo
			Prenda prenda = new Prenda();
			prenda.setIdPrenda(idPrenda);
			prenda.setImagen(imagen);
			prenda.setNombrePrenda(nombre);
			prenda.setDescripcion(descripcion);
			prenda.setPrecio(precio);
			prenda.setCategoria(modelo.entidades.Categoria.valueOf(categoriaStr));
			prenda.setColor(modelo.entidades.Color.valueOf(colorStr));
			prenda.setCorte(modelo.entidades.Corte.valueOf(corteStr));

			List<StockTalla> stockTallas = new ArrayList<>();

			if (nombreTallas != null) {
				for (String tallaStr : nombreTallas) {

					String cantidadStr = req.getParameter("cantidad_" + tallaStr);
					if (cantidadStr == null || cantidadStr.isEmpty()) {
						continue;
					}

					int cantidad = Integer.parseInt(cantidadStr);

					StockTalla stock = new StockTalla(cantidad, Talla.valueOf(tallaStr));

					stockTallas.add(stock);
				}
			}

			prenda.setStockTallas(stockTallas);

			PrendaDAO dao = new PrendaDAO();
			boolean actualizado = dao.actualizar(prenda);

			if (actualizado) {
				req.setAttribute("registroExitoso", true);
			} else {
				req.setAttribute("mensajeError", "No se pudo actualizar la prenda.");
			}

		} catch (Exception e) {
			e.printStackTrace();
			req.setAttribute("mensajeError", "Error al actualizar la prenda.");
		}

		// 3. Llamar a la vista
		listar(req, resp);
	}

}
