package modelo.entidades;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "Bolsa")
public class Bolsa implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idBolsa;

    @Column(name = "precioTotal")
    private double precioTotal;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idUsuario", nullable = false)

    private Usuario usuario;

    @OneToMany(mappedBy = "bolsa", 
               cascade = CascadeType.ALL, 
               orphanRemoval = true, 
               fetch = FetchType.EAGER)
    @JsonManagedReference  // Maneja la serialización JSON: serializa esta relación (Bolsa → Items)
    private List<ItemBolsa> items = new ArrayList<>();
	
	
	public Bolsa() {
		
	}
	
	public Bolsa(double precioTotal, Usuario usuario, List<ItemBolsa> items) {
		this.precioTotal = precioTotal;
		this.usuario = usuario;
		this.items = items;
	}



	public void setIdBolsa(int idBolsa) {
		this.idBolsa = idBolsa;
	}

	public int getIdBolsa() {
		return idBolsa;
	}
	
	public double getPrecioTotal() {
        return precioTotal;
    }
    public void setPrecioTotal(double precioTotal) {
        this.precioTotal = precioTotal;
    }
    public List<ItemBolsa> getItemsBolsa() {
        return items;
    }
    
    public List<ItemBolsa> getItems() {
        return items;
    }
    
    public void setItems(List<ItemBolsa> items) {
        this.items = items;
    }

    // Necesario para pruebas y flujo: suma subtotales de items
    public double calcularMontoTotal() {
        double total = 0.0;
        if (items != null) {
            for (ItemBolsa item : items) {
                total += item.calcularSubtotal();
            }
        }
        return total;
    }
    
    @Override
	public String toString() {
		return "Bolsa [idBolsa=" + idBolsa + ", precioTotal=" + precioTotal + ", items=" + items + "]";
	}



	public Usuario getUsuario() {
		return usuario;
	}



	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	
	
}