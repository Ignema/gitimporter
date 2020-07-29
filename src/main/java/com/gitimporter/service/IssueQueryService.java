package com.gitimporter.service;

import com.gitimporter.domain.*; // for static metamodels
import com.gitimporter.domain.Issue;
import com.gitimporter.repository.IssueRepository;
import com.gitimporter.service.dto.IssueCriteria;
import com.gitimporter.service.dto.IssueDTO;
import com.gitimporter.service.mapper.IssueMapper;
import io.github.jhipster.service.QueryService;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for executing complex queries for {@link Issue} entities in the database.
 * The main input is a {@link IssueCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link IssueDTO} or a {@link Page} of {@link IssueDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class IssueQueryService extends QueryService<Issue> {
    private final Logger log = LoggerFactory.getLogger(IssueQueryService.class);

    private final IssueRepository issueRepository;

    private final IssueMapper issueMapper;

    public IssueQueryService(IssueRepository issueRepository, IssueMapper issueMapper) {
        this.issueRepository = issueRepository;
        this.issueMapper = issueMapper;
    }

    /**
     * Return a {@link List} of {@link IssueDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<IssueDTO> findByCriteria(IssueCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Issue> specification = createSpecification(criteria);
        return issueMapper.toDto(issueRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link IssueDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<IssueDTO> findByCriteria(IssueCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Issue> specification = createSpecification(criteria);
        return issueRepository.findAll(specification, page).map(issueMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(IssueCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Issue> specification = createSpecification(criteria);
        return issueRepository.count(specification);
    }

    /**
     * Function to convert {@link IssueCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Issue> createSpecification(IssueCriteria criteria) {
        Specification<Issue> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Issue_.id));
            }
            if (criteria.getIssueId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getIssueId(), Issue_.issueId));
            }
            if (criteria.getIssueOrder() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getIssueOrder(), Issue_.issueOrder));
            }
            if (criteria.getIssueTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIssueTitle(), Issue_.issueTitle));
            }
            if (criteria.getState() != null) {
                specification = specification.and(buildSpecification(criteria.getState(), Issue_.state));
            }
            if (criteria.getAuthor() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAuthor(), Issue_.author));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Issue_.description));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), Issue_.createdAt));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), Issue_.updatedAt));
            }
            if (criteria.getClosedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getClosedAt(), Issue_.closedAt));
            }
            if (criteria.getClosedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getClosedBy(), Issue_.closedBy));
            }
        }
        return specification;
    }
}
