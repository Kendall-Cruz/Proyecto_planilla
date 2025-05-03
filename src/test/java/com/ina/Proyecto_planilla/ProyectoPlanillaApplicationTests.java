package com.ina.Proyecto_planilla;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.ina.Proyecto_planilla.Dao.IEmpleadoDao;
import com.ina.Proyecto_planilla.Dao.IIncapacidadDao;
import com.ina.Proyecto_planilla.Dao.IPensionDao;
import com.ina.Proyecto_planilla.Dao.IPorcentaje_rentaDao;
import com.ina.Proyecto_planilla.Entities.Incapacidad;
import com.ina.Proyecto_planilla.Entities.Pension;
import com.ina.Proyecto_planilla.Entities.Planilla;
import com.ina.Proyecto_planilla.Entities.Porcentaje_renta;
import com.ina.Proyecto_planilla.Services.PlanillaService;

@SpringBootTest
class ProyectoPlanillaApplicationTests {

	@Test
	void contextLoads() {
	}

	@Autowired
    private PlanillaService planillaService;
    @Autowired
    private IEmpleadoDao empleadoDao;
    @Autowired
    private IPensionDao pensionDao;
    @Autowired
    private IPorcentaje_rentaDao porcentaje_rentaDao;
    @Autowired
    private IIncapacidadDao incapacidadDao;

    @Test
    void testVerificarIncapacidad() {
        double salarioBase = 1800000;
        Long empleadoId = 1L;
        LocalDate fechaPlanilla = LocalDate.of(2025, 3, 12);
        List<Incapacidad> incapacidades = incapacidadDao.findByEmpleadoIdAndFecha(empleadoId, fechaPlanilla);

        double monto = planillaService.verificarIncapacidades(fechaPlanilla, salarioBase, incapacidades);

        org.junit.jupiter.api.Assertions.assertTrue(monto >= 0);
    } 

    @Test
    void testVerificarPuestoEmpleado() {
        int anios = empleadoDao.countTotalDiasTrabajados(1L) / 365;

        Assertions.assertNotNull(anios);
    }

    @Test
    void testDiasTrabajados(){
        long diasTrabajados = empleadoDao.countTotalDiasTrabajados(1L);
        Assertions.assertTrue(diasTrabajados > 0);
    }

    @Test
    void testPuntosCarrera(){
        int puntos_carrera = empleadoDao.getPuntosCarreraByEmpleadoId(4L);
        Assertions.assertTrue(puntos_carrera > 0 );
    }

    @Test
    void testPensiones(){
        List<Pension> pensiones = pensionDao.findAllPensionesActivasMes(LocalDate.of(2025, 03, 29), 1L);
        Assertions.assertTrue(pensiones.size() > 0);
    }

    @Test
    void testPorcentajesRenta(){
        List<Porcentaje_renta> pr = porcentaje_rentaDao.getAllPorcentaje_renta("2025");

        Assertions.assertTrue(pr.size() > 0);
    }
    
    @Test
    void testPorcentajesRentaFunc(){
        double resultado = planillaService.calcularPorcentajeDeRenta(4800000, LocalDate.now());

        Assertions.assertTrue(resultado > 0);
    }
    @Test 
    void testGenerarPlanilla(){

        Planilla planilla = new Planilla();
        planilla.setFecha_creacion(LocalDate.now());
        planilla.setFecha_pago_mensual(LocalDate.of(2025, 3, 29));
        planilla.setFecha_pago_quincenal(LocalDate.of(2025, 3, 15));
        planilla.setFecha_planilla(LocalDate.of(2025, 3, 12));
        planilla.setTipo_planilla("Ordinaria");
        
        Long res = planillaService.generarPlanilla(planilla);

        Assertions.assertTrue(res > 0);
    }

}
