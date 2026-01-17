package recursos;

import java.util.List;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import modelo.dao.PrendaDAO;
import modelo.entidades.Prenda;

@Path("/prendas")
public class RecursoPrenda {

    private PrendaDAO prendaDAO;

    public RecursoPrenda() {
        this.prendaDAO = new PrendaDAO();
    }

    // =====================================================
    // GET - Listar todas las prendas
    // =====================================================
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Prenda> getPrendas() {
        return prendaDAO.getListaPrendas();
    }

    // =====================================================
    // GET - Obtener prenda por ID
    // =====================================================
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Prenda getPrendaPorId(@PathParam("id") int id) {
        return prendaDAO.getPrenda(id);
    }

    /*
    // =====================================================
    // POST - Crear prenda
    // =====================================================
    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public boolean agregarPrenda(Prenda prenda) {
        return prendaDAO.insertar(prenda);
    }*/

    // =====================================================
    // PUT - Actualizar prenda
    // =====================================================
    @PUT
    @Path("/update")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public boolean actualizarPrenda(Prenda prenda) {
        return prendaDAO.actualizar(prenda);
    }

    // =====================================================
    // DELETE - Eliminar prenda
    // =====================================================
    @DELETE
    @Path("/delete/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public boolean eliminarPrenda(@PathParam("id") int id) {
        return prendaDAO.eliminar(id);
    }
}
