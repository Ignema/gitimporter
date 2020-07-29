package com.gitimporter.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.gitimporter.GitimporterApp;
import com.gitimporter.domain.GitInfo;
import com.gitimporter.repository.GitInfoRepository;
import com.gitimporter.service.GitInfoQueryService;
import com.gitimporter.service.GitInfoService;
import com.gitimporter.service.dto.GitInfoCriteria;
import com.gitimporter.service.dto.GitInfoDTO;
import com.gitimporter.service.mapper.GitInfoMapper;
import java.util.List;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link GitInfoResource} REST controller.
 */
@SpringBootTest(classes = GitimporterApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class GitInfoResourceIT {
    private static final Integer DEFAULT_GIT_PROJECT_ID = 1;
    private static final Integer UPDATED_GIT_PROJECT_ID = 2;
    private static final Integer SMALLER_GIT_PROJECT_ID = 1 - 1;

    private static final String DEFAULT_GIT_PROJECT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_GIT_PROJECT_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_ISSUE_COUNT = 1;
    private static final Integer UPDATED_ISSUE_COUNT = 2;
    private static final Integer SMALLER_ISSUE_COUNT = 1 - 1;

    @Autowired
    private GitInfoRepository gitInfoRepository;

    @Autowired
    private GitInfoMapper gitInfoMapper;

    @Autowired
    private GitInfoService gitInfoService;

    @Autowired
    private GitInfoQueryService gitInfoQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGitInfoMockMvc;

    private GitInfo gitInfo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GitInfo createEntity(EntityManager em) {
        GitInfo gitInfo = new GitInfo()
            .gitProjectId(DEFAULT_GIT_PROJECT_ID)
            .gitProjectName(DEFAULT_GIT_PROJECT_NAME)
            .issueCount(DEFAULT_ISSUE_COUNT);
        return gitInfo;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GitInfo createUpdatedEntity(EntityManager em) {
        GitInfo gitInfo = new GitInfo()
            .gitProjectId(UPDATED_GIT_PROJECT_ID)
            .gitProjectName(UPDATED_GIT_PROJECT_NAME)
            .issueCount(UPDATED_ISSUE_COUNT);
        return gitInfo;
    }

    @BeforeEach
    public void initTest() {
        gitInfo = createEntity(em);
    }

    @Test
    @Transactional
    public void createGitInfo() throws Exception {
        int databaseSizeBeforeCreate = gitInfoRepository.findAll().size();
        // Create the GitInfo
        GitInfoDTO gitInfoDTO = gitInfoMapper.toDto(gitInfo);
        restGitInfoMockMvc
            .perform(post("/api/git-infos").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gitInfoDTO)))
            .andExpect(status().isCreated());

        // Validate the GitInfo in the database
        List<GitInfo> gitInfoList = gitInfoRepository.findAll();
        assertThat(gitInfoList).hasSize(databaseSizeBeforeCreate + 1);
        GitInfo testGitInfo = gitInfoList.get(gitInfoList.size() - 1);
        assertThat(testGitInfo.getGitProjectId()).isEqualTo(DEFAULT_GIT_PROJECT_ID);
        assertThat(testGitInfo.getGitProjectName()).isEqualTo(DEFAULT_GIT_PROJECT_NAME);
        assertThat(testGitInfo.getIssueCount()).isEqualTo(DEFAULT_ISSUE_COUNT);
    }

    @Test
    @Transactional
    public void createGitInfoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = gitInfoRepository.findAll().size();

        // Create the GitInfo with an existing ID
        gitInfo.setId(1L);
        GitInfoDTO gitInfoDTO = gitInfoMapper.toDto(gitInfo);

        // An entity with an existing ID cannot be created, so this API call must fail
        restGitInfoMockMvc
            .perform(post("/api/git-infos").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gitInfoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the GitInfo in the database
        List<GitInfo> gitInfoList = gitInfoRepository.findAll();
        assertThat(gitInfoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkGitProjectIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = gitInfoRepository.findAll().size();
        // set the field null
        gitInfo.setGitProjectId(null);

        // Create the GitInfo, which fails.
        GitInfoDTO gitInfoDTO = gitInfoMapper.toDto(gitInfo);

        restGitInfoMockMvc
            .perform(post("/api/git-infos").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gitInfoDTO)))
            .andExpect(status().isBadRequest());

        List<GitInfo> gitInfoList = gitInfoRepository.findAll();
        assertThat(gitInfoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkGitProjectNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = gitInfoRepository.findAll().size();
        // set the field null
        gitInfo.setGitProjectName(null);

        // Create the GitInfo, which fails.
        GitInfoDTO gitInfoDTO = gitInfoMapper.toDto(gitInfo);

        restGitInfoMockMvc
            .perform(post("/api/git-infos").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gitInfoDTO)))
            .andExpect(status().isBadRequest());

        List<GitInfo> gitInfoList = gitInfoRepository.findAll();
        assertThat(gitInfoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIssueCountIsRequired() throws Exception {
        int databaseSizeBeforeTest = gitInfoRepository.findAll().size();
        // set the field null
        gitInfo.setIssueCount(null);

        // Create the GitInfo, which fails.
        GitInfoDTO gitInfoDTO = gitInfoMapper.toDto(gitInfo);

        restGitInfoMockMvc
            .perform(post("/api/git-infos").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gitInfoDTO)))
            .andExpect(status().isBadRequest());

        List<GitInfo> gitInfoList = gitInfoRepository.findAll();
        assertThat(gitInfoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllGitInfos() throws Exception {
        // Initialize the database
        gitInfoRepository.saveAndFlush(gitInfo);

        // Get all the gitInfoList
        restGitInfoMockMvc
            .perform(get("/api/git-infos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(gitInfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].gitProjectId").value(hasItem(DEFAULT_GIT_PROJECT_ID)))
            .andExpect(jsonPath("$.[*].gitProjectName").value(hasItem(DEFAULT_GIT_PROJECT_NAME)))
            .andExpect(jsonPath("$.[*].issueCount").value(hasItem(DEFAULT_ISSUE_COUNT)));
    }

    @Test
    @Transactional
    public void getGitInfo() throws Exception {
        // Initialize the database
        gitInfoRepository.saveAndFlush(gitInfo);

        // Get the gitInfo
        restGitInfoMockMvc
            .perform(get("/api/git-infos/{id}", gitInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(gitInfo.getId().intValue()))
            .andExpect(jsonPath("$.gitProjectId").value(DEFAULT_GIT_PROJECT_ID))
            .andExpect(jsonPath("$.gitProjectName").value(DEFAULT_GIT_PROJECT_NAME))
            .andExpect(jsonPath("$.issueCount").value(DEFAULT_ISSUE_COUNT));
    }

    @Test
    @Transactional
    public void getGitInfosByIdFiltering() throws Exception {
        // Initialize the database
        gitInfoRepository.saveAndFlush(gitInfo);

        Long id = gitInfo.getId();

        defaultGitInfoShouldBeFound("id.equals=" + id);
        defaultGitInfoShouldNotBeFound("id.notEquals=" + id);

        defaultGitInfoShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultGitInfoShouldNotBeFound("id.greaterThan=" + id);

        defaultGitInfoShouldBeFound("id.lessThanOrEqual=" + id);
        defaultGitInfoShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    public void getAllGitInfosByGitProjectIdIsEqualToSomething() throws Exception {
        // Initialize the database
        gitInfoRepository.saveAndFlush(gitInfo);

        // Get all the gitInfoList where gitProjectId equals to DEFAULT_GIT_PROJECT_ID
        defaultGitInfoShouldBeFound("gitProjectId.equals=" + DEFAULT_GIT_PROJECT_ID);

        // Get all the gitInfoList where gitProjectId equals to UPDATED_GIT_PROJECT_ID
        defaultGitInfoShouldNotBeFound("gitProjectId.equals=" + UPDATED_GIT_PROJECT_ID);
    }

    @Test
    @Transactional
    public void getAllGitInfosByGitProjectIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        gitInfoRepository.saveAndFlush(gitInfo);

        // Get all the gitInfoList where gitProjectId not equals to DEFAULT_GIT_PROJECT_ID
        defaultGitInfoShouldNotBeFound("gitProjectId.notEquals=" + DEFAULT_GIT_PROJECT_ID);

        // Get all the gitInfoList where gitProjectId not equals to UPDATED_GIT_PROJECT_ID
        defaultGitInfoShouldBeFound("gitProjectId.notEquals=" + UPDATED_GIT_PROJECT_ID);
    }

    @Test
    @Transactional
    public void getAllGitInfosByGitProjectIdIsInShouldWork() throws Exception {
        // Initialize the database
        gitInfoRepository.saveAndFlush(gitInfo);

        // Get all the gitInfoList where gitProjectId in DEFAULT_GIT_PROJECT_ID or UPDATED_GIT_PROJECT_ID
        defaultGitInfoShouldBeFound("gitProjectId.in=" + DEFAULT_GIT_PROJECT_ID + "," + UPDATED_GIT_PROJECT_ID);

        // Get all the gitInfoList where gitProjectId equals to UPDATED_GIT_PROJECT_ID
        defaultGitInfoShouldNotBeFound("gitProjectId.in=" + UPDATED_GIT_PROJECT_ID);
    }

    @Test
    @Transactional
    public void getAllGitInfosByGitProjectIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        gitInfoRepository.saveAndFlush(gitInfo);

        // Get all the gitInfoList where gitProjectId is not null
        defaultGitInfoShouldBeFound("gitProjectId.specified=true");

        // Get all the gitInfoList where gitProjectId is null
        defaultGitInfoShouldNotBeFound("gitProjectId.specified=false");
    }

    @Test
    @Transactional
    public void getAllGitInfosByGitProjectIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        gitInfoRepository.saveAndFlush(gitInfo);

        // Get all the gitInfoList where gitProjectId is greater than or equal to DEFAULT_GIT_PROJECT_ID
        defaultGitInfoShouldBeFound("gitProjectId.greaterThanOrEqual=" + DEFAULT_GIT_PROJECT_ID);

        // Get all the gitInfoList where gitProjectId is greater than or equal to UPDATED_GIT_PROJECT_ID
        defaultGitInfoShouldNotBeFound("gitProjectId.greaterThanOrEqual=" + UPDATED_GIT_PROJECT_ID);
    }

    @Test
    @Transactional
    public void getAllGitInfosByGitProjectIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        gitInfoRepository.saveAndFlush(gitInfo);

        // Get all the gitInfoList where gitProjectId is less than or equal to DEFAULT_GIT_PROJECT_ID
        defaultGitInfoShouldBeFound("gitProjectId.lessThanOrEqual=" + DEFAULT_GIT_PROJECT_ID);

        // Get all the gitInfoList where gitProjectId is less than or equal to SMALLER_GIT_PROJECT_ID
        defaultGitInfoShouldNotBeFound("gitProjectId.lessThanOrEqual=" + SMALLER_GIT_PROJECT_ID);
    }

    @Test
    @Transactional
    public void getAllGitInfosByGitProjectIdIsLessThanSomething() throws Exception {
        // Initialize the database
        gitInfoRepository.saveAndFlush(gitInfo);

        // Get all the gitInfoList where gitProjectId is less than DEFAULT_GIT_PROJECT_ID
        defaultGitInfoShouldNotBeFound("gitProjectId.lessThan=" + DEFAULT_GIT_PROJECT_ID);

        // Get all the gitInfoList where gitProjectId is less than UPDATED_GIT_PROJECT_ID
        defaultGitInfoShouldBeFound("gitProjectId.lessThan=" + UPDATED_GIT_PROJECT_ID);
    }

    @Test
    @Transactional
    public void getAllGitInfosByGitProjectIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        gitInfoRepository.saveAndFlush(gitInfo);

        // Get all the gitInfoList where gitProjectId is greater than DEFAULT_GIT_PROJECT_ID
        defaultGitInfoShouldNotBeFound("gitProjectId.greaterThan=" + DEFAULT_GIT_PROJECT_ID);

        // Get all the gitInfoList where gitProjectId is greater than SMALLER_GIT_PROJECT_ID
        defaultGitInfoShouldBeFound("gitProjectId.greaterThan=" + SMALLER_GIT_PROJECT_ID);
    }

    @Test
    @Transactional
    public void getAllGitInfosByGitProjectNameIsEqualToSomething() throws Exception {
        // Initialize the database
        gitInfoRepository.saveAndFlush(gitInfo);

        // Get all the gitInfoList where gitProjectName equals to DEFAULT_GIT_PROJECT_NAME
        defaultGitInfoShouldBeFound("gitProjectName.equals=" + DEFAULT_GIT_PROJECT_NAME);

        // Get all the gitInfoList where gitProjectName equals to UPDATED_GIT_PROJECT_NAME
        defaultGitInfoShouldNotBeFound("gitProjectName.equals=" + UPDATED_GIT_PROJECT_NAME);
    }

    @Test
    @Transactional
    public void getAllGitInfosByGitProjectNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        gitInfoRepository.saveAndFlush(gitInfo);

        // Get all the gitInfoList where gitProjectName not equals to DEFAULT_GIT_PROJECT_NAME
        defaultGitInfoShouldNotBeFound("gitProjectName.notEquals=" + DEFAULT_GIT_PROJECT_NAME);

        // Get all the gitInfoList where gitProjectName not equals to UPDATED_GIT_PROJECT_NAME
        defaultGitInfoShouldBeFound("gitProjectName.notEquals=" + UPDATED_GIT_PROJECT_NAME);
    }

    @Test
    @Transactional
    public void getAllGitInfosByGitProjectNameIsInShouldWork() throws Exception {
        // Initialize the database
        gitInfoRepository.saveAndFlush(gitInfo);

        // Get all the gitInfoList where gitProjectName in DEFAULT_GIT_PROJECT_NAME or UPDATED_GIT_PROJECT_NAME
        defaultGitInfoShouldBeFound("gitProjectName.in=" + DEFAULT_GIT_PROJECT_NAME + "," + UPDATED_GIT_PROJECT_NAME);

        // Get all the gitInfoList where gitProjectName equals to UPDATED_GIT_PROJECT_NAME
        defaultGitInfoShouldNotBeFound("gitProjectName.in=" + UPDATED_GIT_PROJECT_NAME);
    }

    @Test
    @Transactional
    public void getAllGitInfosByGitProjectNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        gitInfoRepository.saveAndFlush(gitInfo);

        // Get all the gitInfoList where gitProjectName is not null
        defaultGitInfoShouldBeFound("gitProjectName.specified=true");

        // Get all the gitInfoList where gitProjectName is null
        defaultGitInfoShouldNotBeFound("gitProjectName.specified=false");
    }

    @Test
    @Transactional
    public void getAllGitInfosByGitProjectNameContainsSomething() throws Exception {
        // Initialize the database
        gitInfoRepository.saveAndFlush(gitInfo);

        // Get all the gitInfoList where gitProjectName contains DEFAULT_GIT_PROJECT_NAME
        defaultGitInfoShouldBeFound("gitProjectName.contains=" + DEFAULT_GIT_PROJECT_NAME);

        // Get all the gitInfoList where gitProjectName contains UPDATED_GIT_PROJECT_NAME
        defaultGitInfoShouldNotBeFound("gitProjectName.contains=" + UPDATED_GIT_PROJECT_NAME);
    }

    @Test
    @Transactional
    public void getAllGitInfosByGitProjectNameNotContainsSomething() throws Exception {
        // Initialize the database
        gitInfoRepository.saveAndFlush(gitInfo);

        // Get all the gitInfoList where gitProjectName does not contain DEFAULT_GIT_PROJECT_NAME
        defaultGitInfoShouldNotBeFound("gitProjectName.doesNotContain=" + DEFAULT_GIT_PROJECT_NAME);

        // Get all the gitInfoList where gitProjectName does not contain UPDATED_GIT_PROJECT_NAME
        defaultGitInfoShouldBeFound("gitProjectName.doesNotContain=" + UPDATED_GIT_PROJECT_NAME);
    }

    @Test
    @Transactional
    public void getAllGitInfosByIssueCountIsEqualToSomething() throws Exception {
        // Initialize the database
        gitInfoRepository.saveAndFlush(gitInfo);

        // Get all the gitInfoList where issueCount equals to DEFAULT_ISSUE_COUNT
        defaultGitInfoShouldBeFound("issueCount.equals=" + DEFAULT_ISSUE_COUNT);

        // Get all the gitInfoList where issueCount equals to UPDATED_ISSUE_COUNT
        defaultGitInfoShouldNotBeFound("issueCount.equals=" + UPDATED_ISSUE_COUNT);
    }

    @Test
    @Transactional
    public void getAllGitInfosByIssueCountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        gitInfoRepository.saveAndFlush(gitInfo);

        // Get all the gitInfoList where issueCount not equals to DEFAULT_ISSUE_COUNT
        defaultGitInfoShouldNotBeFound("issueCount.notEquals=" + DEFAULT_ISSUE_COUNT);

        // Get all the gitInfoList where issueCount not equals to UPDATED_ISSUE_COUNT
        defaultGitInfoShouldBeFound("issueCount.notEquals=" + UPDATED_ISSUE_COUNT);
    }

    @Test
    @Transactional
    public void getAllGitInfosByIssueCountIsInShouldWork() throws Exception {
        // Initialize the database
        gitInfoRepository.saveAndFlush(gitInfo);

        // Get all the gitInfoList where issueCount in DEFAULT_ISSUE_COUNT or UPDATED_ISSUE_COUNT
        defaultGitInfoShouldBeFound("issueCount.in=" + DEFAULT_ISSUE_COUNT + "," + UPDATED_ISSUE_COUNT);

        // Get all the gitInfoList where issueCount equals to UPDATED_ISSUE_COUNT
        defaultGitInfoShouldNotBeFound("issueCount.in=" + UPDATED_ISSUE_COUNT);
    }

    @Test
    @Transactional
    public void getAllGitInfosByIssueCountIsNullOrNotNull() throws Exception {
        // Initialize the database
        gitInfoRepository.saveAndFlush(gitInfo);

        // Get all the gitInfoList where issueCount is not null
        defaultGitInfoShouldBeFound("issueCount.specified=true");

        // Get all the gitInfoList where issueCount is null
        defaultGitInfoShouldNotBeFound("issueCount.specified=false");
    }

    @Test
    @Transactional
    public void getAllGitInfosByIssueCountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        gitInfoRepository.saveAndFlush(gitInfo);

        // Get all the gitInfoList where issueCount is greater than or equal to DEFAULT_ISSUE_COUNT
        defaultGitInfoShouldBeFound("issueCount.greaterThanOrEqual=" + DEFAULT_ISSUE_COUNT);

        // Get all the gitInfoList where issueCount is greater than or equal to UPDATED_ISSUE_COUNT
        defaultGitInfoShouldNotBeFound("issueCount.greaterThanOrEqual=" + UPDATED_ISSUE_COUNT);
    }

    @Test
    @Transactional
    public void getAllGitInfosByIssueCountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        gitInfoRepository.saveAndFlush(gitInfo);

        // Get all the gitInfoList where issueCount is less than or equal to DEFAULT_ISSUE_COUNT
        defaultGitInfoShouldBeFound("issueCount.lessThanOrEqual=" + DEFAULT_ISSUE_COUNT);

        // Get all the gitInfoList where issueCount is less than or equal to SMALLER_ISSUE_COUNT
        defaultGitInfoShouldNotBeFound("issueCount.lessThanOrEqual=" + SMALLER_ISSUE_COUNT);
    }

    @Test
    @Transactional
    public void getAllGitInfosByIssueCountIsLessThanSomething() throws Exception {
        // Initialize the database
        gitInfoRepository.saveAndFlush(gitInfo);

        // Get all the gitInfoList where issueCount is less than DEFAULT_ISSUE_COUNT
        defaultGitInfoShouldNotBeFound("issueCount.lessThan=" + DEFAULT_ISSUE_COUNT);

        // Get all the gitInfoList where issueCount is less than UPDATED_ISSUE_COUNT
        defaultGitInfoShouldBeFound("issueCount.lessThan=" + UPDATED_ISSUE_COUNT);
    }

    @Test
    @Transactional
    public void getAllGitInfosByIssueCountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        gitInfoRepository.saveAndFlush(gitInfo);

        // Get all the gitInfoList where issueCount is greater than DEFAULT_ISSUE_COUNT
        defaultGitInfoShouldNotBeFound("issueCount.greaterThan=" + DEFAULT_ISSUE_COUNT);

        // Get all the gitInfoList where issueCount is greater than SMALLER_ISSUE_COUNT
        defaultGitInfoShouldBeFound("issueCount.greaterThan=" + SMALLER_ISSUE_COUNT);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultGitInfoShouldBeFound(String filter) throws Exception {
        restGitInfoMockMvc
            .perform(get("/api/git-infos?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(gitInfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].gitProjectId").value(hasItem(DEFAULT_GIT_PROJECT_ID)))
            .andExpect(jsonPath("$.[*].gitProjectName").value(hasItem(DEFAULT_GIT_PROJECT_NAME)))
            .andExpect(jsonPath("$.[*].issueCount").value(hasItem(DEFAULT_ISSUE_COUNT)));

        // Check, that the count call also returns 1
        restGitInfoMockMvc
            .perform(get("/api/git-infos/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultGitInfoShouldNotBeFound(String filter) throws Exception {
        restGitInfoMockMvc
            .perform(get("/api/git-infos?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restGitInfoMockMvc
            .perform(get("/api/git-infos/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingGitInfo() throws Exception {
        // Get the gitInfo
        restGitInfoMockMvc.perform(get("/api/git-infos/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGitInfo() throws Exception {
        // Initialize the database
        gitInfoRepository.saveAndFlush(gitInfo);

        int databaseSizeBeforeUpdate = gitInfoRepository.findAll().size();

        // Update the gitInfo
        GitInfo updatedGitInfo = gitInfoRepository.findById(gitInfo.getId()).get();
        // Disconnect from session so that the updates on updatedGitInfo are not directly saved in db
        em.detach(updatedGitInfo);
        updatedGitInfo.gitProjectId(UPDATED_GIT_PROJECT_ID).gitProjectName(UPDATED_GIT_PROJECT_NAME).issueCount(UPDATED_ISSUE_COUNT);
        GitInfoDTO gitInfoDTO = gitInfoMapper.toDto(updatedGitInfo);

        restGitInfoMockMvc
            .perform(put("/api/git-infos").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gitInfoDTO)))
            .andExpect(status().isOk());

        // Validate the GitInfo in the database
        List<GitInfo> gitInfoList = gitInfoRepository.findAll();
        assertThat(gitInfoList).hasSize(databaseSizeBeforeUpdate);
        GitInfo testGitInfo = gitInfoList.get(gitInfoList.size() - 1);
        assertThat(testGitInfo.getGitProjectId()).isEqualTo(UPDATED_GIT_PROJECT_ID);
        assertThat(testGitInfo.getGitProjectName()).isEqualTo(UPDATED_GIT_PROJECT_NAME);
        assertThat(testGitInfo.getIssueCount()).isEqualTo(UPDATED_ISSUE_COUNT);
    }

    @Test
    @Transactional
    public void updateNonExistingGitInfo() throws Exception {
        int databaseSizeBeforeUpdate = gitInfoRepository.findAll().size();

        // Create the GitInfo
        GitInfoDTO gitInfoDTO = gitInfoMapper.toDto(gitInfo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGitInfoMockMvc
            .perform(put("/api/git-infos").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gitInfoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the GitInfo in the database
        List<GitInfo> gitInfoList = gitInfoRepository.findAll();
        assertThat(gitInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteGitInfo() throws Exception {
        // Initialize the database
        gitInfoRepository.saveAndFlush(gitInfo);

        int databaseSizeBeforeDelete = gitInfoRepository.findAll().size();

        // Delete the gitInfo
        restGitInfoMockMvc
            .perform(delete("/api/git-infos/{id}", gitInfo.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<GitInfo> gitInfoList = gitInfoRepository.findAll();
        assertThat(gitInfoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
