package com.badals.admin.web.rest;

import com.badals.admin.AdminApp;
import com.badals.admin.domain.ShipmentDoc;
import com.badals.admin.repository.ShipmentDocRepository;
import com.badals.admin.service.ShipmentDocService;
import com.badals.admin.service.dto.ShipmentDocDTO;
import com.badals.admin.service.mapper.ShipmentDocMapper;
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
import java.util.List;

import static com.badals.admin.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ShipmentDocResource} REST controller.
 */
@SpringBootTest(classes = AdminApp.class)
public class ShipmentDocResourceIT {

    private static final String DEFAULT_FILE_KEY = "AAAAAAAAAA";
    private static final String UPDATED_FILE_KEY = "BBBBBBBBBB";

    @Autowired
    private ShipmentDocRepository shipmentDocRepository;

    @Autowired
    private ShipmentDocMapper shipmentDocMapper;

    @Autowired
    private ShipmentDocService shipmentDocService;

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

    private MockMvc restShipmentDocMockMvc;

    private ShipmentDoc shipmentDoc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ShipmentDocResource shipmentDocResource = new ShipmentDocResource(shipmentDocService);
        this.restShipmentDocMockMvc = MockMvcBuilders.standaloneSetup(shipmentDocResource)
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
    public static ShipmentDoc createEntity(EntityManager em) {
        ShipmentDoc shipmentDoc = new ShipmentDoc()
            .fileKey(DEFAULT_FILE_KEY);
        return shipmentDoc;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ShipmentDoc createUpdatedEntity(EntityManager em) {
        ShipmentDoc shipmentDoc = new ShipmentDoc()
            .fileKey(UPDATED_FILE_KEY);
        return shipmentDoc;
    }

    @BeforeEach
    public void initTest() {
        shipmentDoc = createEntity(em);
    }

    @Test
    @Transactional
    public void createShipmentDoc() throws Exception {
        int databaseSizeBeforeCreate = shipmentDocRepository.findAll().size();

        // Create the ShipmentDoc
        ShipmentDocDTO shipmentDocDTO = shipmentDocMapper.toDto(shipmentDoc);
        restShipmentDocMockMvc.perform(post("/api/shipment-docs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(shipmentDocDTO)))
            .andExpect(status().isCreated());

        // Validate the ShipmentDoc in the database
        List<ShipmentDoc> shipmentDocList = shipmentDocRepository.findAll();
        assertThat(shipmentDocList).hasSize(databaseSizeBeforeCreate + 1);
        ShipmentDoc testShipmentDoc = shipmentDocList.get(shipmentDocList.size() - 1);
        assertThat(testShipmentDoc.getFileKey()).isEqualTo(DEFAULT_FILE_KEY);
    }

    @Test
    @Transactional
    public void createShipmentDocWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = shipmentDocRepository.findAll().size();

        // Create the ShipmentDoc with an existing ID
        shipmentDoc.setId(1L);
        ShipmentDocDTO shipmentDocDTO = shipmentDocMapper.toDto(shipmentDoc);

        // An entity with an existing ID cannot be created, so this API call must fail
        restShipmentDocMockMvc.perform(post("/api/shipment-docs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(shipmentDocDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ShipmentDoc in the database
        List<ShipmentDoc> shipmentDocList = shipmentDocRepository.findAll();
        assertThat(shipmentDocList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllShipmentDocs() throws Exception {
        // Initialize the database
        shipmentDocRepository.saveAndFlush(shipmentDoc);

        // Get all the shipmentDocList
        restShipmentDocMockMvc.perform(get("/api/shipment-docs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shipmentDoc.getId().intValue())))
            .andExpect(jsonPath("$.[*].fileKey").value(hasItem(DEFAULT_FILE_KEY)));
    }
    
    @Test
    @Transactional
    public void getShipmentDoc() throws Exception {
        // Initialize the database
        shipmentDocRepository.saveAndFlush(shipmentDoc);

        // Get the shipmentDoc
        restShipmentDocMockMvc.perform(get("/api/shipment-docs/{id}", shipmentDoc.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(shipmentDoc.getId().intValue()))
            .andExpect(jsonPath("$.fileKey").value(DEFAULT_FILE_KEY));
    }

    @Test
    @Transactional
    public void getNonExistingShipmentDoc() throws Exception {
        // Get the shipmentDoc
        restShipmentDocMockMvc.perform(get("/api/shipment-docs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateShipmentDoc() throws Exception {
        // Initialize the database
        shipmentDocRepository.saveAndFlush(shipmentDoc);

        int databaseSizeBeforeUpdate = shipmentDocRepository.findAll().size();

        // Update the shipmentDoc
        ShipmentDoc updatedShipmentDoc = shipmentDocRepository.findById(shipmentDoc.getId()).get();
        // Disconnect from session so that the updates on updatedShipmentDoc are not directly saved in db
        em.detach(updatedShipmentDoc);
        updatedShipmentDoc
            .fileKey(UPDATED_FILE_KEY);
        ShipmentDocDTO shipmentDocDTO = shipmentDocMapper.toDto(updatedShipmentDoc);

        restShipmentDocMockMvc.perform(put("/api/shipment-docs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(shipmentDocDTO)))
            .andExpect(status().isOk());

        // Validate the ShipmentDoc in the database
        List<ShipmentDoc> shipmentDocList = shipmentDocRepository.findAll();
        assertThat(shipmentDocList).hasSize(databaseSizeBeforeUpdate);
        ShipmentDoc testShipmentDoc = shipmentDocList.get(shipmentDocList.size() - 1);
        assertThat(testShipmentDoc.getFileKey()).isEqualTo(UPDATED_FILE_KEY);
    }

    @Test
    @Transactional
    public void updateNonExistingShipmentDoc() throws Exception {
        int databaseSizeBeforeUpdate = shipmentDocRepository.findAll().size();

        // Create the ShipmentDoc
        ShipmentDocDTO shipmentDocDTO = shipmentDocMapper.toDto(shipmentDoc);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShipmentDocMockMvc.perform(put("/api/shipment-docs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(shipmentDocDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ShipmentDoc in the database
        List<ShipmentDoc> shipmentDocList = shipmentDocRepository.findAll();
        assertThat(shipmentDocList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteShipmentDoc() throws Exception {
        // Initialize the database
        shipmentDocRepository.saveAndFlush(shipmentDoc);

        int databaseSizeBeforeDelete = shipmentDocRepository.findAll().size();

        // Delete the shipmentDoc
        restShipmentDocMockMvc.perform(delete("/api/shipment-docs/{id}", shipmentDoc.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ShipmentDoc> shipmentDocList = shipmentDocRepository.findAll();
        assertThat(shipmentDocList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
