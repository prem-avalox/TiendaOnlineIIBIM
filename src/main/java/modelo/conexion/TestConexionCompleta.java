package modelo.conexion;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Clase para hacer un diagnóstico completo de la conexión y los datos
 * Adaptada al Singleton ConexionBD (getInstancia().getConexion(), cerrarConexion()).
 */
public class TestConexionCompleta {

    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║  DIAGNÓSTICO COMPLETO DE BASE DE DATOS                     ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝\n");

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        // Importante: la instancia Singleton
        ConexionBD conexionBD = ConexionBD.getInstancia();

        try {
            // ============================================
            // PASO 1: Probar la conexión
            // ============================================
            System.out.println("📋 PASO 1: Probando conexión...");
            conn = conexionBD.getConexion();

            if (conn == null) {
                System.err.println("❌ La conexión es NULL");
                return;
            }

            if (conn.isClosed()) {
                System.err.println("❌ La conexión está cerrada");
                return;
            }

            System.out.println("✅ Conexión exitosa y activa");
            System.out.println("   Catálogo: " + conn.getCatalog());
            System.out.println("   URL: " + conn.getMetaData().getURL());
            System.out.println("   Usuario: " + conn.getMetaData().getUserName());
            System.out.println();

            // ============================================
            // PASO 2: Verificar que la tabla existe
            // ============================================
            System.out.println("📋 PASO 2: Verificando tabla 'prendas'...");
            stmt = conn.createStatement();

            try {
                rs = stmt.executeQuery("SHOW TABLES LIKE 'prendas'");
                if (rs.next()) {
                    System.out.println("✅ La tabla 'prendas' existe");
                } else {
                    System.err.println("❌ La tabla 'prendas' NO existe");
                    System.err.println("   Verifica el nombre de la tabla en la BD");
                    return;
                }
            } catch (SQLException e) {
                System.err.println("❌ Error al verificar tabla: " + e.getMessage());
                return;
            } finally {
                if (rs != null) {
                    try { rs.close(); } catch (SQLException ignored) {}
                    rs = null;
                }
            }
            System.out.println();

            // ============================================
            // PASO 3: Contar registros
            // ============================================
            System.out.println("📋 PASO 3: Contando registros...");
            rs = stmt.executeQuery("SELECT COUNT(*) as total FROM prendas");
            if (rs.next()) {
                int total = rs.getInt("total");
                System.out.println("📊 Total de registros en 'prendas': " + total);

                if (total == 0) {
                    System.err.println("⚠️  LA TABLA ESTÁ VACÍA!");
                    System.err.println("   Necesitas insertar datos en la tabla 'prendas'");
                    System.err.println("   Ejecuta el script SQL de inserción de datos");
                    return;
                }
            }
            rs.close();
            rs = null;
            System.out.println();

            // ============================================
            // PASO 4: Verificar estructura de columnas
            // ============================================
            System.out.println("📋 PASO 4: Verificando estructura de columnas...");
            rs = stmt.executeQuery("SELECT * FROM prendas LIMIT 1");
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            System.out.println("📊 Columnas encontradas: " + columnCount);
            for (int i = 1; i <= columnCount; i++) {
                System.out.println("   " + i + ". " + metaData.getColumnName(i) +
                        " (" + metaData.getColumnTypeName(i) + ")");
            }
            rs.close();
            rs = null;
            System.out.println();

            // ============================================
            // PASO 5: Consultar los primeros registros
            // ============================================
            System.out.println("📋 PASO 5: Consultando primeros 3 registros...");
            rs = stmt.executeQuery("SELECT * FROM prendas ORDER BY id_prenda DESC LIMIT 3");

            int count = 0;
            while (rs.next()) {
                count++;
                System.out.println("\n   ─────────────────────────────────────────");
                System.out.println("   Registro #" + count);
                System.out.println("   ─────────────────────────────────────────");

                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    Object value = rs.getObject(i);
                    System.out.println("   " + columnName + ": " + value);
                }
            }

            if (count == 0) {
                System.err.println("⚠️  No se pudieron recuperar registros con SELECT");
            } else {
                System.out.println("\n✅ Se recuperaron " + count + " registros exitosamente");
            }
            System.out.println();

            // ============================================
            // RESUMEN FINAL
            // ============================================
            System.out.println("\n╔════════════════════════════════════════════════════════════╗");
            System.out.println("║  DIAGNÓSTICO COMPLETADO                                    ║");
            System.out.println("╚════════════════════════════════════════════════════════════╝");
            System.out.println("\n✅ Si llegaste hasta aquí, la conexión funciona correctamente");
            System.out.println("   y hay datos en la tabla.");
            System.out.println("\n📝 Si el Servlet sigue sin mostrar datos, el problema está en:");
            System.out.println("   1. El mapeo de PrendaDAO.mapearPrenda()");
            System.out.println("   2. El flujo del Servlet");
            System.out.println("   3. La vista JSP");

        } catch (SQLException e) {
            System.err.println("\n❌ ERROR SQL:");
            System.err.println("   Mensaje: " + e.getMessage());
            System.err.println("   Código: " + e.getErrorCode());
            System.err.println("   Estado: " + e.getSQLState());
            e.printStackTrace();

        } finally {
            // Cerrar ResultSet y Statement (la Connection la maneja ConexionBD)
            try {
                if (rs != null) rs.close();
            } catch (SQLException ignored) {}

            try {
                if (stmt != null) stmt.close();
            } catch (SQLException ignored) {}

            // Cerrar usando el método del Singleton (según tu UML)
            conexionBD.cerrarConexion();
            System.out.println("\n🔒 Recursos cerrados");
        }
    }
}
