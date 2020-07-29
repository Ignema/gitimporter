package com.gitimporter.service.dto;

import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import java.io.Serializable;
import java.util.Objects;

/**
 * Criteria class for the {@link com.gitimporter.domain.GitInfo} entity. This class is used
 * in {@link com.gitimporter.web.rest.GitInfoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /git-infos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class GitInfoCriteria implements Serializable, Criteria {
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter gitProjectId;

    private StringFilter gitProjectName;

    private IntegerFilter issueCount;

    public GitInfoCriteria() {}

    public GitInfoCriteria(GitInfoCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.gitProjectId = other.gitProjectId == null ? null : other.gitProjectId.copy();
        this.gitProjectName = other.gitProjectName == null ? null : other.gitProjectName.copy();
        this.issueCount = other.issueCount == null ? null : other.issueCount.copy();
    }

    @Override
    public GitInfoCriteria copy() {
        return new GitInfoCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public IntegerFilter getGitProjectId() {
        return gitProjectId;
    }

    public void setGitProjectId(IntegerFilter gitProjectId) {
        this.gitProjectId = gitProjectId;
    }

    public StringFilter getGitProjectName() {
        return gitProjectName;
    }

    public void setGitProjectName(StringFilter gitProjectName) {
        this.gitProjectName = gitProjectName;
    }

    public IntegerFilter getIssueCount() {
        return issueCount;
    }

    public void setIssueCount(IntegerFilter issueCount) {
        this.issueCount = issueCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final GitInfoCriteria that = (GitInfoCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(gitProjectId, that.gitProjectId) &&
            Objects.equals(gitProjectName, that.gitProjectName) &&
            Objects.equals(issueCount, that.issueCount)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, gitProjectId, gitProjectName, issueCount);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GitInfoCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (gitProjectId != null ? "gitProjectId=" + gitProjectId + ", " : "") +
                (gitProjectName != null ? "gitProjectName=" + gitProjectName + ", " : "") +
                (issueCount != null ? "issueCount=" + issueCount + ", " : "") +
            "}";
    }
}
