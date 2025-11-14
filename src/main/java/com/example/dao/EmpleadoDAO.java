package com.example.dao;

import com.example.model.Empleado;
import java.util.List;

/**
 * Interfaz que define las operaciones de acceso a datos para Empleado.
 * 
 * Esta interfaz es el corazón del patrón DAO. Define QUÉ operaciones
 * podemos realizar con empleados, pero no CÓMO se realizan. Esto permite:
 * 
 * 1. Cambiar la implementación sin afectar al código que usa el DAO
 *    (por ejemplo, cambiar de MySQL a PostgreSQL, o incluso a archivos XML)
 * 
 * 2. Crear múltiples implementaciones para diferentes fuentes de datos
 *    (una para SQL, otra para archivos, otra para un webservice, etc.)
 * 
 * 3. Facilitar las pruebas creando implementaciones "mock" para testing
 * 
 * Seguimos el patrón CRUD: Create, Read, Update, Delete
 */
public interface EmpleadoDAO {
    
    /**
     * Inserta un nuevo empleado en la base de datos.
     * 
     * @param empleado El empleado a insertar (sin ID, será generado por la BD)
     * @throws RuntimeException si ocurre un error durante la inserción
     */
    void insert(Empleado empleado);
    
    /**
     * Busca un empleado por su ID.
     * 
     * @param id El identificador único del empleado
     * @return El empleado encontrado, o null si no existe
     * @throws RuntimeException si ocurre un error durante la búsqueda
     */
    Empleado findById(Integer id);
    
    /**
     * Obtiene todos los empleados de la base de datos.
     * 
     * @return Lista con todos los empleados (lista vacía si no hay ninguno)
     * @throws RuntimeException si ocurre un error durante la consulta
     */
    List<Empleado> findAll();
    
    /**
     * Actualiza los datos de un empleado existente.
     * 
     * @param empleado El empleado con los datos actualizados (debe tener ID)
     * @throws RuntimeException si ocurre un error durante la actualización
     */
    void update(Empleado empleado);
    
    /**
     * Elimina un empleado de la base de datos.
     * 
     * @param id El identificador del empleado a eliminar
     * @throws RuntimeException si ocurre un error durante la eliminación
     */
    void delete(Integer id);
    
    /**
     * Cuenta el número total de empleados en la base de datos.
     * 
     * @return El número de empleados
     * @throws RuntimeException si ocurre un error durante el conteo
     */
    int count();
}