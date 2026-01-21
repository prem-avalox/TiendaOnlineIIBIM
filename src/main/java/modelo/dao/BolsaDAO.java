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
            
            System.out.println("   - Ejecutando query JPQL...");
            
            // Primero buscar la bolsa
            TypedQuery<Bolsa> query = em.createQuery(
                "SELECT b FROM Bolsa b WHERE b.usuario.idUsuario = :idUsuario", 
                Bolsa.class
            );
            query.setParameter("idUsuario", idUsuario);
            
            Bolsa bolsa = query.getSingleResult();
            
            if (bolsa != null) {
                System.out.println("   ✅ Bolsa encontrada:");
                System.out.println("      - ID Bolsa: " + bolsa.getIdBolsa());
                System.out.println("      - Precio Total: $" + bolsa.getPrecioTotal());
                
                // ✅ IMPORTANTE: Forzar la inicialización de la colección ANTES de cerrar el EM
                // Esto evita LazyInitializationException cuando se accede a los items después
                int size = bolsa.getItems().size();
                System.out.println("      - Número de items: " + size);
                
                // Verificar cada item Y forzar la carga de sus relaciones
                for (ItemBolsa item : bolsa.getItems()) {
                    System.out.println("         • Item ID: " + item.getIdItem() + 
                                     " | Cantidad: " + item.getCantidad() +
                                     " | Talla: " + item.getTallaSeleccionada());
                    
                    // ✅ Forzar carga de la prenda para evitar lazy loading posterior
                    if (item.getPrenda() != null) {
                        System.out.println("           Prenda: " + item.getPrenda().getNombrePrenda());
                        // Acceder a propiedades importantes para inicializarlas
                        item.getPrenda().getPrecio();
                        item.getPrenda().getImagen();
                    }
                }
                System.out.println("      - Todos los items completamente inicializados");
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
     * Busca una bolsa por su ID
     * @param idBolsa ID de la bolsa
     * @return Bolsa encontrada o null si no existe
     */
    public Bolsa buscarPorId(int idBolsa) {
        EntityManager em = emf.createEntityManager();
        try {
            System.out.println("\n🔍 DEBUG - BolsaDAO.buscarPorId:");
            System.out.println("   - Buscando bolsa ID: " + idBolsa);
            
            // Buscar por ID usando find() - con FetchType.EAGER cargará automáticamente los items
            Bolsa bolsa = em.find(Bolsa.class, idBolsa);
            
            if (bolsa != null) {
                // ✅ IMPORTANTE: Forzar la inicialización de la colección ANTES de cerrar el EM
                // Esto evita LazyInitializationException cuando se accede a los items después
                bolsa.getItems().size(); // Esto fuerza la carga de todos los items
                
                System.out.println("   ✅ Bolsa encontrada - ID: " + bolsa.getIdBolsa());
                System.out.println("      - Número de items: " + bolsa.getItems().size());
                
                // Inicializar también las prendas de cada item (para evitar lazy loading posterior)
                for (ItemBolsa item : bolsa.getItems()) {
                    if (item.getPrenda() != null) {
                        item.getPrenda().getNombrePrenda(); // Forzar carga de la prenda
                    }
                }
                System.out.println("      - Items completamente inicializados");
            } else {
                System.out.println("   ❌ No se encontró bolsa con ID: " + idBolsa);
            }
            
            return bolsa;
        } catch (Exception e) {
            System.err.println("   ❌ ERROR en buscarPorId:");
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
            em.flush(); // ✅ FORZAR escritura inmediata
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
     * Alias para guardarBolsa
     * @param bolsa Bolsa a guardar
     * @return true si se guardó correctamente, false en caso contrario
     */
    public boolean guardar(Bolsa bolsa) {
        return guardarBolsa(bolsa);
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
            em.flush(); // ✅ FORZAR escritura inmediata
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
     * Alias para actualizarBolsa
     * @param bolsa Bolsa a actualizar
     * @return Bolsa actualizada
     */
    public Bolsa actualizar(Bolsa bolsa) {
        return actualizarBolsa(bolsa);
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