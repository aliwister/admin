package com.badals.admin.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


public class ShipmentItemMapperTest {

    private ShipmentItemMapper shipmentItemMapper;

    @BeforeEach
    public void setUp() {
        shipmentItemMapper = new ShipmentItemMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 2L;
        assertThat(shipmentItemMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(shipmentItemMapper.fromId(null)).isNull();
    }
}
