package com.wes.error_codes;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class CauseHandler {
    public List<Cause> findCausesByError(List<String> causes){
        List<Cause> causesToReturn = new ArrayList<>();

        for(String cause : causes){
            causesToReturn.add(Cause.fromString(cause));
        }

        return causesToReturn;
    }
}
