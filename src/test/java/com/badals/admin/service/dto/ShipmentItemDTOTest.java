package com.badals.admin.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.badals.admin.web.rest.TestUtil;

public class ShipmentItemDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ShipmentItemDTO.class);
        ShipmentItemDTO shipmentItemDTO1 = new ShipmentItemDTO();
        shipmentItemDTO1.setId(1L);
        ShipmentItemDTO shipmentItemDTO2 = new ShipmentItemDTO();
        assertThat(shipmentItemDTO1).isNotEqualTo(shipmentItemDTO2);
        shipmentItemDTO2.setId(shipmentItemDTO1.getId());
        assertThat(shipmentItemDTO1).isEqualTo(shipmentItemDTO2);
        shipmentItemDTO2.setId(2L);
        assertThat(shipmentItemDTO1).isNotEqualTo(shipmentItemDTO2);
        shipmentItemDTO1.setId(null);
        assertThat(shipmentItemDTO1).isNotEqualTo(shipmentItemDTO2);
    }
}
