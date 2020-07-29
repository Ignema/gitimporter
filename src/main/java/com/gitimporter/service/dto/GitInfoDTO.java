package com.gitimporter.service.dto;

import com.gitimporter.domain.GitInfo;
import java.io.Serializable;
import java.util.List;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.gitimporter.domain.GitInfo} entity.
 */

public class GitInfoDTO implements Serializable {
    private Long id;

    @NotNull
    private Integer gitProjectId;

    @NotNull
    private String gitProjectName;

    @NotNull
    private Integer issueCount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getGitProjectId() {
        return gitProjectId;
    }

    public void setGitProjectId(Integer gitProjectId) {
        this.gitProjectId = gitProjectId;
    }

    public String getGitProjectName() {
        return gitProjectName;
    }

    public void setGitProjectName(String gitProjectName) {
        this.gitProjectName = gitProjectName;
    }

    public Integer getIssueCount() {
        return issueCount;
    }

    public void setIssueCount(Integer issueCount) {
        this.issueCount = issueCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GitInfoDTO)) {
            return false;
        }

        return id != null && id.equals(((GitInfoDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GitInfoDTO{" +
            "id=" + getId() +
            ", gitProjectId=" + getGitProjectId() +
            ", gitProjectName='" + getGitProjectName() + "'" +
            ", issueCount=" + getIssueCount() +
            "}";
    }
}
