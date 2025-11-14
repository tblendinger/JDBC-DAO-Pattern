package com.example.dao;

import com.example.model.Empleado;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación del DAO para MySQL.
 * 
 * Esta clase contiene TODA la lógica de acceso a datos. Ninguna otra parte
 * de la aplicación necesita saber que estamos usando MySQL, JDBC, o SQL.
 * Todo está encapsulado aquí.
 * 
 * Ventajas de este diseño:
 * - Si cambiamos de MySQL a PostgreSQL, solo modificamos esta clase
 * - El código de negocio permanece limpio, sin mezclar SQL
 * - Podemos crear otra implementación (ej: EmpleadoDAOXmlImpl) sin tocar nada más
 * - Facilita el testing con implementaciones mock
 */
public class EmpleadoDAOImpl implements EmpleadoDAO {
    
    // Configuración de conexión como constantes
    // En una aplicación real, esto vendría de un archivo de configuración
    private static final String URL = "jdbc:mysql://localhost:3306/University?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASS = "clave123";
    
    // Queries SQL como constantes para facilitar mantenimiento
    private static final String CREATE_TABLE = 
        "CREATE TABLE IF NOT EXISTS empleados (" +
        "id INT AUTO_INCREMENT PRIMARY KEY, " +
        "nombre VARCHAR(100) NOT NULL, " +
        "sueldo DOUBLE)";
    
    private static final String INSERT = 
        "INSERT INTO empleados(nombre, sueldo) VALUES(?, ?)";
    
    private static final String FIND_BY_ID = 
        "SELECT id, nombre, sueldo FROM empleados WHERE id = ?";
    
    private static final String FIND_ALL = 
        "SELECT id, nombre, sueldo FROM empleados ORDER BY id";
    
    private static final String UPDATE = 
        "UPDATE empleados SET nombre = ?, sueldo = ? WHERE id = ?";
    
    private static final String DELETE = 
        "DELETE FROM empleados WHERE id = ?";
    
    private static final String COUNT = 
        "SELECT COUNT(*) FROM empleados";
    
    /**
     * Constructor que inicializa la tabla si no existe.
     * Este es un buen lugar para hacer la configuración inicial
     * que necesita el DAO para funcionar correctamente.
     */
    public EmpleadoDAOImpl() {
        initializeTable();
    }
    
    /**
     * Método privado que crea la tabla si no existe.
     * Esto asegura que la BD esté lista para usar cuando se crea el DAO.
     */
    private void initializeTable() {
        try (Connection conn = getConnection();
             Statement st = conn.createStatement()) {
            st.execute(CREATE_TABLE);
        } catch (SQLException e) {
            throw new RuntimeException("Error al inicializar la tabla empleados", e);
        }
    }
    
    /**
     * Método auxiliar para obtener una conexión.
     * Centralizar esto en un método facilita cambios futuros
     * (por ejemplo, usar un pool de conexiones).
     */
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
    
    @Override
    public void insert(Empleado empleado) {
        // Validación básica antes de intentar insertar
        if (empleado == null) {
            throw new IllegalArgumentException("El empleado no puede ser null");
        }
        if (empleado.getNombre() == null || empleado.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del empleado es obligatorio");
        }
        
        // Try-with-resources asegura que la conexión y el statement se cierren
        try (Connection conn = getConnection();
             PreparedStatement pst = conn.prepareStatement(INSERT)) {
            
            // Asignamos los parámetros del PreparedStatement
            // Nota: los índices comienzan en 1, no en 0
            pst.setString(1, empleado.getNombre());
            pst.setDouble(2, empleado.getSueldo());
            
            // executeUpdate() retorna el número de filas afectadas
            int filasAfectadas = pst.executeUpdate();
            
            if (filasAfectadas == 0) {
                throw new SQLException("La inserción falló, ninguna fila fue afectada");
            }
            
        } catch (SQLException e) {
            // Convertimos SQLException en RuntimeException para simplificar el manejo
            // En una aplicación real, podrías crear excepciones personalizadas más específicas
            throw new RuntimeException("Error al insertar empleado: " + empleado, e);
        }
    }
    
    @Override
    public Empleado findById(Integer id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID debe ser un número positivo");
        }
        
        Empleado empleado = null;
        
        try (Connection conn = getConnection();
             PreparedStatement pst = conn.prepareStatement(FIND_BY_ID)) {
            
            pst.setInt(1, id);
            
            // executeQuery() se usa para SELECT, retorna un ResultSet
            try (ResultSet rs = pst.executeQuery()) {
                // next() avanza al siguiente registro y retorna true si existe
                if (rs.next()) {
                    // Convertimos la fila del ResultSet en un objeto Empleado
                    empleado = mapResultSetToEmpleado(rs);
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar empleado con ID: " + id, e);
        }
        
        return empleado; // Retorna null si no se encontró
    }
    
    @Override
    public List<Empleado> findAll() {
        List<Empleado> empleados = new ArrayList<>();
        
        try (Connection conn = getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(FIND_ALL)) {
            
            // Iteramos sobre todas las filas del ResultSet
            while (rs.next()) {
                empleados.add(mapResultSetToEmpleado(rs));
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener todos los empleados", e);
        }
        
        return empleados;
    }
    
    @Override
    public void update(Empleado empleado) {
        if (empleado == null) {
            throw new IllegalArgumentException("El empleado no puede ser null");
        }
        if (empleado.getId() == null) {
            throw new IllegalArgumentException("El empleado debe tener un ID para actualizarse");
        }
        if (empleado.getNombre() == null || empleado.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del empleado es obligatorio");
        }
        
        try (Connection conn = getConnection();
             PreparedStatement pst = conn.prepareStatement(UPDATE)) {
            
            // Orden importante: primero los valores a actualizar, luego el WHERE
            pst.setString(1, empleado.getNombre());
            pst.setDouble(2, empleado.getSueldo());
            pst.setInt(3, empleado.getId());
            
            int filasAfectadas = pst.executeUpdate();
            
            if (filasAfectadas == 0) {
                throw new SQLException("No se encontró empleado con ID: " + empleado.getId());
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar empleado: " + empleado, e);
        }
    }
    
    @Override
    public void delete(Integer id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID debe ser un número positivo");
        }
        
        try (Connection conn = getConnection();
             PreparedStatement pst = conn.prepareStatement(DELETE)) {
            
            pst.setInt(1, id);
            
            int filasAfectadas = pst.executeUpdate();
            
            if (filasAfectadas == 0) {
                throw new SQLException("No se encontró empleado con ID: " + id);
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar empleado con ID: " + id, e);
        }
    }
    
    @Override
    public int count() {
        try (Connection conn = getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(COUNT)) {
            
            if (rs.next()) {
                // COUNT(*) siempre retorna un valor en la primera columna
                return rs.getInt(1);
            }
            
            return 0;
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al contar empleados", e);
        }
    }
    
    /**
     * Método auxiliar privado que convierte una fila del ResultSet
     * en un objeto Empleado. Esto evita duplicar código y hace que
     * sea fácil cambiar cómo se mapean los datos.
     * 
     * @param rs ResultSet posicionado en una fila válida
     * @return Un objeto Empleado con los datos de esa fila
     * @throws SQLException si hay error al leer los datos
     */
    private Empleado mapResultSetToEmpleado(ResultSet rs) throws SQLException {
        return new Empleado(
            rs.getInt("id"),
            rs.getString("nombre"),
            rs.getDouble("sueldo")
        );
    }
}