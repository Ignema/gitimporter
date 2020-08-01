package com.gitimporter.service.impl;

import com.gitimporter.domain.GitInfo;
import com.gitimporter.domain.Issue;
import com.gitimporter.repository.GitInfoRepository;
import com.gitimporter.repository.IssueRepository;
import com.gitimporter.service.GitInfoQueryService;
import com.gitimporter.service.IssueService;
import com.gitimporter.service.dto.GitInfoDTO;
import com.gitimporter.service.dto.IssueDTO;
import com.gitimporter.service.mapper.GitInfoMapper;
import com.gitimporter.service.mapper.IssueMapper;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IssueServiceImpl extends IssueService {
    private final Logger log = LoggerFactory.getLogger(GitInfoQueryService.class);

    private final IssueRepository issueRepository;

    private final IssueMapper issueMapper;

    public IssueServiceImpl(IssueRepository issueRepository, IssueMapper issueMapper) {
        super(issueRepository, issueMapper);
        this.issueRepository = issueRepository;
        this.issueMapper = issueMapper;
    }

    public void refresh(Integer id) throws ParseException, IOException {
        URL url = null;
        try {
            url = new URL("https://gitlab.com/api/v4/projects/" + id.toString() + "/issues");
        } catch (MalformedURLException e) {
            log.debug("BAD URL");
        }

        assert url != null;
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", "application/json");

        JSONArray obj;

        try {
            InputStream input = connection.getInputStream();

            obj = (JSONArray) new JSONParser().parse(new InputStreamReader(input, StandardCharsets.UTF_8));
        } finally {
            connection.disconnect();
        }

        for (Object object : obj) {
            JSONObject jo = (JSONObject) object;

            IssueDTO data = new IssueDTO();
            data.setId((long) obj.indexOf(jo));
            data.setIssueId(Integer.parseInt(String.valueOf(jo.get("id"))));
            data.setIssueOrder(Integer.parseInt(String.valueOf(jo.get("iid"))));
            data.setIssueTitle((String) jo.get("title"));
            data.setState(true);

            JSONObject author = (JSONObject) jo.get("author");

            data.setAuthor((String) author.get("name"));
            data.setDescription((String) jo.get("description"));
            data.setCreatedAt((String) jo.get("created_at"));
            data.setUpdatedAt((String) jo.get("updated_at"));
            data.setClosedAt((String) jo.get("closed_at"));
            data.setClosedBy((String) author.get("closed_by"));

            Issue issue = issueMapper.toEntity(data);
            issueRepository.save(issue);

            log.debug("SAVED");
        }
    }
}
