package recursos;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import modelo.dao.BolsaDAO;
import modelo.dao.ItemBolsaDAO;
import modelo.entidades.Bolsa;
import modelo.entidades.ItemBolsa;



@Path("/bolsa")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RecursoBolsa {
	private BolsaDAO bolsaDAO;
	private ItemBolsaDAO itemBolsaDAO;
	
	public RecursoBolsa() {
		this.bolsaDAO = new BolsaDAO();
		this.itemBolsaDAO = new ItemBolsaDAO();
	}
	@GET
	@Path("/usuario/{idUsuario}")
	public Response obtenerBolsaPorUsuario(@PathParam("idUsuario") int idUsuario) {
		try {
			// Buscar la bolsa en la base de datos
			Bolsa bolsa = bolsaDAO.buscarBolsaPorUsuario(idUsuario);
			
			// Si no existe, devolver error 404
			if (bolsa == null) {
				return Response.status(Response.Status.NOT_FOUND)
						.entity("No se encontró bolsa para el usuario")
						.build();
			}
			
			// Si existe, devolver la bolsa (Jackson la convierte a JSON)
			return Response.ok(bolsa).build();
			
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity("Error: " + e.getMessage())
					.build();
		}
	}
	
	// MÉTODO 2: GET - Calcular total de la bolsa
	@GET
	@Path("/{idBolsa}/total")
	public Response calcularTotal(@PathParam("idBolsa") int idBolsa) {
		try {
			// Buscar la bolsa
			Bolsa bolsa = bolsaDAO.buscarBolsaPorUsuario(idBolsa);
			
			if (bolsa == null) {
				return Response.status(Response.Status.NOT_FOUND)
						.entity("Bolsa no encontrada")
						.build();
			}
			
			// Calcular el total usando el método de la entidad
			double total = bolsa.calcularMontoTotal();
			
			// Devolver solo el número
			return Response.ok(total).build();
			
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity("Error: " + e.getMessage())
					.build();
		}
	}
	
	// MÉTODO 3: PUT - Actualizar cantidad de un item
	@PUT
	@Path("/item/{idItem}/cantidad/{cantidad}")
	public Response actualizarCantidad(
			@PathParam("idItem") int idItem, 
			@PathParam("cantidad") int cantidad) {
		
		try {
			// Validar que la cantidad sea positiva
			if (cantidad <= 0) {
				return Response.status(Response.Status.BAD_REQUEST)
						.entity("La cantidad debe ser mayor a 0")
						.build();
			}
			
			// Crear objeto ItemBolsa temporal con el ID
			ItemBolsa item = new ItemBolsa();
			item.setIdItem(idItem);
			
			// Actualizar en la base de datos
			boolean actualizado = itemBolsaDAO.actualizarCantidad(item, cantidad);
			
			if (actualizado) {
				return Response.ok("Cantidad actualizada a " + cantidad + " unidades").build();
			} else {
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
						.entity("Error al actualizar")
						.build();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity("Error: " + e.getMessage())
					.build();
		}
	}
	
	// MÉTODO 4: DELETE - Eliminar item
	@DELETE
	@Path("/item/{idItem}")
	public Response eliminarItem(@PathParam("idItem") int idItem) {
		try {
			// Eliminar el item de la base de datos
			itemBolsaDAO.eliminarItem(idItem);
			
			return Response.ok("Item eliminado correctamente").build();
			
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity("Error: " + e.getMessage())
					.build();
		}
	}

}
