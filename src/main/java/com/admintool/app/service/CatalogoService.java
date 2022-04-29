package com.admintool.app.service;

import com.admintool.app.domain.Catalogo;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Catalogo}.
 */
public interface CatalogoService {
    /**
     * Save a catalogo.
     *
     * @param catalogo the entity to save.
     * @return the persisted entity.
     */
    Catalogo save(Catalogo catalogo);

    /**
     * Updates a catalogo.
     *
     * @param catalogo the entity to update.
     * @return the persisted entity.
     */
    Catalogo update(Catalogo catalogo);

    /**
     * Partially updates a catalogo.
     *
     * @param catalogo the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Catalogo> partialUpdate(Catalogo catalogo);

    /**
     * Get all the catalogos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Catalogo> findAll(Pageable pageable);

    /**
     * Get the "id" catalogo.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Catalogo> findOne(Long id);

    /**
     * Delete the "id" catalogo.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
