package com.ina.Proyecto_planilla.ViewModels;

import java.util.List;

import com.ina.Proyecto_planilla.Entities.Detalle_deduccion;
import com.ina.Proyecto_planilla.Entities.Detalle_pago;

import lombok.Data;

@Data
public class DetallePlanillaVM {

    // Datos del empleado
    private Long idEmpleado;
    private String cedula;
    private String nombreCompleto;
    private String telefono;
    private String correo;
    private int puntosCarrera;

    // Datos de la planilla
    private Long idDetallePlanilla;
    private double salarioBase;
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

    // Listas de relaciones ya mapeadas como entidades
    private List<Detalle_deduccion> listaDeducciones;
    private List<Detalle_pago> listaPagos;
    
    // Constructor vacío requerido por JPA
    public DetallePlanillaVM() {
    }
    
    // Constructor para la proyección en consultas JPQL
    public DetallePlanillaVM(Long idEmpleado, String cedula, String nombreCompleto, String telefono, 
                                  String correo, int puntosCarrera, Long idDetallePlanilla, double salarioBase, 
                                  double salarioBruto, double deducciones, double salarioNeto, 
                                  double adelantoQuincenal, double salarioMensual, double retroactivo, 
                                  double porcentajeRenta, double montoPensiones, double montoSubsidio, 
                                  double montoPuntosCarrera, double pagos) {
        this.idEmpleado = idEmpleado;
        this.cedula = cedula;
        this.nombreCompleto = nombreCompleto;
        this.telefono = telefono;
        this.correo = correo;
        this.puntosCarrera = puntosCarrera;
        this.idDetallePlanilla = idDetallePlanilla;
        this.salarioBase = salarioBase;
        this.salarioBruto = salarioBruto;
        this.deducciones = deducciones;
        this.salarioNeto = salarioNeto;
        this.adelantoQuincenal = adelantoQuincenal;
        this.salarioMensual = salarioMensual;
        this.retroactivo = retroactivo;
        this.porcentajeRenta = porcentajeRenta;
        this.montoPensiones = montoPensiones;
        this.montoSubsidio = montoSubsidio;
        this.montoPuntosCarrera = montoPuntosCarrera;
        this.pagos = pagos;
    }
    
    // Método para cargar las listas de relaciones manualmente
    public void cargarListas(List<Detalle_deduccion> deducciones, List<Detalle_pago> pagos) {
        this.listaDeducciones = deducciones;
        this.listaPagos = pagos;
    }
}
