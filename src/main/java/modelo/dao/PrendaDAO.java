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

    public void guardar(String nombre,
                        String descripcion,
                        Categoria categoria,
                        double precio,
                        List<StockTalla> stockTallas,
                        String imagen) {
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
            
            resultados = em.createQuery(jpql, Prenda.class)
                           .setParameter(1, "%" + nombre + "%")
                           .getResultList();
                           
        } catch (Exception e) {
            System.out.println("Error en búsqueda por nombre: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
        return resultados;
    }

    public List<Prenda> filtrarPrenda(Talla tamano,
                                      String color,
                                      String corte) {
        return null;
    }

    public Categoria getCategoria() {
        return null;
    }

    public List<Prenda> buscarPrendas(Categoria categoria) {
        return null;
    }
}
