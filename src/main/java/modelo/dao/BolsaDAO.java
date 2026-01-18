package modelo.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import modelo.entidades.Bolsa;
import modelo.entidades.ItemBolsa;

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
            System.out.println("\n🔍 DEBUG - BolsaDAO.buscarBolsaPorUsuario:");
            System.out.println("   - Buscando bolsa para usuario ID: " + idUsuario);
            
            // JPQL: Buscar bolsa con TODOS los datos necesarios cargados
            // LEFT JOIN FETCH b.items i -> Carga los items
            // LEFT JOIN FETCH i.prenda p -> Carga las prendas de cada item
            // Esto evita el problema de LazyInitializationException
            TypedQuery<Bolsa> query = em.createQuery(
                "SELECT DISTINCT b FROM Bolsa b " +
                "LEFT JOIN FETCH b.items i " +
                "LEFT JOIN FETCH i.prenda p " +
                "WHERE b.usuario.idUsuario = :idUsuario", 
                Bolsa.class
            );
            query.setParameter("idUsuario", idUsuario);
            
            System.out.println("   - Ejecutando query JPQL...");
            Bolsa bolsa = query.getSingleResult();
            
            System.out.println("   ✅ Bolsa encontrada:");
            System.out.println("      - ID Bolsa: " + bolsa.getIdBolsa());
            System.out.println("      - Precio Total: $" + bolsa.getPrecioTotal());
            
            // Forzar la inicialización de la colección (por si acaso)
            if (bolsa != null && bolsa.getItems() != null) {
                int size = bolsa.getItems().size(); // Esto fuerza la carga de la colección
                System.out.println("      - Número de items: " + size);
                
                // Verificar cada item
                for (ItemBolsa item : bolsa.getItems()) {
                    System.out.println("         • Item ID: " + item.getIdItem() + 
                                     " | Prenda: " + (item.getPrenda() != null ? item.getPrenda().getNombrePrenda() : "NULL"));
                }
            }
            
            return bolsa;
        } catch (NoResultException e) {
            // No existe bolsa para este usuario
            System.out.println("   ❌ No se encontró bolsa para usuario ID: " + idUsuario);
            return null;
        } catch (Exception e) {
            System.err.println("   ❌ ERROR en buscarBolsaPorUsuario:");
            e.printStackTrace();
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
     * Actualiza una bolsa existente
     * @param bolsa Bolsa a actualizar
     * @return Bolsa actualizada
     */
    public Bolsa actualizarBolsa(Bolsa bolsa) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Bolsa bolsaActualizada = em.merge(bolsa);
            em.getTransaction().commit();
            return bolsaActualizada;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
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
