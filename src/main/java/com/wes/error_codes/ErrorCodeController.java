package com.wes.error_codes;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

// Add this import statement
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
public class ErrorCodeController {

    @Autowired
    private YamlReaderService yamlReaderService;

    @GetMapping("/hello")
    public String hello(Model model) {
        List<String> machines = yamlReaderService.readMachinesFromYaml();
        model.addAttribute("machines", machines);
        return "hello";
    }

//    @PostMapping("/select-machine")
//    public String selectMachine(@RequestParam String machine, Model model) {
//        List<ErrorCode> errorCodes = yamlReaderService.readErrorCodesFromYaml(machine);
//        model.addAttribute("selectedMachine", machine);
//        model.addAttribute("errorCodes", errorCodes);
//        return "selected-machine";
//    }

    // Update the selectMachine method in ErrorCodeController
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

    // Add a new method to handle selecting an error
    @GetMapping("/select-error")
    public String selectError(@RequestParam String machine, @RequestParam String error, Model model) {
        model.addAttribute("selectedMachine", machine);
        model.addAttribute("selectedError", error);

        log.info(String.valueOf(yamlReaderService.findCausesByError(error)));

        // Fetch details and solutions for the selected error
        // get the error from string
        // find the description of it - just from it's class
        // then read the possible solutions from the config file too?


        // You can use a similar approach as before to read from YAML and populate the model attributes

        return "selected-error";
    }

}
