package test;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import modelo.entidades.Categoria;
import modelo.entidades.Prenda;
import modelo.entidades.StockTalla;
import modelo.entidades.Talla;

public class PrendasTest {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistencia");
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();

        try {
            // Formato: {Nombre, Imagen, Precio, Categoria, Color, Corte, Descripcion, TallaBase}
            Object[][] datos = {
                {"Bolso Cruzado Denim", "img/bolso-cruzado-denim-gris.jpg", 25.99, Categoria.ACCESORIOS, "Gris", "N/A", "Bolso bandolera en tejido denim con correa ajustable.", Talla.UNICA},
                {"Camisa Celeste Slim", "img/camisa-celeste-slim-easy.jpg", 35.50, Categoria.CAMISAS, "Celeste", "Slim Fit", "Camisa de vestir de fácil planchado, ideal para oficina.", Talla.M},
                {"Camisa Negra Regular", "img/camisa-negra-regular-easy.jpg", 32.00, Categoria.CAMISAS, "Negro", "Regular Fit", "Camisa básica de cuello inglés y corte tradicional.", Talla.L},
                {"Camisa Negra Slim", "img/camisa-negra-slim-easy.jpg", 32.00, Categoria.CAMISAS, "Negro", "Slim Fit", "Camisa entallada con diseño moderno y elegante.", Talla.S},
                {"Camisa Slim Algodón", "img/camisa-slim-algodon.jpg", 38.00, Categoria.CAMISAS, "Blanco", "Slim Fit", "Camisa 100% algodón de tacto suave.", Talla.M},
                {"Cinturón Ante", "img/cinturon-ante.jpg", 15.00, Categoria.ACCESORIOS, "Marrón", "N/A", "Cinturón de piel vuelta con hebilla metálica.", Talla.UNICA},
                {"Gorra Blanca Sarga", "img/gorra-blanca-sarga.jpg", 12.99, Categoria.ACCESORIOS, "Blanco", "Ajustable", "Gorra clásica de sarga con seis paneles.", Talla.UNICA},
                {"Loafers Negros", "img/loafers-negro.jpg", 55.00, Categoria.CALZADO, "Negro", "Mocasín", "Zapatos tipo mocasín de piel sintética brillante.", Talla.T40},
                {"Pantalón Denim Crudo", "img/pantalon-denim-crudo-loose-fit.jpg", 45.00, Categoria.PANTALONES, "Crudo", "Loose Fit", "Jeans de corte ancho en color natural.", Talla.M},
                {"Pantalón Denim Gris", "img/pantalon-denim-gris-loose-fit.jpg", 45.00, Categoria.PANTALONES, "Gris", "Loose Fit", "Jeans desgastados de tiro medio y pierna ancha.", Talla.L},
                {"Pantalón Lino Relaxed", "img/pantalon-negro-mezcla-lino-relaxed-fit.jpg", 42.50, Categoria.PANTALONES, "Negro", "Relaxed Fit", "Pantalón ligero de mezcla de lino para verano.", Talla.M},
                {"Pantalón Traje Regular", "img/pantalon-traje-regular-fit.jpg", 50.00, Categoria.PANTALONES, "Azul Marino", "Regular Fit", "Pantalón de vestir con raya marcada.", Talla.L},
                {"Zapatos Vestir", "img/zapatos-vestir.jpg", 65.00, Categoria.CALZADO, "Negro", "Oxford", "Zapatos clásicos de cordones para eventos formales.", Talla.T42}
            };

            for (Object[] d : datos) {
                Prenda p = new Prenda();
                p.setNombrePrenda((String) d[0]);
                p.setImagen((String) d[1]);
                p.setPrecio((Double) d[2]);
                p.setCategoria((Categoria) d[3]);
                p.setColor((String) d[4]);
                p.setCorte((String) d[5]);
                p.setDescripcion((String) d[6]);

                // Crear el stock inicial para la prenda
                StockTalla st = new StockTalla();
                st.setCantidad(10); // Cantidad inicial por defecto
                st.setTalla((Talla) d[7]);
                st.setPrenda(p); // Establecer la relación bidireccional

                // Añadir a la lista de la prenda
                p.getStockTallas().add(st);
                
                em.persist(p);
            }

            em.getTransaction().commit();
            System.out.println("Catálogo y Stock inicial insertados correctamente.");
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
            emf.close();
        }
    }
}