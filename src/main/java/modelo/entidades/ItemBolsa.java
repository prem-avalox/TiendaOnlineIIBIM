package modelo.entidades;

import java.io.Serializable;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonBackReference;

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
	
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "idPrenda", nullable = false)
    private Prenda prenda;

	@ManyToOne
	@JoinColumn(name = "idBolsa", nullable = false)
	@JsonBackReference  // Evita serialización circular: NO serializa esta referencia (Items ← Bolsa)
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
    
    // Necesario para pruebas y flujo: subtotal = precio * cantidad
    public double calcularSubtotal() {
        return (prenda != null) ? prenda.getPrecio() * cantidad : 0.0;
    }
    

	@Override
	public String toString() {
		return this.idItem + " " + this.cantidad + " " + this.tallaSeleccionada + " " + this.prenda ;
	}


}