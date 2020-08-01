package com.gitimporter.service.impl;

import com.gitimporter.domain.GitInfo;
import com.gitimporter.repository.GitInfoRepository;
import com.gitimporter.service.GitInfoQueryService;
import com.gitimporter.service.GitInfoService;
import com.gitimporter.service.dto.GitInfoDTO;
import com.gitimporter.service.mapper.GitInfoMapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.nio.charset.StandardCharsets;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class GitInfoServiceImpl extends GitInfoService {
    private final Logger log = LoggerFactory.getLogger(GitInfoQueryService.class);

    private final GitInfoRepository gitInfoRepository;

    private final GitInfoMapper gitInfoMapper;

    public GitInfoServiceImpl(GitInfoRepository gitInfoRepository, GitInfoMapper gitInfoMapper) {
        super(gitInfoRepository, gitInfoMapper);
        this.gitInfoRepository = gitInfoRepository;
        this.gitInfoMapper = gitInfoMapper;
    }

    public void refresh(String user) throws ParseException, IOException {
        URL url = null;
        try {
            url = new URL("https://gitlab.com/api/v4/users/" + user + "/projects");
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

            GitInfoDTO data = new GitInfoDTO();
            data.setId((long) obj.indexOf(jo));
            data.setGitProjectId(Integer.parseInt(String.valueOf(jo.get("id"))));
            data.setGitProjectName((String) jo.get("name"));

            URL issuesURL = new URL("https://gitlab.com/api/v4/projects/" + data.getGitProjectId() + "/issues");

            HttpURLConnection conn = (HttpURLConnection) issuesURL.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");

            JSONArray Tab;

            try {
                Tab = (JSONArray) new JSONParser().parse(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            } finally {
                conn.disconnect();
            }

            data.setIssueCount(Tab.size());

            GitInfo gitInfo = gitInfoMapper.toEntity(data);
            gitInfoRepository.save(gitInfo);
        }
    }
    //    public Page<GitInfoDTO> findAll(Pageable page) throws IOException, ParseException {
    //
    //        URL url = null;
    //        try {
    //            url = new URL("https://gitlab.com/api/v4/projects");
    //        } catch (MalformedURLException e) {
    //            log.debug("BAD URL");
    //        }
    //
    //        assert url != null;
    //        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    //        connection.setRequestMethod("GET");
    //
    //        InputStream input = connection.getInputStream();
    //
    //        JSONArray obj = (JSONArray) new JSONParser().parse(new InputStreamReader(input, StandardCharsets.UTF_8));
    //
    //
    //        //        try (BufferedReader in = new BufferedReader(
    //        //            new InputStreamReader(connection.getInputStream())))
    //        //        {
    //        //            String line;
    //        //            while ((line = in.readLine()) != null) {
    //        //
    //        //                obj.add( new JSONParser().parse(line) );
    //        //            }
    //        //
    //        //        } catch (ParseException | IOException e) {
    //        //
    //        //            log.debug("FAILED TO PARSE DATA");
    //        //
    //        //        }
    //
    //        //        GitLabApi gitLabApi = new GitLabApi("https://gitlab.com/api/v4", "8Zthzhyh_vzSGtV-sJ7a");
    //        //
    //        //        List<Project> projects = gitLabApi.getProjectApi().getProjects();
    //
    //        List<GitInfoDTO> info = new ArrayList<>();
    //
    //        for (Object object : obj) {
    //            JSONObject jo = (JSONObject) object;
    //
    //            // info.add(new GitInfo(project.getId(), project.getName(), gitLabApi.getIssuesApi().getIssues(project.getId()).size()));
    //
    //            GitInfoDTO data = new GitInfoDTO();
    //            data.setId((long) obj.indexOf(jo));
    //            data.setGitProjectId(Integer.parseInt(String.valueOf(jo.get("id"))));
    //            data.setGitProjectName((String) jo.get("name"));
    //            JSONArray Tab = (JSONArray) new JSONParser()
    //                .parse(
    //                    new InputStreamReader(
    //                        new URL("https://gitlab.com/api/v4/projects/" + data.getGitProjectId() + "/issues")
    //                            .openConnection()
    //                            .getInputStream(),
    //                        StandardCharsets.UTF_8
    //                    )
    //                );
    //            data.setIssueCount(Tab.size());
    //            //data.setIssueCount(gitLabApi.getIssuesApi().getIssues(project.getId()).size());
    //
    //            info.add(data);
    //        }
    //
    //
    //        int start = (int) page.getOffset();
    //        int end = Math.min((start + page.getPageSize()), info.size());
    //
    //        return new PageImpl<GitInfoDTO>(Objects.requireNonNull(info.subList(start, end)), page, info.size());
    //    }
    //
    //
    //    public Optional<GitInfoDTO> findOne(Long id) throws IOException, ParseException {
    //
    //        log.debug("findAll STARTED EXECUTING");
    //
    //        URL url = null;
    //        try {
    //            url = new URL("https://gitlab.com/api/v4/projects/" + id.toString());
    //        } catch (MalformedURLException e) {
    //            log.debug("BAD URL");
    //        }
    //
    //        assert url != null;
    //        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    //        connection.setRequestMethod("GET");
    //
    //        InputStream input = connection.getInputStream();
    //
    //        JSONObject obj = (JSONObject) new JSONParser().parse(new InputStreamReader(input, StandardCharsets.UTF_8));
    //
    //        GitInfoDTO info = new GitInfoDTO();
    //
    //        info.setId(id);
    //        info.setGitProjectId(Integer.parseInt(String.valueOf(obj.get("id"))));
    //        info.setGitProjectName((String) obj.get("name"));
    //        JSONArray Tab = (JSONArray) new JSONParser()
    //            .parse(
    //                new InputStreamReader(
    //                    new URL("https://gitlab.com/api/v4/projects/" + info.getGitProjectId() + "/issues")
    //                        .openConnection()
    //                        .getInputStream(),
    //                    StandardCharsets.UTF_8
    //                )
    //            );
    //        info.setIssueCount(Tab.size());
    //
    //
    //        return Optional.of(info);
    //
    //
    //    }

}
