package com.example.model;

/**
 * Transfer Object (DTO) que representa un empleado.
 * 
 * Este objeto es "plano" - solo contiene datos, sin lógica de negocio compleja.
 * Es el puente entre la base de datos y la aplicación. Cuando el DAO recupera
 * datos de la BD, los empaqueta en objetos Empleado. Cuando necesitamos guardar
 * datos, pasamos objetos Empleado al DAO.
 * 
 * Nota: Este patrón se llama "Transfer Object" o "Data Transfer Object (DTO)"
 * porque su único propósito es transferir datos entre capas.
 */
public class Empleado {
    
    // Atributos que corresponden a las columnas de la tabla
    private Integer id;
    private String nombre;
    private Double sueldo;
    
    /**
     * Constructor vacío necesario para crear instancias sin datos iniciales.
     * Útil cuando queremos crear un empleado y luego ir asignando valores.
     */
    public Empleado() {
    }
    
    /**
     * Constructor completo para crear un empleado con todos sus datos.
     * Lo usaremos principalmente al recuperar datos de la BD.
     */
    public Empleado(Integer id, String nombre, Double sueldo) {
        this.id = id;
        this.nombre = nombre;
        this.sueldo = sueldo;
    }
    
    /**
     * Constructor sin ID para crear nuevos empleados.
     * Cuando insertamos un nuevo empleado, el ID lo genera automáticamente
     * la base de datos (AUTO_INCREMENT), así que no lo necesitamos aquí.
     */
    public Empleado(String nombre, Double sueldo) {
        this.nombre = nombre;
        this.sueldo = sueldo;
    }
    
    // Getters y Setters
    // Estos métodos son esenciales porque encapsulan el acceso a los atributos.
    // Siguiendo las buenas prácticas de orientación a objetos, los atributos
    // son privados y solo se accede a ellos mediante estos métodos públicos.
    
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public Double getSueldo() {
        return sueldo;
    }
    
    public void setSueldo(Double sueldo) {
        this.sueldo = sueldo;
    }
    
    /**
     * Sobrescribimos toString() para poder imprimir fácilmente
     * la información del empleado. Muy útil para debugging y logs.
     */
    @Override
    public String toString() {
        return String.format("Empleado[id=%d, nombre='%s', sueldo=%.2f]", 
                           id, nombre, sueldo);
    }
}