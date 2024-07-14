package com.wes.error_codes;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Controller
public class ErrorCodeController {

    @Autowired
    private YamlReaderService yamlReaderService;
    @Autowired
    private CauseHandler causeHandler;

    @Autowired
    Set<String> machinesFromCSV;

    @Autowired
    Map<String, List<String>>  codesAndCausesFromCsv;

    @GetMapping("/hello")
    public String hello(Model model) {
        log.info("Homepage accessed");
//        List<String> machines = yamlReaderService.readMachinesFromYaml();
//        List<String> machines = (List<String>) machinesFromCSV;//  yamlReaderService.readMachinesFromYaml();
//        Set<String>
        model.addAttribute("machines", machinesFromCSV);

//        log.warn(codesAndCausesFromCsv.);

//        for (Map.Entry<String, List<String>> entry : codesAndCausesFromCsv.entrySet()) {
//            System.out.println("Error: " + entry.getKey());
//            System.out.println("Causes: " + entry.getValue());
//            System.out.println();
//        }

        return "hello"; // to be removed
    }

    private List<String> getErrorCodes() {
        List<String> errorsCodes = new ArrayList<>();
        for (Map.Entry<String, List<String>> entry : codesAndCausesFromCsv.entrySet()) {
            errorsCodes.add(entry.getKey());
        }
        return errorsCodes;
    }

    @PostMapping("/select-machine")
    public String selectMachine(@RequestParam String machine, Model model) {
        model.addAttribute("selectedMachine", machine);

        // Fetch the list of errors for the selected machine

        // get the errors from ErrorCodeConfiguration, then display the error.getErrorCode for each
        // for error in ErrorCodeConfig.getErrors():
        // add their names (.getName) to a list

        // old way below
        List<String> errors = getErrorCodes();
//        List<String> errors = yamlReaderService.readErrorCodesFromYaml(machine)
//                .stream()
//                .map(ErrorCode::name)
//                .collect(Collectors.toList());

        model.addAttribute("errors", errors);

        return "selected-machine";
    }

    @GetMapping("/select-error")
    public String selectError(@RequestParam String machine, @RequestParam String error, Model model) {
        model.addAttribute("selectedMachine", machine);
        model.addAttribute("selectedError", error);

        List<String> causes = yamlReaderService.getCauseByErrorFromConfig(error);
        log.info("Causes for {}: {}", error, causes);

        List<Cause> causeDetails = causeHandler.findCausesByError(causes);

        model.addAttribute("errorDetails", ErrorCode.fromString(error).getDescription());
        model.addAttribute("causeDetails", causeDetails);

        return "selected-error";
    }
}
