package com.wes.error_codes.util;

import com.wes.error_codes.model.Error;
import com.wes.error_codes.model.PossibleCause;
import com.wes.error_codes.reader.ErrorCodeReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;

import java.util.List;
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

}
