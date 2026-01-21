package test;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import modelo.dao.BolsaDAO;
import modelo.dao.PrendaDAO;
import modelo.dao.UsuarioDAO;
import modelo.entidades.*;

import java.util.List;

/**
 * Clase para agregar items de prueba a la bolsa del usuario Martin
 * Útil cuando necesitas probar la funcionalidad de bolsa con datos
 * 
 * EJECUTAR: Click derecho → Run As → Java Application
 */
public class AgregarItemsBolsaMartin {

    public static void main(String[] args) {
        System.out.println("🛒 AGREGANDO ITEMS A BOLSA DE MARTIN...\n");
        
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        BolsaDAO bolsaDAO = new BolsaDAO();
        PrendaDAO prendaDAO = new PrendaDAO();
        
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistencia");
        EntityManager em = emf.createEntityManager();
        
        try {
            // 1. Buscar usuario Martin
            System.out.println("1️⃣ Buscando usuario 'martin'...");
            Usuario martin = usuarioDAO.buscarPorNombreUsuario("martin");
            
            if (martin == null) {
                System.err.println("❌ ERROR: Usuario 'martin' no encontrado");
                System.err.println("   Solución: Ejecuta PoblarBaseDatos.java primero");
                return;
            }
            
            System.out.println("   ✅ Usuario encontrado: " + martin.getNombreUsuario() + 
                             " (ID: " + martin.getIdUsuario() + ")");
            
            // 2. Buscar o crear bolsa
            System.out.println("\n2️⃣ Buscando bolsa del usuario...");
            Bolsa bolsa = bolsaDAO.buscarBolsaPorUsuario(martin.getIdUsuario());
            
            if (bolsa == null) {
                System.out.println("   ⚠️  No existe bolsa, creando una nueva...");
                em.getTransaction().begin();
                bolsa = new Bolsa();
                bolsa.setUsuario(martin);
                bolsa.setPrecioTotal(0.0);
                em.persist(bolsa);
                em.getTransaction().commit();
                System.out.println("   ✅ Bolsa creada: ID " + bolsa.getIdBolsa());
            } else {
                System.out.println("   ✅ Bolsa encontrada: ID " + bolsa.getIdBolsa());
            }
            
            // 3. Limpiar items existentes
            System.out.println("\n3️⃣ Limpiando items existentes...");
            if (!bolsa.getItems().isEmpty()) {
                em.getTransaction().begin();
                // Recargar la bolsa en la transacción actual
                bolsa = em.find(Bolsa.class, bolsa.getIdBolsa());
                bolsa.getItems().clear();
                bolsa.setPrecioTotal(0.0);
                em.merge(bolsa);
                em.getTransaction().commit();
                System.out.println("   ✅ Items anteriores eliminados");
            } else {
                System.out.println("   ✅ La bolsa ya estaba vacía");
            }
            
            // 4. Obtener prendas disponibles
            System.out.println("\n4️⃣ Obteniendo prendas disponibles...");
            List<Prenda> todasPrendas = em.createQuery("SELECT p FROM Prenda p", Prenda.class).getResultList();
            
            if (todasPrendas.isEmpty()) {
                System.err.println("❌ ERROR: No hay prendas en la base de datos");
                System.err.println("   Solución: Ejecuta PoblarBaseDatos.java primero");
                return;
            }
            
            System.out.println("   ✅ " + todasPrendas.size() + " prendas disponibles");
            
            // 5. Agregar 3 items diferentes
            System.out.println("\n5️⃣ Agregando items a la bolsa...");
            em.getTransaction().begin();
            
            // Recargar bolsa en la transacción
            bolsa = em.find(Bolsa.class, bolsa.getIdBolsa());
            
            int itemsAgregados = 0;
            
            // Item 1: Primera prenda (cantidad 2, talla M)
            if (todasPrendas.size() > 0) {
                Prenda prenda1 = em.find(Prenda.class, todasPrendas.get(0).getIdPrenda());
                ItemBolsa item1 = new ItemBolsa();
                item1.setPrenda(prenda1);
                item1.setCantidad(2);
                item1.setTallaSeleccionada(Talla.M);
                item1.setBolsa(bolsa);
                bolsa.getItems().add(item1);
                em.persist(item1);
                itemsAgregados++;
                
                System.out.println("   📦 Item 1: " + prenda1.getNombrePrenda() + 
                                 " (x2, Talla M) - $" + String.format("%.2f", prenda1.getPrecio() * 2));
            }
            
            // Item 2: Segunda prenda (cantidad 1, talla L)
            if (todasPrendas.size() > 1) {
                Prenda prenda2 = em.find(Prenda.class, todasPrendas.get(1).getIdPrenda());
                ItemBolsa item2 = new ItemBolsa();
                item2.setPrenda(prenda2);
                item2.setCantidad(1);
                item2.setTallaSeleccionada(Talla.L);
                item2.setBolsa(bolsa);
                bolsa.getItems().add(item2);
                em.persist(item2);
                itemsAgregados++;
                
                System.out.println("   📦 Item 2: " + prenda2.getNombrePrenda() + 
                                 " (x1, Talla L) - $" + String.format("%.2f", prenda2.getPrecio()));
            }
            
            // Item 3: Tercera prenda (cantidad 1, talla M)
            if (todasPrendas.size() > 2) {
                Prenda prenda3 = em.find(Prenda.class, todasPrendas.get(2).getIdPrenda());
                ItemBolsa item3 = new ItemBolsa();
                item3.setPrenda(prenda3);
                item3.setCantidad(1);
                item3.setTallaSeleccionada(Talla.M);
                item3.setBolsa(bolsa);
                bolsa.getItems().add(item3);
                em.persist(item3);
                itemsAgregados++;
                
                System.out.println("   📦 Item 3: " + prenda3.getNombrePrenda() + 
                                 " (x1, Talla M) - $" + String.format("%.2f", prenda3.getPrecio()));
            }
            
            // 6. Calcular y actualizar total (IMPORTANTE: usando el método del modelo)
            System.out.println("\n6️⃣ Calculando total...");
            double total = bolsa.calcularMontoTotal();
            bolsa.setPrecioTotal(total);
            em.merge(bolsa);
            
            em.getTransaction().commit();
            
            System.out.println("   ✅ Total calculado: $" + String.format("%.2f", total));
            
            // 7. Verificar resultado final
            System.out.println("\n7️⃣ Verificando resultado...");
            Bolsa bolsaFinal = bolsaDAO.buscarBolsaPorUsuario(martin.getIdUsuario());
            
            System.out.println("\n" + "=".repeat(50));
            System.out.println("📊 RESUMEN FINAL:");
            System.out.println("=".repeat(50));
            System.out.println("👤 Usuario: " + martin.getNombreUsuario());
            System.out.println("🛒 Bolsa ID: " + bolsaFinal.getIdBolsa());
            System.out.println("📦 Items agregados: " + itemsAgregados);
            System.out.println("💰 Total: $" + String.format("%.2f", bolsaFinal.getPrecioTotal()));
            System.out.println("=".repeat(50));
            
            System.out.println("\n🎉 ¡ITEMS AGREGADOS EXITOSAMENTE!");
            
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("\n❌ ERROR al agregar items:");
            e.printStackTrace();
        } finally {
            em.close();
            emf.close();
        }
    }
}