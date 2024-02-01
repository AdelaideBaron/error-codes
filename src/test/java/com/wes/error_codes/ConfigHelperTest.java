package com.wes.error_codes;

import com.wes.error_codes.model.Error;
import com.wes.error_codes.reader.ErrorCodeReader;
import com.wes.error_codes.util.ConfigHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

public class ConfigHelperTest {

    private final String TEST_ERROR_CODES_FILE_PATH = "src/test/resources/error_codes_test_file.csv";


    @InjectMocks
    private ErrorCodeReader errorCodeReader;


    public List<Error> errorsFromCsv;

    @BeforeEach
    public void setup(){
        errorCodeReader = new ErrorCodeReader();
        ReflectionTestUtils.setField(errorCodeReader, "ERROR_CODES_FILE_PATH", TEST_ERROR_CODES_FILE_PATH);
        errorsFromCsv = errorCodeReader.getErrors();
    }

    @Test
    @DisplayName("Config Helper extracts names from error codes read from config")
    void configHelperExtractsNamesFromErrorCodesReadFromConfig() {
        ConfigHelper configHelper = new ConfigHelper();
        configHelper.getErrorNamesFromConfig();
        List<String> expected = Arrays.asList("TEST_ERROR", "TEST_ERROR_2", "TEST_ERROR_3");
        assertArrayEquals(new List[]{expected}, new List[]{ConfigHelper.errorCodes});
    }

}
