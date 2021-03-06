package com.gitimporter.service.dto;

import java.io.Serializable;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.gitimporter.domain.Issue} entity.
 */
public class IssueDTO implements Serializable {
    private Long id;

    @NotNull
    private Integer issueId;

    @NotNull
    private Integer issueOrder;

    @NotNull
    private String issueTitle;

    @NotNull
    private Boolean state;

    @NotNull
    private String author;

    @NotNull
    private String description;

    private String createdAt;

    private String updatedAt;

    private String closedAt;

    private String closedBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getIssueId() {
        return issueId;
    }

    public void setIssueId(Integer issueId) {
        this.issueId = issueId;
    }

    public Integer getIssueOrder() {
        return issueOrder;
    }

    public void setIssueOrder(Integer issueOrder) {
        this.issueOrder = issueOrder;
    }

    public String getIssueTitle() {
        return issueTitle;
    }

    public void setIssueTitle(String issueTitle) {
        this.issueTitle = issueTitle;
    }

    public Boolean isState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getClosedAt() {
        return closedAt;
    }

    public void setClosedAt(String closedAt) {
        this.closedAt = closedAt;
    }

    public String getClosedBy() {
        return closedBy;
    }

    public void setClosedBy(String closedBy) {
        this.closedBy = closedBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IssueDTO)) {
            return false;
        }

        return id != null && id.equals(((IssueDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IssueDTO{" +
            "id=" + getId() +
            ", issueId=" + getIssueId() +
            ", issueOrder=" + getIssueOrder() +
            ", issueTitle='" + getIssueTitle() + "'" +
            ", state='" + isState() + "'" +
            ", author='" + getAuthor() + "'" +
            ", description='" + getDescription() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", closedAt='" + getClosedAt() + "'" +
            ", closedBy='" + getClosedBy() + "'" +
            "}";
    }
}
