package com.gitimporter.service.impl;

import com.gitimporter.domain.GitInfo;
import com.gitimporter.repository.GitInfoRepository;
import com.gitimporter.service.GitInfoQueryService;
import com.gitimporter.service.dto.GitInfoCriteria;
import com.gitimporter.service.dto.GitInfoDTO;
import com.gitimporter.service.mapper.GitInfoMapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Function;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class GitInfoServiceImpl extends GitInfoQueryService {
    private final Logger log = LoggerFactory.getLogger(GitInfoQueryService.class);

    public GitInfoServiceImpl(GitInfoRepository gitInfoRepository, GitInfoMapper gitInfoMapper) {
        super(gitInfoRepository, gitInfoMapper);
    }

    public Page<GitInfoDTO> findByCriteria(GitInfoCriteria criteria, Pageable page) throws IOException, ParseException {
        log.debug("findAll STARTED EXECUTING");

        URL url = null;
        try {
            url = new URL("https://gitlab.com/api/v4/projects");
        } catch (MalformedURLException e) {
            log.debug("BAD URL");
        }

        assert url != null;
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        InputStream input = connection.getInputStream();

        JSONArray obj = (JSONArray) new JSONParser().parse(new InputStreamReader(input, StandardCharsets.UTF_8));

        log.debug(obj.toString());

        //        try (BufferedReader in = new BufferedReader(
        //            new InputStreamReader(connection.getInputStream())))
        //        {
        //            String line;
        //            while ((line = in.readLine()) != null) {
        //
        //                obj.add( new JSONParser().parse(line) );
        //            }
        //
        //        } catch (ParseException | IOException e) {
        //
        //            log.debug("FAILED TO PARSE DATA");
        //
        //        }

        //        GitLabApi gitLabApi = new GitLabApi("https://gitlab.com/api/v4", "8Zthzhyh_vzSGtV-sJ7a");
        //
        //        List<Project> projects = gitLabApi.getProjectApi().getProjects();

        List<GitInfoDTO> info = new ArrayList<>();

        log.debug("STARTING ITERATIONS");

        for (Object object : obj) {
            JSONObject jo = (JSONObject) object;

            // info.add(new GitInfo(project.getId(), project.getName(), gitLabApi.getIssuesApi().getIssues(project.getId()).size()));

            log.debug("BEGIN INSERTION");

            GitInfoDTO data = new GitInfoDTO();
            data.setId((long) obj.indexOf(jo));
            data.setGitProjectId(Integer.parseInt(String.valueOf(jo.get("id"))));
            data.setGitProjectName((String) jo.get("name"));
            JSONArray Tab = (JSONArray) new JSONParser()
            .parse(
                    new InputStreamReader(
                        new URL("https://gitlab.com/api/v4/projects/" + data.getGitProjectId() + "/issues")
                            .openConnection()
                            .getInputStream(),
                        StandardCharsets.UTF_8
                    )
                );
            data.setIssueCount(Tab.size());
            //data.setIssueCount(gitLabApi.getIssuesApi().getIssues(project.getId()).size());

            log.debug("INSERTION COMPLETE");

            info.add(data);

            log.debug("FIRST ITERATION COMPLETE");
        }

        log.debug("INITIATING PAGE SETUP");

        int start = (int) page.getOffset();
        int end = Math.min((start + page.getPageSize()), info.size());

        log.debug("RETURNING VALUE...");

        final Specification<GitInfo> specification = createSpecification(criteria);

        return new PageImpl<GitInfoDTO>(Objects.requireNonNull(info.subList(start, end)), page, info.size());
    }
}
