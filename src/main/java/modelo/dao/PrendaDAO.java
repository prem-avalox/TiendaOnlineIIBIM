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
import modelo.entidades.StockTalla;
import modelo.entidades.Talla;

public class PrendaDAO {

	private Connection conexion;

	private EntityManagerFactory emf;

	public PrendaDAO() {
		this.emf = Persistence.createEntityManagerFactory("persistencia");
	}

	//operaciones CRUD básicas

	public boolean insertar(Prenda prenda) {
		return false;
	}
	
	public Prenda buscarPorId(int id) {
		return null;
	}
	
	public void actualizar(Prenda prenda) {
		
	}
	
	public boolean eliminar(int id) {
		return false;
	}
	
	// Métodos de negocio

	public List<Prenda> getListaPrendas() {
	    EntityManager em = emf.createEntityManager();
	    try {
	        // JPQL: Recupera todas las prendas para el catálogo 
	        return em.createQuery("SELECT p FROM Prenda p", Prenda.class).getResultList();
	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    } finally {
	        em.close();
	    }
	}
	
	public List<Prenda> getListaPrendas(String nombre){
		return null;
	}
	
	public List<Prenda> filtrarPrendas(Talla talla, Color color, Corte corte){
		return null;
	}
	
	public List<Prenda> getListaPrendas(int idCategoria){
		return null;
	}
	
	public Prenda getPrenda(int idPrenda){
		return null;
	}

}