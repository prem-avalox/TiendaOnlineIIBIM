package controlador;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import modelo.entidades.Usuario;
import modelo.dao.BolsaDAO;
import modelo.dao.PrendaDAO;
import modelo.entidades.Prenda;
import modelo.entidades.Talla;

@WebServlet("/AgregarItemBolsaController")
public class AgregarItemBolsaController extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.ruteador(req, resp);
	}

	private void ruteador(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String ruta = (req.getParameter("ruta") != null) ? req.getParameter("ruta") : "";

		if ("agregarABolsa".equals(ruta)) {
			this.agregarABolsa(req, resp);
		}
	}

	/**
	 * Caso de uso: Agregar prenda a la bolsa
	 */
	private void agregarABolsa(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		// 0. Validar sesión
		HttpSession session = req.getSession(false);

		if (session == null || session.getAttribute("usuarioLogeado") == null) {
			req.setAttribute("mensajeError", "Debe iniciar sesión para agregar productos a la bolsa.");
			req.getRequestDispatcher("jsp/IniciarSesion.jsp").forward(req, resp);
			return;
		}

		Usuario usuario = (Usuario) session.getAttribute("usuarioLogeado");
		int idUsuario = usuario.getIdUsuario();

		// 1. Obtener parámetros
		String idPrendaStr = req.getParameter("idPrenda");
		String idTallaStr = req.getParameter("idTalla");
		String cantidadStr = req.getParameter("cantidad");

		try {
			int idPrenda = Integer.parseInt(idPrendaStr);
			int cantidad = Integer.parseInt(cantidadStr);
			Talla talla = Talla.valueOf(idTallaStr);

			// 2. Hablar con el modelo
			PrendaDAO prendaDAO = new PrendaDAO();
			Prenda prenda = prendaDAO.getPrenda(idPrenda);

			if (prenda == null) {
				req.setAttribute("mensajeStock", "La prenda no existe.");
				req.getRequestDispatcher("jsp/Prenda.jsp").forward(req, resp);
				return;
			}

			BolsaDAO bolsaDAO = new BolsaDAO();
			boolean agregado = bolsaDAO.agregarItemABolsa(idUsuario, prenda, talla, cantidad);

			// ❌ NO HAY STOCK → volver a la prenda con mensaje
			if (!agregado) {
				req.setAttribute("mensajeStock", "No hay stock disponible para la talla seleccionada.");
				req.setAttribute("prenda", prenda);
				req.setAttribute("tallas", Talla.values());
				req.getRequestDispatcher("jsp/Prenda.jsp").forward(req, resp);
				return;
			}

			// ✅ ÉXITO → usar session SOLO para éxito
			session.setAttribute("itemAgregado", true);

			// 3. Redirigir al detalle
			resp.sendRedirect(
					req.getContextPath() + "/VerCatalogoController?ruta=visualizarPrenda&idPrenda=" + idPrenda);

		} catch (Exception e) {
			e.printStackTrace();
			req.setAttribute("mensajeStock", "Error al agregar el producto.");
			req.getRequestDispatcher("jsp/Prenda.jsp").forward(req, resp);
		}
	}

}
