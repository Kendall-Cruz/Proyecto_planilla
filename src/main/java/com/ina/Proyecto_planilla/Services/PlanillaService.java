package com.ina.Proyecto_planilla.Services;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ina.Proyecto_planilla.Dao.IDeduccionDao;
import com.ina.Proyecto_planilla.Dao.IDetalle_deduccionDao;
import com.ina.Proyecto_planilla.Dao.IDetalle_pagoDao;
import com.ina.Proyecto_planilla.Dao.IDetalle_planillaDao;
import com.ina.Proyecto_planilla.Dao.IEmpleadoDao;
import com.ina.Proyecto_planilla.Dao.IIncapacidadDao;
import com.ina.Proyecto_planilla.Dao.IPagoDao;
import com.ina.Proyecto_planilla.Dao.IPensionDao;
import com.ina.Proyecto_planilla.Dao.IPermisoDao;
import com.ina.Proyecto_planilla.Dao.IPlanillaDao;
import com.ina.Proyecto_planilla.Dao.IPorcentaje_rentaDao;
import com.ina.Proyecto_planilla.Entities.Deduccion;
import com.ina.Proyecto_planilla.Entities.Detalle_deduccion;
import com.ina.Proyecto_planilla.Entities.Detalle_pago;
import com.ina.Proyecto_planilla.Entities.Detalle_planilla;
import com.ina.Proyecto_planilla.Entities.Empleado;
import com.ina.Proyecto_planilla.Entities.Incapacidad;
import com.ina.Proyecto_planilla.Entities.Pago;
import com.ina.Proyecto_planilla.Entities.Pension;
import com.ina.Proyecto_planilla.Entities.Permiso;
import com.ina.Proyecto_planilla.Entities.Planilla;
import com.ina.Proyecto_planilla.Entities.Porcentaje_renta;
import com.ina.Proyecto_planilla.Entities.Puesto_empleado;

import jakarta.transaction.Transactional;

@Service
public class PlanillaService implements IPlanillaService {

    private final double MONTO_PUNTOS_CARRERA = 3375;
    private final int MAX_DIAS_TRABAJADOS = 20;

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
    private IDetalle_pagoDao detallePagoDao;
    @Autowired
    private IDetalle_deduccionDao detalleDeduccionDao;
    @Autowired
    private IPensionDao pensionDao;
    @Autowired
    private IPermisoDao permisoDao;
    @Autowired
    private IPorcentaje_rentaDao porcentaje_rentaDao;

    @Override
    public Planilla obtenerPlanillaPorId(Long idPlanilla) {
        return planillaDao.findById(idPlanilla).orElse(null);
    }

    @Override
    public List<Planilla> findAllPlanilla() {
        return planillaDao.findAll();
    }

