package com.example;

import com.example.dao.EmpleadoDAO;
import com.example.dao.EmpleadoDAOImpl;
import com.example.model.Empleado;
import java.util.List;

/**
 * Aplicación principal que demuestra el uso del patrón DAO.
 * 
 * Observa cómo esta clase NO sabe nada sobre JDBC, SQL, conexiones,
 * PreparedStatements, ResultSets, etc. Simplemente trabaja con objetos
 * Empleado y llama métodos del DAO. Esta es la esencia del patrón DAO:
 * separación total entre la lógica de negocio y el acceso a datos.
 * 
 * Si mañana decidimos cambiar de MySQL a PostgreSQL, MongoDB, o archivos XML,
 * esta clase NO necesita cambiar. Solo crearíamos una nueva implementación
 * del DAO y la usaríamos aquí.
 */
public class EmpleadoApp {
    
    public static void main(String[] args) {
        
        System.out.println("=".repeat(60));
        System.out.println("DEMOSTRACIÓN DEL PATRÓN DAO");
        System.out.println("=".repeat(60));
        
        // Creamos una instancia del DAO
        // Nota: trabajamos con la interfaz EmpleadoDAO, no con la implementación
        // Esto es programación orientada a interfaces, otro principio de buen diseño
        EmpleadoDAO empleadoDAO = new EmpleadoDAOImpl();
        
        // 1. CREAR (INSERT) - Insertamos algunos empleados
        System.out.println("\n1. INSERTANDO EMPLEADOS...");
        System.out.println("-".repeat(60));
        
        Empleado ana = new Empleado("Ana García", 85000.0);
        Empleado luis = new Empleado("Luis Martínez", 92000.0);
        Empleado carmen = new Empleado("Carmen López", 78000.0);
        
        empleadoDAO.insert(ana);
        System.out.println("✓ Insertada: " + ana.getNombre());
        
        empleadoDAO.insert(luis);
        System.out.println("✓ Insertado: " + luis.getNombre());
        
        empleadoDAO.insert(carmen);
        System.out.println("✓ Insertada: " + carmen.getNombre());
        
        // 2. LEER TODOS (SELECT ALL)
        System.out.println("\n2. LISTANDO TODOS LOS EMPLEADOS...");
        System.out.println("-".repeat(60));
        
        List<Empleado> todos = empleadoDAO.findAll();
        System.out.println("Total de empleados: " + todos.size());
        System.out.println();
        
        for (Empleado emp : todos) {
            System.out.println(emp);
        }
        
        // 3. CONTAR
        System.out.println("\n3. CONTANDO EMPLEADOS...");
        System.out.println("-".repeat(60));
        int total = empleadoDAO.count();
        System.out.println("Número total de empleados en la BD: " + total);
        
        // 4. BUSCAR POR ID (SELECT BY ID)
        System.out.println("\n4. BUSCANDO UN EMPLEADO ESPECÍFICO...");
        System.out.println("-".repeat(60));
        
        // Suponiendo que el primer empleado tiene ID 1 (ajusta según tu BD)
        if (!todos.isEmpty()) {
            Integer primerID = todos.get(0).getId();
            Empleado encontrado = empleadoDAO.findById(primerID);
            
            if (encontrado != null) {
                System.out.println("Empleado encontrado con ID " + primerID + ":");
                System.out.println(encontrado);
            } else {
                System.out.println("No se encontró empleado con ID " + primerID);
            }
        }
        
        // 5. ACTUALIZAR (UPDATE)
        System.out.println("\n5. ACTUALIZANDO UN EMPLEADO...");
        System.out.println("-".repeat(60));
        
        if (!todos.isEmpty()) {
            Empleado paraActualizar = todos.get(0);
            System.out.println("Antes: " + paraActualizar);
            
            // Modificamos el sueldo
            Double nuevoSueldo = paraActualizar.getSueldo() * 1.10; // Aumento del 10%
            paraActualizar.setSueldo(nuevoSueldo);
            
            empleadoDAO.update(paraActualizar);
            System.out.println("Después: " + paraActualizar);
            
            // Verificamos que se actualizó en la BD
            Empleado verificacion = empleadoDAO.findById(paraActualizar.getId());
            System.out.println("Verificado en BD: " + verificacion);
        }
        
        // 6. ELIMINAR (DELETE)
        System.out.println("\n6. ELIMINANDO UN EMPLEADO...");
        System.out.println("-".repeat(60));
        
        if (todos.size() > 1) {
            Empleado paraEliminar = todos.get(todos.size() - 1); // Último empleado
            Integer idAEliminar = paraEliminar.getId();
            
            System.out.println("Eliminando: " + paraEliminar);
            empleadoDAO.delete(idAEliminar);
            System.out.println("✓ Empleado eliminado correctamente");
            
            // Verificamos que ya no existe
            Empleado verificacion = empleadoDAO.findById(idAEliminar);
            System.out.println("Verificación (debe ser null): " + verificacion);
        }
        
        // 7. ESTADO FINAL
        System.out.println("\n7. ESTADO FINAL DE LA BASE DE DATOS...");
        System.out.println("-".repeat(60));
        
        List<Empleado> estadoFinal = empleadoDAO.findAll();
        System.out.println("Empleados restantes: " + estadoFinal.size());
        System.out.println();
        
        for (Empleado emp : estadoFinal) {
            System.out.println(emp);
        }
        
        System.out.println("\n" + "=".repeat(60));
        System.out.println("DEMOSTRACIÓN COMPLETADA");
        System.out.println("=".repeat(60));
        
        // NOTA EDUCATIVA:
        // Observa cómo en todo este código nunca mencionamos:
        // - Connection, DriverManager, getConnection()
        // - PreparedStatement, Statement
        // - ResultSet
        // - SQL queries
        // - try-catch de SQLException
        // 
        // Todo eso está encapsulado en el DAO. Este código solo se preocupa
        // por la lógica de negocio: crear empleados, buscarlos, actualizarlos.
        // Esto hace que el código sea más fácil de leer, mantener y probar.
    }
}