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
@WebServlet("/AgregarItemBolsaController")
public class AgregarItemBolsaController extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private BolsaDAO bolsaDAO = new BolsaDAO();
    private PrendaDAO prendaDAO = new PrendaDAO();
    private ItemBolsaDAO itemBolsaDAO = new ItemBolsaDAO();
    private StockTallaDAO stockTallaDAO = new StockTallaDAO();
    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("🛒 AgregarItemBolsaController - Agregar item a bolsa");
        System.out.println("=".repeat(60));

        // Configurar respuesta JSON
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 1. Obtener o simular sesión del usuario
            HttpSession session = req.getSession(true);
            Usuario usuario = (Usuario) session.getAttribute("usuario");
            
            // Simular login si no hay sesión (igual que en VerBolsaController)
            if (usuario == null) {
                usuario = usuarioDAO.buscarPorNombreUsuario("martin");
                if (usuario == null) {
                    response.put("success", false);
                    response.put("message", "Usuario no encontrado. Ejecuta PoblarBaseDatos.java");
                    writeJsonResponse(resp, response);
                    return;
                }
                session.setAttribute("usuario", usuario);
                System.out.println("✅ Sesión simulada para usuario: " + usuario.getNombreUsuario());
            }
            
            // 2. Obtener parámetros del request
            String idPrendaStr = req.getParameter("idPrenda");
            String tallaStr = req.getParameter("talla");
            String cantidadStr = req.getParameter("cantidad");
            
            System.out.println("📥 Parámetros recibidos:");
            System.out.println("   - idPrenda: " + idPrendaStr);
            System.out.println("   - talla: " + tallaStr);
            System.out.println("   - cantidad: " + cantidadStr);
            
            // Validar parámetros
            if (idPrendaStr == null || tallaStr == null || cantidadStr == null) {
                response.put("success", false);
                response.put("message", "Parámetros incompletos");
                writeJsonResponse(resp, response);
                return;
            }
            
            // FLUJO ALTERNO 2.1: Validar que una talla esté seleccionada
            if (tallaStr.isEmpty() || tallaStr.equals("null")) {
                response.put("success", false);
                response.put("message", "Por favor selecciona una talla");
                writeJsonResponse(resp, response);
                return;
            }
            
            int idPrenda = Integer.parseInt(idPrendaStr);
            Talla talla = parsearTalla(tallaStr);
            int cantidad = Integer.parseInt(cantidadStr);
            
            System.out.println("🔍 Talla parseada: " + talla);
            
            if (talla == null) {
                response.put("success", false);
                response.put("message", "Talla no válida: " + tallaStr);
                System.out.println("❌ Talla null - string recibido: '" + tallaStr + "'");
                writeJsonResponse(resp, response);
                return;
            }
            
            // 3. Buscar la prenda
            Prenda prenda = prendaDAO.getPrenda(idPrenda);
            if (prenda == null) {
                response.put("success", false);
                response.put("message", "Prenda no encontrada");
                System.out.println("❌ Prenda no encontrada con ID: " + idPrenda);
                writeJsonResponse(resp, response);
                return;
            }
            
            System.out.println("✅ Prenda encontrada: " + prenda.getNombrePrenda());
            System.out.println("   Stocks disponibles:");
            if (prenda.getStockTallas() != null) {
                for (StockTalla st : prenda.getStockTallas()) {
                    System.out.println("   - Talla " + st.getTalla() + ": " + st.getCantidad() + " unidades");
                }
            }
            
            // 4. FLUJO ALTERNO 3.1: Verificar stock disponible para la talla seleccionada
            StockTalla stockTalla = stockTallaDAO.buscarStockPorPrendaYTalla(idPrenda, talla);
            System.out.println("🔍 Buscando stock para talla: " + talla + " (" + talla.getTalla() + ")");
            
            if (stockTalla == null) {
                System.out.println("❌ Stock NO encontrado para talla: " + talla);
                response.put("success", false);
                response.put("message", "Stock insuficiente para la talla " + talla.getTalla() + ". Disponible: 0");
                writeJsonResponse(resp, response);
                return;
            }
            
            System.out.println("✅ Stock encontrado: " + stockTalla.getCantidad() + " unidades disponibles");
            
            if (stockTalla.getCantidad() < cantidad) {
                System.out.println("⚠️ Stock insuficiente: solicitado=" + cantidad + ", disponible=" + stockTalla.getCantidad());
                response.put("success", false);
                response.put("message", "Stock insuficiente para la talla " + talla.getTalla() + 
                                       ". Disponible: " + stockTalla.getCantidad());
                writeJsonResponse(resp, response);
                return;
            }
            
            // 5. Buscar o crear la bolsa del usuario
            System.out.println("🔍 Buscando bolsa para usuario ID: " + usuario.getIdUsuario());
            Bolsa bolsa = bolsaDAO.buscarBolsaPorUsuario(usuario.getIdUsuario());
            
            if (bolsa == null) {
                // Crear nueva bolsa para el usuario
                System.out.println("🆕 Creando nueva bolsa para usuario: " + usuario.getNombreUsuario());
                bolsa = new Bolsa();
                bolsa.setUsuario(usuario);
                bolsa.setPrecioTotal(0.0);
                bolsaDAO.guardar(bolsa);
                System.out.println("✅ Bolsa creada con ID: " + bolsa.getIdBolsa());
            } else {
                System.out.println("✅ Bolsa encontrada - ID: " + bolsa.getIdBolsa());
                System.out.println("   Items actuales en bolsa: " + (bolsa.getItems() != null ? bolsa.getItems().size() : 0));
            }
            
            // 6. Verificar si la prenda con esa talla ya está en la bolsa
            System.out.println("🔍 Verificando si prenda ya existe en bolsa...");
            ItemBolsa itemExistente = buscarItemEnBolsa(bolsa, idPrenda, talla);
            System.out.println("   Item existente: " + (itemExistente != null ? "SÍ (ID: " + itemExistente.getIdItem() + ")" : "NO"));
            
            if (itemExistente != null) {
                // Si ya existe, incrementar la cantidad
                int nuevaCantidad = itemExistente.getCantidad() + cantidad;
                
                // Verificar que hay stock para la nueva cantidad
                if (stockTalla.getCantidad() < nuevaCantidad) {
                    response.put("success", false);
                    response.put("message", "Stock insuficiente. Ya tienes " + itemExistente.getCantidad() + 
                                           " en la bolsa. Disponible: " + stockTalla.getCantidad());
                    writeJsonResponse(resp, response);
                    return;
                }
                
                itemExistente.setCantidad(nuevaCantidad);
                itemBolsaDAO.actualizarCantidad(itemExistente, nuevaCantidad);
                System.out.println("✅ Cantidad actualizada para item existente");
            } else {
                // 7. Crear nuevo item en la bolsa
                System.out.println("🆕 Creando nuevo item en la bolsa...");
                ItemBolsa nuevoItem = new ItemBolsa();
                nuevoItem.setPrenda(prenda);
                nuevoItem.setTallaSeleccionada(talla);
                nuevoItem.setCantidad(cantidad);
                nuevoItem.setBolsa(bolsa);
                
                System.out.println("   - Prenda: " + prenda.getNombrePrenda() + " (ID: " + prenda.getIdPrenda() + ")");
                System.out.println("   - Talla: " + talla + " (" + talla.getTalla() + ")");
                System.out.println("   - Cantidad: " + cantidad);
                System.out.println("   - Bolsa ID: " + bolsa.getIdBolsa());
                
                // Guardar el item
                System.out.println("💾 Guardando item en base de datos...");
                boolean guardado = itemBolsaDAO.guardarItem(nuevoItem);
                
                System.out.println("   Resultado guardado: " + guardado);
                System.out.println("   ID item generado: " + nuevoItem.getIdItem());
                
                if (!guardado) {
                    System.out.println("❌ ERROR: No se pudo guardar el item");
                    response.put("success", false);
                    response.put("message", "Error al agregar la prenda a la bolsa");
                    writeJsonResponse(resp, response);
                    return;
                }
                
                System.out.println("✅ Nuevo item agregado a la bolsa con ID: " + nuevoItem.getIdItem());
            }
            
            // 8. Actualizar el precio total de la bolsa
            System.out.println("💰 Recalculando precio total de la bolsa...");
            bolsa = bolsaDAO.buscarPorId(bolsa.getIdBolsa()); // Recargar bolsa con todos los items
            System.out.println("   Items en bolsa después de recargar: " + (bolsa.getItems() != null ? bolsa.getItems().size() : 0));
            double nuevoTotal = bolsa.calcularPrecioTotal();
            System.out.println("   Nuevo total calculado: $" + nuevoTotal);
            bolsa.setPrecioTotal(nuevoTotal);
            bolsaDAO.actualizar(bolsa);
            System.out.println("✅ Precio total actualizado en BD");
            
            // 9. FLUJO BÁSICO PASO 4: Respuesta exitosa
            response.put("success", true);
            response.put("message", "¡Prenda agregada a la bolsa exitosamente!");
            response.put("cantidadItems", bolsa.getItems().size());
            response.put("precioTotal", String.format("%.2f", nuevoTotal));
            
            System.out.println("✅ Item agregado exitosamente");
            System.out.println("   - Total items en bolsa: " + bolsa.getItems().size());
            System.out.println("   - Precio total: $" + nuevoTotal);
            
        } catch (NumberFormatException e) {
            response.put("success", false);
            response.put("message", "Datos inválidos");
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", "Talla no válida");
            e.printStackTrace();
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al agregar la prenda: " + e.getMessage());
            e.printStackTrace();
        }
        
        writeJsonResponse(resp, response);
        System.out.println("=".repeat(60) + "\n");
    }
    
    /**
     * Busca un item en la bolsa que coincida con la prenda y talla
     */
    private ItemBolsa buscarItemEnBolsa(Bolsa bolsa, int idPrenda, Talla talla) {
        if (bolsa.getItems() == null) {
            System.out.println("⚠️ La bolsa no tiene items (null)");
            return null;
        }
        
        System.out.println("🔍 Buscando item existente:");
        System.out.println("   - ID Prenda buscada: " + idPrenda);
        System.out.println("   - Talla buscada: " + talla + " (" + (talla != null ? talla.getTalla() : "NULL") + ")");
        System.out.println("   - Items en bolsa: " + bolsa.getItems().size());
        
        for (ItemBolsa item : bolsa.getItems()) {
            System.out.println("   → Comparando con item " + item.getIdItem() + ":");
            System.out.println("      - Prenda: " + item.getPrenda().getIdPrenda() + " vs " + idPrenda + " → " + (item.getPrenda().getIdPrenda() == idPrenda));
            System.out.println("      - Talla: " + item.getTallaSeleccionada() + " (" + item.getTallaSeleccionada().getTalla() + ")");
            System.out.println("      - Comparación ==: " + (item.getTallaSeleccionada() == talla));
            System.out.println("      - Comparación equals: " + (item.getTallaSeleccionada().equals(talla)));
            
            // Usar equals() en lugar de == para comparar enums de forma más robusta
            if (item.getPrenda().getIdPrenda() == idPrenda && 
                item.getTallaSeleccionada().equals(talla)) {
                System.out.println("   ✅ ENCONTRADO item existente con la misma prenda y talla");
                return item;
            }
        }
        
        System.out.println("   ❌ NO se encontró item existente con esa prenda y talla");
        return null;
    }
    
    /**
     * Parsea un string a enum Talla de forma robusta
     * Intenta primero valueOf() y luego busca por el valor de getTalla()
     */
    private Talla parsearTalla(String tallaStr) {
        if (tallaStr == null || tallaStr.isEmpty()) {
            return null;
        }
        
        // Intentar primero el nombre del enum directo
        try {
            return Talla.valueOf(tallaStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            // Si falla, buscar por el valor de getTalla()
            for (Talla t : Talla.values()) {
                if (t.getTalla().equalsIgnoreCase(tallaStr)) {
                    return t;
                }
            }
            return null;
        }
    }
    
    /**
     * Escribe la respuesta JSON
     */
    private void writeJsonResponse(HttpServletResponse resp, Map<String, Object> response) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(response);
        
        PrintWriter out = resp.getWriter();
        out.print(json);
        out.flush();
    }
}
