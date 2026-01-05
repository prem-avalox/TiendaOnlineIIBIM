package modelo.dao;

import java.sql.Connection;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
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

	public boolean insertar(Prenda prenda) {
		return false;
	}

	public void actualizar(String idPrendaStr, String nombre, String descripcion, Categoria categoria, double precio,
			List<StockTalla> nuevosStockTallas, String imagen, String color, String corte) {

		EntityManager em = emf.createEntityManager();
		try {
			em.getTransaction().begin();

			// Convertimos el String a int (Casteo en el DAO)
			int idPrenda = Integer.parseInt(idPrendaStr);

			// 1. Buscamos la prenda existente (Paso 3.1 del diagrama)
			Prenda prendaExistente = em.find(Prenda.class, idPrenda);

			if (prendaExistente != null) {
				// 2. Actualizamos los atributos
				prendaExistente.setNombrePrenda(nombre);
				prendaExistente.setDescripcion(descripcion);
				prendaExistente.setCategoria(categoria);
				prendaExistente.setPrecio(precio);
				prendaExistente.setImagen(imagen);
				prendaExistente.setColor(color);
				prendaExistente.setCorte(corte);

				// 3. Actualizamos el Stock
				// orphanRemoval = true se encarga de eliminar las tallas que quitemos de la
				// lista
				prendaExistente.getStockTallas().clear();

				if (nuevosStockTallas != null) {
					for (StockTalla st : nuevosStockTallas) {
						st.setPrenda(prendaExistente);
						prendaExistente.getStockTallas().add(st);
					}
				}

				// JPA sincroniza los cambios automáticamente al hacer commit
				em.getTransaction().commit();
			}

		} catch (Exception e) {
			if (em.getTransaction().isActive())
				em.getTransaction().rollback();
			throw e;
		} finally {
			em.close();
		}
	}

	public boolean eliminar(int id) {
	    EntityManager em = emf.createEntityManager();
	    try {
	        em.getTransaction().begin();
	        // 1. Buscar la prenda (Paso 5.2 del diagrama)
	        Prenda p = em.find(Prenda.class, id);
	        if (p != null) {
	            em.remove(p); // Se elimina físicamente de la BD
	            em.getTransaction().commit();
	            return true;
	        }
	        return false;
	    } catch (Exception e) {
	        if (em.getTransaction().isActive()) em.getTransaction().rollback();
	        e.printStackTrace();
	        return false;
	    } finally {
	        em.close();
	    }
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
		EntityManager em = emf.createEntityManager();
		List<Prenda> resultados = null;
		try {
			String jpql = "SELECT p FROM Prenda p WHERE p.nombrePrenda LIKE ?1";

			resultados = em.createQuery(jpql, Prenda.class).setParameter(1, "%" + nombre + "%").getResultList();

		} catch (Exception e) {
			System.out.println("Error en búsqueda por nombre: " + e.getMessage());
			e.printStackTrace();
		} finally {
			em.close();
		}
		return resultados;
	}

	public List<Prenda> filtrarPrenda(String tallaStr, String color, String corte) {
		EntityManager em = emf.createEntityManager();
		List<Prenda> resultados = null;
		try {
			StringBuilder jpql = new StringBuilder("SELECT DISTINCT p FROM Prenda p JOIN p.stockTallas s WHERE 1=1");

			// 1. manejo de Talla (Conversión de String a Enum Talla)
			if (tallaStr != null && !tallaStr.isEmpty()) {
				jpql.append(" AND s.talla = :talla");
			}
			if (color != null && !color.isEmpty()) {
				jpql.append(" AND p.color = :color");
			}
			if (corte != null && !corte.isEmpty()) {
				jpql.append(" AND p.corte = :corte");
			}

			TypedQuery<Prenda> query = em.createQuery(jpql.toString(), Prenda.class);

			// 2. asignación de parámetros con conversión segura
			if (tallaStr != null && !tallaStr.isEmpty()) {
				// se convierte el String a Enum Talla
				query.setParameter("talla", modelo.entidades.Talla.valueOf(tallaStr));
			}
			if (color != null && !color.isEmpty())
				query.setParameter("color", color);
			if (corte != null && !corte.isEmpty())
				query.setParameter("corte", corte);

			resultados = query.getResultList();
		} catch (Exception e) {
			System.out.println("Error en filtrarPrenda: " + e.getMessage());
			e.printStackTrace();
		} finally {
			em.close();
		}
		return resultados;
	}

	public List<Prenda> buscarPrendas(String idCategoriaStr) {
		EntityManager em = emf.createEntityManager();
		List<Prenda> resultados = null;
		try {
			// convertir string a enum
			Categoria categoriaEnum = Categoria.valueOf(idCategoriaStr);

			// JPQL: Buscamos prendas filtrando por el objeto Enum
			String jpql = "SELECT p FROM Prenda p WHERE p.categoria = :cat";

			resultados = em.createQuery(jpql, Prenda.class).setParameter("cat", categoriaEnum).getResultList();

		} catch (Exception e) {
			System.out.println("Error al buscar prendas por categoría: " + e.getMessage());
			e.printStackTrace();
		} finally {
			em.close();
		}
		return resultados;
	}

	public Prenda buscarPrenda(String idStr) {
		EntityManager em = emf.createEntityManager();
		Prenda prenda = null;
		try {
			// convertir String a int
			int idInt = Integer.parseInt(idStr);

			// busca por la llave primaria numérica
			prenda = em.find(Prenda.class, idInt);
		} catch (NumberFormatException | NullPointerException e) {
			System.out.println("ID inválido recibido en el DAO: " + idStr);
		} finally {
			em.close();
		}
		return prenda;
	}

}