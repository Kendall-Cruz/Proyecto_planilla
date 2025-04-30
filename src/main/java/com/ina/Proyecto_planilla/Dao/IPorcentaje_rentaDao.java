package com.ina.Proyecto_planilla.Dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ina.Proyecto_planilla.Entities.Porcentaje_renta;

public interface IPorcentaje_rentaDao extends JpaRepository<Porcentaje_renta, Long> {

    @Query(value = "SELECT * FROM Porcentajes_renta p "
            + "WHERE p.anio_vigencia LIKE %:anio%", nativeQuery = true)
    List<Porcentaje_renta> getAllPorcentaje_renta(@Param("anio") String anio);

}
