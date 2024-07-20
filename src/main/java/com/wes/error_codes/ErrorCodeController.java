package com.wes.error_codes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
public class ErrorCodeController {

  @Autowired Set<String> machinesFromCSV;

  @Autowired
  @Qualifier("errorCodesAndCauses")
  Map<String, List<String>> codesAndCausesFromCsv;

  @Autowired Map<String, String> errorDetailsMap;

  @Autowired
  @Qualifier("machinesAndErrorCodes")
  Map<String, List<String>> machinesAndErrorCodes;

  @GetMapping("")
  public String hello(Model model) {
    log.info("Homepage accessed");

    model.addAttribute("machines", machinesFromCSV);

    return "hello";
  }


  private List<String> getPossibleCauses(String errorCode) {
    return codesAndCausesFromCsv.get(errorCode);
  }

  @PostMapping("/select-machine")
  public String selectMachine(@RequestParam String machine, Model model) {
    log.info("Selected machine: " + machine);

    model.addAttribute("selectedMachine", machine);

    List<String> errors = machinesAndErrorCodes.get(machine);
    log.info(String.valueOf(errors));

    model.addAttribute("errors", errors);

    return "selected-machine";
  }

  @GetMapping("/select-error")
  public String selectError(@RequestParam String machine, @RequestParam String error, Model model) {
    model.addAttribute("selectedMachine", machine);
    model.addAttribute("selectedError", error);

    log.info("THE ERROR " + error);
    List<String> causes = getPossibleCauses(error);
    log.info("Causes for {}: {}", error, causes);
    log.info("errorDetails: " + errorDetailsMap.get(error));

    model.addAttribute("errorDetails", errorDetailsMap.get(error));
    model.addAttribute("causeDetails", causes);

    return "selected-error";
  }
}
