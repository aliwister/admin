package com.badals.admin.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.badals.admin.web.rest.TestUtil;

public class PkgDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PkgDTO.class);
        PkgDTO pkgDTO1 = new PkgDTO();
        pkgDTO1.setId(1L);
        PkgDTO pkgDTO2 = new PkgDTO();
        assertThat(pkgDTO1).isNotEqualTo(pkgDTO2);
        pkgDTO2.setId(pkgDTO1.getId());
        assertThat(pkgDTO1).isEqualTo(pkgDTO2);
        pkgDTO2.setId(2L);
        assertThat(pkgDTO1).isNotEqualTo(pkgDTO2);
        pkgDTO1.setId(null);
        assertThat(pkgDTO1).isNotEqualTo(pkgDTO2);
    }
}
