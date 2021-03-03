package com.badals.admin.web.rest;

import com.badals.admin.MainApp;
import com.badals.admin.domain.Pkg;
import com.badals.admin.repository.PkgRepository;
import com.badals.admin.service.PkgService;
import com.badals.admin.service.dto.PkgDTO;
import com.badals.admin.service.mapper.PkgMapper;
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

import com.badals.admin.domain.enumeration.PackageType;
/**
 * Integration tests for the {@link PkgResource} REST controller.
 */
@SpringBootTest(classes = MainApp.class)
public class PkgResourceIT {

    private static final BigDecimal DEFAULT_LENGTH = new BigDecimal(1);
    private static final BigDecimal UPDATED_LENGTH = new BigDecimal(2);

    private static final BigDecimal DEFAULT_WIDTH = new BigDecimal(1);
    private static final BigDecimal UPDATED_WIDTH = new BigDecimal(2);

    private static final BigDecimal DEFAULT_HEIGHT = new BigDecimal(1);
    private static final BigDecimal UPDATED_HEIGHT = new BigDecimal(2);

    private static final BigDecimal DEFAULT_WEIGHT = new BigDecimal(1);
    private static final BigDecimal UPDATED_WEIGHT = new BigDecimal(2);

    private static final PackageType DEFAULT_PACKAGE_TYPE = PackageType.BADALS14X18BAG;
    private static final PackageType UPDATED_PACKAGE_TYPE = PackageType.DHLFLYER;

    @Autowired
    private PkgRepository pkgRepository;

    @Autowired
    private PkgMapper pkgMapper;

    @Autowired
    private PkgService pkgService;

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

    private MockMvc restPkgMockMvc;

