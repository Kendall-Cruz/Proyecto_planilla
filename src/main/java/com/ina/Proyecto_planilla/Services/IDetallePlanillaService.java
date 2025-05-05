package com.ina.Proyecto_planilla.Services;

import java.util.List;

import com.ina.Proyecto_planilla.Dto.DetallePlanillaDTO;

public interface  IDetallePlanillaService {
    List<DetallePlanillaDTO> obtenerDetallesPorPlanilla(Long idPlanilla);
    DetallePlanillaDTO obtenerDetallePorId(Long idDetallePlanilla);
}
