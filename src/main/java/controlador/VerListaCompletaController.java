package controlador;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import modelo.dao.PrendaDAO;
import modelo.entidades.Prenda;
@WebServlet("/VerListaCompletaController")
public class VerListaCompletaController extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.ruteador(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.ruteador(req, resp);
	}

	private void ruteador(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String ruta = (req.getParameter("ruta") != null) ? req.getParameter("ruta") : "listar";

		switch (ruta) {
		case "listar":
			this.listarPrendas(req, resp);
			break;

		case "editar":
			this.editar(req, resp);
			break;
			
		case "guardar":
			this.guardar(req, resp);
			break;

		case "confirmar":
			this.confirmarEliminacion(req, resp);
			break;

		default:
			this.listarPrendas(req, resp);
			break;
		}

	}

	private void listarPrendas(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("Entrando al listar del ver lista completa controller");

		// 1. Obtener parámetros (No se requieren filtros para la lista completa del admin)
	    
	    try {
	        // 2. Hablar con el modelo (Paso 1.1 del diagrama)
	        PrendaDAO prendaDAO = new PrendaDAO();
	        List<Prenda> lista = prendaDAO.getListaPrendas();
	        
	        // 3. Pasar los datos a la vista (Paso 1.2 del diagrama)
	        req.setAttribute("prendas", lista);
	        
	    } catch (Exception e) {
	        req.setAttribute("mensajeError", "Error al cargar la lista: " + e.getMessage());
	    }
	    
	    // Llamar a la vista listar_prendas.jsp
	    req.getRequestDispatcher("jsp/lista_prendas.jsp").forward(req, resp);
	}

	private void editar(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	    // 1. Obtener parámetros (el ID de la fila seleccionada en listar_prendas.jsp)
	    String idStr = req.getParameter("id");
	    
	    try {
	        // 2. Hablar con el modelo (Paso 2.1 del diagrama)
	        PrendaDAO prendaDAO = new PrendaDAO();
	        Prenda prenda = prendaDAO.buscarPrenda(idStr); // Reutilizamos el método de búsqueda
	        
	        if (prenda != null) {
	            // Cargamos los datos para la vista (Paso 2.2)
	            req.setAttribute("p", prenda);
	            
	            // Cargamos los Enums para los select y la lista de stock
	            req.setAttribute("categorias", modelo.entidades.Categoria.values());
	            req.setAttribute("tallasDisponibles", modelo.entidades.Talla.values());
	            
	            // 3. Llamar a la vista datos_prenda.jsp
	            req.getRequestDispatcher("jsp/datos_prenda.jsp").forward(req, resp);
	        } 
	    } catch (Exception e) {
	        req.setAttribute("mensajeError", "Error al cargar datos para edición: " + e.getMessage());
	    }
	}
	
	private void guardar(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	    // 1. Obtener parámetros (el ID llega como String desde el campo oculto)
	    String idPrendaStr = req.getParameter("idPrenda");
	    String imagen = req.getParameter("imagen");
	    String nombre = req.getParameter("nombrePrenda");
	    String descripcion = req.getParameter("descripcion");
	    String precioStr = req.getParameter("precio");
	    String categoriaStr = req.getParameter("categoria");
	    String color = req.getParameter("color");
	    String corte = req.getParameter("corte");
	    
	    String[] tallasArr = req.getParameterValues("talla");
	    String[] cantidadesArr = req.getParameterValues("cantidad");

	    try {
	        double precio = Double.parseDouble(precioStr);
	        modelo.entidades.Categoria categoria = modelo.entidades.Categoria.valueOf(categoriaStr);
	        
	        java.util.List<modelo.entidades.StockTalla> listaStock = new java.util.ArrayList<>();
	        if (tallasArr != null && cantidadesArr != null) {
	            for (int i = 0; i < tallasArr.length; i++) {
	                modelo.entidades.StockTalla st = new modelo.entidades.StockTalla();
	                st.setTalla(modelo.entidades.Talla.valueOf(tallasArr[i]));
	                st.setCantidad(Integer.parseInt(cantidadesArr[i]));
	                listaStock.add(st);
	            }
	        }

	        // 2. Hablar con el modelo pasando el ID como String (Paso 3.1 del diagrama)
	        PrendaDAO prendaDAO = new PrendaDAO();
	        prendaDAO.actualizar(idPrendaStr, nombre, descripcion, categoria, precio, listaStock, imagen, color, corte);

	        // 3. Éxito: Activar modal
	        req.setAttribute("registroExitoso", true);
	        this.listarPrendas(req, resp); 

	    } catch (Exception e) {
	        req.setAttribute("mensajeError", "Error al procesar los datos: " + e.getMessage());
	    }
	}

	private void confirmarEliminacion(HttpServletRequest req, HttpServletResponse resp)
	        throws ServletException, IOException {
	    // 1. Obtener parámetros (ID de la prenda y la respuesta del modal)
	    String idStr = req.getParameter("id");
	    String respuesta = req.getParameter("confirm"); // "si" o "no"

	    try {
	        // Bloque Alt: [resp = true] (Si el usuario confirmó en el modal)
	        if ("si".equals(respuesta) && idStr != null) {
	            // 2. Hablar con el modelo (Paso 5.2: eliminar)
	            PrendaDAO dao = new PrendaDAO();
	            boolean eliminado = dao.eliminar(Integer.parseInt(idStr));
	            
	            if (eliminado) {
	                // Paso 5.1: Presentar mensaje de éxito (exito2)
	                req.setAttribute("mensajeExito2", "Prenda eliminada correctamente.");
	            }
	        }
	        
	        // 3. Llamar a la vista (Paso 5.3: presentar lista actualizada)
	        // Tanto si fue "si" como "no", regresamos a la lista completa
	        this.listarPrendas(req, resp);

	    } catch (Exception e) {
	        req.setAttribute("mensajeError", "Error al eliminar: " + e.getMessage());
	        this.listarPrendas(req, resp);
	    }
	}

}
