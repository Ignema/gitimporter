package com.gitimporter.web.rest;

import com.gitimporter.service.IssueQueryService;
import com.gitimporter.service.IssueService;
import com.gitimporter.service.dto.IssueCriteria;
import com.gitimporter.service.dto.IssueDTO;
import com.gitimporter.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * REST controller for managing {@link com.gitimporter.domain.Issue}.
 */
@RestController
@RequestMapping("/api")
public class IssueResource {
    private final Logger log = LoggerFactory.getLogger(IssueResource.class);

    private static final String ENTITY_NAME = "issue";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IssueService issueService;

    private final IssueQueryService issueQueryService;

    public IssueResource(IssueService issueService, IssueQueryService issueQueryService) {
        this.issueService = issueService;
        this.issueQueryService = issueQueryService;
    }

    /**
     * {@code POST  /issues} : Create a new issue.
     *
     * @param issueDTO the issueDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new issueDTO, or with status {@code 400 (Bad Request)} if the issue has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/issues")
    public ResponseEntity<IssueDTO> createIssue(@Valid @RequestBody IssueDTO issueDTO) throws URISyntaxException {
        log.debug("REST request to save Issue : {}", issueDTO);
        if (issueDTO.getId() != null) {
            throw new BadRequestAlertException("A new issue cannot already have an ID", ENTITY_NAME, "idexists");
        }
        IssueDTO result = issueService.save(issueDTO);
        return ResponseEntity
            .created(new URI("/api/issues/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /issues} : Updates an existing issue.
     *
     * @param issueDTO the issueDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated issueDTO,
     * or with status {@code 400 (Bad Request)} if the issueDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the issueDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/issues")
    public ResponseEntity<IssueDTO> updateIssue(@Valid @RequestBody IssueDTO issueDTO) throws URISyntaxException {
        log.debug("REST request to update Issue : {}", issueDTO);
        if (issueDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        IssueDTO result = issueService.save(issueDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, issueDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /issues} : get all the issues.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of issues in body.
     */
    @GetMapping("/issues")
    public ResponseEntity<List<IssueDTO>> getAllIssues(IssueCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Issues by criteria: {}", criteria);
        Page<IssueDTO> page = issueQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @PostMapping("/issues/refresh")
    public String refreshGitInfo(@Valid @RequestBody Integer id) {
        try {
            issueService.refresh(id);
        } catch (Exception e) {
            log.debug("refresh service failed: " + e);
        }

        return "";
    }

    /**
     * {@code GET  /issues/count} : count all the issues.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/issues/count")
    public ResponseEntity<Long> countIssues(IssueCriteria criteria) {
        log.debug("REST request to count Issues by criteria: {}", criteria);
        return ResponseEntity.ok().body(issueQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /issues/:id} : get the "id" issue.
     *
     * @param id the id of the issueDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the issueDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/issues/{id}")
    public ResponseEntity<IssueDTO> getIssue(@PathVariable Long id) {
        log.debug("REST request to get Issue : {}", id);
        Optional<IssueDTO> issueDTO = issueService.findOne(id);
        return ResponseUtil.wrapOrNotFound(issueDTO);
    }

    /**
     * {@code DELETE  /issues/:id} : delete the "id" issue.
     *
     * @param id the id of the issueDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/issues/{id}")
    public ResponseEntity<Void> deleteIssue(@PathVariable Long id) {
        log.debug("REST request to delete Issue : {}", id);
        issueService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
