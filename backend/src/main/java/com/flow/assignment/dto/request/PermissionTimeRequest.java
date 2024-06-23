package com.flow.assignment.dto.request;

import com.flow.assignment.support.Patterns;
import jakarta.validation.constraints.Min;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

@AllArgsConstructor
@Getter
public class PermissionTimeRequest {

    @DateTimeFormat(pattern = Patterns.DATE_TIME_FORMAT)
    private final LocalDateTime startTime;
    @DateTimeFormat(pattern = Patterns.DATE_TIME_FORMAT)
    private final LocalDateTime endTime;
    @Min(value = 1, message = "PAGINATION_001||page 는 1 이상이어야 합니다.")
    private final int page;
    @Min(value = 1, message = "PAGINATION_002||size 는 1 이상이어야 합니다.")
    private final int size;
}
