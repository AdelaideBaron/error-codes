package com.wes.error_codes.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Machine {
    MACHINE_1("data/machine_1_error_codes.csv"),
    MACHINE_2("data/machine_2_error_codes.csv");

    private final String filePath;
}
