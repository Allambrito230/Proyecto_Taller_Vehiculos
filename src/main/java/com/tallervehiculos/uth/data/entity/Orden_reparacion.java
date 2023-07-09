package com.tallervehiculos.uth.data.entity;

import jakarta.persistence.Entity;

@Entity
public class Orden_reparacion extends AbstractEntity {

    private Integer id_orden;
    private Integer vehiculo_id;
    private String descripcion_problema;
    private String estado_reparacion;

    public Integer getId_orden() {
        return id_orden;
    }
    public void setId_orden(Integer id_orden) {
        this.id_orden = id_orden;
    }
    public Integer getVehiculo_id() {
        return vehiculo_id;
    }
    public void setVehiculo_id(Integer vehiculo_id) {
        this.vehiculo_id = vehiculo_id;
    }
    public String getDescripcion_problema() {
        return descripcion_problema;
    }
    public void setDescripcion_problema(String descripcion_problema) {
        this.descripcion_problema = descripcion_problema;
    }
    public String getEstado_reparacion() {
        return estado_reparacion;
    }
    public void setEstado_reparacion(String estado_reparacion) {
        this.estado_reparacion = estado_reparacion;
    }

}
