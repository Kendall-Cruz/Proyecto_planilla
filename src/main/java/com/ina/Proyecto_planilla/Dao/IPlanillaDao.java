package com.ina.Proyecto_planilla.Dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ina.Proyecto_planilla.Entities.Planilla;

public interface IPlanillaDao extends JpaRepository<Planilla, Long> {
    
}
