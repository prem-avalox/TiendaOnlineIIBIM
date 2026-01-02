package modelo.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BddConnection {

	private static Connection cnn = null;

	public BddConnection() {
		String servidor = "localhost";
		String database = "tienda_online";
		String usuario = "root";
		String password = "root";
		
		String url = "jdbc:mysql://" + servidor + "/" + database;
		
		try {
			DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
			cnn = DriverManager.getConnection(url, usuario, password);
			} catch (SQLException e) {
				e.printStackTrace();
				
		}	
		
	}
	
	public static Connection getConnection() {
		if (cnn == null) {
			new BddConnection();
		} 
		return cnn;
	}
	
	
	public static Connection getConexion() {
		if(cnn ==null) {
			new BddConnection();
		}
		return cnn;
	}
	
	public static void cerrar(ResultSet rs) {
		try {
			rs.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		rs = null;
	}
	
	public static void cerrar(PreparedStatement pstmt) {
		try {
			pstmt.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void cerrar() {
		if (cnn != null) {
			try {
				cnn.close();
				cnn = null;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
