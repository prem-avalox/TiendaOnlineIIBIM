package modelo.dao;

import java.sql.Connection;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import modelo.entidades.Categoria;
import modelo.entidades.Prenda;
import modelo.entidades.StockTalla;
import modelo.entidades.Talla;

public class PrendaDAO {

	private Connection conexion;

	private EntityManagerFactory emf;

	public PrendaDAO() {
		this.emf = Persistence.createEntityManagerFactory("persistencia");
	}

	public Prenda buscarPrenda(int id) {
		return null;
	}

	public boolean insertar(Prenda prenda) {
		return false;
	}

	public boolean actualizar(Prenda prenda) {
		return false;
	}

	public boolean eliminar(int id) {
		return false;
	}

	// Métodos de negocio

	public void guardar(String nombre, String descripcion, Categoria categoria, double precio,
			List<StockTalla> stockTallas, String imagen, String color, String corte) {

		EntityManager em = emf.createEntityManager();
		try {
			em.getTransaction().begin();

			Prenda prenda = new Prenda();
			prenda.setNombrePrenda(nombre);
			prenda.setDescripcion(descripcion);
			prenda.setCategoria(categoria);
			prenda.setPrecio(precio);
			prenda.setImagen(imagen);
			prenda.setColor(color);
			prenda.setCorte(corte);

// Relación bidireccional (muy importante)
			if (stockTallas != null) {
				for (StockTalla st : stockTallas) {
					st.setPrenda(prenda);
				}
				prenda.setStockTallas(stockTallas);
			}

			em.persist(prenda); // Cascade.ALL guardará StockTalla también
			em.getTransaction().commit();

		} catch (Exception e) {
			if (em.getTransaction().isActive())
				em.getTransaction().rollback();
			throw e;
		} finally {
			em.close();
		}
	}

	public List<Prenda> getListaPrendas() {
		EntityManager em = emf.createEntityManager();
		try {
			// JPQL: Selecciona el objeto completo Prenda
			return em.createQuery("SELECT p FROM Prenda p", Prenda.class).getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			em.close();
		}
	}

	public List<Prenda> getListaPrendas(String nombre) {
		return null;
	}

	public List<Prenda> filtrarPrenda(Talla tamano, String color, String corte) {
		return null;
	}

	public Categoria getCategoria() {
		return null;
	}

	public List<Prenda> buscarPrendas(Categoria categoria) {
		return null;
	}
}
