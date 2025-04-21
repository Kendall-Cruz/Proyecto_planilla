package com.ina.Proyecto_planilla.Entities;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Entity
@Table(name = "Titulos_academicos")
public class Titulo_academico implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_titulo_academico;
    
    @ManyToOne
    @JoinColumn(name = "id_empleado", nullable = false)
    private Empleado empleado;
    
    @NotBlank(message = "El nombre del certificado no puede estar vacío")
    @Column(length = 100)
    private String nombre_certificado;
    
    @NotBlank(message = "El nombre de la institución no puede estar vacío")
    @Column(length = 100)
    private String nombre_institucion;
    
    @NotBlank(message = "El año de obtención no puede estar vacío")
    @Column(length = 4)
    private String anio_obtencion;
    
    @NotBlank(message = "El grado académico no puede estar vacío")
    @Column(length = 20)
    private String grado_academico;
}