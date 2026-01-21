package controlador;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import modelo.dao.PrendaDAO;
import modelo.entidades.Categoria;
import modelo.entidades.Color;
import modelo.entidades.Corte;
import modelo.entidades.Prenda;
import modelo.entidades.StockTalla;
import modelo.entidades.Talla;

@WebServlet("/AgregarPrendaController")
public class AgregarPrendaController extends HttpServlet {

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
		String ruta = (req.getParameter("ruta") != null) ? req.getParameter("ruta") : "agregar";

		switch (ruta) {
		case "agregar":
			this.agregarPrenda(req, resp);
			break;
		case "guardar":
			this.guardar(req, resp);
			break;
		default:
			this.agregarPrenda(req, resp);
			break;
		}
	}

	private void agregarPrenda(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 1. Obtener los parámetros (No hay en la carga inicial)

		// 2. Hablar con el modelo: Obtener listas de Enums (Pasos 1.1 al 1.4 del diagrama)
		req.setAttribute("tallas", Talla.values());
		req.setAttribute("categorias", Categoria.values());
		req.setAttribute("cortes", Corte.values());
		req.setAttribute("colores", Color.values());

		// 3. Llamar a la vista (Paso 1.5 del diagrama)
		req.getRequestDispatcher("jsp/FormularioRegistroPrenda.jsp").forward(req, resp);
	}

	private void guardar(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 1. Obtener los parametros (Paso 2 del diagrama)
		String nombre = req.getParameter("nombrePrenda");
		String descripcion = req.getParameter("descripcion");
		String categoriaStr = req.getParameter("categoria"); 
		String precioStr = req.getParameter("precio");
		String imagen = req.getParameter("imagen");
		String colorStr = req.getParameter("color");
		String corteStr = req.getParameter("corte");
		
		// Parámetros múltiples para Stock
		String[] tallasArr = req.getParameterValues("talla");
		String[] cantidadesArr = req.getParameterValues("cantidad");

		try {
			// 2. Hablar con el modelo (Paso 2.1 del diagrama)
			Prenda p = new Prenda();
			p.setNombrePrenda(nombre);
			p.setDescripcion(descripcion);
			p.setImagen(imagen);
			p.setPrecio(Double.parseDouble(precioStr));
			p.setCategoria(Categoria.valueOf(categoriaStr));
			p.setColor(Color.valueOf(colorStr));
			p.setCorte(Corte.valueOf(corteStr));

			// Procesar Stock (conservamos nuestra implementación)
			List<StockTalla> stockTallas = new ArrayList<>();
			if (tallasArr != null && cantidadesArr != null && tallasArr.length == cantidadesArr.length) {
				for (int i = 0; i < tallasArr.length; i++) {
					try {
						int cantidad = Integer.parseInt(cantidadesArr[i]);
						Talla talla = Talla.valueOf(tallasArr[i]);
						if (cantidad > 0) {
							StockTalla st = new StockTalla(cantidad, talla);
							st.setPrenda(p);
							stockTallas.add(st);
						}
					} catch (Exception e) {
						// Ignorar entradas inválidas manteniendo robustez
					}
				}
			}
			p.setStockTallas(stockTallas);

			PrendaDAO dao = new PrendaDAO();
			boolean exito = dao.insertar(p);

			if (exito) {
				// 3. Llamar a la vista: éxito (Paso 2.2 del diagrama)
				req.setAttribute("registroExitoso", true);
				this.agregarPrenda(req, resp);
			} else {
				// 3. Llamar a la vista: error (Paso 2.3 del diagrama)
				req.setAttribute("mensajeError", "No se pudo registrar la prenda en la base de datos.");
				this.agregarPrenda(req, resp);
			}

		} catch (Exception e) {
			req.setAttribute("mensajeError", "Error en los datos: " + e.getMessage());
			this.agregarPrenda(req, resp);
		}
	}
}