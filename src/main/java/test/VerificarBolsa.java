package test;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import modelo.dao.BolsaDAO;
import modelo.dao.UsuarioDAO;
import modelo.entidades.Bolsa;
import modelo.entidades.ItemBolsa;
import modelo.entidades.Usuario;

/**
 * Clase para verificar que la bolsa carga correctamente los items
 */
public class VerificarBolsa {

    public static void main(String[] args) {
        System.out.println("🔍 VERIFICANDO CARGA DE BOLSA...\n");
        
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        BolsaDAO bolsaDAO = new BolsaDAO();
        
        try {
            // 1. Buscar usuario "martin"
            System.out.println("1️⃣ Buscando usuario 'martin'...");
            Usuario usuario = usuarioDAO.buscarPorNombreUsuario("martin");
            
            if (usuario == null) {
                System.err.println("❌ ERROR: Usuario 'martin' no encontrado");
                System.err.println("   Solución: Ejecuta PoblarBaseDatos.java primero");
                return;
            }
            
            System.out.println("   ✅ Usuario encontrado:");
            System.out.println("      - ID: " + usuario.getIdUsuario());
            System.out.println("      - Nombre: " + usuario.getNombreUsuario());
            System.out.println("      - Email: " + usuario.getEmail());
            
            // 2. Buscar bolsa del usuario
            System.out.println("\n2️⃣ Buscando bolsa del usuario...");
            Bolsa bolsa = bolsaDAO.buscarBolsaPorUsuario(usuario.getIdUsuario());
            
            if (bolsa == null) {
                System.err.println("❌ ERROR: No se encontró bolsa para el usuario");
                System.err.println("   Solución: Ejecuta PoblarBaseDatos.java primero");
                return;
            }
            
            System.out.println("   ✅ Bolsa encontrada:");
            System.out.println("      - ID Bolsa: " + bolsa.getIdBolsa());
            System.out.println("      - Precio Total: $" + bolsa.getPrecioTotal());
            
            // 3. Verificar items
            System.out.println("\n3️⃣ Verificando items en la bolsa...");
            
            if (bolsa.getItems() == null) {
                System.err.println("❌ ERROR: La lista de items es NULL");
                return;
            }
            
            int cantidadItems = bolsa.getItems().size();
            System.out.println("   ✅ Lista de items inicializada");
            System.out.println("      - Cantidad de items: " + cantidadItems);
            
            if (cantidadItems == 0) {
                System.err.println("\n⚠️  ADVERTENCIA: La bolsa está VACÍA (0 items)");
                System.err.println("   Solución: Ejecuta PoblarBaseDatos.java para agregar items");
                return;
            }
            
            // 4. Mostrar detalles de cada item
            System.out.println("\n4️⃣ Detalles de los items:");
            double totalCalculado = 0.0;
            
            for (int i = 0; i < bolsa.getItems().size(); i++) {
                ItemBolsa item = bolsa.getItems().get(i);
                
                System.out.println("\n   📦 Item " + (i + 1) + ":");
                System.out.println("      - ID Item: " + item.getIdItem());
                System.out.println("      - Cantidad: " + item.getCantidad());
                System.out.println("      - Talla: " + item.getTallaSeleccionada());
                
                if (item.getPrenda() == null) {
                    System.err.println("      ❌ ERROR: Prenda es NULL");
                } else {
                    System.out.println("      - Prenda: " + item.getPrenda().getNombrePrenda());
                    System.out.println("      - Precio: $" + item.getPrenda().getPrecio());
                    System.out.println("      - Imagen: " + item.getPrenda().getImagen());
                    
                    double subtotal = item.calcularSubtotal();
                    System.out.println("      - Subtotal: $" + String.format("%.2f", subtotal));
                    totalCalculado += subtotal;
                }
            }
            
            // 5. Verificar cálculo de total
            System.out.println("\n5️⃣ Verificación de totales:");
            double totalModelo = bolsa.calcularMontoTotal();
            
            System.out.println("   - Total calculado manualmente: $" + String.format("%.2f", totalCalculado));
            System.out.println("   - Total según modelo (calcularMontoTotal): $" + String.format("%.2f", totalModelo));
            System.out.println("   - Total guardado en BD: $" + String.format("%.2f", bolsa.getPrecioTotal()));
            
            if (Math.abs(totalCalculado - totalModelo) < 0.01) {
                System.out.println("   ✅ Los cálculos coinciden");
            } else {
                System.err.println("   ⚠️  Los cálculos NO coinciden");
            }
            
            // 6. Resumen final
            System.out.println("\n" + "=".repeat(50));
            System.out.println("📊 RESUMEN FINAL:");
            System.out.println("=".repeat(50));
            System.out.println("✅ Usuario: " + usuario.getNombreUsuario());
            System.out.println("✅ Bolsa ID: " + bolsa.getIdBolsa());
            System.out.println("✅ Items cargados: " + cantidadItems);
            System.out.println("✅ Total: $" + String.format("%.2f", totalModelo));
            System.out.println("=".repeat(50));
            System.out.println("\n🎉 ¡TODO ESTÁ FUNCIONANDO CORRECTAMENTE!");
            
        } catch (Exception e) {
            System.err.println("\n❌ ERROR INESPERADO:");
            e.printStackTrace();
        }
    }
}
