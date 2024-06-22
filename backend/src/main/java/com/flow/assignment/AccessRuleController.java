package com.flow.assignment;

import com.flow.assignment.dto.request.CreatingAccessRuleRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/access-rules")
public class AccessRuleController {

    private final AccessRuleService accessRuleService;

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody @Valid final CreatingAccessRuleRequest accessRuleRequest, final
    HttpServletRequest request) {
        Long savedId = accessRuleService.save(accessRuleRequest, request.getHeader("Time-Zone"));
        return ResponseEntity.created(URI.create("/access-rules/" + savedId)).build();
    }
}
