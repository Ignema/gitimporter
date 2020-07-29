package com.gitimporter.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GitInfoMapperTest {
    private GitInfoMapper gitInfoMapper;

    @BeforeEach
    public void setUp() {
        gitInfoMapper = new GitInfoMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(gitInfoMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(gitInfoMapper.fromId(null)).isNull();
    }
}
