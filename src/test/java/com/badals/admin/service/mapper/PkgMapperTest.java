package com.badals.admin.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


public class PkgMapperTest {

    private PkgMapper pkgMapper;

    @BeforeEach
    public void setUp() {
        pkgMapper = new PkgMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 2L;
        assertThat(pkgMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(pkgMapper.fromId(null)).isNull();
    }
}
