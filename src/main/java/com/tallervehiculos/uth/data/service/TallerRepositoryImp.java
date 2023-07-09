package com.tallervehiculos.uth.data.service;

import java.io.IOException;

import com.tallervehiculos.uth.data.entity.ResponseVehiculo;

import retrofit2.Call;
import retrofit2.Response;

public class TallerRepositoryImp {

	private static TallerRepositoryImp instance;
	private RepositoryTaller vehiculo;
	
	private TallerRepositoryImp(String url, Long timeout) {
		this.vehiculo = new RepositoryTaller(url, timeout);
	}
	
	//IMPLEMENTANDO PATRÓN SINGLETON
	public static TallerRepositoryImp getInstance(String url, Long timeout) {
		if(instance == null) {
			synchronized (TallerRepositoryImp.class) {
				if(instance == null) {
					instance = new TallerRepositoryImp(url, timeout);
				}
			}
		}
		return instance;
	}
	
	public ResponseVehiculo getvehiculo() throws IOException {
		Call<ResponseVehiculo> call = vehiculo.getDatabaseService().obtenerEmpleados();
		Response<ResponseVehiculo> response = call.execute(); //AQUI ES DONDE SE CONSULTA A LA URL DE LA BASE DE DATOS
		if(response.isSuccessful()){
			return response.body();
		}else {
			return null;
		}
	}
	
}
