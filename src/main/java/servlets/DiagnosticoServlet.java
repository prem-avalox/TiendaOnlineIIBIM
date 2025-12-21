package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import dao.PrendaDAO;
import modelo.Prenda;
import util.ConexionBD;

/**
 * Servlet de diagnóstico para verificar la conexión y datos desde Tomcat
 */
@WebServlet("/DiagnosticoServlet")
public class DiagnosticoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		
		out.println("<!DOCTYPE html>");
		out.println("<html><head><meta charset='UTF-8'><title>Diagnóstico</title>");
		out.println("<style>");
		out.println("body { font-family: monospace; padding: 20px; background: #f5f5f5; }");
		out.println(".ok { color: green; } .error { color: red; } .info { color: blue; }");
		out.println("pre { background: white; padding: 10px; border-left: 3px solid #333; }");
		out.println("</style></head><body>");
		
		out.println("<h1>🔍 DIAGNÓSTICO DE CONEXIÓN Y DATOS</h1>");
		out.println("<p>Ejecutado desde Tomcat en: " + new java.util.Date() + "</p>");
		out.println("<hr>");
		
		// ==========================================
		// PRUEBA 1: Conexión directa
		// ==========================================
		out.println("<h2>📋 PRUEBA 1: Conexión Directa a MySQL</h2>");
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			out.println("<pre>");
			out.println("Intentando conectar...");
			conn = ConexionBD.getConexion();
			
			if (conn != null && !conn.isClosed()) {
				out.println("<span class='ok'>✅ Conexión exitosa</span>");
				out.println("   Catálogo: " + conn.getCatalog());
				out.println("   URL: " + conn.getMetaData().getURL());
				out.println("   Usuario: " + conn.getMetaData().getUserName());
			} else {
				out.println("<span class='error'>❌ Conexión es NULL o está cerrada</span>");
			}
			out.println("</pre>");
			
		} catch (Exception e) {
			out.println("<pre class='error'>❌ Error de conexión: " + e.getMessage());
			e.printStackTrace(out);
			out.println("</pre>");
		}
		
		// ==========================================
		// PRUEBA 2: Contar registros directamente
		// ==========================================
		out.println("<h2>📋 PRUEBA 2: Contar Registros (SQL Directo)</h2>");
		
		try {
			out.println("<pre>");
			if (conn != null) {
				stmt = conn.createStatement();
				rs = stmt.executeQuery("SELECT COUNT(*) as total FROM prendas");
				
				if (rs.next()) {
					int total = rs.getInt("total");
					out.println("<span class='ok'>✅ Total de registros en BD: " + total + "</span>");
				}
				rs.close();
			}
			out.println("</pre>");
			
		} catch (Exception e) {
			out.println("<pre class='error'>❌ Error al contar: " + e.getMessage());
			e.printStackTrace(out);
			out.println("</pre>");
		}
		
		// ==========================================
		// PRUEBA 3: SELECT directo
		// ==========================================
		out.println("<h2>📋 PRUEBA 3: SELECT Directo (Primeros 3)</h2>");
		
		try {
			out.println("<pre>");
			if (conn != null) {
				rs = stmt.executeQuery("SELECT * FROM prendas ORDER BY id_prenda DESC LIMIT 3");
				
				int count = 0;
				while (rs.next()) {
					count++;
					out.println("\n<span class='info'>Registro #" + count + ":</span>");
					out.println("   ID: " + rs.getInt("id_prenda"));
					out.println("   Nombre: " + rs.getString("nombre"));
					out.println("   Categoría: " + rs.getString("categoria"));
					out.println("   Precio: $" + rs.getBigDecimal("precio"));
				}
				
				if (count > 0) {
					out.println("\n<span class='ok'>✅ Se leyeron " + count + " registros</span>");
				} else {
					out.println("<span class='error'>⚠️ No se recuperaron registros</span>");
				}
			}
			out.println("</pre>");
			
		} catch (Exception e) {
			out.println("<pre class='error'>❌ Error en SELECT: " + e.getMessage());
			e.printStackTrace(out);
			out.println("</pre>");
		} finally {
			try {
				if (rs != null) rs.close();
				if (stmt != null) stmt.close();
				if (conn != null) conn.close();
			} catch (Exception e) {
				out.println("<pre class='error'>Error al cerrar: " + e.getMessage() + "</pre>");
			}
		}
		
		// ==========================================
		// PRUEBA 4: Usar PrendaDAO
		// ==========================================
		out.println("<h2>📋 PRUEBA 4: Usar PrendaDAO.listarPrendas()</h2>");
		
		try {
			out.println("<pre>");
			out.println("Instanciando PrendaDAO...");
			PrendaDAO prendaDAO = new PrendaDAO();
			
			out.println("Llamando a listarPrendas()...");
			List<Prenda> prendas = prendaDAO.listarPrendas();
			
			if (prendas != null) {
				out.println("<span class='ok'>✅ PrendaDAO retornó lista (size: " + prendas.size() + ")</span>");
				
				if (prendas.isEmpty()) {
					out.println("<span class='error'>⚠️ LA LISTA ESTÁ VACÍA</span>");
				} else {
					out.println("\nPrimeras 3 prendas:");
					for (int i = 0; i < Math.min(3, prendas.size()); i++) {
						Prenda p = prendas.get(i);
						out.println("\n<span class='info'>Prenda #" + (i+1) + ":</span>");
						out.println("   ID: " + p.getIdPrenda());
						out.println("   Nombre: " + p.getNombre());
						out.println("   Categoría: " + p.getCategoria());
						out.println("   Precio: $" + p.getPrecio());
					}
				}
			} else {
				out.println("<span class='error'>❌ PrendaDAO retornó NULL</span>");
			}
			out.println("</pre>");
			
		} catch (Exception e) {
			out.println("<pre class='error'>❌ Error con PrendaDAO: " + e.getMessage());
			e.printStackTrace(out);
			out.println("</pre>");
		}
		
		// ==========================================
		// RESUMEN
		// ==========================================
		out.println("<hr>");
		out.println("<h2>📊 RESUMEN</h2>");
		out.println("<p>Si todas las pruebas pasaron excepto la PRUEBA 4, el problema está en PrendaDAO.</p>");
		out.println("<p>Si la PRUEBA 1 falló, el problema está en la conexión.</p>");
		out.println("<p>Si las pruebas 1, 2 y 3 pasaron pero la 4 falló, hay un bug en el mapeo de PrendaDAO.</p>");
		
		out.println("<hr>");
		out.println("<p><a href='" + request.getContextPath() + "/Catalogo'>← Volver al Catálogo</a></p>");
		
		out.println("</body></html>");
		out.close();
	}
}
