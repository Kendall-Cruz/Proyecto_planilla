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
@Table(name = "Puestos")
public class Puesto implements Serializable {
private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_puesto;
    
    @NotBlank(message = "El nombre del puesto no puede estar vacío")
    @Column(length = 50)
    private String nombre_puesto;
    
    @NotNull(message = "El salario base no puede estar vacío")
    private double salario_base;
    
    private boolean salario_global;
    private int categoria;
    
    @OneToMany(mappedBy = "puesto")
    private List<Puesto_empleado> puestos_empleado;
}
