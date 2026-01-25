package modelo.entidades;

import java.io.Serializable;

public enum Color implements Serializable {

    NEGRO("Negro"),
    BLANCO("Blanco"),
    CELESTE("Celeste"),
    GRIS("Gris"),
    CRUDO("Crudo");

    private static final long serialVersionUID = 1L;

    private final String nombreColor;

    private Color(String nombreColor) {
        this.nombreColor = nombreColor;
    }

    public String getNombreColor() {
        return nombreColor;
    }

    @Override
    public String toString() {
        return nombreColor;
    }
}
