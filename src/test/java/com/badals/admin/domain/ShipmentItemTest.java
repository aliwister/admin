package com.badals.admin.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.badals.admin.web.rest.TestUtil;

public class ShipmentItemTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ShipmentItem.class);
        ShipmentItem shipmentItem1 = new ShipmentItem();
        shipmentItem1.setId(1L);
        ShipmentItem shipmentItem2 = new ShipmentItem();
        shipmentItem2.setId(shipmentItem1.getId());
        assertThat(shipmentItem1).isEqualTo(shipmentItem2);
        shipmentItem2.setId(2L);
        assertThat(shipmentItem1).isNotEqualTo(shipmentItem2);
        shipmentItem1.setId(null);
        assertThat(shipmentItem1).isNotEqualTo(shipmentItem2);
    }
}
