package controlador;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/VerCatalogoController")
public class VerCatalogoController extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

	}

	private void listar(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 1. Obtener los parametros
		// 2. Hablar con el modelo
		// 3. Llamar a la vista
	}

	private void buscarPrenda(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 1. Obtener los parametros
		// 2. Hablar con el modelo
		// 3. Llamar a la vista
	}

	private void seleccionarFlitros(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// 1. Obtener los parametros
		// 2. Hablar con el modelo
		// 3. Llamar a la vista
	}

	private void desplegarCategorias(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// 1. Obtener los parametros
		// 2. Hablar con el modelo
		// 3. Llamar a la vista
	}

	private void seleccionarCategoria(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// 1. Obtener los parametros
		// 2. Hablar con el modelo
		// 3. Llamar a la vista
	}

	private void seleccionarPrenda(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// 1. Obtener los parametros
		// 2. Hablar con el modelo
		// 3. Llamar a la vista
	}

}
