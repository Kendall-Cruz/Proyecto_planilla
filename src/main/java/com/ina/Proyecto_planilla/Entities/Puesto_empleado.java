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
@Table(name = "Puestos_empleado")
public class Puesto_empleado implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_puesto_empleado;
    
    @ManyToOne
    @JoinColumn(name = "id_empleado", nullable = false)
    private Empleado empleado;
    
    @ManyToOne
    @JoinColumn(name = "id_puesto", nullable = false)
    private Puesto puesto;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "La fecha de nombramiento no puede estar vacía")
    private LocalDate fecha_nombramiento;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "La fecha de vencimiento no puede estar vacía")
    private LocalDate fecha_vence;
    
    private boolean borrado;
}