package modelo.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    // =========================
    // ATRIBUTOS (según UML)
    // =========================
    private String url;
    private String usuario;
    private String password;

    private static ConexionBD instancia;
    private Connection conexion;

    // Driver (puede quedarse como constante interna)
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";

    // =========================
    // CONSTRUCTOR (Singleton)
    // =========================
    private ConexionBD() {
        // Puedes dejar estos valores aquí o moverlos a un método configurar(...)
        String host = "localhost";
        String port = "3306";
        String database = "tienda_online";

        this.usuario = "root";
        this.password = "Lobster1998*";

        this.url = "jdbc:mysql://" + host + ":" + port + "/" + database
                + "?useSSL=false"
                + "&serverTimezone=UTC"
                + "&allowPublicKeyRetrieval=true"
                + "&useUnicode=true"
                + "&characterEncoding=UTF-8";
    }

    // =========================
    // MÉTODOS (según UML)
    // =========================

    public static ConexionBD getInstancia() {
        if (instancia == null) {
            instancia = new ConexionBD();
        }
        return instancia;
    }

    public Connection getConexion() throws SQLException {
        try {
            if (conexion == null || conexion.isClosed()) {
                Class.forName(DRIVER);
                conexion = DriverManager.getConnection(url, usuario, password);
            }
            return conexion;
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver MySQL no encontrado: " + DRIVER, e);
        }
    }

    public void cerrarConexion() {
        if (conexion != null) {
            try {
                if (!conexion.isClosed()) {
                    conexion.close();
                }
            } catch (SQLException e) {
                // En producción podrías loggear esto
            } finally {
                conexion = null;
            }
        }
    }

    // =========================
    // MÉTODOS ADICIONALES ÚTILES
    // (opcional, recomendados)
    // =========================

    /** Permite configurar credenciales/URL sin editar el constructor. */
    public void configurar(String url, String usuario, String password) {
        this.url = url;
        this.usuario = usuario;
        this.password = password;
    }

    /** Verifica si hay conexión activa (sin abrir una nueva). */
    public boolean estaConectado() {
        try {
            return conexion != null && !conexion.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }

    /** Prueba conexión abriendo/cerrando (sirve para botón “Probar conexión”). */
    public boolean probarConexion() {
        try {
            Connection c = getConexion();
            return c != null && !c.isClosed();
        } catch (SQLException e) {
            return false;
        } finally {
            cerrarConexion();
        }
    }
}
