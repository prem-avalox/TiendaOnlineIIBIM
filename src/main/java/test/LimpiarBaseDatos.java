package test;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * Clase para limpiar la base de datos
 * Ejecutar antes de PoblarBaseDatos
 */
public class LimpiarBaseDatos {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistencia");
        EntityManager em = emf.createEntityManager();

        try {
            System.out.println("🗑️  Limpiando base de datos...\n");

            em.getTransaction().begin();

            // Eliminar en orden por las FK
            System.out.println("🗑️  Eliminando ItemBolsa...");
            em.createQuery("DELETE FROM ItemBolsa").executeUpdate();
            
            System.out.println("🗑️  Eliminando Bolsa...");
            em.createQuery("DELETE FROM Bolsa").executeUpdate();
            
            System.out.println("🗑️  Eliminando StockTalla...");
            em.createQuery("DELETE FROM StockTalla").executeUpdate();
            
            System.out.println("🗑️  Eliminando Prenda...");
            em.createQuery("DELETE FROM Prenda").executeUpdate();
            
            System.out.println("🗑️  Eliminando Usuario...");
            em.createQuery("DELETE FROM Usuario").executeUpdate();

            em.getTransaction().commit();

            System.out.println("\n✅ BASE DE DATOS LIMPIADA EXITOSAMENTE!");
            System.out.println("   Ahora puedes ejecutar PoblarBaseDatos.java\n");

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("❌ ERROR al limpiar base de datos:");
            e.printStackTrace();
        } finally {
            em.close();
            emf.close();
        }
    }
}
