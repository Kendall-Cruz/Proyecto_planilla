package com.ina.Proyecto_planilla.Dao;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ina.Proyecto_planilla.Entities.Incapacidad;

public interface IIncapacidadDao extends JpaRepository<Incapacidad, Long> {

    @Query(value = "SELECT * FROM incapacidad i "
            + "WHERE i.empleado_id = :empleadoId "
            + "AND i.fecha_inicio <= LAST_DAY(:fecha) "
            + // empezó antes o durante el mes
            "AND i.fecha_fin >= DATE_FORMAT(:fecha, '%Y-%m-01')", nativeQuery = true) 
    List<Incapacidad> findByEmpleadoIdAndFecha(@Param("empleadoId") Long empleadoId, @Param("fecha") LocalDate fecha);

}
