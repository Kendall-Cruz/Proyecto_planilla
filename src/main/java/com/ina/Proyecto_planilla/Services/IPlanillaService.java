package com.ina.Proyecto_planilla.Services;

import java.util.List;

import com.ina.Proyecto_planilla.Entities.Planilla;

public interface IPlanillaService {
    public Long generarPlanilla(Planilla planilla);

    public Planilla obtenerPlanillaPorId(Long idPlanilla);

    public List<Planilla> findAllPlanilla();

}
