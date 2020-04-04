package com.badals.admin.web.rest;

import com.badals.admin.AdminApp;
import com.badals.admin.domain.ShipmentReceipt;
import com.badals.admin.repository.ShipmentReceiptRepository;
import com.badals.admin.service.ShipmentReceiptService;
import com.badals.admin.service.dto.ShipmentReceiptDTO;
import com.badals.admin.service.mapper.ShipmentReceiptMapper;
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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.badals.admin.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.badals.admin.domain.enumeration.RejectReason;
/**
 * Integration tests for the {@link ShipmentReceiptResource} REST controller.
 */
@SpringBootTest(classes = AdminApp.class)
public class ShipmentReceiptResourceIT {

    private static final Instant DEFAULT_RECEIVED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_RECEIVED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final BigDecimal DEFAULT_ACCEPTED = new BigDecimal(1);
    private static final BigDecimal UPDATED_ACCEPTED = new BigDecimal(2);

    private static final BigDecimal DEFAULT_REJECTED = new BigDecimal(1);
    private static final BigDecimal UPDATED_REJECTED = new BigDecimal(2);

    private static final RejectReason DEFAULT_REJECT_REASON = RejectReason.SPILLED;
    private static final RejectReason UPDATED_REJECT_REASON = RejectReason.CRACKED;

    @Autowired
    private ShipmentReceiptRepository shipmentReceiptRepository;

    @Autowired
    private ShipmentReceiptMapper shipmentReceiptMapper;

    @Autowired
    private ShipmentReceiptService shipmentReceiptService;

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

    private MockMvc restShipmentReceiptMockMvc;

