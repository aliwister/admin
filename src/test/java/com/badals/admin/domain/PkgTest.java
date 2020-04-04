package com.badals.admin.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.badals.admin.web.rest.TestUtil;

public class PkgTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Pkg.class);
        Pkg pkg1 = new Pkg();
        pkg1.setId(1L);
        Pkg pkg2 = new Pkg();
        pkg2.setId(pkg1.getId());
        assertThat(pkg1).isEqualTo(pkg2);
        pkg2.setId(2L);
        assertThat(pkg1).isNotEqualTo(pkg2);
        pkg1.setId(null);
        assertThat(pkg1).isNotEqualTo(pkg2);
    }
}
