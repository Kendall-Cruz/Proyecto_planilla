package com.ina.Proyecto_planilla.Dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ina.Proyecto_planilla.Entities.Detalle_planilla;

public interface IDetalle_planillaDao extends JpaRepository<Detalle_planilla, Long> {
    
    
    @Query("SELECT d FROM Detalle_planilla d "
            + "LEFT JOIN FETCH d.detalles_deducciones "
            + "LEFT JOIN FETCH d.detalle_pagos "
            + "WHERE d.planilla.id_planilla = :idPlanilla")
    List<Detalle_planilla> obtenerDetallesConRelaciones(@Param("idPlanilla") Long idPlanilla);

    @Query("SELECT d FROM Detalle_planilla d "
            + "WHERE d.planilla.id_planilla = :idPlanilla")
    List<Detalle_planilla> findAllByPlanillaid_Planilla(Long idPlanilla);

    @Query("SELECT d FROM Detalle_planilla d WHERE d.id_detalle_planilla = :id")
    Detalle_planilla findById_detalle_planilla(@Param("id") Long id);


}
