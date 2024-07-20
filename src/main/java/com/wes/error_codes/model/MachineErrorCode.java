package com.wes.error_codes.model;

import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MachineErrorCode {
  private final String errorCode;
  private final String errorDetails;
  private final List<String> possibleCauses;
  private final String machine;
}
