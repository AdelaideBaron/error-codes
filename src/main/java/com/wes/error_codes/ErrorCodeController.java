package com.wes.error_codes;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

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

    @PostMapping("/select-machine")
    public String selectMachine(@RequestParam String machine, Model model) {
        List<ErrorCode> errorCodes = yamlReaderService.readErrorCodesFromYaml(machine);
        model.addAttribute("selectedMachine", machine);
        model.addAttribute("errorCodes", errorCodes);
        return "selected-machine";
    }
}
