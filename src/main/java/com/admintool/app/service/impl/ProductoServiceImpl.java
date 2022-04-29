package com.admintool.app.service.impl;

import com.admintool.app.domain.Producto;
import com.admintool.app.repository.ProductoRepository;
import com.admintool.app.service.ProductoService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Producto}.
 */
@Service
@Transactional
public class ProductoServiceImpl implements ProductoService {

    private final Logger log = LoggerFactory.getLogger(ProductoServiceImpl.class);

    private final ProductoRepository productoRepository;

    public ProductoServiceImpl(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Override
    public Producto save(Producto producto) {
        log.debug("Request to save Producto : {}", producto);
        return productoRepository.save(producto);
    }

    @Override
    public Producto update(Producto producto) {
        log.debug("Request to save Producto : {}", producto);
        return productoRepository.save(producto);
    }

    @Override
    public Optional<Producto> partialUpdate(Producto producto) {
        log.debug("Request to partially update Producto : {}", producto);

        return productoRepository
            .findById(producto.getId())
            .map(existingProducto -> {
                if (producto.getProducto() != null) {
                    existingProducto.setProducto(producto.getProducto());
                }
                if (producto.getPrecio() != null) {
                    existingProducto.setPrecio(producto.getPrecio());
                }

                return existingProducto;
            })
            .map(productoRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Producto> findAll() {
        log.debug("Request to get all Productos");
        return productoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Producto> findOne(Long id) {
        log.debug("Request to get Producto : {}", id);
        return productoRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Producto : {}", id);
        productoRepository.deleteById(id);
    }
}
