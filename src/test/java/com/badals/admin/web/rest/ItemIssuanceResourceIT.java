package com.badals.admin.web.rest;

import com.badals.admin.AdminApp;
import com.badals.admin.domain.ItemIssuance;
import com.badals.admin.repository.ItemIssuanceRepository;
import com.badals.admin.service.ItemIssuanceService;
import com.badals.admin.service.dto.ItemIssuanceDTO;
import com.badals.admin.service.mapper.ItemIssuanceMapper;
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
 * Integration tests for the {@link ItemIssuanceResource} REST controller.
 */
@SpringBootTest(classes = AdminApp.class)
public class ItemIssuanceResourceIT {

    private static final BigDecimal DEFAULT_QUANTITY = new BigDecimal(1);
    private static final BigDecimal UPDATED_QUANTITY = new BigDecimal(2);

    @Autowired
    private ItemIssuanceRepository itemIssuanceRepository;

    @Autowired
    private ItemIssuanceMapper itemIssuanceMapper;

    @Autowired
    private ItemIssuanceService itemIssuanceService;

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

    private MockMvc restItemIssuanceMockMvc;

    private ItemIssuance itemIssuance;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ItemIssuanceResource itemIssuanceResource = new ItemIssuanceResource(itemIssuanceService);
        this.restItemIssuanceMockMvc = MockMvcBuilders.standaloneSetup(itemIssuanceResource)
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
    public static ItemIssuance createEntity(EntityManager em) {
        ItemIssuance itemIssuance = new ItemIssuance()
            .quantity(DEFAULT_QUANTITY);
        return itemIssuance;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ItemIssuance createUpdatedEntity(EntityManager em) {
        ItemIssuance itemIssuance = new ItemIssuance()
            .quantity(UPDATED_QUANTITY);
        return itemIssuance;
    }

    @BeforeEach
    public void initTest() {
        itemIssuance = createEntity(em);
    }

    @Test
    @Transactional
    public void createItemIssuance() throws Exception {
        int databaseSizeBeforeCreate = itemIssuanceRepository.findAll().size();

        // Create the ItemIssuance
        ItemIssuanceDTO itemIssuanceDTO = itemIssuanceMapper.toDto(itemIssuance);
        restItemIssuanceMockMvc.perform(post("/api/item-issuances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(itemIssuanceDTO)))
            .andExpect(status().isCreated());

        // Validate the ItemIssuance in the database
        List<ItemIssuance> itemIssuanceList = itemIssuanceRepository.findAll();
        assertThat(itemIssuanceList).hasSize(databaseSizeBeforeCreate + 1);
        ItemIssuance testItemIssuance = itemIssuanceList.get(itemIssuanceList.size() - 1);
        assertThat(testItemIssuance.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    public void createItemIssuanceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = itemIssuanceRepository.findAll().size();

        // Create the ItemIssuance with an existing ID
        itemIssuance.setId(1L);
        ItemIssuanceDTO itemIssuanceDTO = itemIssuanceMapper.toDto(itemIssuance);

        // An entity with an existing ID cannot be created, so this API call must fail
        restItemIssuanceMockMvc.perform(post("/api/item-issuances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(itemIssuanceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ItemIssuance in the database
        List<ItemIssuance> itemIssuanceList = itemIssuanceRepository.findAll();
        assertThat(itemIssuanceList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllItemIssuances() throws Exception {
        // Initialize the database
        itemIssuanceRepository.saveAndFlush(itemIssuance);

        // Get all the itemIssuanceList
        restItemIssuanceMockMvc.perform(get("/api/item-issuances?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(itemIssuance.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY.intValue())));
    }
    
    @Test
    @Transactional
    public void getItemIssuance() throws Exception {
        // Initialize the database
        itemIssuanceRepository.saveAndFlush(itemIssuance);

        // Get the itemIssuance
        restItemIssuanceMockMvc.perform(get("/api/item-issuances/{id}", itemIssuance.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(itemIssuance.getId().intValue()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingItemIssuance() throws Exception {
        // Get the itemIssuance
        restItemIssuanceMockMvc.perform(get("/api/item-issuances/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateItemIssuance() throws Exception {
        // Initialize the database
        itemIssuanceRepository.saveAndFlush(itemIssuance);

        int databaseSizeBeforeUpdate = itemIssuanceRepository.findAll().size();

        // Update the itemIssuance
        ItemIssuance updatedItemIssuance = itemIssuanceRepository.findById(itemIssuance.getId()).get();
        // Disconnect from session so that the updates on updatedItemIssuance are not directly saved in db
        em.detach(updatedItemIssuance);
        updatedItemIssuance
            .quantity(UPDATED_QUANTITY);
        ItemIssuanceDTO itemIssuanceDTO = itemIssuanceMapper.toDto(updatedItemIssuance);

        restItemIssuanceMockMvc.perform(put("/api/item-issuances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(itemIssuanceDTO)))
            .andExpect(status().isOk());

        // Validate the ItemIssuance in the database
        List<ItemIssuance> itemIssuanceList = itemIssuanceRepository.findAll();
        assertThat(itemIssuanceList).hasSize(databaseSizeBeforeUpdate);
        ItemIssuance testItemIssuance = itemIssuanceList.get(itemIssuanceList.size() - 1);
        assertThat(testItemIssuance.getQuantity()).isEqualTo(UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void updateNonExistingItemIssuance() throws Exception {
        int databaseSizeBeforeUpdate = itemIssuanceRepository.findAll().size();

        // Create the ItemIssuance
        ItemIssuanceDTO itemIssuanceDTO = itemIssuanceMapper.toDto(itemIssuance);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restItemIssuanceMockMvc.perform(put("/api/item-issuances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(itemIssuanceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ItemIssuance in the database
        List<ItemIssuance> itemIssuanceList = itemIssuanceRepository.findAll();
        assertThat(itemIssuanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteItemIssuance() throws Exception {
        // Initialize the database
        itemIssuanceRepository.saveAndFlush(itemIssuance);

        int databaseSizeBeforeDelete = itemIssuanceRepository.findAll().size();

        // Delete the itemIssuance
        restItemIssuanceMockMvc.perform(delete("/api/item-issuances/{id}", itemIssuance.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ItemIssuance> itemIssuanceList = itemIssuanceRepository.findAll();
        assertThat(itemIssuanceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
