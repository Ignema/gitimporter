package com.gitimporter.domain;

import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Issue.
 */
@Entity
@Table(name = "issue")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Issue implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "issue_id", nullable = false)
    private Integer issueId;

    @NotNull
    @Column(name = "issue_order", nullable = false)
    private Integer issueOrder;

    @NotNull
    @Column(name = "issue_title", nullable = false)
    private String issueTitle;

    @NotNull
    @Column(name = "state", nullable = false)
    private Boolean state;

    @NotNull
    @Column(name = "author", nullable = false)
    private String author;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;

    @NotNull
    @Column(name = "updated_at", nullable = false)
    private ZonedDateTime updatedAt;

    @Column(name = "closed_at")
    private ZonedDateTime closedAt;

    @Column(name = "closed_by")
    private String closedBy;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getIssueId() {
        return issueId;
    }

    public Issue issueId(Integer issueId) {
        this.issueId = issueId;
        return this;
    }

    public void setIssueId(Integer issueId) {
        this.issueId = issueId;
    }

    public Integer getIssueOrder() {
        return issueOrder;
    }

    public Issue issueOrder(Integer issueOrder) {
        this.issueOrder = issueOrder;
        return this;
    }

    public void setIssueOrder(Integer issueOrder) {
        this.issueOrder = issueOrder;
    }

    public String getIssueTitle() {
        return issueTitle;
    }

    public Issue issueTitle(String issueTitle) {
        this.issueTitle = issueTitle;
        return this;
    }

    public void setIssueTitle(String issueTitle) {
        this.issueTitle = issueTitle;
    }

    public Boolean isState() {
        return state;
    }

    public Issue state(Boolean state) {
        this.state = state;
        return this;
    }

    public void setState(Boolean state) {
        this.state = state;
    }

    public String getAuthor() {
        return author;
    }

    public Issue author(String author) {
        this.author = author;
        return this;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public Issue description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public Issue createdAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return updatedAt;
    }

    public Issue updatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public ZonedDateTime getClosedAt() {
        return closedAt;
    }

    public Issue closedAt(ZonedDateTime closedAt) {
        this.closedAt = closedAt;
        return this;
    }

    public void setClosedAt(ZonedDateTime closedAt) {
        this.closedAt = closedAt;
    }

    public String getClosedBy() {
        return closedBy;
    }

    public Issue closedBy(String closedBy) {
        this.closedBy = closedBy;
        return this;
    }

    public void setClosedBy(String closedBy) {
        this.closedBy = closedBy;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Issue)) {
            return false;
        }
        return id != null && id.equals(((Issue) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Issue{" +
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
