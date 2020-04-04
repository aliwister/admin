package com.badals.admin.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


public class PackagingContentMapperTest {

    private PackagingContentMapper packagingContentMapper;

    @BeforeEach
    public void setUp() {
        packagingContentMapper = new PackagingContentMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 2L;
        assertThat(packagingContentMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(packagingContentMapper.fromId(null)).isNull();
    }
}
