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
        try {
            em.getTransaction().begin();
            em.persist(item);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            em.close();
        }
    }

    /**
     * Elimina un item de la bolsa
     * Según diagrama de secuencia CU11.2 - Eliminar ítem
     * @param idItem ID del item a eliminar
     */
    public void eliminarItem(int idItem) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            ItemBolsa item = em.find(ItemBolsa.class, idItem);
            if (item != null) {
                em.remove(item);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
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
        try {
            em.getTransaction().begin();
            ItemBolsa itemManaged = em.find(ItemBolsa.class, item.getIdItem());
            if (itemManaged != null) {
                itemManaged.setCantidad(cantidad);
                em.merge(itemManaged);
            }
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            em.close();
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
