package com.flow.assignment.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "access_rule")
@NoArgsConstructor
@Getter
public class AccessRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String IpAddress;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private String content;
    
    private String timeZone;

    @Builder
    public AccessRule(final String ipAddress, final LocalDateTime startTime, final LocalDateTime endTime,
                      final String timeZone, final String content) {
        IpAddress = ipAddress;
        this.startTime = startTime;
        this.endTime = endTime;
        this.content = content;
        this.timeZone = timeZone;
    }
}
