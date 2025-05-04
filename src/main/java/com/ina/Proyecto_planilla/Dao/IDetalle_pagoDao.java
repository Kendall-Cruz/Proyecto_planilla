package com.ina.Proyecto_planilla.Dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ina.Proyecto_planilla.Entities.Detalle_pago;

public interface IDetalle_pagoDao extends JpaRepository<Detalle_pago, Long> {

    @Query("SELECT d FROM Detalle_pago d WHERE d.detalle_planilla.id = :id")
    List<Detalle_pago> buscarPorDetallePlanillaId(@Param("id") Long id);

}
