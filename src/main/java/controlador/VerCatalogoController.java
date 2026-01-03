package controlador;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import modelo.dao.PrendaDAO;
import modelo.entidades.Categoria;
import modelo.entidades.Prenda;

@WebServlet("/VerCatalogoController")
public class VerCatalogoController extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.ruteador(req, resp);

	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.ruteador(req, resp);
	}

	private void ruteador(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String ruta = (req.getParameter("ruta") != null)? req.getParameter("ruta"): "listar";

        switch (ruta) {
            case "listar":
                this.listar(req, resp);
                break;

            case "buscar":
                this.buscarPrenda(req, resp);
                break;

            case "filtros":
                this.seleccionarFiltros(req, resp);
                break;

            case "categorias":
                this.desplegarCategorias(req, resp);
                break;

            case "categoria":
                this.seleccionarCategoria(req, resp);
                break;

            case "detalle":
                this.seleccionarPrenda(req, resp);
                break;

            default:
                this.listar(req, resp);
                break;
        }
    }


	
	private void listar(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("Entrando al listar del VerCatalogoController");
		// 1. Obtener los parametros
		
		try {
	        // 2. hablar con el modelo
	        PrendaDAO prendaDAO = new PrendaDAO();
	        List<Prenda> prendas = prendaDAO.getListaPrendas();
	        
	        // Validar si se obtuvieron resultados
	        if (prendas != null && !prendas.isEmpty()) {
	            // Paso 1.2: listar prendas
	            req.setAttribute("prendas", prendas);
	        } else {
	            // Paso 1.3: presentar mensaje de error si la lista falla
	            req.setAttribute("mensajeError", "No hay prendas.");
	        }
	    } catch (Exception e) {
	        // Paso 1.3: Captura de excepción y envío de mensaje
	        req.setAttribute("mensajeError", "Error interno al procesar el catálogo: " + e.getMessage());
	    }

	    // 3. Llamar a la vista (siempre a catalogo.jsp)
	    req.getRequestDispatcher("jsp/catalogo.jsp").forward(req, resp);
	}

	private void buscarPrenda(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("entrando al buscarPrenda del contoller catalogo");
		// 1. Obtener los parametros
		String nombreBusqueda = req.getParameter("nombre");
		try {
			// 2. Hablar con el modelo
			PrendaDAO prendaDAO = new PrendaDAO();
			List<Prenda> prendasEncontradas = prendaDAO.getListaPrendas(nombreBusqueda);
			
			//validar el resultado
			if(prendasEncontradas != null && !prendasEncontradas.isEmpty()) {
				req.setAttribute("prendas", prendasEncontradas);
			} else {
				req.setAttribute("mensajeError", "No se encontraron prendas con el nombre: " + nombreBusqueda);
			}

		} catch (Exception e) {
			req.setAttribute("mensajeError", "Error interno al buscar prendas: " + e.getMessage());
		}
		// 3. Llamar a la vista
		req.getRequestDispatcher("jsp/catalogo.jsp").forward(req, resp);
}

	private void seleccionarFiltros(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		System.out.println("entrando a seleccionarFiltros del controller catalogo");
		// 1. Obtener los parametros
		String talla = req.getParameter("talla");
		String color = req.getParameter("color");
		String corte = req.getParameter("corte");
		
		try {
	        // 2. Hablar con el modelo 
	        PrendaDAO prendaDAO = new PrendaDAO();
	        List<Prenda> prendasFiltradas = prendaDAO.filtrarPrenda(talla, color, corte);
	        
	        //validar el resultado
	        if (prendasFiltradas != null && !prendasFiltradas.isEmpty()) {
	            req.setAttribute("prendas", prendasFiltradas); 
	        } else {
	            req.setAttribute("mensajeError", "No hay prendas que coincidan con los filtros seleccionados.");
	        }
	    } catch (Exception e) {
	        req.setAttribute("mensajeError", "Error al aplicar filtros: " + e.getMessage());
	    }
		
		// 3. Llamar a la vista
		req.getRequestDispatcher("jsp/catalogo.jsp").forward(req, resp);
	}

	private void desplegarCategorias(HttpServletRequest req, HttpServletResponse resp)
	        throws ServletException, IOException {
	    System.out.println("entrando a desplegar categoria del controller catalogo");
	    //1. Obtener los parametros
	    
	    // 2. Hablar con el modelo para las categorías 
	    Categoria[] categorias = Categoria.values();
	    req.setAttribute("categorias", categorias);
	    
	    //cargar las prendas para que la vista no quede vacía
	    PrendaDAO prendaDAO = new PrendaDAO();
	    req.setAttribute("prendas", prendaDAO.getListaPrendas()); 
	    
	    // Activar el checkbox del sidebar desde el servidor
	    req.setAttribute("menuAbierto", "checked");

	    //3. Llamar a la vista 
	    req.getRequestDispatcher("jsp/catalogo.jsp").forward(req, resp);	
	}

	private void seleccionarCategoria(HttpServletRequest req, HttpServletResponse resp)
	        throws ServletException, IOException {
	    System.out.println("entrando a seleccionarCategoria del controlador");	    
	    // . Obtener el parámetro 
	    String idCategoriaStr = req.getParameter("id");

	    try {
	        // 2. Hablar con el modelo
	        PrendaDAO prendaDAO = new PrendaDAO();
	        List<Prenda> prendasFiltradas = prendaDAO.buscarPrendas(idCategoriaStr);

	        // validar el resultado
	        if (prendasFiltradas != null && !prendasFiltradas.isEmpty()) {
	            req.setAttribute("prendas", prendasFiltradas);
	        } else {
	            req.setAttribute("mensajeError", "No hay prendas en la categoría seleccionada.");
	        }
	        
	        // Recargar categorías para el sidebar
	        //req.setAttribute("categorias", Categoria.values());

	    } catch (Exception e) {
	        req.setAttribute("mensajeError", "Error al procesar la categoría: " + e.getMessage());
	    }

	    // 3. Llamar a la vista 
	    req.getRequestDispatcher("jsp/catalogo.jsp").forward(req, resp);
	}
	
	private void seleccionarPrenda(HttpServletRequest req, HttpServletResponse resp)
	        throws ServletException, IOException {
	    // 1. Obtener el parámetro tal cual viene (String)
	    String idStr = req.getParameter("id");
	    
	    try {
	        // 2. Delegar la búsqueda al DAO pasando el String
	        PrendaDAO prendaDAO = new PrendaDAO();
	        Prenda prendaEncontrada = prendaDAO.buscarPrenda(idStr);
	        
	        if (prendaEncontrada != null) {
	            req.setAttribute("prenda", prendaEncontrada); // 
	        } else {
	            req.setAttribute("mensajeError", "La prenda no existe.");
	        }
	    } catch (Exception e) {
	        req.setAttribute("mensajeError", "Error al cargar el detalle: " + e.getMessage());
	    }

	    // 3. Llamar a la vista
	    req.getRequestDispatcher("jsp/prenda.jsp").forward(req, resp);
	}
}
