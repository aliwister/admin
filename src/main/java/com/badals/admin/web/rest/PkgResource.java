package com.badals.admin.web.rest;

import com.badals.admin.service.PkgService;
import com.badals.admin.web.rest.errors.BadRequestAlertException;
import com.badals.admin.service.dto.PkgDTO;

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
 * REST controller for managing {@link com.badals.admin.domain.Pkg}.
 */
@RestController
@RequestMapping("/api")
public class PkgResource {

    private final Logger log = LoggerFactory.getLogger(PkgResource.class);

    private static final String ENTITY_NAME = "adminPkg";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PkgService pkgService;

    public PkgResource(PkgService pkgService) {
        this.pkgService = pkgService;
    }

    /**
     * {@code POST  /pkgs} : Create a new pkg.
     *
     * @param pkgDTO the pkgDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pkgDTO, or with status {@code 400 (Bad Request)} if the pkg has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/pkgs")
    public ResponseEntity<PkgDTO> createPkg(@RequestBody PkgDTO pkgDTO) throws URISyntaxException {
        log.debug("REST request to save Pkg : {}", pkgDTO);
        if (pkgDTO.getId() != null) {
            throw new BadRequestAlertException("A new pkg cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PkgDTO result = pkgService.save(pkgDTO);
        return ResponseEntity.created(new URI("/api/pkgs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /pkgs} : Updates an existing pkg.
     *
     * @param pkgDTO the pkgDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pkgDTO,
     * or with status {@code 400 (Bad Request)} if the pkgDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pkgDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/pkgs")
    public ResponseEntity<PkgDTO> updatePkg(@RequestBody PkgDTO pkgDTO) throws URISyntaxException {
        log.debug("REST request to update Pkg : {}", pkgDTO);
        if (pkgDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PkgDTO result = pkgService.save(pkgDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pkgDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /pkgs} : get all the pkgs.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pkgs in body.
     */
    @GetMapping("/pkgs")
    public List<PkgDTO> getAllPkgs() {
        log.debug("REST request to get all Pkgs");
        return pkgService.findAll();
    }

    /**
     * {@code GET  /pkgs/:id} : get the "id" pkg.
     *
     * @param id the id of the pkgDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pkgDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/pkgs/{id}")
    public ResponseEntity<PkgDTO> getPkg(@PathVariable Long id) {
        log.debug("REST request to get Pkg : {}", id);
        Optional<PkgDTO> pkgDTO = pkgService.findOne(id);
        return ResponseUtil.wrapOrNotFound(pkgDTO);
    }

    /**
     * {@code DELETE  /pkgs/:id} : delete the "id" pkg.
     *
     * @param id the id of the pkgDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/pkgs/{id}")
    public ResponseEntity<Void> deletePkg(@PathVariable Long id) {
        log.debug("REST request to delete Pkg : {}", id);
        pkgService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
