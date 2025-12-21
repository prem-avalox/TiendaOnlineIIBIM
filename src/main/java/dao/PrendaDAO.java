package dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import modelo.Prenda;
import util.ConexionBD;

/**
 * Clase DAO para gestionar operaciones de Prenda en la base de datos
 */
public class PrendaDAO {
	
	/**
	 * Obtiene todas las prendas del catálogo
	 * @return Lista de prendas
	 */
	public List<Prenda> listarPrendas() {
		List<Prenda> prendas = new ArrayList<>();
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			System.out.println("🔍 PrendaDAO.listarPrendas() - Iniciando...");
			conn = ConexionBD.getConexion();
			System.out.println("🔍 Conexión obtenida: " + (conn != null ? "OK" : "NULL"));
			System.out.println("🔍 Conexión cerrada: " + (conn != null ? conn.isClosed() : "N/A"));
			
			String sql = "SELECT * FROM prendas ORDER BY id_prenda DESC";
			System.out.println("🔍 Ejecutando SQL: " + sql);
			
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			
			System.out.println("🔍 ResultSet obtenido, procesando filas...");
			int count = 0;
			while (rs.next()) {
				Prenda prenda = mapearPrenda(rs);
				prendas.add(prenda);
				count++;
			}
			
			System.out.println("🔍 Total de prendas encontradas: " + count);
			
		} catch (SQLException e) {
			System.err.println("❌ Error al listar prendas: " + e.getMessage());
			e.printStackTrace();
		} finally {
			System.out.println("🔍 Cerrando recursos...");
			cerrarRecursos(conn, stmt, rs);
			System.out.println("🔍 Recursos cerrados");
		}
		
