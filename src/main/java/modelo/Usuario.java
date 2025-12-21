package modelo;

import java.io.Serializable;

/**
 * Clase que representa un Usuario del sistema
 */
public class Usuario implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int idUsuario;
	private String nombreCompleto;
	private String correo;
	private String contrasena;
	private String rol; // "CLIENTE" o "ADMIN"
	
	// Constructores
	public Usuario() {
	}
	
	public Usuario(int idUsuario, String nombreCompleto, String correo, String contrasena, String rol) {
		this.idUsuario = idUsuario;
		this.nombreCompleto = nombreCompleto;
		this.correo = correo;
		this.contrasena = contrasena;
		this.rol = rol;
	}
	
	// Getters y Setters
	public int getIdUsuario() {
		return idUsuario;
	}
	
	public void setIdUsuario(int idUsuario) {
		this.idUsuario = idUsuario;
	}
	
	public String getNombreCompleto() {
		return nombreCompleto;
	}
	
	public void setNombreCompleto(String nombreCompleto) {
		this.nombreCompleto = nombreCompleto;
	}
	
	public String getCorreo() {
		return correo;
	}
	
	public void setCorreo(String correo) {
		this.correo = correo;
	}
	
	public String getContrasena() {
		return contrasena;
	}
	
	public void setContrasena(String contrasena) {
		this.contrasena = contrasena;
	}
	
	public String getRol() {
		return rol;
	}
	
	public void setRol(String rol) {
		this.rol = rol;
	}
	
	@Override
	public String toString() {
		return "Usuario [idUsuario=" + idUsuario + ", nombreCompleto=" + nombreCompleto + 
				", correo=" + correo + ", rol=" + rol + "]";
	}
}
