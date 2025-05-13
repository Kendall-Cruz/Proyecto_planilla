package com.ina.Proyecto_planilla.Services;

import java.time.LocalDate;
import java.util.List;

import com.ina.Proyecto_planilla.Entities.Empleado;
import com.ina.Proyecto_planilla.Entities.Puesto_empleado;

public interface IEmpleadoService {
    public List<Empleado> listarEmpleados();
    public List<Empleado> findAllEmpleadoActivoFecha(LocalDate fecha);
    public Double obtenerSalarioBaseMesAnterior(Long empleadoId, LocalDate fechaPlanilla);
    public Empleado findEmpleadoById(Long id);
    //---
    public List<Puesto_empleado> findAllNombramientos();
    public boolean verificarPuestoActivoEntreFechas(Empleado emplado , LocalDate fecha_inico , LocalDate fecha_fin);
}
