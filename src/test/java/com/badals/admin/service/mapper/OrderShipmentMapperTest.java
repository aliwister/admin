package com.badals.admin.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


public class OrderShipmentMapperTest {

    private OrderShipmentMapper orderShipmentMapper;

    @BeforeEach
    public void setUp() {
        orderShipmentMapper = new OrderShipmentMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 2L;
        assertThat(orderShipmentMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(orderShipmentMapper.fromId(null)).isNull();
    }
}
