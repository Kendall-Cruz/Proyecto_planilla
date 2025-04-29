package com.ina.Proyecto_planilla.Services;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ina.Proyecto_planilla.Dao.IDeduccionDao;
import com.ina.Proyecto_planilla.Dao.IDetalle_pago;
import com.ina.Proyecto_planilla.Dao.IDetalle_planillaDao;
import com.ina.Proyecto_planilla.Dao.IEmpleadoDao;
import com.ina.Proyecto_planilla.Dao.IIncapacidadDao;
import com.ina.Proyecto_planilla.Dao.IPagoDao;
import com.ina.Proyecto_planilla.Dao.IPlanillaDao;
import com.ina.Proyecto_planilla.Entities.Deduccion;
import com.ina.Proyecto_planilla.Entities.Detalle_deduccion;
import com.ina.Proyecto_planilla.Entities.Detalle_pago;
import com.ina.Proyecto_planilla.Entities.Detalle_planilla;
import com.ina.Proyecto_planilla.Entities.Empleado;
import com.ina.Proyecto_planilla.Entities.Incapacidad;
import com.ina.Proyecto_planilla.Entities.Pago;
import com.ina.Proyecto_planilla.Entities.Planilla;
import com.ina.Proyecto_planilla.Entities.Puesto_empleado;

import jakarta.transaction.Transactional;

@Service
public class PlanillaService implements IPlanillaService {

    @Autowired
    private IEmpleadoService empleadoService;
    @Autowired
    private IEmpleadoDao empleadoDao;
    @Autowired
    private IPlanillaDao planillaDao;
    @Autowired
    private IIncapacidadDao incapacidadDao;
    @Autowired
    private IDetalle_planillaDao detallePlanillaDao;
    @Autowired
    private IDeduccionDao deduccionDao;
    @Autowired
    private IPagoDao pagoDao;
    @Autowired
    private IDetalle_pago detallePagoDao;

    @Transactional
    @Override
    public boolean generarPlanilla(Planilla planilla) {
        try {

            double salario_mes_pasado = 0.0;

            List<Empleado> empleados = empleadoService.findAllEmpleadoActivoFecha(planilla.getFecha_planilla());

            for (Empleado empleado : empleados) {

                Detalle_planilla detalle = new Detalle_planilla(empleado, planilla);
                Puesto_empleado puesto = empleadoDao.findActivePuestoByEmpleadoAndDate(empleado.getId_empleado(), planilla.getFecha_planilla());
                salario_mes_pasado = empleadoService.obtenerSalarioBaseMesAnterior(empleado.getId_empleado(), planilla.getFecha_planilla());


                // Verificar incapacidades
                //Subsidio  
                detalle.setMonto_subsidio(verificarIncapacidades(empleado.getId_empleado(), planilla.getFecha_planilla(), salario_mes_pasado)); 

                int anios_trabajados = empleadoDao.countTotalDiasTrabajados(empleado.getId_empleado()) / 365;

                //Aplicar pagos si el salario no es global
                if(!puesto.getPuesto().isSalario_global()){ //Tiene que ser salario del mes actual
                    detalle.setDeducciones(calcularDeducciones(detalle, salario_mes_pasado, anios_trabajados));
                }
                //Si no se aplican solo las deducciones




                

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


    public double calcularPagos(Detalle_planilla detallePlanilla, double salarioBruto , int anios_trabajados) {
        List<Pago> pagos = pagoDao.findAllPagosActivos(); 
        double montoPago;
        double sumaPagos = 0.0; // Variable para almacenar la suma de los pagos 

        for (Pago pago : pagos) {

            Detalle_pago detallePago = new Detalle_pago();

            detallePago.setPago(pago);
            detallePago.setDetalle_planilla(detallePlanilla);

            if (pago.isPorcentaje()) {
                // Si el pago es un porcentaje, calcular el monto basado en el salario base
                montoPago = (salarioBruto * pago.getValor_pago()) / 100;
                if(pago.isUsa_fechas())
                    montoPago = montoPago * anios_trabajados; // Multiplicar por los años trabajados
            } else {
                // Si el pago es un monto fijo, sumar el valor del pago al total
                montoPago = pago.getValor_pago();
                if(pago.isUsa_fechas())
                    montoPago = montoPago * anios_trabajados; // Multiplicar por los años trabajados
            }
            sumaPagos += montoPago; // Sumar el monto del pago a la suma total  

            detallePago.setMonto(montoPago);

            // Guardar el detalle del pago en la base de datos
            detallePagoDao.save(detallePago); 
        }

        return sumaPagos; // Retornar el monto total de pagos
    }

    public double calcularDeducciones(Detalle_planilla detallePlanilla, double salarioBruto , int diasTrabajados) {
        List<Deduccion> deducciones = deduccionDao.findAllDuccionesAct();
        double montoDeduccion = 0.0;
        //int anios_trabajados = 0; // Variable para almacenar los años trabajados

        for (Deduccion deduccion : deducciones) {

            Detalle_deduccion detalleDeduccion = new Detalle_deduccion();

            detalleDeduccion.setDeduccion(deduccion);
            detalleDeduccion.setDetalle_planilla(detallePlanilla);


            if (deduccion.isPorcentaje()) {
                
                // Si la deducción es un porcentaje, calcular el monto basado en el salario base
                montoDeduccion = (salarioBruto * deduccion.getValor_deduccion()) / 100;

            }
            else {
                // Si la deducción es un monto fijo, restar el valor de la deducción al total
                montoDeduccion = salarioBruto - deduccion.getValor_deduccion();
            }

            detalleDeduccion.setMonto(montoDeduccion);



        }

        return 0;
    }
    
    @Override
    public boolean generarDetallePlanilla(Detalle_planilla detallePlanilla) {
        throw new UnsupportedOperationException("Unimplemented method 'generarDetallePlanilla'");
    }

}
