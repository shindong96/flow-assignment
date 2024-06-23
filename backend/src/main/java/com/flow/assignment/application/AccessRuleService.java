package com.flow.assignment.application;

import com.flow.assignment.domain.AccessRule;
import com.flow.assignment.domain.AccessRuleRepository;
import com.flow.assignment.dto.request.CreatingAccessRuleRequest;
import com.flow.assignment.dto.request.PagingRequest;
import com.flow.assignment.dto.response.IpAccessRuleResponses;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AccessRuleService {

    private static final int DIFFERENCES_PAGES_AND_DB_INDEX = 1;
    private static final Sort SORT_DIRECTION_DESC_BY_ID = Sort.by(Direction.DESC, "id");
    private static final String GMT_TIME_ZONE = "GMT";

    private final AccessRuleRepository accessRuleRepository;

    public Long save(final CreatingAccessRuleRequest request, final String timeZone) {
        AccessRule accessRule = AccessRule.builder()
                .ipAddress(request.getIpAddress())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .content(request.getContent())
                .timeZone(timeZone)
                .build();

        AccessRule savedAccessRule = accessRuleRepository.save(
                accessRule.convertTimeZone(GMT_TIME_ZONE));
        return savedAccessRule.getId();
    }

    @Transactional(readOnly = true)
    public IpAccessRuleResponses findAll(final PagingRequest pagingRequest, final String timeZone) {
        Slice<AccessRule> rules = accessRuleRepository.findAll(
                PageRequest.of(pagingRequest.getPage() - DIFFERENCES_PAGES_AND_DB_INDEX, pagingRequest.getSize()
                        , SORT_DIRECTION_DESC_BY_ID));

        List<AccessRule> convertedTimeRules = convertTimeZone(rules.getContent(), timeZone);
        return IpAccessRuleResponses.of(rules.hasNext(), convertedTimeRules);
    }

    @Transactional(readOnly = true)
    public IpAccessRuleResponses findByContentContaining(final PagingRequest pagingRequest, final String content,
                                                         final String timeZone) {
        if (content == null) {
            return findAll(pagingRequest, timeZone);
        }

        Slice<AccessRule> rules = accessRuleRepository.findByContentContaining(content,
                PageRequest.of(pagingRequest.getPage() - DIFFERENCES_PAGES_AND_DB_INDEX, pagingRequest.getSize()
                        , SORT_DIRECTION_DESC_BY_ID));

        List<AccessRule> convertedTimeRules = convertTimeZone(rules.getContent(), timeZone);
        return IpAccessRuleResponses.of(rules.hasNext(), convertedTimeRules);
    }

    public void deleteById(final Long id) {
        accessRuleRepository.deleteById(id);
    }

    private List<AccessRule> convertTimeZone(final List<AccessRule> rules, final String timeZone) {
        return rules.stream()
                .map(accessRule -> accessRule.convertTimeZone(timeZone))
                .collect(Collectors.toList());
    }
}
