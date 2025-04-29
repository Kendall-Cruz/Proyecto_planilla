package com.ina.Proyecto_planilla.Dao;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ina.Proyecto_planilla.Entities.Pension;

public interface IPensionDao extends JpaRepository<Pension, Long> {

    @Query(value = "SELECT p.id_pension , p.id_empleado , p.fecha_rige , p.fecha_vence , p.porcentaje FROM pensiones p "
            + "WHERE MONTH(:fecha) BETWEEN MONTH(p.fecha_rige) AND MONTH(p.fecha_vence)"
            + "AND YEAR(:fecha) BETWEEN YEAR(p.fecha_rige) AND YEAR(p.fecha_vence)"
            + "AND p.id_empleado = :empleadoId",
            nativeQuery = true)
    List<Pension> findAllPensionesActivasMes(@Param("fecha") LocalDate fecha, @Param("empleadoId") Long empleadoId);

}
