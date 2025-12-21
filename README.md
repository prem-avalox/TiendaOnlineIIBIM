# 👔 CLOTHING STORE - Tienda Online de Ropa Masculina

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://adoptium.net/)
[![Maven](https://img.shields.io/badge/Maven-3.8+-red.svg)](https://maven.apache.org/)
[![Tomcat](https://img.shields.io/badge/Tomcat-10.1-yellow.svg)](https://tomcat.apache.org/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-blue.svg)](https://www.mysql.com/)
[![License](https://img.shields.io/badge/License-MIT-green.svg)]()

> Sistema de comercio electrónico desarrollado con **Java EE**, **JSP**, **Servlets**, **MySQL** y arquitectura **MVC**.  
> Proyecto migrado desde PHP con diseño profesional tipo Clothing Store.

---

## 🚀 Inicio Rápido

### Para tu Equipo (Windows, macOS, Linux):

```bash
# 1. Clonar proyecto
git clone <url-del-repo>
cd tiendaOnline

# 2. Instalar dependencias (automático con Maven)
mvn clean install

# 3. Importar en Eclipse
File → Import → Maven → Existing Maven Projects

# 4. Ejecutar
Run As → Run on Server
```

**📖 Ver guía completa:** [`INICIO_RAPIDO.md`](INICIO_RAPIDO.md)

---

## 📋 Características

### ✨ Funcionalidades Principales

- **🛍️ Catálogo de Productos** - Grid responsive de 4 columnas con animaciones
- **🔍 Búsqueda y Filtros** - Por categoría, color y tipo de ajuste
- **👕 Detalle de Producto** - Vista ampliada con selector de tallas interactivo
- **🛒 Carrito de Compras** - Sidebar deslizante con gestión de cantidades
- **👤 Autenticación** - Login/Registro con roles (Cliente/Admin)
- **⚙️ Panel Admin** - CRUD completo de productos
- **📱 Diseño Responsive** - Adaptable a móviles, tablets y desktop
- **🎨 Diseño Elegante** - Inspirado en marcas premium (H&M, Zara)

### 🏗️ Arquitectura

- **Patrón MVC** (Modelo-Vista-Controlador)
- **DAO Pattern** para acceso a datos
- **Session Management** para autenticación
- **Prepared Statements** para seguridad SQL
- **Maven** para gestión de dependencias

---

## 🛠️ Tecnologías

| Tecnología | Versión | Uso |
|------------|---------|-----|
| Java | 17+ | Backend y lógica de negocio |
| Jakarta EE | 6.0 | Servlets y JSP |
| MySQL | 8.0+ | Base de datos |
| Maven | 3.8+ | Gestión de dependencias |
| Apache Tomcat | 10.1 | Servidor de aplicaciones |
| JSTL | 3.0 | Tag libraries |
| HTML5/CSS3 | - | Frontend |
| JavaScript | ES6 | Interactividad |
| Font Awesome | 6.5.1 | Iconografía |
| Google Fonts | - | Tipografía (Playfair Display) |

---

## 📁 Estructura del Proyecto

```
tiendaOnline/
├── 📄 pom.xml                      # Configuración Maven
├── 📂 src/main/
│   ├── 📂 java/
│   │   ├── 📂 dao/                 # Data Access Objects
│   │   │   ├── UsuarioDAO.java
│   │   │   ├── PrendaDAO.java
│   │   │   └── TallaDAO.java
│   │   ├── 📂 modelo/              # Entidades
│   │   │   ├── Usuario.java
│   │   │   ├── Prenda.java
│   │   │   ├── Talla.java
│   │   │   └── ItemBolsa.java
│   │   ├── 📂 servlets/            # Controladores
│   │   │   ├── LoginServlet.java
│   │   │   ├── RegistroServlet.java
│   │   │   ├── CatalogoServlet.java
│   │   │   ├── DetallePrendaServlet.java
│   │   │   ├── BolsaServlet.java
│   │   │   └── AdminPrendasServlet.java
│   │   └── 📂 util/
│   │       └── ConexionBD.java     # Conexión MySQL
│   └── 📂 webapp/
│       ├── 📂 css/
│       │   └── estilos.css         # Estilos Clothing Store
│       ├── 📂 img/                 # 14 imágenes de productos
│       ├── 📂 jsp/
│       │   ├── catalogo.jsp
│       │   ├── detalle-prenda.jsp
│       │   ├── login.jsp
│       │   ├── registro.jsp
│       │   └── 📂 includes/
│       │       ├── header.jsp
│       │       ├── footer.jsp
│       │       ├── sidebar.jsp
│       │       └── sidebar-bolsa.jsp
│       ├── 📂 WEB-INF/
│       │   └── web.xml
│       └── index.jsp
├── 📂 database/
│   ├── INSTALL_DB.sql              # Script de instalación principal
│   ├── verificar_bd.sql            # Verificación de BD
│   └── script_tienda_online.sql
└── 📚 Documentación/
    ├── INICIO_RAPIDO.md
    ├── README_SETUP_UNIVERSAL.md
    ├── GUIA_MYSQL_INSTALACION.md
    └── CHECKLIST.md
```

---

## 💾 Base de Datos

### Tablas:

- **`usuarios`** - Gestión de usuarios (clientes y administradores)
- **`prendas`** - Catálogo de productos (13 prendas reales de Ecuador)
- **`tallas`** - Inventario de tallas y stock por prenda

### Datos de Prueba:

- **2 usuarios**: 1 admin + 1 cliente
- **13 productos**: 4 camisas, 4 pantalones, 2 calzados, 3 accesorios
- **Stock configurado** para todas las tallas

### Credenciales:

```
Administrador:
  Email: admin@tienda.com
  Pass:  admin123

Cliente:
  Email: cliente@example.com
  Pass:  cliente123
```

---

## 📦 Instalación Detallada

### 1. Requisitos Previos

- **Java JDK 17+** - [Descargar](https://adoptium.net/)
- **Maven 3.8+** - [Descargar](https://maven.apache.org/download.cgi)
- **MySQL 8.0+** - [Descargar](https://dev.mysql.com/downloads/mysql/)
- **Apache Tomcat 10.1** - [Descargar](https://tomcat.apache.org/download-10.cgi)
- **Eclipse IDE** - [Descargar](https://www.eclipse.org/downloads/packages/)

### 2. Clonar Repositorio

```bash
git clone <url-del-repositorio>
cd tiendaOnline
```

### 3. Instalar Dependencias (Maven)

**Windows:**
```cmd
install.bat
```

**macOS/Linux:**
```bash
chmod +x install.sh
./install.sh
```

**Manual:**
```bash
mvn clean install
```

### 4. Configurar MySQL

```bash
# Iniciar MySQL
# Windows: net start MySQL80
# macOS:   brew services start mysql
# Linux:   sudo systemctl start mysql

# Crear base de datos
mysql -u root -p < database/INSTALL_DB.sql
```

### 5. Configurar Conexión

Editar `src/main/java/util/ConexionBD.java`:

```java
private static final String USUARIO = "root";
private static final String CONTRASENA = "tu_contraseña";
```

### 6. Importar en Eclipse

1. File → Import
2. Maven → Existing Maven Projects
3. Browse → Seleccionar carpeta `tiendaOnline`
4. Finish

### 7. Configurar Tomcat

1. Window → Preferences → Server → Runtime Environments
2. Add → Apache Tomcat v10.1
3. En Servers: New → Server → Add `tiendaOnline`

### 8. Ejecutar

1. Run As → Run on Server
2. Abrir: http://localhost:8080/tiendaOnline/Catalogo

---

## 🎨 Diseño UI/UX

### Paleta de Colores

```css
--color-primario: #124a7e;    /* Azul oscuro premium */
--color-acento: #e0e0e0;      /* Gris claro */
--color-precio: #575757;      /* Gris medio */
--color-fondo: #ffffff;       /* Blanco */
```

### Tipografía

- **Títulos**: Playfair Display (elegante, serif)
- **Precios**: Roboto Condensed (moderna, sans-serif)

### Grid Responsivo

- **Desktop**: 4 columnas
- **Tablet**: 3 columnas
- **Tablet pequeña**: 2 columnas
- **Móvil**: 1 columna

---

## 🧪 Testing

### Probar Conexión a BD

```bash
# En Eclipse
Run: src/main/java/util/ConexionBD.java
```

### Verificar Base de Datos

```bash
mysql -u root -p < database/verificar_bd.sql
```

### Endpoints Disponibles

```
GET  /Catalogo                    # Listar productos
GET  /Catalogo?accion=buscar&termino=camisa
GET  /Catalogo?accion=categoria&cat=CAMISAS
GET  /DetallePrenda?id=1          # Ver producto
POST /Bolsa?accion=agregar        # Agregar a carrito
POST /Login                       # Iniciar sesión
POST /Registro                    # Registrar usuario
GET  /AdminPrendas                # Panel admin
```

---

## 🤝 Trabajo en Equipo

### Configuración Git

```bash
# Inicializar repositorio
git init
git add .
git commit -m "Proyecto inicial con Maven"
git branch -M main
git remote add origin <url>
git push -u origin main
```

### Para tus compañeros

```bash
# Clonar
git clone <url>
cd tiendaOnline

# Instalar dependencias
mvn clean install

# Importar en Eclipse
# File → Import → Maven → Existing Maven Projects

# Configurar su propia conexión MySQL
# Editar: src/main/java/util/ConexionBD.java

# Ejecutar
# Run As → Run on Server
```

### Ventajas de Maven

✅ **Sin JAR manual** - Descarga automática  
✅ **Multiplataforma** - Funciona en Windows, Mac, Linux  
✅ **Versionado consistente** - Todos usan las mismas librerías  
✅ **Fácil compartir** - Solo código fuente, sin binarios  
✅ **Build reproducible** - Mismo resultado en todos los equipos  

---

## 📚 Documentación

- **[INICIO_RAPIDO.md](INICIO_RAPIDO.md)** - Guía rápida de 5 minutos
- **[README_SETUP_UNIVERSAL.md](README_SETUP_UNIVERSAL.md)** - Instalación completa
- **[GUIA_MYSQL_INSTALACION.md](GUIA_MYSQL_INSTALACION.md)** - Configuración MySQL
- **[CHECKLIST.md](CHECKLIST.md)** - Lista de funcionalidades

---

## 🐛 Solución de Problemas

### Maven no reconocido (Windows)

```cmd
# Agregar a PATH:
# MAVEN_HOME=C:\apache-maven-3.x.x
# PATH=%PATH%;%MAVEN_HOME%\bin
```

### Driver not found

```bash
mvn clean install -U
# En Eclipse: Maven → Update Project
```

### Access denied MySQL

```sql
ALTER USER 'root'@'localhost' IDENTIFIED BY 'nueva_contraseña';
```

### Port 8080 in use

```bash
# Windows: netstat -ano | findstr :8080
# macOS/Linux: lsof -ti:8080 | xargs kill -9
```

---

## 📝 Licencia

MIT License - Proyecto educativo

---

## 👥 Equipo de Desarrollo

Proyecto desarrollado para el curso de Programación Web.

---

## 📞 Soporte

Para problemas:
1. Revisar logs en Eclipse Console
2. Ejecutar `ConexionBD.main()` para probar BD
3. Ejecutar `database/verificar_bd.sql`
4. Consultar documentación en carpeta raíz

---

**✨ ¡Gracias por usar Clothing Store! ✨**