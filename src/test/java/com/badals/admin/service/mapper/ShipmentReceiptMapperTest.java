package com.badals.admin.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


public class ShipmentReceiptMapperTest {

    private ShipmentReceiptMapper shipmentReceiptMapper;

    @BeforeEach
    public void setUp() {
        shipmentReceiptMapper = new ShipmentReceiptMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 2L;
        assertThat(shipmentReceiptMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(shipmentReceiptMapper.fromId(null)).isNull();
    }
}
