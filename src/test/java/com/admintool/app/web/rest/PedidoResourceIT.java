package com.admintool.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.admintool.app.IntegrationTest;
import com.admintool.app.domain.Pedido;
import com.admintool.app.repository.PedidoRepository;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link PedidoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PedidoResourceIT {

    private static final Float DEFAULT_TOTAL = 1F;
    private static final Float UPDATED_TOTAL = 2F;

    private static final LocalDate DEFAULT_FECHA_GENERADO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_GENERADO = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_FECHA_ENTREGA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_ENTREGA = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_PREPARADO = false;
    private static final Boolean UPDATED_PREPARADO = true;

    private static final Boolean DEFAULT_ENTREGADO = false;
    private static final Boolean UPDATED_ENTREGADO = true;

    private static final Boolean DEFAULT_FORANEO = false;
    private static final Boolean UPDATED_FORANEO = true;

    private static final String ENTITY_API_URL = "/api/pedidos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPedidoMockMvc;

    private Pedido pedido;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pedido createEntity(EntityManager em) {
        Pedido pedido = new Pedido()
            .total(DEFAULT_TOTAL)
            .fechaGenerado(DEFAULT_FECHA_GENERADO)
            .fechaEntrega(DEFAULT_FECHA_ENTREGA)
            .preparado(DEFAULT_PREPARADO)
            .entregado(DEFAULT_ENTREGADO)
            .foraneo(DEFAULT_FORANEO);
        return pedido;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pedido createUpdatedEntity(EntityManager em) {
        Pedido pedido = new Pedido()
            .total(UPDATED_TOTAL)
            .fechaGenerado(UPDATED_FECHA_GENERADO)
            .fechaEntrega(UPDATED_FECHA_ENTREGA)
            .preparado(UPDATED_PREPARADO)
            .entregado(UPDATED_ENTREGADO)
            .foraneo(UPDATED_FORANEO);
        return pedido;
    }

    @BeforeEach
    public void initTest() {
        pedido = createEntity(em);
    }

    @Test
    @Transactional
    void createPedido() throws Exception {
        int databaseSizeBeforeCreate = pedidoRepository.findAll().size();
        // Create the Pedido
        restPedidoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pedido)))
            .andExpect(status().isCreated());

        // Validate the Pedido in the database
        List<Pedido> pedidoList = pedidoRepository.findAll();
        assertThat(pedidoList).hasSize(databaseSizeBeforeCreate + 1);
        Pedido testPedido = pedidoList.get(pedidoList.size() - 1);
        assertThat(testPedido.getTotal()).isEqualTo(DEFAULT_TOTAL);
        assertThat(testPedido.getFechaGenerado()).isEqualTo(DEFAULT_FECHA_GENERADO);
        assertThat(testPedido.getFechaEntrega()).isEqualTo(DEFAULT_FECHA_ENTREGA);
        assertThat(testPedido.getPreparado()).isEqualTo(DEFAULT_PREPARADO);
        assertThat(testPedido.getEntregado()).isEqualTo(DEFAULT_ENTREGADO);
        assertThat(testPedido.getForaneo()).isEqualTo(DEFAULT_FORANEO);
    }

    @Test
    @Transactional
    void createPedidoWithExistingId() throws Exception {
        // Create the Pedido with an existing ID
        pedido.setId(1L);

        int databaseSizeBeforeCreate = pedidoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPedidoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pedido)))
            .andExpect(status().isBadRequest());

        // Validate the Pedido in the database
        List<Pedido> pedidoList = pedidoRepository.findAll();
        assertThat(pedidoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPedidos() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList
        restPedidoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pedido.getId().intValue())))
            .andExpect(jsonPath("$.[*].total").value(hasItem(DEFAULT_TOTAL.doubleValue())))
            .andExpect(jsonPath("$.[*].fechaGenerado").value(hasItem(DEFAULT_FECHA_GENERADO.toString())))
            .andExpect(jsonPath("$.[*].fechaEntrega").value(hasItem(DEFAULT_FECHA_ENTREGA.toString())))
            .andExpect(jsonPath("$.[*].preparado").value(hasItem(DEFAULT_PREPARADO.booleanValue())))
            .andExpect(jsonPath("$.[*].entregado").value(hasItem(DEFAULT_ENTREGADO.booleanValue())))
            .andExpect(jsonPath("$.[*].foraneo").value(hasItem(DEFAULT_FORANEO.booleanValue())));
    }

    @Test
    @Transactional
    void getPedido() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get the pedido
        restPedidoMockMvc
            .perform(get(ENTITY_API_URL_ID, pedido.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pedido.getId().intValue()))
            .andExpect(jsonPath("$.total").value(DEFAULT_TOTAL.doubleValue()))
            .andExpect(jsonPath("$.fechaGenerado").value(DEFAULT_FECHA_GENERADO.toString()))
            .andExpect(jsonPath("$.fechaEntrega").value(DEFAULT_FECHA_ENTREGA.toString()))
            .andExpect(jsonPath("$.preparado").value(DEFAULT_PREPARADO.booleanValue()))
            .andExpect(jsonPath("$.entregado").value(DEFAULT_ENTREGADO.booleanValue()))
            .andExpect(jsonPath("$.foraneo").value(DEFAULT_FORANEO.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingPedido() throws Exception {
        // Get the pedido
        restPedidoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPedido() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        int databaseSizeBeforeUpdate = pedidoRepository.findAll().size();

        // Update the pedido
        Pedido updatedPedido = pedidoRepository.findById(pedido.getId()).get();
        // Disconnect from session so that the updates on updatedPedido are not directly saved in db
        em.detach(updatedPedido);
        updatedPedido
            .total(UPDATED_TOTAL)
            .fechaGenerado(UPDATED_FECHA_GENERADO)
            .fechaEntrega(UPDATED_FECHA_ENTREGA)
            .preparado(UPDATED_PREPARADO)
            .entregado(UPDATED_ENTREGADO)
            .foraneo(UPDATED_FORANEO);

        restPedidoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPedido.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPedido))
            )
            .andExpect(status().isOk());

        // Validate the Pedido in the database
        List<Pedido> pedidoList = pedidoRepository.findAll();
        assertThat(pedidoList).hasSize(databaseSizeBeforeUpdate);
        Pedido testPedido = pedidoList.get(pedidoList.size() - 1);
        assertThat(testPedido.getTotal()).isEqualTo(UPDATED_TOTAL);
        assertThat(testPedido.getFechaGenerado()).isEqualTo(UPDATED_FECHA_GENERADO);
        assertThat(testPedido.getFechaEntrega()).isEqualTo(UPDATED_FECHA_ENTREGA);
        assertThat(testPedido.getPreparado()).isEqualTo(UPDATED_PREPARADO);
        assertThat(testPedido.getEntregado()).isEqualTo(UPDATED_ENTREGADO);
        assertThat(testPedido.getForaneo()).isEqualTo(UPDATED_FORANEO);
    }

    @Test
    @Transactional
    void putNonExistingPedido() throws Exception {
        int databaseSizeBeforeUpdate = pedidoRepository.findAll().size();
        pedido.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPedidoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pedido.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pedido))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pedido in the database
        List<Pedido> pedidoList = pedidoRepository.findAll();
        assertThat(pedidoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPedido() throws Exception {
        int databaseSizeBeforeUpdate = pedidoRepository.findAll().size();
        pedido.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPedidoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pedido))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pedido in the database
        List<Pedido> pedidoList = pedidoRepository.findAll();
        assertThat(pedidoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPedido() throws Exception {
        int databaseSizeBeforeUpdate = pedidoRepository.findAll().size();
        pedido.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPedidoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pedido)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pedido in the database
        List<Pedido> pedidoList = pedidoRepository.findAll();
        assertThat(pedidoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePedidoWithPatch() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        int databaseSizeBeforeUpdate = pedidoRepository.findAll().size();

        // Update the pedido using partial update
        Pedido partialUpdatedPedido = new Pedido();
        partialUpdatedPedido.setId(pedido.getId());

        partialUpdatedPedido.total(UPDATED_TOTAL).fechaEntrega(UPDATED_FECHA_ENTREGA).entregado(UPDATED_ENTREGADO);

        restPedidoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPedido.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPedido))
            )
            .andExpect(status().isOk());

        // Validate the Pedido in the database
        List<Pedido> pedidoList = pedidoRepository.findAll();
        assertThat(pedidoList).hasSize(databaseSizeBeforeUpdate);
        Pedido testPedido = pedidoList.get(pedidoList.size() - 1);
        assertThat(testPedido.getTotal()).isEqualTo(UPDATED_TOTAL);
        assertThat(testPedido.getFechaGenerado()).isEqualTo(DEFAULT_FECHA_GENERADO);
        assertThat(testPedido.getFechaEntrega()).isEqualTo(UPDATED_FECHA_ENTREGA);
        assertThat(testPedido.getPreparado()).isEqualTo(DEFAULT_PREPARADO);
        assertThat(testPedido.getEntregado()).isEqualTo(UPDATED_ENTREGADO);
        assertThat(testPedido.getForaneo()).isEqualTo(DEFAULT_FORANEO);
    }

    @Test
    @Transactional
    void fullUpdatePedidoWithPatch() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        int databaseSizeBeforeUpdate = pedidoRepository.findAll().size();

        // Update the pedido using partial update
        Pedido partialUpdatedPedido = new Pedido();
        partialUpdatedPedido.setId(pedido.getId());

        partialUpdatedPedido
            .total(UPDATED_TOTAL)
            .fechaGenerado(UPDATED_FECHA_GENERADO)
            .fechaEntrega(UPDATED_FECHA_ENTREGA)
            .preparado(UPDATED_PREPARADO)
            .entregado(UPDATED_ENTREGADO)
            .foraneo(UPDATED_FORANEO);

        restPedidoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPedido.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPedido))
            )
            .andExpect(status().isOk());

        // Validate the Pedido in the database
        List<Pedido> pedidoList = pedidoRepository.findAll();
        assertThat(pedidoList).hasSize(databaseSizeBeforeUpdate);
        Pedido testPedido = pedidoList.get(pedidoList.size() - 1);
        assertThat(testPedido.getTotal()).isEqualTo(UPDATED_TOTAL);
        assertThat(testPedido.getFechaGenerado()).isEqualTo(UPDATED_FECHA_GENERADO);
        assertThat(testPedido.getFechaEntrega()).isEqualTo(UPDATED_FECHA_ENTREGA);
        assertThat(testPedido.getPreparado()).isEqualTo(UPDATED_PREPARADO);
        assertThat(testPedido.getEntregado()).isEqualTo(UPDATED_ENTREGADO);
        assertThat(testPedido.getForaneo()).isEqualTo(UPDATED_FORANEO);
    }

    @Test
    @Transactional
    void patchNonExistingPedido() throws Exception {
        int databaseSizeBeforeUpdate = pedidoRepository.findAll().size();
        pedido.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPedidoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pedido.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pedido))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pedido in the database
        List<Pedido> pedidoList = pedidoRepository.findAll();
        assertThat(pedidoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPedido() throws Exception {
        int databaseSizeBeforeUpdate = pedidoRepository.findAll().size();
        pedido.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPedidoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pedido))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pedido in the database
        List<Pedido> pedidoList = pedidoRepository.findAll();
        assertThat(pedidoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPedido() throws Exception {
        int databaseSizeBeforeUpdate = pedidoRepository.findAll().size();
        pedido.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPedidoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(pedido)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pedido in the database
        List<Pedido> pedidoList = pedidoRepository.findAll();
        assertThat(pedidoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePedido() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        int databaseSizeBeforeDelete = pedidoRepository.findAll().size();

        // Delete the pedido
        restPedidoMockMvc
            .perform(delete(ENTITY_API_URL_ID, pedido.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Pedido> pedidoList = pedidoRepository.findAll();
        assertThat(pedidoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
