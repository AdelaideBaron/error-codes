package com.wes.error_codes;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import java.util.List;
@RequiredArgsConstructor
@Getter
public class PossibleCauses {
    private final List<PossibleCause> possibleCauseList;
}
