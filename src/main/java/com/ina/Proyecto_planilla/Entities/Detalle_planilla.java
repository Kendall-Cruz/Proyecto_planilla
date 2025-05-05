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

    // Constructor por defecto
    public Detalle_planilla() {
        this.salario_bruto = 0.0;
        this.deducciones = 0.0;
        this.salario_neto = 0.0;
        this.adelanto_quincenal = 0.0;  
        this.salario_mensual = 0.0;
        this.retroactivo = 0.0;
        this.monto_porcentaje_renta = 0.0;
        this.monto_pensiones = 0.0;
        this.monto_subsidio = 0.0;
        this.monto_puntos_carrera = 0.0;
        this.pagos = 0.0;
        this.salario_base = 0.0;
    }

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
        this.monto_pensiones = 0.0;
        this.monto_subsidio = 0.0;
        this.monto_puntos_carrera = 0.0;
        this.pagos = 0.0;
        this.salario_base = 0.0;
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

    @Getter @Setter
    private double salario_base; // Salario base del empleado para la planilla

    @Getter @Setter
    private double salario_proporcional;

    @NotNull
    @Getter @Setter
    private double salario_bruto;

    @NotNull
    @Getter @Setter
    private double deducciones;

    @NotNull
    @Getter @Setter
    private double salario_neto;

    @NotNull
    @Getter @Setter
    private double adelanto_quincenal;

    @NotNull
    @Getter @Setter
    private double salario_mensual;

    @NotNull
    @Getter @Setter
    private double retroactivo;

    @NotNull
    @Getter @Setter
    private double monto_porcentaje_renta;

    @NotNull
    @Getter @Setter
    private double monto_pensiones;

    @NotNull
    @Getter @Setter
    private double monto_subsidio;

    @Getter @Setter
    private double monto_puntos_carrera;

    @Getter @Setter
    private double pagos;

    @Getter @Setter
    private int dias_incapacidad; 

    @Getter @Setter
    private int dias_permiso_sin_goce;
    

    @OneToMany(mappedBy = "detalle_planilla")
    @Getter @Setter
    private List<Detalle_deduccion> detalles_deducciones; // Relación con Detalle_deduccion

    @OneToMany(mappedBy = "detalle_planilla")
    @Getter @Setter
    private List<Detalle_pago> detalle_pagos; // Relación con Detalle_deduccion

}
