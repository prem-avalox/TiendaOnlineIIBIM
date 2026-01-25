package modelo.dao;

import jakarta.persistence.EntityManager;
import modelo.entidades.ItemBolsa;

public class ItemBolsaDAO {

    // Constructor vacío como usas normalmente
    public ItemBolsaDAO() {
    }

    /**
     * Paso 1.1.1 del diagrama: actualizarCantidad
     * @param em El EntityManager que viene desde BolsaDAO para mantener la transacción.
     * @param idItem El ID del ítem a modificar.
     * @param nuevaCantidad La cantidad que el usuario desea ahora.
     * @return boolean indicando si se pudo actualizar (stockDisponible).
     */
    public boolean actualizarCantidad(EntityManager em, int idItem, int nuevaCantidad) {
        try {
            // 1. Buscamos el ítem usando el EntityManager compartido
            ItemBolsa item = em.find(ItemBolsa.class, idItem);
            
            if (item == null) {
                return false;
            }

            // 2. Paso 1.1.1.1: Invocar a PrendaDAO para verificar el stock
            // Se le pasa el 'em', los datos de la prenda y la cantidad actual que tiene el usuario
            PrendaDAO prendaDAO = new PrendaDAO();
            boolean stockDisponible = prendaDAO.verificarStock(
                em,
                item.getPrenda().getIdPrenda(), 
                item.getTallaSeleccionada(), 
                nuevaCantidad, 
                item.getCantidad()
            );

            // 3. Si PrendaDAO responde que hay stock suficiente
            if (stockDisponible) {
                // Seteamos la nueva cantidad en el objeto (JPA se encargará del update al hacer commit)
                item.setCantidad(nuevaCantidad);
                return true; // Retorna stockDisponible = true
            }

            // 4. Si no hubo stock, retornamos false
            return false;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}