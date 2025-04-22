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
@Table(name = "Deducciones")
@Entity
public class Deduccion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_deduccion;
    
    @NotBlank(message = "El nombre de la deducción no puede estar vacío")
    @Column(length = 50)
    private String nombre_deduccion;
    
    private boolean porcentaje;
    
    @NotNull(message = "El valor de la deducción no puede estar vacío")
    private double valor_deduccion; // Puede ser un monto fijo o un porcentaje del salario base
    
    private int categoria;
    private boolean usa_fechas;
    private boolean activo; // Indica si la deducción está activa o no

    @OneToMany(mappedBy = "deduccion")
    private List<Detalle_deduccion> detalles_deduccion; // Relación con Detalle_deduccion
    
}
