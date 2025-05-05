package com.ina.Proyecto_planilla.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ina.Proyecto_planilla.Dao.IDetalle_planillaDao;
import com.ina.Proyecto_planilla.Dto.DetallePlanillaDTO;
import com.ina.Proyecto_planilla.Entities.Detalle_planilla;

@Service
public class DetallePlanillaService implements IDetallePlanillaService {

    @Autowired
    private IDetalle_planillaDao detallePlanillaDao;

    @Override
    public List<DetallePlanillaDTO> obtenerDetallesPorPlanilla(Long idPlanilla) {
        List<Detalle_planilla> detallesPlanilla = detallePlanillaDao.findAllByPlanillaid_Planilla(idPlanilla);

        for (Detalle_planilla detalle : detallesPlanilla) {
            detalle.getDetalle_pagos().size();
            detalle.getDetalles_deducciones().size();
        }

        return detallesPlanilla.stream().map(detalle -> new DetallePlanillaDTO(
                detalle.getId_detalle_planilla(),
                detalle.getEmpleado().getId_empleado(),
                detalle.getEmpleado().getNombre() + " " + detalle.getEmpleado().getApellido1() + " " + detalle.getEmpleado().getApellido2(),
                detalle.getPlanilla().getId_planilla(),
                detalle.getSalario_base(),
                detalle.getSalario_bruto(),
                detalle.getDeducciones(),
                detalle.getSalario_neto(),
                detalle.getAdelanto_quincenal(),
                detalle.getSalario_mensual(),
                detalle.getRetroactivo(),
                detalle.getMonto_porcentaje_renta(),
                detalle.getMonto_pensiones(),
                detalle.getMonto_subsidio(),
                detalle.getMonto_puntos_carrera(),
                detalle.getPagos(),
                detalle.getDetalles_deducciones(),
                detalle.getDetalle_pagos()
        )).toList();
    }

}
