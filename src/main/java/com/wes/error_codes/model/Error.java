package com.wes.error_codes.model;

import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class Error {
  private final String errorCode;
  private final List<PossibleCause> possibleCauses;
}
