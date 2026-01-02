package servlets;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import modelo.dao.UsuarioDAO;
import modelo.entidades.Usuario;

/**
 * Servlet para manejar el registro de nuevos usuarios
 */
@WebServlet("/Registro")
public class RegistroServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UsuarioDAO usuarioDAO;
	
	public RegistroServlet() {
		super();
		this.usuarioDAO = new UsuarioDAO();
	}
	
	/**
	 * Muestra la página de registro
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		request.getRequestDispatcher("/jsp/registro.jsp").forward(request, response);
	}
	
	/**
	 * Procesa el formulario de registro
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		String nombreCompleto = request.getParameter("nombreCompleto");
		String correo = request.getParameter("correo");
		String contrasena = request.getParameter("contrasena");
		String confirmarContrasena = request.getParameter("confirmarContrasena");
		
		// Validar que todos los campos estén completos
		if (nombreCompleto == null || nombreCompleto.trim().isEmpty() ||
			correo == null || correo.trim().isEmpty() ||
			contrasena == null || contrasena.trim().isEmpty() ||
			confirmarContrasena == null || confirmarContrasena.trim().isEmpty()) {
			
			request.setAttribute("error", "Complete todos los campos antes de enviar el formulario");
			request.getRequestDispatcher("/jsp/registro.jsp").forward(request, response);
			return;
		}
		
		// Validar que las contraseñas coincidan
		if (!contrasena.equals(confirmarContrasena)) {
			request.setAttribute("error", "Las contraseñas no coinciden");
			request.getRequestDispatcher("/jsp/registro.jsp").forward(request, response);
			return;
		}
		
		// Verificar que el correo no esté registrado
		if (usuarioDAO.existeCorreo(correo)) {
			request.setAttribute("error", "El correo ya se encuentra registrado");
			request.getRequestDispatcher("/jsp/registro.jsp").forward(request, response);
			return;
		}
		
		// Registrar el nuevo usuario
		Usuario nuevoUsuario = new Usuario();
		nuevoUsuario.setNombreCompleto(nombreCompleto);
		nuevoUsuario.setCorreo(correo);
		nuevoUsuario.setContrasena(contrasena); // En producción, usar hash
		nuevoUsuario.setRol("CLIENTE");
		
		boolean registrado = usuarioDAO.registrarUsuario(nuevoUsuario);
		
		if (registrado) {
			// Registro exitoso
			request.setAttribute("mensaje", "Registro exitoso. Ahora puede iniciar sesión");
			request.getRequestDispatcher("/jsp/login.jsp").forward(request, response);
		} else {
			// Error al registrar
			request.setAttribute("error", "Error al registrar el usuario. Intente nuevamente");
			request.getRequestDispatcher("/jsp/registro.jsp").forward(request, response);
		}
	}
}
