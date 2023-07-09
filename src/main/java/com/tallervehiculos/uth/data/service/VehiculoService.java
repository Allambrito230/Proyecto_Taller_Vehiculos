package com.tallervehiculos.uth.data.service;

import com.tallervehiculos.uth.data.entity.Vehiculo;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class VehiculoService {

    private final VehiculoRepository repository;

    public VehiculoService(VehiculoRepository repository) {
        this.repository = repository;
    }

    public Optional<Vehiculo> get(Long id) {
        return repository.findById(id);
    }

    public Vehiculo update(Vehiculo entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Page<Vehiculo> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<Vehiculo> list(Pageable pageable, Specification<Vehiculo> filter) {
        return repository.findAll(filter, pageable);
    }

    public int count() {
        return (int) repository.count();
    }

}
