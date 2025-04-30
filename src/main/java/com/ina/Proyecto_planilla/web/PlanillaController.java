package com.ina.Proyecto_planilla.web;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ina.Proyecto_planilla.Entities.Detalle_planilla;
import com.ina.Proyecto_planilla.Entities.Empleado;
import com.ina.Proyecto_planilla.Entities.Planilla;
import com.ina.Proyecto_planilla.Services.IEmpleadoService;
import com.ina.Proyecto_planilla.Services.IPlanillaService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/planillas")
public class PlanillaController {

    @Autowired
    IEmpleadoService empleadoService;
    @Autowired
    IPlanillaService planillaService;

    @RequestMapping("/")
    public String index(Model model) {

        return "Planilla/index";
    }

    @GetMapping("/generar")
    public String generarPlanilla(Model model) {
        model.addAttribute("planilla", new Planilla());
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

    @PostMapping("/crearPlanilla")
    public String crearPlanilla(@Valid @ModelAttribute("planilla") Planilla planilla, BindingResult result, Model model) {

        if (result.hasErrors()) {
            return "Planilla/crearPlanilla"; 
        }

        Long res = planillaService.generarPlanilla(planilla);

        if(res > 0){
            return "Planilla/crearPlanilla"; //Hay que redigir ya sea a la misma pagina pero cargando en la parte de abajo los detalles o crear una pagina solo para mostrar 
        }
        else
            return "Planilla/crearPlanilla"; //Aqui se pondría el error
      
    }

}
