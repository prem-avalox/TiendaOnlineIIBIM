# 👔 Tienda Online - Clothing Store

Sistema de comercio electrónico de ropa masculina desarrollado con **Java EE**, **JSP**, **Servlets** y **MySQL**.

## 🚀 Características

- 🛍️ **Catálogo de productos** con búsqueda y filtros
- 👕 **Detalle de productos** con selector de tallas
- 🛒 **Carrito de compras** con gestión de cantidades
- 👤 **Autenticación** de usuarios (Login/Registro)
- ⚙️ **Panel de administración** con CRUD de productos
- 📱 **Diseño responsive** y moderno
- 🎨 **Interfaz elegante** inspirada en tiendas premium

## 🛠️ Tecnologías

- **Backend**: Java 17, Jakarta EE (Servlets, JSP)
- **Frontend**: HTML5, CSS3, JavaScript
- **Base de datos**: MySQL 8.0
- **Servidor**: Apache Tomcat 10.1
- **Build**: Maven 3.8+

## 📋 Requisitos Previos

- Java JDK 17 o superior
- Apache Tomcat 10.1
- MySQL 8.0 o superior
- Maven 3.8+
- Eclipse IDE for Enterprise Java and Web Developers (recomendado)

## ⚙️ Instalación

### 1. Clonar el repositorio

```bash
git clone https://github.com/tu-usuario/tiendaOnline.git
cd tiendaOnline
```

### 2. Configurar la base de datos

```bash
# Iniciar MySQL
mysql -u root -p

# Ejecutar el script de instalación
source database/INSTALAR_BD_COMPLETA.sql
```

### 3. Configurar la conexión a la base de datos

Edita el archivo `src/main/java/util/ConexionBD.java` y ajusta las credenciales:

```java
private static final String USUARIO = "root";
private static final String CONTRASENA = "tu_contraseña";
```

### 4. Compilar el proyecto

```bash
mvn clean package
```

### 5. Importar en Eclipse

1. **File → Import → Maven → Existing Maven Projects**
2. Selecciona la carpeta del proyecto
3. Click en **Finish**

### 6. Configurar Tomcat en Eclipse

1. Window → Preferences → Server → Runtime Environments
2. Add → Apache Tomcat v10.1
3. Selecciona la ruta de instalación de Tomcat

### 7. Ejecutar el proyecto

1. Click derecho en el proyecto → **Run As → Run on Server**
2. Selecciona Tomcat 10.1
3. Abre el navegador en: `http://localhost:8080/tiendaOnline`

## 📁 Estructura del Proyecto

```
tiendaOnline/
├── src/main/java/
│   ├── dao/              # Capa de acceso a datos
│   ├── modelo/           # Modelos de dominio
│   ├── servlets/         # Controladores (Servlets)
│   └── util/             # Utilidades (Conexión BD)
├── src/main/webapp/
│   ├── WEB-INF/          # Configuración y librerías
│   ├── css/              # Estilos CSS
│   ├── js/               # Scripts JavaScript
│   ├── images/           # Imágenes de productos
│   └── *.jsp             # Vistas JSP
├── database/             # Scripts SQL
├── pom.xml               # Configuración Maven
└── README.md             # Este archivo
```

## 🔧 Solución de Problemas

### Error: Driver MySQL no encontrado

Si obtienes `ClassNotFoundException: com.mysql.cj.jdbc.Driver`:

```bash
mvn clean package
```

Luego reinicia el servidor Tomcat en Eclipse.

### Error de conexión a MySQL

Verifica que:
- MySQL esté corriendo
- La base de datos `tienda_online` exista
- Las credenciales en `ConexionBD.java` sean correctas

### El servidor no inicia

1. Limpia el proyecto: **Project → Clean**
2. Limpia el servidor: Click derecho en Tomcat → **Clean...**
3. Republica: Click derecho en Tomcat → **Clean Tomcat Work Directory**

## 👥 Usuarios de Prueba

### Administrador
- **Correo**: `admin@tienda.com`
- **Contraseña**: `admin123`

### Cliente
- **Correo**: `cliente@test.com`
- **Contraseña**: `cliente123`

## 📝 Funcionalidades Implementadas

### Para Clientes
- ✅ Ver catálogo de productos
- ✅ Buscar y filtrar productos
- ✅ Ver detalle de producto con selector de tallas
- ✅ Agregar productos al carrito
- ✅ Modificar cantidades en el carrito
- ✅ Eliminar productos del carrito
- ✅ Registro de nuevos usuarios
- ✅ Login de usuarios existentes

### Para Administradores
- ✅ Ver lista de todos los productos
- ✅ Agregar nuevos productos
- ✅ Editar productos existentes
- ✅ Eliminar productos
- ✅ Gestión de imágenes

## 🤝 Contribuir

Las contribuciones son bienvenidas. Para cambios importantes:

1. Haz un Fork del proyecto
2. Crea una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request

## 📄 Licencia

Este proyecto está bajo la Licencia MIT. Ver el archivo `LICENSE` para más detalles.

## 👨‍💻 Autor

Desarrollado como proyecto académico de Java EE.

## 📞 Contacto

Para preguntas o sugerencias, abre un issue en el repositorio.

---

⭐ Si te gustó este proyecto, considera darle una estrella en GitHub!
