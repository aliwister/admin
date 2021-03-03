package com.badals.admin.web.rest;

import com.badals.admin.MainApp;
import com.badals.admin.domain.PurchaseShipment;
import com.badals.admin.repository.PurchaseShipmentRepository;
import com.badals.admin.service.PurchaseShipmentService;
import com.badals.admin.service.dto.PurchaseShipmentDTO;
import com.badals.admin.service.mapper.PurchaseShipmentMapper;
import com.badals.admin.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.List;

import static com.badals.admin.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link PurchaseShipmentResource} REST controller.
 */
@SpringBootTest(classes = MainApp.class)
public class PurchaseShipmentResourceIT {

    private static final BigDecimal DEFAULT_QUANTITY = new BigDecimal(1);
    private static final BigDecimal UPDATED_QUANTITY = new BigDecimal(2);

    @Autowired
    private PurchaseShipmentRepository purchaseShipmentRepository;

    @Autowired
    private PurchaseShipmentMapper purchaseShipmentMapper;

    @Autowired
    private PurchaseShipmentService purchaseShipmentService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restPurchaseShipmentMockMvc;

    private PurchaseShipment purchaseShipment;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PurchaseShipmentResource purchaseShipmentResource = new PurchaseShipmentResource(purchaseShipmentService);
        this.restPurchaseShipmentMockMvc = MockMvcBuilders.standaloneSetup(purchaseShipmentResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PurchaseShipment createEntity(EntityManager em) {
        PurchaseShipment purchaseShipment = new PurchaseShipment()
            .quantity(DEFAULT_QUANTITY);
        return purchaseShipment;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PurchaseShipment createUpdatedEntity(EntityManager em) {
        PurchaseShipment purchaseShipment = new PurchaseShipment()
            .quantity(UPDATED_QUANTITY);
        return purchaseShipment;
    }

    @BeforeEach
    public void initTest() {
        purchaseShipment = createEntity(em);
    }

    @Test
    @Transactional
    public void createPurchaseShipment() throws Exception {
        int databaseSizeBeforeCreate = purchaseShipmentRepository.findAll().size();

        // Create the PurchaseShipment
        PurchaseShipmentDTO purchaseShipmentDTO = purchaseShipmentMapper.toDto(purchaseShipment);
        restPurchaseShipmentMockMvc.perform(post("/api/purchase-shipments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(purchaseShipmentDTO)))
            .andExpect(status().isCreated());

        // Validate the PurchaseShipment in the database
        List<PurchaseShipment> purchaseShipmentList = purchaseShipmentRepository.findAll();
        assertThat(purchaseShipmentList).hasSize(databaseSizeBeforeCreate + 1);
        PurchaseShipment testPurchaseShipment = purchaseShipmentList.get(purchaseShipmentList.size() - 1);
        assertThat(testPurchaseShipment.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    public void createPurchaseShipmentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = purchaseShipmentRepository.findAll().size();

        // Create the PurchaseShipment with an existing ID
        purchaseShipment.setId(1L);
        PurchaseShipmentDTO purchaseShipmentDTO = purchaseShipmentMapper.toDto(purchaseShipment);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPurchaseShipmentMockMvc.perform(post("/api/purchase-shipments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(purchaseShipmentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PurchaseShipment in the database
        List<PurchaseShipment> purchaseShipmentList = purchaseShipmentRepository.findAll();
        assertThat(purchaseShipmentList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllPurchaseShipments() throws Exception {
        // Initialize the database
        purchaseShipmentRepository.saveAndFlush(purchaseShipment);

        // Get all the purchaseShipmentList
        restPurchaseShipmentMockMvc.perform(get("/api/purchase-shipments?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(purchaseShipment.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY.intValue())));
    }
    
    @Test
    @Transactional
    public void getPurchaseShipment() throws Exception {
        // Initialize the database
        purchaseShipmentRepository.saveAndFlush(purchaseShipment);

        // Get the purchaseShipment
        restPurchaseShipmentMockMvc.perform(get("/api/purchase-shipments/{id}", purchaseShipment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(purchaseShipment.getId().intValue()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingPurchaseShipment() throws Exception {
        // Get the purchaseShipment
        restPurchaseShipmentMockMvc.perform(get("/api/purchase-shipments/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePurchaseShipment() throws Exception {
        // Initialize the database
        purchaseShipmentRepository.saveAndFlush(purchaseShipment);

        int databaseSizeBeforeUpdate = purchaseShipmentRepository.findAll().size();

        // Update the purchaseShipment
        PurchaseShipment updatedPurchaseShipment = purchaseShipmentRepository.findById(purchaseShipment.getId()).get();
        // Disconnect from session so that the updates on updatedPurchaseShipment are not directly saved in db
        em.detach(updatedPurchaseShipment);
        updatedPurchaseShipment
            .quantity(UPDATED_QUANTITY);
        PurchaseShipmentDTO purchaseShipmentDTO = purchaseShipmentMapper.toDto(updatedPurchaseShipment);

        restPurchaseShipmentMockMvc.perform(put("/api/purchase-shipments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(purchaseShipmentDTO)))
            .andExpect(status().isOk());

        // Validate the PurchaseShipment in the database
        List<PurchaseShipment> purchaseShipmentList = purchaseShipmentRepository.findAll();
        assertThat(purchaseShipmentList).hasSize(databaseSizeBeforeUpdate);
        PurchaseShipment testPurchaseShipment = purchaseShipmentList.get(purchaseShipmentList.size() - 1);
        assertThat(testPurchaseShipment.getQuantity()).isEqualTo(UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void updateNonExistingPurchaseShipment() throws Exception {
        int databaseSizeBeforeUpdate = purchaseShipmentRepository.findAll().size();

        // Create the PurchaseShipment
        PurchaseShipmentDTO purchaseShipmentDTO = purchaseShipmentMapper.toDto(purchaseShipment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPurchaseShipmentMockMvc.perform(put("/api/purchase-shipments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(purchaseShipmentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PurchaseShipment in the database
        List<PurchaseShipment> purchaseShipmentList = purchaseShipmentRepository.findAll();
        assertThat(purchaseShipmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePurchaseShipment() throws Exception {
        // Initialize the database
        purchaseShipmentRepository.saveAndFlush(purchaseShipment);

        int databaseSizeBeforeDelete = purchaseShipmentRepository.findAll().size();

        // Delete the purchaseShipment
        restPurchaseShipmentMockMvc.perform(delete("/api/purchase-shipments/{id}", purchaseShipment.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PurchaseShipment> purchaseShipmentList = purchaseShipmentRepository.findAll();
        assertThat(purchaseShipmentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
