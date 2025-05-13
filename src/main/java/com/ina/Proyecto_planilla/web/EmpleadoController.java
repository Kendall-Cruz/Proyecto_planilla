package com.ina.Proyecto_planilla.web;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ina.Proyecto_planilla.Dao.IPuestoDao;
import com.ina.Proyecto_planilla.Dao.IPuesto_empleadoDao;
import com.ina.Proyecto_planilla.Entities.Empleado;
import com.ina.Proyecto_planilla.Entities.Puesto;
import com.ina.Proyecto_planilla.Entities.Puesto_empleado;
import com.ina.Proyecto_planilla.Services.IEmpleadoService;

import jakarta.servlet.http.HttpSession;





@Controller
@RequestMapping("/empleados")
public class EmpleadoController {

    @Autowired
    private IEmpleadoService empleadoService;

    @Autowired
    private IPuesto_empleadoDao puesto_empleadoDao;

    @Autowired
    private IPuestoDao puestoDao;

    @Autowired
    HttpSession session;

    private String mensaje;



    
    @RequestMapping("/")
    public String listarEmpleados(Model model) {
        
        List<Empleado> empleados = empleadoService.listarEmpleados();

        if(!empleados.isEmpty()) {
            model.addAttribute("empleados", empleados);
        }
        return "Empleado/index"; 
    }


    @GetMapping("/nombramientos")
    public String listarNombramientos(Model model) {
        List<Puesto_empleado> nombramientos = empleadoService.findAllNombramientos();

        model.addAttribute("nombramientos", nombramientos);

        return "Empleado/nombramientos";
    }

    @PostMapping("/eliminarNombramiento")
    public String eliminarNombramientos(@RequestParam("id") Long id , Model model) {

        Puesto_empleado nombramiento = puesto_empleadoDao.findById(id).orElse(null);

        if(nombramiento != null) {
            if(nombramiento.getFecha_vence().isBefore(LocalDate.now())) {
                puesto_empleadoDao.delete(nombramiento);
            } else {
                mensaje="No se puede eliminar un nombramiento que no ha vencido.";
                model.addAttribute("mensaje", mensaje);
                return "redirect:/empleados/nombramientos";
            }
        }
        mensaje = "El nombramiento no existe.";
        model.addAttribute("mensaje", mensaje );

        return "redirect:/empleados/nombramientos";
    }
    
    @GetMapping("/editarNombramiento/{id}")
    public String getMethodName(@PathVariable("id") Long id, Model model) {
        Puesto_empleado puesto_empleado = puesto_empleadoDao.findById(id).orElse(null);

        if(puesto_empleado != null) {
            model.addAttribute("puesto_empleado", puesto_empleado);
            return "Empleado/editarNombramiento";
        } else {
            model.addAttribute("mensaje", "El nombramiento no existe.");
            return "redirect:/planillas/";
        }
    }

    @PostMapping("/editarNombramiento")
    public String editarNombramiento(@RequestParam("id") Long id, @RequestParam("fecha_vence") LocalDate fecha_vence,  Model model) {
        Puesto_empleado puesto_empleado = puesto_empleadoDao.findById(id).orElse(null);

        if(puesto_empleado != null) {
            if(fecha_vence.isBefore(LocalDate.now())) {
                mensaje =  "La fecha de vencimiento no puede ser anterior a la fecha actual.";
                model.addAttribute("mensaje", mensaje);
                return "redirect:/empleados/nombramientos";
            }
            if(fecha_vence.isBefore(puesto_empleado.getFecha_nombramiento())) {
                mensaje = "La fecha de vencimiento no puede ser anterior a la fecha de inicio.";
                model.addAttribute("mensaje", mensaje );
                return "redirect:/empleados/nombramientos";
            }
            if(puesto_empleado.getFecha_vence().isBefore(LocalDate.now())) {
                mensaje = "No se puede editar un nombramiento que ya ha vencido.";
                model.addAttribute("mensaje", mensaje);
                return "redirect:/empleados/nombramientos";
            }

            puesto_empleado.setFecha_vence(fecha_vence);
            puesto_empleadoDao.save(puesto_empleado);
            return "redirect:/empleados/nombramientos";
        } else {
            mensaje = "El nombramiento no existe.";
            model.addAttribute("mensaje", mensaje);
            return "redirect:/planillas/";
        }
    }


    @GetMapping("/listarPuestos")
    public String listarPuestos (Model model) {

        List<Puesto> puestos = puestoDao.findAll();

        model.addAttribute("puestos", puestos);


        return "Empleado/listarPuestos";
    }

    @GetMapping("/listarEmpleados/{id}")
    public String listarEmpleados(@PathVariable("id") Long id, Model model) {

        session.setAttribute("idPuesto", id);

        List<Empleado> empleados = empleadoService.listarEmpleados();

        model.addAttribute("empleados", empleados);



        return "Empleado/listarEmpleados";
    }

    @GetMapping("/crearNombramiento/{id}")
    public String crearNombramiento(@PathVariable("id") Long id, Model model) {

        session.setAttribute("idEmpleado", id);

        Empleado empleado = empleadoService.findEmpleadoById(id);
        Puesto puesto = puestoDao.findById((Long) session.getAttribute("idPuesto")).orElse(null);

        model.addAttribute("puesto", puesto);
        model.addAttribute("empleado", empleado);


        return "Empleado/crearNombramiento";
    }

    @PostMapping("/crearNombramiento")
    public String crearNombramiento(@RequestParam("fecha_nombramiento") LocalDate fecha_nombramiento, @RequestParam("fecha_vence") LocalDate fecha_vence , Model model) {

        Long idEmpleado = (Long) session.getAttribute("idEmpleado");
        Long idPuesto = (Long) session.getAttribute("idPuesto");

        Empleado empleado = empleadoService.findEmpleadoById(idEmpleado);
        Puesto puesto = puestoDao.findById(idPuesto).orElse(null);

        if(fecha_nombramiento == null){
            mensaje = "La fecha del nombramiento no puede estar vacía.";
            model.addAttribute("mensaje", mensaje );
            return "redirect:/empleados/crearNombramiento/" + empleado.getId_empleado();
        }

        if(fecha_vence.isBefore(fecha_nombramiento)){
            mensaje = "La fecha de vencimiento no puede ser anterior a la fecha de inicio.";
            model.addAttribute("mensaje", mensaje );
            return "redirect:/empleados/crearNombramiento/" + empleado.getId_empleado();
        }

        if(!empleadoService.verificarPuestoActivoEntreFechas(empleado, fecha_nombramiento, fecha_vence)){
            Puesto_empleado puesto_empleado = new Puesto_empleado();
            puesto_empleado.setEmpleado(empleado);
            puesto_empleado.setPuesto(puesto);
            puesto_empleado.setFecha_nombramiento(fecha_nombramiento);
            puesto_empleado.setFecha_vence(fecha_vence);

            puesto_empleadoDao.save(puesto_empleado);

            return "redirect:/empleados/nombramientos";
        } else {
            mensaje = "El empleado ya tiene un nombramiento activo en ese rango de fechas.";
            model.addAttribute("mensaje", mensaje );
            return "Empleado/crearNombramiento/" + empleado.getId_empleado();
        }

    }
    
    
    
    
    

    
    
    
}
