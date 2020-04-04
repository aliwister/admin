package com.badals.admin.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.badals.admin.web.rest.TestUtil;

public class PackagingContentDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PackagingContentDTO.class);
        PackagingContentDTO packagingContentDTO1 = new PackagingContentDTO();
        packagingContentDTO1.setId(1L);
        PackagingContentDTO packagingContentDTO2 = new PackagingContentDTO();
        assertThat(packagingContentDTO1).isNotEqualTo(packagingContentDTO2);
        packagingContentDTO2.setId(packagingContentDTO1.getId());
        assertThat(packagingContentDTO1).isEqualTo(packagingContentDTO2);
        packagingContentDTO2.setId(2L);
        assertThat(packagingContentDTO1).isNotEqualTo(packagingContentDTO2);
        packagingContentDTO1.setId(null);
        assertThat(packagingContentDTO1).isNotEqualTo(packagingContentDTO2);
    }
}
