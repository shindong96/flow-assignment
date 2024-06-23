package com.flow.assignment.support;

import com.flow.assignment.domain.AccessRuleRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class DataSupport {

    private final AccessRuleRepository accessRepository;

    public DataSupport(final AccessRuleRepository accessRepository) {
        this.accessRepository = accessRepository;
    }
    
}
