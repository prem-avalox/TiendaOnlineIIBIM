package controlador;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import modelo.dao.BolsaDAO;
import modelo.dao.ItemBolsaDAO;
import modelo.dao.StockTallaDAO;
import modelo.dao.UsuarioDAO;
import modelo.entidades.Bolsa;
import modelo.entidades.ItemBolsa;
import modelo.entidades.StockTalla;
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
	private StockTallaDAO stockTallaDAO = new StockTallaDAO();
	
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
		System.out.println("   ⚠️  FORZANDO REFRESH COMPLETO DESDE BD");
		
		// 1. Buscar la bolsa del usuario usando BolsaDAO (según diagrama UML)
		// ✅ IMPORTANTE: Esto siempre trae datos frescos de la BD
		Bolsa bolsa = bolsaDAO.buscarBolsaPorUsuario(usuario.getIdUsuario());
		
		System.out.println("   - Bolsa encontrada: " + (bolsa != null ? "SÍ (ID: " + bolsa.getIdBolsa() + ")" : "NO"));
		
		if (bolsa == null) {
			// Si no tiene bolsa, mostrar vacía
			System.out.println("   ⚠️  Bolsa es NULL - mostrando vacía");
			presentarLista(req, resp, Collections.emptyList(), 0.0);
			return;
		}
		
		// 2. Obtener los items de la bolsa (según diagrama: bolsa.getItems())
		// ✅ Los items ya deben estar completamente inicializados por BolsaDAO
		List<ItemBolsa> items = bolsa.getItems();
		
		System.out.println("   - Items obtenidos: " + (items != null ? items.size() + " items" : "NULL"));
		
		// ✅ VERIFICACIÓN ADICIONAL: Si items es null o vacío, intentar recargar
		if (items == null || items.isEmpty()) {
			System.out.println("   ⚠️  Items NULL o vacío - Intentando refresh adicional...");
			// Recargar la bolsa usando el ID para forzar un refresh completo
			bolsa = bolsaDAO.buscarPorId(bolsa.getIdBolsa());
			if (bolsa != null) {
				items = bolsa.getItems();
				System.out.println("   - Items después de refresh: " + (items != null ? items.size() + " items" : "NULL"));
			}
		}
		
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
		
		// Normalizar lista para evitar nulls que dejan el sidebar en estado "Cargando"
		List<ItemBolsa> safeItems = (items != null) ? items : Collections.emptyList();

		// FLUJO ALTERNO del diagrama de secuencia: [items > 0]
		if (!safeItems.isEmpty()) {
			// Caso 1: HAY items en la bolsa -> presentar(items, montoTotal)
			System.out.println("   ✅ HAY ITEMS - Configurando atributos para JSP");
			req.setAttribute("items", safeItems);
			req.setAttribute("montoTotal", montoTotal);
			req.setAttribute("cantidadItems", safeItems.size());
			req.setAttribute("bolsaVacia", false);
			
			System.out.println("   → Attributes set:");
			System.out.println("      - items: " + items.size() + " elementos");
			System.out.println("      - montoTotal: $" + montoTotal);
			System.out.println("      - cantidadItems: " + items.size());
			System.out.println("      - bolsaVacia: false");
		} else {
			// Caso 2: NO HAY items -> mostrarMensajeVacio()
			System.out.println("   ⚠️  NO HAY ITEMS - Mostrando bolsa vacía");
			req.setAttribute("items", Collections.emptyList());
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
	 * INCLUYE validación de stock según diagrama de secuencia 2.3
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
					ItemBolsa itemAActualizar = null;
					for (ItemBolsa item : bolsa.getItems()) {
						if (item.getIdItem() == idItem) {
							itemAActualizar = item;
							break;
						}
					}
					
					if (itemAActualizar == null) {
						System.err.println("❌ Item no encontrado");
						req.setAttribute("error", "Item no encontrado");
						abrirBolsa(req, resp);
						return;
					}
					
					// VALIDACIÓN DE STOCK según diagrama de secuencia 2.3
					System.out.println("🔍 Verificando stock disponible...");
					StockTalla stock = stockTallaDAO.buscarStock(
						itemAActualizar.getPrenda().getIdPrenda(), 
						itemAActualizar.getTallaSeleccionada()
					);
					
					boolean stockDisponible = stockTallaDAO.validarStock(stock, nuevaCantidad);
					
					if (!stockDisponible) {
						System.err.println("❌ STOCK INSUFICIENTE");
						req.setAttribute("error", 
							"Stock insuficiente. Disponible: " + 
							(stock != null ? stock.getCantidad() : 0) + 
							" | Solicitado: " + nuevaCantidad
						);
						abrirBolsa(req, resp);
						return;
					}
					
					System.out.println("✅ Stock disponible - continuando actualización");
					
					// Usar ItemBolsaDAO para actualizar correctamente
					ItemBolsaDAO itemBolsaDAO = new ItemBolsaDAO();
					boolean actualizado = itemBolsaDAO.actualizarCantidad(itemAActualizar, nuevaCantidad);
					
					if (actualizado) {
						// Recargar la bolsa y recalcular total
						bolsa = bolsaDAO.buscarBolsaPorUsuario(usuario.getIdUsuario());
						double nuevoTotal = bolsa.calcularMontoTotal();
						bolsa.setPrecioTotal(nuevoTotal);
						bolsaDAO.actualizarBolsa(bolsa);
						
						System.out.println("✅ Cantidad actualizada exitosamente. Nuevo total: $" + nuevoTotal);
					} else {
						System.err.println("❌ Error al actualizar la cantidad en la base de datos");
					}
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
		
		System.out.println("\n========== ELIMINAR ITEM ==========");
		System.out.println("📋 Sesión activa: " + (session != null));
		
		if (session != null && session.getAttribute("usuario") != null) {
			Usuario usuario = (Usuario) session.getAttribute("usuario");
			System.out.println("👤 Usuario: " + usuario.getNombreUsuario());
			
			try {
				// Obtener idItem del parámetro o del atributo (cuando viene de actualizarCantidad)
				String idItemParam = req.getParameter("idItem");
				if (idItemParam == null) {
					idItemParam = String.valueOf(req.getAttribute("idItem"));
					System.out.println("📝 ID Item obtenido desde atributo: " + idItemParam);
				} else {
					System.out.println("📝 ID Item obtenido desde parámetro: " + idItemParam);
				}
				
				int idItem = Integer.parseInt(idItemParam);
				
				System.out.println("🗑️ Eliminando item con ID: " + idItem);
				
				// Usar ItemBolsaDAO para eliminar correctamente
				ItemBolsaDAO itemBolsaDAO = new ItemBolsaDAO();
				System.out.println("🔧 ItemBolsaDAO creado, ejecutando eliminarItem()...");
				itemBolsaDAO.eliminarItem(idItem);
				System.out.println("✅ Método eliminarItem() del DAO ejecutado");
				
				// IMPORTANTE: Buscar una bolsa FRESCA después de la eliminación
				// Esto asegura que la colección de items esté actualizada
				System.out.println("🔄 Refrescando bolsa desde BD...");
				Bolsa bolsa = bolsaDAO.buscarBolsaPorUsuario(usuario.getIdUsuario());
				
				if (bolsa != null) {
					System.out.println("📦 Bolsa refrescada - ID: " + bolsa.getIdBolsa());
					System.out.println("📊 Items restantes en la bolsa: " + (bolsa.getItems() != null ? bolsa.getItems().size() : 0));
					
					// Recalcular total usando los items actuales
					double nuevoTotal = bolsa.calcularMontoTotal();
					bolsa.setPrecioTotal(nuevoTotal);
					
					// Actualizar en la base de datos
					bolsaDAO.actualizarBolsa(bolsa);
					
					System.out.println("✅ Item eliminado exitosamente");
					System.out.println("💰 Nuevo total de la bolsa: $" + String.format("%.2f", nuevoTotal));
				} else {
					System.err.println("❌ No se encontró la bolsa del usuario");
				}
			} catch (NumberFormatException e) {
				System.err.println("❌ Error al parsear ID del item: " + e.getMessage());
				e.printStackTrace();
			} catch (Exception e) {
				System.err.println("❌ Error inesperado al eliminar item: " + e.getMessage());
				e.printStackTrace();
			}
		} else {
			System.err.println("❌ Sesión no válida o usuario no autenticado");
		}
		
		System.out.println("========== FIN ELIMINAR ITEM ==========\n");
		
		// Recargar la bolsa y mostrar vista actualizada
		abrirBolsa(req, resp);
	}
}
