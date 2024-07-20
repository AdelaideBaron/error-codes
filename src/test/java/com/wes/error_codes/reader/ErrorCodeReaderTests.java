package com.wes.error_codes.reader;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.wes.error_codes.TestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class ErrorCodeReaderTests {

  private TestData testData = new TestData();

  @InjectMocks private ErrorCodeCSVReader errorCodeReader;

  @BeforeEach
  public void setup() {
    errorCodeReader = new ErrorCodeCSVReader();
    ReflectionTestUtils.setField(
        errorCodeReader, "MACHINE_ERROR_CODES", testData.TEST_ERROR_CODES_FILE_PATH);
  }

  @Test
  public void getMachinesFromCsv_populatedCsv_returnsAllMachines() {
    assertEquals(
        testData.TEST_ERROR_CODES.getAllUniqueMachines(), errorCodeReader.getMachinesFromCsv());
  }

  @Test
  public void getErrorCodeAndCausesFromCsv_populatedCsv_returnsCodesAndPotentialCauses() {
    assertEquals(
        testData.TEST_ERROR_CODES.getAllErrorCodesWithCauses(),
        errorCodeReader.getErrorCodeAndCausesFromCsv());
  }

  @Test
  public void mapErrorCodesToMachines_populatedCsv_returnsMachinesWithErrorCodes() {
    assertThat(testData.TEST_ERROR_CODES.getAllErrorCodesForEachMachine())
        .isEqualTo(errorCodeReader.getMachinesWithErrorCodes());
  }

  @Test
  public void getErrorToDetailsMap_populatedCsv_returnsErrorsWithDetails() {
    assertThat(testData.TEST_ERROR_CODES.getDetailsForAllErrorCodes())
        .isEqualTo(errorCodeReader.getErrorsWithDetails());
  }

}
