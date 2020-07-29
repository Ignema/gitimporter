package com.gitimporter.repository;

import com.gitimporter.domain.GitInfo;
import java.io.IOException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the GitInfo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GitInfoRepository extends JpaRepository<GitInfo, Long>, JpaSpecificationExecutor<GitInfo> {}
