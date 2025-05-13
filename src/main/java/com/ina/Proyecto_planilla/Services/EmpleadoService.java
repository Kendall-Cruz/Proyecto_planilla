package com.ina.Proyecto_planilla.Services;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ina.Proyecto_planilla.Dao.IEmpleadoDao;
import com.ina.Proyecto_planilla.Dao.IPuesto_empleadoDao;
import com.ina.Proyecto_planilla.Entities.Empleado;
import com.ina.Proyecto_planilla.Entities.Puesto_empleado;

@Service
public class EmpleadoService implements IEmpleadoService {

    @Autowired
    private IEmpleadoDao empleadoDao;
    @Autowired
    private IPuesto_empleadoDao puestoEmpleadoDao;

    @Override
    public List<Empleado> listarEmpleados() {
        return empleadoDao.findAll();
    }
    @Override
    public Double obtenerSalarioBaseMesAnterior(Long empleadoId, LocalDate fechaPlanilla) {
        // Calcular el primer día del mes anterior
        YearMonth mesAnterior = YearMonth.from(fechaPlanilla).minusMonths(1);
        LocalDate inicioMesAnterior = mesAnterior.atDay(1);
        LocalDate finMesAnterior = mesAnterior.atEndOfMonth();

        // Buscar el puesto activo en el mes anterior
        Puesto_empleado puestoActivo = empleadoDao.findActivePuestoByEmpleadoAndDate(
                empleadoId, inicioMesAnterior);

        if (puestoActivo != null) {
            return puestoActivo.getPuesto().getSalario_base();
        } else {
            return 0.0;
        }
    }

    @Override
    public List<Empleado> findAllEmpleadoActivoFecha(LocalDate fecha) {
        return empleadoDao.findAllEmpleadoActivoEnMesSQL(fecha);
    }
    @Override
    public List<Puesto_empleado> findAllNombramientos() {
        List<Puesto_empleado> puestos = puestoEmpleadoDao.findAll();

        return puestos.stream()
                .filter(p -> !p.isBorrado())
                .toList();
    }
    @Override
    public Empleado findEmpleadoById(Long id) {
        return empleadoDao.findById(id).orElse(null);
    }

    @Override
    public boolean verificarPuestoActivoEntreFechas(Empleado emplado , LocalDate fecha_inico , LocalDate fecha_fin){
        List<Puesto_empleado> puestos = empleadoDao.findAllPuestoByEmpleadoId(emplado.getId_empleado());

        for (Puesto_empleado puesto : puestos) {

            if (puesto.getFecha_nombramiento().isBefore(fecha_fin) && puesto.getFecha_vence().isAfter(fecha_inico)) {
                return true; // El puesto está activo entre las fechas dadas
            }
        }
        return false; // Ningún puesto activo entre las fechas dadas
    }


    
    
}
