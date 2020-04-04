package com.badals.admin.service;

import com.badals.admin.domain.PackagingContent;
import com.badals.admin.repository.PackagingContentRepository;
import com.badals.admin.service.dto.PackagingContentDTO;
import com.badals.admin.service.mapper.PackagingContentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link PackagingContent}.
 */
@Service
@Transactional
public class PackagingContentService {

    private final Logger log = LoggerFactory.getLogger(PackagingContentService.class);

    private final PackagingContentRepository packagingContentRepository;

    private final PackagingContentMapper packagingContentMapper;

    public PackagingContentService(PackagingContentRepository packagingContentRepository, PackagingContentMapper packagingContentMapper) {
        this.packagingContentRepository = packagingContentRepository;
        this.packagingContentMapper = packagingContentMapper;
    }

    /**
     * Save a packagingContent.
     *
     * @param packagingContentDTO the entity to save.
     * @return the persisted entity.
     */
    public PackagingContentDTO save(PackagingContentDTO packagingContentDTO) {
        log.debug("Request to save PackagingContent : {}", packagingContentDTO);
        PackagingContent packagingContent = packagingContentMapper.toEntity(packagingContentDTO);
        packagingContent = packagingContentRepository.save(packagingContent);
        return packagingContentMapper.toDto(packagingContent);
    }

    /**
     * Get all the packagingContents.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<PackagingContentDTO> findAll() {
        log.debug("Request to get all PackagingContents");
        return packagingContentRepository.findAll().stream()
            .map(packagingContentMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one packagingContent by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PackagingContentDTO> findOne(Long id) {
        log.debug("Request to get PackagingContent : {}", id);
        return packagingContentRepository.findById(id)
            .map(packagingContentMapper::toDto);
    }

    /**
     * Delete the packagingContent by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete PackagingContent : {}", id);
        packagingContentRepository.deleteById(id);
    }
}
