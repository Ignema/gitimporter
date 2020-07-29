package com.gitimporter.service;

import com.gitimporter.domain.*; // for static metamodels
import com.gitimporter.domain.GitInfo;
import com.gitimporter.repository.GitInfoRepository;
import com.gitimporter.service.dto.GitInfoCriteria;
import com.gitimporter.service.dto.GitInfoDTO;
import com.gitimporter.service.mapper.GitInfoMapper;
import io.github.jhipster.service.QueryService;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for executing complex queries for {@link GitInfo} entities in the database.
 * The main input is a {@link GitInfoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link GitInfoDTO} or a {@link Page} of {@link GitInfoDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class GitInfoQueryService extends QueryService<GitInfo> {
    private final Logger log = LoggerFactory.getLogger(GitInfoQueryService.class);

    private final GitInfoRepository gitInfoRepository;

    private final GitInfoMapper gitInfoMapper;

    @Autowired
    public GitInfoQueryService(GitInfoRepository gitInfoRepository, GitInfoMapper gitInfoMapper) {
        this.gitInfoRepository = gitInfoRepository;
        this.gitInfoMapper = gitInfoMapper;
    }

    /**
     * Return a {@link List} of {@link GitInfoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<GitInfoDTO> findByCriteria(GitInfoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<GitInfo> specification = createSpecification(criteria);
        return gitInfoMapper.toDto(gitInfoRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link GitInfoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<GitInfoDTO> findByCriteria(GitInfoCriteria criteria, Pageable page) throws IOException, ParseException {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<GitInfo> specification = createSpecification(criteria);
        return gitInfoRepository.findAll(specification, page).map(gitInfoMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(GitInfoCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<GitInfo> specification = createSpecification(criteria);
        return gitInfoRepository.count(specification);
    }

    /**
     * Function to convert {@link GitInfoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<GitInfo> createSpecification(GitInfoCriteria criteria) {
        Specification<GitInfo> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), GitInfo_.id));
            }
            if (criteria.getGitProjectId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getGitProjectId(), GitInfo_.gitProjectId));
            }
            if (criteria.getGitProjectName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getGitProjectName(), GitInfo_.gitProjectName));
            }
            if (criteria.getIssueCount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getIssueCount(), GitInfo_.issueCount));
            }
        }
        return specification;
    }
}
