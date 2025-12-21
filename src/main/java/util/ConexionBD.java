package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase para gestionar la conexión a la base de datos MySQL
 * Configuración para tienda_online
 */
public class ConexionBD {
	
	// ================================================
	// CONFIGURACIÓN DE LA BASE DE DATOS
	// ================================================
	
	// Host y puerto de MySQL
	private static final String HOST = "localhost";
	private static final String PORT = "3306";
	private static final String DATABASE = "tienda_online";
	
	// Credenciales de acceso
	// NOTA: Cambia estos valores según tu configuración de MySQL
	private static final String USUARIO = "root";
	private static final String CONTRASENA = ""; // Vacío para XAMPP, sino pon tu contraseña
	
	// Driver de MySQL
	private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
	
	// URL de conexión completa con parámetros
	private static final String URL = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE
			+ "?useSSL=false"                      // Desactivar SSL para desarrollo local
			+ "&serverTimezone=UTC"                 // Zona horaria
			+ "&allowPublicKeyRetrieval=true"      // Para MySQL 8.0+
			+ "&useUnicode=true"                    // Soporte Unicode
			+ "&characterEncoding=UTF-8";           // Codificación UTF-8
	
	// ================================================
	// MÉTODOS PÚBLICOS
	// ================================================
	
	/**
	 * Obtiene una conexión a la base de datos
	 * @return Connection objeto de conexión activo
	 * @throws SQLException si hay error en la conexión
	 */
	public static Connection getConexion() throws SQLException {
		Connection conexion = null;
		try {
			// Cargar el driver de MySQL
			Class.forName(DRIVER);
			
			// Establecer la conexión
			conexion = DriverManager.getConnection(URL, USUARIO, CONTRASENA);
			
			// Log de éxito (opcional, comentar en producción)
			System.out.println("✅ Conexión exitosa a MySQL: " + DATABASE);
			
		} catch (ClassNotFoundException e) {
			System.err.println("❌ ERROR: No se encontró el driver de MySQL");
			System.err.println("   Solución: Agregar mysql-connector-j-8.x.x.jar al classpath");
			e.printStackTrace();
			throw new SQLException("Driver MySQL no encontrado", e);
			
		} catch (SQLException e) {
			System.err.println("❌ ERROR: No se pudo conectar a la base de datos");
			System.err.println("   Host: " + HOST + ":" + PORT);
			System.err.println("   Base de datos: " + DATABASE);
			System.err.println("   Usuario: " + USUARIO);
			System.err.println("\n   Verifica que:");
			System.err.println("   1. MySQL esté corriendo");
			System.err.println("   2. La base de datos '" + DATABASE + "' exista");
			System.err.println("   3. El usuario y contraseña sean correctos");
			e.printStackTrace();
			throw e;
		}
		
		return conexion;
	}
	
	/**
	 * Cierra la conexión a la base de datos de forma segura
	 * @param conexion la conexión a cerrar
	 */
	public static void cerrarConexion(Connection conexion) {
		if (conexion != null) {
			try {
				if (!conexion.isClosed()) {
					conexion.close();
					System.out.println("🔒 Conexión cerrada correctamente");
				}
			} catch (SQLException e) {
				System.err.println("⚠️  Error al cerrar la conexión");
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Verifica si la conexión está disponible
	 * @return true si puede conectarse, false en caso contrario
	 */
	public static boolean verificarConexion() {
		try {
			Connection conn = getConexion();
			if (conn != null && !conn.isClosed()) {
				cerrarConexion(conn);
				return true;
			}
		} catch (SQLException e) {
			return false;
		}
		return false;
	}
	
	// ================================================
	// MÉTODO DE PRUEBA
	// ================================================
	
	/**
	 * Método main para probar la conexión
	 * Ejecutar: Run As → Java Application
	 */
	public static void main(String[] args) {
		System.out.println("================================================");
		System.out.println("  PRUEBA DE CONEXIÓN A MYSQL");
		System.out.println("================================================");
		System.out.println("Host: " + HOST + ":" + PORT);
		System.out.println("Base de datos: " + DATABASE);
		System.out.println("Usuario: " + USUARIO);
		System.out.println("================================================\n");
		
		try {
			// Intentar conectar
			Connection conn = getConexion();
			
			if (conn != null && !conn.isClosed()) {
				System.out.println("\n✅ ¡CONEXIÓN EXITOSA!");
				System.out.println("   La aplicación puede conectarse a MySQL correctamente");
				
				// Cerrar conexión
				cerrarConexion(conn);
				
				System.out.println("\n================================================");
				System.out.println("  TODO ESTÁ LISTO PARA USAR LA APLICACIÓN");
				System.out.println("================================================");
			}
			
		} catch (SQLException e) {
			System.err.println("\n❌ FALLÓ LA CONEXIÓN");
			System.err.println("\n================================================");
			System.err.println("  PASOS PARA SOLUCIONAR:");
			System.err.println("================================================");
			System.err.println("1. Verifica que MySQL esté corriendo");
			System.err.println("   - XAMPP: Inicia MySQL desde el panel");
			System.err.println("   - Mac: brew services start mysql");
			System.err.println("   - Linux: sudo systemctl start mysql");
			System.err.println("   - Windows: net start MySQL80");
			System.err.println("\n2. Verifica que la BD existe:");
			System.err.println("   mysql -u root -p");
			System.err.println("   SHOW DATABASES;");
			System.err.println("\n3. Si no existe, ejecuta el script:");
			System.err.println("   source /ruta/a/script_tienda_online.sql");
			System.err.println("\n4. Verifica usuario y contraseña en ConexionBD.java");
			System.err.println("================================================");
		}
	}
}
