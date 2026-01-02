package modelo.dao;

import java.sql.Connection;
import java.util.List;

import modelo.entidades.Categoria;
import modelo.entidades.Prenda;
import modelo.entidades.StockTalla;
import modelo.entidades.Talla;

public class PrendaDAO {

    private Connection conexion;


    public PrendaDAO() {
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
        return null;
    }

    public List<Prenda> getListaPrendas(String nombre) {
        return null;
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
