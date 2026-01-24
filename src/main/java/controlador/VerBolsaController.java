package controlador;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import modelo.dao.BolsaDAO;
import modelo.entidades.Bolsa;

@WebServlet("/VerBolsaController")
public class VerBolsaController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        this.ruteador(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        this.ruteador(req, resp);
    }

    private void ruteador(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String ruta = (req.getParameter("ruta") != null)
                ? req.getParameter("ruta")
                : "abrirBolsa";

        switch (ruta) {
        case "abrirBolsa":
            this.verBolsa(req, resp);
            break;
            
        case "eliminarItem":
            this.eliminarItemBolsa(req, resp);
            break;
            
        case "ajustarCantidadItem":
            this.ajustarCantidadItem(req, resp);
            break;
            

            
            
        default:
            this.verBolsa(req, resp);
            break;
        }
    }

    /**
     * Caso de uso: Ver Bolsa
     * Diagrama: Usuario → SidebarBolsa → VerBolsaController → BolsaDAO
     */
    private void verBolsa(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try {
            // 1. Obtener idUsuario (desde sesión)
            Integer idUsuario = (Integer) req.getSession().getAttribute("idUsuario");

            if (idUsuario == null) {
                req.setAttribute("mensajeError", "Usuario no autenticado");
                req.getRequestDispatcher("jsp/SidebarBolsa.jsp").forward(req, resp);
                return;
            }

            // 2. Hablar con el modelo
            BolsaDAO bolsaDAO = new BolsaDAO();
            Bolsa bolsa = bolsaDAO.getBolsa(idUsuario);

            // 3. Flujo alterno: bolsa inexistente
            if (bolsa == null || bolsa.getItems() == null || bolsa.getItems().isEmpty()) {
                req.setAttribute("mensajeError", "Tu bolsa está vacía");
            } else {
                req.setAttribute("bolsa", bolsa);
            }

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("mensajeError", "Error al cargar la bolsa");
        }

        // 4. Llamar a la vista (Sidebar)
        req.getRequestDispatcher("jsp/SidebarBolsa.jsp").forward(req, resp);
    }
    
    private void eliminarItemBolsa(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try {
            // 1. Obtener parámetros
            String idItemStr = req.getParameter("idItem");

            if (idItemStr == null || idItemStr.isEmpty()) {
                req.setAttribute("mensajeError", "Item inválido");
                req.getRequestDispatcher("jsp/SidebarBolsa.jsp").forward(req, resp);
                return;
            }

            int idItem = Integer.parseInt(idItemStr);

            // 2. Hablar con el modelo
            BolsaDAO bolsaDAO = new BolsaDAO();
            boolean eliminado = bolsaDAO.eliminarItem(idItem);

            // 3. Flujo alterno (alt del diagrama)
            if (eliminado) {

                // Recargar bolsa
                Integer idUsuario = (Integer) req.getSession().getAttribute("idUsuario");

                if (idUsuario != null) {
                    req.setAttribute("bolsa", bolsaDAO.getBolsa(idUsuario));
                }

            } else {
                req.setAttribute("mensajeError", "No se pudo eliminar el item");
            }

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("mensajeError", "Error al eliminar el item de la bolsa");
        }

        // 4. Presentar resultado (Sidebar)
        req.getRequestDispatcher("jsp/SidebarBolsa.jsp").forward(req, resp);
    }

    private void ajustarCantidadItem(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try {
            // 1. Obtener parámetros
            String idItemStr = req.getParameter("idItem");
            String nuevaCantidadStr = req.getParameter("nuevaCantidad");

            if (idItemStr == null || nuevaCantidadStr == null ||
                idItemStr.isEmpty() || nuevaCantidadStr.isEmpty()) {

                req.setAttribute("mensajeError", "Parámetros inválidos");
                req.getRequestDispatcher("jsp/SidebarBolsa.jsp").forward(req, resp);
                return;
            }

            int idItem = Integer.parseInt(idItemStr);
            int nuevaCantidad = Integer.parseInt(nuevaCantidadStr);

            if (nuevaCantidad <= 0) {
                req.setAttribute("mensajeError", "La cantidad debe ser mayor a cero");
                req.getRequestDispatcher("jsp/SidebarBolsa.jsp").forward(req, resp);
                return;
            }

            // 2. Hablar con el modelo
            BolsaDAO bolsaDAO = new BolsaDAO();
            boolean actualizado = bolsaDAO.ajustarItem(idItem, nuevaCantidad);

            // 3. Flujo alterno (alt del diagrama)
            if (actualizado) {

                Integer idUsuario = (Integer) req.getSession().getAttribute("idUsuario");

                if (idUsuario != null) {
                    req.setAttribute("bolsa", bolsaDAO.getBolsa(idUsuario));
                }

            } else {
                req.setAttribute("mensajeError", "No hay stock suficiente para la cantidad solicitada");
            }

        } catch (NumberFormatException e) {

            req.setAttribute("mensajeError", "Formato numérico inválido");

        } catch (Exception e) {

            e.printStackTrace();
            req.setAttribute("mensajeError", "Error al ajustar la cantidad del item");
        }

        // 4. Presentar resultado
        req.getRequestDispatcher("jsp/SidebarBolsa.jsp").forward(req, resp);
    }

    
}
