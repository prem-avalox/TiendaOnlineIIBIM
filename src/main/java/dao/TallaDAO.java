package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import modelo.Talla;
import util.ConexionBD;

/**
 * Clase DAO para gestionar operaciones de Talla en la base de datos
 */
public class TallaDAO {
	
	/**
	 * Obtiene todas las tallas disponibles para una prenda
	 * @param idPrenda ID de la prenda
	 * @return Lista de tallas
	 */
	public List<Talla> listarTallasPorPrenda(int idPrenda) {
		List<Talla> tallas = new ArrayList<>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			conn = ConexionBD.getConexion();
			String sql = "SELECT * FROM tallas WHERE id_prenda = ? ORDER BY " +
					"CASE talla " +
					"WHEN 'XS' THEN 1 " +
					"WHEN 'S' THEN 2 " +
					"WHEN 'M' THEN 3 " +
					"WHEN 'L' THEN 4 " +
					"WHEN 'XL' THEN 5 " +
					"WHEN 'XXL' THEN 6 " +
					"END";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, idPrenda);
			rs = ps.executeQuery();
			
			while (rs.next()) {
				Talla talla = mapearTalla(rs);
				tallas.add(talla);
			}
			
		} catch (SQLException e) {
			System.err.println("Error al listar tallas: " + e.getMessage());
			e.printStackTrace();
		} finally {
			cerrarRecursos(conn, ps, rs);
		}
		
		return tallas;
	}
	
	/**
	 * Obtiene una talla específica
	 * @param idTalla ID de la talla
	 * @return Talla encontrada o null
	 */
	public Talla obtenerTallaPorId(int idTalla) {
		Talla talla = null;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			conn = ConexionBD.getConexion();
			String sql = "SELECT * FROM tallas WHERE id_talla = ?";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, idTalla);
			rs = ps.executeQuery();
			
			if (rs.next()) {
				talla = mapearTalla(rs);
			}
			
		} catch (SQLException e) {
			System.err.println("Error al obtener talla: " + e.getMessage());
			e.printStackTrace();
		} finally {
			cerrarRecursos(conn, ps, rs);
		}
		
		return talla;
	}
	
	/**
	 * Agrega una nueva talla para una prenda
	 * @param talla objeto Talla a agregar
	 * @return true si se agregó correctamente
	 */
	public boolean agregarTalla(Talla talla) {
		Connection conn = null;
		PreparedStatement ps = null;
		boolean agregado = false;
		
		try {
			conn = ConexionBD.getConexion();
			String sql = "INSERT INTO tallas (id_prenda, talla, stock) VALUES (?, ?, ?)";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, talla.getIdPrenda());
			ps.setString(2, talla.getTalla());
			ps.setInt(3, talla.getStock());
			
			int filasAfectadas = ps.executeUpdate();
			agregado = (filasAfectadas > 0);
			
		} catch (SQLException e) {
			System.err.println("Error al agregar talla: " + e.getMessage());
			e.printStackTrace();
		} finally {
			cerrarRecursos(conn, ps, null);
		}
		
		return agregado;
	}
	
	/**
	 * Actualiza el stock de una talla
	 * @param idTalla ID de la talla
	 * @param nuevoStock nuevo valor del stock
	 * @return true si se actualizó correctamente
	 */
	public boolean actualizarStock(int idTalla, int nuevoStock) {
		Connection conn = null;
		PreparedStatement ps = null;
		boolean actualizado = false;
		
		try {
			conn = ConexionBD.getConexion();
			String sql = "UPDATE tallas SET stock = ? WHERE id_talla = ?";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, nuevoStock);
			ps.setInt(2, idTalla);
			
			int filasAfectadas = ps.executeUpdate();
			actualizado = (filasAfectadas > 0);
			
		} catch (SQLException e) {
			System.err.println("Error al actualizar stock: " + e.getMessage());
			e.printStackTrace();
		} finally {
			cerrarRecursos(conn, ps, null);
		}
		
		return actualizado;
	}
	
	/**
	 * Verifica si hay stock disponible para una talla específica
	 * @param idPrenda ID de la prenda
	 * @param talla nombre de la talla
	 * @return cantidad disponible
	 */
	public int verificarStock(int idPrenda, String talla) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int stock = 0;
		
		try {
			conn = ConexionBD.getConexion();
			String sql = "SELECT stock FROM tallas WHERE id_prenda = ? AND talla = ?";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, idPrenda);
			ps.setString(2, talla);
			rs = ps.executeQuery();
			
			if (rs.next()) {
				stock = rs.getInt("stock");
			}
			
		} catch (SQLException e) {
			System.err.println("Error al verificar stock: " + e.getMessage());
			e.printStackTrace();
		} finally {
			cerrarRecursos(conn, ps, rs);
		}
		
		return stock;
	}
	
	/**
	 * Elimina todas las tallas de una prenda
	 * @param idPrenda ID de la prenda
	 * @return true si se eliminaron correctamente
	 */
	public boolean eliminarTallasPorPrenda(int idPrenda) {
		Connection conn = null;
		PreparedStatement ps = null;
		boolean eliminado = false;
		
		try {
			conn = ConexionBD.getConexion();
			String sql = "DELETE FROM tallas WHERE id_prenda = ?";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, idPrenda);
			
			ps.executeUpdate();
			eliminado = true;
			
		} catch (SQLException e) {
			System.err.println("Error al eliminar tallas: " + e.getMessage());
			e.printStackTrace();
		} finally {
			cerrarRecursos(conn, ps, null);
		}
		
		return eliminado;
	}
	
	/**
	 * Mapea un ResultSet a un objeto Talla
	 */
	private Talla mapearTalla(ResultSet rs) throws SQLException {
		Talla talla = new Talla();
		talla.setIdTalla(rs.getInt("id_talla"));
		talla.setIdPrenda(rs.getInt("id_prenda"));
		talla.setTalla(rs.getString("talla"));
		talla.setStock(rs.getInt("stock"));
		return talla;
	}
	
	/**
	 * Cierra los recursos de base de datos
	 */
	private void cerrarRecursos(Connection conn, Statement stmt, ResultSet rs) {
		try {
			if (rs != null) rs.close();
			if (stmt != null) stmt.close();
			if (conn != null) conn.close();
		} catch (SQLException e) {
			System.err.println("Error al cerrar recursos: " + e.getMessage());
		}
	}
}