    private ShipmentReceipt shipmentReceipt;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ShipmentReceiptResource shipmentReceiptResource = new ShipmentReceiptResource(shipmentReceiptService);
        this.restShipmentReceiptMockMvc = MockMvcBuilders.standaloneSetup(shipmentReceiptResource)
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
    public static ShipmentReceipt createEntity(EntityManager em) {
        ShipmentReceipt shipmentReceipt = new ShipmentReceipt()
            .receivedDate(DEFAULT_RECEIVED_DATE)
            .accepted(DEFAULT_ACCEPTED)
            .rejected(DEFAULT_REJECTED)
            .rejectReason(DEFAULT_REJECT_REASON);
        return shipmentReceipt;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ShipmentReceipt createUpdatedEntity(EntityManager em) {
        ShipmentReceipt shipmentReceipt = new ShipmentReceipt()
            .receivedDate(UPDATED_RECEIVED_DATE)
            .accepted(UPDATED_ACCEPTED)
            .rejected(UPDATED_REJECTED)
            .rejectReason(UPDATED_REJECT_REASON);
        return shipmentReceipt;
    }

    @BeforeEach
    public void initTest() {
        shipmentReceipt = createEntity(em);
    }

    @Test
    @Transactional
    public void createShipmentReceipt() throws Exception {
        int databaseSizeBeforeCreate = shipmentReceiptRepository.findAll().size();

        // Create the ShipmentReceipt
        ShipmentReceiptDTO shipmentReceiptDTO = shipmentReceiptMapper.toDto(shipmentReceipt);
        restShipmentReceiptMockMvc.perform(post("/api/shipment-receipts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(shipmentReceiptDTO)))
            .andExpect(status().isCreated());

        // Validate the ShipmentReceipt in the database
        List<ShipmentReceipt> shipmentReceiptList = shipmentReceiptRepository.findAll();
        assertThat(shipmentReceiptList).hasSize(databaseSizeBeforeCreate + 1);
        ShipmentReceipt testShipmentReceipt = shipmentReceiptList.get(shipmentReceiptList.size() - 1);
        assertThat(testShipmentReceipt.getReceivedDate()).isEqualTo(DEFAULT_RECEIVED_DATE);
        assertThat(testShipmentReceipt.getAccepted()).isEqualTo(DEFAULT_ACCEPTED);
        assertThat(testShipmentReceipt.getRejected()).isEqualTo(DEFAULT_REJECTED);
        assertThat(testShipmentReceipt.getRejectReason()).isEqualTo(DEFAULT_REJECT_REASON);
    }

    @Test
    @Transactional
    public void createShipmentReceiptWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = shipmentReceiptRepository.findAll().size();

        // Create the ShipmentReceipt with an existing ID
        shipmentReceipt.setId(1L);
        ShipmentReceiptDTO shipmentReceiptDTO = shipmentReceiptMapper.toDto(shipmentReceipt);

        // An entity with an existing ID cannot be created, so this API call must fail
        restShipmentReceiptMockMvc.perform(post("/api/shipment-receipts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(shipmentReceiptDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ShipmentReceipt in the database
        List<ShipmentReceipt> shipmentReceiptList = shipmentReceiptRepository.findAll();
        assertThat(shipmentReceiptList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllShipmentReceipts() throws Exception {
        // Initialize the database
        shipmentReceiptRepository.saveAndFlush(shipmentReceipt);

        // Get all the shipmentReceiptList
        restShipmentReceiptMockMvc.perform(get("/api/shipment-receipts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shipmentReceipt.getId().intValue())))
            .andExpect(jsonPath("$.[*].receivedDate").value(hasItem(DEFAULT_RECEIVED_DATE.toString())))
            .andExpect(jsonPath("$.[*].accepted").value(hasItem(DEFAULT_ACCEPTED.intValue())))
            .andExpect(jsonPath("$.[*].rejected").value(hasItem(DEFAULT_REJECTED.intValue())))
            .andExpect(jsonPath("$.[*].rejectReason").value(hasItem(DEFAULT_REJECT_REASON.toString())));
    }
    
    @Test
    @Transactional
    public void getShipmentReceipt() throws Exception {
        // Initialize the database
        shipmentReceiptRepository.saveAndFlush(shipmentReceipt);

        // Get the shipmentReceipt
        restShipmentReceiptMockMvc.perform(get("/api/shipment-receipts/{id}", shipmentReceipt.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(shipmentReceipt.getId().intValue()))
            .andExpect(jsonPath("$.receivedDate").value(DEFAULT_RECEIVED_DATE.toString()))
            .andExpect(jsonPath("$.accepted").value(DEFAULT_ACCEPTED.intValue()))
            .andExpect(jsonPath("$.rejected").value(DEFAULT_REJECTED.intValue()))
            .andExpect(jsonPath("$.rejectReason").value(DEFAULT_REJECT_REASON.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingShipmentReceipt() throws Exception {
        // Get the shipmentReceipt
        restShipmentReceiptMockMvc.perform(get("/api/shipment-receipts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateShipmentReceipt() throws Exception {
        // Initialize the database
        shipmentReceiptRepository.saveAndFlush(shipmentReceipt);

        int databaseSizeBeforeUpdate = shipmentReceiptRepository.findAll().size();

        // Update the shipmentReceipt
        ShipmentReceipt updatedShipmentReceipt = shipmentReceiptRepository.findById(shipmentReceipt.getId()).get();
        // Disconnect from session so that the updates on updatedShipmentReceipt are not directly saved in db
        em.detach(updatedShipmentReceipt);
        updatedShipmentReceipt
            .receivedDate(UPDATED_RECEIVED_DATE)
            .accepted(UPDATED_ACCEPTED)
            .rejected(UPDATED_REJECTED)
            .rejectReason(UPDATED_REJECT_REASON);
        ShipmentReceiptDTO shipmentReceiptDTO = shipmentReceiptMapper.toDto(updatedShipmentReceipt);

        restShipmentReceiptMockMvc.perform(put("/api/shipment-receipts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(shipmentReceiptDTO)))
            .andExpect(status().isOk());

        // Validate the ShipmentReceipt in the database
        List<ShipmentReceipt> shipmentReceiptList = shipmentReceiptRepository.findAll();
        assertThat(shipmentReceiptList).hasSize(databaseSizeBeforeUpdate);
        ShipmentReceipt testShipmentReceipt = shipmentReceiptList.get(shipmentReceiptList.size() - 1);
        assertThat(testShipmentReceipt.getReceivedDate()).isEqualTo(UPDATED_RECEIVED_DATE);
        assertThat(testShipmentReceipt.getAccepted()).isEqualTo(UPDATED_ACCEPTED);
        assertThat(testShipmentReceipt.getRejected()).isEqualTo(UPDATED_REJECTED);
        assertThat(testShipmentReceipt.getRejectReason()).isEqualTo(UPDATED_REJECT_REASON);
    }

    @Test
    @Transactional
    public void updateNonExistingShipmentReceipt() throws Exception {
        int databaseSizeBeforeUpdate = shipmentReceiptRepository.findAll().size();

        // Create the ShipmentReceipt
        ShipmentReceiptDTO shipmentReceiptDTO = shipmentReceiptMapper.toDto(shipmentReceipt);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShipmentReceiptMockMvc.perform(put("/api/shipment-receipts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(shipmentReceiptDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ShipmentReceipt in the database
        List<ShipmentReceipt> shipmentReceiptList = shipmentReceiptRepository.findAll();
        assertThat(shipmentReceiptList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteShipmentReceipt() throws Exception {
        // Initialize the database
        shipmentReceiptRepository.saveAndFlush(shipmentReceipt);

        int databaseSizeBeforeDelete = shipmentReceiptRepository.findAll().size();

        // Delete the shipmentReceipt
        restShipmentReceiptMockMvc.perform(delete("/api/shipment-receipts/{id}", shipmentReceipt.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ShipmentReceipt> shipmentReceiptList = shipmentReceiptRepository.findAll();
        assertThat(shipmentReceiptList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
