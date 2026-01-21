package modelo.dao;

import java.sql.Connection;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import modelo.entidades.Categoria;
import modelo.entidades.Color;
import modelo.entidades.Corte;
import modelo.entidades.Prenda;
import modelo.entidades.Talla;

public class PrendaDAO {

    private EntityManagerFactory emf;

    public PrendaDAO() {
        this.emf = Persistence.createEntityManagerFactory("persistencia");
    }

    /** UML: insertar(prenda): boolean */
    public boolean insertar(Prenda prenda) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(prenda);
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

    /** UML: actualizar(prenda): boolean */
    public boolean actualizar(Prenda prenda) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Prenda managed = em.find(Prenda.class, prenda.getIdPrenda());
            if (managed == null) {
                em.getTransaction().rollback();
                return false;
            }
            managed.setImagen(prenda.getImagen());
            managed.setNombrePrenda(prenda.getNombrePrenda());
            managed.setDescripcion(prenda.getDescripcion());
            managed.setPrecio(prenda.getPrecio());
            managed.setCategoria(prenda.getCategoria());
            managed.setColor(prenda.getColor());
            managed.setCorte(prenda.getCorte());
            managed.setStockTallas(prenda.getStockTallas());
            em.merge(managed);
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

    /** UML: eliminar(idPrenda): boolean */
    public boolean eliminar(int idPrenda) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Prenda prenda = em.find(Prenda.class, idPrenda);
            if (prenda != null) {
                em.remove(prenda);
            }
            em.getTransaction().commit();
            return prenda != null;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            return false;
        } finally {
            em.close();
        }
    }

    /** UML: getListaPrendas(): List<Prenda> */
    public List<Prenda> getListaPrendas() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT p FROM Prenda p", Prenda.class).getResultList();
        } catch (Exception e) {
            return null;
        } finally {
            em.close();
        }
    }

    /** UML: getListaPrendas(nombre): List<Prenda> */
    public List<Prenda> getListaPrendas(String nombre) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                "SELECT p FROM Prenda p WHERE LOWER(p.nombrePrenda) LIKE LOWER(:nombre)",
                Prenda.class
            )
            .setParameter("nombre", "%" + nombre + "%")
            .getResultList();
        } catch (Exception e) {
            return null;
        } finally {
            em.close();
        }
    }

    /** UML: getListaPrendas(categoria): List<Prenda> */
    public List<Prenda> getListaPrendas(Categoria categoria) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                "SELECT p FROM Prenda p WHERE p.categoria = :categoria",
                Prenda.class
            )
            .setParameter("categoria", categoria)
            .getResultList();
        } catch (Exception e) {
            return null;
        } finally {
            em.close();
        }
    }

    /** UML: filtrarPrendas(talla, color, corte): List<Prenda> */
    public List<Prenda> filtrarPrendas(Talla talla, Color color, Corte corte) {
        EntityManager em = emf.createEntityManager();
        try {
            String jpql = "SELECT p FROM Prenda p WHERE (:talla IS NULL OR EXISTS (SELECT st FROM StockTalla st WHERE st.prenda = p AND st.talla = :talla)) " +
                          "AND (:color IS NULL OR p.color = :color) " +
                          "AND (:corte IS NULL OR p.corte = :corte)";
            TypedQuery<Prenda> query = em.createQuery(jpql, Prenda.class);
            query.setParameter("talla", talla);
            query.setParameter("color", color);
            query.setParameter("corte", corte);
            return query.getResultList();
        } catch (Exception e) {
            return null;
        } finally {
            em.close();
        }
    }

    /** UML: getPrenda(idPrenda): Prenda */
    public Prenda getPrenda(int idPrenda) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Prenda> query = em.createQuery(
                "SELECT p FROM Prenda p LEFT JOIN FETCH p.stockTallas WHERE p.idPrenda = :idPrenda",
                Prenda.class
            );
            query.setParameter("idPrenda", idPrenda);
            return query.getSingleResult();
        } catch (Exception e) {
            return null;
        } finally {
            em.close();
        }
    }
}