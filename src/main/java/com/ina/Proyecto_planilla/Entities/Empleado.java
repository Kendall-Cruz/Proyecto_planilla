package com.ina.Proyecto_planilla.Entities;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "Empleados")
public class Empleado implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_empleado;
    
    @NotBlank(message = "La cédula no puede estar vacía")
    @Column(length = 10)
    private String cedula;
    
    @NotBlank(message = "El nombre no puede estar vacío")
    @Column(length = 30)
    private String nombre;
    
    @NotBlank(message = "El primer apellido no puede estar vacío")
    @Column(length = 30)
    private String apellido1;
    
    @Column(length = 30)
    private String apellido2;
    
    @NotBlank(message = "El teléfono no puede estar vacío")
    @Column(length = 8)
    private String telefono;
    
    @NotBlank(message = "El correo no puede estar vacío")
    @Column(length = 50)
    private String correo;
    
    @NotNull(message = "Los puntos de carrera no pueden estar vacíos")
    private int puntos_carrera;
    
    @OneToMany(mappedBy = "empleado")
    private List<Puesto_empleado> puestos_empleado;
    
    @OneToMany(mappedBy = "empleado")
    private List<Pension> pensiones;
    
    @OneToMany(mappedBy = "empleado")
    private List<Permiso> permisos;
    
    @OneToMany(mappedBy = "empleado")
    private List<Incapacidad> incapacidades;
    
    @OneToMany(mappedBy = "empleado")
    private List<Titulo_academico> titulos_academicos;
    
    @OneToMany(mappedBy = "empleado")
    private List<Detalle_planilla> detalles_planilla;
}
