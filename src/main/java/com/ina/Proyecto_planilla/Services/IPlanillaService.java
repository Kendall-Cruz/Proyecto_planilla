package com.ina.Proyecto_planilla.Services;

import java.time.LocalDate;

import com.ina.Proyecto_planilla.Entities.Detalle_planilla;
import com.ina.Proyecto_planilla.Entities.Planilla;

public interface IPlanillaService {
    public Long generarPlanilla(Planilla planilla);

    public boolean generarDetallePlanilla(Detalle_planilla detallePlanilla);

    public double verificarIncapacidades(Long empleadoId , LocalDate fechaPlanilla , double salarioBase);

}
