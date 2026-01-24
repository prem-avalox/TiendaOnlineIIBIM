package modelo.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.NoResultException;
import modelo.entidades.Usuario;

public class UsuarioDAO {
	private EntityManagerFactory emf;

	public UsuarioDAO() {
		this.emf = Persistence.createEntityManagerFactory("persistencia");
	}
	

	
	public boolean insertar(Usuario usuario) {
		EntityManager em = emf.createEntityManager();
		try {
			em.getTransaction().begin();
			em.persist(usuario); 
			em.getTransaction().commit();
			return true;
		} catch (Exception e) {
			if (em.getTransaction().isActive()) em.getTransaction().rollback();
			e.printStackTrace();
			return false;
		} finally {
			em.close();
		}
	}
	
	//metodos del negocio
	
	public boolean verificarCredenciales(String nombre, String correo) {
		EntityManager em = emf.createEntityManager();
		try {
			String jpql = "SELECT u FROM Usuario u WHERE u.email = :correo OR u.nombreUsuario = :nombre";
			Usuario u = em.createQuery(jpql, Usuario.class)
					.setParameter("correo", correo)
					.setParameter("nombre", nombre)
					.getSingleResult();
			
			return false; 
		} catch (NoResultException e) {
			return true; 
		} finally {
			em.close();
		}
	}
	
	public Usuario autenticar(String usuario, String contrasena) {
	    EntityManager em = emf.createEntityManager();
	    try {
	        String jpql = "SELECT u FROM Usuario u WHERE (u.nombreUsuario = :user OR u.email = :user) AND u.contrasena = :pass";
	        return em.createQuery(jpql, Usuario.class)
	                 .setParameter("user", usuario)
	                 .setParameter("pass", contrasena)
	                 .getSingleResult();
	    } catch (NoResultException e) {
	        return null; 
	    } finally {
	        em.close();
	    }
	}
}