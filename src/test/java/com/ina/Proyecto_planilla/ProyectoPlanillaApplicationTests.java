package com.ina.Proyecto_planilla;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.ina.Proyecto_planilla.Services.PlanillaService;

@SpringBootTest
class ProyectoPlanillaApplicationTests {

	@Test
	void contextLoads() {
	}

	 @Autowired
    private PlanillaService planillaService;

    @Test
    void testVerificarIncapacidad() {
        double salarioBase = 1500000;
        Long empleadoId = 1L;
        LocalDate fechaPlanilla = LocalDate.of(2025, 4, 1);

        double monto = planillaService.verificarIncapacidades(empleadoId, fechaPlanilla, salarioBase);

        org.junit.jupiter.api.Assertions.assertTrue(monto >= 0);
    }

}
