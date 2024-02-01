package com.wes.error_codes.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import java.util.List;

@RequiredArgsConstructor
@Getter
public class Error {
    private final String errorCode;
    private final List<PossibleCause> possibleCauses;
}
