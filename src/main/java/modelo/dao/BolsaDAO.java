package modelo.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

import modelo.entidades.Bolsa;
import modelo.entidades.ItemBolsa;
import modelo.entidades.Prenda;
import modelo.entidades.StockTalla;
import modelo.entidades.Talla;
import modelo.entidades.Usuario;

public class BolsaDAO {

	private EntityManagerFactory emf;

	public BolsaDAO() {
		this.emf = Persistence.createEntityManagerFactory("persistencia");
	}

	/**
	 * Agrega un item a la bolsa del usuario
	 */
	public boolean agregarItemABolsa(int idUsuario, Prenda prenda, Talla talla, int cantidad) {

		EntityManager em = emf.createEntityManager();

		try {
			em.getTransaction().begin();

			// 🔑 usar otra variable (NO reasignar prenda)
			Prenda prendaEM = em.find(Prenda.class, prenda.getIdPrenda());
			if (prendaEM == null)
				return false;

			// 1. Obtener usuario
			Usuario usuario = em.find(Usuario.class, idUsuario);
			if (usuario == null)
				return false;

			// 2. Obtener o crear bolsa
			Bolsa bolsa = em.createQuery("SELECT b FROM Bolsa b WHERE b.usuario.idUsuario = :idUsuario", Bolsa.class)
					.setParameter("idUsuario", idUsuario).getResultStream().findFirst().orElse(null);

			if (bolsa == null) {
				bolsa = new Bolsa();
				bolsa.setUsuario(usuario);
				bolsa.setPrecioTotal(0);
				em.persist(bolsa);
			}

			// 3. Validar stock por talla
			StockTalla stock = prendaEM.getStockTallas().stream().filter(st -> st.getTalla() == talla).findFirst()
					.orElse(null);

			if (stock == null || stock.getCantidad() < cantidad) {
				return false;
			}

			// 4. Buscar item existente
			ItemBolsa item = bolsa.getItems().stream().filter(
					i -> i.getPrenda().getIdPrenda() == prendaEM.getIdPrenda() && i.getTallaSeleccionada() == talla)
					.findFirst().orElse(null);

			if (item != null) {
				item.setCantidad(item.getCantidad() + cantidad);
			} else {
				ItemBolsa nuevo = new ItemBolsa();
				nuevo.setPrenda(prendaEM);
				nuevo.setTallaSeleccionada(talla);
				nuevo.setCantidad(cantidad);
				nuevo.setBolsa(bolsa);

				bolsa.getItems().add(nuevo);
				em.persist(nuevo);
			}

			// 5. Descontar stock
			stock.setCantidad(stock.getCantidad() - cantidad);

			// 6. Recalcular total
			this.calcularTotal(bolsa);

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
	 * Obtiene la bolsa asociada a un usuario
	 */

	public Bolsa getBolsa(int idUsuario) {

		EntityManager em = emf.createEntityManager();

		try {
			Bolsa bolsa = em
					.createQuery("SELECT b FROM Bolsa b LEFT JOIN FETCH b.items i " + "LEFT JOIN FETCH i.prenda "
							+ "WHERE b.usuario.idUsuario = :idUsuario", Bolsa.class)
					.setParameter("idUsuario", idUsuario).getResultStream().findFirst().orElse(null);

			return bolsa;

		} finally {
			em.close();
		}
	}

	private void calcularTotal(Bolsa bolsa) {
		if (bolsa != null && bolsa.getItems() != null) {
			// Usamos stream para sumar (Precio * Cantidad) de cada ítem
			double total = bolsa.getItems().stream().mapToDouble(i -> i.getCantidad() * i.getPrenda().getPrecio())
					.sum();

			// Seteamos el total directamente en el objeto Bolsa
			bolsa.setPrecioTotal(total);
		}
	}

	public boolean eliminarItem(int idItem) {

		EntityManager em = emf.createEntityManager();

		try {
			em.getTransaction().begin();

			ItemBolsa item = em.find(ItemBolsa.class, idItem);
			if (item == null)
				return false;

			Bolsa bolsa = item.getBolsa();

			// 🔁 DEVOLVER STOCK
			StockTalla stock = em
					.createQuery("SELECT s FROM StockTalla s WHERE s.prenda.idPrenda = :idPrenda AND s.talla = :talla",
							StockTalla.class)
					.setParameter("idPrenda", item.getPrenda().getIdPrenda())
					.setParameter("talla", item.getTallaSeleccionada()).getSingleResult();

			stock.setCantidad(stock.getCantidad() + item.getCantidad());

			// 🗑️ ELIMINAR ITEM
			bolsa.getItems().remove(item);
			em.remove(item);

			this.calcularTotal(bolsa);

			em.getTransaction().commit();
			return true;

		} catch (Exception e) {
			if (em.getTransaction().isActive())
				em.getTransaction().rollback();
			e.printStackTrace();
			return false;

		} finally {
			em.close();
		}
	}

	public boolean ajustarItem(int idItem, int nuevaCantidad) {
		EntityManager em = emf.createEntityManager();
		try {
			em.getTransaction().begin();

			// 1.1.1: Llamamos a ItemBolsaDAO pasando el 'em' por parámetro
			ItemBolsaDAO itemDAO = new ItemBolsaDAO();
			boolean actualizado = itemDAO.actualizarCantidad(em, idItem, nuevaCantidad);

			if (actualizado) {
				// 1.1.2: Si hay éxito, calculamos el total
				ItemBolsa item = em.find(ItemBolsa.class, idItem);
				Bolsa bolsa = item.getBolsa();

				this.calcularTotal(bolsa);

				em.getTransaction().commit();
				return true;
			}

			return false; // stockDisponible = false
		} catch (Exception e) {
			if (em.getTransaction().isActive())
				em.getTransaction().rollback();
			return false;
		} finally {
			em.close();
		}
	}
}
