package test;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import modelo.entidades.Categoria;
import modelo.entidades.Color;
import modelo.entidades.Corte;
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
                {"Bolso Cruzado Denim", "img/bolso-cruzado-denim-gris.jpg", 25.99, Categoria.ACCESORIOS, Color.GRIS, Corte.REGULAR, "Bolso bandolera en tejido denim con correa ajustable.", Talla.UNICA},
                {"Camisa Celeste Slim", "img/camisa-celeste-slim-easy.jpg", 35.50, Categoria.CAMISAS, Color.CELESTE, Corte.SLIM, "Camisa de vestir de fácil planchado, ideal para oficina.", Talla.M},
                {"Camisa Negra Regular", "img/camisa-negra-regular-easy.jpg", 32.00, Categoria.CAMISAS, Color.NEGRO, Corte.REGULAR, "Camisa básica de cuello inglés y corte tradicional.", Talla.L},
                {"Camisa Negra Slim", "img/camisa-negra-slim-easy.jpg", 32.00, Categoria.CAMISAS, Color.NEGRO, Corte.SLIM, "Camisa entallada con diseño moderno y elegante.", Talla.S},
                {"Camisa Slim Algodón", "img/camisa-slim-algodon.jpg", 38.00, Categoria.CAMISAS, Color.BLANCO, Corte.SLIM, "Camisa 100% algodón de tacto suave.", Talla.M},
                {"Cinturón Ante", "img/cinturon-ante.jpg", 15.00, Categoria.ACCESORIOS, Color.NEGRO, Corte.REGULAR, "Cinturón de piel vuelta con hebilla metálica.", Talla.UNICA},
                {"Gorra Blanca Sarga", "img/gorra-blanca-sarga.jpg", 12.99, Categoria.ACCESORIOS, Color.BLANCO, Corte.REGULAR, "Gorra clásica de sarga con seis paneles.", Talla.UNICA},
                {"Loafers Negros", "img/loafers-negro.jpg", 55.00, Categoria.CALZADO, Color.NEGRO, Corte.REGULAR, "Zapatos tipo mocasín de piel sintética brillante.", Talla.T40},
                {"Pantalón Denim Crudo", "img/pantalon-denim-crudo-loose-fit.jpg", 45.00, Categoria.PANTALONES, Color.CRUDO, Corte.RELAJADO, "Jeans de corte ancho en color natural.", Talla.M},
                {"Pantalón Denim Gris", "img/pantalon-denim-gris-loose-fit.jpg", 45.00, Categoria.PANTALONES, Color.GRIS, Corte.RELAJADO, "Jeans desgastados de tiro medio y pierna ancha.", Talla.L},
                {"Pantalón Lino Relaxed", "img/pantalon-negro-mezcla-lino-relaxed-fit.jpg", 42.50, Categoria.PANTALONES, Color.NEGRO, Corte.RELAJADO, "Pantalón ligero de mezcla de lino para verano.", Talla.M},
                {"Pantalón Traje Regular", "img/pantalon-traje-regular-fit.jpg", 50.00, Categoria.PANTALONES, Color.NEGRO, Corte.REGULAR, "Pantalón de vestir con raya marcada.", Talla.L},
                {"Zapatos Vestir", "img/zapatos-vestir.jpg", 65.00, Categoria.CALZADO, Color.NEGRO, Corte.REGULAR, "Zapatos clásicos de cordones para eventos formales.", Talla.T42}
            };

            for (Object[] d : datos) {
                Prenda p = new Prenda();
                p.setNombrePrenda((String) d[0]);
                p.setImagen((String) d[1]);
                p.setPrecio((Double) d[2]);
                p.setCategoria((Categoria) d[3]);
                p.setColor((Color) d[4]); // Actualizado a Enum
                p.setCorte((Corte) d[5]); // Actualizado a Enum
                p.setDescripcion((String) d[6]);

                StockTalla st = new StockTalla();
                st.setCantidad(10);
                st.setTalla((Talla) d[7]);
                st.setPrenda(p);
                p.getStockTallas().add(st);
                
                em.persist(p);
            }

            em.getTransaction().commit();
            System.out.println("Catálogo insertado con Enums correctamente.");
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
            emf.close();
        }
    }
}