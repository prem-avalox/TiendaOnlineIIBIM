package modelo.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import modelo.entidades.StockTalla;
import modelo.entidades.Talla;

/**
 * DAO para gestionar operaciones de persistencia de la entidad StockTalla
 * Según diagrama CU11.3 - Ajustar cantidad (flujo alterno verificar stock)
 */
public class StockTallaDAO {

    private EntityManagerFactory emf;

    public StockTallaDAO() {
        this.emf = Persistence.createEntityManagerFactory("persistencia");
    }

    /** UML: buscarStock(idPrenda, cantidad, idTalla): StockTalla */
    public StockTalla buscarStock(int idPrenda, int cantidad, int idTalla) {
        EntityManager em = emf.createEntityManager();
        try {
            Talla tallaEnum = resolveTalla(idTalla);
            if (tallaEnum == null) {
                return null;
            }
            return em.createQuery(
                "SELECT st FROM StockTalla st WHERE st.prenda.idPrenda = :idPrenda AND st.talla = :talla",
                StockTalla.class
            )
            .setParameter("idPrenda", idPrenda)
            .setParameter("talla", tallaEnum)
            .getSingleResult();
        } catch (Exception e) {
            return null;
        } finally {
            em.close();
        }
    }
    
    /** UML: actualizarCantidad(idStockTalla, cantidad): boolean */
    public boolean actualizarCantidad(int idStockTalla, int cantidad) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            StockTalla stock = em.find(StockTalla.class, idStockTalla);
            if (stock != null) {
                stock.setCantidad(cantidad);
                em.merge(stock);
                em.flush();
            }
            em.getTransaction().commit();
            return stock != null;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            return false;
        } finally {
            em.close();
        }
    }

    /** UML: descontarStock(idPrenda, cantidad): boolean */
    public boolean descontarStock(int idPrenda, int cantidad) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            StockTalla stock = em.createQuery(
                "SELECT st FROM StockTalla st WHERE st.prenda.idPrenda = :idPrenda",
                StockTalla.class
            )
            .setParameter("idPrenda", idPrenda)
            .setMaxResults(1)
            .getSingleResult();
            if (stock != null) {
                int nuevaCantidad = stock.getCantidad() - cantidad;
                stock.setCantidad(nuevaCantidad);
                em.merge(stock);
                em.flush();
            }
            em.getTransaction().commit();
            return stock != null;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            return false;
        } finally {
            em.close();
        }
    }

    private Talla resolveTalla(int idTalla) {
        Talla[] values = Talla.values();
        return (idTalla >= 0 && idTalla < values.length) ? values[idTalla] : null;
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