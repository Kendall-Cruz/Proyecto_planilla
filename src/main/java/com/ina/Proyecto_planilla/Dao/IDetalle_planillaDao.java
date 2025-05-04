package com.ina.Proyecto_planilla.Dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ina.Proyecto_planilla.Entities.Detalle_planilla;
import com.ina.Proyecto_planilla.ViewModels.DetallePlanillaVM;

public interface IDetalle_planillaDao extends JpaRepository<Detalle_planilla, Long> {

    @Query("SELECT new com.ina.Proyecto_planilla.dto.DetallePlanillaVM(" +
           "d.idDetalle, d.empleado.cedula, d.empleado.nombre, d.empleado.apellido1, " +
           "d.empleado.apellido2, d.salarioBruto, d.totalDeducciones, d.salarioNeto) " +
           "FROM Detalle_planilla d WHERE d.planilla.idPlanilla = :idPlanilla")
    List<DetallePlanillaVM> obtenerDetallePlanilla(@Param("idPlanilla") Long idPlanilla);
}
