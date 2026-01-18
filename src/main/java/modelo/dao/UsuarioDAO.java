package modelo.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import modelo.entidades.Usuario;

/**
 * DAO para gestionar operaciones de persistencia de la entidad Usuario
 */
public class UsuarioDAO {

    private EntityManagerFactory emf;

    public UsuarioDAO() {
        this.emf = Persistence.createEntityManagerFactory("persistencia");
    }

    /**
     * Busca un usuario por su nombre de usuario
     * @param nombreUsuario Nombre de usuario a buscar
     * @return Usuario encontrado o null si no existe
     */
    public Usuario buscarPorNombreUsuario(String nombreUsuario) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Usuario> query = em.createQuery(
                "SELECT u FROM Usuario u WHERE u.nombreUsuario = :nombreUsuario", 
                Usuario.class
            );
            query.setParameter("nombreUsuario", nombreUsuario);
            
            return query.getSingleResult();
        } catch (NoResultException e) {
            // Usuario no encontrado
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            em.close();
        }
    }

    /**
     * Busca un usuario por su ID
     * @param idUsuario ID del usuario
     * @return Usuario encontrado o null si no existe
     */
    public Usuario buscarPorId(int idUsuario) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Usuario.class, idUsuario);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            em.close();
        }
    }

    /**
     * Busca un usuario por email y contraseña (para login)
     * @param email Email del usuario
     * @param contrasena Contraseña del usuario
     * @return Usuario encontrado o null si credenciales incorrectas
     */
    public Usuario buscarPorEmailYContrasena(String email, String contrasena) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Usuario> query = em.createQuery(
                "SELECT u FROM Usuario u WHERE u.email = :email AND u.contrasena = :contrasena", 
                Usuario.class
            );
            query.setParameter("email", email);
            query.setParameter("contrasena", contrasena);
            
            return query.getSingleResult();
        } catch (NoResultException e) {
            // Credenciales incorrectas
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            em.close();
        }
    }

    /**
     * Guarda un nuevo usuario
     * @param usuario Usuario a guardar
     * @return true si se guardó correctamente
     */
    public boolean guardar(Usuario usuario) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(usuario);
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
     * Actualiza un usuario existente
     * @param usuario Usuario a actualizar
     * @return Usuario actualizado
     */
    public Usuario actualizar(Usuario usuario) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Usuario usuarioActualizado = em.merge(usuario);
            em.getTransaction().commit();
            return usuarioActualizado;
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
     * Cierra el EntityManagerFactory
     */
    public void cerrar() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}
