package com.ina.Proyecto_planilla.Dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ina.Proyecto_planilla.Entities.Deduccion;

public interface IDeduccionDao extends JpaRepository<Deduccion, Long> {
    
    @Query("SELECT d FROM Deduccion d Where d.activo = 1")
    List<Deduccion> findAllDuccionesAct();

    
}
