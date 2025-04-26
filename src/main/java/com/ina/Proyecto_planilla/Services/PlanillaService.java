package com.ina.Proyecto_planilla.Services;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ina.Proyecto_planilla.Dao.IDetalle_planillaDao;
import com.ina.Proyecto_planilla.Dao.IIncapacidadDao;
import com.ina.Proyecto_planilla.Dao.IPlanillaDao;
import com.ina.Proyecto_planilla.Entities.Detalle_planilla;
import com.ina.Proyecto_planilla.Entities.Empleado;
import com.ina.Proyecto_planilla.Entities.Incapacidad;
import com.ina.Proyecto_planilla.Entities.Planilla;

import jakarta.transaction.Transactional;

@Service
public class PlanillaService implements IPlanillaService {

    @Autowired
    private IEmpleadoService empleadoService;
    @Autowired
    private IPlanillaDao planillaDao;
    @Autowired
    private IIncapacidadDao incapacidadDao;
    @Autowired
    private IDetalle_planillaDao detallePlanillaDao;

    @Transactional
    @Override
    public boolean generarPlanilla(Planilla planilla) {
        try {

            double salario_mes_pasado = 0.0;

            List<Empleado> empleados = empleadoService.findAllEmpleadoActivoFecha(planilla.getFecha_planilla());

            for (Empleado empleado : empleados) {

                Detalle_planilla detalle = new Detalle_planilla(empleado, planilla);
                salario_mes_pasado = empleadoService.obtenerSalarioBaseMesAnterior(empleado.getId_empleado(), planilla.getFecha_planilla());

                // Verificar incapacidades  
                //Hacer cambios en la tabla de detalle_planilla para que se guarde el monto de la incapacidad que equivale al subsidio
                double subsidio = verificarIncapacidades(empleado.getId_empleado(), planilla.getFecha_planilla(), salario_mes_pasado);

                detallePlanillaDao.save(detalle);

            }

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public double verificarIncapacidades(Long empleadoId, LocalDate fechaPlanilla, double salarioBase) {

        List<Incapacidad> incapacidades = incapacidadDao.findByEmpleadoIdAndFecha(empleadoId, fechaPlanilla);

        int totalDiasIncapacidad = 0;

        // Definir el rango del mes
        LocalDate primerDiaMes = fechaPlanilla.withDayOfMonth(1);
        LocalDate ultimoDiaMes = fechaPlanilla.withDayOfMonth(fechaPlanilla.lengthOfMonth());

        for (Incapacidad incapacidad : incapacidades) {
            // Limitar el rango de la incapacidad al mes
            LocalDate inicio = incapacidad.getFecha_inicio().isBefore(primerDiaMes) ? primerDiaMes : incapacidad.getFecha_inicio();
            LocalDate fin = incapacidad.getFecha_fin().isAfter(ultimoDiaMes) ? ultimoDiaMes : incapacidad.getFecha_fin();

            int dias = (int) ChronoUnit.DAYS.between(inicio, fin) + 1;

            if (dias > 0) {
                totalDiasIncapacidad += dias;
            }
        }

        // Guardar en el detalle si tenés ese campo
        

        return calcularMontoIncapacidad(totalDiasIncapacidad, salarioBase); 
    }

    public double calcularMontoIncapacidad(int diasIncapacidad, double salarioBase) {
        double salarioDiario = salarioBase / 20;
        
        if(diasIncapacidad > 3) {
            return (salarioDiario * diasIncapacidad) * 0.40; 
        } else {
            return 0.0;
        }
    }

    @Override
    public boolean generarDetallePlanilla(Detalle_planilla detallePlanilla) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'generarDetallePlanilla'");
    }

}
