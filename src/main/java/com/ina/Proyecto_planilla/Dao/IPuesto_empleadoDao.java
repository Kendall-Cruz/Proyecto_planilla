package com.ina.Proyecto_planilla.Dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ina.Proyecto_planilla.Entities.Puesto_empleado;

public interface IPuesto_empleadoDao extends JpaRepository<Puesto_empleado, Long> {
    
}
