package modelo.entidades;

import java.io.Serializable;

public enum Corte implements Serializable {

    SLIM("Slim Fit"),
    REGULAR("Regular Fit"),
    OVERSIZE("Oversize"),
    SKINNY("Skinny Fit"),
    RELAJADO("Relaxed Fit");

    private static final long serialVersionUID = 1L;

    private final String nombreCorte;

    private Corte(String nombreCorte) {
        this.nombreCorte = nombreCorte;
    }

    public String getNombreCorte() {
        return nombreCorte;
    }

    @Override
    public String toString() {
        return nombreCorte;
    }
}
