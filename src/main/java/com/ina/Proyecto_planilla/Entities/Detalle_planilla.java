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
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Detalles_planilla")
public class Detalle_planilla implements Serializable {

    private static final long serialVersionUID = 1L;

    public Detalle_planilla(Empleado empleado, Planilla planilla) {
        this.empleado = empleado;
        this.planilla = planilla;
        this.salario_bruto = 0.0;
        this.deducciones = 0.0;
        this.salario_neto = 0.0;
        this.adelanto_quincenal = 0.0;  
        this.salario_mensual = 0.0;
        this.retroactivo = 0.0;
        this.monto_porcentaje_renta = 0.0;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter @Setter
    private Long id_detalle_planilla;

    @ManyToOne
    @JoinColumn(name = "id_planilla", nullable = false)
    @Getter @Setter
    private Planilla planilla; // Relación con Planilla

    @ManyToOne
    @JoinColumn(name = "id_empleado", nullable = false)
    @Getter @Setter
    private Empleado empleado; // Relación con Empleado

    @NotNull(message = "El salario bruto no puede estar vacío")
    @Getter @Setter
    private double salario_bruto;

    @NotNull(message = "Las deducciones no pueden estar vacías")
    @Getter @Setter
    private double deducciones;

    @NotNull(message = "El salario neto no puede estar vacío")
    @Getter @Setter
    private double salario_neto;

    @NotNull(message = "El adelanto quincenal no puede estar vacío")
    @Getter @Setter
    private double adelanto_quincenal;

    @NotNull(message = "El salario mensual no puede estar vacío")
    @Getter @Setter
    private double salario_mensual;

    @NotNull(message = "El retroactivo no puede estar vacío")
    @Getter @Setter
    private double retroactivo;

    @NotNull(message = "El monto porcentaje renta no puede estar vacío")
    @Getter @Setter
    private double monto_porcentaje_renta;

    @OneToMany(mappedBy = "detalle_planilla")
    @Getter @Setter
    private List<Detalle_deduccion> detalles_deducciones; // Relación con Detalle_deduccion

    @OneToMany(mappedBy = "detalle_planilla")
    @Getter @Setter
    private List<Detalle_pago> detalle_pagos; // Relación con Detalle_deduccion

}
