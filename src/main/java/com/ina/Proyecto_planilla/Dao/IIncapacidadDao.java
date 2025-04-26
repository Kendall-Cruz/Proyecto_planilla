package com.ina.Proyecto_planilla.Dao;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ina.Proyecto_planilla.Entities.Incapacidad;

public interface IIncapacidadDao extends JpaRepository<Incapacidad, Long> {

    @Query(value = "SELECT * FROM dbo.Incapacidades i "
            + "WHERE i.id_empleado = :empleadoId "
            + "AND i.fecha_inicio <= EOMONTH(:fecha) "
            + "AND i.fecha_fin >= DATEFROMPARTS(YEAR(:fecha), MONTH(:fecha), 1)",
            nativeQuery = true)
    List<Incapacidad> findByEmpleadoIdAndFecha(@Param("empleadoId") Long empleadoId, @Param("fecha") LocalDate fecha);

}
