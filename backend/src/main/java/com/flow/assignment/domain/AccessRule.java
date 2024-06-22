package com.flow.assignment.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.time.ZoneId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "access_rule")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class AccessRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ipAddress;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private String content;

    private String timeZone;

    public AccessRule convertTimeZone(final String timeZone) {
        ZoneId fromZoneId = ZoneId.of(this.timeZone);
        ZoneId toZoneId = ZoneId.of(timeZone);

        LocalDateTime convertedStartTime = startTime.atZone(fromZoneId)
                .withZoneSameInstant(toZoneId)
                .toLocalDateTime();
        LocalDateTime convertedEndTime = endTime.atZone(fromZoneId)
                .withZoneSameInstant(toZoneId)
                .toLocalDateTime();

        return AccessRule.builder()
                .id(this.id)
                .ipAddress(this.ipAddress)
                .content(this.content)
                .startTime(convertedStartTime)
                .endTime(convertedEndTime)
                .build();
    }
}
