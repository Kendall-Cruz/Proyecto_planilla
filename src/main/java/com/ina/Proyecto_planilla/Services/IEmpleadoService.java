package com.ina.Proyecto_planilla.Services;

import java.time.LocalDate;
import java.util.List;

import com.ina.Proyecto_planilla.Entities.Empleado;

public interface IEmpleadoService {
    public List<Empleado> listarEmpleados();
    public List<Empleado> findAllEmpleadoActivoFecha(LocalDate fecha);
}
