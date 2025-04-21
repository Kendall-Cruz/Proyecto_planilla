package com.ina.Proyecto_planilla.Dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ina.Proyecto_planilla.Entities.Empleado;

public interface IEmpleadoDao extends JpaRepository<Empleado, Long> {
    
    Empleado findByNombre(String nombre);
    Empleado findById(long id);
    
}
