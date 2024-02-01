package com.wes.error_codes;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.wes.error_codes.model.Error;
import com.wes.error_codes.model.Machine;
import com.wes.error_codes.model.PossibleCause;
import com.wes.error_codes.reader.ErrorCodeReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class ErrorCodeReaderTest {

    private final String TEST_ERROR_CODES_FILE_PATH = "src/test/resources/error_codes_test_file.csv";


    @InjectMocks
    private ErrorCodeReader errorCodeReader;

    public List<Error> errorsFromCsv;

    @BeforeEach
    public void setup(){
        errorCodeReader = new ErrorCodeReader();
        ReflectionTestUtils.setField(errorCodeReader, "ERROR_CODES_FILE_PATH", TEST_ERROR_CODES_FILE_PATH);
        errorsFromCsv = errorCodeReader.getErrors();
        errorCodeReader.readErrorsForAllMachines();
    }

    @Test
    @DisplayName("Error code config creates all errors from csv")
    void errorCodeConfigCreatesAllErrorsFromCsv() {
        assertEquals(3,errorsFromCsv.size());
        for(Error error : errorsFromCsv){
            if(error.getErrorCode().equals("TEST_ERROR")){
                assertEquals(1,error.getPossibleCauses().size());
                PossibleCause expectedPossibleCause = new PossibleCause("some_cause");
                error.getPossibleCauses().contains(expectedPossibleCause);
            } else if (error.getErrorCode().equals("TEST_ERROR_2")){
                assertEquals(3,error.getPossibleCauses().size());
                PossibleCause expectedPossibleCause3 = new PossibleCause("another_cause_three");
                PossibleCause expectedPossibleCause = new PossibleCause("another_cause");
                PossibleCause expectedPossibleCause2 = new PossibleCause("another_cause_two");
                error.getPossibleCauses().contains(expectedPossibleCause);
                error.getPossibleCauses().contains(expectedPossibleCause2);
                error.getPossibleCauses().contains(expectedPossibleCause3);
            }else if (error.getErrorCode().equals("TEST_ERROR_3")){
                assertEquals(3,error.getPossibleCauses().size());
                PossibleCause expectedPossibleCause3 = new PossibleCause("another_cause_one");
                PossibleCause expectedPossibleCause = new PossibleCause("another_cause_five");
                PossibleCause expectedPossibleCause2 = new PossibleCause("another_cause_four");
                error.getPossibleCauses().contains(expectedPossibleCause);
                error.getPossibleCauses().contains(expectedPossibleCause2);
                error.getPossibleCauses().contains(expectedPossibleCause3);
            }
        }
    }

    @Test
    @DisplayName("Error code config sets errors from csv")
    void errorCodeConfigSetsErrorsFromCsv() {
        assertEquals(3,ErrorCodeReader.errorsFromCSV.size());
    }

    @Test
    @DisplayName("Reads each machine's config")
    void readsEachMachineSConfig() {
        assertEquals(Machine.values().length, errorCodeReader.machineErrors.size());
    }

    @Test
    @DisplayName("Gets expected amount of errors")
    void getsExpectedAmountOfErrors() {
        Machine randomMachine = Machine.values()[0]; // pick random machine
        int expected = getAmountOfErrorsFromCsv(randomMachine.getFilePath());

        int actual = errorCodeReader.machineErrors.get(randomMachine).size();

        assertEquals(expected, actual, "Machine checked %s".formatted(randomMachine));
    }

//    @Test
//    @DisplayName("Gets expected amount of possible causes")
//    void getsExpectedAmountOfPossibleCauses() {
//        Machine randomMachine = Machine.values()[0]; // pick random machine
//        getRandomErrorFromCSV(randomMachine.getFilePath());
//
//    }
//
//    private String getRandomErrorFromCSV(String errorCsvPath){
//// get a random error from the csv (column 0, ignoring the header)
//        // count not null entries in other column, i.e. how many possible causes there are
//    }


    private int getAmountOfErrorsFromCsv(String errorCsvPath){
        int notNullCount = 0;
        try (CSVReader reader = new CSVReader(new FileReader(errorCsvPath))) {
            String[] line;
            while ((line = reader.readNext()) != null) {
                if (line.length > 0 && line[0] != null && !line[0].isEmpty()) {
                    notNullCount++;
                }
            }
            notNullCount--;
        } catch (IOException | CsvValidationException e) {
            throw new RuntimeException(e);
        }
        return notNullCount;
    }


}
