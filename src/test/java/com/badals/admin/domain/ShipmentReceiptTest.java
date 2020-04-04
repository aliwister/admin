package com.badals.admin.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.badals.admin.web.rest.TestUtil;

public class ShipmentReceiptTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ShipmentReceipt.class);
        ShipmentReceipt shipmentReceipt1 = new ShipmentReceipt();
        shipmentReceipt1.setId(1L);
        ShipmentReceipt shipmentReceipt2 = new ShipmentReceipt();
        shipmentReceipt2.setId(shipmentReceipt1.getId());
        assertThat(shipmentReceipt1).isEqualTo(shipmentReceipt2);
        shipmentReceipt2.setId(2L);
        assertThat(shipmentReceipt1).isNotEqualTo(shipmentReceipt2);
        shipmentReceipt1.setId(null);
        assertThat(shipmentReceipt1).isNotEqualTo(shipmentReceipt2);
    }
}
