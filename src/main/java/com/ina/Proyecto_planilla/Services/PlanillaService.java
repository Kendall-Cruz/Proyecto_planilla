package com.ina.Proyecto_planilla.Services;

import org.springframework.stereotype.Service;

@Service
public abstract class PlanillaService implements IPlanillaService {
    /*
    @Transactional
    @Override
    public boolean generarPlanilla(Planilla planilla) {
        try {
            planillaRepository.save(planilla);

            List<Empleado> empleados = empleadoService.findAllEmpleadoActivoFecha(planilla.getFecha_planilla());

            for (Empleado empleado : empleados) {
                //BigDecimal bruto = calcularSalarioBruto(empleado);
                //BigDecimal deducciones = deduccionService.calcularDeducciones(empleado, planilla.getFecha_planilla());
                //BigDecimal neto = bruto.subtract(deducciones);

                Detalle_planilla detalle = new DetallePlanilla();
                detalle.setEmpleado(empleado);
                detalle.setPlanilla(planilla);

            }

            return true;
        } catch (Exception e) {
            return false;
        }
    }
        */
}
