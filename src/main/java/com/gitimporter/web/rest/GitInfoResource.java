package com.gitimporter.web.rest;

import com.gitimporter.service.GitInfoQueryService;
import com.gitimporter.service.GitInfoService;
import com.gitimporter.service.dto.GitInfoCriteria;
import com.gitimporter.service.dto.GitInfoDTO;
import com.gitimporter.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import org.json.simple.parser.ParseException;
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
 * REST controller for managing {@link com.gitimporter.domain.GitInfo}.
 */
@RestController
@RequestMapping("/api")
public class GitInfoResource {
    private final Logger log = LoggerFactory.getLogger(GitInfoResource.class);

    private static final String ENTITY_NAME = "gitInfo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GitInfoService gitInfoService;

    private final GitInfoQueryService gitInfoQueryService;

    public GitInfoResource(GitInfoService gitInfoService, GitInfoQueryService gitInfoQueryService) {
        this.gitInfoService = gitInfoService;
        this.gitInfoQueryService = gitInfoQueryService;
    }

    /**
     * {@code POST  /git-infos} : Create a new gitInfo.
     *
     * @param gitInfoDTO the gitInfoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new gitInfoDTO, or with status {@code 400 (Bad Request)} if the gitInfo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/git-infos")
    public ResponseEntity<GitInfoDTO> createGitInfo(@Valid @RequestBody GitInfoDTO gitInfoDTO) throws URISyntaxException {
        log.debug("REST request to save GitInfo : {}", gitInfoDTO);
        if (gitInfoDTO.getId() != null) {
            throw new BadRequestAlertException("A new gitInfo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GitInfoDTO result = gitInfoService.save(gitInfoDTO);
        return ResponseEntity
            .created(new URI("/api/git-infos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /git-infos} : Updates an existing gitInfo.
     *
     * @param gitInfoDTO the gitInfoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated gitInfoDTO,
     * or with status {@code 400 (Bad Request)} if the gitInfoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the gitInfoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/git-infos")
    public ResponseEntity<GitInfoDTO> updateGitInfo(@Valid @RequestBody GitInfoDTO gitInfoDTO) throws URISyntaxException {
        log.debug("REST request to update GitInfo : {}", gitInfoDTO);
        if (gitInfoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        GitInfoDTO result = gitInfoService.save(gitInfoDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, gitInfoDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /git-infos} : get all the gitInfos.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of gitInfos in body.
     */
    @GetMapping("/git-infos")
    public ResponseEntity<List<GitInfoDTO>> getAllGitInfos(GitInfoCriteria criteria, Pageable pageable) throws IOException, ParseException {
        log.debug("REST request to get GitInfos by criteria: {}", criteria);

        Page<GitInfoDTO> page = gitInfoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @PostMapping("/git-infos/refresh")
    public String refreshGitInfo(@Valid @RequestBody String username) {
        try {
            gitInfoService.refresh(username);
        } catch (Exception e) {
            log.debug("refresh service failed: " + e);
        }

        return "";
    }

    /**
     * {@code GET  /git-infos/count} : count all the gitInfos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/git-infos/count")
    public ResponseEntity<Long> countGitInfos(GitInfoCriteria criteria) {
        log.debug("REST request to count GitInfos by criteria: {}", criteria);
        return ResponseEntity.ok().body(gitInfoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /git-infos/:id} : get the "id" gitInfo.
     *
     * @param id the id of the gitInfoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the gitInfoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/git-infos/{id}")
    public ResponseEntity getGitInfo(@PathVariable Long id) throws IOException, ParseException {
        log.debug("REST request to get GitInfo : {}", id);

        Optional<GitInfoDTO> gitInfoDTO = gitInfoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(gitInfoDTO);
    }

    /**
     * {@code DELETE  /git-infos/:id} : delete the "id" gitInfo.
     *
     * @param id the id of the gitInfoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/git-infos/{id}")
    public ResponseEntity<Void> deleteGitInfo(@PathVariable Long id) {
        log.debug("REST request to delete GitInfo : {}", id);
        gitInfoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