		System.out.println("🔍 Retornando lista con " + prendas.size() + " prendas");
		return prendas;
	}
	
	/**
	 * Obtiene una prenda por su ID
	 * @param idPrenda ID de la prenda
	 * @return Prenda encontrada o null
	 */
	public Prenda obtenerPrendaPorId(int idPrenda) {
		Prenda prenda = null;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			conn = ConexionBD.getConexion();
			String sql = "SELECT * FROM prendas WHERE id_prenda = ?";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, idPrenda);
			rs = ps.executeQuery();
			
			if (rs.next()) {
				prenda = mapearPrenda(rs);
			}
			
		} catch (SQLException e) {
			System.err.println("Error al obtener prenda: " + e.getMessage());
			e.printStackTrace();
		} finally {
			cerrarRecursos(conn, ps, rs);
		}
		
		return prenda;
	}
	
	/**
	 * Busca prendas por nombre
	 * @param termino término de búsqueda
	 * @return Lista de prendas que coinciden
	 */
	public List<Prenda> buscarPrendas(String termino) {
		List<Prenda> prendas = new ArrayList<>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			conn = ConexionBD.getConexion();
			String sql = "SELECT * FROM prendas WHERE nombre LIKE ? OR descripcion LIKE ?";
			ps = conn.prepareStatement(sql);
			String patron = "%" + termino + "%";
			ps.setString(1, patron);
			ps.setString(2, patron);
			rs = ps.executeQuery();
			
			while (rs.next()) {
				Prenda prenda = mapearPrenda(rs);
				prendas.add(prenda);
			}
			
		} catch (SQLException e) {
			System.err.println("Error al buscar prendas: " + e.getMessage());
			e.printStackTrace();
		} finally {
			cerrarRecursos(conn, ps, rs);
		}
		
		return prendas;
	}
	
	/**
	 * Filtra prendas por categoría, color y/o tipo de ajuste
	 * @param categoria categoría de la prenda (puede ser null)
	 * @param color color de la prenda (puede ser null)
	 * @param tipoAjuste tipo de ajuste (puede ser null)
	 * @return Lista de prendas filtradas
	 */
	public List<Prenda> filtrarPrendas(String categoria, String color, String tipoAjuste) {
		List<Prenda> prendas = new ArrayList<>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			conn = ConexionBD.getConexion();
			StringBuilder sql = new StringBuilder("SELECT * FROM prendas WHERE 1=1");
			
			if (categoria != null && !categoria.isEmpty()) {
				sql.append(" AND categoria = ?");
			}
			if (color != null && !color.isEmpty()) {
				sql.append(" AND color = ?");
			}
			if (tipoAjuste != null && !tipoAjuste.isEmpty()) {
				sql.append(" AND tipo_ajuste = ?");
			}
			
			ps = conn.prepareStatement(sql.toString());
			
			int index = 1;
			if (categoria != null && !categoria.isEmpty()) {
				ps.setString(index++, categoria);
			}
			if (color != null && !color.isEmpty()) {
				ps.setString(index++, color);
			}
			if (tipoAjuste != null && !tipoAjuste.isEmpty()) {
				ps.setString(index++, tipoAjuste);
			}
			
			rs = ps.executeQuery();
			
			while (rs.next()) {
				Prenda prenda = mapearPrenda(rs);
				prendas.add(prenda);
			}
			
		} catch (SQLException e) {
			System.err.println("Error al filtrar prendas: " + e.getMessage());
			e.printStackTrace();
		} finally {
			cerrarRecursos(conn, ps, rs);
		}
		
		return prendas;
	}
	
	/**
	 * Agrega una nueva prenda
	 * @param prenda objeto Prenda a agregar
	 * @return true si se agregó correctamente
	 */
	public boolean agregarPrenda(Prenda prenda) {
		Connection conn = null;
		PreparedStatement ps = null;
		boolean agregado = false;
		
		try {
			conn = ConexionBD.getConexion();
			String sql = "INSERT INTO prendas (nombre, descripcion, categoria, precio, imagen_url, color, tipo_ajuste) " +
					"VALUES (?, ?, ?, ?, ?, ?, ?)";
			ps = conn.prepareStatement(sql);
			ps.setString(1, prenda.getNombre());
			ps.setString(2, prenda.getDescripcion());
			ps.setString(3, prenda.getCategoria());
			ps.setBigDecimal(4, prenda.getPrecio());
			ps.setString(5, prenda.getImagenUrl());
			ps.setString(6, prenda.getColor());
			ps.setString(7, prenda.getTipoAjuste());
			
			int filasAfectadas = ps.executeUpdate();
			agregado = (filasAfectadas > 0);
			
		} catch (SQLException e) {
			System.err.println("Error al agregar prenda: " + e.getMessage());
			e.printStackTrace();
		} finally {
			cerrarRecursos(conn, ps, null);
		}
		
		return agregado;
	}
	
	/**
	 * Actualiza una prenda existente
	 * @param prenda objeto Prenda con los datos actualizados
	 * @return true si se actualizó correctamente
	 */
	public boolean actualizarPrenda(Prenda prenda) {
		Connection conn = null;
		PreparedStatement ps = null;
		boolean actualizado = false;
		
		try {
			conn = ConexionBD.getConexion();
			String sql = "UPDATE prendas SET nombre=?, descripcion=?, categoria=?, precio=?, " +
					"imagen_url=?, color=?, tipo_ajuste=? WHERE id_prenda=?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, prenda.getNombre());
			ps.setString(2, prenda.getDescripcion());
			ps.setString(3, prenda.getCategoria());
			ps.setBigDecimal(4, prenda.getPrecio());
			ps.setString(5, prenda.getImagenUrl());
			ps.setString(6, prenda.getColor());
			ps.setString(7, prenda.getTipoAjuste());
			ps.setInt(8, prenda.getIdPrenda());
			
			int filasAfectadas = ps.executeUpdate();
			actualizado = (filasAfectadas > 0);
			
		} catch (SQLException e) {
			System.err.println("Error al actualizar prenda: " + e.getMessage());
			e.printStackTrace();
		} finally {
			cerrarRecursos(conn, ps, null);
		}
		
		return actualizado;
	}
	
	/**
	 * Elimina una prenda por su ID
	 * @param idPrenda ID de la prenda a eliminar
	 * @return true si se eliminó correctamente
	 */
	public boolean eliminarPrenda(int idPrenda) {
		Connection conn = null;
		PreparedStatement ps = null;
		boolean eliminado = false;
		
		try {
			conn = ConexionBD.getConexion();
			String sql = "DELETE FROM prendas WHERE id_prenda = ?";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, idPrenda);
			
			int filasAfectadas = ps.executeUpdate();
			eliminado = (filasAfectadas > 0);
			
		} catch (SQLException e) {
			System.err.println("Error al eliminar prenda: " + e.getMessage());
			e.printStackTrace();
		} finally {
			cerrarRecursos(conn, ps, null);
		}
		
		return eliminado;
	}
	
	/**
	 * Mapea un ResultSet a un objeto Prenda
	 */
	private Prenda mapearPrenda(ResultSet rs) throws SQLException {
		Prenda prenda = new Prenda();
		prenda.setIdPrenda(rs.getInt("id_prenda"));
		prenda.setNombre(rs.getString("nombre"));
		prenda.setDescripcion(rs.getString("descripcion"));
		prenda.setCategoria(rs.getString("categoria"));
		prenda.setPrecio(rs.getBigDecimal("precio"));
		prenda.setImagenUrl(rs.getString("imagen_url"));
		prenda.setColor(rs.getString("color"));
		prenda.setTipoAjuste(rs.getString("tipo_ajuste"));
		return prenda;
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
