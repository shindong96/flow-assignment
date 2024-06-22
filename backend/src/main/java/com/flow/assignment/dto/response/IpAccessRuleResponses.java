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

    private final Boolean hasNext;
    private final List<IpAccessRuleResponse> accessRules;

    public static IpAccessRuleResponses of(final boolean hasNext, final List<AccessRule> rules) {
        return IpAccessRuleResponses.builder()
                .hasNext(hasNext)
                .accessRules(convertToResponse(rules))
                .build();
    }
    
    private static List<IpAccessRuleResponse> convertToResponse(final List<AccessRule> rules) {
        return rules.stream()
                .map(IpAccessRuleResponse::from)
                .collect(Collectors.toList());
    }
}
