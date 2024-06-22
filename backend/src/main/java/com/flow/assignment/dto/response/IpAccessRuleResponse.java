package com.flow.assignment.dto.response;

import com.flow.assignment.domain.AccessRule;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Builder
@Getter
public class IpAccessRuleResponse {

    private final String ipAddress;

    private final String content;

    private final LocalDateTime startTime;

    private final LocalDateTime endTime;

    public static IpAccessRuleResponse from(final AccessRule accessRule) {
        return IpAccessRuleResponse.builder()
                .ipAddress(accessRule.getIpAddress())
                .content(accessRule.getContent())
                .startTime(accessRule.getStartTime())
                .endTime(accessRule.getEndTime())
                .build();
    }
}
