package com.flow.assignment.support;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.zone.ZoneRulesException;

public class TimeZoneConverter {

    public static LocalDateTime convert(final LocalDateTime dateTime, final String fromZone, final String toZone) {
        if (dateTime == null) {
            return dateTime;
        }

        try {
            ZoneId fromZoneId = ZoneId.of(fromZone);
            ZoneId toZoneId = ZoneId.of(toZone);

            return dateTime.atZone(fromZoneId)
                    .withZoneSameInstant(toZoneId)
                    .toLocalDateTime();
        } catch (ZoneRulesException e) {
            throw new IllegalArgumentException(ErrorCode.TIME_ZONE_RULE);
        }
    }
}
