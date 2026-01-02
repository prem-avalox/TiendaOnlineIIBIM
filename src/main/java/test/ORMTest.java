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
		
		Bolsa miBolsa = new Bolsa();
		miBolsa.setPrecioTotal(0.0);

		// 2. Crear el Usuario y ASIGNARLE la bolsa
		Usuario miUsuario = new Usuario();
		miUsuario.setNombreUsuario("user");
		miUsuario.setEmail("user@gmail.com");
		miUsuario.setContrasena("1234");
		miUsuario.setBolsa(miBolsa); // <--- ESTO ES LO QUE FALTA O ESTÁ LLEGANDO NULL

		// 3. Persistir
		em.getTransaction().begin();
		em.persist(miUsuario); // Gracias al CascadeType.ALL en Usuario, persistirá la Bolsa automáticamente
		em.getTransaction().commit();
	}

}
