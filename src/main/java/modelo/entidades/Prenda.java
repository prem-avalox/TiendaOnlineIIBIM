package modelo.entidades;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "Prenda")
public class Prenda implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idPrenda;

    @Column(name = "imagenPrenda", length = 255)
    private String imagen;

    @Column(name = "nombrePrenda", nullable = false, length = 100)
    private String nombrePrenda;

    @Column(name = "precio", nullable = false)
    private double precio;

    @Column(name = "descripcion", length = 500)
    private String descripcion;

    
    @Enumerated(EnumType.STRING)
    @Column(name = "color",nullable = false, length = 50)
    private Color color;

    @Enumerated(EnumType.STRING)
    @Column(name = "corte", nullable = false,length = 30)
    private Corte corte;

    @Enumerated(EnumType.STRING)
    @Column(name = "categoria", nullable = false, length = 30)
    private Categoria categoria;

    
 // Relación 1:N con StockTalla (Una prenda tiene múltiples registros de stock)
    // CascadeType.ALL asegura que si borras la prenda, se borra su stock
    @OneToMany(mappedBy = "prenda",
               cascade = CascadeType.ALL,
               orphanRemoval = true,
               fetch = FetchType.LAZY)
    @JsonManagedReference  // Maneja la serialización JSON: serializa esta relación (Prenda → StockTallas)
    private List<StockTalla> stockTallas = new ArrayList<>();

    public Prenda() {}


    // Constructor actualizado con los nuevos tipos de Enum
    public Prenda(String imagen, String nombrePrenda, double precio, String descripcion, Color color, Corte corte, Categoria categoria, List<StockTalla> stockTallas) {
        this.imagen = imagen;
        this.nombrePrenda = nombrePrenda;
        this.precio = precio;
        this.descripcion = descripcion;
        this.color = color;
        this.corte = corte;
        this.categoria = categoria;
        if (stockTallas != null) {
            for (StockTalla stock : stockTallas) {
                stock.setPrenda(this);   // 🔑 CLAVE
                this.stockTallas.add(stock);
            }
        }
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
    
    /**
     * Retorna un objeto con los datos básicos de la prenda
     * Según diagrama de secuencia CU11: prenda.getDatosPrenda()
     * Nota: Este método puede retornar la prenda misma, ya que todos los datos son accesibles vía getters
     * @return esta instancia de Prenda con todos sus datos
     */
    public Prenda getDatosPrenda() {
        return this;
    }

    @Override
    public String toString() {
        return "Prenda [idPrenda=" + idPrenda + ", imagen=" + imagen + ", nombrePrenda=" + nombrePrenda +
               ", precio=" + precio + ", descripcion=" + descripcion + ", color=" + color +
               ", corte=" + corte + ", categoria=" + categoria + "]";
    }
}