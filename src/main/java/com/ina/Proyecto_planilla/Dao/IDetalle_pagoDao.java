package com.ina.Proyecto_planilla.Dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ina.Proyecto_planilla.Entities.Detalle_pago;

public interface IDetalle_pagoDao extends JpaRepository<Detalle_pago, Long> {

    
}
