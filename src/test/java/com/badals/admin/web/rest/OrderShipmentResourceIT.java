package com.badals.admin.web.rest;

import com.badals.admin.AdminApp;
import com.badals.admin.domain.OrderShipment;
import com.badals.admin.repository.OrderShipmentRepository;
import com.badals.admin.service.OrderShipmentService;
import com.badals.admin.service.dto.OrderShipmentDTO;
import com.badals.admin.service.mapper.OrderShipmentMapper;
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
 * Integration tests for the {@link OrderShipmentResource} REST controller.
 */
@SpringBootTest(classes = AdminApp.class)
public class OrderShipmentResourceIT {

    private static final BigDecimal DEFAULT_QUANTITY = new BigDecimal(1);
    private static final BigDecimal UPDATED_QUANTITY = new BigDecimal(2);

    @Autowired
    private OrderShipmentRepository orderShipmentRepository;

    @Autowired
    private OrderShipmentMapper orderShipmentMapper;

    @Autowired
    private OrderShipmentService orderShipmentService;

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

    private MockMvc restOrderShipmentMockMvc;

    private OrderShipment orderShipment;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final OrderShipmentResource orderShipmentResource = new OrderShipmentResource(orderShipmentService);
        this.restOrderShipmentMockMvc = MockMvcBuilders.standaloneSetup(orderShipmentResource)
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
    public static OrderShipment createEntity(EntityManager em) {
        OrderShipment orderShipment = new OrderShipment()
            .quantity(DEFAULT_QUANTITY);
        return orderShipment;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderShipment createUpdatedEntity(EntityManager em) {
        OrderShipment orderShipment = new OrderShipment()
            .quantity(UPDATED_QUANTITY);
        return orderShipment;
    }

    @BeforeEach
    public void initTest() {
        orderShipment = createEntity(em);
    }

    @Test
    @Transactional
    public void createOrderShipment() throws Exception {
        int databaseSizeBeforeCreate = orderShipmentRepository.findAll().size();

        // Create the OrderShipment
        OrderShipmentDTO orderShipmentDTO = orderShipmentMapper.toDto(orderShipment);
        restOrderShipmentMockMvc.perform(post("/api/order-shipments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(orderShipmentDTO)))
            .andExpect(status().isCreated());

        // Validate the OrderShipment in the database
        List<OrderShipment> orderShipmentList = orderShipmentRepository.findAll();
        assertThat(orderShipmentList).hasSize(databaseSizeBeforeCreate + 1);
        OrderShipment testOrderShipment = orderShipmentList.get(orderShipmentList.size() - 1);
        assertThat(testOrderShipment.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    public void createOrderShipmentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = orderShipmentRepository.findAll().size();

        // Create the OrderShipment with an existing ID
        orderShipment.setId(1L);
        OrderShipmentDTO orderShipmentDTO = orderShipmentMapper.toDto(orderShipment);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrderShipmentMockMvc.perform(post("/api/order-shipments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(orderShipmentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the OrderShipment in the database
        List<OrderShipment> orderShipmentList = orderShipmentRepository.findAll();
        assertThat(orderShipmentList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllOrderShipments() throws Exception {
        // Initialize the database
        orderShipmentRepository.saveAndFlush(orderShipment);

        // Get all the orderShipmentList
        restOrderShipmentMockMvc.perform(get("/api/order-shipments?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderShipment.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY.intValue())));
    }
    
    @Test
    @Transactional
    public void getOrderShipment() throws Exception {
        // Initialize the database
        orderShipmentRepository.saveAndFlush(orderShipment);

        // Get the orderShipment
        restOrderShipmentMockMvc.perform(get("/api/order-shipments/{id}", orderShipment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(orderShipment.getId().intValue()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingOrderShipment() throws Exception {
        // Get the orderShipment
        restOrderShipmentMockMvc.perform(get("/api/order-shipments/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOrderShipment() throws Exception {
        // Initialize the database
        orderShipmentRepository.saveAndFlush(orderShipment);

        int databaseSizeBeforeUpdate = orderShipmentRepository.findAll().size();

        // Update the orderShipment
        OrderShipment updatedOrderShipment = orderShipmentRepository.findById(orderShipment.getId()).get();
        // Disconnect from session so that the updates on updatedOrderShipment are not directly saved in db
        em.detach(updatedOrderShipment);
        updatedOrderShipment
            .quantity(UPDATED_QUANTITY);
        OrderShipmentDTO orderShipmentDTO = orderShipmentMapper.toDto(updatedOrderShipment);

        restOrderShipmentMockMvc.perform(put("/api/order-shipments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(orderShipmentDTO)))
            .andExpect(status().isOk());

        // Validate the OrderShipment in the database
        List<OrderShipment> orderShipmentList = orderShipmentRepository.findAll();
        assertThat(orderShipmentList).hasSize(databaseSizeBeforeUpdate);
        OrderShipment testOrderShipment = orderShipmentList.get(orderShipmentList.size() - 1);
        assertThat(testOrderShipment.getQuantity()).isEqualTo(UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void updateNonExistingOrderShipment() throws Exception {
        int databaseSizeBeforeUpdate = orderShipmentRepository.findAll().size();

        // Create the OrderShipment
        OrderShipmentDTO orderShipmentDTO = orderShipmentMapper.toDto(orderShipment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderShipmentMockMvc.perform(put("/api/order-shipments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(orderShipmentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the OrderShipment in the database
        List<OrderShipment> orderShipmentList = orderShipmentRepository.findAll();
        assertThat(orderShipmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteOrderShipment() throws Exception {
        // Initialize the database
        orderShipmentRepository.saveAndFlush(orderShipment);

        int databaseSizeBeforeDelete = orderShipmentRepository.findAll().size();

        // Delete the orderShipment
        restOrderShipmentMockMvc.perform(delete("/api/order-shipments/{id}", orderShipment.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OrderShipment> orderShipmentList = orderShipmentRepository.findAll();
        assertThat(orderShipmentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
