package com.badals.admin.service.mapper;

import com.badals.admin.domain.Shipment;
import com.badals.admin.repository.ShipmentRepository;
import com.badals.admin.service.dto.ShipmentDTO;
import org.mapstruct.ObjectFactory;
import org.mapstruct.TargetType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//@Component
public class ProgressResolver {
    @Autowired
    private ShipmentRepository shipmentRepository;

    @ObjectFactory
    public ShipmentDTO resolve(Shipment entity, @TargetType Class<ShipmentDTO> type) {
        return null;
    }
}
