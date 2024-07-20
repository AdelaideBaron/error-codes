package com.wes.error_codes;

import com.wes.error_codes.reader.ErrorCodeCSVReader;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
public class ErrorCodeController {

  @Autowired private ErrorCodeCSVReader errorCodeCSVReader;

  @GetMapping("")
  public String hello(Model model) {
    log.info("Homepage accessed");

    model.addAttribute("machines", errorCodeCSVReader.getMachinesFromCsv());

    return "hello";
  }

  @PostMapping("/select-machine")
  public String selectMachine(@RequestParam String machine, Model model) {
    log.info("Viewing selected machine: {}", machine);

    List<String> errors = errorCodeCSVReader.getMachinesWithErrorCodes().get(machine);

    model.addAttribute("selectedMachine", machine);
    model.addAttribute("errors", errors);

    return "selected-machine";
  }

  @GetMapping("/select-error")
  public String selectError(@RequestParam String machine, @RequestParam String error, Model model) {
    log.info("Viewing error {} for machine {}", error, machine);
    model.addAttribute("selectedMachine", machine);
    model.addAttribute("selectedError", error);

    List<String> causes = getPossibleCauses(error);

    model.addAttribute("errorDetails", errorCodeCSVReader.getErrorsWithDetails().get(error));
    model.addAttribute("causeDetails", causes);

    return "selected-error";
  }

  private List<String> getPossibleCauses(String errorCode) {
    return errorCodeCSVReader.getErrorCodeAndCausesFromCsv().get(errorCode);
  }
}
