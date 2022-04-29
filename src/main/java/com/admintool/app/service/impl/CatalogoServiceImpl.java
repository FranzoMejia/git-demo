package com.admintool.app.service.impl;

import com.admintool.app.domain.Catalogo;
import com.admintool.app.repository.CatalogoRepository;
import com.admintool.app.service.CatalogoService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Catalogo}.
 */
@Service
@Transactional
public class CatalogoServiceImpl implements CatalogoService {

    private final Logger log = LoggerFactory.getLogger(CatalogoServiceImpl.class);

    private final CatalogoRepository catalogoRepository;

    public CatalogoServiceImpl(CatalogoRepository catalogoRepository) {
        this.catalogoRepository = catalogoRepository;
    }

    @Override
    public Catalogo save(Catalogo catalogo) {
        log.debug("Request to save Catalogo : {}", catalogo);
        return catalogoRepository.save(catalogo);
    }

    @Override
    public Catalogo update(Catalogo catalogo) {
        log.debug("Request to save Catalogo : {}", catalogo);
        return catalogoRepository.save(catalogo);
    }

    @Override
    public Optional<Catalogo> partialUpdate(Catalogo catalogo) {
        log.debug("Request to partially update Catalogo : {}", catalogo);

        return catalogoRepository
            .findById(catalogo.getId())
            .map(existingCatalogo -> {
                if (catalogo.getProductoDesc() != null) {
                    existingCatalogo.setProductoDesc(catalogo.getProductoDesc());
                }
                if (catalogo.getPrecioSugerido() != null) {
                    existingCatalogo.setPrecioSugerido(catalogo.getPrecioSugerido());
                }

                return existingCatalogo;
            })
            .map(catalogoRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Catalogo> findAll(Pageable pageable) {
        log.debug("Request to get all Catalogos");
        return catalogoRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Catalogo> findOne(Long id) {
        log.debug("Request to get Catalogo : {}", id);
        return catalogoRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Catalogo : {}", id);
        catalogoRepository.deleteById(id);
    }
}
