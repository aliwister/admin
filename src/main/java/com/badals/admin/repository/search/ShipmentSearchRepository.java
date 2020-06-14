package com.badals.admin.repository.search;

import com.badals.admin.service.dto.ShipmentDTO;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ShipmentSearchRepository extends ElasticsearchRepository<ShipmentDTO, Long> {
    ShipmentDTO findByTrackingNum(String trackingNum);
}
