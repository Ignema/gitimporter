package com.gitimporter.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A GitInfo.
 */
@Entity
@Table(name = "git_info")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class GitInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "git_project_id", nullable = false)
    private Integer gitProjectId;

    @NotNull
    @Column(name = "git_project_name", nullable = false)
    private String gitProjectName;

    @NotNull
    @Column(name = "issue_count", nullable = false)
    private Integer issueCount;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getGitProjectId() {
        return gitProjectId;
    }

    public GitInfo gitProjectId(Integer gitProjectId) {
        this.gitProjectId = gitProjectId;
        return this;
    }

    public void setGitProjectId(Integer gitProjectId) {
        this.gitProjectId = gitProjectId;
    }

    public String getGitProjectName() {
        return gitProjectName;
    }

    public GitInfo gitProjectName(String gitProjectName) {
        this.gitProjectName = gitProjectName;
        return this;
    }

    public void setGitProjectName(String gitProjectName) {
        this.gitProjectName = gitProjectName;
    }

    public Integer getIssueCount() {
        return issueCount;
    }

    public GitInfo issueCount(Integer issueCount) {
        this.issueCount = issueCount;
        return this;
    }

    public void setIssueCount(Integer issueCount) {
        this.issueCount = issueCount;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GitInfo)) {
            return false;
        }
        return id != null && id.equals(((GitInfo) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GitInfo{" +
            "id=" + getId() +
            ", gitProjectId=" + getGitProjectId() +
            ", gitProjectName='" + getGitProjectName() + "'" +
            ", issueCount=" + getIssueCount() +
            "}";
    }
}
