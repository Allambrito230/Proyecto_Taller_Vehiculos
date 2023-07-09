package com.tallervehiculos.uth.data.service;

import com.tallervehiculos.uth.data.entity.Orden_reparacion;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class Orden_reparacionService {

    private final Orden_reparacionRepository repository;

    public Orden_reparacionService(Orden_reparacionRepository repository) {
        this.repository = repository;
    }

    public Optional<Orden_reparacion> get(Long id) {
        return repository.findById(id);
    }

    public Orden_reparacion update(Orden_reparacion entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Page<Orden_reparacion> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<Orden_reparacion> list(Pageable pageable, Specification<Orden_reparacion> filter) {
        return repository.findAll(filter, pageable);
    }

    public int count() {
        return (int) repository.count();
    }

}
