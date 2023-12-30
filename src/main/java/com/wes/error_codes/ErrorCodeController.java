package com.wes.error_codes;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
public class ErrorCodeController {

    @Autowired
    private YamlReaderService yamlReaderService;
    @Autowired
    private CauseHandler causeHandler;

    @GetMapping("/hello")
    public String hello(Model model) {
        List<String> machines = yamlReaderService.readMachinesFromYaml();
        model.addAttribute("machines", machines);
        return "hello";
    }

    @PostMapping("/select-machine")
    public String selectMachine(@RequestParam String machine, Model model) {
        model.addAttribute("selectedMachine", machine);

        // Fetch the list of errors for the selected machine
        List<String> errors = yamlReaderService.readErrorCodesFromYaml(machine)
                .stream()
                .map(ErrorCode::name)
                .collect(Collectors.toList());

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
