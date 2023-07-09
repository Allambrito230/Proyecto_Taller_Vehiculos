package com.tallervehiculos.uth.data.service;

import com.tallervehiculos.uth.data.entity.Orden_reparacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface Orden_reparacionRepository
        extends
            JpaRepository<Orden_reparacion, Long>,
            JpaSpecificationExecutor<Orden_reparacion> {

}
