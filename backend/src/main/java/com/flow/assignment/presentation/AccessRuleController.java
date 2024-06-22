package com.flow.assignment.presentation;

import com.flow.assignment.application.AccessRuleService;
import com.flow.assignment.dto.request.CreatingAccessRuleRequest;
import com.flow.assignment.dto.request.PagingRequest;
import com.flow.assignment.dto.response.IpAccessRuleResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping
    public ResponseEntity<IpAccessRuleResponses> findAll(@ModelAttribute @Valid final PagingRequest pagingRequest,
                                                         final HttpServletRequest request) {
        IpAccessRuleResponses responses = accessRuleService.findAll(pagingRequest, request.getHeader("Time-Zone"));
        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable final Long id) {
        accessRuleService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
