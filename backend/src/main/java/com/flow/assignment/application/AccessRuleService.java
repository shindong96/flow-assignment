package com.flow.assignment.application;

import com.flow.assignment.domain.AccessRule;
import com.flow.assignment.domain.AccessRuleRepository;
import com.flow.assignment.dto.request.CreatingAccessRuleRequest;
import com.flow.assignment.dto.request.PagingRequest;
import com.flow.assignment.dto.request.PermissionTimeRequest;
import com.flow.assignment.dto.response.IpAccessRuleResponses;
import com.flow.assignment.support.ErrorCode;
import com.flow.assignment.support.TimeZoneConverter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AccessRuleService {

    private static final int DIFFERENCES_PAGES_AND_DB_INDEX = 1;
    private static final Sort SORT_DIRECTION_DESC_BY_ID = Sort.by(Direction.DESC, "id");
    private static final String GMT_TIME_ZONE = "GMT";
    private static final LocalDateTime MAX_DATE = LocalDateTime.of(LocalDate.of(9999, 12, 31),
            LocalTime.of(23, 59));
    private static final LocalDateTime MIN_DATE = LocalDateTime.of(LocalDate.of(0, 1, 1),
            LocalTime.of(0, 0));

    private final AccessRuleRepository accessRuleRepository;

    public Long save(final CreatingAccessRuleRequest request, final String timeZone) {
        checkTimeZone(timeZone);
        AccessRule accessRule = AccessRule.builder()
                .ipAddress(request.getIpAddress())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .content(request.getContent())
                .timeZone(timeZone)
                .build();

        AccessRule savedAccessRule = accessRuleRepository.save(
                accessRule.convertTimeZone(GMT_TIME_ZONE));
        return savedAccessRule.getId();
    }

    @Transactional(readOnly = true)
    public IpAccessRuleResponses findAll(final PagingRequest pagingRequest, final String timeZone) {
        checkTimeZone(timeZone);
        Page<AccessRule> rules = accessRuleRepository.findAll(
                PageRequest.of(pagingRequest.getPage() - DIFFERENCES_PAGES_AND_DB_INDEX, pagingRequest.getSize()
                        , SORT_DIRECTION_DESC_BY_ID));

        List<AccessRule> convertedTimeRules = convertTimeZone(rules.getContent(), timeZone);
        return IpAccessRuleResponses.of(rules.getTotalElements(), convertedTimeRules);
    }

    @Transactional(readOnly = true)
    public IpAccessRuleResponses findByContentContaining(final PagingRequest pagingRequest, final String content,
                                                         final String timeZone) {
        checkTimeZone(timeZone);
        if (content == null) {
            return findAll(pagingRequest, timeZone);
        }

        Page<AccessRule> rules = accessRuleRepository.findByContentContaining(content,
                PageRequest.of(pagingRequest.getPage() - DIFFERENCES_PAGES_AND_DB_INDEX, pagingRequest.getSize()
                        , SORT_DIRECTION_DESC_BY_ID));

        List<AccessRule> convertedTimeRules = convertTimeZone(rules.getContent(), timeZone);
        return IpAccessRuleResponses.of(rules.getTotalElements(), convertedTimeRules);
    }

    @Transactional(readOnly = true)
    public IpAccessRuleResponses findByPermissionTime(final PermissionTimeRequest request, final String timeZone) {
        checkTimeZone(timeZone);

        LocalDateTime startTime = request.getStartTime();
        LocalDateTime endTime = request.getEndTime();
        validateStartIsGreaterThanEqualEnd(startTime, endTime);

        LocalDateTime convertedStartTime = TimeZoneConverter.convert(startTime, timeZone, GMT_TIME_ZONE);
        LocalDateTime convertedEndTime = TimeZoneConverter.convert(endTime, timeZone, GMT_TIME_ZONE);

        Page<AccessRule> rules = accessRuleRepository.findByPermissionTime(
                convertedStartTime == null ? MIN_DATE : convertedStartTime,
                convertedEndTime == null ? MAX_DATE : convertedEndTime,
                PageRequest.of(request.getPage() - DIFFERENCES_PAGES_AND_DB_INDEX, request.getSize()
                        , SORT_DIRECTION_DESC_BY_ID));

        List<AccessRule> convertedTimeRules = convertTimeZone(rules.getContent(), timeZone);
        return IpAccessRuleResponses.of(rules.getTotalElements(), convertedTimeRules);
    }

    public void deleteById(final Long id) {
        accessRuleRepository.deleteById(id);
    }

    private void checkTimeZone(final String timeZone) {
        if (timeZone == null) {
            throw new IllegalArgumentException(ErrorCode.TIME_ZONE_NOT_NULL);
        }
    }

    private List<AccessRule> convertTimeZone(final List<AccessRule> rules, final String timeZone) {
        return rules.stream()
                .map(accessRule -> accessRule.convertTimeZone(timeZone))
                .collect(Collectors.toList());
    }

    private void validateStartIsGreaterThanEqualEnd(final LocalDateTime start, final LocalDateTime end) {
        if (start == null || end == null) {
            return;
        }

        if (start.isAfter(end)) {
            throw new IllegalArgumentException(ErrorCode.PERMISSION_TIME_RULE);
        }
    }
}
