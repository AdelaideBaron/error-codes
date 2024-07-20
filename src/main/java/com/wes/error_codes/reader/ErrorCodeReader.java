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
  private String MACHINE_ERROR_CODES = "src/main/resources/data/machine_error_codes.csv";

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

      String currentErrorCode = null;
      String currentErrorDetails = null;
      String currentMachine = null;
      List<String> currentPossibleCauses = new ArrayList<>();

      // Skip the header row (first row)
      for (int i = 1; i < rows.size(); i++) {
        String[] row = rows.get(i);

        if (row.length < 4) {
          continue; // Ignore rows with insufficient columns
        }

        String errorCode = row[0].trim();
        String errorDetails = row[1].trim();
        String possibleCause = row[2].trim();
        String machine = row[3].trim();

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
}
