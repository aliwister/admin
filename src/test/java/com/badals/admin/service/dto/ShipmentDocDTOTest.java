package com.badals.admin.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.badals.admin.web.rest.TestUtil;

public class ShipmentDocDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ShipmentDocDTO.class);
        ShipmentDocDTO shipmentDocDTO1 = new ShipmentDocDTO();
        shipmentDocDTO1.setId(1L);
        ShipmentDocDTO shipmentDocDTO2 = new ShipmentDocDTO();
        assertThat(shipmentDocDTO1).isNotEqualTo(shipmentDocDTO2);
        shipmentDocDTO2.setId(shipmentDocDTO1.getId());
        assertThat(shipmentDocDTO1).isEqualTo(shipmentDocDTO2);
        shipmentDocDTO2.setId(2L);
        assertThat(shipmentDocDTO1).isNotEqualTo(shipmentDocDTO2);
        shipmentDocDTO1.setId(null);
        assertThat(shipmentDocDTO1).isNotEqualTo(shipmentDocDTO2);
    }
}