    private Pkg pkg;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PkgResource pkgResource = new PkgResource(pkgService);
        this.restPkgMockMvc = MockMvcBuilders.standaloneSetup(pkgResource)
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
    public static Pkg createEntity(EntityManager em) {
        Pkg pkg = new Pkg()
            .length(DEFAULT_LENGTH)
            .width(DEFAULT_WIDTH)
            .height(DEFAULT_HEIGHT)
            .weight(DEFAULT_WEIGHT)
            .packageType(DEFAULT_PACKAGE_TYPE);
        return pkg;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pkg createUpdatedEntity(EntityManager em) {
        Pkg pkg = new Pkg()
            .length(UPDATED_LENGTH)
            .width(UPDATED_WIDTH)
            .height(UPDATED_HEIGHT)
            .weight(UPDATED_WEIGHT)
            .packageType(UPDATED_PACKAGE_TYPE);
        return pkg;
    }

    @BeforeEach
    public void initTest() {
        pkg = createEntity(em);
    }

    @Test
    @Transactional
    public void createPkg() throws Exception {
        int databaseSizeBeforeCreate = pkgRepository.findAll().size();

        // Create the Pkg
        PkgDTO pkgDTO = pkgMapper.toDto(pkg);
        restPkgMockMvc.perform(post("/api/pkgs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pkgDTO)))
            .andExpect(status().isCreated());

        // Validate the Pkg in the database
        List<Pkg> pkgList = pkgRepository.findAll();
        assertThat(pkgList).hasSize(databaseSizeBeforeCreate + 1);
        Pkg testPkg = pkgList.get(pkgList.size() - 1);
        assertThat(testPkg.getLength()).isEqualTo(DEFAULT_LENGTH);
        assertThat(testPkg.getWidth()).isEqualTo(DEFAULT_WIDTH);
        assertThat(testPkg.getHeight()).isEqualTo(DEFAULT_HEIGHT);
        assertThat(testPkg.getWeight()).isEqualTo(DEFAULT_WEIGHT);
        assertThat(testPkg.getPackageType()).isEqualTo(DEFAULT_PACKAGE_TYPE);
    }

    @Test
    @Transactional
    public void createPkgWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = pkgRepository.findAll().size();

        // Create the Pkg with an existing ID
        pkg.setId(1L);
        PkgDTO pkgDTO = pkgMapper.toDto(pkg);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPkgMockMvc.perform(post("/api/pkgs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pkgDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Pkg in the database
        List<Pkg> pkgList = pkgRepository.findAll();
        assertThat(pkgList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllPkgs() throws Exception {
        // Initialize the database
        pkgRepository.saveAndFlush(pkg);

        // Get all the pkgList
        restPkgMockMvc.perform(get("/api/pkgs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pkg.getId().intValue())))
            .andExpect(jsonPath("$.[*].length").value(hasItem(DEFAULT_LENGTH.intValue())))
            .andExpect(jsonPath("$.[*].width").value(hasItem(DEFAULT_WIDTH.intValue())))
            .andExpect(jsonPath("$.[*].height").value(hasItem(DEFAULT_HEIGHT.intValue())))
            .andExpect(jsonPath("$.[*].weight").value(hasItem(DEFAULT_WEIGHT.intValue())))
            .andExpect(jsonPath("$.[*].packageType").value(hasItem(DEFAULT_PACKAGE_TYPE.toString())));
    }
    
    @Test
    @Transactional
    public void getPkg() throws Exception {
        // Initialize the database
        pkgRepository.saveAndFlush(pkg);

        // Get the pkg
        restPkgMockMvc.perform(get("/api/pkgs/{id}", pkg.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(pkg.getId().intValue()))
            .andExpect(jsonPath("$.length").value(DEFAULT_LENGTH.intValue()))
            .andExpect(jsonPath("$.width").value(DEFAULT_WIDTH.intValue()))
            .andExpect(jsonPath("$.height").value(DEFAULT_HEIGHT.intValue()))
            .andExpect(jsonPath("$.weight").value(DEFAULT_WEIGHT.intValue()))
            .andExpect(jsonPath("$.packageType").value(DEFAULT_PACKAGE_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPkg() throws Exception {
        // Get the pkg
        restPkgMockMvc.perform(get("/api/pkgs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePkg() throws Exception {
        // Initialize the database
        pkgRepository.saveAndFlush(pkg);

        int databaseSizeBeforeUpdate = pkgRepository.findAll().size();

        // Update the pkg
        Pkg updatedPkg = pkgRepository.findById(pkg.getId()).get();
        // Disconnect from session so that the updates on updatedPkg are not directly saved in db
        em.detach(updatedPkg);
        updatedPkg
            .length(UPDATED_LENGTH)
            .width(UPDATED_WIDTH)
            .height(UPDATED_HEIGHT)
            .weight(UPDATED_WEIGHT)
            .packageType(UPDATED_PACKAGE_TYPE);
        PkgDTO pkgDTO = pkgMapper.toDto(updatedPkg);

        restPkgMockMvc.perform(put("/api/pkgs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pkgDTO)))
            .andExpect(status().isOk());

        // Validate the Pkg in the database
        List<Pkg> pkgList = pkgRepository.findAll();
        assertThat(pkgList).hasSize(databaseSizeBeforeUpdate);
        Pkg testPkg = pkgList.get(pkgList.size() - 1);
        assertThat(testPkg.getLength()).isEqualTo(UPDATED_LENGTH);
        assertThat(testPkg.getWidth()).isEqualTo(UPDATED_WIDTH);
        assertThat(testPkg.getHeight()).isEqualTo(UPDATED_HEIGHT);
        assertThat(testPkg.getWeight()).isEqualTo(UPDATED_WEIGHT);
        assertThat(testPkg.getPackageType()).isEqualTo(UPDATED_PACKAGE_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingPkg() throws Exception {
        int databaseSizeBeforeUpdate = pkgRepository.findAll().size();

        // Create the Pkg
        PkgDTO pkgDTO = pkgMapper.toDto(pkg);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPkgMockMvc.perform(put("/api/pkgs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pkgDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Pkg in the database
        List<Pkg> pkgList = pkgRepository.findAll();
        assertThat(pkgList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePkg() throws Exception {
        // Initialize the database
        pkgRepository.saveAndFlush(pkg);

        int databaseSizeBeforeDelete = pkgRepository.findAll().size();

        // Delete the pkg
        restPkgMockMvc.perform(delete("/api/pkgs/{id}", pkg.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Pkg> pkgList = pkgRepository.findAll();
        assertThat(pkgList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
