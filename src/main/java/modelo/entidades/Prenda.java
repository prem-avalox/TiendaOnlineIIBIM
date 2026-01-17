package modelo.entidades;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "Prenda")
public class Prenda implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idPrenda;

    @Column(name = "imagen", length = 255)
    private String imagen;

    @Column(name = "nombrePrenda", nullable = false, length = 100)
    private String nombrePrenda;

    @Column(name = "precio", nullable = false)
    private double precio;

    @Column(name = "descripcion", length = 500)
    private String descripcion;

    
    @Enumerated(EnumType.STRING)
    @Column(name = "color", length = 50)
    private Color color;

    @Enumerated(EnumType.STRING)
    @Column(name = "corte", length = 30)
    private Corte corte;

    @Enumerated(EnumType.STRING)
    @Column(name = "categoria", nullable = false, length = 30)
    private Categoria categoria;

    @OneToMany(mappedBy = "prenda",
               cascade = CascadeType.ALL,
               orphanRemoval = true,
               fetch = FetchType.LAZY)
    private List<StockTalla> stockTallas = new ArrayList<>();

    public Prenda() {}

    public Prenda(int idPrenda, String imagen, String nombrePrenda, double precio, String descripcion,
                  Color color, Corte corte, Categoria categoria, List<StockTalla> stockTallas) {
        this.idPrenda = idPrenda;
        this.imagen = imagen;
        this.nombrePrenda = nombrePrenda;
        this.precio = precio;
        this.descripcion = descripcion;
        this.color = color;
        this.corte = corte;
        this.categoria = categoria;
        this.stockTallas = (stockTallas != null) ? stockTallas : new ArrayList<>();
    }


    public int getIdPrenda() { return idPrenda; }
    public void setIdPrenda(int idPrenda) { this.idPrenda = idPrenda; }

    public String getImagen() { return imagen; }
    public void setImagen(String imagen) { this.imagen = imagen; }

    public String getNombrePrenda() { return nombrePrenda; }
    public void setNombrePrenda(String nombrePrenda) { this.nombrePrenda = nombrePrenda; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Color getColor() { return color; }
    public void setColor(Color color) { this.color = color; }

    public Corte getCorte() { return corte; }
    public void setCorte(Corte corte) { this.corte = corte; }

    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }

    public List<StockTalla> getStockTallas() { return stockTallas; }
    public void setStockTallas(List<StockTalla> stockTallas) {
        this.stockTallas = (stockTallas != null) ? stockTallas : new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Prenda [idPrenda=" + idPrenda + ", imagen=" + imagen + ", nombrePrenda=" + nombrePrenda +
               ", precio=" + precio + ", descripcion=" + descripcion + ", color=" + color +
               ", corte=" + corte + ", categoria=" + categoria + "]";
    }
}