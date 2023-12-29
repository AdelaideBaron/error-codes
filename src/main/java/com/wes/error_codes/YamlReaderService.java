package com.wes.error_codes;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class YamlReaderService {

    @Value("classpath:echo-config.yml")
    private Resource echoConfigResource;

    public List<String> readMachinesFromYaml() {
        try (InputStream inputStream = echoConfigResource.getInputStream()) {
            Yaml yaml = new Yaml();
            Map<String, List<Map<String, Object>>> yamlData = yaml.load(inputStream);

            List<String> machines = new ArrayList<>();
            if (yamlData != null && yamlData.containsKey("echo")) {
                yamlData.get("echo").forEach(entry -> {
                    if (entry.containsKey("machine")) {
                        machines.add((String) entry.get("machine"));
                    }
                });
            }
            return machines;
        } catch (IOException e) {
            throw new RuntimeException("Failed to read machines from YAML file", e);
        }
    }

    public List<ErrorCode> readErrorCodesFromYaml(String machine) {
        try (InputStream inputStream = echoConfigResource.getInputStream()) {
            Yaml yaml = new Yaml();
            Map<String, List<Map<String, Object>>> yamlData = yaml.load(inputStream);

            List<ErrorCode> errorCodes = new ArrayList<>();
            if (yamlData != null && yamlData.containsKey("echo")) {
                yamlData.get("echo").forEach(entry -> {
                    if (entry.containsKey("machine") && entry.containsKey("errorCodes")) {
                        String currentMachine = (String) entry.get("machine");
                        if (machine.equals(currentMachine)) {
                            List<String> codes = (List<String>) entry.get("errorCodes");
                            errorCodes.addAll(convertToErrorCodeEnum(codes));
                        }
                    }
                });
            }
            return errorCodes;
        } catch (IOException e) {
            throw new RuntimeException("Failed to read error codes from YAML file", e);
        }
    }


    private List<ErrorCode> convertToErrorCodeEnum(List<String> errorCodes) {
        List<ErrorCode> result = new ArrayList<>();
        for (String code : errorCodes) {
            result.add(ErrorCode.valueOf(code));
        }
        return result;
    }



    @Value("classpath:error-causes.yml") // Assuming you've renamed your YAML file
    private Resource errorsConfigResource;

    public List<String> readErrorsFromYaml() {
        try (InputStream inputStream = errorsConfigResource.getInputStream()) {
            Yaml yaml = new Yaml();
            Map<String, List<Map<String, Object>>> yamlData = yaml.load(inputStream);

            List<String> errors = new ArrayList<>();
            if (yamlData != null && yamlData.containsKey("errors")) {
                yamlData.get("errors").forEach(entry -> {
                    if (entry.containsKey("code")) {
                        errors.add((String) entry.get("code"));
                    }
                });
            }
            return errors;
        } catch (IOException e) {
            throw new RuntimeException("Failed to read errors from YAML file", e);
        }
    }

    public List<Cause> findCausesByError(List<String> causes){
        List<Cause> causesToReturn = new ArrayList<>();

        for(String cause : causes){
            causesToReturn.add(Cause.fromString(cause));
        }

        return causesToReturn;
    }

    public List<String> getCauseByErrorFromConfig(String errorCode) {
        try (InputStream inputStream = errorsConfigResource.getInputStream()) {
            Yaml yaml = new Yaml();
            Map<String, List<Map<String, Object>>> yamlData = yaml.load(inputStream);

            List<String> causes = new ArrayList<>();
            if (yamlData != null && yamlData.containsKey("errors")) {
                yamlData.get("errors").forEach(entry -> {
                    if (entry.containsKey("code") && entry.containsKey("causes")) {
                        String currentCode = (String) entry.get("code");
                        if (errorCode.equals(currentCode)) {
                            List<String> currentCauses = (List<String>) entry.get("causes");
                            causes.addAll(currentCauses);
                        }
                    }
                });
            }
            return causes;
        } catch (IOException e) {
            throw new RuntimeException("Failed to find causes from YAML file", e);
        }
    }
}
