package com.ina.Proyecto_planilla.Dao;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ina.Proyecto_planilla.Entities.Empleado;
import com.ina.Proyecto_planilla.Entities.Puesto_empleado;

public interface IEmpleadoDao extends JpaRepository<Empleado, Long> {

    Empleado findByNombre(String nombre);

    Empleado findById(long id);

    @Query(value = "SELECT * FROM Puestos_empleado pe "
            + "WHERE pe.id_empleado = :empleadoId "
            + "AND MONTH(:fecha) BETWEEN MONTH(pe.fecha_nombramiento) AND MONTH(pe.fecha_vence) "
            + "AND YEAR(:fecha) BETWEEN YEAR(pe.fecha_nombramiento) AND YEAR(pe.fecha_vence) "
            + "AND pe.borrado = 0", nativeQuery = true)
    Puesto_empleado findActivePuestoByEmpleadoAndDate(@Param("empleadoId") Long empleadoId, @Param("fecha") LocalDate fecha);

    @Query(value = "SELECT DISTINCT e.* FROM Puestos_empleado pe "
            + "JOIN Empleados e ON pe.id_empleado = e.id_empleado "
            + "WHERE :fecha BETWEEN pe.fecha_nombramiento AND pe.fecha_vence "
            + "AND pe.borrado = 0", nativeQuery = true)
    List<Empleado> findAllEmpleadoActivoEnMesSQL(@Param("fecha") LocalDate fecha);

    @Query(value = "SELECT SUM(DATEDIFF(DAY, pe.fecha_nombramiento, "
            + "CASE WHEN pe.fecha_vence < GETDATE() THEN pe.fecha_vence ELSE GETDATE() END) + 1) "
            + "FROM Puestos_empleado pe "
            + "WHERE pe.id_empleado = :empleadoId",
            nativeQuery = true)
    int countTotalDiasTrabajados(@Param("empleadoId") Long empleadoId);

    @Query(value = "SELECT e.puntos_carrera "
            + "FROM empleados e WHERE e.id_empleado = :empleadoId",
            nativeQuery = true)
    int getPuntosCarreraByEmpleadoId(@Param("empleadoId") Long empleadoId);

    @Query("SELECT pe FROM Puesto_empleado pe "
            + "JOIN FETCH pe.empleado e "
            + "JOIN FETCH pe.puesto p "
            + "WHERE :fecha BETWEEN pe.fecha_nombramiento AND pe.fecha_vence "
            + "AND pe.borrado = false")
    List<Puesto_empleado> findAllPuestosActivosConEmpleado(@Param("fecha") LocalDate fecha);

}
