package com.gitimporter.repository;

import com.gitimporter.domain.GitInfo;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the GitInfo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GitInfoRepository extends JpaRepository<GitInfo, Long>, JpaSpecificationExecutor<GitInfo> {}
