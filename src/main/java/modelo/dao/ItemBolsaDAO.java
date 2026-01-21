package modelo.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import modelo.entidades.ItemBolsa;
import modelo.entidades.StockTalla;

public class ItemBolsaDAO {

    private EntityManagerFactory emf;

    public ItemBolsaDAO() {
        this.emf = Persistence.createEntityManagerFactory("persistencia");
    }

    /** UML: guardarItem(item): boolean */
    public boolean guardarItem(ItemBolsa item) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(item);
            em.flush();
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            return false;
        } finally {
            em.close();
        }
    }

    /** UML: eliminarItem(idPrenda): void */
    public void eliminarItem(int idPrenda) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            ItemBolsa item = em.find(ItemBolsa.class, idPrenda);
            if (item != null) {
                em.remove(item);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        } finally {
            em.close();
        }
    }

    /** UML: actualizarCantidad(item, cantidad): boolean */
    public boolean actualizarCantidad(ItemBolsa item, int cantidad) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            ItemBolsa managed = em.find(ItemBolsa.class, item.getIdItem());
            if (managed != null) {
                managed.setCantidad(cantidad);
                em.merge(managed);
                em.flush();
            }
            em.getTransaction().commit();
            return managed != null;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            return false;
        } finally {
            em.close();
        }
    }

    /** UML: verificarStock(idPrenda, itTalla, cantidad): boolean */
    public boolean verificarStock(int idPrenda, int itTalla, int cantidad) {
        EntityManager em = emf.createEntityManager();
        try {
            StockTalla stock = em.createQuery(
                "SELECT st FROM StockTalla st WHERE st.prenda.idPrenda = :idPrenda AND st.talla = :talla",
                StockTalla.class
            )
            .setParameter("idPrenda", idPrenda)
            .setParameter("talla", itTalla)
            .getSingleResult();
            return stock != null && stock.getCantidad() >= cantidad;
        } catch (Exception e) {
            return false;
        } finally {
            em.close();
        }
    }

    public void cerrar() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}