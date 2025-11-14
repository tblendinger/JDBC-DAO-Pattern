package com.example.dao;

/**
 * Factory para crear diferentes implementaciones de DAOs.
 * 
 * Este es el patrón Abstract Factory aplicado a DAOs, como describe
 * el módulo 3. Permite tener múltiples "familias" de implementaciones
 * (MySQL, PostgreSQL, XML, etc.) y cambiar entre ellas fácilmente.
 * 
 * Ventajas de usar este patrón:
 * 1. El código cliente no necesita saber qué implementación específica usa
 * 2. Podemos cambiar la fuente de datos en un solo lugar
 * 3. Facilita tener diferentes configuraciones (desarrollo, producción, testing)
 * 4. Permite tener múltiples fuentes de datos activas simultáneamente
 * 
 * Ejemplo de uso:
 *   DAOFactory factory = DAOFactory.getInstance(DAOFactory.MYSQL);
 *   EmpleadoDAO dao = factory.getEmpleadoDAO();
 */
public abstract class DAOFactory {
    
    // Constantes que identifican los diferentes tipos de factory
    public static final int MYSQL = 1;
    public static final int POSTGRESQL = 2;
    public static final int XML = 3;
    public static final int MEMORY = 4; // Para testing
    
    /**
     * Método abstracto que cada factory concreta debe implementar.
     * Retorna la implementación específica del EmpleadoDAO.
     */
    public abstract EmpleadoDAO getEmpleadoDAO();
    
    // Si tuviéramos más entidades, agregaríamos más métodos:
    // public abstract DepartamentoDAO getDepartamentoDAO();
    // public abstract ProyectoDAO getProyectoDAO();
    
    /**
     * Método estático que retorna la factory apropiada según el tipo.
     * Este es el punto de entrada principal del patrón.
     * 
     * @param tipo El tipo de factory deseado (usa las constantes de esta clase)
     * @return Una instancia de la factory concreta correspondiente
     */
    public static DAOFactory getInstance(int tipo) {
        switch (tipo) {
            case MYSQL:
                return new MySQLDAOFactory();
            case POSTGRESQL:
                // return new PostgreSQLDAOFactory(); // Implementación futura
                throw new UnsupportedOperationException("PostgreSQL no implementado aún");
            case XML:
                // return new XMLDAOFactory(); // Implementación futura
                throw new UnsupportedOperationException("XML no implementado aún");
            case MEMORY:
                // return new InMemoryDAOFactory(); // Para testing
                throw new UnsupportedOperationException("InMemory no implementado aún");
            default:
                throw new IllegalArgumentException("Tipo de factory desconocido: " + tipo);
        }
    }
}

/**
 * Factory concreta para MySQL.
 * Esta clase conoce cómo crear las implementaciones específicas para MySQL.
 */
class MySQLDAOFactory extends DAOFactory {
    
    @Override
    public EmpleadoDAO getEmpleadoDAO() {
        // Retorna la implementación MySQL del DAO
        return new EmpleadoDAOImpl();
    }
    
    // Si tuviéramos más DAOs:
    // @Override
    // public DepartamentoDAO getDepartamentoDAO() {
    //     return new DepartamentoDAOMySQLImpl();
    // }
}

/**
 * EJEMPLO DE CÓMO USARÍAS ESTE PATRÓN:
 * 
 * // En lugar de crear directamente el DAO:
 * // EmpleadoDAO dao = new EmpleadoDAOImpl();
 * 
 * // Usas la factory:
 * DAOFactory factory = DAOFactory.getInstance(DAOFactory.MYSQL);
 * EmpleadoDAO dao = factory.getEmpleadoDAO();
 * 
 * // Ahora tu aplicación no sabe que está usando MySQL.
 * // Para cambiar a PostgreSQL en el futuro, solo cambias:
 * // DAOFactory factory = DAOFactory.getInstance(DAOFactory.POSTGRESQL);
 * 
 * // Y todo el resto del código sigue funcionando sin cambios.
 * 
 * 
 * EJEMPLO CON ARCHIVO DE CONFIGURACIÓN:
 * 
 * // Podrías leer el tipo de BD desde un archivo de configuración:
 * Properties config = new Properties();
 * config.load(new FileInputStream("config.properties"));
 * int dbType = Integer.parseInt(config.getProperty("database.type"));
 * 
 * DAOFactory factory = DAOFactory.getInstance(dbType);
 * EmpleadoDAO dao = factory.getEmpleadoDAO();
 * 
 * // Ahora cambiar de base de datos es tan simple como editar config.properties
 */
