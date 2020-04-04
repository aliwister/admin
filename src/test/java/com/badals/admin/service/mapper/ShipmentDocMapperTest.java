package com.badals.admin.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


public class ShipmentDocMapperTest {

    private ShipmentDocMapper shipmentDocMapper;

    @BeforeEach
    public void setUp() {
        shipmentDocMapper = new ShipmentDocMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 2L;
        assertThat(shipmentDocMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(shipmentDocMapper.fromId(null)).isNull();
    }
}
