package com.admintool.app.service;

import com.admintool.app.domain.Producto;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Producto}.
 */
public interface ProductoService {
    /**
     * Save a producto.
     *
     * @param producto the entity to save.
     * @return the persisted entity.
     */
    Producto save(Producto producto);

    /**
     * Updates a producto.
     *
     * @param producto the entity to update.
     * @return the persisted entity.
     */
    Producto update(Producto producto);

    /**
     * Partially updates a producto.
     *
     * @param producto the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Producto> partialUpdate(Producto producto);

    /**
     * Get all the productos.
     *
     * @return the list of entities.
     */
    List<Producto> findAll();

    /**
     * Get the "id" producto.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Producto> findOne(Long id);

    /**
     * Delete the "id" producto.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
