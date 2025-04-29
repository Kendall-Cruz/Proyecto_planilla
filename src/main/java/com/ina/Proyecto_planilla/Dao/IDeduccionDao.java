package com.ina.Proyecto_planilla.Dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ina.Proyecto_planilla.Entities.Deduccion;

public interface IDeduccionDao extends JpaRepository<Deduccion, Long> {
    
    List<Deduccion> findByActivoTrue();
}
