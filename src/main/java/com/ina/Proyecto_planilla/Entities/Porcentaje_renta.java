package com.ina.Proyecto_planilla.Entities;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "Porcentajes_renta")
public class Porcentaje_renta implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_porcentaje_renta;

    @NotNull(message = "El tope mínimo no puede estar vacío")
    private double tope_minimo;
    @NotNull(message = "El tope máximo no puede estar vacío")
    private double tope_maximo;
    @NotNull(message = "El porcentaje no puede estar vacío")
    private double porcentaje; 
    private String anio_vigencia;  

}
