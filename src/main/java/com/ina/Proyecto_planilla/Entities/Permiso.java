package com.ina.Proyecto_planilla.Entities;

import java.io.Serializable;
import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "Permisos")
public class Permiso implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_permiso;
    
    @ManyToOne
    @JoinColumn(name = "id_empleado", nullable = false)
    private Empleado empleado;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "La fecha de inicio no puede estar vacía")
    private LocalDate fecha_inicio;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "La fecha de fin no puede estar vacía")
    private LocalDate fecha_fin;
    
    private boolean con_goce_salario;
}