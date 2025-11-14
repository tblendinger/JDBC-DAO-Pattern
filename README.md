# Proyecto JDBC con Patrón DAO

Este proyecto demuestra la implementación del patrón **Data Access Object (DAO)** en Java con JDBC, siguiendo las mejores prácticas descritas en el módulo de estudio.

## 🏗️ Estructura del Proyecto

```
src/main/java/com/example/
├── model/
│   └── Empleado.java              # Transfer Object (DTO)
├── dao/
│   ├── EmpleadoDAO.java           # Interfaz del DAO
│   ├── EmpleadoDAOImpl.java       # Implementación MySQL
│   └── DAOFactory.java            # Abstract Factory (opcional)
├── EmpleadoApp.java               # Aplicación principal
└── EmpleadoAppConFactory.java     # App usando Factory (opcional)
```

## 📚 Conceptos Implementados

### 1. Transfer Object (DTO)
La clase `Empleado` es un objeto plano que transporta datos entre capas. No contiene lógica de negocio compleja, solo getters, setters y datos.

### 2. Interfaz DAO
`EmpleadoDAO` define el contrato de operaciones CRUD:
- `insert(Empleado)` - Crear nuevo empleado
- `findById(Integer)` - Buscar por ID
- `findAll()` - Obtener todos los empleados
- `update(Empleado)` - Actualizar empleado existente
- `delete(Integer)` - Eliminar por ID
- `count()` - Contar total de empleados

### 3. Implementación DAO
`EmpleadoDAOImpl` encapsula TODA la lógica JDBC:
- Manejo de conexiones
- Preparación de statements
- Ejecución de queries SQL
- Mapeo de ResultSet a objetos
- Manejo de excepciones

### 4. Abstract Factory (Opcional)
`DAOFactory` permite crear familias de DAOs para diferentes fuentes de datos (MySQL, PostgreSQL, XML, etc.) sin cambiar el código cliente.

## 🎯 Ventajas del Patrón DAO

### Separación de Responsabilidades
El código de negocio no conoce detalles de implementación de la base de datos. Solo trabaja con objetos Empleado y métodos del DAO.

### Flexibilidad
Cambiar de MySQL a PostgreSQL solo requiere crear una nueva implementación del DAO. El resto del código permanece intacto.

### Testabilidad
Puedes crear implementaciones mock del DAO para pruebas sin necesidad de una base de datos real.

### Mantenibilidad
Toda la lógica SQL está centralizada en el DAO. Cambios en la estructura de la BD solo afectan a esta capa.

### Reutilización
El mismo DAO puede ser usado por múltiples partes de la aplicación sin duplicar código.

## 🚀 Cómo Ejecutar

### Prerrequisitos
- Java 17 o superior
- Maven 3.6 o superior
- MySQL 8.0 o superior corriendo en localhost:3306
- Base de datos llamada "University" creada

### Configuración de la Base de Datos

```sql
-- Crear la base de datos
CREATE DATABASE IF NOT EXISTS University;

-- Usar la base de datos
USE University;

-- La tabla empleados se creará automáticamente al ejecutar la aplicación
```

### Compilar el Proyecto

```bash
mvn clean compile
```

### Ejecutar la Aplicación Principal

```bash
mvn exec:java -Dexec.mainClass="com.example.EmpleadoApp"
```

### Ejecutar la Versión con Factory

```bash
mvn exec:java -Dexec.mainClass="com.example.EmpleadoAppConFactory"
```

## 📝 Modificar la Configuración

Si tus credenciales de MySQL son diferentes, edita `EmpleadoDAOImpl.java`:

```java
private static final String URL = "jdbc:mysql://localhost:3306/University?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
private static final String USER = "tu_usuario";
private static final String PASS = "tu_contraseña";
```

## 🔄 Comparación: Antes vs Después

### Antes (Sin DAO)
```java
public static void main(String[] args) {
    try (Connection conn = DriverManager.getConnection(url, user, pass)) {
        String sql = "INSERT INTO empleados(nombre, sueldo) VALUES(?, ?)";
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, "Ana");
            pst.setDouble(2, 85000.0);
            pst.executeUpdate();
        }
        // Más código JDBC mezclado con lógica de negocio...
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
```

