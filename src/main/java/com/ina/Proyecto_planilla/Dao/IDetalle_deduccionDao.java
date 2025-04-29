package com.ina.Proyecto_planilla.Dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ina.Proyecto_planilla.Entities.Detalle_deduccion;

public interface IDetalle_deduccionDao extends JpaRepository<Detalle_deduccion, Long> {
    
}
