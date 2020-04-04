package com.badals.admin.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.badals.admin.web.rest.TestUtil;

public class ItemIssuanceDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ItemIssuanceDTO.class);
        ItemIssuanceDTO itemIssuanceDTO1 = new ItemIssuanceDTO();
        itemIssuanceDTO1.setId(1L);
        ItemIssuanceDTO itemIssuanceDTO2 = new ItemIssuanceDTO();
        assertThat(itemIssuanceDTO1).isNotEqualTo(itemIssuanceDTO2);
        itemIssuanceDTO2.setId(itemIssuanceDTO1.getId());
        assertThat(itemIssuanceDTO1).isEqualTo(itemIssuanceDTO2);
        itemIssuanceDTO2.setId(2L);
        assertThat(itemIssuanceDTO1).isNotEqualTo(itemIssuanceDTO2);
        itemIssuanceDTO1.setId(null);
        assertThat(itemIssuanceDTO1).isNotEqualTo(itemIssuanceDTO2);
    }
}
