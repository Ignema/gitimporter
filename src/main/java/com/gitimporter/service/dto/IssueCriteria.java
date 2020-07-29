package com.gitimporter.service.dto;

import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.ZonedDateTimeFilter;
import java.io.Serializable;
import java.util.Objects;

/**
 * Criteria class for the {@link com.gitimporter.domain.Issue} entity. This class is used
 * in {@link com.gitimporter.web.rest.IssueResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /issues?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class IssueCriteria implements Serializable, Criteria {
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter issueId;

    private IntegerFilter issueOrder;

    private StringFilter issueTitle;

    private BooleanFilter state;

    private StringFilter author;

    private StringFilter description;

    private ZonedDateTimeFilter createdAt;

    private ZonedDateTimeFilter updatedAt;

    private ZonedDateTimeFilter closedAt;

    private StringFilter closedBy;

    public IssueCriteria() {}

    public IssueCriteria(IssueCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.issueId = other.issueId == null ? null : other.issueId.copy();
        this.issueOrder = other.issueOrder == null ? null : other.issueOrder.copy();
        this.issueTitle = other.issueTitle == null ? null : other.issueTitle.copy();
        this.state = other.state == null ? null : other.state.copy();
        this.author = other.author == null ? null : other.author.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
        this.updatedAt = other.updatedAt == null ? null : other.updatedAt.copy();
        this.closedAt = other.closedAt == null ? null : other.closedAt.copy();
        this.closedBy = other.closedBy == null ? null : other.closedBy.copy();
    }

    @Override
    public IssueCriteria copy() {
        return new IssueCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public IntegerFilter getIssueId() {
        return issueId;
    }

    public void setIssueId(IntegerFilter issueId) {
        this.issueId = issueId;
    }

    public IntegerFilter getIssueOrder() {
        return issueOrder;
    }

    public void setIssueOrder(IntegerFilter issueOrder) {
        this.issueOrder = issueOrder;
    }

    public StringFilter getIssueTitle() {
        return issueTitle;
    }

    public void setIssueTitle(StringFilter issueTitle) {
        this.issueTitle = issueTitle;
    }

    public BooleanFilter getState() {
        return state;
    }

    public void setState(BooleanFilter state) {
        this.state = state;
    }

    public StringFilter getAuthor() {
        return author;
    }

    public void setAuthor(StringFilter author) {
        this.author = author;
    }

    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public ZonedDateTimeFilter getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTimeFilter createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTimeFilter getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(ZonedDateTimeFilter updatedAt) {
        this.updatedAt = updatedAt;
    }

    public ZonedDateTimeFilter getClosedAt() {
        return closedAt;
    }

    public void setClosedAt(ZonedDateTimeFilter closedAt) {
        this.closedAt = closedAt;
    }

    public StringFilter getClosedBy() {
        return closedBy;
    }

    public void setClosedBy(StringFilter closedBy) {
        this.closedBy = closedBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final IssueCriteria that = (IssueCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(issueId, that.issueId) &&
            Objects.equals(issueOrder, that.issueOrder) &&
            Objects.equals(issueTitle, that.issueTitle) &&
            Objects.equals(state, that.state) &&
            Objects.equals(author, that.author) &&
            Objects.equals(description, that.description) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(updatedAt, that.updatedAt) &&
            Objects.equals(closedAt, that.closedAt) &&
            Objects.equals(closedBy, that.closedBy)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, issueId, issueOrder, issueTitle, state, author, description, createdAt, updatedAt, closedAt, closedBy);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IssueCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (issueId != null ? "issueId=" + issueId + ", " : "") +
                (issueOrder != null ? "issueOrder=" + issueOrder + ", " : "") +
                (issueTitle != null ? "issueTitle=" + issueTitle + ", " : "") +
                (state != null ? "state=" + state + ", " : "") +
                (author != null ? "author=" + author + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (createdAt != null ? "createdAt=" + createdAt + ", " : "") +
                (updatedAt != null ? "updatedAt=" + updatedAt + ", " : "") +
                (closedAt != null ? "closedAt=" + closedAt + ", " : "") +
                (closedBy != null ? "closedBy=" + closedBy + ", " : "") +
            "}";
    }
}
