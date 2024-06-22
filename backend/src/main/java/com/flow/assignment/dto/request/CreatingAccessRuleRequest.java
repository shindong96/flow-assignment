package com.flow.assignment.dto.request;

import com.flow.assignment.dto.request.validation.ValidIpAddress;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CreatingAccessRuleRequest {

    @ValidIpAddress
    @NotBlank
    private String ipAddress;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private String content;
}
