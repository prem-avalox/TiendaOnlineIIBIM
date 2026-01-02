package test;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import modelo.entidades.Bolsa;
import modelo.entidades.Usuario;

public class ORMTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistencia");
		EntityManager em = emf.createEntityManager();
		
		//Insertar
		Usuario user = new Usuario (0, "user", "user@gmail.com", "1234", null);
		
		em.getTransaction().begin();
		em.persist(user);
		em.getTransaction().commit();
	}

}
