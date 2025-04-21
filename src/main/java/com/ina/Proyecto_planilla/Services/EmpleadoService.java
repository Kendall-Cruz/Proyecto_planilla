package com.ina.Proyecto_planilla.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ina.Proyecto_planilla.Dao.IEmpleadoDao;
import com.ina.Proyecto_planilla.Entities.Empleado;

@Service
public class EmpleadoService implements IEmpleadoService {

    @Autowired
    private IEmpleadoDao empleadoDao;

    @Override
    public List<Empleado> listarEmpleados() {
        return empleadoDao.findAll();
    }
    
}
