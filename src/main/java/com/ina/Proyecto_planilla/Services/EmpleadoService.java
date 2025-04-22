package com.ina.Proyecto_planilla.Services;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ina.Proyecto_planilla.Dao.IEmpleadoDao;
import com.ina.Proyecto_planilla.Entities.Empleado;
import com.ina.Proyecto_planilla.Entities.Puesto_empleado;

@Service
public class EmpleadoService implements IEmpleadoService {

    @Autowired
    private IEmpleadoDao empleadoDao;

    @Override
    public List<Empleado> listarEmpleados() {
        return empleadoDao.findAll();
    }

    public Double obtenerSalarioBaseMesAnterior(Long empleadoId, LocalDate fechaPlanilla) {
        // Calcular el primer día del mes anterior
        YearMonth mesAnterior = YearMonth.from(fechaPlanilla).minusMonths(1);
        LocalDate inicioMesAnterior = mesAnterior.atDay(1);
        LocalDate finMesAnterior = mesAnterior.atEndOfMonth();

        // Buscar el puesto activo en el mes anterior
        Optional<Puesto_empleado> puestoActivo = empleadoDao.findActivePuestoByEmpleadoAndDate(
                empleadoId, inicioMesAnterior);

        if (puestoActivo.isPresent()) {
            return puestoActivo.get().getPuesto().getSalario_base();
        } else {
            throw new RuntimeException("No se encontró un puesto activo para el empleado en el mes anterior.");
        }
    }
    
}
