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
@WebServlet("/GestionarPrendasController")
public class GestionarPrendasController extends HttpServlet {

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
		
		// 1. Obtener parámetros 
		
	    try {
	        // 2. Hablar con el modelo 
	        PrendaDAO prendaDAO = new PrendaDAO();
	        List<Prenda> lista = prendaDAO.getListaPrendas();
	        
	        req.setAttribute("prendas", lista);
	        
	    } catch (Exception e) {
	        req.setAttribute("mensajeError", "Error al cargar la lista: " + e.getMessage());
	    }
	    
	    //3. Llamar a la vista listar_prendas.jsp
	    req.getRequestDispatcher("jsp/ListaPrendas.jsp").forward(req, resp);
	}

	private void editar(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	    // 1. Obtener parámetros
	    String idStr = req.getParameter("id");
	    
	    try {
	        // 2. Hablar con el modelo
	        PrendaDAO prendaDAO = new PrendaDAO();
	        Prenda prenda = prendaDAO.buscarPrenda(idStr); 
	        
	        if (prenda != null) {
	            req.setAttribute("p", prenda);
	            
	            req.setAttribute("categorias", modelo.entidades.Categoria.values());
	            req.setAttribute("tallasDisponibles", modelo.entidades.Talla.values());
	            
	            req.getRequestDispatcher("jsp/DatosPrenda.jsp").forward(req, resp);
	        } 
	    } catch (Exception e) {
	        req.setAttribute("mensajeError", "Error al cargar datos para edición: " + e.getMessage());
	    }
	}
	
	private void guardar(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	    // 1. Obtener parámetros 
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

	        // 2. Hablar con el modelo 
	        PrendaDAO prendaDAO = new PrendaDAO();
	        prendaDAO.actualizar(idPrendaStr, nombre, descripcion, categoria, precio, listaStock, imagen, color, corte);

	        // 3. El controlador presenta la lista
	        req.setAttribute("registroExitoso", true);
	        this.listarPrendas(req, resp); 

	    } catch (Exception e) {
	        req.setAttribute("mensajeError", "Error al procesar los datos: " + e.getMessage());
	    }
	}

	private void confirmarEliminacion(HttpServletRequest req, HttpServletResponse resp)
	        throws ServletException, IOException {
	    // 1. Obtener parámetros 
	    String idStr = req.getParameter("id");
	    String respuesta = req.getParameter("confirm"); 

	    try {
	        if ("si".equals(respuesta) && idStr != null) {
	            // 2. Hablar con el modelo 
	            PrendaDAO dao = new PrendaDAO();
	            boolean eliminado = dao.eliminar(Integer.parseInt(idStr));
	            
	            if (eliminado) {
	                
	                req.setAttribute("mensajeExito2", "Prenda eliminada correctamente.");
	            }
	        }
	        
	        // 3. Llamar a la vista 
	        this.listarPrendas(req, resp);

	    } catch (Exception e) {
	        req.setAttribute("mensajeError", "Error al eliminar: " + e.getMessage());
	        this.listarPrendas(req, resp);
	    }
	}

}
