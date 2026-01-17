package modelo.entidades;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

@Entity
@Table(
	    name = "StockTalla",
	    uniqueConstraints = {
	        @UniqueConstraint(columnNames = {"idPrenda", "talla"})
	    }
	)
public class StockTalla implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idStockTalla;

    @Column(name = "cantidad", nullable = false)
    private int cantidad;

    // MUCHOS StockTalla pertenecen a UNA Prenda
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_prenda", nullable = false)
    @JsonIgnore   
    private Prenda prenda;

    @Enumerated(EnumType.STRING)
    @Column(name = "talla", nullable = false)
    private Talla talla;

    public StockTalla() {}


	public StockTalla(int cantidad, Talla talla) {
		this.cantidad = cantidad;
		this.talla = talla;
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

	public Talla getTalla() {
        return talla;
    }

    public void setTalla(Talla talla) {
        this.talla = talla;
    }
    

	public Prenda getPrenda() {
		return prenda;
	}



	public void setPrenda(Prenda prenda) {
		this.prenda = prenda;
	}



	@Override
	public String toString() {
		return "StockTalla [idStockTalla=" + idStockTalla + ", cantidad=" + cantidad + ", talla=" + talla + "]";
	}
	
	
	
	
}
