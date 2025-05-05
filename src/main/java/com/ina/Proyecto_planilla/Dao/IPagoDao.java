package com.ina.Proyecto_planilla.Dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ina.Proyecto_planilla.Entities.Pago;

public interface IPagoDao extends JpaRepository<Pago, Long> {
    List<Pago> findByActivoTrue();
}
