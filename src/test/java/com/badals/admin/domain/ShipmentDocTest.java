package com.badals.admin.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.badals.admin.web.rest.TestUtil;

public class ShipmentDocTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ShipmentDoc.class);
        ShipmentDoc shipmentDoc1 = new ShipmentDoc();
        shipmentDoc1.setId(1L);
        ShipmentDoc shipmentDoc2 = new ShipmentDoc();
        shipmentDoc2.setId(shipmentDoc1.getId());
        assertThat(shipmentDoc1).isEqualTo(shipmentDoc2);
        shipmentDoc2.setId(2L);
        assertThat(shipmentDoc1).isNotEqualTo(shipmentDoc2);
        shipmentDoc1.setId(null);
        assertThat(shipmentDoc1).isNotEqualTo(shipmentDoc2);
    }
}
