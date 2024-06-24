package com.flow.assignment.presentation;

import com.flow.assignment.application.AccessRuleService;
import com.flow.assignment.dto.request.CreatingAccessRuleRequest;
import com.flow.assignment.dto.request.PagingRequest;
import com.flow.assignment.dto.request.PermissionTimeRequest;
import com.flow.assignment.dto.response.IpAccessRuleResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/access-rules")
@CrossOrigin
public class AccessRuleController {

    private final AccessRuleService accessRuleService;
    private final String timeZone;

    public AccessRuleController(final AccessRuleService accessRuleService,
                                @Value("${http.header.time-zone}") final String timeZone) {
        this.accessRuleService = accessRuleService;
        this.timeZone = timeZone;
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody @Valid final CreatingAccessRuleRequest accessRuleRequest, final
    HttpServletRequest request) {
        Long savedId = accessRuleService.save(accessRuleRequest, request.getHeader(timeZone));
        return ResponseEntity.created(URI.create("/access-rules/" + savedId)).build();
    }

    @GetMapping
    public ResponseEntity<IpAccessRuleResponses> findAll(@ModelAttribute @Valid final PagingRequest pagingRequest,
                                                         final HttpServletRequest request) {
        IpAccessRuleResponses responses = accessRuleService.findAll(pagingRequest, request.getHeader(timeZone));
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/content")
    public ResponseEntity<IpAccessRuleResponses> findByContentContaining(
            @ModelAttribute @Valid final PagingRequest pagingRequest,
            @RequestParam(required = false) final String inclusion,
            final HttpServletRequest request) {
        IpAccessRuleResponses responses = accessRuleService.findByContentContaining(pagingRequest, inclusion,
                request.getHeader(timeZone));
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/permission")
    public ResponseEntity<IpAccessRuleResponses> findByPermissionTime(
            @ModelAttribute @Valid final PermissionTimeRequest permissionTimeRequest,
            final HttpServletRequest request) {
        IpAccessRuleResponses responses = accessRuleService
                .findByPermissionTime(permissionTimeRequest, request.getHeader(timeZone));
        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable final Long id) {
        accessRuleService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
