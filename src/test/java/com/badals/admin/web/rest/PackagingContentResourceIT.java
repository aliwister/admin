package com.badals.admin.web.rest;

import com.badals.admin.MainApp;
import com.badals.admin.domain.PackagingContent;
import com.badals.admin.repository.PackagingContentRepository;
import com.badals.admin.service.PackagingContentService;
import com.badals.admin.service.dto.PackagingContentDTO;
import com.badals.admin.service.mapper.PackagingContentMapper;
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
 * Integration tests for the {@link PackagingContentResource} REST controller.
 */
@SpringBootTest(classes = MainApp.class)
public class PackagingContentResourceIT {

    private static final BigDecimal DEFAULT_QUANTITY = new BigDecimal(1);
    private static final BigDecimal UPDATED_QUANTITY = new BigDecimal(2);

    @Autowired
    private PackagingContentRepository packagingContentRepository;

    @Autowired
    private PackagingContentMapper packagingContentMapper;

    @Autowired
    private PackagingContentService packagingContentService;

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

    private MockMvc restPackagingContentMockMvc;

    private PackagingContent packagingContent;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PackagingContentResource packagingContentResource = new PackagingContentResource(packagingContentService);
        this.restPackagingContentMockMvc = MockMvcBuilders.standaloneSetup(packagingContentResource)
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
    public static PackagingContent createEntity(EntityManager em) {
        PackagingContent packagingContent = new PackagingContent()
            .quantity(DEFAULT_QUANTITY);
        return packagingContent;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PackagingContent createUpdatedEntity(EntityManager em) {
        PackagingContent packagingContent = new PackagingContent()
            .quantity(UPDATED_QUANTITY);
        return packagingContent;
    }

    @BeforeEach
    public void initTest() {
        packagingContent = createEntity(em);
    }

    @Test
    @Transactional
    public void createPackagingContent() throws Exception {
        int databaseSizeBeforeCreate = packagingContentRepository.findAll().size();

        // Create the PackagingContent
        PackagingContentDTO packagingContentDTO = packagingContentMapper.toDto(packagingContent);
        restPackagingContentMockMvc.perform(post("/api/packaging-contents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(packagingContentDTO)))
            .andExpect(status().isCreated());

        // Validate the PackagingContent in the database
        List<PackagingContent> packagingContentList = packagingContentRepository.findAll();
        assertThat(packagingContentList).hasSize(databaseSizeBeforeCreate + 1);
        PackagingContent testPackagingContent = packagingContentList.get(packagingContentList.size() - 1);
        assertThat(testPackagingContent.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    public void createPackagingContentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = packagingContentRepository.findAll().size();

        // Create the PackagingContent with an existing ID
        packagingContent.setId(1L);
        PackagingContentDTO packagingContentDTO = packagingContentMapper.toDto(packagingContent);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPackagingContentMockMvc.perform(post("/api/packaging-contents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(packagingContentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PackagingContent in the database
        List<PackagingContent> packagingContentList = packagingContentRepository.findAll();
        assertThat(packagingContentList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllPackagingContents() throws Exception {
        // Initialize the database
        packagingContentRepository.saveAndFlush(packagingContent);

        // Get all the packagingContentList
        restPackagingContentMockMvc.perform(get("/api/packaging-contents?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(packagingContent.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY.intValue())));
    }
    
    @Test
    @Transactional
    public void getPackagingContent() throws Exception {
        // Initialize the database
        packagingContentRepository.saveAndFlush(packagingContent);

        // Get the packagingContent
        restPackagingContentMockMvc.perform(get("/api/packaging-contents/{id}", packagingContent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(packagingContent.getId().intValue()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingPackagingContent() throws Exception {
        // Get the packagingContent
        restPackagingContentMockMvc.perform(get("/api/packaging-contents/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePackagingContent() throws Exception {
        // Initialize the database
        packagingContentRepository.saveAndFlush(packagingContent);

        int databaseSizeBeforeUpdate = packagingContentRepository.findAll().size();

        // Update the packagingContent
        PackagingContent updatedPackagingContent = packagingContentRepository.findById(packagingContent.getId()).get();
        // Disconnect from session so that the updates on updatedPackagingContent are not directly saved in db
        em.detach(updatedPackagingContent);
        updatedPackagingContent
            .quantity(UPDATED_QUANTITY);
        PackagingContentDTO packagingContentDTO = packagingContentMapper.toDto(updatedPackagingContent);

        restPackagingContentMockMvc.perform(put("/api/packaging-contents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(packagingContentDTO)))
            .andExpect(status().isOk());

        // Validate the PackagingContent in the database
        List<PackagingContent> packagingContentList = packagingContentRepository.findAll();
        assertThat(packagingContentList).hasSize(databaseSizeBeforeUpdate);
        PackagingContent testPackagingContent = packagingContentList.get(packagingContentList.size() - 1);
        assertThat(testPackagingContent.getQuantity()).isEqualTo(UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void updateNonExistingPackagingContent() throws Exception {
        int databaseSizeBeforeUpdate = packagingContentRepository.findAll().size();

        // Create the PackagingContent
        PackagingContentDTO packagingContentDTO = packagingContentMapper.toDto(packagingContent);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPackagingContentMockMvc.perform(put("/api/packaging-contents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(packagingContentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PackagingContent in the database
        List<PackagingContent> packagingContentList = packagingContentRepository.findAll();
        assertThat(packagingContentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePackagingContent() throws Exception {
        // Initialize the database
        packagingContentRepository.saveAndFlush(packagingContent);

        int databaseSizeBeforeDelete = packagingContentRepository.findAll().size();

        // Delete the packagingContent
        restPackagingContentMockMvc.perform(delete("/api/packaging-contents/{id}", packagingContent.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PackagingContent> packagingContentList = packagingContentRepository.findAll();
        assertThat(packagingContentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
