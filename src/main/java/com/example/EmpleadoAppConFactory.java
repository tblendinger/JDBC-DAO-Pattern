package com.example;

import com.example.dao.DAOFactory;
import com.example.dao.EmpleadoDAO;
import com.example.model.Empleado;
import java.util.List;

/**
 * Versión alternativa de la aplicación que usa el patrón Abstract Factory.
 * 
 * Esta aproximación es más escalable y flexible que crear directamente
 * las instancias de los DAOs. Es especialmente útil cuando:
 * 
 * 1. Tu aplicación debe soportar múltiples bases de datos
 * 2. Quieres configurar la fuente de datos desde un archivo
 * 3. Necesitas cambiar entre implementaciones según el entorno (dev/prod/test)
 * 4. Tienes múltiples DAOs (empleado, departamento, proyecto, etc.)
 */
public class EmpleadoAppConFactory {
    
    public static void main(String[] args) {
        
        System.out.println("=".repeat(60));
        System.out.println("DEMOSTRACIÓN DEL PATRÓN DAO + ABSTRACT FACTORY");
        System.out.println("=".repeat(60));
        
        // En lugar de crear directamente el DAO como:
        // EmpleadoDAO empleadoDAO = new EmpleadoDAOImpl();
        
        // Usamos la factory para obtenerlo:
        DAOFactory factory = DAOFactory.getInstance(DAOFactory.MYSQL);
        EmpleadoDAO empleadoDAO = factory.getEmpleadoDAO();
        
        // VENTAJA: Si mañana quieres usar PostgreSQL, solo cambias esta línea:
        // DAOFactory factory = DAOFactory.getInstance(DAOFactory.POSTGRESQL);
        // Y todo el resto del código sigue funcionando sin modificaciones.
        
        // VENTAJA 2: En una aplicación real, este valor vendría de configuración:
        // int dbType = Configuration.getDatabaseType(); // Lee de config.properties
        // DAOFactory factory = DAOFactory.getInstance(dbType);
        // Ahora cambiar de BD es tan simple como editar un archivo de configuración.
        
        System.out.println("\nUsando factory de tipo: MySQL");
        System.out.println("Implementación obtenida: " + empleadoDAO.getClass().getSimpleName());
        
        // Operaciones de demostración
        System.out.println("\n" + "-".repeat(60));
        
        // Insertamos un empleado de ejemplo
        Empleado nuevo = new Empleado("Pedro Sánchez", 88000.0);
        empleadoDAO.insert(nuevo);
        System.out.println("✓ Empleado insertado: " + nuevo.getNombre());
        
        // Obtenemos todos los empleados
        List<Empleado> todos = empleadoDAO.findAll();
        System.out.println("\nTotal de empleados en la BD: " + todos.size());
        
        System.out.println("\nListado completo:");
        for (Empleado emp : todos) {
            System.out.println("  • " + emp);
        }
        
        System.out.println("\n" + "=".repeat(60));
        System.out.println("VENTAJAS DE USAR ABSTRACT FACTORY:");
        System.out.println("=".repeat(60));
        
        System.out.println("""
            
            1. FLEXIBILIDAD: Puedes cambiar de MySQL a PostgreSQL, XML, o cualquier
               otra fuente de datos modificando una sola línea de código.
            
            2. CONFIGURABILIDAD: El tipo de BD puede leerse desde un archivo de
               configuración, permitiendo diferentes configuraciones para desarrollo,
               testing y producción sin cambiar código.
            
            3. ESCALABILIDAD: Cuando agregues más entidades (Departamento, Proyecto),
               la factory puede crear todos los DAOs necesarios de la misma familia.
               
               Ejemplo:
               DAOFactory factory = DAOFactory.getInstance(DAOFactory.MYSQL);
               EmpleadoDAO empDAO = factory.getEmpleadoDAO();
               DepartamentoDAO depDAO = factory.getDepartamentoDAO();
               ProyectoDAO proyDAO = factory.getProyectoDAO();
               
               Todos estos DAOs trabajarán con la misma fuente de datos (MySQL)
               de forma consistente.
            
            4. TESTING: Puedes crear una implementación en memoria para tests:
               DAOFactory factory = DAOFactory.getInstance(DAOFactory.MEMORY);
               // Ahora todos tus tests usan datos en memoria, sin BD real
            
            5. MÚLTIPLES FUENTES: Podrías tener usuarios en MySQL y logs en XML:
               DAOFactory mysqlFactory = DAOFactory.getInstance(DAOFactory.MYSQL);
               DAOFactory xmlFactory = DAOFactory.getInstance(DAOFactory.XML);
               
               EmpleadoDAO empDAO = mysqlFactory.getEmpleadoDAO();
               AuditoriaDAO audDAO = xmlFactory.getAuditoriaDAO();
            """);
        
        System.out.println("=".repeat(60));
        System.out.println("COMPARACIÓN DE ENFOQUES:");
        System.out.println("=".repeat(60));
        
        System.out.println("""
            
            SIN PATRÓN DAO (tu código original):
            ➤ Lógica de negocio mezclada con SQL
            ➤ Difícil de probar sin una BD real
            ➤ Cambiar de BD requiere refactorizar todo
            ➤ Código repetitivo en múltiples lugares
            
            CON DAO SIMPLE:
            ➤ Lógica de acceso a datos encapsulada
            ➤ Código de negocio limpio y legible
            ➤ Más fácil de mantener y probar
            ➤ Pero: la aplicación conoce la implementación específica
            
            CON DAO + ABSTRACT FACTORY:
            ➤ Todo lo anterior +
            ➤ La aplicación no conoce la implementación específica
            ➤ Cambiar de BD es trivial (una línea o un config)
            ➤ Soporte natural para múltiples fuentes de datos
            ➤ Perfecto para aplicaciones grandes y evolutivas
            """);
        
        System.out.println("=".repeat(60));
    }
}