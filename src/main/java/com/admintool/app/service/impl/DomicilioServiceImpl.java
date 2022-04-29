package com.admintool.app.service.impl;

import com.admintool.app.domain.Domicilio;
import com.admintool.app.repository.DomicilioRepository;
import com.admintool.app.service.DomicilioService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Domicilio}.
 */
@Service
@Transactional
public class DomicilioServiceImpl implements DomicilioService {

    private final Logger log = LoggerFactory.getLogger(DomicilioServiceImpl.class);

    private final DomicilioRepository domicilioRepository;

    public DomicilioServiceImpl(DomicilioRepository domicilioRepository) {
        this.domicilioRepository = domicilioRepository;
    }

    @Override
    public Domicilio save(Domicilio domicilio) {
        log.debug("Request to save Domicilio : {}", domicilio);
        return domicilioRepository.save(domicilio);
    }

    @Override
    public Domicilio update(Domicilio domicilio) {
        log.debug("Request to save Domicilio : {}", domicilio);
        return domicilioRepository.save(domicilio);
    }

    @Override
    public Optional<Domicilio> partialUpdate(Domicilio domicilio) {
        log.debug("Request to partially update Domicilio : {}", domicilio);

        return domicilioRepository
            .findById(domicilio.getId())
            .map(existingDomicilio -> {
                if (domicilio.getDescripcion() != null) {
                    existingDomicilio.setDescripcion(domicilio.getDescripcion());
                }
                if (domicilio.getUbicacion() != null) {
                    existingDomicilio.setUbicacion(domicilio.getUbicacion());
                }

                return existingDomicilio;
            })
            .map(domicilioRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Domicilio> findAll() {
        log.debug("Request to get all Domicilios");
        return domicilioRepository.findAll();
    }

    /**
     *  Get all the domicilios where Pedido is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Domicilio> findAllWherePedidoIsNull() {
        log.debug("Request to get all domicilios where Pedido is null");
        return StreamSupport
            .stream(domicilioRepository.findAll().spliterator(), false)
            .filter(domicilio -> domicilio.getPedido() == null)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Domicilio> findOne(Long id) {
        log.debug("Request to get Domicilio : {}", id);
        return domicilioRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Domicilio : {}", id);
        domicilioRepository.deleteById(id);
    }
}
