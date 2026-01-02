package modelo.entidades;

import java.io.Serializable;

public enum Categoria implements Serializable {

    CAMISAS("Camisas"),
    CALZADO("Calzado"),
    ACCESORIOS("Accesorios"),
    PANTALONES("Pantalones");

    private static final long serialVersionUID = 1L;

    private final String nombreCategoria;

    private Categoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }

    public String getNombreCategoria() {
        return nombreCategoria;
    }

    @Override
    public String toString() {
        return nombreCategoria;
    }
}
