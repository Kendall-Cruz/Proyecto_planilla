package com.ina.Proyecto_planilla.Entities;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "Detalles_deduccion")
public class Detalle_deduccion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_detalle_deduccion;
    @ManyToOne
    @JoinColumn(name = "id_deduccion", nullable = false)
    private Deduccion deduccion;
    @ManyToOne
    @JoinColumn(name = "id_detalle_planilla", nullable = false)
    private Detalle_planilla detalle_planilla;
    private double monto;
}
