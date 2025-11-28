package com.example;

import com.example.dao.EmpleadoDAO;
import com.example.dao.EmpleadoDAOImpl;
import com.example.model.Empleado;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 * Interfaz gráfica para gestión de empleados usando Swing.
 * Combina el patrón DAO con una GUI para crear un CRUD completo visual.
 */
public class EmpleadoGUI extends JFrame {
    
    // DAO para acceso a datos
    private EmpleadoDAO empleadoDAO;
    
    // Componentes de la interfaz
    private JTextField txtNombre;
    private JTextField txtSueldo;
    private JTextField txtId;
    private JTable tablaEmpleados;
    private DefaultTableModel modeloTabla;
    private JButton btnInsertar;
    private JButton btnActualizar;
    private JButton btnEliminar;
    private JButton btnLimpiar;
    private JButton btnRefrescar;
    private JLabel lblEstado;
    
    public EmpleadoGUI() {
        // Inicializar DAO
        empleadoDAO = new EmpleadoDAOImpl();
        
        // Configurar ventana principal
        setTitle("Sistema de Gestión de Empleados");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);
        
        // Crear componentes
        crearPanelFormulario();
        crearTabla();
        crearBotones();
        crearBarraEstado();
        
        // Cargar datos iniciales
        cargarDatosTabla();
        
