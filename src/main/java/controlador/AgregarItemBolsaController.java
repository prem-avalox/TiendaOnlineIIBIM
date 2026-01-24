package controlador;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import modelo.dao.BolsaDAO;
import modelo.dao.PrendaDAO;
import modelo.entidades.Prenda;
import modelo.entidades.Talla;

@WebServlet("/AgregarItemBolsaController")
public class AgregarItemBolsaController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        this.ruteador(req, resp);
    }

    private void ruteador(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String ruta = (req.getParameter("ruta") != null)
                ? req.getParameter("ruta")
                : "";

        if ("agregarABolsa".equals(ruta)) {
            this.agregarABolsa(req, resp);
        }
    }

    /**
     * Caso de uso: Agregar prenda a la bolsa
     */
    private void agregarABolsa(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // 1. Obtener parámetros
        String idUsuarioStr = req.getParameter("idUsuario");
        String idPrendaStr = req.getParameter("idPrenda");
        String idTallaStr = req.getParameter("idTalla");
        String cantidadStr = req.getParameter("cantidad");

        try {
            int idUsuario = Integer.parseInt(idUsuarioStr);
            int idPrenda = Integer.parseInt(idPrendaStr);
            int cantidad = Integer.parseInt(cantidadStr);
            Talla talla = Talla.valueOf(idTallaStr);

            // 2. Hablar con el modelo
            PrendaDAO prendaDAO = new PrendaDAO();
            Prenda prenda = prendaDAO.getPrenda(idPrenda);

            BolsaDAO bolsaDAO = new BolsaDAO();
            boolean agregado = bolsaDAO.agregarItemABolsa(
                    idUsuario,
                    prenda,
                    talla,
                    cantidad
            );

            if (!agregado) {
                req.setAttribute("mensajeError", "No se pudo agregar el producto a la bolsa");
            }

        } catch (Exception e) {
            req.setAttribute("mensajeError", "Error al agregar producto a la bolsa");
        }

        // 3. Llamar a la vista
        req.getRequestDispatcher("jsp/Prenda.jsp").forward(req, resp);
    }

}
