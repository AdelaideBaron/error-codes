package com.wes.error_codes.reader;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.wes.error_codes.model.Error;
import com.wes.error_codes.model.Machine;
import com.wes.error_codes.model.PossibleCause;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@Slf4j
public class ErrorCodeReader { // obvs rename
    // TODO do you want one csv per machine, or the same CSV and a column for machines?
    private String ERROR_CODES_FILE_PATH = "src/main/resources/data/machine_1_error_codes.csv";

    public List<Error> getErrors(){
        return readErrors();
    } // Todo can disable use of this, only used within tests so cleanup needed

    public static List<Error> errorsFromCSV;

    public static Map<Machine, List<Error>> machineErrors = new HashMap<>();

    @Bean
    public void readErrorsForAllMachines(){
        for(Machine machine : Machine.values()){
            machineErrors.put(machine, readErrors(machine.getFilePath()));
        }
    }

    public List<Error> readErrors(String filePath){ // refactor, can be void
        List<Error> errors = new ArrayList<>(); // Define errors list here
        List<List<String>> records = new ArrayList<List<String>>();
        try (CSVReader csvReader = new CSVReader(new FileReader(filePath));) {
            String[] row = null;
            StringBuffer errorCode = new StringBuffer("");
            List<PossibleCause> possibleCauseList = new ArrayList<>();
            boolean firstLine = true;
            while ((row = csvReader.readNext()) != null) {
                if(firstLine){
                    firstLine = false;
                } else {
                    if(row[0] != ""){
                        errorCode.append(row[0].replace(" ", ""));
                        possibleCauseList.add(new PossibleCause(row[1]));
                    } else if(row[0] == "" && row[1] != "" ){
                        possibleCauseList.add(new PossibleCause(row[1]));
                    } else if(row[0] == "" && row[1] == ""){
                        errors.add(new Error(errorCode.toString(), possibleCauseList));
                        errorCode.replace(0,errorCode.length(), "");
                        possibleCauseList.clear();
                    }
                }
            }
            errors.add(new Error(errorCode.toString(), possibleCauseList));

        } catch (IOException | CsvValidationException e) {
            throw new RuntimeException(e);
        }
        errorsFromCSV = errors;
        return errors;
    }



    // Todo refactor the below
    @Bean
    public List<Error> readErrors(){ // refactor, can be void
        List<Error> errors = new ArrayList<>(); // Define errors list here
        List<List<String>> records = new ArrayList<List<String>>();
        try (CSVReader csvReader = new CSVReader(new FileReader(ERROR_CODES_FILE_PATH));) {
            String[] row = null;
            StringBuffer errorCode = new StringBuffer("");
            List<PossibleCause> possibleCauseList = new ArrayList<>();
            boolean firstLine = true;
            while ((row = csvReader.readNext()) != null) {
                if(firstLine){
                    firstLine = false;
                } else {
                    if(row[0] != ""){
                        log.debug("reading new error: " + row[0]);
                        errorCode.append(row[0].replace(" ", ""));
                        possibleCauseList.add(new PossibleCause(row[1]));
                    } else if(row[0] == "" && row[1] != "" ){
                        possibleCauseList.add(new PossibleCause(row[1]));
                    } else if(row[0] == "" && row[1] == ""){
                        log.debug("creating error...");
//                        for(PossibleCause cause : possibleCauseList){
//                            log.info(cause.getCause());
//                        }
                        errors.add(new Error(errorCode.toString(), possibleCauseList));
                        log.debug("Clearing building error: " + errorCode);
                        errorCode.replace(0,errorCode.length(), "");
                        possibleCauseList.clear();
                        log.debug("error cleared: " + errorCode + " possible causes cleared: " +possibleCauseList);
                    }
                }
            }
            log.debug("creating error..."); // last one to be created after reading complete
//            for(PossibleCause cause : possibleCauseList){
//                log.info(cause.getCause());
//            }
//            errorCode.replace(0,errorCode.length(), "");
//            possibleCauseList.clear(); // hm is this it?
            errors.add(new Error(errorCode.toString(), possibleCauseList));

        } catch (IOException | CsvValidationException e) {
            throw new RuntimeException(e);
        }
        errorsFromCSV = errors;
        return errors;
    }
}
