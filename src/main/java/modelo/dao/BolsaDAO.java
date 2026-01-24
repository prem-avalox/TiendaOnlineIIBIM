package modelo.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

import modelo.entidades.Bolsa;
import modelo.entidades.ItemBolsa;
import modelo.entidades.Prenda;
import modelo.entidades.Talla;
import modelo.entidades.Usuario;

public class BolsaDAO {

    private EntityManagerFactory emf;

    public BolsaDAO() {
        this.emf = Persistence.createEntityManagerFactory("persistencia");
    }
    
    
    public boolean agregarItemABolsa(
            int idUsuario,
            Prenda prenda,
            Talla talla,
            int cantidad) {

        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            // 1. Obtener o crear la bolsa del usuario
            Bolsa bolsa = em.createQuery(
                    "SELECT b FROM Bolsa b WHERE b.usuario.idUsuario = :idUsuario",
                    Bolsa.class)
                    .setParameter("idUsuario", idUsuario)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);

            if (bolsa == null) {
                bolsa = new Bolsa();
                bolsa.setUsuario(em.getReference(Usuario.class, idUsuario));
                bolsa.setPrecioTotal(0.0);
                em.persist(bolsa);
            }

            // 2. Delegar la creación del item
            ItemBolsaDAO itemBolsaDAO = new ItemBolsaDAO(em);
            boolean agregado = itemBolsaDAO.agregarItem(
                    bolsa,
                    prenda,
                    talla,
                    cantidad
            );

            if (!agregado) {
                em.getTransaction().rollback();
                return false;
            }

            // 3. Recalcular total
            double total = calcularTotal(bolsa);
            bolsa.setPrecioTotal(total);

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
        Bolsa bolsa = null;

        try {
            String jpql = """
                SELECT b
                FROM Bolsa b
                LEFT JOIN FETCH b.items i
                LEFT JOIN FETCH i.prenda
                WHERE b.usuario.idUsuario = :idUsuario
            """;

            TypedQuery<Bolsa> query = em.createQuery(jpql, Bolsa.class);
            query.setParameter("idUsuario", idUsuario);

            bolsa = query.getResultStream().findFirst().orElse(null);

            // Calcular total dentro del flujo
            if (bolsa != null) {
                bolsa.setPrecioTotal(calcularTotal(bolsa));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }

        return bolsa;
    }

    /**
     * Calcula el total de una bolsa
     */
    public double calcularTotal(Bolsa bolsa) {

        double total = 0.0;

        if (bolsa == null || bolsa.getItems() == null) {
            return total;
        }

        for (ItemBolsa item : bolsa.getItems()) {
            if (item.getPrenda() != null) {
                total += item.getCantidad() * item.getPrenda().getPrecio();
            }
        }

        return total;
    }
    
    
    public boolean eliminarItem(int idItem) {

        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            // 1. Buscar el item
            ItemBolsa item = em.find(ItemBolsa.class, idItem);

            if (item == null) {
                return false;
            }

            // 2. Obtener la bolsa asociada
            Bolsa bolsa = item.getBolsa();

            // 3. Romper la relación bidireccional
            bolsa.getItems().remove(item);
            em.remove(item);

            // 4. Recalcular total
            double total = calcularTotal(bolsa);
            bolsa.setPrecioTotal(total);

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
     * Ajusta la cantidad de un item de la bolsa
     * Orquesta el flujo completo según el diagrama
     */
    public boolean ajustarItem(int idItem, int nuevaCantidad) {

        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            // 1. Delegar la actualización del item
            ItemBolsaDAO itemBolsaDAO = new ItemBolsaDAO(em);
            boolean actualizado = itemBolsaDAO.actualizarCantidad(idItem, nuevaCantidad);

            if (!actualizado) {
                em.getTransaction().rollback();
                return false;
            }

            // 2. Recalcular total de la bolsa
            ItemBolsa item = em.find(ItemBolsa.class, idItem);
            Bolsa bolsa = item.getBolsa();

            double total = calcularTotal(bolsa);
            bolsa.setPrecioTotal(total);

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
    
    
}
