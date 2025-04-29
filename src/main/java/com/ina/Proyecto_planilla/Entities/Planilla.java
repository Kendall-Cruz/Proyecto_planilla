package com.ina.Proyecto_planilla.Entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

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
@Table(name = "Planillas")
public class Planilla implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_planilla;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "La fecha de planilla no puede estar vacía")
    private LocalDate fecha_planilla;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "La fecha de pago quincenal no puede estar vacía")
    private LocalDate fecha_pago_quincenal;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "La fecha de pago mensual no puede estar vacía")
    private LocalDate fecha_pago_mensual;
    
    @NotBlank(message = "El tipo de planilla no puede estar vacío")
    @Column(length = 20)
    private String tipo_planilla;
    
    @OneToMany(mappedBy = "planilla")
    private List<Detalle_planilla> detalles_planilla;
}