package modelo.entidades;

import java.io.Serializable;
import java.util.List;

public class StockTalla implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int idStockTalla;
	private int cantidad;
	private List<Talla> tallas;
	
	public StockTalla() {
		
	}

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
