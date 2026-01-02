package modelo.entidades;

import java.io.Serializable;

/**
 * Enum que representa las tallas disponibles para cada prenda
 */
public enum Talla implements Serializable {

    XS(1, "XS"),
    S(2, "S"),
    M(3, "M"),
    L(4, "L"),
    XL(5, "XL"),
    XXL(6, "XXL"),

    // Calzado
    T38(7, "38"),
    T40(8, "40"),
    T42(9, "42"),

    // Accesorios
    UNICA(10, "Única");

    private static final long serialVersionUID = 1L;

    private final int idTalla;
    private final String talla;

    // Constructor SIEMPRE privado en enum
    private Talla(int idTalla, String talla) {
        this.idTalla = idTalla;
        this.talla = talla;
    }

    public int getIdTalla() {
        return idTalla;
    }

    public String getTalla() {
        return talla;
    }

    @Override
    public String toString() {
        return talla;
    }
}
