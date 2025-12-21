package modelo;

import java.io.Serializable;

/**
 * Clase que representa las tallas disponibles para cada prenda
 */
public class Talla implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int idTalla;
	private int idPrenda;
	private String talla; // Camisas: "XS", "S", "M", "L", "XL", "XXL" | Pantalones: "28/32", "30/32", etc. | Calzado: "38", "40", etc. | Accesorios: "Única"
	private int stock;
	
	// Constructores
	public Talla() {
	}
	
	public Talla(int idTalla, int idPrenda, String talla, int stock) {
		this.idTalla = idTalla;
		this.idPrenda = idPrenda;
		this.talla = talla;
		this.stock = stock;
	}
	
	// Getters y Setters
	public int getIdTalla() {
		return idTalla;
	}
	
	public void setIdTalla(int idTalla) {
		this.idTalla = idTalla;
	}
	
	public int getIdPrenda() {
		return idPrenda;
	}
	
	public void setIdPrenda(int idPrenda) {
		this.idPrenda = idPrenda;
	}
	
	public String getTalla() {
		return talla;
	}
	
	public void setTalla(String talla) {
		this.talla = talla;
	}
	
	public int getStock() {
		return stock;
	}
	
	public void setStock(int stock) {
		this.stock = stock;
	}
	
	public boolean isDisponible() {
		return stock > 0;
	}
	
	@Override
	public String toString() {
		return "Talla [idTalla=" + idTalla + ", talla=" + talla + 
				", stock=" + stock + "]";
	}
}
