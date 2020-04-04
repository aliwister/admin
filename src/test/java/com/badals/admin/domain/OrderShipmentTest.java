package com.badals.admin.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.badals.admin.web.rest.TestUtil;

public class OrderShipmentTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrderShipment.class);
        OrderShipment orderShipment1 = new OrderShipment();
        orderShipment1.setId(1L);
        OrderShipment orderShipment2 = new OrderShipment();
        orderShipment2.setId(orderShipment1.getId());
        assertThat(orderShipment1).isEqualTo(orderShipment2);
        orderShipment2.setId(2L);
        assertThat(orderShipment1).isNotEqualTo(orderShipment2);
        orderShipment1.setId(null);
        assertThat(orderShipment1).isNotEqualTo(orderShipment2);
    }
}
