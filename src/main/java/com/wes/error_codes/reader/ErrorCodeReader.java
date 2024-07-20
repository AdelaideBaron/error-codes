package com.wes.error_codes.reader;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import com.wes.error_codes.model.MachineErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class ErrorCodeReader { // obvs rename
  private final String MACHINE_ERROR_CODES = "src/main/resources/data/machine_error_codes.csv";

  private final String ERROR_CODE_COL_HEAD = "Error Code";

  private final String ERROR_DETAILS_COL_HEAD = "Error Details";

  private final String POSSIBLE_CAUSES_COL_HEAD = "Possible Causes";

  private final String MACHINE_COL_HEAD = "Machine";

  @Bean
  public Set<String> getMachinesFromCsv() {
    Set<String> uniqueEntries = new HashSet<>();

    try (CSVReader reader = new CSVReader(new FileReader(MACHINE_ERROR_CODES))) {
      List<String[]> rows = reader.readAll();

      // Skip the header row (first row)
      for (int i = 1; i < rows.size(); i++) {
        String[] row = rows.get(i);
        if (row.length > 2 && row[3] != null && !row[3].isEmpty()) {
          uniqueEntries.add(row[3]);
        }
      }

    } catch (IOException | CsvException e) {
      e.printStackTrace();
    }

    return uniqueEntries;
  }

  @Bean
  @Qualifier("errorCodesAndCauses")
  public Map<String, List<String>> getErrorCodeAndCausesFromCsv() {
    Map<String, List<String>> errorToCausesMap = new HashMap<>();

    try (CSVReader reader = new CSVReader(new FileReader(MACHINE_ERROR_CODES))) {
      List<String[]> rows = reader.readAll();

      String currentError = null;
      // Skip the first line (header)
      for (int i = 1; i < rows.size(); i++) {
        String[] row = rows.get(i);
        if (row.length > 1) {
          String error = row[0].trim();
          String cause = row[2].trim();

          if (!error.isEmpty()) {
            currentError = error;
            errorToCausesMap.putIfAbsent(currentError, new ArrayList<>());
          }

          if (currentError != null && !cause.isEmpty()) {
            errorToCausesMap.get(currentError).add(cause);
          }
        }
      }
    } catch (IOException | CsvException e) {
      e.printStackTrace();
    }
    return errorToCausesMap;
  }

  @Bean
  @Qualifier("machinesAndErrorCodes")
  public Map<String, List<String>> getMachinesWithErrorCodes() {
    Map<String, List<String>> machineToErrorsMap = new HashMap<>();

    try (CSVReader reader = new CSVReader(new FileReader(MACHINE_ERROR_CODES))) {
      List<String[]> rows = reader.readAll();

      // Skip the header row (first row)
      for (int i = 1; i < rows.size(); i++) {
        String[] row = rows.get(i);
        if (row.length > 3) {
          String error = row[0].trim();
          String machine = row[3].trim();

          if (!error.isEmpty() && !machine.isEmpty()) {
            machineToErrorsMap.putIfAbsent(machine, new ArrayList<>());
            machineToErrorsMap.get(machine).add(error);
          }
        }
      }

    } catch (IOException | CsvException e) {
      e.printStackTrace();
    }

    log.info("Machine to Errors Map: {}", machineToErrorsMap);
    return machineToErrorsMap;
  }

  @Bean
  public Map<String, String> getErrorsWithDetails() {
    Map<String, String> errorToDetailsMap = new HashMap<>();

    try (CSVReader reader = new CSVReader(new FileReader(MACHINE_ERROR_CODES))) {
      List<String[]> rows = reader.readAll();

      for (int i = 1; i < rows.size(); i++) {
        String[] row = rows.get(i);
        if (row.length > 3) {
          String error = row[0].trim();
          String errorDetails = row[1].trim();

          if (!error.isEmpty() && !errorDetails.isEmpty()) {
            errorToDetailsMap.put(error, errorDetails);
          }
        }
      }
    } catch (IOException | CsvException e) {
      e.printStackTrace();
    }
    log.info("ADDI " + errorToDetailsMap);
    return errorToDetailsMap;
  }

  public List<MachineErrorCode> mapCsvToMachineErrorCodes() {
    List<MachineErrorCode> machineErrorCodes = new ArrayList<>();

    try (CSVReader reader = new CSVReader(new FileReader(MACHINE_ERROR_CODES))) {
      List<String[]> rows = reader.readAll();

      if (rows.isEmpty()) {
        return machineErrorCodes;
      }

      String[] headers = rows.get(0);
      int errorCodeIndex = findColumnIndex(headers, ERROR_CODE_COL_HEAD);
      int errorDetailsIndex = findColumnIndex(headers, ERROR_DETAILS_COL_HEAD);
      int possibleCausesIndex = findColumnIndex(headers, POSSIBLE_CAUSES_COL_HEAD);
      int machineIndex = findColumnIndex(headers, MACHINE_COL_HEAD);

      String currentErrorCode = null;
      String currentErrorDetails = null;
      String currentMachine = null;
      List<String> currentPossibleCauses = new ArrayList<>();

      // Process rows
      for (int i = 1; i < rows.size(); i++) {
        String[] row = rows.get(i);

        if (row.length < headers.length) {
          continue; // Ignore rows with insufficient columns
        }

        String errorCode = getValueAtIndex(row, errorCodeIndex);
        String errorDetails = getValueAtIndex(row, errorDetailsIndex);
        String possibleCause = getValueAtIndex(row, possibleCausesIndex);
        String machine = getValueAtIndex(row, machineIndex);

        // If the error code is populated, create a new MachineErrorCode instance
        if (!errorCode.isEmpty()) {
          // If there was a previous error code being built, add it to the list
          if (currentErrorCode != null) {
            machineErrorCodes.add(new MachineErrorCode(currentErrorCode, currentErrorDetails, currentPossibleCauses, currentMachine));
          }

          // Start a new MachineErrorCode instance
          currentErrorCode = errorCode;
          currentErrorDetails = errorDetails;
          currentMachine = machine;
          currentPossibleCauses = new ArrayList<>();
        }

        // Add the possible cause to the current MachineErrorCode instance
        if (!possibleCause.isEmpty()) {
          currentPossibleCauses.add(possibleCause);
        }
      }

      // Add the last MachineErrorCode instance if it exists
      if (currentErrorCode != null) {
        machineErrorCodes.add(new MachineErrorCode(currentErrorCode, currentErrorDetails, currentPossibleCauses, currentMachine));
      }

    } catch (IOException | CsvException e) {
      e.printStackTrace();
    }

    return machineErrorCodes;
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
