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

import com.ina.Proyecto_planilla.Dto.DetallePlanillaDTO;
import com.ina.Proyecto_planilla.Entities.Empleado;
import com.ina.Proyecto_planilla.Entities.Planilla;
import com.ina.Proyecto_planilla.Services.IDetallePlanillaService;
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
    @Autowired
    IDetallePlanillaService detallePlanillaService;

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

        planilla.setFecha_creacion(LocalDate.now());

        if (result.hasErrors()) {
            return "Planilla/crearPlanilla";
        }

        Long res = planillaService.generarPlanilla(planilla);

        if (res > 0) {
            // Obtener la planilla generada
            Planilla nuevaPlanilla = planillaService.obtenerPlanillaPorId(res);
            if(nuevaPlanilla != null) {
                model.addAttribute("planilla", nuevaPlanilla);
            } else {
                model.addAttribute("error", "No se pudo obtener la planilla generada.");
                return "Planilla/crearPlanilla"; // Aquí se pondría el error
            }
            // Agregar la planilla al modelo para pasarla al siguiente método
            return "Planilla/listarDetalles"; //Hay que redigir ya sea a la misma pagina pero cargando en la parte de abajo los detalles o crear una pagina solo para mostrar 
        } else {
            return "Planilla/crearPlanilla"; //Aqui se pondría el error
        }

    }

    @GetMapping("/detalles")
    public String mostrarDetallesPlanilla(@ModelAttribute("planilla") Planilla planilla, Model model) {
        model.addAttribute("planilla", planilla);
        List<DetallePlanillaDTO> detallesPlanilla = detallePlanillaService.obtenerDetallesPorPlanilla(planilla.getId_planilla());
        model.addAttribute("detallesPlanilla", detallesPlanilla);

        return "Planilla/ListarDetalles";
    }

}
