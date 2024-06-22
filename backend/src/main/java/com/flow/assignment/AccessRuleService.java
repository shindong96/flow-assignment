package com.flow.assignment;

import com.flow.assignment.dto.request.CreatingAccessRuleRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AccessRuleService {

    public Long save(final CreatingAccessRuleRequest accessRuleRequest, final String timeZone) {
        return 1L;
    }
}
