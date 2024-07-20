package com.wes.error_codes.model;

import java.util.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ErrorCodes {
  private final List<MachineErrorCode> machineErrorCodes;

  public Set<String> getAllUniqueMachines() {
    Set<String> uniqueEntries = new HashSet<>();
    for (MachineErrorCode machineErrorCode : machineErrorCodes) {
      uniqueEntries.add(machineErrorCode.getMachine());
    }
    return uniqueEntries;
  }

  public Map<String, List<String>> getAllErrorCodesWithCauses() {
    Map<String, List<String>> errorToCausesMap = new HashMap<>();

    for (MachineErrorCode machineErrorCode : machineErrorCodes) {
      errorToCausesMap.put(machineErrorCode.getErrorCode(), machineErrorCode.getPossibleCauses());
    }

    return errorToCausesMap;
  }

  public Map<String, String> getDetailsForAllErrorCodes() {
    Map<String, String> errorToDetailsMap = new HashMap<>();

    for (MachineErrorCode machineErrorCode : machineErrorCodes) {
      errorToDetailsMap.put(machineErrorCode.getErrorCode(), machineErrorCode.getErrorDetails());
    }

    return errorToDetailsMap;
  }

  public Map<String, List<String>> getAllErrorCodesForEachMachine() {
    Map<String, List<String>> machineToErrorsMap = new HashMap<>();

    Map<String, String> errorCodeMachines = getErrorCodesAndTheirMachines();

    for (String machine : getAllUniqueMachines()) {
      List<String> errorsForMachine =
          errorCodeMachines.entrySet().stream()
              .filter(entry -> machine.equals(entry.getValue()))
              .map(Map.Entry::getKey)
              .toList();
      machineToErrorsMap.put(machine, errorsForMachine);
    }

    return machineToErrorsMap;
  }

  private Map<String, String> getErrorCodesAndTheirMachines() {
    Map<String, String> errorCodeMachines = new HashMap<>();

    for (MachineErrorCode machineErrorCode : machineErrorCodes) {
      errorCodeMachines.put(machineErrorCode.getErrorCode(), machineErrorCode.getMachine());
    }
    return errorCodeMachines;
  }
}
