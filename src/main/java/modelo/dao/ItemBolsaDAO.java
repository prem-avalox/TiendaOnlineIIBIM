package modelo.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import modelo.entidades.ItemBolsa;
import modelo.entidades.StockTalla;

/**
 * DAO para gestionar operaciones de persistencia de la entidad ItemBolsa
 * Según diagramas CU11 - Ver Bolsa (flujos alternos)
 */
public class ItemBolsaDAO {

    private EntityManagerFactory emf;

    public ItemBolsaDAO() {
        this.emf = Persistence.createEntityManagerFactory("persistencia");
    }

    /**
     * Guarda un nuevo item en la base de datos
     * @param item ItemBolsa a guardar
     * @return true si se guardó correctamente, false en caso contrario
     */
    public boolean guardarItem(ItemBolsa item) {
        EntityManager em = emf.createEntityManager();
        System.out.println("\n" + "=".repeat(50));
        System.out.println("💾 [ItemBolsaDAO] GUARDAR ITEM");
        System.out.println("=".repeat(50));
        System.out.println("Item a guardar:");
        System.out.println("   - Prenda: " + (item.getPrenda() != null ? item.getPrenda().getNombrePrenda() + " (ID: " + item.getPrenda().getIdPrenda() + ")" : "NULL"));
        System.out.println("   - Talla: " + (item.getTallaSeleccionada() != null ? item.getTallaSeleccionada().getTalla() : "NULL"));
        System.out.println("   - Cantidad: " + item.getCantidad());
        System.out.println("   - Bolsa ID: " + (item.getBolsa() != null ? item.getBolsa().getIdBolsa() : "NULL"));
        
        try {
            System.out.println("🔄 Iniciando transacción...");
            em.getTransaction().begin();
            
            System.out.println("💾 Ejecutando persist...");
            em.persist(item);
            
            System.out.println("💾 Ejecutando flush para escribir a BD inmediatamente...");
            em.flush(); // ✅ FORZAR escritura inmediata a la BD
            
            System.out.println("✅ Haciendo commit...");
            em.getTransaction().commit();
            
            System.out.println("✅ ITEM GUARDADO EXITOSAMENTE");
            System.out.println("   - ID generado: " + item.getIdItem());
            System.out.println("=".repeat(50) + "\n");
            return true;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                System.err.println("🔙 Haciendo rollback...");
                em.getTransaction().rollback();
            }
            System.err.println("❌ ERROR AL GUARDAR ITEM:");
            System.err.println("   Mensaje: " + e.getMessage());
            System.err.println("   Clase: " + e.getClass().getName());
            e.printStackTrace();
            System.out.println("=".repeat(50) + "\n");
            return false;
        } finally {
            em.close();
            System.out.println("🔒 EntityManager cerrado");
        }
    }

    /**
     * Elimina un item de la bolsa
     * Según diagrama de secuencia CU11.2 - Eliminar ítem
     * IMPORTANTE: Maneja correctamente la relación bidireccional con la bolsa
     * @param idItem ID del item a eliminar
     */
    public void eliminarItem(int idItem) {
        EntityManager em = emf.createEntityManager();
        System.out.println("🔍 [ItemBolsaDAO] Iniciando eliminación del item con ID: " + idItem);
        try {
            em.getTransaction().begin();
            System.out.println("🔄 [ItemBolsaDAO] Transacción iniciada");
            
            ItemBolsa item = em.find(ItemBolsa.class, idItem);
            
            if (item != null) {
                System.out.println("✅ [ItemBolsaDAO] Item encontrado - ID: " + item.getIdItem());
                
                // Opción 2: Usar orphanRemoval removemos de la lista del padre
                if (item.getBolsa() != null) {
                    System.out.println("🔗 [ItemBolsaDAO] Removiendo item de la lista de la bolsa (triggering orphanRemoval)...");
                    item.getBolsa().getItems().remove(item);
                    // No llamamos a em.remove(item) explícitamente, confiamos en orphanRemoval 
                    // y CascadeType.ALL al hacer commit de la transacción que envuelve al padre.
                    
                    // Necesitamos asegurar que el padre sea "merged" o que el cambio se detecte.
                    // Como el padre fue cargado a través de 'item' en este contexto, está managed.
                    em.merge(item.getBolsa());
                } else {
                    // Si no tiene bolsa (raro), hacemos remove directo
                     em.remove(item);
                }

                System.out.println("🗑️ [ItemBolsaDAO] Item removido de la lista");
            } else {
                System.err.println("❌ [ItemBolsaDAO] Item NO encontrado con ID: " + idItem);
            }
            
            em.getTransaction().commit();
            System.out.println("✅ [ItemBolsaDAO] Transacción confirmada (commit)");
            
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
                System.err.println("🔙 [ItemBolsaDAO] Transacción revertida (rollback)");
            }
            System.err.println("❌ [ItemBolsaDAO] Error al eliminar item: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
            System.out.println("🔒 [ItemBolsaDAO] EntityManager cerrado");
        }
    }

    /**
     * Actualiza la cantidad de un item en la bolsa
     * Según diagrama de secuencia CU11.3 - Ajustar cantidad
     * @param item Item a actualizar
     * @param cantidad Nueva cantidad
     * @return true si se actualizó correctamente, false en caso contrario
     */
    public boolean actualizarCantidad(ItemBolsa item, int cantidad) {
        EntityManager em = emf.createEntityManager();
        System.out.println("🔍 [ItemBolsaDAO] Iniciando actualización de cantidad");
        System.out.println("   - ID Item: " + item.getIdItem());
        System.out.println("   - Cantidad actual: " + item.getCantidad());
        System.out.println("   - Nueva cantidad: " + cantidad);
        
        try {
            em.getTransaction().begin();
            System.out.println("🔄 [ItemBolsaDAO] Transacción iniciada");
            
            ItemBolsa itemManaged = em.find(ItemBolsa.class, item.getIdItem());
            if (itemManaged != null) {
                System.out.println("✅ [ItemBolsaDAO] Item encontrado en BD");
                itemManaged.setCantidad(cantidad);
                em.merge(itemManaged);
                em.flush(); // ✅ FORZAR escritura inmediata
                System.out.println("💾 [ItemBolsaDAO] Cantidad actualizada, merged y flushed");
            } else {
                System.err.println("❌ [ItemBolsaDAO] Item NO encontrado con ID: " + item.getIdItem());
            }
            
            em.getTransaction().commit();
            System.out.println("✅ [ItemBolsaDAO] Transacción confirmada (commit)");
            return true;
            
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
                System.err.println("🔙 [ItemBolsaDAO] Transacción revertida (rollback)");
            }
            System.err.println("❌ [ItemBolsaDAO] Error al actualizar cantidad: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            em.close();
            System.out.println("🔒 [ItemBolsaDAO] EntityManager cerrado");
        }
    }

    /**
     * Verifica si hay stock suficiente para un item
     * Según diagrama de secuencia CU11.3 - Flujo alterno verificar stock
     * @param idPrenda ID de la prenda
     * @param idTalla ID de la talla
     * @param cantidad Cantidad solicitada
     * @return true si hay stock suficiente, false en caso contrario
     */
    public boolean verificarStock(int idPrenda, int idTalla, int cantidad) {
        EntityManager em = emf.createEntityManager();
        try {
            // Buscar el stock correspondiente
            StockTalla stock = em.createQuery(
                "SELECT st FROM StockTalla st " +
                "WHERE st.prenda.idPrenda = :idPrenda " +
                "AND st.talla = :talla", 
                StockTalla.class
            )
            .setParameter("idPrenda", idPrenda)
            .setParameter("talla", idTalla)
            .getSingleResult();
            
            // Verificar si hay stock suficiente
            return stock != null && stock.getCantidad() >= cantidad;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            em.close();
        }
    }

    /**
     * Cierra el EntityManagerFactory
     */
    public void cerrar() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}
