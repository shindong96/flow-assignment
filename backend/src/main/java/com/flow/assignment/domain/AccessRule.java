package com.flow.assignment.domain;

import com.flow.assignment.support.TimeZoneConverter;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
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

    public AccessRule convertTimeZone(final String toZone) {
        LocalDateTime convertedStartTime = TimeZoneConverter.convert(startTime, timeZone, toZone);
        LocalDateTime convertedEndTime = TimeZoneConverter.convert(endTime, timeZone, toZone);

        return AccessRule.builder()
                .id(this.id)
                .ipAddress(this.ipAddress)
                .content(this.content)
                .startTime(convertedStartTime)
                .endTime(convertedEndTime)
                .timeZone(toZone)
                .build();
    }
}
