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

	private EntityManagerFactory emf;

	public PrendaDAO() {
		this.emf = Persistence.createEntityManagerFactory("persistencia");
	}

	// operaciones CRUD básicas

	public boolean insertar(Prenda prenda) {
	    EntityManager em = emf.createEntityManager();
	    try {
	        em.getTransaction().begin();
	        if (prenda.getStockTallas() != null) {
	            for (StockTalla st : prenda.getStockTallas()) {
	                st.setPrenda(prenda); 
	            }
	        }

	        em.persist(prenda);

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
			return true; 

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
	    EntityManager em = emf.createEntityManager();
	    List<Prenda> prendas = null;
	    try {
	        String jpql = "SELECT p FROM Prenda p WHERE p.nombrePrenda LIKE :nombre";
	        TypedQuery<Prenda> query = em.createQuery(jpql, Prenda.class);
	        query.setParameter("nombre", "%" + nombre + "%");
	        
	        prendas = query.getResultList();
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        em.close();
	    }
	    return prendas;
	}
	
	public List<Prenda> filtrarPrendas(Talla talla, Color color, Corte corte) {
	    EntityManager em = emf.createEntityManager();
	    List<Prenda> resultados = null;
	    try {
	        StringBuilder jpql = new StringBuilder("SELECT DISTINCT p FROM Prenda p JOIN p.stockTallas s WHERE 1=1");

	        if (talla != null) {
	            jpql.append(" AND s.talla = :talla");
	        }
	        if (color != null) {
	            jpql.append(" AND p.color = :color");
	        }
	        if (corte != null) {
	            jpql.append(" AND p.corte = :corte");
	        }

	        TypedQuery<Prenda> query = em.createQuery(jpql.toString(), Prenda.class);

	        if (talla != null) query.setParameter("talla", talla);
	        if (color != null) query.setParameter("color", color);
	        if (corte != null) query.setParameter("corte", corte);

	        resultados = query.getResultList();
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        em.close();
	    }
	    return resultados;
	}

	public List<Prenda> getListaPrendas(int idCategoria) {
	    EntityManager em = emf.createEntityManager();
	    List<Prenda> resultados = null;
	    try {
	        Categoria categoriaEnum = Categoria.values()[idCategoria];

	        String jpql = "SELECT p FROM Prenda p WHERE p.categoria = :cat";
	        TypedQuery<Prenda> query = em.createQuery(jpql, Prenda.class);
	        query.setParameter("cat", categoriaEnum);

	        resultados = query.getResultList();
	    } catch (Exception e) {
	        System.out.println("Error al buscar prendas por categoría: " + e.getMessage());
	        e.printStackTrace();
	    } finally {
	        em.close();
	    }
	    return resultados;
	}

	public Prenda getPrenda(int idPrenda) {
		EntityManager em = emf.createEntityManager();
		try {
			return em.find(Prenda.class, idPrenda);
		} finally {
			em.close();
		}
	}

}