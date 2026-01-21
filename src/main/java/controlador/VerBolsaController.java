package controlador;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import modelo.dao.BolsaDAO;
import modelo.dao.ItemBolsaDAO;
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
	private ItemBolsaDAO itemBolsaDAO = new ItemBolsaDAO();
	
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
				solicitarActualizarCantidad(req, resp);
				break;
			case "eliminarItem":
				solicitarEliminarItem(req, resp);
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
		Bolsa bolsa = bolsaDAO.buscarBolsaPorUsuario(usuario.getIdUsuario());
		
		if (bolsa == null) {
			req.setAttribute("items", java.util.Collections.emptyList());
			req.setAttribute("montoTotal", 0.0);
			req.setAttribute("cantidadItems", 0);
			req.setAttribute("bolsaVacia", true);
		} else {
			double montoTotal = bolsa.calcularMontoTotal();
			bolsa.setPrecioTotal(montoTotal);
			bolsaDAO.actualizarBolsa(bolsa);
			req.setAttribute("items", bolsa.getItems());
			req.setAttribute("montoTotal", montoTotal);
			req.setAttribute("cantidadItems", bolsa.getItems() != null ? bolsa.getItems().size() : 0);
			req.setAttribute("bolsaVacia", bolsa.getItems() == null || bolsa.getItems().isEmpty());
		}
		req.getRequestDispatcher("jsp/SidebarBolsa.jsp").forward(req, resp);
	}

	/**
	 * Actualiza la cantidad de un item en la bolsa
	 * Según UML: Bolsa.actualizarCantidad(idItem, cantidad)
	 * INCLUYE validación de stock según diagrama de secuencia 2.3
	 * Si la cantidad es 0, elimina el item automáticamente
	 */
	private void solicitarActualizarCantidad(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String idItemStr = req.getParameter("idItem");
        String cantidadStr = req.getParameter("cantidad");
        if (idItemStr == null || cantidadStr == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        int idItem = Integer.parseInt(idItemStr);
        int cantidad = Integer.parseInt(cantidadStr);

        // Si la cantidad es 0 o menor, elimina directamente usando el método existente
        if (cantidad <= 0) {
            solicitarEliminarItem(req, resp);
            return;
        }

        ItemBolsa item = new ItemBolsa();
        item.setIdItem(idItem);
        itemBolsaDAO.actualizarCantidad(item, cantidad);

        // Recalcular total de la bolsa tras la actualización
        HttpSession session = req.getSession(false);
        if (session != null) {
            Usuario usuario = (Usuario) session.getAttribute("usuario");
            if (usuario != null) {
                Bolsa bolsa = bolsaDAO.buscarBolsaPorUsuario(usuario.getIdUsuario());
                if (bolsa != null) {
                    double montoTotal = bolsa.calcularMontoTotal();
                    bolsa.setPrecioTotal(montoTotal);
                    bolsaDAO.actualizarBolsa(bolsa);
                }
            }
        }
        abrirBolsa(req, resp);
    }

	/**
	 * Elimina un item de la bolsa
	 * Según UML: Bolsa.eliminarItem(idItem)
	 */
	private void solicitarEliminarItem(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String idItemStr = req.getParameter("idItem");
        if (idItemStr == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        int idItem = Integer.parseInt(idItemStr);
        itemBolsaDAO.eliminarItem(idItem);

        // Recalcular total de la bolsa tras eliminar
        HttpSession session = req.getSession(false);
        if (session != null) {
            Usuario usuario = (Usuario) session.getAttribute("usuario");
            if (usuario != null) {
                Bolsa bolsa = bolsaDAO.buscarBolsaPorUsuario(usuario.getIdUsuario());
                if (bolsa != null) {
                    double montoTotal = bolsa.calcularMontoTotal();
                    bolsa.setPrecioTotal(montoTotal);
                    bolsaDAO.actualizarBolsa(bolsa);
                    req.setAttribute("bolsaVacia", bolsa.getItems() == null || bolsa.getItems().isEmpty());
                }
            }
        }
        abrirBolsa(req, resp);
    }
}
