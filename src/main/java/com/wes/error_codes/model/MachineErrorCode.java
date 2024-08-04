package com.wes.error_codes.model;

import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class MachineErrorCode {
  private final String errorCode;
  private final StringBuilder  errorDetails;
  private final List<String> possibleCauses;
  private final String machine;
  private final List<String> correctiveActions;

  public void appendErrorDetails(String details) {
    if (!details.isEmpty()) {
      this.errorDetails.append(" & ").append(details.trim());
    }
  }
}
