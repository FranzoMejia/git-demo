package com.admintool.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.admintool.app.IntegrationTest;
import com.admintool.app.domain.Domicilio;
import com.admintool.app.repository.DomicilioRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link DomicilioResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DomicilioResourceIT {

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final String DEFAULT_UBICACION = "AAAAAAAAAA";
    private static final String UPDATED_UBICACION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/domicilios";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DomicilioRepository domicilioRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDomicilioMockMvc;

    private Domicilio domicilio;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Domicilio createEntity(EntityManager em) {
        Domicilio domicilio = new Domicilio().descripcion(DEFAULT_DESCRIPCION).ubicacion(DEFAULT_UBICACION);
        return domicilio;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Domicilio createUpdatedEntity(EntityManager em) {
        Domicilio domicilio = new Domicilio().descripcion(UPDATED_DESCRIPCION).ubicacion(UPDATED_UBICACION);
        return domicilio;
    }

    @BeforeEach
    public void initTest() {
        domicilio = createEntity(em);
    }

    @Test
    @Transactional
    void createDomicilio() throws Exception {
        int databaseSizeBeforeCreate = domicilioRepository.findAll().size();
        // Create the Domicilio
        restDomicilioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(domicilio)))
            .andExpect(status().isCreated());

        // Validate the Domicilio in the database
        List<Domicilio> domicilioList = domicilioRepository.findAll();
        assertThat(domicilioList).hasSize(databaseSizeBeforeCreate + 1);
        Domicilio testDomicilio = domicilioList.get(domicilioList.size() - 1);
        assertThat(testDomicilio.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testDomicilio.getUbicacion()).isEqualTo(DEFAULT_UBICACION);
    }

    @Test
    @Transactional
    void createDomicilioWithExistingId() throws Exception {
        // Create the Domicilio with an existing ID
        domicilio.setId(1L);

        int databaseSizeBeforeCreate = domicilioRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDomicilioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(domicilio)))
            .andExpect(status().isBadRequest());

        // Validate the Domicilio in the database
        List<Domicilio> domicilioList = domicilioRepository.findAll();
        assertThat(domicilioList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDescripcionIsRequired() throws Exception {
        int databaseSizeBeforeTest = domicilioRepository.findAll().size();
        // set the field null
        domicilio.setDescripcion(null);

        // Create the Domicilio, which fails.

        restDomicilioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(domicilio)))
            .andExpect(status().isBadRequest());

        List<Domicilio> domicilioList = domicilioRepository.findAll();
        assertThat(domicilioList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDomicilios() throws Exception {
        // Initialize the database
        domicilioRepository.saveAndFlush(domicilio);

        // Get all the domicilioList
        restDomicilioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(domicilio.getId().intValue())))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].ubicacion").value(hasItem(DEFAULT_UBICACION)));
    }

    @Test
    @Transactional
    void getDomicilio() throws Exception {
        // Initialize the database
        domicilioRepository.saveAndFlush(domicilio);

        // Get the domicilio
        restDomicilioMockMvc
            .perform(get(ENTITY_API_URL_ID, domicilio.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(domicilio.getId().intValue()))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION))
            .andExpect(jsonPath("$.ubicacion").value(DEFAULT_UBICACION));
    }

    @Test
    @Transactional
    void getNonExistingDomicilio() throws Exception {
        // Get the domicilio
        restDomicilioMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDomicilio() throws Exception {
        // Initialize the database
        domicilioRepository.saveAndFlush(domicilio);

        int databaseSizeBeforeUpdate = domicilioRepository.findAll().size();

        // Update the domicilio
        Domicilio updatedDomicilio = domicilioRepository.findById(domicilio.getId()).get();
        // Disconnect from session so that the updates on updatedDomicilio are not directly saved in db
        em.detach(updatedDomicilio);
        updatedDomicilio.descripcion(UPDATED_DESCRIPCION).ubicacion(UPDATED_UBICACION);

        restDomicilioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDomicilio.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDomicilio))
            )
            .andExpect(status().isOk());

        // Validate the Domicilio in the database
        List<Domicilio> domicilioList = domicilioRepository.findAll();
        assertThat(domicilioList).hasSize(databaseSizeBeforeUpdate);
        Domicilio testDomicilio = domicilioList.get(domicilioList.size() - 1);
        assertThat(testDomicilio.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testDomicilio.getUbicacion()).isEqualTo(UPDATED_UBICACION);
    }

    @Test
    @Transactional
    void putNonExistingDomicilio() throws Exception {
        int databaseSizeBeforeUpdate = domicilioRepository.findAll().size();
        domicilio.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDomicilioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, domicilio.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(domicilio))
            )
            .andExpect(status().isBadRequest());

        // Validate the Domicilio in the database
        List<Domicilio> domicilioList = domicilioRepository.findAll();
        assertThat(domicilioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDomicilio() throws Exception {
        int databaseSizeBeforeUpdate = domicilioRepository.findAll().size();
        domicilio.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDomicilioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(domicilio))
            )
            .andExpect(status().isBadRequest());

        // Validate the Domicilio in the database
        List<Domicilio> domicilioList = domicilioRepository.findAll();
        assertThat(domicilioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDomicilio() throws Exception {
        int databaseSizeBeforeUpdate = domicilioRepository.findAll().size();
        domicilio.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDomicilioMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(domicilio)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Domicilio in the database
        List<Domicilio> domicilioList = domicilioRepository.findAll();
        assertThat(domicilioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDomicilioWithPatch() throws Exception {
        // Initialize the database
        domicilioRepository.saveAndFlush(domicilio);

        int databaseSizeBeforeUpdate = domicilioRepository.findAll().size();

        // Update the domicilio using partial update
        Domicilio partialUpdatedDomicilio = new Domicilio();
        partialUpdatedDomicilio.setId(domicilio.getId());

        partialUpdatedDomicilio.descripcion(UPDATED_DESCRIPCION);

        restDomicilioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDomicilio.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDomicilio))
            )
            .andExpect(status().isOk());

        // Validate the Domicilio in the database
        List<Domicilio> domicilioList = domicilioRepository.findAll();
        assertThat(domicilioList).hasSize(databaseSizeBeforeUpdate);
        Domicilio testDomicilio = domicilioList.get(domicilioList.size() - 1);
        assertThat(testDomicilio.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testDomicilio.getUbicacion()).isEqualTo(DEFAULT_UBICACION);
    }

    @Test
    @Transactional
    void fullUpdateDomicilioWithPatch() throws Exception {
        // Initialize the database
        domicilioRepository.saveAndFlush(domicilio);

        int databaseSizeBeforeUpdate = domicilioRepository.findAll().size();

        // Update the domicilio using partial update
        Domicilio partialUpdatedDomicilio = new Domicilio();
        partialUpdatedDomicilio.setId(domicilio.getId());

        partialUpdatedDomicilio.descripcion(UPDATED_DESCRIPCION).ubicacion(UPDATED_UBICACION);

        restDomicilioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDomicilio.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDomicilio))
            )
            .andExpect(status().isOk());

        // Validate the Domicilio in the database
        List<Domicilio> domicilioList = domicilioRepository.findAll();
        assertThat(domicilioList).hasSize(databaseSizeBeforeUpdate);
        Domicilio testDomicilio = domicilioList.get(domicilioList.size() - 1);
        assertThat(testDomicilio.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testDomicilio.getUbicacion()).isEqualTo(UPDATED_UBICACION);
    }

    @Test
    @Transactional
    void patchNonExistingDomicilio() throws Exception {
        int databaseSizeBeforeUpdate = domicilioRepository.findAll().size();
        domicilio.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDomicilioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, domicilio.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(domicilio))
            )
            .andExpect(status().isBadRequest());

        // Validate the Domicilio in the database
        List<Domicilio> domicilioList = domicilioRepository.findAll();
        assertThat(domicilioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDomicilio() throws Exception {
        int databaseSizeBeforeUpdate = domicilioRepository.findAll().size();
        domicilio.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDomicilioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(domicilio))
            )
            .andExpect(status().isBadRequest());

        // Validate the Domicilio in the database
        List<Domicilio> domicilioList = domicilioRepository.findAll();
        assertThat(domicilioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDomicilio() throws Exception {
        int databaseSizeBeforeUpdate = domicilioRepository.findAll().size();
        domicilio.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDomicilioMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(domicilio))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Domicilio in the database
        List<Domicilio> domicilioList = domicilioRepository.findAll();
        assertThat(domicilioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDomicilio() throws Exception {
        // Initialize the database
        domicilioRepository.saveAndFlush(domicilio);

        int databaseSizeBeforeDelete = domicilioRepository.findAll().size();

        // Delete the domicilio
        restDomicilioMockMvc
            .perform(delete(ENTITY_API_URL_ID, domicilio.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Domicilio> domicilioList = domicilioRepository.findAll();
        assertThat(domicilioList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
