package com.wes.error_codes;

public class ErrorCodeReaderTest {

  //    private final String TEST_ERROR_CODES_FILE_PATH =
  // "src/test/resources/error_codes_test_file.csv";
  //
  //
  //    @InjectMocks
  //    private ErrorCodeReader errorCodeReader;
  //
  //    public List<Error> errorsFromCsv;
  //
  //    @BeforeEach
  //    public void setup() {
  //        errorCodeReader = new ErrorCodeReader();
  //        ReflectionTestUtils.setField(errorCodeReader, "ERROR_CODES_FILE_PATH",
  // TEST_ERROR_CODES_FILE_PATH);
  //        errorsFromCsv = errorCodeReader.getErrors();
  //        errorCodeReader.readErrorsForAllMachines();
  //    }
  //
  //    @Test
  //    @DisplayName("Error code config creates all errors from csv")
  //    void errorCodeConfigCreatesAllErrorsFromCsv() {
  //        assertEquals(3, errorsFromCsv.size());
  //        for (Error error : errorsFromCsv) {
  //            if (error.getErrorCode().equals("TEST_ERROR")) {
  //                assertEquals(1, error.getPossibleCauses().size());
  //                PossibleCause expectedPossibleCause = new PossibleCause("some_cause");
  //                error.getPossibleCauses().contains(expectedPossibleCause);
  //            } else if (error.getErrorCode().equals("TEST_ERROR_2")) {
  //                assertEquals(3, error.getPossibleCauses().size());
  //                PossibleCause expectedPossibleCause3 = new PossibleCause("another_cause_three");
  //                PossibleCause expectedPossibleCause = new PossibleCause("another_cause");
  //                PossibleCause expectedPossibleCause2 = new PossibleCause("another_cause_two");
  //                error.getPossibleCauses().contains(expectedPossibleCause);
  //                error.getPossibleCauses().contains(expectedPossibleCause2);
  //                error.getPossibleCauses().contains(expectedPossibleCause3);
  //            } else if (error.getErrorCode().equals("TEST_ERROR_3")) {
  //                assertEquals(3, error.getPossibleCauses().size());
  //                PossibleCause expectedPossibleCause3 = new PossibleCause("another_cause_one");
  //                PossibleCause expectedPossibleCause = new PossibleCause("another_cause_five");
  //                PossibleCause expectedPossibleCause2 = new PossibleCause("another_cause_four");
  //                error.getPossibleCauses().contains(expectedPossibleCause);
  //                error.getPossibleCauses().contains(expectedPossibleCause2);
  //                error.getPossibleCauses().contains(expectedPossibleCause3);
  //            }
  //        }
  //    }
  //
  //    @Test
  //    @DisplayName("Error code config sets errors from csv")
  //    void errorCodeConfigSetsErrorsFromCsv() {
  //        assertEquals(3, ErrorCodeReader.errorsFromCSV.size());
  //    }
  //
  //    @Test
  //    @DisplayName("Reads each machine's config")
  //    void readsEachMachineSConfig() {
  //        assertEquals(Machine.values().length, errorCodeReader.machineErrors.size());
  //    }
  //
  //    @Test
  //    @DisplayName("Gets expected amount of errors")
  //    void getsExpectedAmountOfErrors() {
  //        Machine randomMachine = Machine.values()[0]; // pick random machine
  //        int expected = getAmountOfErrorsFromCsv(randomMachine.getFilePath());
  //
  //        int actual = errorCodeReader.machineErrors.get(randomMachine).size();
  //
  //        assertEquals(expected, actual, "Machine checked %s".formatted(randomMachine));
  //    }
  //
  //    @Test
  //    @DisplayName("Gets expected amount of possible causes")
  //    void getsExpectedAmountOfPossibleCauses() {
  //        Machine randomMachine = Machine.values()[0]; // pick random machine
  //        String errorCode = getRandomErrorFromCsv(randomMachine.getFilePath());
  //        int expectedPossibleCauseCount = countPossibleCausesForErrorFromCsv(errorCode,
  // randomMachine.getFilePath());
  //
  //        Optional<Error> targetError = errorCodeReader.machineErrors.get(randomMachine).stream()
  //                .filter(error -> error.getErrorCode().equals(errorCode))
  //                .findFirst();
  //
  //        if (targetError.isPresent()) {
  //            System.out.println("Found error: " + targetError.get().getErrorCode());
  //            Error actualError = targetError.get();
  //            assertEquals(errorCode, actualError.getErrorCode());
  //            assertEquals(expectedPossibleCauseCount, actualError.getPossibleCauses().size(),
  // actualError.getErrorCode());
  //        } else {
  //            System.out.println("Expected error: %s not found".formatted(errorCode));
  //            System.out.println(errorCodeReader.machineErrors.get(randomMachine));
  //        }
  //    }
  //
  //    private String getRandomErrorFromCsv(String errorCsvPath){ // this never picks the middle
  // one??
  //        List<String> lines = new ArrayList<>();
  //        try (CSVReader reader = new CSVReader(new FileReader(errorCsvPath))) {
  //            reader.skip(1);  // Skip the header
  //            String[] line;
  //            while ((line = reader.readNext()) != null) {
  //                if (line.length > 0 && line[0] != null && !line[0].isEmpty()) {
  //                    lines.add(line[0]);
  //                }
  //            }
  //        } catch (IOException | CsvException e) {
  //            e.printStackTrace();
  //        }
  //
  //        Random random = new Random();
  //        String randomLine = lines.get(random.nextInt(lines.size()));
  //        return randomLine.split(",")[0];
  //    }
  //
  //    private int countPossibleCausesForErrorFromCsv(String errorCode, String errorCsvPath){
  //        CSVReader reader = null;
  //        int count = 0;
  //        try {
  //            reader = new CSVReader(new FileReader(errorCsvPath));
  //            String[] line;
  //            boolean startCounting = false;
  //            while ((line = reader.readNext()) != null) {
  //                if (line[0].equals(errorCode)) {
  //                    startCounting = true;
  //                }
  //                if (startCounting && line[1].equals("")) {
  //                    break;
  //                }
  //                if (startCounting) {
  //                    count++;
  //                }
  //            }
  //        } catch (IOException e) {
  //            e.printStackTrace();
  //        } catch (CsvValidationException e) {
  //            throw new RuntimeException(e);
  //        } finally {
  //            if (reader != null) {
  //                try {
  //                    reader.close();
  //                } catch (IOException e) {
  //                    e.printStackTrace();
  //                }
  //            }
  //        }
  //        return count;
  //    }
  //
  //    private int getAmountOfErrorsFromCsv(String errorCsvPath){
  //        int notNullCount = 0;
  //        try (CSVReader reader = new CSVReader(new FileReader(errorCsvPath))) {
  //            String[] line;
  //            while ((line = reader.readNext()) != null) {
  //                if (line.length > 0 && line[0] != null && !line[0].isEmpty()) {
  //                    notNullCount++;
  //                }
  //            }
  //            notNullCount--;
  //        } catch (IOException | CsvValidationException e) {
  //            throw new RuntimeException(e);
  //        }
  //        return notNullCount;
  //    }

}
