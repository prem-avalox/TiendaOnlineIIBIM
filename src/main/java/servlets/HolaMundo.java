package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException; // Nota: Si usas Java antiguo es javax.servlet
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// Esta anotación define la URL. En el navegador entrarás como /HolaMundo
@WebServlet("/HolaMundo")
public class HolaMundo extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public HolaMundo() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 1. Decimos que el tipo de contenido es HTML
		response.setContentType("text/html");
		
		// 2. Obtenemos el objeto para escribir la respuesta
		PrintWriter out = response.getWriter();
		
		// 3. Escribimos el HTML
		out.println("<html>");
		out.println("<body>");
		out.println("<h1>Hola Mundo desde un Servlet</h1>");
		out.println("</body>");
		out.println("</html>");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}