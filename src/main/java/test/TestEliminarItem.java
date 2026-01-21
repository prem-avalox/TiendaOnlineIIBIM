package test;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import modelo.dao.BolsaDAO;
import modelo.dao.ItemBolsaDAO;
import modelo.dao.UsuarioDAO;
import modelo.entidades.Bolsa;
import modelo.entidades.ItemBolsa;
import modelo.entidades.Usuario;

/**
 * Test para verificar la eliminación de items
 */
public class TestEliminarItem {

    public static void main(String[] args) {
        System.out.println("🧪 TEST: Eliminar Item de Bolsa");
        System.out.println("===============================");
        
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        BolsaDAO bolsaDAO = new BolsaDAO();
        ItemBolsaDAO itemBolsaDAO = new ItemBolsaDAO();
        
        // 1. Obtener usuario martin y su bolsa
        Usuario martin = usuarioDAO.buscarPorNombreUsuario("martin");
        if (martin == null) {
            System.err.println("❌ Usuario 'martin' no encontrado. Ejecuta PoblarBaseDatos primero.");
            return;
        }
        
        Bolsa bolsa = bolsaDAO.buscarBolsaPorUsuario(martin.getIdUsuario());
        if (bolsa == null || bolsa.getItems().isEmpty()) {
            System.err.println("❌ La bolsa de martin está vacía o no existe. Ejecuta AgregarItemsBolsaMartin primero.");
            return;
        }
        
        System.out.println("✅ Estado inicial:");
        System.out.println("   - Bolsa ID: " + bolsa.getIdBolsa());
        System.out.println("   - Items: " + bolsa.getItems().size());
        
        // 2. Seleccionar el primer item para eliminar
        ItemBolsa itemAEliminar = bolsa.getItems().get(0);
        int idItem = itemAEliminar.getIdItem();
        System.out.println("   - Eliminando item ID: " + idItem + " (" + itemAEliminar.getPrenda().getNombrePrenda() + ")");
        
        // 3. Eliminar item
        itemBolsaDAO.eliminarItem(idItem);
        
        // 4. Verificar resultado (recargar bolsa desde BD)
        System.out.println("\n🔄 Verificando resultado...");
        Bolsa bolsaActualizada = bolsaDAO.buscarBolsaPorUsuario(martin.getIdUsuario());
        
        System.out.println("   - Items restantes: " + bolsaActualizada.getItems().size());
        
        boolean eliminado = true;
        for (ItemBolsa item : bolsaActualizada.getItems()) {
            if (item.getIdItem() == idItem) {
                eliminado = false;
                break;
            }
        }
        
        if (eliminado) {
            System.out.println("✅ ÉXITO: El item fue eliminado correctamente de la BD y de la lista.");
        } else {
            System.out.println("❌ ERROR: El item SIGUE apareciendo en la lista de la bolsa.");
        }
    }
}
