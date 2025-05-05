package com.ina.Proyecto_planilla.Dao;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ina.Proyecto_planilla.Entities.Permiso;

public interface IPermisoDao extends JpaRepository<Permiso, Long> {

    @Query(value = "SELECT * FROM Permisos p " +
            "WHERE p.id_empleado = :empleadoId " +
            "AND p.fecha_inicio <= EOMONTH(DATEADD(MONTH, -1, :fecha)) " +
            "AND p.fecha_fin >= DATEFROMPARTS(YEAR(DATEADD(MONTH, -1, :fecha)), MONTH(DATEADD(MONTH, -1, :fecha)), 1) " +
            "AND p.con_goce_salario = 0", 
            nativeQuery = true)
    List<Permiso> obtenerPermisosSinGoceEmpleado(@Param("empleadoId") Long empleadoId, @Param("fecha") LocalDate fecha);
}
