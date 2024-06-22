package com.flow.assignment.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class AccessRuleTest {

    @DisplayName("원하는 시간대를 받아 시작,끝 시간을 변환해준다.")
    @ParameterizedTest
    @CsvSource(value = {"America/New_York,4,30,23", "Europe/London,5,1,4"})
    void convertTimeByTimeZone(String timeZone, int month, int date, int hour) {
        // given
        AccessRule accessRule = AccessRule.builder()
                .startTime(LocalDateTime.of(LocalDate.of(2023, 5, 1), LocalTime.NOON))
                .endTime(LocalDateTime.of(LocalDate.of(2023, 5, 1), LocalTime.NOON))
                .timeZone("Asia/Seoul")
                .build();

        // when
        AccessRule convertedAccessRule = accessRule.convertTimeZone(timeZone);

        // then
        assertThat(convertedAccessRule.getStartTime())
                .isEqualTo(LocalDateTime.of(LocalDate.of(2023, month, date), LocalTime.of(hour, 0, 0)));
    }
}
