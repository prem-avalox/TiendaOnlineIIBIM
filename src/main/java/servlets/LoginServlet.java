package servlets;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import modelo.Usuario;
import dao.UsuarioDAO;

/**
 * Servlet para manejar el inicio de sesión
 */
@WebServlet("/Login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UsuarioDAO usuarioDAO;
	
	public LoginServlet() {
		super();
		this.usuarioDAO = new UsuarioDAO();
	}
	
	/**
	 * Muestra la página de login
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		request.getRequestDispatcher("/jsp/login.jsp").forward(request, response);
	}
	
	/**
	 * Procesa el formulario de login
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		String correo = request.getParameter("correo");
		String contrasena = request.getParameter("contrasena");
		
		// Validar que los campos no estén vacíos
		if (correo == null || correo.trim().isEmpty() || 
			contrasena == null || contrasena.trim().isEmpty()) {
			request.setAttribute("error", "Complete todos los campos antes de enviar el formulario");
			request.getRequestDispatcher("/jsp/login.jsp").forward(request, response);
			return;
		}
		
		// Validar credenciales
		Usuario usuario = usuarioDAO.validarUsuario(correo, contrasena);
		
		if (usuario != null) {
			// Login exitoso - crear sesión
			HttpSession session = request.getSession();
			session.setAttribute("usuario", usuario);
			session.setAttribute("nombreUsuario", usuario.getNombreCompleto());
			session.setAttribute("rol", usuario.getRol());
			
			// Redirigir según el rol
			if ("ADMIN".equals(usuario.getRol())) {
				response.sendRedirect(request.getContextPath() + "/AdminPrendas");
			} else {
				response.sendRedirect(request.getContextPath() + "/Catalogo");
			}
		} else {
			// Login fallido
			request.setAttribute("error", "Correo o contraseña incorrectos");
			request.getRequestDispatcher("/jsp/login.jsp").forward(request, response);
		}
	}
}