    @Transactional
    @Override
    public Long generarPlanilla(Planilla planilla) {
        try {
            planillaDao.save(planilla);

            List<Puesto_empleado> puestosActivos = empleadoDao.findAllPuestosActivosConEmpleado(planilla.getFecha_planilla());

            for (Puesto_empleado puesto : puestosActivos) {
                Empleado empleado = puesto.getEmpleado();
                double salarioMesPasado = empleadoService.obtenerSalarioBaseMesAnterior(empleado.getId_empleado(), planilla.getFecha_planilla());
                double salarioBase = puesto.getPuesto().getSalario_base();
                double subsidio = 0.0;

                // Se crea el detalle y se guarda con el empleado y la planilla predefinidos y sus otros valores en 0
                Detalle_planilla detalle = new Detalle_planilla(empleado, planilla);
                detallePlanillaDao.save(detalle);

                detalle.setSalario_base(salarioBase);
                // Calcular años trabajados
                int aniosTrabajados = empleadoDao.countTotalDiasTrabajados(empleado.getId_empleado()) / 365;

                List<Incapacidad> incapacidades = incapacidadDao.findByEmpleadoIdAndFecha(empleado.getId_empleado(), planilla.getFecha_planilla());

                if (incapacidades.isEmpty()) {
                    detalle.setMonto_subsidio(0.0);
                } else {
                    int diasIncapacidad = verificarIncapacidades(planilla.getFecha_planilla(), incapacidades); 
                    subsidio = calcularMontoIncapacidad(diasIncapacidad, salarioMesPasado);
                    detalle.setMonto_subsidio(subsidio); 
                    detalle.setDias_incapacidad(diasIncapacidad - 3); // Restar los 3 días que no se pagan
                }

                // Permisos sin goce de salario
                List<Permiso> permisos = permisoDao.obtenerPermisosSinGoceEmpleado(empleado.getId_empleado(), planilla.getFecha_planilla());
                if (!permisos.isEmpty()) {
                    int totalDiasPermiso = verificarPermisosSinGoce(planilla.getFecha_planilla(), permisos);
                    if(totalDiasPermiso >= 20) {
                        continue;
                    } else {
                        detalle.setDias_permiso_sin_goce(totalDiasPermiso);
                    }

                }

                //Sacar el salario proporcional
                double salarioDiario = salarioBase / MAX_DIAS_TRABAJADOS;
                int dias_trabajados = MAX_DIAS_TRABAJADOS - (detalle.getDias_incapacidad() + detalle.getDias_permiso_sin_goce());
                detalle.setSalario_proporcional(salarioDiario * dias_trabajados);

                double salarioProporcional = detalle.getSalario_proporcional();

                // Pagos adicionales si NO es salario global
                if (!puesto.getPuesto().isSalario_global()) {
                    double pagos = calcularPagos(detalle, puesto, aniosTrabajados);
                    double puntosCarrera = calcularPuntosCarrera(empleado.getId_empleado());

                    detalle.setPagos(pagos);
                    detalle.setMonto_puntos_carrera(puntosCarrera);

                    salarioProporcional += pagos + puntosCarrera;
                }

                detalle.setSalario_bruto(salarioProporcional);

                // Pensiones
                double montoPensiones = calcularPensiones(empleado.getId_empleado(), planilla.getFecha_planilla(), salarioProporcional);
                detalle.setMonto_pensiones(montoPensiones);
                salarioProporcional -= montoPensiones;

                // Porcentaje de renta
                double montoRenta = calcularPorcentajeDeRenta(salarioProporcional, planilla.getFecha_planilla());
                detalle.setMonto_porcentaje_renta(montoRenta);
                salarioProporcional -= montoRenta;

                // Otras deducciones
                double deducciones = calcularDeducciones(detalle, puesto, salarioProporcional, aniosTrabajados);
                detalle.setDeducciones(deducciones);
                salarioProporcional -= deducciones;

                // Salario neto
                double salarioNeto = salarioProporcional + detalle.getMonto_subsidio();
                detalle.setSalario_neto(salarioNeto);

                
                detalle.setAdelanto_quincenal((salarioNeto * 40) / 100);
                detalle.setSalario_mensual((salarioNeto * 60) / 100);

                // Guardar detalle final
                detallePlanillaDao.save(detalle);
            }

            return planilla.getId_planilla();

        } catch (Exception e) {
            throw e;
        }
    }

   
    public int verificarPermisosSinGoce(LocalDate fechaPlanilla, List<Permiso> permisos) {
        int totalDiasPermiso = 0;

        // Obtener el mes anterior
        LocalDate fechaMesAnterior = fechaPlanilla.minusMonths(1);
        LocalDate primerDiaMesAnterior = fechaMesAnterior.withDayOfMonth(1);
        LocalDate ultimoDiaMesAnterior = fechaMesAnterior.withDayOfMonth(fechaMesAnterior.lengthOfMonth());

        for (Permiso permiso : permisos) {
            LocalDate inicio = permiso.getFecha_inicio().isBefore(primerDiaMesAnterior) ? primerDiaMesAnterior : permiso.getFecha_inicio();
            LocalDate fin = permiso.getFecha_fin().isAfter(ultimoDiaMesAnterior) ? ultimoDiaMesAnterior : permiso.getFecha_fin();

            int dias = (int) ChronoUnit.DAYS.between(inicio, fin) + 1;

            if (dias > 0) {
                totalDiasPermiso += dias;
            }
        }

        return totalDiasPermiso;
    }

    public int verificarIncapacidades(LocalDate fechaPlanilla, List<Incapacidad> incapacidades) {
        int totalDiasIncapacidad = 0;

        // Obtener el mes anterior
        LocalDate fechaMesAnterior = fechaPlanilla.minusMonths(1);
        // Definir el rango del mes anterior
        LocalDate primerDiaMesAnterior = fechaMesAnterior.withDayOfMonth(1);
        LocalDate ultimoDiaMesAnterior = fechaMesAnterior.withDayOfMonth(fechaMesAnterior.lengthOfMonth());

        for (Incapacidad incapacidad : incapacidades) {
            // Limitar el rango de la incapacidad al mes anterior
            LocalDate inicio = incapacidad.getFecha_inicio().isBefore(primerDiaMesAnterior) ? primerDiaMesAnterior : incapacidad.getFecha_inicio();

            LocalDate fin = incapacidad.getFecha_fin().isAfter(ultimoDiaMesAnterior) ? ultimoDiaMesAnterior : incapacidad.getFecha_fin();

            int dias = (int) ChronoUnit.DAYS.between(inicio, fin) + 1;

            if (dias > 0) {
                totalDiasIncapacidad += dias;
            }
        }

        return totalDiasIncapacidad;
    }

    public double calcularMontoIncapacidad(int diasIncapacidad, double salarioBase) {
        double salarioDiario = salarioBase / MAX_DIAS_TRABAJADOS;
    
        if (diasIncapacidad <= 3) {
            // Pagar los primeros días de incapacidad al 100%
            return salarioDiario * diasIncapacidad;
        } else {
            // Pagar los primeros 3 días al 100%
            int diasSubsidiados = diasIncapacidad - 3;
            double subsidio = (salarioDiario * diasSubsidiados) * 0.40;
    
            return (salarioDiario * 3) + subsidio;
        }
    }

