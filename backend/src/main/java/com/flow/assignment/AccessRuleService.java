package com.flow.assignment;

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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AccessRuleService {

    private static final int DIFFERENCES_PAGES_AND_DB_INDEX = 1;

    private final AccessRuleRepository accessRuleRepository;

    public Long save(final CreatingAccessRuleRequest request, final String timeZone) {
        AccessRule accessRule = AccessRule.builder()
                .ipAddress(request.getIpAddress())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .content(request.getContent())
                .timeZone(timeZone)
                .build();

        AccessRule savedAccessRule = accessRuleRepository.save(accessRule);
        return savedAccessRule.getId();
    }

    public void deleteById(final Long id) {
        accessRuleRepository.deleteById(id);
    }

    public IpAccessRuleResponses findAll(final PagingRequest pagingRequest, final String timeZone) {
        Slice<AccessRule> rules = accessRuleRepository.findAll(
                PageRequest.of(pagingRequest.getPage() - DIFFERENCES_PAGES_AND_DB_INDEX, pagingRequest.getSize()));

        List<AccessRule> convertedTimeRules = convertTimeZone(rules.getContent(), timeZone);
        return IpAccessRuleResponses.of(rules.hasNext(), convertedTimeRules);
    }

    private List<AccessRule> convertTimeZone(final List<AccessRule> rules, final String timeZone) {
        return rules.stream()
                .map(accessRule -> accessRule.convertTimeZone(timeZone))
                .collect(Collectors.toList());
    }
}
