package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet de prueba para verificar que Tomcat carga la aplicación
 */
@WebServlet("/test")
public class TestServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Test - tiendaOnline</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>✅ ¡LA APLICACIÓN FUNCIONA!</h1>");
        out.println("<p>Si ves esto, Tomcat está cargando correctamente tiendaOnline.</p>");
        out.println("<hr>");
        out.println("<h2>Información del Sistema:</h2>");
        out.println("<ul>");
        out.println("<li>Context Path: " + request.getContextPath() + "</li>");
        out.println("<li>Servlet Path: " + request.getServletPath() + "</li>");
        out.println("<li>Server Info: " + getServletContext().getServerInfo() + "</li>");
        out.println("</ul>");
        out.println("<hr>");
        out.println("<h2>Próximos pasos:</h2>");
        out.println("<ul>");
        out.println("<li><a href='" + request.getContextPath() + "/'>Ir a index.jsp</a></li>");
        out.println("<li><a href='" + request.getContextPath() + "/Catalogo'>Ir al Catálogo</a></li>");
        out.println("</ul>");
        out.println("</body>");
        out.println("</html>");
        
        out.close();
    }
}
