package com.gitimporter.web.rest;

import static com.gitimporter.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.gitimporter.GitimporterApp;
import com.gitimporter.domain.Issue;
import com.gitimporter.repository.IssueRepository;
import com.gitimporter.service.IssueQueryService;
import com.gitimporter.service.IssueService;
import com.gitimporter.service.dto.IssueCriteria;
import com.gitimporter.service.dto.IssueDTO;
import com.gitimporter.service.mapper.IssueMapper;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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
 * Integration tests for the {@link IssueResource} REST controller.
 */
@SpringBootTest(classes = GitimporterApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class IssueResourceIT {
    private static final Integer DEFAULT_ISSUE_ID = 1;
    private static final Integer UPDATED_ISSUE_ID = 2;
    private static final Integer SMALLER_ISSUE_ID = 1 - 1;

    private static final Integer DEFAULT_ISSUE_ORDER = 1;
    private static final Integer UPDATED_ISSUE_ORDER = 2;
    private static final Integer SMALLER_ISSUE_ORDER = 1 - 1;

    private static final String DEFAULT_ISSUE_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_ISSUE_TITLE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_STATE = false;
    private static final Boolean UPDATED_STATE = true;

    private static final String DEFAULT_AUTHOR = "AAAAAAAAAA";
    private static final String UPDATED_AUTHOR = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_UPDATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_UPDATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_CLOSED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CLOSED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CLOSED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String DEFAULT_CLOSED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CLOSED_BY = "BBBBBBBBBB";

    @Autowired
    private IssueRepository issueRepository;

    @Autowired
    private IssueMapper issueMapper;

    @Autowired
    private IssueService issueService;

    @Autowired
    private IssueQueryService issueQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restIssueMockMvc;

    private Issue issue;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Issue createEntity(EntityManager em) {
        Issue issue = new Issue()
            .issueId(DEFAULT_ISSUE_ID)
            .issueOrder(DEFAULT_ISSUE_ORDER)
            .issueTitle(DEFAULT_ISSUE_TITLE)
            .state(DEFAULT_STATE)
            .author(DEFAULT_AUTHOR)
            .description(DEFAULT_DESCRIPTION)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT)
            .closedAt(DEFAULT_CLOSED_AT)
            .closedBy(DEFAULT_CLOSED_BY);
        return issue;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Issue createUpdatedEntity(EntityManager em) {
        Issue issue = new Issue()
            .issueId(UPDATED_ISSUE_ID)
            .issueOrder(UPDATED_ISSUE_ORDER)
            .issueTitle(UPDATED_ISSUE_TITLE)
            .state(UPDATED_STATE)
            .author(UPDATED_AUTHOR)
            .description(UPDATED_DESCRIPTION)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .closedAt(UPDATED_CLOSED_AT)
            .closedBy(UPDATED_CLOSED_BY);
        return issue;
    }

    @BeforeEach
    public void initTest() {
        issue = createEntity(em);
    }

    @Test
    @Transactional
    public void createIssue() throws Exception {
        int databaseSizeBeforeCreate = issueRepository.findAll().size();
        // Create the Issue
        IssueDTO issueDTO = issueMapper.toDto(issue);
        restIssueMockMvc
            .perform(post("/api/issues").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(issueDTO)))
            .andExpect(status().isCreated());

        // Validate the Issue in the database
        List<Issue> issueList = issueRepository.findAll();
        assertThat(issueList).hasSize(databaseSizeBeforeCreate + 1);
        Issue testIssue = issueList.get(issueList.size() - 1);
        assertThat(testIssue.getIssueId()).isEqualTo(DEFAULT_ISSUE_ID);
        assertThat(testIssue.getIssueOrder()).isEqualTo(DEFAULT_ISSUE_ORDER);
        assertThat(testIssue.getIssueTitle()).isEqualTo(DEFAULT_ISSUE_TITLE);
        assertThat(testIssue.isState()).isEqualTo(DEFAULT_STATE);
        assertThat(testIssue.getAuthor()).isEqualTo(DEFAULT_AUTHOR);
        assertThat(testIssue.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testIssue.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testIssue.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
        assertThat(testIssue.getClosedAt()).isEqualTo(DEFAULT_CLOSED_AT);
        assertThat(testIssue.getClosedBy()).isEqualTo(DEFAULT_CLOSED_BY);
    }

    @Test
    @Transactional
    public void createIssueWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = issueRepository.findAll().size();

        // Create the Issue with an existing ID
        issue.setId(1L);
        IssueDTO issueDTO = issueMapper.toDto(issue);

        // An entity with an existing ID cannot be created, so this API call must fail
        restIssueMockMvc
            .perform(post("/api/issues").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(issueDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Issue in the database
        List<Issue> issueList = issueRepository.findAll();
        assertThat(issueList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkIssueIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = issueRepository.findAll().size();
        // set the field null
        issue.setIssueId(null);

        // Create the Issue, which fails.
        IssueDTO issueDTO = issueMapper.toDto(issue);

        restIssueMockMvc
            .perform(post("/api/issues").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(issueDTO)))
            .andExpect(status().isBadRequest());

        List<Issue> issueList = issueRepository.findAll();
        assertThat(issueList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIssueOrderIsRequired() throws Exception {
        int databaseSizeBeforeTest = issueRepository.findAll().size();
        // set the field null
        issue.setIssueOrder(null);

        // Create the Issue, which fails.
        IssueDTO issueDTO = issueMapper.toDto(issue);

        restIssueMockMvc
            .perform(post("/api/issues").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(issueDTO)))
            .andExpect(status().isBadRequest());

        List<Issue> issueList = issueRepository.findAll();
        assertThat(issueList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIssueTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = issueRepository.findAll().size();
        // set the field null
        issue.setIssueTitle(null);

        // Create the Issue, which fails.
        IssueDTO issueDTO = issueMapper.toDto(issue);

        restIssueMockMvc
            .perform(post("/api/issues").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(issueDTO)))
            .andExpect(status().isBadRequest());

        List<Issue> issueList = issueRepository.findAll();
        assertThat(issueList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStateIsRequired() throws Exception {
        int databaseSizeBeforeTest = issueRepository.findAll().size();
        // set the field null
        issue.setState(null);

        // Create the Issue, which fails.
        IssueDTO issueDTO = issueMapper.toDto(issue);

        restIssueMockMvc
            .perform(post("/api/issues").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(issueDTO)))
            .andExpect(status().isBadRequest());

        List<Issue> issueList = issueRepository.findAll();
        assertThat(issueList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAuthorIsRequired() throws Exception {
        int databaseSizeBeforeTest = issueRepository.findAll().size();
        // set the field null
        issue.setAuthor(null);

        // Create the Issue, which fails.
        IssueDTO issueDTO = issueMapper.toDto(issue);

        restIssueMockMvc
            .perform(post("/api/issues").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(issueDTO)))
            .andExpect(status().isBadRequest());

        List<Issue> issueList = issueRepository.findAll();
        assertThat(issueList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = issueRepository.findAll().size();
        // set the field null
        issue.setDescription(null);

        // Create the Issue, which fails.
        IssueDTO issueDTO = issueMapper.toDto(issue);

        restIssueMockMvc
            .perform(post("/api/issues").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(issueDTO)))
            .andExpect(status().isBadRequest());

        List<Issue> issueList = issueRepository.findAll();
        assertThat(issueList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = issueRepository.findAll().size();
        // set the field null
        issue.setCreatedAt(null);

        // Create the Issue, which fails.
        IssueDTO issueDTO = issueMapper.toDto(issue);

        restIssueMockMvc
            .perform(post("/api/issues").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(issueDTO)))
            .andExpect(status().isBadRequest());

        List<Issue> issueList = issueRepository.findAll();
        assertThat(issueList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUpdatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = issueRepository.findAll().size();
        // set the field null
        issue.setUpdatedAt(null);

        // Create the Issue, which fails.
        IssueDTO issueDTO = issueMapper.toDto(issue);

        restIssueMockMvc
            .perform(post("/api/issues").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(issueDTO)))
            .andExpect(status().isBadRequest());

        List<Issue> issueList = issueRepository.findAll();
        assertThat(issueList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllIssues() throws Exception {
        // Initialize the database
        issueRepository.saveAndFlush(issue);

        // Get all the issueList
        restIssueMockMvc
            .perform(get("/api/issues?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(issue.getId().intValue())))
            .andExpect(jsonPath("$.[*].issueId").value(hasItem(DEFAULT_ISSUE_ID)))
            .andExpect(jsonPath("$.[*].issueOrder").value(hasItem(DEFAULT_ISSUE_ORDER)))
            .andExpect(jsonPath("$.[*].issueTitle").value(hasItem(DEFAULT_ISSUE_TITLE)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.booleanValue())))
            .andExpect(jsonPath("$.[*].author").value(hasItem(DEFAULT_AUTHOR)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(sameInstant(DEFAULT_UPDATED_AT))))
            .andExpect(jsonPath("$.[*].closedAt").value(hasItem(sameInstant(DEFAULT_CLOSED_AT))))
            .andExpect(jsonPath("$.[*].closedBy").value(hasItem(DEFAULT_CLOSED_BY)));
    }

    @Test
    @Transactional
    public void getIssue() throws Exception {
        // Initialize the database
        issueRepository.saveAndFlush(issue);

        // Get the issue
        restIssueMockMvc
            .perform(get("/api/issues/{id}", issue.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(issue.getId().intValue()))
            .andExpect(jsonPath("$.issueId").value(DEFAULT_ISSUE_ID))
            .andExpect(jsonPath("$.issueOrder").value(DEFAULT_ISSUE_ORDER))
            .andExpect(jsonPath("$.issueTitle").value(DEFAULT_ISSUE_TITLE))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE.booleanValue()))
            .andExpect(jsonPath("$.author").value(DEFAULT_AUTHOR))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.createdAt").value(sameInstant(DEFAULT_CREATED_AT)))
            .andExpect(jsonPath("$.updatedAt").value(sameInstant(DEFAULT_UPDATED_AT)))
            .andExpect(jsonPath("$.closedAt").value(sameInstant(DEFAULT_CLOSED_AT)))
            .andExpect(jsonPath("$.closedBy").value(DEFAULT_CLOSED_BY));
    }

    @Test
    @Transactional
    public void getIssuesByIdFiltering() throws Exception {
        // Initialize the database
        issueRepository.saveAndFlush(issue);

        Long id = issue.getId();

        defaultIssueShouldBeFound("id.equals=" + id);
        defaultIssueShouldNotBeFound("id.notEquals=" + id);

        defaultIssueShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultIssueShouldNotBeFound("id.greaterThan=" + id);

        defaultIssueShouldBeFound("id.lessThanOrEqual=" + id);
        defaultIssueShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    public void getAllIssuesByIssueIdIsEqualToSomething() throws Exception {
        // Initialize the database
        issueRepository.saveAndFlush(issue);

        // Get all the issueList where issueId equals to DEFAULT_ISSUE_ID
        defaultIssueShouldBeFound("issueId.equals=" + DEFAULT_ISSUE_ID);

        // Get all the issueList where issueId equals to UPDATED_ISSUE_ID
        defaultIssueShouldNotBeFound("issueId.equals=" + UPDATED_ISSUE_ID);
    }

    @Test
    @Transactional
    public void getAllIssuesByIssueIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        issueRepository.saveAndFlush(issue);

        // Get all the issueList where issueId not equals to DEFAULT_ISSUE_ID
        defaultIssueShouldNotBeFound("issueId.notEquals=" + DEFAULT_ISSUE_ID);

        // Get all the issueList where issueId not equals to UPDATED_ISSUE_ID
        defaultIssueShouldBeFound("issueId.notEquals=" + UPDATED_ISSUE_ID);
    }

    @Test
    @Transactional
    public void getAllIssuesByIssueIdIsInShouldWork() throws Exception {
        // Initialize the database
        issueRepository.saveAndFlush(issue);

        // Get all the issueList where issueId in DEFAULT_ISSUE_ID or UPDATED_ISSUE_ID
        defaultIssueShouldBeFound("issueId.in=" + DEFAULT_ISSUE_ID + "," + UPDATED_ISSUE_ID);

        // Get all the issueList where issueId equals to UPDATED_ISSUE_ID
        defaultIssueShouldNotBeFound("issueId.in=" + UPDATED_ISSUE_ID);
    }

    @Test
    @Transactional
    public void getAllIssuesByIssueIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        issueRepository.saveAndFlush(issue);

        // Get all the issueList where issueId is not null
        defaultIssueShouldBeFound("issueId.specified=true");

        // Get all the issueList where issueId is null
        defaultIssueShouldNotBeFound("issueId.specified=false");
    }

    @Test
    @Transactional
    public void getAllIssuesByIssueIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        issueRepository.saveAndFlush(issue);

        // Get all the issueList where issueId is greater than or equal to DEFAULT_ISSUE_ID
        defaultIssueShouldBeFound("issueId.greaterThanOrEqual=" + DEFAULT_ISSUE_ID);

        // Get all the issueList where issueId is greater than or equal to UPDATED_ISSUE_ID
        defaultIssueShouldNotBeFound("issueId.greaterThanOrEqual=" + UPDATED_ISSUE_ID);
    }

    @Test
    @Transactional
    public void getAllIssuesByIssueIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        issueRepository.saveAndFlush(issue);

        // Get all the issueList where issueId is less than or equal to DEFAULT_ISSUE_ID
        defaultIssueShouldBeFound("issueId.lessThanOrEqual=" + DEFAULT_ISSUE_ID);

        // Get all the issueList where issueId is less than or equal to SMALLER_ISSUE_ID
        defaultIssueShouldNotBeFound("issueId.lessThanOrEqual=" + SMALLER_ISSUE_ID);
    }

    @Test
    @Transactional
    public void getAllIssuesByIssueIdIsLessThanSomething() throws Exception {
        // Initialize the database
        issueRepository.saveAndFlush(issue);

        // Get all the issueList where issueId is less than DEFAULT_ISSUE_ID
        defaultIssueShouldNotBeFound("issueId.lessThan=" + DEFAULT_ISSUE_ID);

        // Get all the issueList where issueId is less than UPDATED_ISSUE_ID
        defaultIssueShouldBeFound("issueId.lessThan=" + UPDATED_ISSUE_ID);
    }

    @Test
    @Transactional
    public void getAllIssuesByIssueIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        issueRepository.saveAndFlush(issue);

        // Get all the issueList where issueId is greater than DEFAULT_ISSUE_ID
        defaultIssueShouldNotBeFound("issueId.greaterThan=" + DEFAULT_ISSUE_ID);

        // Get all the issueList where issueId is greater than SMALLER_ISSUE_ID
        defaultIssueShouldBeFound("issueId.greaterThan=" + SMALLER_ISSUE_ID);
    }

    @Test
    @Transactional
    public void getAllIssuesByIssueOrderIsEqualToSomething() throws Exception {
        // Initialize the database
        issueRepository.saveAndFlush(issue);

        // Get all the issueList where issueOrder equals to DEFAULT_ISSUE_ORDER
        defaultIssueShouldBeFound("issueOrder.equals=" + DEFAULT_ISSUE_ORDER);

        // Get all the issueList where issueOrder equals to UPDATED_ISSUE_ORDER
        defaultIssueShouldNotBeFound("issueOrder.equals=" + UPDATED_ISSUE_ORDER);
    }

    @Test
    @Transactional
    public void getAllIssuesByIssueOrderIsNotEqualToSomething() throws Exception {
        // Initialize the database
        issueRepository.saveAndFlush(issue);

        // Get all the issueList where issueOrder not equals to DEFAULT_ISSUE_ORDER
        defaultIssueShouldNotBeFound("issueOrder.notEquals=" + DEFAULT_ISSUE_ORDER);

        // Get all the issueList where issueOrder not equals to UPDATED_ISSUE_ORDER
        defaultIssueShouldBeFound("issueOrder.notEquals=" + UPDATED_ISSUE_ORDER);
    }

    @Test
    @Transactional
    public void getAllIssuesByIssueOrderIsInShouldWork() throws Exception {
        // Initialize the database
        issueRepository.saveAndFlush(issue);

        // Get all the issueList where issueOrder in DEFAULT_ISSUE_ORDER or UPDATED_ISSUE_ORDER
        defaultIssueShouldBeFound("issueOrder.in=" + DEFAULT_ISSUE_ORDER + "," + UPDATED_ISSUE_ORDER);

        // Get all the issueList where issueOrder equals to UPDATED_ISSUE_ORDER
        defaultIssueShouldNotBeFound("issueOrder.in=" + UPDATED_ISSUE_ORDER);
    }

    @Test
    @Transactional
    public void getAllIssuesByIssueOrderIsNullOrNotNull() throws Exception {
        // Initialize the database
        issueRepository.saveAndFlush(issue);

        // Get all the issueList where issueOrder is not null
        defaultIssueShouldBeFound("issueOrder.specified=true");

        // Get all the issueList where issueOrder is null
        defaultIssueShouldNotBeFound("issueOrder.specified=false");
    }

    @Test
    @Transactional
    public void getAllIssuesByIssueOrderIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        issueRepository.saveAndFlush(issue);

        // Get all the issueList where issueOrder is greater than or equal to DEFAULT_ISSUE_ORDER
        defaultIssueShouldBeFound("issueOrder.greaterThanOrEqual=" + DEFAULT_ISSUE_ORDER);

        // Get all the issueList where issueOrder is greater than or equal to UPDATED_ISSUE_ORDER
        defaultIssueShouldNotBeFound("issueOrder.greaterThanOrEqual=" + UPDATED_ISSUE_ORDER);
    }

    @Test
    @Transactional
    public void getAllIssuesByIssueOrderIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        issueRepository.saveAndFlush(issue);

        // Get all the issueList where issueOrder is less than or equal to DEFAULT_ISSUE_ORDER
        defaultIssueShouldBeFound("issueOrder.lessThanOrEqual=" + DEFAULT_ISSUE_ORDER);

        // Get all the issueList where issueOrder is less than or equal to SMALLER_ISSUE_ORDER
        defaultIssueShouldNotBeFound("issueOrder.lessThanOrEqual=" + SMALLER_ISSUE_ORDER);
    }

    @Test
    @Transactional
    public void getAllIssuesByIssueOrderIsLessThanSomething() throws Exception {
        // Initialize the database
        issueRepository.saveAndFlush(issue);

        // Get all the issueList where issueOrder is less than DEFAULT_ISSUE_ORDER
        defaultIssueShouldNotBeFound("issueOrder.lessThan=" + DEFAULT_ISSUE_ORDER);

        // Get all the issueList where issueOrder is less than UPDATED_ISSUE_ORDER
        defaultIssueShouldBeFound("issueOrder.lessThan=" + UPDATED_ISSUE_ORDER);
    }

    @Test
    @Transactional
    public void getAllIssuesByIssueOrderIsGreaterThanSomething() throws Exception {
        // Initialize the database
        issueRepository.saveAndFlush(issue);

        // Get all the issueList where issueOrder is greater than DEFAULT_ISSUE_ORDER
        defaultIssueShouldNotBeFound("issueOrder.greaterThan=" + DEFAULT_ISSUE_ORDER);

        // Get all the issueList where issueOrder is greater than SMALLER_ISSUE_ORDER
        defaultIssueShouldBeFound("issueOrder.greaterThan=" + SMALLER_ISSUE_ORDER);
    }

    @Test
    @Transactional
    public void getAllIssuesByIssueTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        issueRepository.saveAndFlush(issue);

        // Get all the issueList where issueTitle equals to DEFAULT_ISSUE_TITLE
        defaultIssueShouldBeFound("issueTitle.equals=" + DEFAULT_ISSUE_TITLE);

        // Get all the issueList where issueTitle equals to UPDATED_ISSUE_TITLE
        defaultIssueShouldNotBeFound("issueTitle.equals=" + UPDATED_ISSUE_TITLE);
    }

    @Test
    @Transactional
    public void getAllIssuesByIssueTitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        issueRepository.saveAndFlush(issue);

        // Get all the issueList where issueTitle not equals to DEFAULT_ISSUE_TITLE
        defaultIssueShouldNotBeFound("issueTitle.notEquals=" + DEFAULT_ISSUE_TITLE);

        // Get all the issueList where issueTitle not equals to UPDATED_ISSUE_TITLE
        defaultIssueShouldBeFound("issueTitle.notEquals=" + UPDATED_ISSUE_TITLE);
    }

    @Test
    @Transactional
    public void getAllIssuesByIssueTitleIsInShouldWork() throws Exception {
        // Initialize the database
        issueRepository.saveAndFlush(issue);

        // Get all the issueList where issueTitle in DEFAULT_ISSUE_TITLE or UPDATED_ISSUE_TITLE
        defaultIssueShouldBeFound("issueTitle.in=" + DEFAULT_ISSUE_TITLE + "," + UPDATED_ISSUE_TITLE);

        // Get all the issueList where issueTitle equals to UPDATED_ISSUE_TITLE
        defaultIssueShouldNotBeFound("issueTitle.in=" + UPDATED_ISSUE_TITLE);
    }

    @Test
    @Transactional
    public void getAllIssuesByIssueTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        issueRepository.saveAndFlush(issue);

        // Get all the issueList where issueTitle is not null
        defaultIssueShouldBeFound("issueTitle.specified=true");

        // Get all the issueList where issueTitle is null
        defaultIssueShouldNotBeFound("issueTitle.specified=false");
    }

    @Test
    @Transactional
    public void getAllIssuesByIssueTitleContainsSomething() throws Exception {
        // Initialize the database
        issueRepository.saveAndFlush(issue);

        // Get all the issueList where issueTitle contains DEFAULT_ISSUE_TITLE
        defaultIssueShouldBeFound("issueTitle.contains=" + DEFAULT_ISSUE_TITLE);

        // Get all the issueList where issueTitle contains UPDATED_ISSUE_TITLE
        defaultIssueShouldNotBeFound("issueTitle.contains=" + UPDATED_ISSUE_TITLE);
    }

    @Test
    @Transactional
    public void getAllIssuesByIssueTitleNotContainsSomething() throws Exception {
        // Initialize the database
        issueRepository.saveAndFlush(issue);

        // Get all the issueList where issueTitle does not contain DEFAULT_ISSUE_TITLE
        defaultIssueShouldNotBeFound("issueTitle.doesNotContain=" + DEFAULT_ISSUE_TITLE);

        // Get all the issueList where issueTitle does not contain UPDATED_ISSUE_TITLE
        defaultIssueShouldBeFound("issueTitle.doesNotContain=" + UPDATED_ISSUE_TITLE);
    }

    @Test
    @Transactional
    public void getAllIssuesByStateIsEqualToSomething() throws Exception {
        // Initialize the database
        issueRepository.saveAndFlush(issue);

        // Get all the issueList where state equals to DEFAULT_STATE
        defaultIssueShouldBeFound("state.equals=" + DEFAULT_STATE);

        // Get all the issueList where state equals to UPDATED_STATE
        defaultIssueShouldNotBeFound("state.equals=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    public void getAllIssuesByStateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        issueRepository.saveAndFlush(issue);

        // Get all the issueList where state not equals to DEFAULT_STATE
        defaultIssueShouldNotBeFound("state.notEquals=" + DEFAULT_STATE);

        // Get all the issueList where state not equals to UPDATED_STATE
        defaultIssueShouldBeFound("state.notEquals=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    public void getAllIssuesByStateIsInShouldWork() throws Exception {
        // Initialize the database
        issueRepository.saveAndFlush(issue);

        // Get all the issueList where state in DEFAULT_STATE or UPDATED_STATE
        defaultIssueShouldBeFound("state.in=" + DEFAULT_STATE + "," + UPDATED_STATE);

        // Get all the issueList where state equals to UPDATED_STATE
        defaultIssueShouldNotBeFound("state.in=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    public void getAllIssuesByStateIsNullOrNotNull() throws Exception {
        // Initialize the database
        issueRepository.saveAndFlush(issue);

        // Get all the issueList where state is not null
        defaultIssueShouldBeFound("state.specified=true");

        // Get all the issueList where state is null
        defaultIssueShouldNotBeFound("state.specified=false");
    }

    @Test
    @Transactional
    public void getAllIssuesByAuthorIsEqualToSomething() throws Exception {
        // Initialize the database
        issueRepository.saveAndFlush(issue);

        // Get all the issueList where author equals to DEFAULT_AUTHOR
        defaultIssueShouldBeFound("author.equals=" + DEFAULT_AUTHOR);

        // Get all the issueList where author equals to UPDATED_AUTHOR
        defaultIssueShouldNotBeFound("author.equals=" + UPDATED_AUTHOR);
    }

    @Test
    @Transactional
    public void getAllIssuesByAuthorIsNotEqualToSomething() throws Exception {
        // Initialize the database
        issueRepository.saveAndFlush(issue);

        // Get all the issueList where author not equals to DEFAULT_AUTHOR
        defaultIssueShouldNotBeFound("author.notEquals=" + DEFAULT_AUTHOR);

        // Get all the issueList where author not equals to UPDATED_AUTHOR
        defaultIssueShouldBeFound("author.notEquals=" + UPDATED_AUTHOR);
    }

    @Test
    @Transactional
    public void getAllIssuesByAuthorIsInShouldWork() throws Exception {
        // Initialize the database
        issueRepository.saveAndFlush(issue);

        // Get all the issueList where author in DEFAULT_AUTHOR or UPDATED_AUTHOR
        defaultIssueShouldBeFound("author.in=" + DEFAULT_AUTHOR + "," + UPDATED_AUTHOR);

        // Get all the issueList where author equals to UPDATED_AUTHOR
        defaultIssueShouldNotBeFound("author.in=" + UPDATED_AUTHOR);
    }

    @Test
    @Transactional
    public void getAllIssuesByAuthorIsNullOrNotNull() throws Exception {
        // Initialize the database
        issueRepository.saveAndFlush(issue);

        // Get all the issueList where author is not null
        defaultIssueShouldBeFound("author.specified=true");

        // Get all the issueList where author is null
        defaultIssueShouldNotBeFound("author.specified=false");
    }

    @Test
    @Transactional
    public void getAllIssuesByAuthorContainsSomething() throws Exception {
        // Initialize the database
        issueRepository.saveAndFlush(issue);

        // Get all the issueList where author contains DEFAULT_AUTHOR
        defaultIssueShouldBeFound("author.contains=" + DEFAULT_AUTHOR);

        // Get all the issueList where author contains UPDATED_AUTHOR
        defaultIssueShouldNotBeFound("author.contains=" + UPDATED_AUTHOR);
    }

    @Test
    @Transactional
    public void getAllIssuesByAuthorNotContainsSomething() throws Exception {
        // Initialize the database
        issueRepository.saveAndFlush(issue);

        // Get all the issueList where author does not contain DEFAULT_AUTHOR
        defaultIssueShouldNotBeFound("author.doesNotContain=" + DEFAULT_AUTHOR);

        // Get all the issueList where author does not contain UPDATED_AUTHOR
        defaultIssueShouldBeFound("author.doesNotContain=" + UPDATED_AUTHOR);
    }

    @Test
    @Transactional
    public void getAllIssuesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        issueRepository.saveAndFlush(issue);

        // Get all the issueList where description equals to DEFAULT_DESCRIPTION
        defaultIssueShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the issueList where description equals to UPDATED_DESCRIPTION
        defaultIssueShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllIssuesByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        issueRepository.saveAndFlush(issue);

        // Get all the issueList where description not equals to DEFAULT_DESCRIPTION
        defaultIssueShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the issueList where description not equals to UPDATED_DESCRIPTION
        defaultIssueShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllIssuesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        issueRepository.saveAndFlush(issue);

        // Get all the issueList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultIssueShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the issueList where description equals to UPDATED_DESCRIPTION
        defaultIssueShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllIssuesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        issueRepository.saveAndFlush(issue);

        // Get all the issueList where description is not null
        defaultIssueShouldBeFound("description.specified=true");

        // Get all the issueList where description is null
        defaultIssueShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    public void getAllIssuesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        issueRepository.saveAndFlush(issue);

        // Get all the issueList where description contains DEFAULT_DESCRIPTION
        defaultIssueShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the issueList where description contains UPDATED_DESCRIPTION
        defaultIssueShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllIssuesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        issueRepository.saveAndFlush(issue);

        // Get all the issueList where description does not contain DEFAULT_DESCRIPTION
        defaultIssueShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the issueList where description does not contain UPDATED_DESCRIPTION
        defaultIssueShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllIssuesByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        issueRepository.saveAndFlush(issue);

        // Get all the issueList where createdAt equals to DEFAULT_CREATED_AT
        defaultIssueShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the issueList where createdAt equals to UPDATED_CREATED_AT
        defaultIssueShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    public void getAllIssuesByCreatedAtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        issueRepository.saveAndFlush(issue);

        // Get all the issueList where createdAt not equals to DEFAULT_CREATED_AT
        defaultIssueShouldNotBeFound("createdAt.notEquals=" + DEFAULT_CREATED_AT);

        // Get all the issueList where createdAt not equals to UPDATED_CREATED_AT
        defaultIssueShouldBeFound("createdAt.notEquals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    public void getAllIssuesByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        issueRepository.saveAndFlush(issue);

        // Get all the issueList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultIssueShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the issueList where createdAt equals to UPDATED_CREATED_AT
        defaultIssueShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    public void getAllIssuesByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        issueRepository.saveAndFlush(issue);

        // Get all the issueList where createdAt is not null
        defaultIssueShouldBeFound("createdAt.specified=true");

        // Get all the issueList where createdAt is null
        defaultIssueShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    public void getAllIssuesByCreatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        issueRepository.saveAndFlush(issue);

        // Get all the issueList where createdAt is greater than or equal to DEFAULT_CREATED_AT
        defaultIssueShouldBeFound("createdAt.greaterThanOrEqual=" + DEFAULT_CREATED_AT);

        // Get all the issueList where createdAt is greater than or equal to UPDATED_CREATED_AT
        defaultIssueShouldNotBeFound("createdAt.greaterThanOrEqual=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    public void getAllIssuesByCreatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        issueRepository.saveAndFlush(issue);

        // Get all the issueList where createdAt is less than or equal to DEFAULT_CREATED_AT
        defaultIssueShouldBeFound("createdAt.lessThanOrEqual=" + DEFAULT_CREATED_AT);

        // Get all the issueList where createdAt is less than or equal to SMALLER_CREATED_AT
        defaultIssueShouldNotBeFound("createdAt.lessThanOrEqual=" + SMALLER_CREATED_AT);
    }

    @Test
    @Transactional
    public void getAllIssuesByCreatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        issueRepository.saveAndFlush(issue);

        // Get all the issueList where createdAt is less than DEFAULT_CREATED_AT
        defaultIssueShouldNotBeFound("createdAt.lessThan=" + DEFAULT_CREATED_AT);

        // Get all the issueList where createdAt is less than UPDATED_CREATED_AT
        defaultIssueShouldBeFound("createdAt.lessThan=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    public void getAllIssuesByCreatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        issueRepository.saveAndFlush(issue);

        // Get all the issueList where createdAt is greater than DEFAULT_CREATED_AT
        defaultIssueShouldNotBeFound("createdAt.greaterThan=" + DEFAULT_CREATED_AT);

        // Get all the issueList where createdAt is greater than SMALLER_CREATED_AT
        defaultIssueShouldBeFound("createdAt.greaterThan=" + SMALLER_CREATED_AT);
    }

    @Test
    @Transactional
    public void getAllIssuesByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        issueRepository.saveAndFlush(issue);

        // Get all the issueList where updatedAt equals to DEFAULT_UPDATED_AT
        defaultIssueShouldBeFound("updatedAt.equals=" + DEFAULT_UPDATED_AT);

        // Get all the issueList where updatedAt equals to UPDATED_UPDATED_AT
        defaultIssueShouldNotBeFound("updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    public void getAllIssuesByUpdatedAtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        issueRepository.saveAndFlush(issue);

        // Get all the issueList where updatedAt not equals to DEFAULT_UPDATED_AT
        defaultIssueShouldNotBeFound("updatedAt.notEquals=" + DEFAULT_UPDATED_AT);

        // Get all the issueList where updatedAt not equals to UPDATED_UPDATED_AT
        defaultIssueShouldBeFound("updatedAt.notEquals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    public void getAllIssuesByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        issueRepository.saveAndFlush(issue);

        // Get all the issueList where updatedAt in DEFAULT_UPDATED_AT or UPDATED_UPDATED_AT
        defaultIssueShouldBeFound("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT);

        // Get all the issueList where updatedAt equals to UPDATED_UPDATED_AT
        defaultIssueShouldNotBeFound("updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    public void getAllIssuesByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        issueRepository.saveAndFlush(issue);

        // Get all the issueList where updatedAt is not null
        defaultIssueShouldBeFound("updatedAt.specified=true");

        // Get all the issueList where updatedAt is null
        defaultIssueShouldNotBeFound("updatedAt.specified=false");
    }

    @Test
    @Transactional
    public void getAllIssuesByUpdatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        issueRepository.saveAndFlush(issue);

        // Get all the issueList where updatedAt is greater than or equal to DEFAULT_UPDATED_AT
        defaultIssueShouldBeFound("updatedAt.greaterThanOrEqual=" + DEFAULT_UPDATED_AT);

        // Get all the issueList where updatedAt is greater than or equal to UPDATED_UPDATED_AT
        defaultIssueShouldNotBeFound("updatedAt.greaterThanOrEqual=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    public void getAllIssuesByUpdatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        issueRepository.saveAndFlush(issue);

        // Get all the issueList where updatedAt is less than or equal to DEFAULT_UPDATED_AT
        defaultIssueShouldBeFound("updatedAt.lessThanOrEqual=" + DEFAULT_UPDATED_AT);

        // Get all the issueList where updatedAt is less than or equal to SMALLER_UPDATED_AT
        defaultIssueShouldNotBeFound("updatedAt.lessThanOrEqual=" + SMALLER_UPDATED_AT);
    }

    @Test
    @Transactional
    public void getAllIssuesByUpdatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        issueRepository.saveAndFlush(issue);

        // Get all the issueList where updatedAt is less than DEFAULT_UPDATED_AT
        defaultIssueShouldNotBeFound("updatedAt.lessThan=" + DEFAULT_UPDATED_AT);

        // Get all the issueList where updatedAt is less than UPDATED_UPDATED_AT
        defaultIssueShouldBeFound("updatedAt.lessThan=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    public void getAllIssuesByUpdatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        issueRepository.saveAndFlush(issue);

        // Get all the issueList where updatedAt is greater than DEFAULT_UPDATED_AT
        defaultIssueShouldNotBeFound("updatedAt.greaterThan=" + DEFAULT_UPDATED_AT);

        // Get all the issueList where updatedAt is greater than SMALLER_UPDATED_AT
        defaultIssueShouldBeFound("updatedAt.greaterThan=" + SMALLER_UPDATED_AT);
    }

    @Test
    @Transactional
    public void getAllIssuesByClosedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        issueRepository.saveAndFlush(issue);

        // Get all the issueList where closedAt equals to DEFAULT_CLOSED_AT
        defaultIssueShouldBeFound("closedAt.equals=" + DEFAULT_CLOSED_AT);

        // Get all the issueList where closedAt equals to UPDATED_CLOSED_AT
        defaultIssueShouldNotBeFound("closedAt.equals=" + UPDATED_CLOSED_AT);
    }

    @Test
    @Transactional
    public void getAllIssuesByClosedAtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        issueRepository.saveAndFlush(issue);

        // Get all the issueList where closedAt not equals to DEFAULT_CLOSED_AT
        defaultIssueShouldNotBeFound("closedAt.notEquals=" + DEFAULT_CLOSED_AT);

        // Get all the issueList where closedAt not equals to UPDATED_CLOSED_AT
        defaultIssueShouldBeFound("closedAt.notEquals=" + UPDATED_CLOSED_AT);
    }

    @Test
    @Transactional
    public void getAllIssuesByClosedAtIsInShouldWork() throws Exception {
        // Initialize the database
        issueRepository.saveAndFlush(issue);

        // Get all the issueList where closedAt in DEFAULT_CLOSED_AT or UPDATED_CLOSED_AT
        defaultIssueShouldBeFound("closedAt.in=" + DEFAULT_CLOSED_AT + "," + UPDATED_CLOSED_AT);

        // Get all the issueList where closedAt equals to UPDATED_CLOSED_AT
        defaultIssueShouldNotBeFound("closedAt.in=" + UPDATED_CLOSED_AT);
    }

    @Test
    @Transactional
    public void getAllIssuesByClosedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        issueRepository.saveAndFlush(issue);

        // Get all the issueList where closedAt is not null
        defaultIssueShouldBeFound("closedAt.specified=true");

        // Get all the issueList where closedAt is null
        defaultIssueShouldNotBeFound("closedAt.specified=false");
    }

    @Test
    @Transactional
    public void getAllIssuesByClosedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        issueRepository.saveAndFlush(issue);

        // Get all the issueList where closedAt is greater than or equal to DEFAULT_CLOSED_AT
        defaultIssueShouldBeFound("closedAt.greaterThanOrEqual=" + DEFAULT_CLOSED_AT);

        // Get all the issueList where closedAt is greater than or equal to UPDATED_CLOSED_AT
        defaultIssueShouldNotBeFound("closedAt.greaterThanOrEqual=" + UPDATED_CLOSED_AT);
    }

    @Test
    @Transactional
    public void getAllIssuesByClosedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        issueRepository.saveAndFlush(issue);

        // Get all the issueList where closedAt is less than or equal to DEFAULT_CLOSED_AT
        defaultIssueShouldBeFound("closedAt.lessThanOrEqual=" + DEFAULT_CLOSED_AT);

        // Get all the issueList where closedAt is less than or equal to SMALLER_CLOSED_AT
        defaultIssueShouldNotBeFound("closedAt.lessThanOrEqual=" + SMALLER_CLOSED_AT);
    }

    @Test
    @Transactional
    public void getAllIssuesByClosedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        issueRepository.saveAndFlush(issue);

        // Get all the issueList where closedAt is less than DEFAULT_CLOSED_AT
        defaultIssueShouldNotBeFound("closedAt.lessThan=" + DEFAULT_CLOSED_AT);

        // Get all the issueList where closedAt is less than UPDATED_CLOSED_AT
        defaultIssueShouldBeFound("closedAt.lessThan=" + UPDATED_CLOSED_AT);
    }

    @Test
    @Transactional
    public void getAllIssuesByClosedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        issueRepository.saveAndFlush(issue);

        // Get all the issueList where closedAt is greater than DEFAULT_CLOSED_AT
        defaultIssueShouldNotBeFound("closedAt.greaterThan=" + DEFAULT_CLOSED_AT);

        // Get all the issueList where closedAt is greater than SMALLER_CLOSED_AT
        defaultIssueShouldBeFound("closedAt.greaterThan=" + SMALLER_CLOSED_AT);
    }

    @Test
    @Transactional
    public void getAllIssuesByClosedByIsEqualToSomething() throws Exception {
        // Initialize the database
        issueRepository.saveAndFlush(issue);

        // Get all the issueList where closedBy equals to DEFAULT_CLOSED_BY
        defaultIssueShouldBeFound("closedBy.equals=" + DEFAULT_CLOSED_BY);

        // Get all the issueList where closedBy equals to UPDATED_CLOSED_BY
        defaultIssueShouldNotBeFound("closedBy.equals=" + UPDATED_CLOSED_BY);
    }

    @Test
    @Transactional
    public void getAllIssuesByClosedByIsNotEqualToSomething() throws Exception {
        // Initialize the database
        issueRepository.saveAndFlush(issue);

        // Get all the issueList where closedBy not equals to DEFAULT_CLOSED_BY
        defaultIssueShouldNotBeFound("closedBy.notEquals=" + DEFAULT_CLOSED_BY);

        // Get all the issueList where closedBy not equals to UPDATED_CLOSED_BY
        defaultIssueShouldBeFound("closedBy.notEquals=" + UPDATED_CLOSED_BY);
    }

    @Test
    @Transactional
    public void getAllIssuesByClosedByIsInShouldWork() throws Exception {
        // Initialize the database
        issueRepository.saveAndFlush(issue);

        // Get all the issueList where closedBy in DEFAULT_CLOSED_BY or UPDATED_CLOSED_BY
        defaultIssueShouldBeFound("closedBy.in=" + DEFAULT_CLOSED_BY + "," + UPDATED_CLOSED_BY);

        // Get all the issueList where closedBy equals to UPDATED_CLOSED_BY
        defaultIssueShouldNotBeFound("closedBy.in=" + UPDATED_CLOSED_BY);
    }

    @Test
    @Transactional
    public void getAllIssuesByClosedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        issueRepository.saveAndFlush(issue);

        // Get all the issueList where closedBy is not null
        defaultIssueShouldBeFound("closedBy.specified=true");

        // Get all the issueList where closedBy is null
        defaultIssueShouldNotBeFound("closedBy.specified=false");
    }

    @Test
    @Transactional
    public void getAllIssuesByClosedByContainsSomething() throws Exception {
        // Initialize the database
        issueRepository.saveAndFlush(issue);

        // Get all the issueList where closedBy contains DEFAULT_CLOSED_BY
        defaultIssueShouldBeFound("closedBy.contains=" + DEFAULT_CLOSED_BY);

        // Get all the issueList where closedBy contains UPDATED_CLOSED_BY
        defaultIssueShouldNotBeFound("closedBy.contains=" + UPDATED_CLOSED_BY);
    }

    @Test
    @Transactional
    public void getAllIssuesByClosedByNotContainsSomething() throws Exception {
        // Initialize the database
        issueRepository.saveAndFlush(issue);

        // Get all the issueList where closedBy does not contain DEFAULT_CLOSED_BY
        defaultIssueShouldNotBeFound("closedBy.doesNotContain=" + DEFAULT_CLOSED_BY);

        // Get all the issueList where closedBy does not contain UPDATED_CLOSED_BY
        defaultIssueShouldBeFound("closedBy.doesNotContain=" + UPDATED_CLOSED_BY);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultIssueShouldBeFound(String filter) throws Exception {
        restIssueMockMvc
            .perform(get("/api/issues?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(issue.getId().intValue())))
            .andExpect(jsonPath("$.[*].issueId").value(hasItem(DEFAULT_ISSUE_ID)))
            .andExpect(jsonPath("$.[*].issueOrder").value(hasItem(DEFAULT_ISSUE_ORDER)))
            .andExpect(jsonPath("$.[*].issueTitle").value(hasItem(DEFAULT_ISSUE_TITLE)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.booleanValue())))
            .andExpect(jsonPath("$.[*].author").value(hasItem(DEFAULT_AUTHOR)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(sameInstant(DEFAULT_UPDATED_AT))))
            .andExpect(jsonPath("$.[*].closedAt").value(hasItem(sameInstant(DEFAULT_CLOSED_AT))))
            .andExpect(jsonPath("$.[*].closedBy").value(hasItem(DEFAULT_CLOSED_BY)));

        // Check, that the count call also returns 1
        restIssueMockMvc
            .perform(get("/api/issues/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultIssueShouldNotBeFound(String filter) throws Exception {
        restIssueMockMvc
            .perform(get("/api/issues?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restIssueMockMvc
            .perform(get("/api/issues/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingIssue() throws Exception {
        // Get the issue
        restIssueMockMvc.perform(get("/api/issues/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateIssue() throws Exception {
        // Initialize the database
        issueRepository.saveAndFlush(issue);

        int databaseSizeBeforeUpdate = issueRepository.findAll().size();

        // Update the issue
        Issue updatedIssue = issueRepository.findById(issue.getId()).get();
        // Disconnect from session so that the updates on updatedIssue are not directly saved in db
        em.detach(updatedIssue);
        updatedIssue
            .issueId(UPDATED_ISSUE_ID)
            .issueOrder(UPDATED_ISSUE_ORDER)
            .issueTitle(UPDATED_ISSUE_TITLE)
            .state(UPDATED_STATE)
            .author(UPDATED_AUTHOR)
            .description(UPDATED_DESCRIPTION)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .closedAt(UPDATED_CLOSED_AT)
            .closedBy(UPDATED_CLOSED_BY);
        IssueDTO issueDTO = issueMapper.toDto(updatedIssue);

        restIssueMockMvc
            .perform(put("/api/issues").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(issueDTO)))
            .andExpect(status().isOk());

        // Validate the Issue in the database
        List<Issue> issueList = issueRepository.findAll();
        assertThat(issueList).hasSize(databaseSizeBeforeUpdate);
        Issue testIssue = issueList.get(issueList.size() - 1);
        assertThat(testIssue.getIssueId()).isEqualTo(UPDATED_ISSUE_ID);
        assertThat(testIssue.getIssueOrder()).isEqualTo(UPDATED_ISSUE_ORDER);
        assertThat(testIssue.getIssueTitle()).isEqualTo(UPDATED_ISSUE_TITLE);
        assertThat(testIssue.isState()).isEqualTo(UPDATED_STATE);
        assertThat(testIssue.getAuthor()).isEqualTo(UPDATED_AUTHOR);
        assertThat(testIssue.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testIssue.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testIssue.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testIssue.getClosedAt()).isEqualTo(UPDATED_CLOSED_AT);
        assertThat(testIssue.getClosedBy()).isEqualTo(UPDATED_CLOSED_BY);
    }

    @Test
    @Transactional
    public void updateNonExistingIssue() throws Exception {
        int databaseSizeBeforeUpdate = issueRepository.findAll().size();

        // Create the Issue
        IssueDTO issueDTO = issueMapper.toDto(issue);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIssueMockMvc
            .perform(put("/api/issues").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(issueDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Issue in the database
        List<Issue> issueList = issueRepository.findAll();
        assertThat(issueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteIssue() throws Exception {
        // Initialize the database
        issueRepository.saveAndFlush(issue);

        int databaseSizeBeforeDelete = issueRepository.findAll().size();

        // Delete the issue
        restIssueMockMvc
            .perform(delete("/api/issues/{id}", issue.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Issue> issueList = issueRepository.findAll();
        assertThat(issueList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
