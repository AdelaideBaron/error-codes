package com.wes.error_codes.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Machine {
  MACHINE_1("src/main/resources/data/ignore_this_file_1.csv"),
  MACHINE_2("src/main/resources/data/ignore_this_file_2.csv");

  private final String filePath;
}
