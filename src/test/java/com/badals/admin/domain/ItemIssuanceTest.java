package com.badals.admin.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.badals.admin.web.rest.TestUtil;

public class ItemIssuanceTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ItemIssuance.class);
        ItemIssuance itemIssuance1 = new ItemIssuance();
        itemIssuance1.setId(1L);
        ItemIssuance itemIssuance2 = new ItemIssuance();
        itemIssuance2.setId(itemIssuance1.getId());
        assertThat(itemIssuance1).isEqualTo(itemIssuance2);
        itemIssuance2.setId(2L);
        assertThat(itemIssuance1).isNotEqualTo(itemIssuance2);
        itemIssuance1.setId(null);
        assertThat(itemIssuance1).isNotEqualTo(itemIssuance2);
    }
}
