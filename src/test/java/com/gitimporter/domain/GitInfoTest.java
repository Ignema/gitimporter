package com.gitimporter.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.gitimporter.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

public class GitInfoTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(GitInfo.class);
        GitInfo gitInfo1 = new GitInfo();
        gitInfo1.setId(1L);
        GitInfo gitInfo2 = new GitInfo();
        gitInfo2.setId(gitInfo1.getId());
        assertThat(gitInfo1).isEqualTo(gitInfo2);
        gitInfo2.setId(2L);
        assertThat(gitInfo1).isNotEqualTo(gitInfo2);
        gitInfo1.setId(null);
        assertThat(gitInfo1).isNotEqualTo(gitInfo2);
    }
}
