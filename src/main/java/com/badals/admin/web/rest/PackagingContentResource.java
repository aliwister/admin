package com.badals.admin.web.rest;

import com.badals.admin.service.PackagingContentService;
import com.badals.admin.web.rest.errors.BadRequestAlertException;
import com.badals.admin.service.dto.PackagingContentDTO;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.badals.admin.domain.PackagingContent}.
 */
@RestController
@RequestMapping("/api")
public class PackagingContentResource {

    private final Logger log = LoggerFactory.getLogger(PackagingContentResource.class);

    private static final String ENTITY_NAME = "adminPackagingContent";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PackagingContentService packagingContentService;

    public PackagingContentResource(PackagingContentService packagingContentService) {
        this.packagingContentService = packagingContentService;
    }

    /**
     * {@code POST  /packaging-contents} : Create a new packagingContent.
     *
     * @param packagingContentDTO the packagingContentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new packagingContentDTO, or with status {@code 400 (Bad Request)} if the packagingContent has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/packaging-contents")
    public ResponseEntity<PackagingContentDTO> createPackagingContent(@RequestBody PackagingContentDTO packagingContentDTO) throws URISyntaxException {
        log.debug("REST request to save PackagingContent : {}", packagingContentDTO);
        if (packagingContentDTO.getId() != null) {
            throw new BadRequestAlertException("A new packagingContent cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PackagingContentDTO result = packagingContentService.save(packagingContentDTO);
        return ResponseEntity.created(new URI("/api/packaging-contents/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /packaging-contents} : Updates an existing packagingContent.
     *
     * @param packagingContentDTO the packagingContentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated packagingContentDTO,
     * or with status {@code 400 (Bad Request)} if the packagingContentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the packagingContentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/packaging-contents")
    public ResponseEntity<PackagingContentDTO> updatePackagingContent(@RequestBody PackagingContentDTO packagingContentDTO) throws URISyntaxException {
        log.debug("REST request to update PackagingContent : {}", packagingContentDTO);
        if (packagingContentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PackagingContentDTO result = packagingContentService.save(packagingContentDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, packagingContentDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /packaging-contents} : get all the packagingContents.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of packagingContents in body.
     */
    @GetMapping("/packaging-contents")
    public List<PackagingContentDTO> getAllPackagingContents() {
        log.debug("REST request to get all PackagingContents");
        return packagingContentService.findAll();
    }

    /**
     * {@code GET  /packaging-contents/:id} : get the "id" packagingContent.
     *
     * @param id the id of the packagingContentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the packagingContentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/packaging-contents/{id}")
    public ResponseEntity<PackagingContentDTO> getPackagingContent(@PathVariable Long id) {
        log.debug("REST request to get PackagingContent : {}", id);
        Optional<PackagingContentDTO> packagingContentDTO = packagingContentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(packagingContentDTO);
    }

    /**
     * {@code DELETE  /packaging-contents/:id} : delete the "id" packagingContent.
     *
     * @param id the id of the packagingContentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/packaging-contents/{id}")
    public ResponseEntity<Void> deletePackagingContent(@PathVariable Long id) {
        log.debug("REST request to delete PackagingContent : {}", id);
        packagingContentService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
