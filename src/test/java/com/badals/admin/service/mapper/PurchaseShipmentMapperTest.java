package com.badals.admin.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


public class PurchaseShipmentMapperTest {

    private PurchaseShipmentMapper purchaseShipmentMapper;

    @BeforeEach
    public void setUp() {
        purchaseShipmentMapper = new PurchaseShipmentMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 2L;
        assertThat(purchaseShipmentMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(purchaseShipmentMapper.fromId(null)).isNull();
    }
}
