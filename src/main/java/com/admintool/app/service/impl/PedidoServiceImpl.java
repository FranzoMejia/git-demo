package com.admintool.app.service.impl;

import com.admintool.app.domain.Pedido;
import com.admintool.app.repository.PedidoRepository;
import com.admintool.app.service.PedidoService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Pedido}.
 */
@Service
@Transactional
public class PedidoServiceImpl implements PedidoService {

    private final Logger log = LoggerFactory.getLogger(PedidoServiceImpl.class);

    private final PedidoRepository pedidoRepository;

    public PedidoServiceImpl(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    @Override
    public Pedido save(Pedido pedido) {
        log.debug("Request to save Pedido : {}", pedido);
        return pedidoRepository.save(pedido);
    }

    @Override
    public Pedido update(Pedido pedido) {
        log.debug("Request to save Pedido : {}", pedido);
        return pedidoRepository.save(pedido);
    }

    @Override
    public Optional<Pedido> partialUpdate(Pedido pedido) {
        log.debug("Request to partially update Pedido : {}", pedido);

        return pedidoRepository
            .findById(pedido.getId())
            .map(existingPedido -> {
                if (pedido.getTotal() != null) {
                    existingPedido.setTotal(pedido.getTotal());
                }
                if (pedido.getFechaGenerado() != null) {
                    existingPedido.setFechaGenerado(pedido.getFechaGenerado());
                }
                if (pedido.getFechaEntrega() != null) {
                    existingPedido.setFechaEntrega(pedido.getFechaEntrega());
                }
                if (pedido.getPreparado() != null) {
                    existingPedido.setPreparado(pedido.getPreparado());
                }
                if (pedido.getEntregado() != null) {
                    existingPedido.setEntregado(pedido.getEntregado());
                }
                if (pedido.getForaneo() != null) {
                    existingPedido.setForaneo(pedido.getForaneo());
                }

                return existingPedido;
            })
            .map(pedidoRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Pedido> findAll(Pageable pageable) {
        log.debug("Request to get all Pedidos");
        return pedidoRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Pedido> findOne(Long id) {
        log.debug("Request to get Pedido : {}", id);
        return pedidoRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Pedido : {}", id);
        pedidoRepository.deleteById(id);
    }
}
