package com.gitimporter.service;

import com.gitimporter.domain.GitInfo;
import com.gitimporter.repository.GitInfoRepository;
import com.gitimporter.service.dto.GitInfoDTO;
import com.gitimporter.service.mapper.GitInfoMapper;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link GitInfo}.
 */
@Service
@Transactional
public class GitInfoService {
    private final Logger log = LoggerFactory.getLogger(GitInfoService.class);

    private final GitInfoRepository gitInfoRepository;

    private final GitInfoMapper gitInfoMapper;

    @Autowired
    public GitInfoService(GitInfoRepository gitInfoRepository, GitInfoMapper gitInfoMapper) {
        this.gitInfoRepository = gitInfoRepository;
        this.gitInfoMapper = gitInfoMapper;
    }

    /**
     * Save a gitInfo.
     *
     * @param gitInfoDTO the entity to save.
     * @return the persisted entity.
     */
    public GitInfoDTO save(GitInfoDTO gitInfoDTO) {
        log.debug("Request to save GitInfo : {}", gitInfoDTO);
        GitInfo gitInfo = gitInfoMapper.toEntity(gitInfoDTO);
        gitInfo = gitInfoRepository.save(gitInfo);
        return gitInfoMapper.toDto(gitInfo);
    }

    /**
     * Get all the gitInfos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<GitInfoDTO> findAll(Pageable pageable) throws IOException {
        log.debug("Request to get all GitInfos");

        return gitInfoRepository.findAll(pageable).map(gitInfoMapper::toDto);
    }

    /**
     * Get one gitInfo by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<GitInfoDTO> findOne(Long id) {
        log.debug("Request to get GitInfo : {}", id);
        return gitInfoRepository.findById(id).map(gitInfoMapper::toDto);
    }

    /**
     * Delete the gitInfo by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete GitInfo : {}", id);
        gitInfoRepository.deleteById(id);
    }
}
