package com.tallervehiculos.uth.data.controller;

import java.io.IOException;

import com.tallervehiculos.uth.data.service.TallerRepositoryImp;
import com.tallervehiculos.uth.views.registrodevehículo.registrodevehiculoViewModel;


public class OrdenVehiculos_InteractorImp {

	private TallerRepositoryImp modelo;
	private registrodevehiculoViewModel vista;
	
	public OrdenVehiculos_InteractorImp(registrodevehiculoViewModel vista) {
		super();
		this.modelo = TallerRepositoryImp.getInstance("https://apex.oracle.com/", 600000L);
		this.vista = vista;
	}

	@Override
	public void consultarVehiculo() {
		try {
			ResponseVehiculo respuesta = this.modelo.getvehiculo();
			this.vista.refrescarGridVehiculos(respuesta.getItems());
		}catch(IOException e) {
			e.printStackTrace();
		}
		
	}
}
