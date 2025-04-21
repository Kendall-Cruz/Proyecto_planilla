package com.ina.Proyecto_planilla.Entities;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "Detalles_planilla")
public class Detalle_planilla implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_detalle_planilla;

    @ManyToOne
    @JoinColumn(name = "id_planilla", nullable = false)
    private Planilla planilla; // Relación con Planilla

    @ManyToOne
    @JoinColumn(name = "id_empleado", nullable = false)
    private Empleado empleado; // Relación con Empleado

    @NotNull(message = "El salario bruto no puede estar vacío")
    private double salario_bruto;

    @NotNull(message = "Las deducciones no pueden estar vacías")
    private double deducciones;

    @NotNull(message = "El salario neto no puede estar vacío")
    private double salario_neto;

    @NotNull(message = "El adelanto quincenal no puede estar vacío")
    private double adelanto_quincenal;

    @NotNull(message = "El salario mensual no puede estar vacío")
    private double salario_mensual;

    @NotNull(message = "El retroactivo no puede estar vacío")
    private double retroactivo;

    @NotNull(message = "El monto porcentaje renta no puede estar vacío")
    private double monto_porcentaje_renta;

    @OneToMany(mappedBy = "detalle_planilla")
    private List<Detalle_deduccion> detalles_deducciones; // Relación con Detalle_deduccion

    @OneToMany(mappedBy = "detalle_planilla")
    private List<Detalle_pago> detalle_pagos; // Relación con Detalle_deduccion

}
