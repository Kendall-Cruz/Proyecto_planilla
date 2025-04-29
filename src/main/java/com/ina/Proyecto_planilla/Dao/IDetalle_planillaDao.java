package com.ina.Proyecto_planilla.Dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ina.Proyecto_planilla.Entities.Detalle_planilla;

public interface IDetalle_planillaDao extends JpaRepository<Detalle_planilla, Long> {
    
}
