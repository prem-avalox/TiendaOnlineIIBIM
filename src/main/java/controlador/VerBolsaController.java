package controlador;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import modelo.dao.BolsaDAO;
import modelo.entidades.Bolsa;
import modelo.entidades.Usuario;

@WebServlet("/VerBolsaController")
public class VerBolsaController extends HttpServlet {

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

		String ruta = (req.getParameter("ruta") != null) ? req.getParameter("ruta") : "abrirBolsa";

		switch (ruta) {
		case "abrirBolsa":
			this.verBolsa(req, resp);
			break;

		case "eliminarItem":
			this.eliminarItemBolsa(req, resp);
			break;

		case "ajustarCantidadItem":
			this.ajustarCantidadItem(req, resp);
			break;

		default:
			this.verBolsa(req, resp);
			break;
		}
	}

	/**
	 * Caso de uso: Ver Bolsa Diagrama: Usuario → SidebarBolsa → VerBolsaController
	 * → BolsaDAO
	 */

	private void verBolsa(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		HttpSession session = req.getSession(false);

		// Usuario no logueado → bolsa vacía
		if (session == null || session.getAttribute("usuarioLogeado") == null) {
			req.setAttribute("mensajeError", "Debes iniciar sesión para ver tu bolsa.");
			req.getRequestDispatcher("jsp/SidebarBolsa.jsp").forward(req, resp);
			return;
		}

		try {
			Usuario usuario = (Usuario) session.getAttribute("usuarioLogeado");
			int idUsuario = usuario.getIdUsuario();

			BolsaDAO bolsaDAO = new BolsaDAO();
			Bolsa bolsa = bolsaDAO.getBolsa(idUsuario); // 👈 clave

			if (bolsa == null || bolsa.getItems().isEmpty()) {
				req.setAttribute("mensajeError", "Tu bolsa está vacía.");
			} else {
				req.setAttribute("bolsa", bolsa);
			}

		} catch (Exception e) {
			e.printStackTrace();
			req.setAttribute("mensajeError", "Error al cargar la bolsa.");
		}

		req.getRequestDispatcher("jsp/SidebarBolsa.jsp").forward(req, resp);
	}

	private void eliminarItemBolsa(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		try {
			HttpSession session = req.getSession(false);
			if (session == null || session.getAttribute("usuarioLogeado") == null) {
				return;
			}

			int idItem = Integer.parseInt(req.getParameter("idItem"));

			BolsaDAO bolsaDAO = new BolsaDAO();
			boolean eliminado = bolsaDAO.eliminarItem(idItem);

			if (!eliminado) {
				req.setAttribute("mensajeError", "No se pudo eliminar el item");
			}

			Usuario usuario = (Usuario) session.getAttribute("usuarioLogeado");
			Bolsa bolsa = bolsaDAO.getBolsa(usuario.getIdUsuario());

			req.setAttribute("bolsa", bolsa);

		} catch (Exception e) {
			e.printStackTrace();
			req.setAttribute("mensajeError", "Error al eliminar el item");
		}

		req.getRequestDispatcher("jsp/SidebarBolsa.jsp").forward(req, resp);
	}

	private void ajustarCantidadItem(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			// 1. Obtener parámetros del request
			int idItem = Integer.parseInt(req.getParameter("idItem"));
			int nuevaCantidad = Integer.parseInt(req.getParameter("nuevaCantidad"));

			// VALIDACIÓN DE SEGURIDAD: Si por algún error llega 0 o negativo, forzamos a 1
	        if (nuevaCantidad < 1) {
	            nuevaCantidad = 1;
	        }
	        
			HttpSession session = req.getSession(false);
			if (session == null || session.getAttribute("usuarioLogeado") == null) {
				return;
			}

			// 1.1 Invocamos al DAO para ajustar la cantidad (según el diagrama)
			BolsaDAO bolsaDAO = new BolsaDAO();
			boolean stockDisponible = bolsaDAO.ajustarItem(idItem, nuevaCantidad);

			// Bloque 'alt' del diagrama: presentar resultado
			Usuario usuario = (Usuario) session.getAttribute("usuarioLogeado");

			if (!stockDisponible) {
				// Caso stockDisponible = false: presentar mensaje de error
				req.setAttribute("mensajeError", "No hay stock suficiente disponible.");
			}

			// En ambos casos refrescamos la bolsa para mostrar los datos actuales
			Bolsa bolsa = bolsaDAO.getBolsa(usuario.getIdUsuario());
			req.setAttribute("bolsa", bolsa);

		} catch (Exception e) {
			e.printStackTrace();
			req.setAttribute("mensajeError", "Error al ajustar la cantidad.");
		}

		// Retornamos el fragmento del Sidebar para que el AJAX lo inyecte sin salir de
		// la página
		req.getRequestDispatcher("jsp/SidebarBolsa.jsp").forward(req, resp);
	}
}
