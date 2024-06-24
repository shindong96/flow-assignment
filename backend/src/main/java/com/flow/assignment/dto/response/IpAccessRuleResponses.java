package com.flow.assignment.dto.response;

import com.flow.assignment.domain.AccessRule;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
@Builder
public class IpAccessRuleResponses {

    private final Long totalCount;
    private final List<IpAccessRuleResponse> accessRules;

    public static IpAccessRuleResponses of(final long totalCount, final List<AccessRule> rules) {
        return IpAccessRuleResponses.builder()
                .totalCount(totalCount)
                .accessRules(convertToResponse(rules))
                .build();
    }

    private static List<IpAccessRuleResponse> convertToResponse(final List<AccessRule> rules) {
        return rules.stream()
                .map(IpAccessRuleResponse::from)
                .collect(Collectors.toList());
    }
}
