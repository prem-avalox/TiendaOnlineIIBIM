package modelo.entidades;

import java.io.Serializable;

import jakarta.persistence.*;

@Entity
@Table(name = "Usuario")
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idUsuario;

    @Column(name = "nombreUsuario", nullable = false, length = 50)
    private String nombreUsuario;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "contrasena", nullable = false, length = 255)
    private String contrasena;
    
    @Column(name = "isAdmin", nullable = false)
    private boolean isAdmin = false; 

	
	public Usuario() {
	}


    public Usuario(String nombreUsuario, String email, String contrasena, boolean isAdmin) {
        this.nombreUsuario = nombreUsuario;
        this.email = email;
        this.contrasena = contrasena;
        this.isAdmin = isAdmin;
    }

	public int getIdUsuario() {
		return idUsuario;
	}

	public String getNombreUsuario() {
		return nombreUsuario;
	}

	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getContrasena() {
		return contrasena;
	}

	public void setContrasena(String contrasena) {
		this.contrasena = contrasena;
	}

	public boolean isIsAdmin() {
		return isAdmin; 
	}
    public void setIsAdmin(boolean isAdmin) {
    	this.isAdmin = isAdmin; 
    }
    
	@Override
	public String toString() {
		return "Usuario [idUsuario=" + idUsuario + ", nombreUsuario=" + nombreUsuario + ", email=" + email+  " ]";
	}
	
	
}
