package controlador;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import modelo.dao.BolsaDAO;
import modelo.dao.UsuarioDAO;
import modelo.entidades.Bolsa;
import modelo.entidades.ItemBolsa;
import modelo.entidades.Usuario;

/**
 * Controlador para gestionar la visualización de la bolsa de compras
 * Implementa el caso de uso CU11 - Ver Bolsa según diagrama de secuencia
 * 
 * NOTA: Como el CU "Iniciar Sesión" no está implementado, este controlador
 * simula automáticamente una sesión con el usuario "martin" que tiene items en su bolsa.
 */
@WebServlet("/VerBolsaController")
public class VerBolsaController extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private BolsaDAO bolsaDAO = new BolsaDAO();
	private UsuarioDAO usuarioDAO = new UsuarioDAO();
	
	@Override
	public void init() throws ServletException {
		super.init();
		System.out.println("\n" + "=".repeat(60));
		System.out.println("🚀 VerBolsaController INICIALIZADO CORRECTAMENTE");
		System.out.println("   Servlet disponible en: /VerBolsaController");
		System.out.println("=".repeat(60) + "\n");
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("\n" + "=".repeat(60));
		System.out.println("📥 VerBolsaController.doGet() EJECUTADO");
		System.out.println("   Request URI: " + req.getRequestURI());
		System.out.println("   Context Path: " + req.getContextPath());
		System.out.println("=".repeat(60));
		
		String action = req.getParameter("action");
		System.out.println("   Action parameter: " + (action != null ? action : "null (default: abrirBolsa)"));
		
		if (action == null || action.isEmpty()) {
			action = "abrirBolsa";
		}
		
		switch (action) {
			case "abrirBolsa":
				abrirBolsa(req, resp);
				break;
			case "actualizarCantidad":
				actualizarCantidad(req, resp);
				break;
			case "eliminarItem":
				eliminarItem(req, resp);
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
	 * 
	 * SIMULACIÓN DE SESIÓN: Como el login no está implementado, 
	 * se crea automáticamente una sesión con el usuario "martin"
	 */
	private void abrirBolsa(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 1. Obtener o crear la sesión
		HttpSession session = req.getSession(true); // true = crear si no existe
		
		// 2. Verificar si ya hay un usuario en sesión
		Usuario usuario = (Usuario) session.getAttribute("usuario");
		
		// 3. Si NO hay usuario en sesión, simular login con "martin"
		if (usuario == null) {
			// SIMULACIÓN: Buscar usuario "martin" en la base de datos
			usuario = usuarioDAO.buscarPorNombreUsuario("martin");
			
			if (usuario == null) {
				// Si no existe, mostrar error
				req.setAttribute("error", "Usuario 'martin' no encontrado. Ejecuta PoblarBaseDatos.java primero.");
				req.getRequestDispatcher("jsp/error.jsp").forward(req, resp);
				return;
			}
			
			// Guardar usuario en sesión (simula inicio de sesión exitoso)
			session.setAttribute("usuario", usuario);
			System.out.println("✅ Sesión simulada creada para usuario: " + usuario.getNombreUsuario());
		}
		
		// 4. Obtener el contenido de la bolsa
		obtenerContenido(req, resp, usuario);
	}

	/**
	 * Obtiene el contenido de la bolsa del usuario
	 * Según el diagrama: obtiene la bolsa, llama a bolsa.getItems() y calcula totales
	 */
	private void obtenerContenido(HttpServletRequest req, HttpServletResponse resp, Usuario usuario)
			throws ServletException, IOException {
		
		System.out.println("\n🔍 DEBUG - obtenerContenido:");
		System.out.println("   - Usuario ID: " + usuario.getIdUsuario());
		System.out.println("   - Usuario Nombre: " + usuario.getNombreUsuario());
		
		// 1. Buscar la bolsa del usuario usando BolsaDAO (según diagrama UML)
		Bolsa bolsa = bolsaDAO.buscarBolsaPorUsuario(usuario.getIdUsuario());
		
		System.out.println("   - Bolsa encontrada: " + (bolsa != null ? "SÍ (ID: " + bolsa.getIdBolsa() + ")" : "NO"));
		
		if (bolsa == null) {
			// Si no tiene bolsa, mostrar vacía
			System.out.println("   ⚠️  Bolsa es NULL - mostrando vacía");
			presentarLista(req, resp, null, 0.0);
			return;
		}
		
		// 2. Obtener los items de la bolsa (según diagrama: bolsa.getItems())
		List<ItemBolsa> items = bolsa.getItems();
		
		System.out.println("   - Items obtenidos: " + (items != null ? items.size() + " items" : "NULL"));
		
		// DEBUG: Mostrar detalles de cada item
		if (items != null && !items.isEmpty()) {
			System.out.println("   📦 Detalles de items:");
			for (int i = 0; i < items.size(); i++) {
				ItemBolsa item = items.get(i);
				System.out.println("      Item " + (i+1) + ":");
				System.out.println("         - ID: " + item.getIdItem());
				System.out.println("         - Cantidad: " + item.getCantidad());
				System.out.println("         - Talla: " + item.getTallaSeleccionada());
				System.out.println("         - Prenda: " + (item.getPrenda() != null ? item.getPrenda().getNombrePrenda() : "NULL"));
				if (item.getPrenda() != null) {
					System.out.println("         - Precio: $" + item.getPrenda().getPrecio());
					System.out.println("         - Subtotal: $" + item.calcularSubtotal());
				}
			}
		}
		
		// 3. Calcular el monto total usando el método del modelo
		double montoTotal = 0.0;
		
		if (items != null && !items.isEmpty()) {
			// Usar el método calcularSubtotal que delega a cada item
			montoTotal = calcularSubtotal(items);
			System.out.println("   💰 Monto Total Calculado: $" + montoTotal);
			
			// También se podría usar directamente el método del modelo:
			// montoTotal = bolsa.calcularMontoTotal();
		} else {
			System.out.println("   ⚠️  Items vacío o NULL - monto = 0");
		}
		
		// 4. Presentar la lista en la vista
		System.out.println("   → Llamando a presentarLista()");
		presentarLista(req, resp, items, montoTotal);
	}

	/**
	 * Prepara los datos para presentar en la vista
	 * Según el diagrama: sidebarBolsa.presentar(items, montoTotal)
	 * Implementa el flujo alterno: si items > 0, mostrar lista; sino, mostrar mensaje vacío
	 */
	private void presentarLista(HttpServletRequest req, HttpServletResponse resp, List<ItemBolsa> items, 
			double montoTotal) throws ServletException, IOException {
		
		System.out.println("\n📋 DEBUG - presentarLista:");
		System.out.println("   - Items recibidos: " + (items != null ? items.size() : "NULL"));
		System.out.println("   - Monto Total: $" + montoTotal);
		
		// FLUJO ALTERNO del diagrama de secuencia: [items > 0]
		if (items != null && items.size() > 0) {
			// Caso 1: HAY items en la bolsa -> presentar(items, montoTotal)
			System.out.println("   ✅ HAY ITEMS - Configurando atributos para JSP");
			req.setAttribute("items", items);
			req.setAttribute("montoTotal", montoTotal);
			req.setAttribute("cantidadItems", items.size());
			req.setAttribute("bolsaVacia", false);
			
			System.out.println("   → Attributes set:");
			System.out.println("      - items: " + items.size() + " elementos");
			System.out.println("      - montoTotal: $" + montoTotal);
			System.out.println("      - cantidadItems: " + items.size());
			System.out.println("      - bolsaVacia: false");
		} else {
			// Caso 2: NO HAY items -> mostrarMensajeVacio()
			System.out.println("   ⚠️  NO HAY ITEMS - Mostrando bolsa vacía");
			req.setAttribute("items", null);
			req.setAttribute("montoTotal", 0.0);
			req.setAttribute("cantidadItems", 0);
			req.setAttribute("bolsaVacia", true);
		}
		
		// Forward a la vista completa de la bolsa
		System.out.println("   → Forwarding a: jsp/SidebarBolsa.jsp");
		req.getRequestDispatcher("jsp/SidebarBolsa.jsp").forward(req, resp);
		System.out.println("   ✅ Forward completado\n");
	}

	/**
	 * Calcula el monto total de todos los items en la bolsa
	 * Delegado al modelo: usa ItemBolsa.calcularSubtotal() según diseño UML
	 * @param items lista de items en la bolsa
	 * @return monto total
	 */
	private double calcularSubtotal(List<ItemBolsa> items) {
		double total = 0.0;
		
		if (items != null) {
			// Loop del diagrama: por cada item, llamar a calcularSubtotal()
			for (ItemBolsa item : items) {
				// Según UML: itemBolsa.calcularSubtotal()
				total += item.calcularSubtotal();
			}
		}
		
		return total;
	}

	/**
	 * Actualiza la cantidad de un item en la bolsa
	 * Según UML: Bolsa.actualizarCantidad(idItem, cantidad)
	 * Si la cantidad es 0, elimina el item automáticamente
	 */
	private void actualizarCantidad(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		
		HttpSession session = req.getSession(false);
		
		if (session != null && session.getAttribute("usuario") != null) {
			Usuario usuario = (Usuario) session.getAttribute("usuario");
			
			try {
				int idItem = Integer.parseInt(req.getParameter("idItem"));
				int nuevaCantidad = Integer.parseInt(req.getParameter("cantidad"));
				
				System.out.println("🔄 Actualizando cantidad del item " + idItem + " a " + nuevaCantidad);
				
				// Si la cantidad es 0, eliminar el item
				if (nuevaCantidad <= 0) {
					System.out.println("🗑️ Cantidad 0 detectada - eliminando item automáticamente");
					req.setAttribute("idItem", idItem);
					eliminarItem(req, resp);
					return;
				}
				
				// Buscar la bolsa del usuario
				Bolsa bolsa = bolsaDAO.buscarBolsaPorUsuario(usuario.getIdUsuario());
				
				if (bolsa != null && bolsa.getItems() != null) {
					// Buscar el item en la bolsa
					for (ItemBolsa item : bolsa.getItems()) {
						if (item.getIdItem() == idItem) {
							item.setCantidad(nuevaCantidad);
							break;
						}
					}
					
					// Recalcular total
					double nuevoTotal = bolsa.calcularMontoTotal();
					bolsa.setPrecioTotal(nuevoTotal);
					
					// Actualizar en la base de datos
					bolsaDAO.actualizarBolsa(bolsa);
					
					System.out.println("✅ Cantidad actualizada exitosamente. Nuevo total: $" + nuevoTotal);
				}
			} catch (NumberFormatException e) {
				System.err.println("❌ Error al parsear parámetros: " + e.getMessage());
			}
		}
		
		// Recargar la bolsa
		abrirBolsa(req, resp);
	}

	/**
	 * Elimina un item de la bolsa
	 * Según UML: Bolsa.eliminarItem(idItem)
	 */
	private void eliminarItem(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		
		HttpSession session = req.getSession(false);
		
		if (session != null && session.getAttribute("usuario") != null) {
			Usuario usuario = (Usuario) session.getAttribute("usuario");
			
			try {
				int idItem = Integer.parseInt(req.getParameter("idItem"));
				
				System.out.println("🗑️ Eliminando item " + idItem);
				
				// Buscar la bolsa del usuario
				Bolsa bolsa = bolsaDAO.buscarBolsaPorUsuario(usuario.getIdUsuario());
				
				if (bolsa != null && bolsa.getItems() != null) {
					// Buscar y eliminar el item
					bolsa.getItems().removeIf(item -> item.getIdItem() == idItem);
					
					// Recalcular total
					double nuevoTotal = bolsa.calcularMontoTotal();
					bolsa.setPrecioTotal(nuevoTotal);
					
					// Actualizar en la base de datos
					bolsaDAO.actualizarBolsa(bolsa);
					
					System.out.println("✅ Item eliminado exitosamente. Nuevo total: $" + nuevoTotal);
				}
			} catch (NumberFormatException e) {
				System.err.println("❌ Error al parsear parámetros: " + e.getMessage());
			}
		}
		
		// Recargar la bolsa
		abrirBolsa(req, resp);
	}
}
