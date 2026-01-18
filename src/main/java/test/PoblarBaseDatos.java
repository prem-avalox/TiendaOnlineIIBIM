package test;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import modelo.entidades.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase para poblar la base de datos usando JPA/ORM
 * Ejecutar como Java Application para insertar datos de prueba
 */
public class PoblarBaseDatos {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistencia");
        EntityManager em = emf.createEntityManager();

        try {
            System.out.println("🚀 Iniciando población de base de datos...\n");

            em.getTransaction().begin();

            // ========================================
            // 1. CREAR USUARIOS
            // ========================================
            System.out.println("👤 Creando usuarios...");
            
            Usuario martin = new Usuario();
            martin.setNombreUsuario("martin");
            martin.setEmail("martin@tienda.com");
            martin.setContrasena("martin123");
            martin.setIsAdmin(false);
            em.persist(martin);

            Usuario testuser = new Usuario();
            testuser.setNombreUsuario("testuser");
            testuser.setEmail("test@example.com");
            testuser.setContrasena("password123");
            testuser.setIsAdmin(false);
            em.persist(testuser);

            Usuario admin = new Usuario();
            admin.setNombreUsuario("admin");
            admin.setEmail("admin@tienda.com");
            admin.setContrasena("admin123");
            admin.setIsAdmin(true);
            em.persist(admin);

            System.out.println("   ✅ 3 usuarios creados\n");

            // ========================================
            // 2. CREAR PRENDAS CON STOCK
            // ========================================
            System.out.println("👕 Creando prendas...");

            // CAMISA 1: Celeste Slim
            Prenda camisaCeleste = crearPrenda(
                "camisa-celeste-slim-easy.jpg",
                "Camisa Celeste Slim Fit Easy Iron",
                34.99,
                "Camisa elegante de corte slim con tecnología easy iron",
                Color.CELESTE,
                Corte.SLIM,
                Categoria.CAMISAS
            );
            agregarStock(camisaCeleste, Talla.S, 30);
            agregarStock(camisaCeleste, Talla.M, 50);
            agregarStock(camisaCeleste, Talla.L, 40);
            agregarStock(camisaCeleste, Talla.XL, 25);
            em.persist(camisaCeleste);

            // CAMISA 2: Negra Slim
            Prenda camisaNegraSlim = crearPrenda(
                "camisa-negra-slim-easy.jpg",
                "Camisa Negra Slim Fit Easy Iron",
                34.99,
                "Camisa negra de corte slim con tecnología easy iron",
                Color.NEGRO,
                Corte.SLIM,
                Categoria.CAMISAS
            );
            agregarStock(camisaNegraSlim, Talla.S, 25);
            agregarStock(camisaNegraSlim, Talla.M, 45);
            agregarStock(camisaNegraSlim, Talla.L, 35);
            agregarStock(camisaNegraSlim, Talla.XL, 20);
            em.persist(camisaNegraSlim);

            // CAMISA 3: Negra Regular
            Prenda camisaNegraRegular = crearPrenda(
                "camisa-negra-regular-easy.jpg",
                "Camisa Negra Regular Fit Easy Iron",
                32.99,
                "Camisa negra de corte regular con tecnología easy iron",
                Color.NEGRO,
                Corte.REGULAR,
                Categoria.CAMISAS
            );
            agregarStock(camisaNegraRegular, Talla.S, 35);
            agregarStock(camisaNegraRegular, Talla.M, 55);
            agregarStock(camisaNegraRegular, Talla.L, 45);
            agregarStock(camisaNegraRegular, Talla.XL, 30);
            em.persist(camisaNegraRegular);

            // CAMISA 4: Blanca Algodón
            Prenda camisaBlanca = crearPrenda(
                "camisa-slim-algodon.jpg",
                "Camisa Slim Fit Algodón",
                29.99,
                "Camisa slim fit 100% algodón, perfecta para cualquier ocasión",
                Color.BLANCO,
                Corte.SLIM,
                Categoria.CAMISAS
            );
            agregarStock(camisaBlanca, Talla.S, 40);
            agregarStock(camisaBlanca, Talla.M, 60);
            agregarStock(camisaBlanca, Talla.L, 50);
            agregarStock(camisaBlanca, Talla.XL, 35);
            em.persist(camisaBlanca);

            // PANTALÓN 1: Traje Regular
            Prenda pantalonTraje = crearPrenda(
                "pantalon-traje-regular-fit.jpg",
                "Pantalón de Traje Regular Fit",
                49.99,
                "Pantalón formal de corte regular, ideal para oficina",
                Color.NEGRO,
                Corte.REGULAR,
                Categoria.PANTALONES
            );
            agregarStock(pantalonTraje, Talla.S, 20);
            agregarStock(pantalonTraje, Talla.M, 40);
            agregarStock(pantalonTraje, Talla.L, 35);
            agregarStock(pantalonTraje, Talla.XL, 25);
            em.persist(pantalonTraje);

            // PANTALÓN 2: Lino Relaxed
            Prenda pantalonLino = crearPrenda(
                "pantalon-negro-mezcla-lino-relaxed-fit.jpg",
                "Pantalón Negro Mezcla Lino Relaxed Fit",
                44.99,
                "Pantalón casual con mezcla de lino, corte relajado y cómodo",
                Color.NEGRO,
                Corte.RELAJADO,
                Categoria.PANTALONES
            );
            agregarStock(pantalonLino, Talla.S, 25);
            agregarStock(pantalonLino, Talla.M, 45);
            agregarStock(pantalonLino, Talla.L, 40);
            agregarStock(pantalonLino, Talla.XL, 30);
            em.persist(pantalonLino);

            // PANTALÓN 3: Denim Gris
            Prenda pantalonDenimGris = crearPrenda(
                "pantalon-denim-gris-loose-fit.jpg",
                "Pantalón Denim Gris Loose Fit",
                39.99,
                "Pantalón denim gris de corte holgado, estilo casual",
                Color.GRIS,
                Corte.OVERSIZE,
                Categoria.PANTALONES
            );
            agregarStock(pantalonDenimGris, Talla.S, 30);
            agregarStock(pantalonDenimGris, Talla.M, 50);
            agregarStock(pantalonDenimGris, Talla.L, 45);
            agregarStock(pantalonDenimGris, Talla.XL, 35);
            em.persist(pantalonDenimGris);

            // PANTALÓN 4: Denim Crudo
            Prenda pantalonDenimCrudo = crearPrenda(
                "pantalon-denim-crudo-loose-fit.jpg",
                "Pantalón Denim Crudo Loose Fit",
                39.99,
                "Pantalón denim color crudo de corte holgado",
                Color.CRUDO,
                Corte.OVERSIZE,
                Categoria.PANTALONES
            );
            agregarStock(pantalonDenimCrudo, Talla.S, 28);
            agregarStock(pantalonDenimCrudo, Talla.M, 48);
            agregarStock(pantalonDenimCrudo, Talla.L, 42);
            agregarStock(pantalonDenimCrudo, Talla.XL, 32);
            em.persist(pantalonDenimCrudo);

            // CALZADO 1: Loafers
            Prenda loafers = crearPrenda(
                "loafers-negro.jpg",
                "Loafers Clásicos Negro",
                69.99,
                "Zapatos loafers de cuero negro, elegantes y versátiles",
                Color.NEGRO,
                Corte.REGULAR,
                Categoria.CALZADO
            );
            agregarStock(loafers, Talla.S, 15);
            agregarStock(loafers, Talla.M, 30);
            agregarStock(loafers, Talla.L, 25);
            agregarStock(loafers, Talla.XL, 20);
            em.persist(loafers);

            // CALZADO 2: Zapatos de Vestir
            Prenda zapatosVestir = crearPrenda(
                "zapatos-vestir.jpg",
                "Zapatos de Vestir Formales",
                79.99,
                "Zapatos formales de cuero para ocasiones especiales",
                Color.NEGRO,
                Corte.REGULAR,
                Categoria.CALZADO
            );
            agregarStock(zapatosVestir, Talla.S, 12);
            agregarStock(zapatosVestir, Talla.M, 25);
            agregarStock(zapatosVestir, Talla.L, 20);
            agregarStock(zapatosVestir, Talla.XL, 15);
            em.persist(zapatosVestir);

            // ACCESORIO 1: Cinturón
            Prenda cinturon = crearPrenda(
                "cinturon-ante.jpg",
                "Cinturón de Ante Clásico",
                24.99,
                "Cinturón elegante de ante con hebilla metálica",
                Color.NEGRO,
                Corte.REGULAR,
                Categoria.ACCESORIOS
            );
            agregarStock(cinturon, Talla.S, 50);
            agregarStock(cinturon, Talla.M, 80);
            agregarStock(cinturon, Talla.L, 70);
            agregarStock(cinturon, Talla.XL, 50);
            em.persist(cinturon);

            // ACCESORIO 2: Gorra
            Prenda gorra = crearPrenda(
                "gorra-blanca-sarga.jpg",
                "Gorra Blanca de Sarga",
                19.99,
                "Gorra deportiva de sarga con ajuste posterior",
                Color.BLANCO,
                Corte.REGULAR,
                Categoria.ACCESORIOS
            );
            agregarStock(gorra, Talla.M, 100); // Talla única
            em.persist(gorra);

            // ACCESORIO 3: Bolso
            Prenda bolso = crearPrenda(
                "bolso-cruzado-denim-gris.jpg",
                "Bolso Cruzado Denim Gris",
                34.99,
                "Bolso pequeño cruzado de denim con múltiples compartimentos",
                Color.GRIS,
                Corte.REGULAR,
                Categoria.ACCESORIOS
            );
            agregarStock(bolso, Talla.M, 60); // Talla única
            em.persist(bolso);

            System.out.println("   ✅ 13 prendas creadas con stock\n");

            // Flush para obtener IDs generados
            em.flush();

            // ========================================
            // 3. CREAR BOLSAS
            // ========================================
            System.out.println("🛒 Creando bolsas...");

            // Bolsa para Martin (con items)
            Bolsa bolsaMartin = new Bolsa();
            bolsaMartin.setUsuario(martin);
            bolsaMartin.setPrecioTotal(0.0);
            em.persist(bolsaMartin);

            // Bolsa para Testuser (con items)
            Bolsa bolsaTestuser = new Bolsa();
            bolsaTestuser.setUsuario(testuser);
            bolsaTestuser.setPrecioTotal(0.0);
            em.persist(bolsaTestuser);

            // Bolsa para Admin (vacía)
            Bolsa bolsaAdmin = new Bolsa();
            bolsaAdmin.setUsuario(admin);
            bolsaAdmin.setPrecioTotal(0.0);
            em.persist(bolsaAdmin);

            System.out.println("   ✅ 3 bolsas creadas\n");

            // Flush para obtener IDs de bolsas
            em.flush();

            // ========================================
            // 4. AGREGAR ITEMS A BOLSA DE MARTIN
            // ========================================
            System.out.println("📦 Agregando items a bolsa de Martin...");

            // Item 1: Camisa Celeste x2 (Talla M)
            ItemBolsa item1 = new ItemBolsa();
            item1.setPrenda(camisaCeleste);
            item1.setCantidad(2);
            item1.setTallaSeleccionada(Talla.M);
            item1.setBolsa(bolsaMartin);
            bolsaMartin.getItems().add(item1);
            em.persist(item1);

            // Item 2: Pantalón Traje x1 (Talla L)
            ItemBolsa item2 = new ItemBolsa();
            item2.setPrenda(pantalonTraje);
            item2.setCantidad(1);
            item2.setTallaSeleccionada(Talla.L);
            item2.setBolsa(bolsaMartin);
            bolsaMartin.getItems().add(item2);
            em.persist(item2);

            // Item 3: Loafers x1 (Talla M)
            ItemBolsa item3 = new ItemBolsa();
            item3.setPrenda(loafers);
            item3.setCantidad(1);
            item3.setTallaSeleccionada(Talla.M);
            item3.setBolsa(bolsaMartin);
            bolsaMartin.getItems().add(item3);
            em.persist(item3);

            // Item 4: Cinturón x1 (Talla M)
            ItemBolsa item4 = new ItemBolsa();
            item4.setPrenda(cinturon);
            item4.setCantidad(1);
            item4.setTallaSeleccionada(Talla.M);
            item4.setBolsa(bolsaMartin);
            bolsaMartin.getItems().add(item4);
            em.persist(item4);

            // Calcular total usando el método del modelo (como debe ser!)
            double totalMartin = bolsaMartin.calcularMontoTotal();
            bolsaMartin.setPrecioTotal(totalMartin);
            em.merge(bolsaMartin);

            System.out.println("   ✅ 4 items agregados (Total: $" + String.format("%.2f", totalMartin) + ")\n");

            // ========================================
            // 5. AGREGAR ITEMS A BOLSA DE TESTUSER
            // ========================================
            System.out.println("📦 Agregando items a bolsa de Testuser...");

            // Item 1: Camisa Negra Slim x1 (Talla L)
            ItemBolsa item5 = new ItemBolsa();
            item5.setPrenda(camisaNegraSlim);
            item5.setCantidad(1);
            item5.setTallaSeleccionada(Talla.L);
            item5.setBolsa(bolsaTestuser);
            bolsaTestuser.getItems().add(item5);
            em.persist(item5);

            // Item 2: Pantalón Denim Gris x2 (Talla M)
            ItemBolsa item6 = new ItemBolsa();
            item6.setPrenda(pantalonDenimGris);
            item6.setCantidad(2);
            item6.setTallaSeleccionada(Talla.M);
            item6.setBolsa(bolsaTestuser);
            bolsaTestuser.getItems().add(item6);
            em.persist(item6);

            // Calcular total usando el método del modelo
            double totalTestuser = bolsaTestuser.calcularMontoTotal();
            bolsaTestuser.setPrecioTotal(totalTestuser);
            em.merge(bolsaTestuser);

            System.out.println("   ✅ 2 items agregados (Total: $" + String.format("%.2f", totalTestuser) + ")\n");

            // ========================================
            // COMMIT
            // ========================================
            em.getTransaction().commit();

            System.out.println("✅ BASE DE DATOS POBLADA EXITOSAMENTE!\n");
            System.out.println("📊 RESUMEN:");
            System.out.println("   - 3 usuarios");
            System.out.println("   - 13 prendas");
            System.out.println("   - 50 registros de stock");
            System.out.println("   - 3 bolsas");
            System.out.println("   - 6 items en bolsas");
            System.out.println("\n👤 USUARIOS CREADOS:");
            System.out.println("   • martin / martin123");
            System.out.println("   • testuser / password123");
            System.out.println("   • admin / admin123");
            System.out.println("\n🎉 ¡Listo para probar!");

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("❌ ERROR al poblar base de datos:");
            e.printStackTrace();
        } finally {
            em.close();
            emf.close();
        }
    }

    /**
     * Método helper para crear prendas
     */
    private static Prenda crearPrenda(String imagen, String nombre, double precio,
                                      String descripcion, Color color, Corte corte,
                                      Categoria categoria) {
        Prenda prenda = new Prenda();
        prenda.setImagen(imagen);
        prenda.setNombrePrenda(nombre);
        prenda.setPrecio(precio);
        prenda.setDescripcion(descripcion);
        prenda.setColor(color);
        prenda.setCorte(corte);
        prenda.setCategoria(categoria);
        prenda.setStockTallas(new ArrayList<>());
        return prenda;
    }

    /**
     * Método helper para agregar stock a una prenda
     */
    private static void agregarStock(Prenda prenda, Talla talla, int cantidad) {
        StockTalla stock = new StockTalla();
        stock.setCantidad(cantidad);
        stock.setTalla(talla);
        stock.setPrenda(prenda);
        prenda.getStockTallas().add(stock);
    }
}
