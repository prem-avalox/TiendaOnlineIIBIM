package modelo.dao;

import jakarta.persistence.EntityManager;
import modelo.entidades.Bolsa;
import modelo.entidades.ItemBolsa;
import modelo.entidades.Prenda;
import modelo.entidades.Talla;

public class ItemBolsaDAO {

    private EntityManager em;

    public ItemBolsaDAO(EntityManager em) {
        this.em = em;
    }
    
    
    public boolean agregarItem(
            Bolsa bolsa,
            Prenda prenda,
            Talla talla,
            int cantidad) {

        try {
            // 1. Verificar stock
            PrendaDAO prendaDAO = new PrendaDAO(em);
            boolean stockDisponible = prendaDAO.verificarStock(talla, cantidad);

            if (!stockDisponible) {
                return false;
            }

            // 2. Verificar si el item ya existe (misma prenda + talla)
            ItemBolsa itemExistente = bolsa.getItems()
                    .stream()
                    .filter(i ->
                            i.getPrenda().getIdPrenda() == prenda.getIdPrenda() &&
                            i.getTallaSeleccionada() == talla)
                    .findFirst()
                    .orElse(null);

            if (itemExistente != null) {
                // Si existe, solo aumentar cantidad
                itemExistente.setCantidad(
                        itemExistente.getCantidad() + cantidad
                );
                return true;
            }

            // 3. Crear nuevo ItemBolsa
            ItemBolsa nuevoItem = new ItemBolsa();
            nuevoItem.setPrenda(prenda);
            nuevoItem.setTallaSeleccionada(talla);
            nuevoItem.setCantidad(cantidad);
            nuevoItem.setBolsa(bolsa);

            // 4. Agregar a la bolsa (composición)
            bolsa.getItems().add(nuevoItem);

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    

    /**
     * Actualiza la cantidad de un item de la bolsa
     */
    public boolean actualizarCantidad(int idItem, int nuevaCantidad) {

        ItemBolsa item = em.find(ItemBolsa.class, idItem);

        if (item == null) {
            return false;
        }

        Talla talla = item.getTallaSeleccionada();

        PrendaDAO prendaDAO = new PrendaDAO(em);

        boolean stockDisponible =
                prendaDAO.verificarStock(talla, nuevaCantidad);

        if (!stockDisponible) {
            return false;
        }

        item.setCantidad(nuevaCantidad);
        return true;
    }

}
