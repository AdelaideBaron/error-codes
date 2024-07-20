package com.wes.error_codes;

import com.wes.error_codes.model.ErrorCodes;
import com.wes.error_codes.model.MachineErrorCode;
import java.util.List;

public class TestData {

  public final String TEST_ERROR_CODES_FILE_PATH =
      "src/test/resources/data/machine_error_codes.csv";

  private final MachineErrorCode machineErrorCodeRow1 =
      new MachineErrorCode("ERROR_ABC", "error_1_details", List.of("some_cause"), "MACHINE_1");
  private final MachineErrorCode machineErrorCodeRow2 =
      new MachineErrorCode(
          "ERROR_XYZ",
          "error_2_details",
          List.of("another_cause", "another_cause_two", "another_cause_three"),
          "MACHINE_2");
  private final MachineErrorCode machineErrorCodeRow3 =
      new MachineErrorCode(
          "SOME_ERROR_CODE",
          "error_3_details",
          List.of("another_cause_one", "another_cause_four", "another_cause_five"),
          "MACHINE_1");

  public final ErrorCodes TEST_ERROR_CODES =
      new ErrorCodes(List.of(machineErrorCodeRow1, machineErrorCodeRow2, machineErrorCodeRow3));
}
