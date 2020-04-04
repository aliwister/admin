package com.badals.admin.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.badals.admin.web.rest.TestUtil;

public class OrderShipmentDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrderShipmentDTO.class);
        OrderShipmentDTO orderShipmentDTO1 = new OrderShipmentDTO();
        orderShipmentDTO1.setId(1L);
        OrderShipmentDTO orderShipmentDTO2 = new OrderShipmentDTO();
        assertThat(orderShipmentDTO1).isNotEqualTo(orderShipmentDTO2);
        orderShipmentDTO2.setId(orderShipmentDTO1.getId());
        assertThat(orderShipmentDTO1).isEqualTo(orderShipmentDTO2);
        orderShipmentDTO2.setId(2L);
        assertThat(orderShipmentDTO1).isNotEqualTo(orderShipmentDTO2);
        orderShipmentDTO1.setId(null);
        assertThat(orderShipmentDTO1).isNotEqualTo(orderShipmentDTO2);
    }
}
