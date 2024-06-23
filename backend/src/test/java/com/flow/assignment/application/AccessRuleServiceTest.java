package com.flow.assignment.application;

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
        for (int i = 0; i < 3; i++) {
            saveAccessRule();
        }

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

    @DisplayName("내용 포함 여부를 이용한 조회를 테스트한다. ")
    @Test
    void findByContentContaining() {
        // given
        String content = "카카오";
        saveWithContent("구글 IP");
        saveWithContent(content + "IP");
        saveWithContent(content + "IP2");
        saveWithContent(content + "IP3");
        PagingRequest pagingRequest = new PagingRequest(1, 2);

        // when
        IpAccessRuleResponses responses = accessRuleService
                .findByContentContaining(pagingRequest, content, "Asia/Seoul");

        // then
        assertAll(
                () -> assertThat(responses.getHasNext()).isTrue(),
                () -> assertThat(responses.getAccessRules()).extracting("id")
                        .containsExactly(4L, 3L)
        );
    }

    @DisplayName("내용 포함 여부 조회에서 내용이 null일 경우 테스트한다. ")
    @Test
    void findByContentContaining_withContentNull() {
        // given
        String kakaoIp = "카카오 IP";
        String googleIp = "구글 IP";
        saveWithContent(googleIp);
        saveWithContent(kakaoIp);
        saveWithContent(kakaoIp + "2");
        saveWithContent(kakaoIp + "3");
        PagingRequest pagingRequest = new PagingRequest(2, 2);

        // when
        IpAccessRuleResponses responses = accessRuleService
                .findByContentContaining(pagingRequest, null, "Asia/Seoul");

        // then
        assertAll(
                () -> assertThat(responses.getHasNext()).isFalse(),
                () -> assertThat(responses.getAccessRules()).extracting("content")
                        .containsExactly(kakaoIp, googleIp)
        );
    }

    private void saveAccessRule() {
        accessRuleRepository.save(AccessRule.builder()
                .startTime(LocalDateTime.of(LocalDate.of(2023, 5, 1), LocalTime.NOON))
                .endTime(LocalDateTime.of(LocalDate.of(2023, 5, 1), LocalTime.NOON))
                .timeZone("Asia/Seoul")
                .build());
    }

    private void saveWithContent(final String content) {
        accessRuleRepository.save(AccessRule.builder()
                .startTime(LocalDateTime.of(LocalDate.of(2023, 5, 1), LocalTime.NOON))
                .endTime(LocalDateTime.of(LocalDate.of(2023, 5, 1), LocalTime.NOON))
                .timeZone("Asia/Seoul")
                .content(content)
                .build());
    }
}
