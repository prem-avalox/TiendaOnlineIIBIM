package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import util.ConexionBD;

/**
 * Servlet de diagnóstico para verificar la conexión a la base de datos
 */
@WebServlet("/DiagnosticoDB")
public class DiagnosticoDBServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Diagnóstico Base de Datos</title>");
        out.println("<style>");
        out.println("body { font-family: Arial, sans-serif; margin: 40px; }");
        out.println("h1 { color: #333; }");
        out.println(".success { color: green; font-weight: bold; }");
        out.println(".error { color: red; font-weight: bold; }");
        out.println(".info { background: #f0f0f0; padding: 10px; margin: 10px 0; border-left: 4px solid #333; }");
        out.println("table { border-collapse: collapse; width: 100%; margin: 20px 0; }");
        out.println("th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }");
        out.println("th { background-color: #333; color: white; }");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>🔍 Diagnóstico de Base de Datos</h1>");
        
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            // Test 1: Conexión
            out.println("<h2>Test 1: Conexión a MySQL</h2>");
            long startTime = System.currentTimeMillis();
            conn = ConexionBD.getConexion();
            long connectionTime = System.currentTimeMillis() - startTime;
            
            out.println("<p class='success'>✅ Conexión exitosa a la base de datos</p>");
            out.println("<p class='info'>Tiempo de conexión: " + connectionTime + " ms</p>");
            out.println("<p class='info'>Connection object: " + conn.getClass().getName() + "</p>");
            out.println("<p class='info'>Connection closed: " + conn.isClosed() + "</p>");
            
            // Test 2: Verificar tablas
            out.println("<h2>Test 2: Tablas en la Base de Datos</h2>");
            out.println("<p class='info'>Connection antes de crear statement: " + !conn.isClosed() + "</p>");
            stmt = conn.createStatement();
            out.println("<p class='info'>Statement creado exitosamente</p>");
            rs = stmt.executeQuery("SHOW TABLES");
            out.println("<p class='info'>Query ejecutada, procesando resultados...</p>");
            
            out.println("<table>");
            out.println("<tr><th>Tabla</th></tr>");
            int tableCount = 0;
            while (rs.next()) {
                out.println("<tr><td>" + rs.getString(1) + "</td></tr>");
                tableCount++;
            }
            out.println("</table>");
            out.println("<p class='info'>Total de tablas: " + tableCount + "</p>");
            rs.close();
            
            // Test 3: Contar registros
            out.println("<h2>Test 3: Registros en las Tablas</h2>");
            out.println("<p class='info'>Connection antes de contar: " + !conn.isClosed() + "</p>");
            
            // Prendas
            out.println("<p>Ejecutando: SELECT COUNT(*) FROM prendas</p>");
            rs = stmt.executeQuery("SELECT COUNT(*) FROM prendas");
            rs.next();
            int prendasCount = rs.getInt(1);
            out.println("<p>Prendas: <strong>" + prendasCount + "</strong></p>");
            
            if (prendasCount == 0) {
                out.println("<p class='error'>⚠️ No hay prendas en la base de datos!</p>");
                out.println("<p class='info'>Ejecuta el script: database/INSTALAR_BD_COMPLETA.sql</p>");
            }
            rs.close();
            
            // Tallas
            rs = stmt.executeQuery("SELECT COUNT(*) FROM tallas");
            rs.next();
            int tallasCount = rs.getInt(1);
            out.println("<p>Tallas: <strong>" + tallasCount + "</strong></p>");
            rs.close();
            
            // Usuarios
            rs = stmt.executeQuery("SELECT COUNT(*) FROM usuarios");
            rs.next();
            int usuariosCount = rs.getInt(1);
            out.println("<p>Usuarios: <strong>" + usuariosCount + "</strong></p>");
            rs.close();
            
            // Test 4: Ver prendas
            if (prendasCount > 0) {
                out.println("<h2>Test 4: Prendas Disponibles</h2>");
                rs = stmt.executeQuery("SELECT id_prenda, nombre, categoria, precio FROM prendas LIMIT 10");
                
                out.println("<table>");
                out.println("<tr><th>ID</th><th>Nombre</th><th>Categoría</th><th>Precio</th></tr>");
                while (rs.next()) {
                    out.println("<tr>");
                    out.println("<td>" + rs.getInt("id_prenda") + "</td>");
                    out.println("<td>" + rs.getString("nombre") + "</td>");
                    out.println("<td>" + rs.getString("categoria") + "</td>");
                    out.println("<td>$" + rs.getBigDecimal("precio") + "</td>");
                    out.println("</tr>");
                }
                out.println("</table>");
                rs.close();
            } else {
                out.println("<h2>Test 4: No hay prendas</h2>");
                out.println("<p class='error'>❌ No hay prendas en la base de datos</p>");
                out.println("<div class='info'>");
                out.println("<p><strong>Solución:</strong> Ejecuta el script SQL para insertar datos:</p>");
                out.println("<pre>mysql -u root -p tienda_online < database/INSERTAR_DATOS_RAPIDO.sql</pre>");
                out.println("<p>O ejecuta el script en DBeaver/MySQL Workbench</p>");
                out.println("</div>");
            }
            
            // Resumen
            out.println("<h2>📊 Resumen</h2>");
            out.println("<div class='info'>");
            if (prendasCount > 0 && tallasCount > 0) {
                out.println("<p class='success'>✅ La base de datos está correctamente configurada y tiene datos</p>");
                out.println("<p>Puedes acceder al catálogo: <a href='" + request.getContextPath() + "/Catalogo'>Ver Catálogo</a></p>");
            } else {
                out.println("<p class='error'>❌ La base de datos está vacía. Necesitas insertar datos.</p>");
                out.println("<p>Ve a: /Users/martin/eclipse-workspace/tiendaOnline/database/INSERTAR_DATOS_RAPIDO.sql</p>");
            }
            out.println("</div>");
            
        } catch (Exception e) {
            out.println("<h2>❌ Error</h2>");
            out.println("<p class='error'>Error: " + e.getMessage() + "</p>");
            out.println("<pre>");
            e.printStackTrace(out);
            out.println("</pre>");
            
            out.println("<div class='info'>");
            out.println("<h3>Posibles soluciones:</h3>");
            out.println("<ol>");
            out.println("<li>Verifica que MySQL esté corriendo</li>");
            out.println("<li>Verifica que la base de datos 'tienda_online' exista</li>");
            out.println("<li>Verifica las credenciales en ConexionBD.java</li>");
            out.println("<li>Ejecuta el script de instalación: database/INSTALAR_SIMPLE.sql</li>");
            out.println("</ol>");
            out.println("</div>");
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (Exception e) {
                e.printStackTrace(out);
            }
        }
        
        out.println("<hr>");
        out.println("<p><a href='" + request.getContextPath() + "/Catalogo'>← Volver al Catálogo</a></p>");
        out.println("</body>");
        out.println("</html>");
    }
}
