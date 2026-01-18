package modelo.entidades;

import java.io.Serializable;

import jakarta.persistence.*;

/**
 * Clase que representa un item en la bolsa de compras
 */
@Entity
@Table(name = "ItemBolsa")
public class ItemBolsa implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idItem;
	
	@Column (name="cantidad")
	private int cantidad;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "talla")
	private Talla tallaSeleccionada;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idPrenda", nullable = false)
    private Prenda prenda;

	@ManyToOne
	@JoinColumn(name = "idBolsa", nullable = false) 
	private Bolsa bolsa; 
	
	// Constructores
	public ItemBolsa() {
	}
	
	
	
	public ItemBolsa(int idItem, int cantidad, Talla tallaSeleccionada, Prenda prenda) {
		this.cantidad = cantidad;
		this.tallaSeleccionada = tallaSeleccionada;
		this.prenda = prenda;
	}
	
	public int getIdItem() {
		return idItem;
	}


	public void setIdItem(int idItem) {
		this.idItem = idItem;
	}


	public int getCantidad() {
		return cantidad;
	}


	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}


	public Talla getTallaSeleccionada() {
		return tallaSeleccionada;
	}


	public void setTallaSeleccionada(Talla tallaSeleccionada) {
		this.tallaSeleccionada = tallaSeleccionada;
	}


	public Prenda getPrenda() {
		return prenda;
	}


	public void setPrenda(Prenda prenda) {
		this.prenda = prenda;
	}
	
	public Bolsa getBolsa() {
	    return bolsa;
	}

	public void setBolsa(Bolsa bolsa) {
	    this.bolsa = bolsa;
	}
	
	/**
	 * Calcula el subtotal del item (precio * cantidad)
	 * Según diagrama de secuencia CU11 - Ver Bolsa
	 * @return subtotal calculado
	 */
	public double calcularSubtotal() {
		if (prenda != null) {
			return prenda.getPrecio() * cantidad;
		}
		return 0.0;
	}
	
	/**
	 * Obtiene el tipo de talla seleccionada como String
	 * Según diagrama de secuencia: talla.getTipoTalla()
	 * @return nombre de la talla (S, M, L, XL, etc.)
	 */
	public String getTipoTalla() {
		return tallaSeleccionada != null ? tallaSeleccionada.name() : null;
	}


	@Override
	public String toString() {
		return this.idItem + " " + this.cantidad + " " + this.tallaSeleccionada + " " + this.prenda ;
	}


}
