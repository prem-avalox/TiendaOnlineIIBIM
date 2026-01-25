package modelo.entidades;

import java.io.Serializable;

/**
 * Enum que representa las tallas disponibles para cada prenda
 */
public enum Talla implements Serializable {

    XS("XS"),
    S("S"),
    M("M"),
    L("L"),
    XL("XL"),
    XXL("XXL"),

    // Calzado
    T38("38"),
    T40("40"),
    T42("42"),

    // Accesorios
    UNICA("Única");

    private static final long serialVersionUID = 1L;

    private final String talla;

    // Constructor SIEMPRE privado en enum
    private Talla(String talla) {
        this.talla = talla;
    }
    

    public String getTalla() {
        return talla;
    }

    @Override
    public String toString() {
        return talla;
    }
}
