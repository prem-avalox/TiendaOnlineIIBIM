package controlador;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import modelo.dao.UsuarioDAO;
import modelo.entidades.Usuario;

@WebServlet("/RegistrarseController")
public class RegistrarseController extends HttpServlet {

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
		String ruta = (req.getParameter("ruta") != null) ? req.getParameter("ruta") : "crear";

		switch (ruta) {
		case "crear":
			this.registrarse(req, resp);
			break;
		case "enviarFormulario":
			this.enviarCredenciales(req, resp);
			break;
		}
	}
	
	public void registrarse(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//1. Obtener los parámetros
		
		//2. Hablar con el modelo
		
		//3. Llamar a la vista
		req.getRequestDispatcher("jsp/CrearCuenta.jsp").forward(req, resp);
	}
	
	public void enviarCredenciales(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	    // 1. Obtener los parámetros (
	    String nombre = req.getParameter("nombre");
	    String email = req.getParameter("correo");
	    String password = req.getParameter("contraseña");

	    // 2. Hablar con el modelo
	    UsuarioDAO usuarioDAO = new UsuarioDAO();
	    boolean credencialesValidas = usuarioDAO.verificarCredenciales(nombre, email);

	    if (credencialesValidas ) {
	        Usuario nuevoUsuario = new Usuario(nombre, email, password, false); 
	        usuarioDAO.insertar(nuevoUsuario); 
	        
	        // 3. Llamar a la vista
	        resp.sendRedirect("IniciarSesionController?ruta=enviarFormulario");
	    } else {
	        req.setAttribute("mensajeError", "El correo/usuario ya se encuentra registrado"); 
	        this.registrarse(req, resp);
	    }
	}
}
