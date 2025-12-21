package servlets;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import modelo.ItemBolsa;
import modelo.Prenda;
import dao.PrendaDAO;
import dao.TallaDAO;

/**
 * Servlet para gestionar la bolsa de compras
 */
@WebServlet("/Bolsa")
public class BolsaServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private PrendaDAO prendaDAO;
	private TallaDAO tallaDAO;
	
	public BolsaServlet() {
		super();
		this.prendaDAO = new PrendaDAO();
		this.tallaDAO = new TallaDAO();
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		String accion = request.getParameter("accion");
		
		if (accion == null) {
			accion = "ver";
		}
		
		switch (accion) {
			case "agregar":
				agregarItem(request, response);
				break;
			case "eliminar":
				eliminarItem(request, response);
				break;
			case "aumentar":
				aumentarCantidad(request, response);
				break;
			case "disminuir":
				disminuirCantidad(request, response);
				break;
			case "vaciar":
				vaciarBolsa(request, response);
				break;
			default:
				verBolsa(request, response);
				break;
		}
	}
	
	/**
	 * Obtiene o crea la bolsa de compras en la sesión
	 */
	@SuppressWarnings("unchecked")
	private Map<String, ItemBolsa> obtenerBolsa(HttpSession session) {
		Map<String, ItemBolsa> bolsa = (Map<String, ItemBolsa>) session.getAttribute("bolsa");
		if (bolsa == null) {
			bolsa = new HashMap<>();
			session.setAttribute("bolsa", bolsa);
		}
		return bolsa;
	}
	
	/**
	 * Agrega un item a la bolsa
	 */
	private void agregarItem(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		String idPrendaParam = request.getParameter("idPrenda");
		String tallaSeleccionada = request.getParameter("talla");
		
		// Validar parámetros
		if (idPrendaParam == null || tallaSeleccionada == null || tallaSeleccionada.trim().isEmpty()) {
			request.setAttribute("error", "Seleccione una talla antes de agregar a la bolsa");
			doGet(request, response);
			return;
		}
		
		try {
			int idPrenda = Integer.parseInt(idPrendaParam);
			
			// Verificar stock disponible
			int stockDisponible = tallaDAO.verificarStock(idPrenda, tallaSeleccionada);
			
			if (stockDisponible <= 0) {
				request.setAttribute("error", "Talla no disponible en stock");
				response.sendRedirect(request.getContextPath() + "/DetallePrenda?id=" + idPrenda);
				return;
			}
			
			// Obtener información de la prenda
			Prenda prenda = prendaDAO.obtenerPrendaPorId(idPrenda);
			
			if (prenda == null) {
				request.setAttribute("error", "Prenda no encontrada");
				response.sendRedirect(request.getContextPath() + "/Catalogo");
				return;
			}
			
			// Obtener la bolsa de la sesión
			HttpSession session = request.getSession();
			Map<String, ItemBolsa> bolsa = obtenerBolsa(session);
			
			// Crear clave única (idPrenda-talla)
			String key = idPrenda + "-" + tallaSeleccionada;
			
			// Verificar si el item ya existe en la bolsa
			if (bolsa.containsKey(key)) {
				ItemBolsa item = bolsa.get(key);
				
				// Verificar que no exceda el stock
				if (item.getCantidad() < stockDisponible) {
					item.setCantidad(item.getCantidad() + 1);
				} else {
					request.setAttribute("error", "No hay más stock disponible para esta talla");
					response.sendRedirect(request.getContextPath() + "/DetallePrenda?id=" + idPrenda);
					return;
				}
			} else {
				// Crear nuevo item
				ItemBolsa nuevoItem = new ItemBolsa(
					idPrenda,
					prenda.getNombre(),
					prenda.getImagenUrl(),
					tallaSeleccionada,
					prenda.getPrecio(),
					1
				);
				bolsa.put(key, nuevoItem);
			}
			
			// Actualizar la sesión
			session.setAttribute("bolsa", bolsa);
			session.setAttribute("mensaje", "Prenda agregada a la bolsa");
			
			// Redirigir de vuelta al detalle de la prenda
			response.sendRedirect(request.getContextPath() + "/DetallePrenda?id=" + idPrenda);
			
		} catch (NumberFormatException e) {
			response.sendRedirect(request.getContextPath() + "/Catalogo");
		}
	}
	
	/**
	 * Elimina un item de la bolsa
	 */
	private void eliminarItem(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		String key = request.getParameter("key");
		
		if (key != null && !key.trim().isEmpty()) {
			HttpSession session = request.getSession();
			Map<String, ItemBolsa> bolsa = obtenerBolsa(session);
			
			bolsa.remove(key);
			session.setAttribute("bolsa", bolsa);
		}
		
		// Redirigir de vuelta
		String referer = request.getHeader("Referer");
		if (referer != null) {
			response.sendRedirect(referer);
		} else {
			response.sendRedirect(request.getContextPath() + "/Catalogo");
		}
	}
	
	/**
	 * Aumenta la cantidad de un item
	 */
	private void aumentarCantidad(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		String key = request.getParameter("key");
		
		if (key != null && !key.trim().isEmpty()) {
			HttpSession session = request.getSession();
			Map<String, ItemBolsa> bolsa = obtenerBolsa(session);
			
			ItemBolsa item = bolsa.get(key);
			if (item != null) {
				// Verificar stock disponible
				int stockDisponible = tallaDAO.verificarStock(item.getIdPrenda(), item.getTalla());
				
				if (item.getCantidad() < stockDisponible) {
					item.setCantidad(item.getCantidad() + 1);
					session.setAttribute("bolsa", bolsa);
				}
			}
		}
		
		// Redirigir de vuelta
		String referer = request.getHeader("Referer");
		if (referer != null) {
			response.sendRedirect(referer);
		} else {
			response.sendRedirect(request.getContextPath() + "/Catalogo");
		}
	}
	
	/**
	 * Disminuye la cantidad de un item
	 */
	private void disminuirCantidad(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		String key = request.getParameter("key");
		
		if (key != null && !key.trim().isEmpty()) {
			HttpSession session = request.getSession();
			Map<String, ItemBolsa> bolsa = obtenerBolsa(session);
			
			ItemBolsa item = bolsa.get(key);
			if (item != null) {
				if (item.getCantidad() > 1) {
					item.setCantidad(item.getCantidad() - 1);
				} else {
					bolsa.remove(key);
				}
				session.setAttribute("bolsa", bolsa);
			}
		}
		
		// Redirigir de vuelta
		String referer = request.getHeader("Referer");
		if (referer != null) {
			response.sendRedirect(referer);
		} else {
			response.sendRedirect(request.getContextPath() + "/Catalogo");
		}
	}
	
	/**
	 * Vacía completamente la bolsa
	 */
	private void vaciarBolsa(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		Map<String, ItemBolsa> bolsa = new HashMap<>();
		session.setAttribute("bolsa", bolsa);
		
		// Redirigir de vuelta
		String referer = request.getHeader("Referer");
		if (referer != null) {
			response.sendRedirect(referer);
		} else {
			response.sendRedirect(request.getContextPath() + "/Catalogo");
		}
	}
	
	/**
	 * Muestra la vista de la bolsa
	 */
	private void verBolsa(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		Map<String, ItemBolsa> bolsa = obtenerBolsa(session);
		
		// Calcular el subtotal
		BigDecimal subtotal = BigDecimal.ZERO;
		for (ItemBolsa item : bolsa.values()) {
			subtotal = subtotal.add(item.getSubtotal());
		}
		
		request.setAttribute("bolsa", bolsa.values());
		request.setAttribute("subtotal", subtotal);
		request.getRequestDispatcher("/jsp/bolsa.jsp").forward(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		doGet(request, response);
	}
}
