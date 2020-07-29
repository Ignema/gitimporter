package com.gitimporter.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.gitimporter.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

public class IssueDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(IssueDTO.class);
        IssueDTO issueDTO1 = new IssueDTO();
        issueDTO1.setId(1L);
        IssueDTO issueDTO2 = new IssueDTO();
        assertThat(issueDTO1).isNotEqualTo(issueDTO2);
        issueDTO2.setId(issueDTO1.getId());
        assertThat(issueDTO1).isEqualTo(issueDTO2);
        issueDTO2.setId(2L);
        assertThat(issueDTO1).isNotEqualTo(issueDTO2);
        issueDTO1.setId(null);
        assertThat(issueDTO1).isNotEqualTo(issueDTO2);
    }
}
