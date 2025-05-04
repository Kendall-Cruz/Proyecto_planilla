package com.ina.Proyecto_planilla.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.ina.Proyecto_planilla.Dao.IDetalle_deduccionDao;
import com.ina.Proyecto_planilla.Dao.IDetalle_pagoDao;
import com.ina.Proyecto_planilla.Entities.Detalle_deduccion;
import com.ina.Proyecto_planilla.Entities.Detalle_pago;
import com.ina.Proyecto_planilla.ViewModels.DetallePlanillaVM;

public class Detalle_planillaService {
     @Autowired
    private IDetalle_deduccionDao detalleDeduccionDao;
    
    @Autowired
    private IDetalle_pagoDao detallePagoDao;
    
    public DetallePlanillaVM getDetalleCompleto(Long idDetallePlanilla) {
        // Obtener el ViewModel básico
        DetallePlanillaVM viewModel = detallePlanillaViewModelRepository.findDetallePlanillaById(idDetallePlanilla);
        
        if (viewModel != null) {
            // Cargar las relaciones
            List<Detalle_deduccion> deducciones = detalleDeduccionDao.findByDetallePlanillaId(idDetallePlanilla);
            List<Detalle_pago> pagos = detallePagoDao.findByDetallePlanillaId(idDetallePlanilla);
            
            viewModel.cargarListas(deducciones, pagos);
        }
        
        return viewModel;
    }
    
    public List<DetallePlanillaVM> getDetallesPorPlanilla(Long idPlanilla) {
        List<DetallePlanillaVM> viewModels = detallePlanillaViewModelRepository.findDetallesPlanillaByPlanillaId(idPlanilla);
        
        // Cargar las relaciones para cada ViewModel
        for (DetallePlanillaVM vm : viewModels) {
            List<Detalle_deduccion> deducciones = detalleDeduccionDao.findByDetallePlanillaId(vm.getIdDetallePlanilla());
            List<Detalle_pago> pagos = detallePagoDao.findByDetallePlanillaId(vm.getIdDetallePlanilla());
            
            vm.cargarListas(deducciones, pagos);
        }
        
        return viewModels;
    }
}
