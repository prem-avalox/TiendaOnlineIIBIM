package modelo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Clase que representa una Prenda del catálogo
 */
public class Prenda implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int idPrenda;
	private String nombre;
	private String descripcion;
	private String categoria; // "CAMISAS", "PANTALONES", "CALZADO", "ACCESORIOS"
	private BigDecimal precio;
	private String imagenUrl;
	private String color;
	private String tipoAjuste; // "Slim", "Regular", "Oversize", etc.
	
	// Constructores
	public Prenda() {
	}
	
	public Prenda(int idPrenda, String nombre, String descripcion, String categoria, 
			BigDecimal precio, String imagenUrl, String color, String tipoAjuste) {
		this.idPrenda = idPrenda;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.categoria = categoria;
		this.precio = precio;
		this.imagenUrl = imagenUrl;
		this.color = color;
		this.tipoAjuste = tipoAjuste;
	}
	
	// Getters y Setters
	public int getIdPrenda() {
		return idPrenda;
	}
	
	public void setIdPrenda(int idPrenda) {
		this.idPrenda = idPrenda;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getDescripcion() {
		return descripcion;
	}
	
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	public String getCategoria() {
		return categoria;
	}
	
	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}
	
	public BigDecimal getPrecio() {
		return precio;
	}
	
	public void setPrecio(BigDecimal precio) {
		this.precio = precio;
	}
	
	public String getImagenUrl() {
		return imagenUrl;
	}
	
	public void setImagenUrl(String imagenUrl) {
		this.imagenUrl = imagenUrl;
	}
	
	public String getColor() {
		return color;
	}
	
	public void setColor(String color) {
		this.color = color;
	}
	
	public String getTipoAjuste() {
		return tipoAjuste;
	}
	
	public void setTipoAjuste(String tipoAjuste) {
		this.tipoAjuste = tipoAjuste;
	}
	
	@Override
	public String toString() {
		return "Prenda [idPrenda=" + idPrenda + ", nombre=" + nombre + 
				", categoria=" + categoria + ", precio=" + precio + "]";
	}
}
