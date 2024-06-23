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

    @ValidIpAddress(message = "IP_ADDRESS_001||IPv4나 IPv6 형식에 맞게 입력하여야 합니다.")
    @NotBlank(message = "IP_ADDRESS_002||IP 주소는 공백일 수 없습니다.")
    private String ipAddress;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private String content;
}
