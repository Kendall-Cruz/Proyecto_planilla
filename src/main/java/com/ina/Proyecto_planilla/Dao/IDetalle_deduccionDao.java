package com.ina.Proyecto_planilla.Dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ina.Proyecto_planilla.Entities.Detalle_deduccion;

public interface IDetalle_deduccionDao extends JpaRepository<Detalle_deduccion, Long> {
    @Query("SELECT d FROM Detalle_deduccion d WHERE d.detalle_planilla.id = :id")
    List<Detalle_deduccion> buscarPorDetallePlanillaId(@Param("id") Long id);
}
