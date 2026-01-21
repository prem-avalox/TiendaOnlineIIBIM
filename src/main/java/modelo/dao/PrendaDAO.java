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

	// operaciones CRUD básicas

	public boolean insertar(Prenda prenda) {
		return false;
	}

	public boolean actualizar(Prenda prenda) {

		EntityManager em = emf.createEntityManager();

		try {
			em.getTransaction().begin();

			Prenda p = em.find(Prenda.class, prenda.getIdPrenda());

			// 🔴 No existe la prenda
			if (p == null) {
				return false;
			}

			// Datos simples
			p.setImagen(prenda.getImagen());
			p.setNombrePrenda(prenda.getNombrePrenda());
			p.setDescripcion(prenda.getDescripcion());
			p.setPrecio(prenda.getPrecio());
			p.setCategoria(prenda.getCategoria());
			p.setColor(prenda.getColor());
			p.setCorte(prenda.getCorte());

			// Stock por talla
			for (StockTalla stForm : prenda.getStockTallas()) {

				StockTalla existente = p.getStockTallas().stream().filter(s -> s.getTalla() == stForm.getTalla())
						.findFirst().orElse(null);

				if (existente != null) {
					existente.setCantidad(stForm.getCantidad());
				} else {
					StockTalla nuevo = new StockTalla(stForm.getCantidad(), stForm.getTalla());
					nuevo.setPrenda(p);
					p.getStockTallas().add(nuevo);
				}
			}

			em.getTransaction().commit();
			return true; // ✅ TODO OK

		} catch (Exception e) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			return false; // ❌ Error controlado
		} finally {
			em.close();
		}
	}

	public boolean eliminar(int id) {
		EntityManager em = emf.createEntityManager();
		try {
			em.getTransaction().begin();

			Prenda prenda = em.find(Prenda.class, id);
			if (prenda == null) {
				return false;
			}

			em.remove(prenda); // cascade + orphanRemoval hacen el resto
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

	public List<Prenda> getListaPrendas(String nombre) {
		return null;
	}

	public List<Prenda> filtrarPrendas(Talla talla, Color color, Corte corte) {
		return null;
	}

	public List<Prenda> getListaPrendas(int idCategoria) {
		return null;
	}

	public Prenda getPrenda(int idPrenda) {
		EntityManager em = emf.createEntityManager();
		try {
			// Usar JPQL con JOIN FETCH para cargar eagerly los stockTallas
			// Esto evita LazyInitializationException en la vista JSP
			TypedQuery<Prenda> query = em.createQuery(
				"SELECT p FROM Prenda p " +
				"LEFT JOIN FETCH p.stockTallas " +
				"WHERE p.idPrenda = :idPrenda", 
				Prenda.class
			);
			query.setParameter("idPrenda", idPrenda);
			
			return query.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			em.close();
		}
	}

}