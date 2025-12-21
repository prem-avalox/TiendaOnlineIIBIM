package modelo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Clase que representa un item en la bolsa de compras
 */
public class ItemBolsa implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int idPrenda;
	private String nombrePrenda;
	private String imagenUrl;
	private String talla;
	private BigDecimal precioUnitario;
	private int cantidad;
	
	// Constructores
	public ItemBolsa() {
	}
	
	public ItemBolsa(int idPrenda, String nombrePrenda, String imagenUrl, 
			String talla, BigDecimal precioUnitario, int cantidad) {
		this.idPrenda = idPrenda;
		this.nombrePrenda = nombrePrenda;
		this.imagenUrl = imagenUrl;
		this.talla = talla;
		this.precioUnitario = precioUnitario;
		this.cantidad = cantidad;
	}
	
	// Getters y Setters
	public int getIdPrenda() {
		return idPrenda;
	}
	
	public void setIdPrenda(int idPrenda) {
		this.idPrenda = idPrenda;
	}
	
	public String getNombrePrenda() {
		return nombrePrenda;
	}
	
	public void setNombrePrenda(String nombrePrenda) {
		this.nombrePrenda = nombrePrenda;
	}
	
	public String getImagenUrl() {
		return imagenUrl;
	}
	
	public void setImagenUrl(String imagenUrl) {
		this.imagenUrl = imagenUrl;
	}
	
	public String getTalla() {
		return talla;
	}
	
	public void setTalla(String talla) {
		this.talla = talla;
	}
	
	public BigDecimal getPrecioUnitario() {
		return precioUnitario;
	}
	
	public void setPrecioUnitario(BigDecimal precioUnitario) {
		this.precioUnitario = precioUnitario;
	}
	
	public int getCantidad() {
		return cantidad;
	}
	
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}
	
	/**
	 * Calcula el subtotal del item (precio * cantidad)
	 */
	public BigDecimal getSubtotal() {
		return precioUnitario.multiply(new BigDecimal(cantidad));
	}
	
	/**
	 * Genera una clave única para identificar el item en la bolsa
	 */
	public String getKey() {
		return idPrenda + "-" + talla;
	}
	
	@Override
	public String toString() {
		return "ItemBolsa [idPrenda=" + idPrenda + ", nombrePrenda=" + nombrePrenda + 
				", talla=" + talla + ", cantidad=" + cantidad + "]";
	}
}
