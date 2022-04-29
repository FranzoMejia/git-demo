package com.admintool.app.web.rest;

import com.admintool.app.domain.Catalogo;
import com.admintool.app.repository.CatalogoRepository;
import com.admintool.app.service.CatalogoService;
import com.admintool.app.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.admintool.app.domain.Catalogo}.
 */
@RestController
@RequestMapping("/api")
public class CatalogoResource {

    private final Logger log = LoggerFactory.getLogger(CatalogoResource.class);

    private static final String ENTITY_NAME = "catalogo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CatalogoService catalogoService;

    private final CatalogoRepository catalogoRepository;

    public CatalogoResource(CatalogoService catalogoService, CatalogoRepository catalogoRepository) {
        this.catalogoService = catalogoService;
        this.catalogoRepository = catalogoRepository;
    }

    /**
     * {@code POST  /catalogos} : Create a new catalogo.
     *
     * @param catalogo the catalogo to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new catalogo, or with status {@code 400 (Bad Request)} if the catalogo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/catalogos")
    public ResponseEntity<Catalogo> createCatalogo(@RequestBody Catalogo catalogo) throws URISyntaxException {
        log.debug("REST request to save Catalogo : {}", catalogo);
        if (catalogo.getId() != null) {
            throw new BadRequestAlertException("A new catalogo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Catalogo result = catalogoService.save(catalogo);
        return ResponseEntity
            .created(new URI("/api/catalogos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /catalogos/:id} : Updates an existing catalogo.
     *
     * @param id the id of the catalogo to save.
     * @param catalogo the catalogo to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated catalogo,
     * or with status {@code 400 (Bad Request)} if the catalogo is not valid,
     * or with status {@code 500 (Internal Server Error)} if the catalogo couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/catalogos/{id}")
    public ResponseEntity<Catalogo> updateCatalogo(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Catalogo catalogo
    ) throws URISyntaxException {
        log.debug("REST request to update Catalogo : {}, {}", id, catalogo);
        if (catalogo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, catalogo.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!catalogoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Catalogo result = catalogoService.update(catalogo);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, catalogo.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /catalogos/:id} : Partial updates given fields of an existing catalogo, field will ignore if it is null
     *
     * @param id the id of the catalogo to save.
     * @param catalogo the catalogo to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated catalogo,
     * or with status {@code 400 (Bad Request)} if the catalogo is not valid,
     * or with status {@code 404 (Not Found)} if the catalogo is not found,
     * or with status {@code 500 (Internal Server Error)} if the catalogo couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/catalogos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Catalogo> partialUpdateCatalogo(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Catalogo catalogo
    ) throws URISyntaxException {
        log.debug("REST request to partial update Catalogo partially : {}, {}", id, catalogo);
        if (catalogo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, catalogo.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!catalogoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Catalogo> result = catalogoService.partialUpdate(catalogo);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, catalogo.getId().toString())
        );
    }

    /**
     * {@code GET  /catalogos} : get all the catalogos.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of catalogos in body.
     */
    @GetMapping("/catalogos")
    public ResponseEntity<List<Catalogo>> getAllCatalogos(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Catalogos");
        Page<Catalogo> page = catalogoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /catalogos/:id} : get the "id" catalogo.
     *
     * @param id the id of the catalogo to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the catalogo, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/catalogos/{id}")
    public ResponseEntity<Catalogo> getCatalogo(@PathVariable Long id) {
        log.debug("REST request to get Catalogo : {}", id);
        Optional<Catalogo> catalogo = catalogoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(catalogo);
    }

    /**
     * {@code DELETE  /catalogos/:id} : delete the "id" catalogo.
     *
     * @param id the id of the catalogo to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/catalogos/{id}")
    public ResponseEntity<Void> deleteCatalogo(@PathVariable Long id) {
        log.debug("REST request to delete Catalogo : {}", id);
        catalogoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
