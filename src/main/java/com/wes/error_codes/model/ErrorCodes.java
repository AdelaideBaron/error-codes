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

//  public Map<String, List<String>> getAllErrorCodesWithCauses() {
//    Map<String, List<String>> errorToCausesMap = new HashMap<>();
//
//    for (MachineErrorCode machineErrorCode : machineErrorCodes) {
//      errorToCausesMap.put(machineErrorCode.getErrorCode(), machineErrorCode.getPossibleCauses());
//    }
//
//    return errorToCausesMap;
//  }

  public Map<String, List<Map.Entry<String, String>>> getAllErrorCodesWithCausesAndActions() {
    Map<String, List<Map.Entry<String, String>>> errorToCausesAndActionsMap = new HashMap<>();

    for (MachineErrorCode machineErrorCode : machineErrorCodes) {
      List<String> causes = machineErrorCode.getPossibleCauses();
      List<String> actions = machineErrorCode.getCorrectiveActions();

      List<Map.Entry<String, String>> causeActionPairs = new ArrayList<>();
      for (int i = 0; i < causes.size(); i++) {
        String cause = causes.get(i);
        String action = (i < actions.size()) ? actions.get(i) : "";
        causeActionPairs.add(new AbstractMap.SimpleEntry<>(cause, action));
      }

      errorToCausesAndActionsMap.put(machineErrorCode.getErrorCode(), causeActionPairs);
    }

    return errorToCausesAndActionsMap;
  }

  public Map<String, String> getDetailsForAllErrorCodes() {
    Map<String, String> errorToDetailsMap = new HashMap<>();

    for (MachineErrorCode machineErrorCode : machineErrorCodes) {
      // check if the error code already exists, if so - append to
      String errorCode = machineErrorCode.getErrorCode();

      if(errorToDetailsMap.containsKey(errorCode)){
        String currentDetails = errorToDetailsMap.get(errorCode);
        errorToDetailsMap.replace(errorCode, currentDetails + ", " + machineErrorCode.getErrorDetails());
      }
      errorToDetailsMap.put(machineErrorCode.getErrorCode(), String.valueOf(machineErrorCode.getErrorDetails()));
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
