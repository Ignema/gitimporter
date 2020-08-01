package com.gitimporter.config;

import com.gitimporter.repository.GitInfoRepository;
import com.gitimporter.repository.IssueRepository;
import com.gitimporter.service.GitInfoService;
import com.gitimporter.service.IssueService;
import com.gitimporter.service.impl.GitInfoServiceImpl;
import com.gitimporter.service.impl.IssueServiceImpl;
import com.gitimporter.service.mapper.GitInfoMapper;
import com.gitimporter.service.mapper.IssueMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IssueConfiguration {

    @Bean
    public IssueService issueService(IssueRepository issueRepository, IssueMapper issueMapper) {
        return new IssueServiceImpl(issueRepository, issueMapper);
    }
}
