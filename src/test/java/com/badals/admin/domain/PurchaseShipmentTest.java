package com.badals.admin.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.badals.admin.web.rest.TestUtil;

public class PurchaseShipmentTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PurchaseShipment.class);
        PurchaseShipment purchaseShipment1 = new PurchaseShipment();
        purchaseShipment1.setId(1L);
        PurchaseShipment purchaseShipment2 = new PurchaseShipment();
        purchaseShipment2.setId(purchaseShipment1.getId());
        assertThat(purchaseShipment1).isEqualTo(purchaseShipment2);
        purchaseShipment2.setId(2L);
        assertThat(purchaseShipment1).isNotEqualTo(purchaseShipment2);
        purchaseShipment1.setId(null);
        assertThat(purchaseShipment1).isNotEqualTo(purchaseShipment2);
    }
}