        setVisible(true);
    }
    
    /**
     * Crea el panel de formulario para ingresar datos del empleado
     */
    private void crearPanelFormulario() {
        // Panel contenedor
        JPanel panelForm = new JPanel();
        panelForm.setBounds(20, 20, 350, 150);
        panelForm.setLayout(null);
        panelForm.setBorder(BorderFactory.createTitledBorder("Datos del Empleado"));
        
        // Label y TextField para ID
        JLabel lblId = new JLabel("ID:");
        lblId.setBounds(10, 30, 80, 25);
        panelForm.add(lblId);
        
        txtId = new JTextField();
        txtId.setBounds(100, 30, 230, 25);
        txtId.setEnabled(false); // ID es auto-generado
        panelForm.add(txtId);
        
        // Label y TextField para Nombre
        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setBounds(10, 65, 80, 25);
        panelForm.add(lblNombre);
        
        txtNombre = new JTextField();
        txtNombre.setBounds(100, 65, 230, 25);
        panelForm.add(txtNombre);
        
        // Label y TextField para Sueldo
        JLabel lblSueldo = new JLabel("Sueldo:");
        lblSueldo.setBounds(10, 100, 80, 25);
        panelForm.add(lblSueldo);
        
        txtSueldo = new JTextField();
        txtSueldo.setBounds(100, 100, 230, 25);
        panelForm.add(txtSueldo);
        
        add(panelForm);
    }
    
    /**
     * Crea la tabla para mostrar los empleados
     */
    private void crearTabla() {
        // Definir columnas
        String[] columnas = {"ID", "Nombre", "Sueldo"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Tabla no editable directamente
            }
        };
        
        tablaEmpleados = new JTable(modeloTabla);
        tablaEmpleados.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Listener para cuando se selecciona una fila
        tablaEmpleados.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int filaSeleccionada = tablaEmpleados.getSelectedRow();
                if (filaSeleccionada != -1) {
                    cargarEmpleadoSeleccionado(filaSeleccionada);
                }
            }
        });
        
        // ScrollPane para la tabla
        JScrollPane scrollPane = new JScrollPane(tablaEmpleados);
        scrollPane.setBounds(390, 20, 380, 400);
        add(scrollPane);
    }
    
    /**
     * Crea los botones de acción
     */
    private void crearBotones() {
        // Panel para botones
        JPanel panelBotones = new JPanel();
        panelBotones.setBounds(20, 180, 350, 240);
        panelBotones.setLayout(new GridLayout(5, 1, 10, 10));
        panelBotones.setBorder(BorderFactory.createTitledBorder("Acciones"));
        
        // Botón Insertar
        btnInsertar = new JButton("Insertar Nuevo Empleado");
        btnInsertar.setBackground(new Color(46, 204, 113));
        btnInsertar.setForeground(Color.WHITE);
        btnInsertar.setFocusPainted(false);
        btnInsertar.addActionListener(e -> insertarEmpleado());
        panelBotones.add(btnInsertar);
        
        // Botón Actualizar
        btnActualizar = new JButton("Actualizar Empleado");
        btnActualizar.setBackground(new Color(52, 152, 219));
        btnActualizar.setForeground(Color.WHITE);
        btnActualizar.setFocusPainted(false);
        btnActualizar.addActionListener(e -> actualizarEmpleado());
        panelBotones.add(btnActualizar);
        
        // Botón Eliminar
        btnEliminar = new JButton("Eliminar Empleado");
        btnEliminar.setBackground(new Color(231, 76, 60));
        btnEliminar.setForeground(Color.WHITE);
        btnEliminar.setFocusPainted(false);
        btnEliminar.addActionListener(e -> eliminarEmpleado());
        panelBotones.add(btnEliminar);
        
        // Botón Limpiar
        btnLimpiar = new JButton("Limpiar Formulario");
        btnLimpiar.setBackground(new Color(149, 165, 166));
        btnLimpiar.setForeground(Color.WHITE);
        btnLimpiar.setFocusPainted(false);
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        panelBotones.add(btnLimpiar);
        
        // Botón Refrescar
        btnRefrescar = new JButton("Refrescar Tabla");
        btnRefrescar.setBackground(new Color(155, 89, 182));
        btnRefrescar.setForeground(Color.WHITE);
        btnRefrescar.setFocusPainted(false);
        btnRefrescar.addActionListener(e -> cargarDatosTabla());
        panelBotones.add(btnRefrescar);
        
        add(panelBotones);
    }
    
    /**
     * Crea la barra de estado en la parte inferior
     */
    private void crearBarraEstado() {
        lblEstado = new JLabel("Listo");
        lblEstado.setBounds(20, 520, 750, 30);
        lblEstado.setBorder(BorderFactory.createEtchedBorder());
        lblEstado.setOpaque(true);
        lblEstado.setBackground(new Color(236, 240, 241));
        add(lblEstado);
    }
    
    /**
     * Carga todos los empleados en la tabla
     */
    private void cargarDatosTabla() {
        try {
            // Limpiar tabla
            modeloTabla.setRowCount(0);
            
            // Obtener empleados del DAO
            List<Empleado> empleados = empleadoDAO.findAll();
            
            // Agregar cada empleado a la tabla
            for (Empleado emp : empleados) {
                Object[] fila = {
                    emp.getId(),
                    emp.getNombre(),
                    String.format("$%.2f", emp.getSueldo())
                };
                modeloTabla.addRow(fila);
            }
            
            actualizarEstado("Tabla actualizada. Total de empleados: " + empleados.size());
            
        } catch (Exception e) {
            mostrarError("Error al cargar datos: " + e.getMessage());
        }
    }
    
    /**
     * Carga los datos del empleado seleccionado en el formulario
     */
    private void cargarEmpleadoSeleccionado(int fila) {
        try {
            Integer id = (Integer) modeloTabla.getValueAt(fila, 0);
            Empleado emp = empleadoDAO.findById(id);
            
            if (emp != null) {
                txtId.setText(emp.getId().toString());
                txtNombre.setText(emp.getNombre());
                txtSueldo.setText(emp.getSueldo().toString());
                actualizarEstado("Empleado cargado: " + emp.getNombre());
            }
            
        } catch (Exception e) {
            mostrarError("Error al cargar empleado: " + e.getMessage());
        }
    }
    
    /**
     * Inserta un nuevo empleado
     */
    private void insertarEmpleado() {
        try {
            // Validar campos
            if (!validarCampos()) {
                return;
            }
            
            // Crear nuevo empleado
            String nombre = txtNombre.getText().trim();
            Double sueldo = Double.parseDouble(txtSueldo.getText().trim());
            
            Empleado nuevoEmpleado = new Empleado(nombre, sueldo);
            
            // Insertar usando DAO
            empleadoDAO.insert(nuevoEmpleado);
            
            // Actualizar interfaz
            cargarDatosTabla();
            limpiarFormulario();
            actualizarEstado("Empleado insertado exitosamente: " + nombre);
            
            JOptionPane.showMessageDialog(this, 
                "Empleado insertado correctamente", 
                "Éxito", 
                JOptionPane.INFORMATION_MESSAGE);
            
        } catch (NumberFormatException e) {
            mostrarError("El sueldo debe ser un número válido");
        } catch (Exception e) {
            mostrarError("Error al insertar: " + e.getMessage());
        }
    }
    
    /**
     * Actualiza un empleado existente
     */
    private void actualizarEmpleado() {
        try {
            // Validar que haya un ID
            if (txtId.getText().trim().isEmpty()) {
                mostrarError("Seleccione un empleado de la tabla para actualizar");
                return;
            }
            
            // Validar campos
            if (!validarCampos()) {
                return;
            }
            
            // Crear empleado con datos actualizados
            Integer id = Integer.parseInt(txtId.getText().trim());
            String nombre = txtNombre.getText().trim();
            Double sueldo = Double.parseDouble(txtSueldo.getText().trim());
            
            Empleado empleado = new Empleado(id, nombre, sueldo);
            
            // Actualizar usando DAO
            empleadoDAO.update(empleado);
            
            // Actualizar interfaz
            cargarDatosTabla();
            limpiarFormulario();
            actualizarEstado("Empleado actualizado exitosamente: " + nombre);
            
            JOptionPane.showMessageDialog(this, 
                "Empleado actualizado correctamente", 
                "Éxito", 
                JOptionPane.INFORMATION_MESSAGE);
            
        } catch (NumberFormatException e) {
            mostrarError("Datos numéricos inválidos");
        } catch (Exception e) {
            mostrarError("Error al actualizar: " + e.getMessage());
        }
    }
    
    /**
     * Elimina un empleado
     */
    private void eliminarEmpleado() {
        try {
            // Validar que haya un ID
            if (txtId.getText().trim().isEmpty()) {
                mostrarError("Seleccione un empleado de la tabla para eliminar");
                return;
            }
            
            Integer id = Integer.parseInt(txtId.getText().trim());
            String nombre = txtNombre.getText().trim();
            
            // Confirmar eliminación
            int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de eliminar al empleado: " + nombre + "?",
                "Confirmar Eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
            
            if (confirmacion == JOptionPane.YES_OPTION) {
                // Eliminar usando DAO
                empleadoDAO.delete(id);
                
                // Actualizar interfaz
                cargarDatosTabla();
                limpiarFormulario();
                actualizarEstado("Empleado eliminado: " + nombre);
                
                JOptionPane.showMessageDialog(this,
                    "Empleado eliminado correctamente",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
            }
            
        } catch (NumberFormatException e) {
            mostrarError("ID inválido");
        } catch (Exception e) {
            mostrarError("Error al eliminar: " + e.getMessage());
        }
    }
    
    /**
     * Limpia todos los campos del formulario
     */
    private void limpiarFormulario() {
        txtId.setText("");
        txtNombre.setText("");
        txtSueldo.setText("");
        tablaEmpleados.clearSelection();
        actualizarEstado("Formulario limpiado");
    }
    
    /**
     * Valida que los campos requeridos estén completos
     */
    private boolean validarCampos() {
        if (txtNombre.getText().trim().isEmpty()) {
            mostrarError("El nombre es obligatorio");
            txtNombre.requestFocus();
            return false;
        }
        
        if (txtSueldo.getText().trim().isEmpty()) {
            mostrarError("El sueldo es obligatorio");
            txtSueldo.requestFocus();
            return false;
        }
        
        try {
            Double sueldo = Double.parseDouble(txtSueldo.getText().trim());
            if (sueldo < 0) {
                mostrarError("El sueldo no puede ser negativo");
                txtSueldo.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            mostrarError("El sueldo debe ser un número válido");
            txtSueldo.requestFocus();
            return false;
        }
        
        return true;
    }
    
    /**
     * Actualiza el mensaje de la barra de estado
     */
    private void actualizarEstado(String mensaje) {
        lblEstado.setText(mensaje);
    }
    
    /**
     * Muestra un mensaje de error
     */
    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this,
            mensaje,
            "Error",
            JOptionPane.ERROR_MESSAGE);
        actualizarEstado("Error: " + mensaje);
    }
    
    /**
     * Método main para ejecutar la aplicación
     */
    public static void main(String[] args) {
        // Ejecutar en el Event Dispatch Thread
        SwingUtilities.invokeLater(() -> new EmpleadoGUI());
    }
}