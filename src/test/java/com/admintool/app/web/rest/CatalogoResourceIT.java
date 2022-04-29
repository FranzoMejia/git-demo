package com.admintool.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.admintool.app.IntegrationTest;
import com.admintool.app.domain.Catalogo;
import com.admintool.app.repository.CatalogoRepository;
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
 * Integration tests for the {@link CatalogoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CatalogoResourceIT {

    private static final String DEFAULT_PRODUCTO_DESC = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCTO_DESC = "BBBBBBBBBB";

    private static final Float DEFAULT_PRECIO_SUGERIDO = 1F;
    private static final Float UPDATED_PRECIO_SUGERIDO = 2F;

    private static final String ENTITY_API_URL = "/api/catalogos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CatalogoRepository catalogoRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCatalogoMockMvc;

    private Catalogo catalogo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Catalogo createEntity(EntityManager em) {
        Catalogo catalogo = new Catalogo().productoDesc(DEFAULT_PRODUCTO_DESC).precioSugerido(DEFAULT_PRECIO_SUGERIDO);
        return catalogo;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Catalogo createUpdatedEntity(EntityManager em) {
        Catalogo catalogo = new Catalogo().productoDesc(UPDATED_PRODUCTO_DESC).precioSugerido(UPDATED_PRECIO_SUGERIDO);
        return catalogo;
    }

    @BeforeEach
    public void initTest() {
        catalogo = createEntity(em);
    }

    @Test
    @Transactional
    void createCatalogo() throws Exception {
        int databaseSizeBeforeCreate = catalogoRepository.findAll().size();
        // Create the Catalogo
        restCatalogoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(catalogo)))
            .andExpect(status().isCreated());

        // Validate the Catalogo in the database
        List<Catalogo> catalogoList = catalogoRepository.findAll();
        assertThat(catalogoList).hasSize(databaseSizeBeforeCreate + 1);
        Catalogo testCatalogo = catalogoList.get(catalogoList.size() - 1);
        assertThat(testCatalogo.getProductoDesc()).isEqualTo(DEFAULT_PRODUCTO_DESC);
        assertThat(testCatalogo.getPrecioSugerido()).isEqualTo(DEFAULT_PRECIO_SUGERIDO);
    }

    @Test
    @Transactional
    void createCatalogoWithExistingId() throws Exception {
        // Create the Catalogo with an existing ID
        catalogo.setId(1L);

        int databaseSizeBeforeCreate = catalogoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCatalogoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(catalogo)))
            .andExpect(status().isBadRequest());

        // Validate the Catalogo in the database
        List<Catalogo> catalogoList = catalogoRepository.findAll();
        assertThat(catalogoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCatalogos() throws Exception {
        // Initialize the database
        catalogoRepository.saveAndFlush(catalogo);

        // Get all the catalogoList
        restCatalogoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(catalogo.getId().intValue())))
            .andExpect(jsonPath("$.[*].productoDesc").value(hasItem(DEFAULT_PRODUCTO_DESC)))
            .andExpect(jsonPath("$.[*].precioSugerido").value(hasItem(DEFAULT_PRECIO_SUGERIDO.doubleValue())));
    }

    @Test
    @Transactional
    void getCatalogo() throws Exception {
        // Initialize the database
        catalogoRepository.saveAndFlush(catalogo);

        // Get the catalogo
        restCatalogoMockMvc
            .perform(get(ENTITY_API_URL_ID, catalogo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(catalogo.getId().intValue()))
            .andExpect(jsonPath("$.productoDesc").value(DEFAULT_PRODUCTO_DESC))
            .andExpect(jsonPath("$.precioSugerido").value(DEFAULT_PRECIO_SUGERIDO.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingCatalogo() throws Exception {
        // Get the catalogo
        restCatalogoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCatalogo() throws Exception {
        // Initialize the database
        catalogoRepository.saveAndFlush(catalogo);

        int databaseSizeBeforeUpdate = catalogoRepository.findAll().size();

        // Update the catalogo
        Catalogo updatedCatalogo = catalogoRepository.findById(catalogo.getId()).get();
        // Disconnect from session so that the updates on updatedCatalogo are not directly saved in db
        em.detach(updatedCatalogo);
        updatedCatalogo.productoDesc(UPDATED_PRODUCTO_DESC).precioSugerido(UPDATED_PRECIO_SUGERIDO);

        restCatalogoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCatalogo.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCatalogo))
            )
            .andExpect(status().isOk());

        // Validate the Catalogo in the database
        List<Catalogo> catalogoList = catalogoRepository.findAll();
        assertThat(catalogoList).hasSize(databaseSizeBeforeUpdate);
        Catalogo testCatalogo = catalogoList.get(catalogoList.size() - 1);
        assertThat(testCatalogo.getProductoDesc()).isEqualTo(UPDATED_PRODUCTO_DESC);
        assertThat(testCatalogo.getPrecioSugerido()).isEqualTo(UPDATED_PRECIO_SUGERIDO);
    }

    @Test
    @Transactional
    void putNonExistingCatalogo() throws Exception {
        int databaseSizeBeforeUpdate = catalogoRepository.findAll().size();
        catalogo.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCatalogoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, catalogo.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(catalogo))
            )
            .andExpect(status().isBadRequest());

        // Validate the Catalogo in the database
        List<Catalogo> catalogoList = catalogoRepository.findAll();
        assertThat(catalogoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCatalogo() throws Exception {
        int databaseSizeBeforeUpdate = catalogoRepository.findAll().size();
        catalogo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCatalogoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(catalogo))
            )
            .andExpect(status().isBadRequest());

        // Validate the Catalogo in the database
        List<Catalogo> catalogoList = catalogoRepository.findAll();
        assertThat(catalogoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCatalogo() throws Exception {
        int databaseSizeBeforeUpdate = catalogoRepository.findAll().size();
        catalogo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCatalogoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(catalogo)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Catalogo in the database
        List<Catalogo> catalogoList = catalogoRepository.findAll();
        assertThat(catalogoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCatalogoWithPatch() throws Exception {
        // Initialize the database
        catalogoRepository.saveAndFlush(catalogo);

        int databaseSizeBeforeUpdate = catalogoRepository.findAll().size();

        // Update the catalogo using partial update
        Catalogo partialUpdatedCatalogo = new Catalogo();
        partialUpdatedCatalogo.setId(catalogo.getId());

        partialUpdatedCatalogo.productoDesc(UPDATED_PRODUCTO_DESC);

        restCatalogoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCatalogo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCatalogo))
            )
            .andExpect(status().isOk());

        // Validate the Catalogo in the database
        List<Catalogo> catalogoList = catalogoRepository.findAll();
        assertThat(catalogoList).hasSize(databaseSizeBeforeUpdate);
        Catalogo testCatalogo = catalogoList.get(catalogoList.size() - 1);
        assertThat(testCatalogo.getProductoDesc()).isEqualTo(UPDATED_PRODUCTO_DESC);
        assertThat(testCatalogo.getPrecioSugerido()).isEqualTo(DEFAULT_PRECIO_SUGERIDO);
    }

    @Test
    @Transactional
    void fullUpdateCatalogoWithPatch() throws Exception {
        // Initialize the database
        catalogoRepository.saveAndFlush(catalogo);

        int databaseSizeBeforeUpdate = catalogoRepository.findAll().size();

        // Update the catalogo using partial update
        Catalogo partialUpdatedCatalogo = new Catalogo();
        partialUpdatedCatalogo.setId(catalogo.getId());

        partialUpdatedCatalogo.productoDesc(UPDATED_PRODUCTO_DESC).precioSugerido(UPDATED_PRECIO_SUGERIDO);

        restCatalogoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCatalogo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCatalogo))
            )
            .andExpect(status().isOk());

        // Validate the Catalogo in the database
        List<Catalogo> catalogoList = catalogoRepository.findAll();
        assertThat(catalogoList).hasSize(databaseSizeBeforeUpdate);
        Catalogo testCatalogo = catalogoList.get(catalogoList.size() - 1);
        assertThat(testCatalogo.getProductoDesc()).isEqualTo(UPDATED_PRODUCTO_DESC);
        assertThat(testCatalogo.getPrecioSugerido()).isEqualTo(UPDATED_PRECIO_SUGERIDO);
    }

    @Test
    @Transactional
    void patchNonExistingCatalogo() throws Exception {
        int databaseSizeBeforeUpdate = catalogoRepository.findAll().size();
        catalogo.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCatalogoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, catalogo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(catalogo))
            )
            .andExpect(status().isBadRequest());

        // Validate the Catalogo in the database
        List<Catalogo> catalogoList = catalogoRepository.findAll();
        assertThat(catalogoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCatalogo() throws Exception {
        int databaseSizeBeforeUpdate = catalogoRepository.findAll().size();
        catalogo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCatalogoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(catalogo))
            )
            .andExpect(status().isBadRequest());

        // Validate the Catalogo in the database
        List<Catalogo> catalogoList = catalogoRepository.findAll();
        assertThat(catalogoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCatalogo() throws Exception {
        int databaseSizeBeforeUpdate = catalogoRepository.findAll().size();
        catalogo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCatalogoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(catalogo)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Catalogo in the database
        List<Catalogo> catalogoList = catalogoRepository.findAll();
        assertThat(catalogoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCatalogo() throws Exception {
        // Initialize the database
        catalogoRepository.saveAndFlush(catalogo);

        int databaseSizeBeforeDelete = catalogoRepository.findAll().size();

        // Delete the catalogo
        restCatalogoMockMvc
            .perform(delete(ENTITY_API_URL_ID, catalogo.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Catalogo> catalogoList = catalogoRepository.findAll();
        assertThat(catalogoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
