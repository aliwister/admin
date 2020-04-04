package com.badals.admin.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.badals.admin.web.rest.TestUtil;

public class PackagingContentTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PackagingContent.class);
        PackagingContent packagingContent1 = new PackagingContent();
        packagingContent1.setId(1L);
        PackagingContent packagingContent2 = new PackagingContent();
        packagingContent2.setId(packagingContent1.getId());
        assertThat(packagingContent1).isEqualTo(packagingContent2);
        packagingContent2.setId(2L);
        assertThat(packagingContent1).isNotEqualTo(packagingContent2);
        packagingContent1.setId(null);
        assertThat(packagingContent1).isNotEqualTo(packagingContent2);
    }
}
