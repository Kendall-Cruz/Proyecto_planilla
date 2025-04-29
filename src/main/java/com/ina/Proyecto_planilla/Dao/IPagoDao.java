package com.ina.Proyecto_planilla.Dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ina.Proyecto_planilla.Entities.Pago;

public interface IPagoDao extends JpaRepository<Pago, Long> {
    
    @Query("SELECT p FROM Pago p WHERE p.activo = 1")
    List<Pago> findAllPagosActivos();
    

    
}
