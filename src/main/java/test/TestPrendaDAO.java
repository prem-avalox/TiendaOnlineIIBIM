package test;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import modelo.entidades.Prenda;

public class TestPrendaDAO {
	public static void main(String[] args) {
		System.out.println("=== TEST: Verificando conexión y carga de prendas ===\n");
		
		EntityManagerFactory emf = null;
		EntityManager em = null;
		
		try {
			// 1. Crear EntityManagerFactory
			System.out.println("1. Creando EntityManagerFactory...");
			emf = Persistence.createEntityManagerFactory("persistencia");
			System.out.println("   ✓ EntityManagerFactory creado exitosamente\n");
			
			// 2. Crear EntityManager
			System.out.println("2. Creando EntityManager...");
			em = emf.createEntityManager();
			System.out.println("   ✓ EntityManager creado exitosamente\n");
			
			// 3. Consultar prendas
			System.out.println("3. Ejecutando consulta JPQL: SELECT p FROM Prenda p");
			List<Prenda> prendas = em.createQuery("SELECT p FROM Prenda p", Prenda.class).getResultList();
			System.out.println("   ✓ Consulta ejecutada exitosamente\n");
			
			// 4. Mostrar resultados
			System.out.println("4. RESULTADOS:");
			System.out.println("   Total de prendas encontradas: " + (prendas != null ? prendas.size() : 0));
			
			if (prendas == null || prendas.isEmpty()) {
				System.out.println("   ✗ NO HAY PRENDAS EN LA BASE DE DATOS");
				System.out.println("\n   Verifica:");
				System.out.println("   - Que la base de datos 'tienda_online' existe");
				System.out.println("   - Que la tabla 'Prenda' tiene datos");
				System.out.println("   - Que la configuración en persistence.xml es correcta");
			} else {
				System.out.println("   ✓ Prendas encontradas:\n");
				for (Prenda prenda : prendas) {
					System.out.println("      ID: " + prenda.getIdPrenda() + 
							         " | Nombre: " + prenda.getNombrePrenda() + 
							         " | Precio: $" + prenda.getPrecio() +
							         " | Categoría: " + prenda.getCategoria());
				}
			}
			
		} catch (Exception e) {
			System.out.println("   ✗ ERROR: " + e.getMessage());
			e.printStackTrace();
		} finally {
			if (em != null) {
				em.close();
				System.out.println("\n5. EntityManager cerrado");
			}
			if (emf != null) {
				emf.close();
				System.out.println("6. EntityManagerFactory cerrado");
			}
		}
		
		System.out.println("\n=== FIN DEL TEST ===");
	}
}
