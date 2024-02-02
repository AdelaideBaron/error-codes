package com.wes.error_codes.util;

import com.wes.error_codes.model.Error;
import com.wes.error_codes.model.Machine;
import com.wes.error_codes.model.PossibleCause;
import com.wes.error_codes.reader.ErrorCodeReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class ConfigHelper { // parser? look at naming conventions for this...

    public static List<String> errorCodes;
    public static List<PossibleCause> possibleCauses;

    @Bean
    public List<String> getErrorNamesFromConfig(){
        List<String> errors = ErrorCodeReader.errorsFromCSV
                .stream()
                .map(Error::getErrorCode)
                .toList();
        errorCodes = errors;
        return errors;
    }

    public static List<String> getMachineNamesAsList() {
        Set<Machine> machineSet =ErrorCodeReader.machineErrors.keySet();
        List<Machine> machineList = new ArrayList<>(machineSet);
        List<String> machines = machineList.stream()
                .map(Enum::name)
                .collect(Collectors.toList());
        return machines;
    }

}