    public double calcularPagos(Detalle_planilla detallePlanilla, Puesto_empleado puesto, int anios_trabajados) {
        List<Pago> pagos = pagoDao.findByActivoTrue();
        double montoPago;
        double sumaPagos = 0.0; // Variable para almacenar la suma de los pagos 

        for (Pago pago : pagos) {

            Detalle_pago detallePago = new Detalle_pago();

            detallePago.setPago(pago);
            detallePago.setDetalle_planilla(detallePlanilla);

            //Si la categoría del pago es igual a la categoría del puesto o si la categoría del pago es igual a cero lo cual se aplica para cualquier categoría
            if (puesto.getPuesto().getCategoria() == pago.getCategoria() || pago.getCategoria() == 0) {
                if (pago.isPorcentaje()) {
                    // Si el pago es un porcentaje, calcular el monto basado en el salario base
                    montoPago = (puesto.getPuesto().getSalario_base() * pago.getValor_pago()) / 100;
                    if (pago.isUsa_fechas()) {
                        montoPago = montoPago * anios_trabajados; // Multiplicar por los años trabajados
                    }
                } else {
                    // Si el pago es un monto fijo, sumar el valor del pago al total
                    montoPago = pago.getValor_pago();
                    if (pago.isUsa_fechas()) {
                        montoPago = montoPago * anios_trabajados; // Multiplicar por los años trabajados

                    }
                }

                if (montoPago > 0) {
                    sumaPagos += montoPago; // Sumar el monto del pago a la suma total  
                    detallePago.setMonto(montoPago);

                    // Guardar el detalle del pago en la base de datos
                    detallePagoDao.save(detallePago);
                }

            }

        }

        return sumaPagos;
    }

    public double calcularDeducciones(Detalle_planilla detallePlanilla, Puesto_empleado puesto, double salarioBruto, int añosTrabajados) {
        List<Deduccion> deducciones = deduccionDao.findByActivoTrue();
        double montoDeduccion = 0.0;
        double sumaDeducciones = 0;

        for (Deduccion deduccion : deducciones) {

            if (salarioBruto > deduccion.getValor_deduccion()) {

                if (puesto.getPuesto().getCategoria() == deduccion.getCategoria() || deduccion.getCategoria() == 0) {
                    Detalle_deduccion detalleDeduccion = new Detalle_deduccion();

                    detalleDeduccion.setDeduccion(deduccion);
                    detalleDeduccion.setDetalle_planilla(detallePlanilla);

                    if (deduccion.isPorcentaje()) {
                        // Si la deducción es un porcentaje, calcular el monto basado en el salario base
                        montoDeduccion = (salarioBruto * deduccion.getValor_deduccion()) / 100;
                    } else {
                        // Si la deducción es un monto fijo, solo usar el valor directamente
                        montoDeduccion = deduccion.getValor_deduccion();
                    }

                    detalleDeduccion.setMonto(montoDeduccion);
                    sumaDeducciones += montoDeduccion;

                    detalleDeduccionDao.save(detalleDeduccion);
                }
            }

        }
        return sumaDeducciones;
    }

    //calcularPensiones
    public double calcularPensiones(Long empleadoId, LocalDate fecha, double salario_bruto) {

        double acumulado_pensiones = 0.0;

        List<Pension> pensiones = pensionDao.findAllPensionesActivasMes(fecha, empleadoId);
        if (!pensiones.isEmpty()) {
            for (Pension pension : pensiones) {
                if (pension.getMonto_pension() < salario_bruto) {
                    acumulado_pensiones += pension.getMonto_pension();
                }
            }
            return acumulado_pensiones;
        }
        return 0;
    }

    //calcularPuntosCarrera
    public double calcularPuntosCarrera(Long empleadoId) {
        return empleadoDao.getPuntosCarreraByEmpleadoId(empleadoId) * MONTO_PUNTOS_CARRERA;
    }

    public double calcularPorcentajeDeRenta(double salario_bruto, LocalDate fecha_planilla) {

        List<Porcentaje_renta> rublos = porcentaje_rentaDao.getAllPorcentaje_renta(String.valueOf(fecha_planilla.getYear()));
        double monto_porcentaje_renta = 0;

        for (Porcentaje_renta rublo : rublos) {
            double topeMinimo = rublo.getTope_minimo();
            double topeMaximo = rublo.getTope_maximo();
            double porcentaje = rublo.getPorcentaje() / 100.0;

            if (topeMaximo == 0 || salario_bruto < topeMaximo) {

                double base = salario_bruto - topeMinimo;
                if (base > 0) {
                    monto_porcentaje_renta += base * porcentaje;
                }
                break;
            } else if (salario_bruto > topeMaximo) {

                double base = topeMaximo - topeMinimo;
                monto_porcentaje_renta += base * porcentaje;
            }
        }

        return monto_porcentaje_renta;
    }

}
