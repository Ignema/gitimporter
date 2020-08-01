package com.gitimporter.config;

import com.gitimporter.repository.GitInfoRepository;
import com.gitimporter.service.GitInfoQueryService;
import com.gitimporter.service.GitInfoService;
import com.gitimporter.service.impl.GitInfoServiceImpl;
import com.gitimporter.service.mapper.GitInfoMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GitInfoConfiguration {

    @Bean
    public GitInfoService gitInfoService(GitInfoRepository gitInfoRepository, GitInfoMapper gitInfoMapper) {
        return new GitInfoServiceImpl(gitInfoRepository, gitInfoMapper);
    }
}
