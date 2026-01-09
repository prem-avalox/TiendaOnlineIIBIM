package controlador;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import modelo.entidades.Bolsa;
import modelo.entidades.ItemBolsa;
import modelo.entidades.Usuario;

/**
 * Controlador para gestionar la visualización de la bolsa de compras
 * Implementa el caso de uso CU11 - Ver Bolsa según diagrama de secuencia
 */
@WebServlet("/VerBolsaController")
public class VerBolsaController extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String action = req.getParameter("action");
		
		if (action == null || action.isEmpty()) {
			action = "abrirBolsa";
		}
		
		switch (action) {
			case "abrirBolsa":
				abrirBolsa(req, resp);
				break;
			case "vaciarBolsa":
				vaciarBolsa(req, resp);
				break;
			default:
				abrirBolsa(req, resp);
				break;
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}

	/**
	 * Método principal que abre la bolsa del usuario
	 * Según el diagrama de secuencia: Usuario -> abrirBolsa() -> VerBolsaController
	 */
	private void abrirBolsa(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		/*
		// 1. Obtener la sesión del usuario
		HttpSession session = req.getSession(false);
		
		if (session == null || session.getAttribute("usuario") == null) {
			// Si no hay sesión, redirigir al login
			resp.sendRedirect(req.getContextPath() + "/login.jsp");
			return;
		}
		
		// 2. Obtener el usuario de la sesión
		Usuario usuario = (Usuario) session.getAttribute("usuario");
		
		// 3. Obtener el contenido de la bolsa
		obtenerContenido(req, resp, usuario);
		*/
	}

	/**
	 * Obtiene el contenido de la bolsa del usuario
	 * Según el diagrama: obtiene la bolsa, llama a bolsa.getItems() y calcula totales
	 */
	private void obtenerContenido(HttpServletRequest req, HttpServletResponse resp, Usuario usuario)
			throws ServletException, IOException {
		/*
		// 1. Obtener la bolsa del usuario (relación JPA OneToOne)
		//Bolsa bolsa = usuario.getBolsa();
		
		if (bolsa == null) {
			// Si no tiene bolsa, mostrar vacía
			presentarLista(req, resp, null, 0.0);
			return;
		}
		
		// 2. Obtener los items de la bolsa (según diagrama: bolsa.getItems())
		List<ItemBolsa> items = bolsa.getItems();
		
		// 3. Calcular el monto total
		double montoTotal = 0.0;
		
		if (items != null && !items.isEmpty()) {
			// Por cada item, calcular subtotal
			montoTotal = calcularSubtotal(items);
		}
		
		// 4. Presentar la lista en la vista
		presentarLista(req, resp, items, montoTotal);*/
	}

	/**
	 * Prepara los datos para presentar en la vista
	 * Según el diagrama: sidebarBolsa.presentar(items, montoTotal)
	 * Implementa el flujo alterno: si items > 0, mostrar lista; sino, mostrar mensaje vacío
	 */
	private void presentarLista(HttpServletRequest req, HttpServletResponse resp, List<ItemBolsa> items, 
			double montoTotal) throws ServletException, IOException {
		
		// FLUJO ALTERNO del diagrama de secuencia: [items > 0]
		if (items != null && items.size() > 0) {
			// Caso 1: HAY items en la bolsa -> presentar(items, montoTotal)
			req.setAttribute("items", items);
			req.setAttribute("montoTotal", montoTotal);
			req.setAttribute("cantidadItems", items.size());
			req.setAttribute("bolsaVacia", false);
		} else {
			// Caso 2: NO HAY items -> mostrarMensajeVacio()
			req.setAttribute("items", null);
			req.setAttribute("montoTotal", 0.0);
			req.setAttribute("cantidadItems", 0);
			req.setAttribute("bolsaVacia", true);
		}
		
		// Forward solo al contenido del sidebar (para carga AJAX)
		req.getRequestDispatcher("jsp/SidebarBolsa.jsp").forward(req, resp);
	}

	/**
	 * Calcula el subtotal de todos los items en la bolsa
	 * Según el diagrama: por cada item, obtiene prenda, cantidad y calcula monto
	 */
	private double calcularSubtotal(List<ItemBolsa> items) {
		double total = 0.0;
		
		if (items != null) {
			// Loop del diagrama: por cada item
			for (ItemBolsa item : items) {
				// Obtener la prenda del item
				if (item.getPrenda() != null) {
					// Calcular: precio × cantidad
					double subtotal = item.getPrenda().getPrecio() * item.getCantidad();
					total += subtotal;
				}
			}
		}
		
		return total;
	}

	/**
	 * Vacía todos los items de la bolsa
	 */
	private void vaciarBolsa(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		/*
		HttpSession session = req.getSession(false);
		
		if (session != null && session.getAttribute("usuario") != null) {
			Usuario usuario = (Usuario) session.getAttribute("usuario");
			Bolsa bolsa = usuario.getBolsa();
			
			if (bolsa != null) {
				// Limpiar la lista de items (JPA manejará el cascade)
				bolsa.getItems().clear();
				bolsa.setPrecioTotal(0.0);
				// Aquí deberías persistir los cambios con tu EntityManager si es necesario
			}
		}
		
		// Redirigir de vuelta a la bolsa
		resp.sendRedirect(req.getContextPath() + "/VerBolsaController?action=abrirBolsa");
		*/
	}
}
