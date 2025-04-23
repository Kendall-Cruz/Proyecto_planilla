package com.ina.Proyecto_planilla.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ina.Proyecto_planilla.Entities.Empleado;
import com.ina.Proyecto_planilla.Services.IEmpleadoService;


@Controller
@RequestMapping("/empleados")
public class EmpleadoController {

    @Autowired
    private IEmpleadoService empleadoService;
    
    @RequestMapping("/")
    public String listarEmpleados(Model model) {
        
        List<Empleado> empleados = empleadoService.listarEmpleados();

        if(!empleados.isEmpty()) {
            model.addAttribute("empleados", empleados);
        }

        return "Empleado/index"; 
    }

    
    
    
}
