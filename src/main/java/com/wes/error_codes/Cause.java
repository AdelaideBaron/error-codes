package com.wes.error_codes;

import lombok.Getter;

@Getter
public enum Cause {
  CAUSE_1("Cause 1 description", "Solution"),
  CAUSE_2("Cause 2 description", "Solution"),
  CAUSE_3("Cause 3 description", "Solution"),
  CAUSE_4("Cause 4 description", "Solution"),
  CAUSE_5("Cause 5 description", "Solution");

  @Getter private final String description;
  private final String solution;

  Cause(String description, String solution) {
    this.description = description;
    this.solution = solution;
  }

  public static Cause fromString(String cause) {
    for (Cause errorCause : Cause.values()) {
      if (errorCause.name().equalsIgnoreCase(cause)) {
        return errorCause;
      }
    }
    throw new IllegalArgumentException("No constant with the specified code: " + cause);
  }
}
