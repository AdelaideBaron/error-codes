package com.wes.error_codes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class ErrorCodeControllerTests {

    @Mock
    private YamlReaderService yamlReaderService;

    @Mock
    private CauseHandler causeHandler;

    @InjectMocks
    private ErrorCodeController errorCodeController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(errorCodeController).build();
    }

    @Test
    public void testHello() throws Exception {
        mockMvc.perform(get("/hello"))
                .andExpect(status().isOk());
//                .andExpect(view().name("hello"));
    }

    @Test
    public void testSelectMachine() throws Exception {
        String machine = "Machine1";
        when(yamlReaderService.readMachinesFromYaml()).thenReturn(Arrays.asList(machine));

        mockMvc.perform(post("/select-machine")
                        .param("machine", machine))
                .andExpect(status().isOk())
                .andExpect(view().name("selected-machine"))
                .andExpect(model().attribute("selectedMachine", machine));
    }

    @Test
    public void testSelectError() throws Exception {
        String machine = "Machine1";
        String error = "ERROR_CODE_1";


        when(yamlReaderService.getCauseByErrorFromConfig(error)).thenReturn(Arrays.asList("Cause1", "Cause2"));
        when(causeHandler.findCausesByError(Arrays.asList("Cause1", "Cause2")))
                .thenReturn(Arrays.asList(Cause.CAUSE_1, Cause.CAUSE_2));

        mockMvc.perform(get("/select-error")
                        .param("machine", machine)
                        .param("error", error))
                .andExpect(status().isOk())
                .andExpect(view().name("selected-error"))
                .andExpect(model().attribute("selectedMachine", machine))
                .andExpect(model().attribute("selectedError", error))
                .andExpect(model().attribute("causeDetails", Arrays.asList(Cause.CAUSE_1, Cause.CAUSE_2)));
    }

}
