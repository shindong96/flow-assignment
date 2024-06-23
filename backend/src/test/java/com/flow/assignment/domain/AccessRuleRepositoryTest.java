package com.flow.assignment.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

@DataJpaTest
class AccessRuleRepositoryTest {

    @Autowired
    private AccessRuleRepository repository;

    @DisplayName("content 포함 여부로 조회를 검증한다.")
    @ParameterizedTest
    @CsvSource(value = {"구글,false,1", "카카오,false,2", "네이버,true,2"})
    void findByContentContaining(String content, boolean hasNext, int size) {
        // given
        saveWithContent("구글 IP");
        saveWithContent("카카오엔터 IP");
        saveWithContent("카카오톡 IP");
        saveWithContent("네이버 IP");
        saveWithContent("네이버파이낸셜 IP");
        saveWithContent("네이버웹툰 IP");
        PageRequest pageRequest = PageRequest.of(0, 2, Sort.by(Direction.DESC, "id"));

        // when
        Slice<AccessRule> results = repository.findByContentContaining(content, pageRequest);

        // then
        assertAll(
                () -> assertThat(results.hasNext()).isEqualTo(hasNext),
                () -> assertThat(results.getContent()).hasSize(size)
        );
    }

    @DisplayName("혀용 시간으로 조회를 검증한다.")
    @Test
    void findByPermissionTime() {
        // given
        LocalDateTime permissionStartTime = LocalDateTime.of(LocalDate.of(2022, 5, 6), LocalTime.NOON);
        LocalDateTime permissionEndTime = LocalDateTime.of(LocalDate.of(2022, 5, 6), LocalTime.MIDNIGHT);

        Long success1 = saveWithTimes(permissionStartTime.minusHours(1), permissionEndTime);
        Long success2 = saveWithTimes(permissionStartTime, permissionEndTime.plusHours(1));
        saveWithTimes(permissionStartTime.plusDays(1), permissionEndTime);
        Long success3 = saveWithTimes(permissionStartTime.minusHours(1), permissionEndTime.plusHours(2));
        PageRequest pageRequest = PageRequest.of(0, 2, Sort.by(Direction.DESC, "id"));

        // when
        Slice<AccessRule> results = repository
                .findByPermissionTime(permissionStartTime, permissionEndTime, pageRequest);

        // then
        assertAll(
                () -> assertThat(results.hasNext()).isEqualTo(true),
                () -> assertThat(results.getContent()).extracting("id")
                        .containsExactly(success3, success2)
        );
    }

    private Long saveWithTimes(final LocalDateTime start, final LocalDateTime end) {
        AccessRule accessRule = repository.save(AccessRule.builder()
                .startTime(start)
                .endTime(end)
                .build());
        return accessRule.getId();
    }

    private void saveWithContent(final String content) {
        repository.save(AccessRule.builder()
                .content(content)
                .build());
    }
}
