package modelo.entidades;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "Bolsa")
public class Bolsa implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idBolsa;

    @Column(name = "precioTotal")
    private double precioTotal;

    @OneToMany(mappedBy = "bolsa", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ItemBolsa> items = new ArrayList<>();
	
	
	public Bolsa() {
		
	}

	
	
	public Bolsa(int idBolsa, double precioTotal, List<ItemBolsa> items) {
		super();
		this.idBolsa = idBolsa;
		this.precioTotal = precioTotal;
		this.items = items;
	}



	public int getIdBolsa() {
		return idBolsa;
	}
	public void setIdBolsa(int idBolsa) {
		this.idBolsa = idBolsa;
	}
	public double getPrecioTotal() {
		return precioTotal;
	}
	public void setPrecioTotal(double precioTotal) {
		this.precioTotal = precioTotal;
	}
	public List<ItemBolsa> getItems() {
		return items;
	}
	public void setItems(List<ItemBolsa> items) {
		this.items = items;
	}

	@Override
	public String toString() {
		return "Bolsa [idBolsa=" + idBolsa + ", precioTotal=" + precioTotal + ", items=" + items + "]";
	}
	
	
	
}
