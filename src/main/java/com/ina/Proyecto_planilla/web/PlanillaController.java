package com.ina.Proyecto_planilla.web;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ina.Proyecto_planilla.Entities.Empleado;
import com.ina.Proyecto_planilla.Services.IEmpleadoService;

@Controller
@RequestMapping("/planillas")
public class PlanillaController {

    @Autowired
    IEmpleadoService empleadoService;

    @RequestMapping("/")
    public String index(Model model) {

        return "Planilla/index";
    }

    @GetMapping("/generar")
    public String generarPlanilla() {

        return "Planilla/crearPlanilla";
    }

    @GetMapping("/listarEmpleados")
    public String listarEmpleados(Model model) {

        List<Empleado> empleados = empleadoService.findAllEmpleadoActivoFecha(LocalDate.now());

        model.addAttribute("empleados", empleados);

        return "Planilla/listarEmpleadosAct";
    }

    @GetMapping("/filtrarEmpleados")
    public String filtrarEmpleados(@RequestParam(value = "fecha", required = false) LocalDate fecha, Model model) {
        model.addAttribute("fecha", fecha);
        if (fecha == null) {

            List<Empleado> empleados = empleadoService.findAllEmpleadoActivoFecha(LocalDate.now());
            model.addAttribute("empleados", empleados);
            return "Planilla/listarEmpleadosAct";
        }

        List<Empleado> empleados = empleadoService.findAllEmpleadoActivoFecha(fecha);
        model.addAttribute("empleados", empleados);
        return "Planilla/listarEmpleadosAct";
    }

}
