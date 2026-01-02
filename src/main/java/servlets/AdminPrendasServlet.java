package servlets;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import modelo.dao.PrendaDAO;
import modelo.dao.TallaDAO;
import modelo.entidades.Prenda;
import modelo.entidades.Talla;

/**
 * Servlet para la gestión administrativa de prendas (CRUD)
 */
@WebServlet("/AdminPrendas")
public class AdminPrendasServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private PrendaDAO prendaDAO;
	private TallaDAO tallaDAO;
	
	public AdminPrendasServlet() {
		super();
		this.prendaDAO = new PrendaDAO();
		this.tallaDAO = new TallaDAO();
	}
	
	/**
	 * Verifica que el usuario sea administrador
	 */
	private boolean esAdministrador(HttpSession session) {
		String rol = (String) session.getAttribute("rol");
		return "ADMIN".equals(rol);
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		// Verificar que el usuario sea administrador
		HttpSession session = request.getSession();
		if (!esAdministrador(session)) {
			response.sendRedirect(request.getContextPath() + "/Login");
			return;
		}
		
		String accion = request.getParameter("accion");
		
		if (accion == null) {
			accion = "listar";
		}
		
		switch (accion) {
			case "nuevo":
				mostrarFormularioNuevo(request, response);
				break;
			case "editar":
				mostrarFormularioEditar(request, response);
				break;
			case "eliminar":
				confirmarEliminacion(request, response);
				break;
			default:
				listarPrendas(request, response);
				break;
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		// Verificar que el usuario sea administrador
		HttpSession session = request.getSession();
		if (!esAdministrador(session)) {
			response.sendRedirect(request.getContextPath() + "/Login");
			return;
		}
		
		String accion = request.getParameter("accion");
		
		if (accion == null) {
			accion = "listar";
		}
		
		switch (accion) {
			case "guardar":
				guardarPrenda(request, response);
				break;
			case "actualizar":
				actualizarPrenda(request, response);
				break;
			case "eliminarConfirmado":
				eliminarPrenda(request, response);
				break;
			default:
				listarPrendas(request, response);
				break;
		}
	}
	
	/**
	 * Lista todas las prendas en la vista de administración
	 */
	private void listarPrendas(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		List<Prenda> prendas = prendaDAO.listarPrendas();
		
		request.setAttribute("prendas", prendas);
		request.getRequestDispatcher("/jsp/admin/admin-prendas.jsp").forward(request, response);
	}
	
	/**
	 * Muestra el formulario para agregar una nueva prenda
	 */
	private void mostrarFormularioNuevo(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		request.setAttribute("accion", "guardar");
		request.getRequestDispatcher("/jsp/admin/form-prenda.jsp").forward(request, response);
	}
	
	/**
	 * Muestra el formulario para editar una prenda existente
	 */
	private void mostrarFormularioEditar(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		String idParam = request.getParameter("id");
		
		if (idParam == null || idParam.trim().isEmpty()) {
			response.sendRedirect(request.getContextPath() + "/AdminPrendas");
			return;
		}
		
		try {
			int idPrenda = Integer.parseInt(idParam);
			Prenda prenda = prendaDAO.obtenerPrendaPorId(idPrenda);
			
			if (prenda == null) {
				request.setAttribute("error", "Prenda no encontrada");
				listarPrendas(request, response);
				return;
			}
			
			// Obtener las tallas de la prenda
			List<Talla> tallas = tallaDAO.listarTallasPorPrenda(idPrenda);
			
			request.setAttribute("prenda", prenda);
			request.setAttribute("tallas", tallas);
			request.setAttribute("accion", "actualizar");
			request.getRequestDispatcher("/jsp/admin/form-prenda.jsp").forward(request, response);
			
		} catch (NumberFormatException e) {
			response.sendRedirect(request.getContextPath() + "/AdminPrendas");
		}
	}
	
	/**
	 * Guarda una nueva prenda
	 */
	private void guardarPrenda(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		// Obtener datos del formulario
		String nombre = request.getParameter("nombre");
		String descripcion = request.getParameter("descripcion");
		String categoria = request.getParameter("categoria");
		String precioParam = request.getParameter("precio");
		String imagenUrl = request.getParameter("imagenUrl");
		String color = request.getParameter("color");
		String tipoAjuste = request.getParameter("tipoAjuste");
		
		// Validar campos obligatorios
		if (nombre == null || nombre.trim().isEmpty() ||
			categoria == null || categoria.trim().isEmpty() ||
			precioParam == null || precioParam.trim().isEmpty()) {
			
			request.setAttribute("error", "Complete todos los campos obligatorios");
			request.setAttribute("accion", "guardar");
			request.getRequestDispatcher("/jsp/admin/form-prenda.jsp").forward(request, response);
			return;
		}
		
		try {
			BigDecimal precio = new BigDecimal(precioParam);
			
			// Crear objeto Prenda
			Prenda prenda = new Prenda();
			prenda.setNombre(nombre);
			prenda.setDescripcion(descripcion);
			prenda.setCategoria(categoria);
			prenda.setPrecio(precio);
			prenda.setImagenUrl(imagenUrl);
			prenda.setColor(color);
			prenda.setTipoAjuste(tipoAjuste);
			
			// Guardar en la base de datos
			boolean guardado = prendaDAO.agregarPrenda(prenda);
			
			if (guardado) {
				request.setAttribute("mensaje", "Prenda agregada correctamente");
			} else {
				request.setAttribute("error", "Error al agregar la prenda");
			}
			
			listarPrendas(request, response);
			
		} catch (NumberFormatException e) {
			request.setAttribute("error", "Precio inválido");
			request.setAttribute("accion", "guardar");
			request.getRequestDispatcher("/jsp/admin/form-prenda.jsp").forward(request, response);
		}
	}
	
	/**
	 * Actualiza una prenda existente
	 */
	private void actualizarPrenda(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		String idParam = request.getParameter("id");
		String nombre = request.getParameter("nombre");
		String descripcion = request.getParameter("descripcion");
		String categoria = request.getParameter("categoria");
		String precioParam = request.getParameter("precio");
		String imagenUrl = request.getParameter("imagenUrl");
		String color = request.getParameter("color");
		String tipoAjuste = request.getParameter("tipoAjuste");
		
		// Validar campos obligatorios
		if (idParam == null || idParam.trim().isEmpty() ||
			nombre == null || nombre.trim().isEmpty() ||
			categoria == null || categoria.trim().isEmpty() ||
			precioParam == null || precioParam.trim().isEmpty()) {
			
			request.setAttribute("error", "Complete todos los campos obligatorios");
			request.setAttribute("accion", "actualizar");
			request.getRequestDispatcher("/jsp/admin/form-prenda.jsp").forward(request, response);
			return;
		}
		
		try {
			int idPrenda = Integer.parseInt(idParam);
			BigDecimal precio = new BigDecimal(precioParam);
			
			// Crear objeto Prenda
			Prenda prenda = new Prenda();
			prenda.setIdPrenda(idPrenda);
			prenda.setNombre(nombre);
			prenda.setDescripcion(descripcion);
			prenda.setCategoria(categoria);
			prenda.setPrecio(precio);
			prenda.setImagenUrl(imagenUrl);
			prenda.setColor(color);
			prenda.setTipoAjuste(tipoAjuste);
			
			// Actualizar en la base de datos
			boolean actualizado = prendaDAO.actualizarPrenda(prenda);
			
			if (actualizado) {
				request.setAttribute("mensaje", "Prenda actualizada correctamente");
			} else {
				request.setAttribute("error", "Error al actualizar la prenda");
			}
			
			listarPrendas(request, response);
			
		} catch (NumberFormatException e) {
			request.setAttribute("error", "Datos inválidos");
			request.setAttribute("accion", "actualizar");
			request.getRequestDispatcher("/jsp/admin/form-prenda.jsp").forward(request, response);
		}
	}
	
	/**
	 * Muestra confirmación para eliminar una prenda
	 */
	private void confirmarEliminacion(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		String idParam = request.getParameter("id");
		
		if (idParam == null || idParam.trim().isEmpty()) {
			response.sendRedirect(request.getContextPath() + "/AdminPrendas");
			return;
		}
		
		try {
			int idPrenda = Integer.parseInt(idParam);
			Prenda prenda = prendaDAO.obtenerPrendaPorId(idPrenda);
			
			if (prenda == null) {
				request.setAttribute("error", "Prenda no encontrada");
				listarPrendas(request, response);
				return;
			}
			
			request.setAttribute("prenda", prenda);
			request.getRequestDispatcher("/jsp/admin/confirmar-eliminar.jsp").forward(request, response);
			
		} catch (NumberFormatException e) {
			response.sendRedirect(request.getContextPath() + "/AdminPrendas");
		}
	}
	
	/**
	 * Elimina una prenda de la base de datos
	 */
	private void eliminarPrenda(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		String idParam = request.getParameter("id");
		
		if (idParam == null || idParam.trim().isEmpty()) {
			response.sendRedirect(request.getContextPath() + "/AdminPrendas");
			return;
		}
		
		try {
			int idPrenda = Integer.parseInt(idParam);
			
			// Primero eliminar las tallas asociadas
			tallaDAO.eliminarTallasPorPrenda(idPrenda);
			
			// Luego eliminar la prenda
			boolean eliminado = prendaDAO.eliminarPrenda(idPrenda);
			
			if (eliminado) {
				request.setAttribute("mensaje", "Prenda eliminada correctamente");
			} else {
				request.setAttribute("error", "Error al eliminar la prenda");
			}
			
			listarPrendas(request, response);
			
		} catch (NumberFormatException e) {
			response.sendRedirect(request.getContextPath() + "/AdminPrendas");
		}
	}
}
