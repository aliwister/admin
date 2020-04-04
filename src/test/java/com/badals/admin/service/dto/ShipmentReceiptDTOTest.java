package com.badals.admin.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.badals.admin.web.rest.TestUtil;

public class ShipmentReceiptDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ShipmentReceiptDTO.class);
        ShipmentReceiptDTO shipmentReceiptDTO1 = new ShipmentReceiptDTO();
        shipmentReceiptDTO1.setId(1L);
        ShipmentReceiptDTO shipmentReceiptDTO2 = new ShipmentReceiptDTO();
        assertThat(shipmentReceiptDTO1).isNotEqualTo(shipmentReceiptDTO2);
        shipmentReceiptDTO2.setId(shipmentReceiptDTO1.getId());
        assertThat(shipmentReceiptDTO1).isEqualTo(shipmentReceiptDTO2);
        shipmentReceiptDTO2.setId(2L);
        assertThat(shipmentReceiptDTO1).isNotEqualTo(shipmentReceiptDTO2);
        shipmentReceiptDTO1.setId(null);
        assertThat(shipmentReceiptDTO1).isNotEqualTo(shipmentReceiptDTO2);
    }
}
