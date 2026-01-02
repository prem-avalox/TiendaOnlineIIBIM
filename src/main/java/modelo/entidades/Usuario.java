package modelo.entidades;

import java.io.Serializable;

/**
 * Clase que representa un Usuario del sistema
 */
public class Usuario implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int idUsuario;
	private String nombreUsuario;
	private String email;
	private String contrasena;
	private Bolsa bolsa;
	
	// Constructores
	public Usuario() {
	}

	public Usuario(int idUsuario, String nombreUsuario, String email, String contrasena, Bolsa bolsa) {
		super();
		this.idUsuario = idUsuario;
		this.nombreUsuario = nombreUsuario;
		this.email = email;
		this.contrasena = contrasena;
		this.bolsa = bolsa;
	}

	public int getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(int idUsuario) {
		this.idUsuario = idUsuario;
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

	
	public Bolsa getBolsa() {
		return bolsa;
	}

	public void setBolsa(Bolsa bolsa) {
		this.bolsa = bolsa;
	}

	@Override
	public String toString() {
		return "Usuario [idUsuario=" + idUsuario + ", nombreUsuario=" + nombreUsuario + ", email=" + email
				+ ", contrasena=" + contrasena + "]";
	}
	
	
}
