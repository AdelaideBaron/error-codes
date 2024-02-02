package com.wes.error_codes;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import org.springframework.ui.Model;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ErrorCodeControllerTest {
    @Autowired
    private MockMvc mockMvc;

//    @Test
//    public void testHello() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.get("/hello"))
////                .andExpect(MockMvcResultMatchers.status().isOk())
////                .andExpect(MockMvcResultMatchers.model().attributeExists("machines"))
//                .andExpect(MockMvcResultMatchers.view().name("hello"));
//    }

    @Test
    public void testSelectMachine() throws Exception {
        String machine = "MACHINE_1";
        Model model = mock(Model.class);

        mockMvc.perform(post("/select-machine")
                        .param("machine", machine))
                .andExpect(status().isOk())
                .andExpect(view().name("selected-machine"));

        verify(model, times(1)).addAttribute("selectedMachine", machine);
    }
}
