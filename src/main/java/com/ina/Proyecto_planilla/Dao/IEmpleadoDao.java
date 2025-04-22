package com.ina.Proyecto_planilla.Dao;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ina.Proyecto_planilla.Entities.Empleado;
import com.ina.Proyecto_planilla.Entities.Puesto_empleado;

public interface IEmpleadoDao extends JpaRepository<Empleado, Long> {

    Empleado findByNombre(String nombre);

    Empleado findById(long id);

    @Query("SELECT pe FROM Puesto_empleado pe "+ "WHERE pe.empleado.id_empleado = :empleadoId " + "AND :fecha BETWEEN pe.fecha_nombramiento AND pe.fecha_vence")
    Optional<Puesto_empleado> findActivePuestoByEmpleadoAndDate(@Param("empleadoId") Long empleadoId, @Param("fecha") LocalDate fecha);

}
