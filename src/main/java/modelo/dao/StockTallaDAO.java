package modelo.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
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

    /**
     * Busca el stock de una prenda en una talla específica
     * Según diagrama de secuencia CU11.3: buscarStock(idPrenda, talla):StockTalla
     * @param idPrenda ID de la prenda
     * @param talla Talla a buscar
     * @return StockTalla encontrado o null si no existe
     */
    public StockTalla buscarStock(int idPrenda, Talla talla) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                "SELECT st FROM StockTalla st " +
                "WHERE st.prenda.idPrenda = :idPrenda " +
                "AND st.talla = :talla", 
                StockTalla.class
            )
            .setParameter("idPrenda", idPrenda)
            .setParameter("talla", talla)
            .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            em.close();
        }
    }

    /**
     * Actualiza el stock de una prenda
     * @param stock StockTalla a actualizar
     * @return true si se actualizó correctamente, false en caso contrario
     */
    public boolean actualizarStock(StockTalla stock) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(stock);
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
     * Verifica si hay stock disponible
     * Según diagrama: validarStock(cantidad):boolean
     * @param stock StockTalla a verificar
     * @param cantidad Cantidad solicitada
     * @return true si hay stock suficiente, false en caso contrario
     */
    public boolean validarStock(StockTalla stock, int cantidad) {
        return stock != null && stock.getCantidad() >= cantidad;
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
