package com.flow.assignment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.flow.assignment.domain.AccessRule;
import com.flow.assignment.domain.AccessRuleRepository;
import com.flow.assignment.dto.request.PagingRequest;
import com.flow.assignment.dto.response.IpAccessRuleResponses;
import com.flow.assignment.support.DatabaseCleanUp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AccessRuleServiceTest {

    @Autowired
    private AccessRuleService accessRuleService;

    @Autowired
    private AccessRuleRepository accessRuleRepository;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @BeforeEach
    void setUp() {
        databaseCleanUp.execute();
    }

    @DisplayName("전체 조회 시 페이징 조건에 맞게 넘어가는지 테스트한다.")
    @Test
    void findAll() {
        // given
        AccessRule accessRule1 = AccessRule.builder()
                .startTime(LocalDateTime.of(LocalDate.of(2023, 5, 1), LocalTime.NOON))
                .endTime(LocalDateTime.of(LocalDate.of(2023, 5, 1), LocalTime.NOON))
                .timeZone("Asia/Seoul")
                .build();
        AccessRule accessRule2 = AccessRule.builder()
                .startTime(LocalDateTime.of(LocalDate.of(2023, 5, 1), LocalTime.NOON))
                .endTime(LocalDateTime.of(LocalDate.of(2023, 5, 1), LocalTime.NOON))
                .timeZone("Asia/Seoul")
                .build();
        AccessRule accessRule3 = AccessRule.builder()
                .startTime(LocalDateTime.of(LocalDate.of(2023, 5, 1), LocalTime.NOON))
                .endTime(LocalDateTime.of(LocalDate.of(2023, 5, 1), LocalTime.NOON))
                .timeZone("Asia/Seoul")
                .build();

        accessRuleRepository.save(accessRule1);
        accessRuleRepository.save(accessRule2);
        accessRuleRepository.save(accessRule3);

        // when
        IpAccessRuleResponses responses = accessRuleService.findAll(new PagingRequest(1, 2), "Asia/Seoul");

        // then
        assertAll(
                () -> assertThat(responses.getHasNext()).isTrue(),
                () -> assertThat(responses.getAccessRules()).extracting("id")
                        .containsExactly(3L, 2L)
                        .doesNotContain(1L)
        );
    }
}
