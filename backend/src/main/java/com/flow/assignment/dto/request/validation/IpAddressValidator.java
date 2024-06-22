package com.flow.assignment.dto.request.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class IpAddressValidator implements ConstraintValidator<ValidIpAddress, String> {

    private static final Pattern IPV4_PATTERN = Pattern.compile(
            "^(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9]?[0-9])\\." +
                    "(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9]?[0-9])\\." +
                    "(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9]?[0-9])\\." +
                    "(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9]?[0-9])$"
    );

    private static final Pattern IPV6_PATTERN = Pattern.compile(
            "^(?:[0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}$|" +
                    "^((?:[0-9A-Fa-f]{1,4}:){1,7}:|:(:[0-9A-Fa-f]{1,4}){1,7})$|" +
                    "^(?:[0-9A-Fa-f]{1,4}:){1,6}:(?:[0-9A-Fa-f]{1,4})?$|" +
                    "^(?:[0-9A-Fa-f]{1,4}:){1,5}:(?::[0-9A-Fa-f]{1,4}){1,2}$|" +
                    "^(?:[0-9A-Fa-f]{1,4}:){1,4}:(?::[0-9A-Fa-f]{1,4}){1,3}$|" +
                    "^(?:[0-9A-Fa-f]{1,4}:){1,3}:(?::[0-9A-Fa-f]{1,4}){1,4}$|" +
                    "^(?:[0-9A-Fa-f]{1,4}:){1,2}:(?::[0-9A-Fa-f]{1,4}){1,5}$|" +
                    "^[0-9A-Fa-f]{1,4}:(?::[0-9A-Fa-f]{1,4}){1,6}$|" +
                    "^:(?::[0-9A-Fa-f]{1,4}){1,7}$"
    );

    @Override
    public void initialize(ValidIpAddress constraintAnnotation) {
    }

    @Override
    public boolean isValid(String ip, ConstraintValidatorContext context) {
        if (ip == null) {
            return true; // null 값은 여기서 처리하지 않음
        }

        return IPV4_PATTERN.matcher(ip).matches() || IPV6_PATTERN.matcher(ip).matches();
    }
}

