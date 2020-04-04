package com.badals.admin.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.badals.admin.web.rest.TestUtil;

public class PurchaseShipmentDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PurchaseShipmentDTO.class);
        PurchaseShipmentDTO purchaseShipmentDTO1 = new PurchaseShipmentDTO();
        purchaseShipmentDTO1.setId(1L);
        PurchaseShipmentDTO purchaseShipmentDTO2 = new PurchaseShipmentDTO();
        assertThat(purchaseShipmentDTO1).isNotEqualTo(purchaseShipmentDTO2);
        purchaseShipmentDTO2.setId(purchaseShipmentDTO1.getId());
        assertThat(purchaseShipmentDTO1).isEqualTo(purchaseShipmentDTO2);
        purchaseShipmentDTO2.setId(2L);
        assertThat(purchaseShipmentDTO1).isNotEqualTo(purchaseShipmentDTO2);
        purchaseShipmentDTO1.setId(null);
        assertThat(purchaseShipmentDTO1).isNotEqualTo(purchaseShipmentDTO2);
    }
}
