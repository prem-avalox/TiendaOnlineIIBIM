package modelo.entidades;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "StockTalla")
public class StockTalla implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idStockTalla;

    @Column(name = "cantidad", nullable = false)
    private int cantidad;

    // MUCHOS StockTalla pertenecen a UNA Prenda
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idPrenda", nullable = false)
    private Prenda prenda;

    // 0..* Tallas (enum) por StockTalla
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
        name = "StockTalla_Talla",
        joinColumns = @JoinColumn(name = "idStockTalla")
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "talla", nullable = false)
    private List<Talla> tallas;

    public StockTalla() {}



	public StockTalla(int idStockTalla, int cantidad, List<Talla> tallas) {
		super();
		this.idStockTalla = idStockTalla;
		this.cantidad = cantidad;
		this.tallas = tallas;
	}

	public int getIdStockTalla() {
		return idStockTalla;
	}

	public void setIdStockTalla(int idStockTalla) {
		this.idStockTalla = idStockTalla;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public List<Talla> getTallas() {
		return tallas;
	}

	public void setTallas(List<Talla> tallas) {
		this.tallas = tallas;
	}

	@Override
	public String toString() {
		return "StockTalla [idStockTalla=" + idStockTalla + ", cantidad=" + cantidad + ", tallas=" + tallas + "]";
	}
	
	
	
	
}
