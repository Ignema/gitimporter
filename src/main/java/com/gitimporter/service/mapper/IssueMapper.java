package com.gitimporter.service.mapper;

import com.gitimporter.domain.*;
import com.gitimporter.service.dto.IssueDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Issue} and its DTO {@link IssueDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface IssueMapper extends EntityMapper<IssueDTO, Issue> {
    default Issue fromId(Long id) {
        if (id == null) {
            return null;
        }
        Issue issue = new Issue();
        issue.setId(id);
        return issue;
    }
}
