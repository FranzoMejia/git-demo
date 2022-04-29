package com.admintool.app.service;

import com.admintool.app.domain.Pedido;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Pedido}.
 */
public interface PedidoService {
    /**
     * Save a pedido.
     *
     * @param pedido the entity to save.
     * @return the persisted entity.
     */
    Pedido save(Pedido pedido);

    /**
     * Updates a pedido.
     *
     * @param pedido the entity to update.
     * @return the persisted entity.
     */
    Pedido update(Pedido pedido);

    /**
     * Partially updates a pedido.
     *
     * @param pedido the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Pedido> partialUpdate(Pedido pedido);

    /**
     * Get all the pedidos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Pedido> findAll(Pageable pageable);

    /**
     * Get the "id" pedido.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Pedido> findOne(Long id);

    /**
     * Delete the "id" pedido.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
