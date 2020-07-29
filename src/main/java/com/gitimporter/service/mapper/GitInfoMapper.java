package com.gitimporter.service.mapper;

import com.gitimporter.domain.*;
import com.gitimporter.service.dto.GitInfoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link GitInfo} and its DTO {@link GitInfoDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface GitInfoMapper extends EntityMapper<GitInfoDTO, GitInfo> {
    default GitInfo fromId(Long id) {
        if (id == null) {
            return null;
        }
        GitInfo gitInfo = new GitInfo();
        gitInfo.setId(id);
        return gitInfo;
    }
}
