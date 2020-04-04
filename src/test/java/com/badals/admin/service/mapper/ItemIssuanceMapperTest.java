package com.badals.admin.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


public class ItemIssuanceMapperTest {

    private ItemIssuanceMapper itemIssuanceMapper;

    @BeforeEach
    public void setUp() {
        itemIssuanceMapper = new ItemIssuanceMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 2L;
        assertThat(itemIssuanceMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(itemIssuanceMapper.fromId(null)).isNull();
    }
}
