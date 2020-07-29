package com.gitimporter.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.gitimporter.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

public class GitInfoDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(GitInfoDTO.class);
        GitInfoDTO gitInfoDTO1 = new GitInfoDTO();
        gitInfoDTO1.setId(1L);
        GitInfoDTO gitInfoDTO2 = new GitInfoDTO();
        assertThat(gitInfoDTO1).isNotEqualTo(gitInfoDTO2);
        gitInfoDTO2.setId(gitInfoDTO1.getId());
        assertThat(gitInfoDTO1).isEqualTo(gitInfoDTO2);
        gitInfoDTO2.setId(2L);
        assertThat(gitInfoDTO1).isNotEqualTo(gitInfoDTO2);
        gitInfoDTO1.setId(null);
        assertThat(gitInfoDTO1).isNotEqualTo(gitInfoDTO2);
    }
}
