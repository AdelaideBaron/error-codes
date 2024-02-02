package com.wes.error_codes.util;

import com.wes.error_codes.model.Error;
import com.wes.error_codes.model.Machine;
import com.wes.error_codes.model.PossibleCause;
import com.wes.error_codes.reader.ErrorCodeReader;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class ConfigHelper { // parser? look at naming conventions for this...

    public static List<Error> getErrorsForMachine(Machine machine){
        return ErrorCodeReader.machineErrors.get(machine);
    }

    public static List<String> getErrorCodesForMachine(Machine machine){
        return ErrorCodeReader.machineErrors.get(machine).stream()
                .map(Error::getErrorCode)
                .toList();
    }

    public static List<PossibleCause> getPossibleCausesForError(Error errorToFind, Machine machine){
        List<Error> errors =ErrorCodeReader.machineErrors.get(machine);

        Optional<Error> foundError = errors.stream()
                .filter(error -> error.getErrorCode().equals(errorToFind))
                .findFirst();

        if (foundError.isPresent()) {
            Error error = foundError.get();
            return error.getPossibleCauses();
        } else {
            log.error("Error not found: " + errorToFind);
        }
        return null;
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
