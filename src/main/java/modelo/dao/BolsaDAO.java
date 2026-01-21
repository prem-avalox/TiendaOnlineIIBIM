package modelo.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import modelo.entidades.Bolsa;

/**
 * DAO para gestionar operaciones de persistencia de la entidad Bolsa
 * Según diagrama CU11 - Ver Bolsa
 */
public class BolsaDAO {

    private EntityManagerFactory emf;

    public BolsaDAO() {
        this.emf = Persistence.createEntityManagerFactory("persistencia");
    }

    /**
     * Busca la bolsa activa de un usuario
     * Según diagrama de secuencia: buscarBolsaPorUsuario(idUsuario):Bolsa
     * @param idUsuario ID del usuario
     * @return Bolsa del usuario o null si no existe
     */
    public Bolsa buscarBolsaPorUsuario(int idUsuario) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Bolsa> query = em.createQuery(
                "SELECT b FROM Bolsa b WHERE b.usuario.idUsuario = :idUsuario",
                Bolsa.class
            );
            query.setParameter("idUsuario", idUsuario);
            Bolsa bolsa = query.getSingleResult();
            // Forzar carga de items/prenda antes de cerrar EM (necesario para uso posterior)
            if (bolsa != null) {
                bolsa.getItems().size();
                bolsa.getItems().forEach(item -> {
                    if (item.getPrenda() != null) {
                        item.getPrenda().getNombrePrenda();
                    }
                });
            }
            return bolsa;
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

    /**
     * Guarda una nueva bolsa en la base de datos
     * @param bolsa Bolsa a guardar
     * @return true si se guardó correctamente, false en caso contrario
     */
    public boolean guardarBolsa(Bolsa bolsa) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(bolsa);
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

    /**
     * Actualiza una bolsa existente
     * @param bolsa Bolsa a actualizar
     * @return Bolsa actualizada
     */
    public Bolsa actualizarBolsa(Bolsa bolsa) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Bolsa merged = em.merge(bolsa);
            em.flush();
            em.getTransaction().commit();
            return merged;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            return null;
        } finally {
            em.close();
        }
    }

    /**
     * Cierra/elimina una bolsa
     * @param idBolsa ID de la bolsa a cerrar
     * @return true si se cerró correctamente, false en caso contrario
     */
    public boolean cerrarBolsa(int idBolsa) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Bolsa bolsa = em.find(Bolsa.class, idBolsa);
            if (bolsa != null) {
                em.remove(bolsa);
            }
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

    /**
     * Cierra el EntityManagerFactory
     */
    public void cerrar() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}