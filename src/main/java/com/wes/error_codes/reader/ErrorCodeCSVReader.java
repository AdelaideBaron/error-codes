package com.wes.error_codes.reader;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.wes.error_codes.model.ErrorCodes;
import com.wes.error_codes.model.MachineErrorCode;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.wes.error_codes.model.ErrorCodes;
import com.wes.error_codes.model.MachineErrorCode;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ErrorCodeCSVReader {

  @Value("${errorCode.filePath}")
  private String MACHINE_ERROR_CODES;

  private final String ERROR_CODE_COL_HEAD = "Error Code";
  private final String ERROR_DETAILS_COL_HEAD = "Error Details";
  private final String POSSIBLE_CAUSES_COL_HEAD = "Possible Causes";
  private final String MACHINE_COL_HEAD = "Machine";
  private final String CORRECTIVE_ACTION_COL_HEAD = "Corrective Action";

  public Set<String> getMachinesFromCsv() {
    return mapCsvToMachineErrorCodes().getAllUniqueMachines();
  }

  public Map<String, List<Map.Entry<String, String>>> getErrorCodeAndCausesAndActionsFromCsv() {
    return mapCsvToMachineErrorCodes().getAllErrorCodesWithCausesAndActions();
  }

  public Map<String, List<String>> getMachinesWithErrorCodes() {
    return mapCsvToMachineErrorCodes().getAllErrorCodesForEachMachine();
  }

  public Map<String, String> getErrorsWithDetails() {
    return mapCsvToMachineErrorCodes().getDetailsForAllErrorCodes();
  }

  private ErrorCodes mapCsvToMachineErrorCodes() {
    Map<String, MachineErrorCode> errorCodeMap = new HashMap<>();

    try (CSVReader reader = new CSVReader(new FileReader(MACHINE_ERROR_CODES))) {
      List<String[]> rows = reader.readAll();

      if (rows.isEmpty()) {
        return new ErrorCodes(new ArrayList<>(errorCodeMap.values()));
      }

      String[] headers = rows.get(0);
      int errorCodeIndex = findColumnIndex(headers, ERROR_CODE_COL_HEAD);
      int errorDetailsIndex = findColumnIndex(headers, ERROR_DETAILS_COL_HEAD);
      int possibleCausesIndex = findColumnIndex(headers, POSSIBLE_CAUSES_COL_HEAD);
      int machineIndex = findColumnIndex(headers, MACHINE_COL_HEAD);
      int correctiveActionIndex = findColumnIndex(headers, CORRECTIVE_ACTION_COL_HEAD);

      String previousErrorCode = "";
      for (int i = 1; i < rows.size(); i++) {
        String[] row = rows.get(i);

        if (row.length < headers.length || Arrays.stream(row).allMatch(String::isEmpty)) {
          continue;
        }

        String errorCode = getValueAtIndex(row, errorCodeIndex);
        String errorDetails = getValueAtIndex(row, errorDetailsIndex);
        String possibleCause = getValueAtIndex(row, possibleCausesIndex);
        String machine = getValueAtIndex(row, machineIndex);
        String correctiveAction = getValueAtIndex(row, correctiveActionIndex);

        if (errorCode.isEmpty()) {
          continue;
        }

        MachineErrorCode machineErrorCode;
        if (errorCode.equals(previousErrorCode)) {
          machineErrorCode = errorCodeMap.get(errorCode);
          machineErrorCode. appendErrorDetails(errorDetails);
        } else {
          machineErrorCode = new MachineErrorCode(
                  errorCode, new StringBuilder(errorDetails), new ArrayList<>(), machine, new ArrayList<>()
          );
          errorCodeMap.put(errorCode, machineErrorCode);
          previousErrorCode = errorCode;
        }

        if (!possibleCause.isEmpty() && !machineErrorCode.getPossibleCauses().contains(possibleCause)) {
          machineErrorCode.getPossibleCauses().add(possibleCause);
        }

        if (!correctiveAction.isEmpty() && !machineErrorCode.getCorrectiveActions().contains(correctiveAction)) {
          machineErrorCode.getCorrectiveActions().add(correctiveAction);
        }
      }

    } catch (IOException | CsvException e) {
      e.printStackTrace();
    }

    return new ErrorCodes(new ArrayList<>(errorCodeMap.values()));
  }




  private int findColumnIndex(String[] headers, String columnName) {
    for (int i = 0; i < headers.length; i++) {
      if (columnName.equalsIgnoreCase(headers[i].trim())) {
        return i;
      }
    }
    throw new IllegalArgumentException("Column not found: " + columnName);
  }

  private String getValueAtIndex(String[] row, int index) {
    if (index >= 0 && index < row.length) {
      return row[index].trim();
    }
    return "";
  }
}