**Problemas:**
- Lógica de negocio mezclada con código de BD
- Difícil de probar
- Código repetitivo
- Cambiar de BD requiere refactorizar todo

### Después (Con DAO)
```java
public static void main(String[] args) {
    EmpleadoDAO dao = new EmpleadoDAOImpl();
    
    Empleado ana = new Empleado("Ana", 85000.0);
    dao.insert(ana);
    
    List<Empleado> todos = dao.findAll();
    for (Empleado emp : todos) {
        System.out.println(emp);
    }
}
```

**Ventajas:**
- Código limpio y legible
- Fácil de mantener y probar
- Reutilizable
- Cambiar de BD solo afecta al DAO

## 🎓 Flujo de Ejecución

1. **Business Object** (EmpleadoApp) crea o obtiene referencia al DAO
2. **Business Object** solicita operación al DAO (insert, findAll, etc.)
3. **DAO** se conecta a la base de datos
4. **DAO** ejecuta la operación SQL correspondiente
5. **DAO** convierte ResultSet en Transfer Objects (Empleado)
6. **DAO** retorna los objetos al Business Object
7. **Business Object** trabaja con los objetos sin conocer detalles de BD

## 🔧 Extensiones Futuras

### Agregar más entidades
```java
// 1. Crear el Transfer Object
public class Departamento { /* ... */ }

// 2. Crear la interfaz DAO
public interface DepartamentoDAO { /* ... */ }

// 3. Implementar el DAO
public class DepartamentoDaoImpl implements DepartamentoDAO { /* ... */ }

// 4. Agregar al Factory
public abstract DepartamentoDAO getDepartamentoDAO();
```

### Implementar otra fuente de datos
```java
// Crear implementación para PostgreSQL
public class EmpleadoDAOPostgreSQLImpl implements EmpleadoDAO {
    // Implementar usando driver PostgreSQL
}

// Agregar al Factory
case POSTGRESQL:
    return new PostgreSQLDAOFactory();
```

### Connection Pool
Para producción, considera usar un pool de conexiones como HikariCP:

```xml
<dependency>
    <groupId>com.zaxxer</groupId>
    <artifactId>HikariCP</artifactId>
    <version>5.0.1</version>
</dependency>
```

## 📖 Referencias

- Patrón DAO: Módulo 3 - Lectura 4
- JDBC Fundamentals: Módulo 3 - Lecturas 2 y 3
- Abstract Factory Pattern: Módulo 2

## ⚠️ Notas Importantes

1. **Manejo de Excepciones**: En producción, considera crear excepciones personalizadas en lugar de lanzar RuntimeException genéricos.

2. **Transacciones**: Este ejemplo no implementa manejo de transacciones. Para operaciones que requieran atomicidad, deberías agregar soporte para transacciones.

3. **Seguridad**: Las credenciales están hardcodeadas para simplicidad didáctica. En producción, usa variables de entorno o archivos de configuración externos.

4. **Connection Pooling**: Para aplicaciones con muchas conexiones concurrentes, implementa un pool de conexiones.

5. **Logging**: Considera agregar un framework de logging (SLF4J, Log4j) en lugar de System.out.println.

## 📞 Preguntas Comunes

**P: ¿Por qué usar una interfaz si solo tengo una implementación?**
R: Porque facilita testing (puedes crear mocks), permite múltiples implementaciones futuras, y promueve programación orientada a interfaces.

**P: ¿Cuándo usar DAOFactory vs crear DAOs directamente?**
R: Usa Factory cuando necesites soportar múltiples fuentes de datos o cuando quieras configurar la implementación desde archivos de configuración.

**P: ¿Es necesario cerrar las conexiones si uso try-with-resources?**
R: No, try-with-resources cierra automáticamente los recursos que implementan AutoCloseable (Connection, Statement, ResultSet).

**P: ¿Qué pasa si mi tabla tiene muchas columnas?**
R: Considera usar un ORM (JPA/Hibernate) que automatiza el mapeo objeto-relacional para casos más complejos.
