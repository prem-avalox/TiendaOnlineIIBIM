package controlador;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import modelo.dao.BolsaDAO;
import modelo.dao.ItemBolsaDAO;
import modelo.dao.PrendaDAO;
import modelo.dao.StockTallaDAO;
import modelo.dao.UsuarioDAO;
import modelo.entidades.Bolsa;
import modelo.entidades.ItemBolsa;
import modelo.entidades.Prenda;
import modelo.entidades.StockTalla;
import modelo.entidades.Talla;
import modelo.entidades.Usuario;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;

/**
 * Controlador para agregar items a la bolsa de compras
 * Implementa el caso de uso CU10 - Agregar prenda a la bolsa según diagrama de secuencia
 */
@WebServlet("/AgregarPrendaBolsaController")
public class AgregarPrendaBolsaController extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private BolsaDAO bolsaDAO = new BolsaDAO();
    private PrendaDAO prendaDAO = new PrendaDAO();
    private ItemBolsaDAO itemBolsaDAO = new ItemBolsaDAO();
    private StockTallaDAO stockTallaDAO = new StockTallaDAO();
    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        Map<String, Object> response;

        try {
            HttpSession session = req.getSession(true);
            String idPrendaStr = req.getParameter("idPrenda");
            String nombreTalla = req.getParameter("talla");
            String cantidadStr = req.getParameter("cantidad");

            if (idPrendaStr == null || nombreTalla == null || cantidadStr == null) {
                response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Parámetros incompletos");
                writeJsonResponse(resp, response);
                return;
            }

            int idPrenda = Integer.parseInt(idPrendaStr);
            int cantidad = Integer.parseInt(cantidadStr);

            response = agregarPrenda(idPrenda, nombreTalla, cantidad, session);

        } catch (Exception e) {
            response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error al agregar la prenda: " + e.getMessage());
        }

        writeJsonResponse(resp, response);
    }

    /**
     * UML: agregarPrenda(idPrenda, nombreTalla, cantidadInicial)
     */
    private Map<String, Object> agregarPrenda(int idPrenda, String nombreTalla, int cantidad, HttpSession session) {
        Map<String, Object> response = new HashMap<>();

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            usuario = usuarioDAO.buscarPorNombreUsuario("martin");
            if (usuario == null) {
                response.put("success", false);
                response.put("message", "Usuario no encontrado. Ejecuta PoblarBaseDatos.java");
                return response;
            }
            session.setAttribute("usuario", usuario);
        }

        Prenda prenda = prendaDAO.getPrenda(idPrenda);
        if (prenda == null) {
            response.put("success", false);
            response.put("message", "Prenda no encontrada");
            return response;
        }

        Talla tallaEnum = resolveTalla(nombreTalla);
        if (tallaEnum == null) {
            response.put("success", false);
            response.put("message", "Talla no válida: " + nombreTalla);
            return response;
        }

        StockTalla stock = stockTallaDAO.buscarStock(idPrenda, cantidad, tallaEnum.ordinal());
        if (stock == null || stock.getCantidad() < cantidad) {
            response.put("success", false);
            response.put("message", "Stock insuficiente");
            return response;
        }

        Bolsa bolsa = bolsaDAO.buscarBolsaPorUsuario(usuario.getIdUsuario());
        if (bolsa == null) {
            bolsa = new Bolsa();
            bolsa.setUsuario(usuario);
            bolsa.setPrecioTotal(0.0);
            bolsaDAO.guardarBolsa(bolsa);
        }

        ItemBolsa item = new ItemBolsa();
        item.setPrenda(prenda);
        item.setTallaSeleccionada(tallaEnum);
        item.setCantidad(cantidad);
        item.setBolsa(bolsa);
        if (bolsa.getItems() != null) {
            bolsa.getItems().add(item);
        }

        itemBolsaDAO.guardarItem(item);

        Bolsa bolsaRefrescada = bolsaDAO.buscarBolsaPorUsuario(usuario.getIdUsuario());
        if (bolsaRefrescada != null) {
            double nuevoTotal = bolsaRefrescada.calcularMontoTotal();
            bolsaRefrescada.setPrecioTotal(nuevoTotal);
            bolsaDAO.actualizarBolsa(bolsaRefrescada);
            bolsa = bolsaRefrescada;
        }

        stockTallaDAO.descontarStock(idPrenda, cantidad);

        response.put("success", true);
        response.put("message", "Prenda agregada a la bolsa exitosamente");
        response.put("cantidadItems", bolsa.getItems() != null ? bolsa.getItems().size() : 0);
        response.put("precioTotal", String.format("%.2f", bolsa.getPrecioTotal()));
        return response;
    }

    // Acepta tanto nombre de talla como ordinal en texto
    private Talla resolveTalla(String valor) {
        if (valor == null) return null;
        // Primero intentar ordinal numérico
        try {
            int ordinal = Integer.parseInt(valor);
            Talla[] values = Talla.values();
            if (ordinal >= 0 && ordinal < values.length) {
                return values[ordinal];
            }
        } catch (NumberFormatException ignore) {}
        // Luego intentar por nombre (case-insensitive) comparando enum name y el valor de getTalla()
        for (Talla t : Talla.values()) {
            if (t.name().equalsIgnoreCase(valor) || t.getTalla().equalsIgnoreCase(valor)) {
                return t;
            }
        }
        return null;
    }

    private void writeJsonResponse(HttpServletResponse resp, Map<String, Object> response) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(response);
        PrintWriter out = resp.getWriter();
        out.print(json);
        out.flush();
    }
}