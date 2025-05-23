package com.ina.Proyecto_planilla.Dto;

import java.util.List;

import com.ina.Proyecto_planilla.Entities.Detalle_deduccion;
import com.ina.Proyecto_planilla.Entities.Detalle_pago;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DetallePlanillaDTO {
    private Long idDetallePlanilla;
    private Long idEmpleado;
    private String nombreEmpleado;
    private Long idPlanilla;
    private double salarioBase;
    private double salarioProporcional;
    private double salarioBruto;
    private double deducciones;
    private double salarioNeto;
    private double adelantoQuincenal;
    private double salarioMensual;
    private double retroactivo;
    private double porcentajeRenta;
    private double montoPensiones;
    private double montoSubsidio;
    private double montoPuntosCarrera;
    private double pagos;
    private int diasIncapacidad; 
    private int diasPermisoSinGoce;
    private int diasTrabajados;

    // Listas relacionadas
    private List<Detalle_deduccion> detallesDeducciones;
    private List<Detalle_pago> detallePagos;
}