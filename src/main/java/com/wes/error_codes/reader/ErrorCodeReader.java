package com.wes.error_codes.reader;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;
import com.wes.error_codes.model.Error;
import com.wes.error_codes.model.PossibleCause;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

@Configuration
@Slf4j
public class ErrorCodeReader { // obvs rename
    private String MACHINE_ERROR_CODES = "src/main/resources/data/machine_error_codes.csv";

    @Bean
    public Set<String> getMachinesFromCsv(){
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
    public Map<String, List<String>> getErrorCodeAndCausesFromCsv(){
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
    public Map<String, List<String>> mapErrorCodesToMachines() {
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
    public Map<String, String> getErrorToDetailsMap() {
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

    public static List<Error> errorsFromCSV;

    public List<Error> readErrors(String filePath){
        List<Error> errors = new ArrayList<>();
        try (CSVReader csvReader = new CSVReader(new FileReader(filePath));) {
            String[] row = null;
            StringBuffer errorCode = new StringBuffer("");
            List<PossibleCause> possibleCauseList = new ArrayList<>();
            boolean firstLine = true;
            while ((row = csvReader.readNext()) != null) {
                if(firstLine){
                    firstLine = false;
                } else {
                    if(!row[0].isEmpty()){
                        errorCode.append(row[0].replace(" ", ""));
                        possibleCauseList.add(new PossibleCause(row[1]));
                    } else if(row[0].isEmpty() && !row[1].isEmpty()){
                        possibleCauseList.add(new PossibleCause(row[1]));
                    } else if(row[0].isEmpty() && row[1].isEmpty()){
                        errors.add(new Error(errorCode.toString(), new ArrayList<>(possibleCauseList)));
                        errorCode.setLength(0);
                        possibleCauseList.clear();
                    }
                }
            }
            if(errorCode.length() > 0){
                errors.add(new Error(errorCode.toString(), new ArrayList<>(possibleCauseList)));
            }

        } catch (IOException | CsvValidationException e) {
            throw new RuntimeException(e);
        }
        errorsFromCSV = errors;
        return errors;
    }
}
