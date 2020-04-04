package com.badals.admin.service;

import com.badals.admin.domain.Pkg;
import com.badals.admin.repository.PkgRepository;
import com.badals.admin.service.dto.PkgDTO;
import com.badals.admin.service.mapper.PkgMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Pkg}.
 */
@Service
@Transactional
public class PkgService {

    private final Logger log = LoggerFactory.getLogger(PkgService.class);

    private final PkgRepository pkgRepository;

    private final PkgMapper pkgMapper;

    public PkgService(PkgRepository pkgRepository, PkgMapper pkgMapper) {
        this.pkgRepository = pkgRepository;
        this.pkgMapper = pkgMapper;
    }

    /**
     * Save a pkg.
     *
     * @param pkgDTO the entity to save.
     * @return the persisted entity.
     */
    public PkgDTO save(PkgDTO pkgDTO) {
        log.debug("Request to save Pkg : {}", pkgDTO);
        Pkg pkg = pkgMapper.toEntity(pkgDTO);
        pkg = pkgRepository.save(pkg);
        return pkgMapper.toDto(pkg);
    }

    /**
     * Get all the pkgs.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<PkgDTO> findAll() {
        log.debug("Request to get all Pkgs");
        return pkgRepository.findAll().stream()
            .map(pkgMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one pkg by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PkgDTO> findOne(Long id) {
        log.debug("Request to get Pkg : {}", id);
        return pkgRepository.findById(id)
            .map(pkgMapper::toDto);
    }

    /**
     * Delete the pkg by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Pkg : {}", id);
        pkgRepository.deleteById(id);
    }
}
