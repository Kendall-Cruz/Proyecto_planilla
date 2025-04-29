package com.ina.Proyecto_planilla.Entities;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Table(name = "Pagos")
@Entity
public class Pago implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_pago;

    @NotBlank(message = "El nombre del pago no puede estar vacío")
    @Size(max = 50, message = "El nombre del pago no puede exceder los 50 caracteres")
    private String nombre_pago;

    private boolean porcentaje;

    @NotNull(message = "El valor del pago no puede estar vacío")
    private double valor_pago; // Puede ser un monto fijo o un porcentaje del salario base

    private int categoria;

    private boolean usa_fechas;

    private boolean activo; // Indica si el pago está activo o no

    
    @OneToMany(mappedBy = "pago")
    private List<Detalle_pago> detalle_pagos; // Relación con Detalle_pago

}
