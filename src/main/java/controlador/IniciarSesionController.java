package controlador;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import modelo.dao.UsuarioDAO;
import modelo.entidades.Usuario;

@WebServlet("/IniciarSesionController")
public class IniciarSesionController extends HttpServlet {
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
		String ruta = (req.getParameter("ruta") != null) ? req.getParameter("ruta") : "logear";

		switch (ruta) {
		case "logear":
			this.iniciarSesion(req, resp);
			break;
		case "ingresar":
			this.enviarCredenciales(req, resp);
			break;
	
		
		}
	}
	
	public void iniciarSesion(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//1. Obtener los parámetros
		
		//2. Hablar con el modelo
		
		//3. Llamar a la vista
		req.getRequestDispatcher("jsp/IniciarSesion.jsp").forward(req, resp);
	}
	
	public void enviarCredenciales(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	    // 1. Obtener los parámetros (Paso 2 del diagrama)
	    String usuario = req.getParameter("usuario");
	    String contraseña = req.getParameter("contraseña");

	    // 2. Hablar con el modelo 
	    UsuarioDAO usuarioDAO = new UsuarioDAO();
	    Usuario userAutenticado = usuarioDAO.autenticar(usuario, contraseña);

	    if (userAutenticado != null) {
	    	// crear sesión: se guarda al usuario para que todo el sistema sepa quién es
	        HttpSession session = req.getSession();
	        session.setAttribute("usuarioLogeado", userAutenticado);
	        
	        if (userAutenticado.isIsAdmin()) {
	        	// 3. Llamar a la vista
	            resp.sendRedirect("GestionarPrendasController?ruta=listar");
	        } else {
	        	// 3. Llamar a la vista
	            resp.sendRedirect("VerCatalogoController?ruta=ingresar");
	        }
	    } else {
	        req.setAttribute("mensajeError", "Credenciales incorrectas. Intente nuevamente.");
	        this.iniciarSesion(req, resp);
	    }
	}
	
}
