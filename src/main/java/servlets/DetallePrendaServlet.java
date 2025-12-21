package servlets;

import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import modelo.Prenda;
import modelo.Talla;
import dao.PrendaDAO;
import dao.TallaDAO;

/**
 * Servlet para mostrar los detalles de una prenda
 */
@WebServlet("/DetallePrenda")
public class DetallePrendaServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private PrendaDAO prendaDAO;
	private TallaDAO tallaDAO;
	
	public DetallePrendaServlet() {
		super();
		this.prendaDAO = new PrendaDAO();
		this.tallaDAO = new TallaDAO();
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		String idParam = request.getParameter("id");
		
		if (idParam == null || idParam.trim().isEmpty()) {
			response.sendRedirect(request.getContextPath() + "/Catalogo");
			return;
		}
		
		try {
			int idPrenda = Integer.parseInt(idParam);
			
			// Obtener la prenda
			Prenda prenda = prendaDAO.obtenerPrendaPorId(idPrenda);
			
			if (prenda == null) {
				request.setAttribute("error", "Prenda no encontrada");
				response.sendRedirect(request.getContextPath() + "/Catalogo");
				return;
			}
			
			// Obtener las tallas disponibles
			List<Talla> tallas = tallaDAO.listarTallasPorPrenda(idPrenda);
			
			request.setAttribute("prenda", prenda);
			request.setAttribute("tallas", tallas);
			request.getRequestDispatcher("/jsp/detalle-prenda.jsp").forward(request, response);
			
		} catch (NumberFormatException e) {
			response.sendRedirect(request.getContextPath() + "/Catalogo");
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		doGet(request, response);
	}
}
