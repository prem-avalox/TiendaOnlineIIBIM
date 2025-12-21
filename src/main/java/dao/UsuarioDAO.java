package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import modelo.Usuario;
import util.ConexionBD;

/**
 * Clase DAO para gestionar operaciones de Usuario en la base de datos
 */
public class UsuarioDAO {
	
	/**
	 * Valida las credenciales de un usuario
	 * @param correo correo del usuario
	 * @param contrasena contraseña del usuario
	 * @return Usuario si las credenciales son válidas, null en caso contrario
	 */
	public Usuario validarUsuario(String correo, String contrasena) {
		Usuario usuario = null;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			conn = ConexionBD.getConexion();
			String sql = "SELECT * FROM usuarios WHERE correo = ? AND contrasena = ?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, correo);
			ps.setString(2, contrasena); // En producción, usar hash (bcrypt)
			
			rs = ps.executeQuery();
			
			if (rs.next()) {
				usuario = new Usuario();
				usuario.setIdUsuario(rs.getInt("id_usuario"));
				usuario.setNombreCompleto(rs.getString("nombre_completo"));
				usuario.setCorreo(rs.getString("correo"));
				usuario.setContrasena(rs.getString("contrasena"));
				usuario.setRol(rs.getString("rol"));
			}
			
		} catch (SQLException e) {
			System.err.println("Error al validar usuario: " + e.getMessage());
			e.printStackTrace();
		} finally {
			cerrarRecursos(conn, ps, rs);
		}
		
		return usuario;
	}
	
	/**
	 * Registra un nuevo usuario en la base de datos
	 * @param usuario objeto Usuario con los datos a registrar
	 * @return true si se registró correctamente, false en caso contrario
	 */
	public boolean registrarUsuario(Usuario usuario) {
		Connection conn = null;
		PreparedStatement ps = null;
		boolean registrado = false;
		
		try {
			conn = ConexionBD.getConexion();
			String sql = "INSERT INTO usuarios (nombre_completo, correo, contrasena, rol) VALUES (?, ?, ?, ?)";
			ps = conn.prepareStatement(sql);
			ps.setString(1, usuario.getNombreCompleto());
			ps.setString(2, usuario.getCorreo());
			ps.setString(3, usuario.getContrasena()); // En producción, usar hash
			ps.setString(4, usuario.getRol());
			
			int filasAfectadas = ps.executeUpdate();
			registrado = (filasAfectadas > 0);
			
		} catch (SQLException e) {
			System.err.println("Error al registrar usuario: " + e.getMessage());
			e.printStackTrace();
		} finally {
			cerrarRecursos(conn, ps, null);
		}
		
		return registrado;
	}
	
	/**
	 * Verifica si un correo ya está registrado
	 * @param correo correo a verificar
	 * @return true si el correo existe, false en caso contrario
	 */
	public boolean existeCorreo(String correo) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean existe = false;
		
		try {
			conn = ConexionBD.getConexion();
			String sql = "SELECT COUNT(*) FROM usuarios WHERE correo = ?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, correo);
			
			rs = ps.executeQuery();
			
			if (rs.next()) {
				existe = (rs.getInt(1) > 0);
			}
			
		} catch (SQLException e) {
			System.err.println("Error al verificar correo: " + e.getMessage());
			e.printStackTrace();
		} finally {
			cerrarRecursos(conn, ps, rs);
		}
		
		return existe;
	}
	
	/**
	 * Cierra los recursos de base de datos
	 */
	private void cerrarRecursos(Connection conn, PreparedStatement ps, ResultSet rs) {
		try {
			if (rs != null) rs.close();
			if (ps != null) ps.close();
			if (conn != null) conn.close();
		} catch (SQLException e) {
			System.err.println("Error al cerrar recursos: " + e.getMessage());
		}
	}
}
