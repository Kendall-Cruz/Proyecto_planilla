package com.ina.Proyecto_planilla;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.ina.Proyecto_planilla.Dao.IEmpleadoDao;
import com.ina.Proyecto_planilla.Dao.IPensionDao;
import com.ina.Proyecto_planilla.Entities.Pension;
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

    @Test
    void testVerificarIncapacidad() {
        double salarioBase = 1500000;
        Long empleadoId = 1L;
        LocalDate fechaPlanilla = LocalDate.of(2025, 4, 1);

        double monto = planillaService.verificarIncapacidades(empleadoId, fechaPlanilla, salarioBase);

        org.junit.jupiter.api.Assertions.assertTrue(monto >= 0);
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
        List<Pension> pensiones = pensionDao.findAllPensionesActivasMes(LocalDate.of(2025, 04, 29), 1L);
        Assertions.assertTrue(pensiones.size() > 0);
    }
    

}
