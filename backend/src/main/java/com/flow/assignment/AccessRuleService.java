package com.flow.assignment;

import com.flow.assignment.domain.AccessRule;
import com.flow.assignment.domain.AccessRuleRepository;
import com.flow.assignment.dto.request.CreatingAccessRuleRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AccessRuleService {

    private final AccessRuleRepository accessRuleRepository;

    public Long save(final CreatingAccessRuleRequest request, final String timeZone) {
        AccessRule accessRule = AccessRule.builder().ipAddress(request.getIpAddress())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .content(request.getContent())
                .timeZone(timeZone)
                .build();

        AccessRule savedAccessRule = accessRuleRepository.save(accessRule);
        return savedAccessRule.getId();
    }
}
