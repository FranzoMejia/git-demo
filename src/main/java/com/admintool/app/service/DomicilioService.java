package com.admintool.app.service;

import com.admintool.app.domain.Domicilio;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Domicilio}.
 */
public interface DomicilioService {
    /**
     * Save a domicilio.
     *
     * @param domicilio the entity to save.
     * @return the persisted entity.
     */
    Domicilio save(Domicilio domicilio);

    /**
     * Updates a domicilio.
     *
     * @param domicilio the entity to update.
     * @return the persisted entity.
     */
    Domicilio update(Domicilio domicilio);

    /**
     * Partially updates a domicilio.
     *
     * @param domicilio the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Domicilio> partialUpdate(Domicilio domicilio);

    /**
     * Get all the domicilios.
     *
     * @return the list of entities.
     */
    List<Domicilio> findAll();
    /**
     * Get all the Domicilio where Pedido is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<Domicilio> findAllWherePedidoIsNull();

    /**
     * Get the "id" domicilio.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Domicilio> findOne(Long id);

    /**
     * Delete the "id" domicilio.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
